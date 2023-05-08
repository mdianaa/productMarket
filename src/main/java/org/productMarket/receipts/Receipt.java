package org.productMarket.receipts;

import org.productMarket.cashiers.Cashier;
import org.productMarket.products.Product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

public class Receipt implements Serializable {

    private static long serialNumber;
    private Cashier cashier;
    private LocalDate dateOfIssue;
    private Map<Product, Integer> products;
    private BigDecimal totalAmount;

    public Receipt(Cashier cashier, LocalDate dateOfIssue, Map<Product, Integer> products, BigDecimal totalAmount) {
        Receipt.serialNumber++;
        this.setCashier(cashier);
        this.setDateOfIssue(dateOfIssue);
        this.products = products;
        this.totalAmount = totalAmount;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setCashier(Cashier cashier) {
        if (cashier == null) {
            throw new NullPointerException("Cashier cannot be null!");
        }
        this.cashier = cashier;
    }

    public void setDateOfIssue(LocalDate dateOfIssue) {
        if (dateOfIssue == null) {
            throw new NullPointerException("Date of issue cannot be null!");
        }
        this.dateOfIssue = dateOfIssue;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "serialNumber=" + serialNumber +
                ", cashier=" + cashier +
                ", dateOfIssue=" + dateOfIssue +
                ", products=" + products +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
