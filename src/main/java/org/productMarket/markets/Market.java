package org.productMarket.markets;

import org.productMarket.cashiers.Cashier;
import org.productMarket.counters.CashDesk;
import org.productMarket.customers.Customer;
import org.productMarket.receipts.Receipt;
import org.productMarket.enums.ProductCategory;
import org.productMarket.exceptions.*;
import org.productMarket.products.Product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Market implements Serializable {

    private double edibleProductsMarkup;
    private double nonEdibleProductsMarkup;
    private double discount;
    private int daysLeftTillExpiry;
    private Set<Cashier> cashiers;
    private Set<CashDesk> cashDesks;
    private BigDecimal expenses;
    private Map<Product, Integer> productsInStock;
    private List<Receipt> receipts;
    private int countReceipts;
    private BigDecimal income;

    public Market(double edibleProductsMarkup, double nonEdibleProductsMarkup, int daysLeftTillExpiry, double discount) throws NegativeProductMarkup, NegativeDays, NegativeDiscountValue {
        setEdibleProductsMarkup(edibleProductsMarkup);
        setNonEdibleProductsMarkup(nonEdibleProductsMarkup);
        setDaysLeftTillExpiry(daysLeftTillExpiry);
        setDiscount(discount);
        this.cashiers = new HashSet<>();
        this.cashDesks = new HashSet<>();
        this.productsInStock = new HashMap<>();
        this.receipts = new ArrayList<>();
        this.income = new BigDecimal(0);
        this.expenses = new BigDecimal(0);
    }

    public void addDeliveredProduct(Product product, Integer quantity) throws InvalidQuantityOfProduct, NegativeSellingPrice, NonSellableExpiredProduct {
        if (quantity <= 0) {
            throw new InvalidQuantityOfProduct("Quantity must be a positive number!");
        }

        // define selling price on product
        calculateSellingPrice(product);

        // check whether the product is for discount before adding it and add discount
        calculateDiscount(product);

        // add product in the market
        this.productsInStock.putIfAbsent(product, 0);
        this.productsInStock.put(product, this.productsInStock.get(product) + quantity);

        // calculate expenses from the delivery
        this.expenses = this.expenses.add(product.getDeliveryPrice().multiply(BigDecimal.valueOf(quantity)));
    }

    private void calculateDiscount(Product product) throws NonSellableExpiredProduct, NegativeSellingPrice {
        if (!product.isExpired() && isForDiscount(product)) {
            product.setSellingPrice(product.getSellingPrice().subtract(product.getSellingPrice().multiply(BigDecimal.valueOf(this.discount)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
        }
    }

    private void calculateSellingPrice(Product product) throws NegativeSellingPrice {
        if (product.getType().equals(ProductCategory.EDIBLE)) {
            product.setSellingPrice(product.getDeliveryPrice().multiply(BigDecimal.valueOf(this.edibleProductsMarkup)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP).add(product.getDeliveryPrice()));
        } else if (product.getType().equals(ProductCategory.NON_EDIBLE)) {
            product.setSellingPrice(product.getDeliveryPrice().multiply(BigDecimal.valueOf(this.nonEdibleProductsMarkup)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP).add(product.getDeliveryPrice()));
        }
    }

    private void decreaseProductQuantity(Product product, Integer requiredQuantity) {
        int availableQuantity = this.productsInStock.get(product);
        if (availableQuantity - requiredQuantity == 0) {
            this.productsInStock.remove(product);
        } else {
            this.productsInStock.put(product, availableQuantity - requiredQuantity);
        }
    }

    public Receipt sellProducts(Map<String, Integer> shoppingList, CashDesk cashDesk, Customer customer) {
        // create map for the sold products
        Map<Product, Integer> soldProducts = new HashMap<>();

        // iterate shoppingList and productsInStock to find the same products
        for (Map.Entry<String, Integer> shoppingListProduct : shoppingList.entrySet()) {

            String productToBuy = shoppingListProduct.getKey();
            int requiredQuantity = shoppingListProduct.getValue();
            for (Map.Entry<Product, Integer> product : this.productsInStock.entrySet()) {

                Product stockProduct = product.getKey();
                // get the same product name from the productsInStock as in shoppingList
                if (stockProduct.getName().equals(productToBuy)) {
                    if (customer.canBuyProduct(stockProduct, requiredQuantity)) {
                        // customer has enough money, buys the product and his money are decreased
                        customer.decreaseMoney(stockProduct, requiredQuantity);
                        soldProducts.put(stockProduct, requiredQuantity);
                    } else {
                        // customer does not have enough to buy current product
                        shoppingList.remove(productToBuy);
                    }
                }
            }
        }

        // current receipt from cash desk
        Receipt currentReceipt = cashDesk.generateReceipt(soldProducts);

        // add receipt to the total amount of receipts
        this.addReceipt(currentReceipt);
        this.countReceipts++;

        // update shoppingList in market
        soldProducts.forEach(this::decreaseProductQuantity);

        // calculate income from the sold shoppingList and add it to the total income
        this.setIncome(getIncome().add(calculateCurrentIncome(soldProducts)));

        return currentReceipt;
    }

    public Map<String, Integer> checkProductsAvailability(Map<String, Integer> shoppingList) {
        Map<String, Integer> availableProducts = new HashMap<>();

        for (Map.Entry<String, Integer> productEntry : shoppingList.entrySet()) {
            try {
                if (isAvailable(productEntry.getKey(), productEntry.getValue())) {
                    availableProducts.put(productEntry.getKey(), productEntry.getValue());
                }
            } catch (InsufficientQuantityOfProduct e) {
                System.out.println(e.getMessage());
            }
        }

        if (availableProducts.size() == 0) {
            System.out.println("No products from the shopping list were able to be sold from the market!");
            return null;
        }

        return availableProducts;
    }

    private boolean isForDiscount(Product product) {
        return ChronoUnit.DAYS.between(LocalDate.now(), product.getDateOfExpiry()) < this.daysLeftTillExpiry;
    }

    private boolean isAvailable(String product, int quantity) throws InsufficientQuantityOfProduct {
        for (Map.Entry<Product, Integer> productEntry : productsInStock.entrySet()) {
            if (productEntry.getKey().getName().equals(product) && productEntry.getValue() >= quantity) {
                // the market has the product and the quantity is enough
                return true;
            } else if (productEntry.getKey().getName().equals(product) && productEntry.getValue() < quantity) {
                // the market has the product but the quantity is not enough
                int insufficientQuantity = quantity - productEntry.getValue();
                throw new InsufficientQuantityOfProduct(String.format("Insufficient quantity from the selected product: \nProduct: %s\nInsufficient quantity: %d\n", productEntry.getKey().getName(), insufficientQuantity));
            }
        }

        // return false if the product does not exist on the market
        return false;
    }

    public void addCashier(Cashier cashier) {
        this.cashiers.add(cashier);
        this.expenses = this.expenses.add(cashier.getSalary());
    }

    public void addCashDesk(CashDesk cashDesk) {
        this.cashDesks.add(cashDesk);
    }

    public void addReceipt(Receipt receipt) {
        this.receipts.add(receipt);
    }

    public BigDecimal calculateProfit() {
        return income.subtract(expenses);
    }

    public BigDecimal calculateCurrentIncome(Map<Product, Integer> products) {
        // sold products
        BigDecimal income = BigDecimal.valueOf(0);
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            income = income.add(entry.getKey().getSellingPrice().multiply(BigDecimal.valueOf(entry.getValue())));
        }

        return income;
    }

    // getters

    public double getEdibleProductsMarkup() {
        return edibleProductsMarkup;
    }

    public double getNonEdibleProductsMarkup() {
        return nonEdibleProductsMarkup;
    }

    public double getDiscount() {
        return discount;
    }

    public int getDaysLeftTillExpiry() {
        return daysLeftTillExpiry;
    }

    public Set<Cashier> getCashiers() {
        return Collections.unmodifiableSet(cashiers);
    }

    public Set<CashDesk> getCashDesks() {
        return Collections.unmodifiableSet(cashDesks);
    }

    public Map<Product, Integer> getProductsInStock() {
        return Collections.unmodifiableMap(productsInStock);
    }

    public List<Receipt> getReceipts() {
        return Collections.unmodifiableList(receipts);
    }

    public BigDecimal getExpenses() {
        return expenses;
    }

    public int getCountReceipts() {
        return countReceipts;
    }

    public BigDecimal getIncome() {
        return income;
    }

    // setters

    public void setEdibleProductsMarkup(double edibleProductsMarkup) throws NegativeProductMarkup {
        if (edibleProductsMarkup <= 0) {
            throw new NegativeProductMarkup("Product markup cannot be zero or negative number!");
        }
        this.edibleProductsMarkup = edibleProductsMarkup;
    }

    public void setNonEdibleProductsMarkup(double nonEdibleProductsMarkup) throws NegativeProductMarkup {
        if (nonEdibleProductsMarkup <= 0) {
            throw new NegativeProductMarkup("Product markup cannot be zero or negative number!");
        }
        this.nonEdibleProductsMarkup = nonEdibleProductsMarkup;
    }

    public void setDaysLeftTillExpiry(int daysLeftTillExpiry) throws NegativeDays {
        if (daysLeftTillExpiry <= 0) {
            throw new NegativeDays("Days left till expiry cannot be zero or negative number!");
        }
        this.daysLeftTillExpiry = daysLeftTillExpiry;
    }

    public void setDiscount(double discount) throws NegativeDiscountValue {
        if (discount <= 0) {
            throw new NegativeDiscountValue("Discount cannot be zero or negative number!");
        }
        this.discount = discount;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    @Override
    public String toString() {
        return "Market{" +
                "edibleProductsMarkup=" + edibleProductsMarkup +
                ", nonEdibleProductsMarkup=" + nonEdibleProductsMarkup +
                ", discount=" + discount +
                ", daysLeftTillExpiry=" + daysLeftTillExpiry +
                ", cashiers=" + cashiers +
                ", cashDesks=" + cashDesks +
                ", expenses=" + expenses +
                ", productsInStock=" + productsInStock +
                ", receipts=" + receipts +
                ", countReceipts=" + countReceipts +
                ", income=" + income +
                '}';
    }
}
