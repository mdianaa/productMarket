package org.productMarket.receipts;

import org.productMarket.cashiers.Cashier;
import org.productMarket.markets.Market;
import org.productMarket.products.Product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

public class Receipt implements Serializable {

    private int serialNumber;
    private Cashier cashier;
    private LocalDate dateOfIssue;
    private Map<Product, Integer> products;
    private BigDecimal totalPrice;

    public Receipt(Cashier cashier, Map<Product, Integer> products) {
        this.serialNumber = ++Market.receiptsCount;
        this.setCashier(cashier);
        this. dateOfIssue = LocalDate.now();
        this.products = products;
        this.totalPrice = calculateTotalPrice();
    }

    public void issueReceipt() {
        // TODO - write in file
    }

    private BigDecimal calculateTotalPrice() {
        return this.products.entrySet()
                .stream()
                .map(p -> p.getKey().getSellingPrice().multiply(BigDecimal.valueOf(p.getValue())))
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
    }

    public long getSerialNumber() {
        return serialNumber;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public LocalDate getDateOfIssue() {
        return dateOfIssue;
    }

    public Map<Product, Integer> getProducts() {
        return Collections.unmodifiableMap(products);
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setCashier(Cashier cashier) {
        if (cashier == null) {
            throw new NullPointerException("Cashier cannot be null!");
        }
        this.cashier = cashier;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "serialNumber=" + serialNumber +
                ", cashier=" + cashier +
                ", dateOfIssue=" + dateOfIssue +
                ", products=" + products +
                ", totalAmount=" + totalPrice +
                '}';
    }
}
