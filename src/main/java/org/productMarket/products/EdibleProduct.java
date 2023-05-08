package org.productMarket.products;

import org.productMarket.enums.ProductCategory;
import org.productMarket.exceptions.NegativeDeliveryPrice;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EdibleProduct extends Product {

    public EdibleProduct(String name, BigDecimal deliveryPrice, LocalDate dateOfExpiry) throws NegativeDeliveryPrice {
        super(name, deliveryPrice, ProductCategory.EDIBLE, dateOfExpiry);
    }
}
