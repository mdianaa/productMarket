package org.productMarket.markets;

import org.productMarket.cashiers.Cashier;
import org.productMarket.counters.CashDesk;
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

    public static double edibleProductsMarkup;
    public static double nonEdibleProductsMarkup;
    public static double discount;
    public static int daysLeftTillExpiry;
    public static Set<Cashier> cashiers;
//    public static Map<Product, Integer> deliveredProducts;
    public static BigDecimal expenses;
    public static Map<Product, Integer> soldProducts;
    public static Map<Product, Integer> productsInStock;
    public static List<Receipt> receipts;
    public static int receiptsCount;
    public static BigDecimal income;

    public Market(double edibleProductsMarkup, double nonEdibleProductsMarkup, int daysLeftTillExpiry, double discount) throws NegativeProductMarkup, NegativeDays, NegativeDiscountValue {
        setEdibleProductsMarkup(edibleProductsMarkup);
        setNonEdibleProductsMarkup(nonEdibleProductsMarkup);
        setDaysLeftTillExpiry(daysLeftTillExpiry);
        setDiscount(discount);
        cashiers = new HashSet<>();
        soldProducts = new HashMap<>();
        productsInStock = new HashMap<>();
        receipts = new ArrayList<>();
        income = new BigDecimal(0);
        expenses = new BigDecimal(0);
    }

    public static void addDeliveredProduct(Product product, Integer quantity) throws InvalidQuantityOfProduct {
        if (quantity <= 0) {
            throw new InvalidQuantityOfProduct("Quantity must be a positive number!");
        }
        expenses = expenses.add(product.getDeliveryPrice().multiply(BigDecimal.valueOf(quantity)));

        productsInStock.putIfAbsent(product, 0);
        productsInStock.put(product, productsInStock.get(product) + quantity);
    }

    public static void defineSellingPrice(Product product) throws NegativeSellingPrice {
        if (product.getType().equals(ProductCategory.EDIBLE)) {
            product.setSellingPrice(product.getDeliveryPrice().multiply(BigDecimal.valueOf(edibleProductsMarkup)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP).add(product.getDeliveryPrice()));
        } else {
            product.setSellingPrice(product.getDeliveryPrice().multiply(BigDecimal.valueOf(nonEdibleProductsMarkup)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP).add(product.getDeliveryPrice()));
        }
    }

    public static void defineSellingPriceWithDiscount(Product product) throws NonSellableExpiredProduct, NegativeSellingPrice {
        if (!product.isExpired() && isForDiscount(product)) {
            product.setSellingPrice(product.getSellingPrice().subtract(product.getSellingPrice().multiply(BigDecimal.valueOf(discount)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
        }
    }

    public static boolean isForDiscount(Product product) {
        return ChronoUnit.DAYS.between(LocalDate.now(), product.getDateOfExpiry()) < daysLeftTillExpiry;
    }

    public static boolean isEnoughQuantity(Product product, int quantity) throws InsufficientQuantityOfProduct, NonExistingProduct {
        if (isAvailable(product) && productsInStock.get(product) >= quantity) {
            return true;
        }
        // the market has the product but the quantity is not enough
        int insufficientQuantity = quantity - productsInStock.get(product);
        throw new InsufficientQuantityOfProduct(String.format("Insufficient quantity from the selected product: \nProduct: %s\nInsufficient quantity: %d\n", product.getName(), insufficientQuantity));
    }

    public static boolean isAvailable(Product product) throws NonExistingProduct {
        if (!productsInStock.containsKey(product)) {
            // the market doesn't have the product at all
            throw new NonExistingProduct(String.format("The selected product \"%s\" doesn't exist in the market!\n", product.getName()));
        }
        return true;
    }

    public static void decreaseProductQuantity(Product product, Integer requiredQuantity) {
        int availableQuantity = productsInStock.get(product);
        if (availableQuantity - requiredQuantity == 0) {
            productsInStock.remove(product);
        } else {
            productsInStock.put(product, availableQuantity - requiredQuantity);
        }

        soldProducts.putIfAbsent(product, 0);
        soldProducts.put(product, soldProducts.get(product) + requiredQuantity);
    }

    public static Receipt sellProducts(Map<Product, Integer> products, CashDesk cashDesk) throws NonExistingProduct, InsufficientQuantityOfProduct, NoSoldProducts {
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            if (!Market.isAvailable(entry.getKey()) || !Market.isEnoughQuantity(entry.getKey(), entry.getValue())) {
                products.remove(entry.getKey());
            }
        }
        return cashDesk.payForProducts(products);
    }

    public static void addCashier(Cashier cashier) {
        cashiers.add(cashier);
        expenses = expenses.add(cashier.getSalary());
    }

    public static void addReceipt(Receipt receipt) {
        receipts.add(receipt);
    }

//    public static BigDecimal calculateExpenses() throws NoDeliveredProducts, NoAvailableCashiers {
//        return calculateCashiersSalaries()
//                .add(calculateDeliveryExpenses());
//    }

    public static BigDecimal calculateProfit() {
        return income.subtract(expenses);
    }

//    public static BigDecimal calculateCashiersSalaries() throws NoAvailableCashiers {
//        if (cashiers.size() == 0) {
//            throw new NoAvailableCashiers("Currently, there are no available cashiers!");
//        }
//        return cashiers.stream().map(Cashier::getSalary).reduce(BigDecimal.valueOf(0), BigDecimal::add);
//    }

//    public static BigDecimal calculateDeliveryExpenses() throws NoDeliveredProducts {
//        // delivered products
//        if (deliveredProducts.size() == 0) {
//            throw new NoDeliveredProducts("Currently, there are no delivered products in the market!");
//        }
//        BigDecimal deliveryExpenses = BigDecimal.valueOf(0);
//        for (Map.Entry<Product, Integer> entry : deliveredProducts.entrySet()) {
//            deliveryExpenses.add(entry.getKey().getDeliveryPrice().multiply(BigDecimal.valueOf(entry.getValue())));
//        }
//
//        return deliveryExpenses;
//    }

    public static BigDecimal calculateCurrentIncome(Map<Product, Integer> products) throws NoSoldProducts {
        // sold products
        BigDecimal income = BigDecimal.valueOf(0);
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            income.add(entry.getKey().getSellingPrice().multiply(BigDecimal.valueOf(entry.getValue())));
        }

        return income;
    }

    // getters

    public static double getEdibleProductsMarkup() {
        return edibleProductsMarkup;
    }

    public static double getNonEdibleProductsMarkup() {
        return nonEdibleProductsMarkup;
    }

    public static double getDiscount() {
        return discount;
    }

    public static int getDaysLeftTillExpiry() {
        return daysLeftTillExpiry;
    }

    public static int getCountReceipts() {
        return receipts.size();
    }

    public static Set<Cashier> getCashiers() {
        return Collections.unmodifiableSet(cashiers);
    }

    public static Map<Product, Integer> getSoldProducts() {
        return Collections.unmodifiableMap(soldProducts);
    }

    public static Map<Product, Integer> getProductsInStock() {
        return Collections.unmodifiableMap(productsInStock);
    }

    public static List<Receipt> getReceipts() {
        return Collections.unmodifiableList(receipts);
    }

    // setters

    public static void setEdibleProductsMarkup(double edibleProductsMarkup) throws NegativeProductMarkup {
        if (edibleProductsMarkup <= 0) {
            throw new NegativeProductMarkup("Product markup cannot be zero or negative number!");
        }
        Market.edibleProductsMarkup = edibleProductsMarkup;
    }

    public static void setNonEdibleProductsMarkup(double nonEdibleProductsMarkup) throws NegativeProductMarkup {
        if (nonEdibleProductsMarkup <= 0) {
            throw new NegativeProductMarkup("Product markup cannot be zero or negative number!");
        }
        Market.nonEdibleProductsMarkup = nonEdibleProductsMarkup;
    }

    public static void setDaysLeftTillExpiry(int daysLeftTillExpiry) throws NegativeDays {
        if (daysLeftTillExpiry < 0) {
            throw new NegativeDays("Days left till expiry cannot be negative number!");
        }
        Market.daysLeftTillExpiry = daysLeftTillExpiry;
    }

    public static void setDiscount(double discount) throws NegativeDiscountValue {
        if (discount <= 0) {
            throw new NegativeDiscountValue("Discount cannot be zero or negative number!");
        }
        Market.discount = discount;
    }

}
