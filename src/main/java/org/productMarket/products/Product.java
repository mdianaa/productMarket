package org.productMarket.products;

import org.productMarket.enums.ProductCategory;
import org.productMarket.exceptions.NegativeDeliveryPrice;
import org.productMarket.exceptions.NegativeSellingPrice;
import org.productMarket.exceptions.NonSellableExpiredProduct;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

public abstract class Product implements Serializable {

    private String ID;
    private String name;
    private BigDecimal deliveryPrice;
    private BigDecimal sellingPrice;
    private ProductCategory type;
    private LocalDate dateOfExpiry;

    public Product(String name, BigDecimal deliveryPrice, ProductCategory type, LocalDate dateOfExpiry) throws NegativeDeliveryPrice {
        this.ID = UUID.randomUUID().toString();
        this.setName(name);
        this.setDeliveryPrice(deliveryPrice);
        this.type = type;
        this.setDateOfExpiry(dateOfExpiry);
    }

    public boolean isExpired() throws NonSellableExpiredProduct {
        if (LocalDate.now().isAfter(this.dateOfExpiry)) {
            throw new NonSellableExpiredProduct(String.format("This product is expired with %d days!", ChronoUnit.DAYS.between(this.dateOfExpiry, LocalDate.now())));
        }
        return false;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getDeliveryPrice() {
        return deliveryPrice;
    }

    public ProductCategory getType() {
        return type;
    }

    public LocalDate getDateOfExpiry() {
        return dateOfExpiry;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    private void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new NullPointerException("Name cannot be null, blank or white space!");
        }
        this.name = name;
    }

    private void setDeliveryPrice(BigDecimal deliveryPrice) throws NegativeDeliveryPrice {
        if (deliveryPrice.compareTo(BigDecimal.valueOf(0)) <= 0) {
            throw new NegativeDeliveryPrice("Delivery price cannot be zero or negative number!");
        }
        this.deliveryPrice = deliveryPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) throws NegativeSellingPrice {
        if (sellingPrice.compareTo(BigDecimal.valueOf(0)) <= 0) {
            throw new NegativeSellingPrice("Selling price cannot be zero or negative number!");
        }
        this.sellingPrice = sellingPrice;
    }

    private void setDateOfExpiry(LocalDate dateOfExpiry) {
        if (dateOfExpiry == null) {
            throw new NullPointerException("Date of expiry cannot be null for edible products!");
        }
        this.dateOfExpiry = dateOfExpiry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return ID.equals(product.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

    @Override
    public String toString() {
        return "Product{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", deliveryPrice=" + deliveryPrice +
                ", sellingPrice=" + sellingPrice +
                ", type=" + type +
                ", dateOfExpiry=" + dateOfExpiry +
                '}';
    }
}
