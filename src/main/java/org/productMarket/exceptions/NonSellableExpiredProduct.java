package org.productMarket.exceptions;

public class NonSellableExpiredProduct extends Exception {

    private long days;

    public NonSellableExpiredProduct(String message, long days) {
        super(message);
        this.days = days;
    }
}
