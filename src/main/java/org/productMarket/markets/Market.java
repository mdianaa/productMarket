package org.productMarket.markets;

import org.productMarket.cashiers.Cashier;
import org.productMarket.receipts.Receipt;
import org.productMarket.enums.ProductCategory;
import org.productMarket.exceptions.*;
import org.productMarket.products.Product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Market implements Serializable {

    private String name;
    private double edibleProductsMarkup;
    private double nonEdibleProductsMarkup;
    private double discount;
    private int daysLeftTillExpiry;
    private BigDecimal totalPriceFromAllReceipts;
    private Set<Cashier> cashiers;
    private Map<Product, Integer> deliveredProducts;
    private Map<Product, Integer> soldProducts;
    private Map<Product, Integer> productsInStock;
    private List<Receipt> receipts;

    public Market(String name, double edibleProductsMarkup, double nonEdibleProductsMarkup, int daysLeftTillExpiry, double discount) throws NegativeProductMarkup, NegativeDays, NegativeDiscountValue {
        this.setName(name);
        this.setEdibleProductsMarkup(edibleProductsMarkup);
        this.setNonEdibleProductsMarkup(nonEdibleProductsMarkup);
        this.setDaysLeftTillExpiry(daysLeftTillExpiry);
        this.setDiscount(discount);
        this.totalPriceFromAllReceipts = new BigDecimal(BigInteger.valueOf(0));
        this.cashiers = new HashSet<>();
        this.deliveredProducts = new HashMap<>();
        this.soldProducts = new HashMap<>();
        this.productsInStock = new HashMap<>();
        this.receipts = new ArrayList<>();
    }

    public void addDeliveredProduct(Product product, Integer quantity) throws InvalidQuantityOfProduct {
        if (quantity <= 0) {
            throw new InvalidQuantityOfProduct("Quantity must be a positive number!");
        }

        this.deliveredProducts.putIfAbsent(product, 0);
        this.deliveredProducts.put(product, this.deliveredProducts.get(product) + quantity);
        this.productsInStock.putIfAbsent(product, 0);
        this.productsInStock.put(product, this.productsInStock.get(product) + quantity);
    }

    public void defineSellingPrice(Product product) throws NegativeSellingPrice {
        if (product.getType().equals(ProductCategory.EDIBLE)) {
            product.setSellingPrice(product.getDeliveryPrice().multiply(BigDecimal.valueOf(this.edibleProductsMarkup)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP).add(product.getDeliveryPrice()));
        } else {
            product.setSellingPrice(product.getDeliveryPrice().multiply(BigDecimal.valueOf(this.nonEdibleProductsMarkup)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP).add(product.getDeliveryPrice()));
        }
//        return product.getSellingPrice();
    }

    public void defineSellingPriceWithDiscount(Product product) throws NonSellableExpiredProduct, NegativeSellingPrice {
        if (!product.isExpired() && this.isForDiscount(product)) {
            product.setSellingPrice(product.getSellingPrice().subtract(product.getSellingPrice().multiply(BigDecimal.valueOf(this.discount)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
        }
    }

    public boolean isForDiscount(Product product) {
        if (ChronoUnit.DAYS.between(LocalDate.now(), product.getDateOfExpiry()) < this.daysLeftTillExpiry) {
            return true;
        }
        return false;
    }

    public boolean isEnoughQuantity(Product product, int quantity) throws InsufficientQuantityOfProduct, NonExistingProduct {
        if (this.isAvailable(product) && this.productsInStock.get(product) >= quantity) {
            return true;
        }
        // the market has the product but the quantity is not enough
        int insufficientQuantity = quantity - this.productsInStock.get(product);
        throw new InsufficientQuantityOfProduct(String.format("Insufficient quantity from the selected product: \nProduct: %s\nInsufficient quantity: %d\n", product.getName(), insufficientQuantity));
    }

    public boolean isAvailable(Product product) throws NonExistingProduct {
        if (!this.productsInStock.containsKey(product)) {
            // the market doesn't have the product at all
            throw new NonExistingProduct(String.format("The selected product \"%s\" doesn't exist in the market!\n", product.getName()));
        }
        return true;
    }

    public void sellProduct (Product product, Integer requiredQuantity) {
        int availableQuantity = this.productsInStock.get(product);
        if (availableQuantity - requiredQuantity == 0) {
            this.productsInStock.remove(product);
        } else {
            this.productsInStock.put(product, availableQuantity - requiredQuantity);
        }

        this.soldProducts.putIfAbsent(product, 0);
        this.soldProducts.put(product, this.soldProducts.get(product) + requiredQuantity);
    }

    public void addCashier(Cashier cashier) {
        this.cashiers.add(cashier);
    }

    public void addReceipt(Receipt receipt) {
        this.receipts.add(receipt);
    }

    public BigDecimal calculateExpenses() throws NoDeliveredProducts, NoAvailableCashiers {
        return this.calculateCashiersSalaries()
                .add(this.calculateDeliveryExpenses());
    }

    public BigDecimal calculateProfit() throws NoSoldProducts, NoDeliveredProducts, NoAvailableCashiers {
        return calculateIncome().subtract(calculateExpenses());
    }

    public BigDecimal calculateCashiersSalaries() throws NoAvailableCashiers {
        if (this.cashiers.size() == 0) {
            throw new NoAvailableCashiers("Currently, there are no available cashiers!");
        }
        return BigDecimal.valueOf(this.cashiers.stream().map(c -> c.getSalaryType().getSalary()).reduce((double) 0, Double::sum));
    }

    public BigDecimal calculateDeliveryExpenses() throws NoDeliveredProducts {
        // delivered products
        if (this.deliveredProducts.size() == 0) {
            throw new NoDeliveredProducts("Currently, there are no delivered products in the market!");
        }
        BigDecimal deliveryExpenses = BigDecimal.valueOf(0);
        for (Map.Entry<Product, Integer> entry : this.deliveredProducts.entrySet()) {
            deliveryExpenses.add(entry.getKey().getDeliveryPrice().multiply(BigDecimal.valueOf(entry.getValue())));
        }

        return deliveryExpenses;
    }

    public BigDecimal calculateIncome() throws NoSoldProducts {
        // sold products
        if (this.soldProducts.size() == 0) {
            throw new NoSoldProducts("Currently, there are no sold products!");
        }

        BigDecimal income = BigDecimal.valueOf(0);
        for (Map.Entry<Product, Integer> entry : this.soldProducts.entrySet()) {
            income.add(entry.getKey().getSellingPrice().multiply(BigDecimal.valueOf(entry.getValue())));
        }

        return income;
    }

    // getters

    public String getName() {
        return name;
    }

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

    public int getCountReceipts() {
        return this.receipts.size();
    }

    public BigDecimal getTotalPriceFromAllReceipts() {
        return totalPriceFromAllReceipts;
    }

    public Set<Cashier> getCashiers() {
        return Collections.unmodifiableSet(cashiers);
    }

    public Map<Product, Integer> getDeliveredProducts() {
        return Collections.unmodifiableMap(deliveredProducts);
    }

    public Map<Product, Integer> getSoldProducts() {
        return Collections.unmodifiableMap(soldProducts);
    }

    public Map<Product, Integer> getProductsInStock() {
        return Collections.unmodifiableMap(productsInStock);
    }

    public List<Receipt> getReceipts() {
        return Collections.unmodifiableList(receipts);
    }

    // setters

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new NullPointerException("Name cannot be null, blank space or whitespace!");
        }
        this.name = name;
    }

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
        if (daysLeftTillExpiry < 0) {
            throw new NegativeDays("Days left till expiry cannot be negative number!");
        }
        this.daysLeftTillExpiry = daysLeftTillExpiry;
    }

    public void setDiscount(double discount) throws NegativeDiscountValue {
        if (discount <= 0) {
            throw new NegativeDiscountValue("Discount cannot be zero or negative number!");
        }
        this.discount = discount;
    }

    public void setTotalPriceFromAllReceipts(BigDecimal totalPriceFromAllReceipts) {
        this.totalPriceFromAllReceipts = totalPriceFromAllReceipts;
    }

    @Override
    public String toString() {
        return "Market{" +
                "edibleProductsMarkup=" + edibleProductsMarkup +
                ", nonEdibleProductsMarkup=" + nonEdibleProductsMarkup +
                ", discount=" + discount +
                ", daysLeftTillExpiry=" + daysLeftTillExpiry +
                ", totalPriceFromAllReceipts=" + totalPriceFromAllReceipts +
                ", cashiers=" + cashiers +
                ", soldProducts=" + soldProducts +
                ", productsInStock=" + productsInStock +
                ", receipts=" + receipts +
                '}';
    }
}
