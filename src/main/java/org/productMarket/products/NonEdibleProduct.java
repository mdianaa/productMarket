package org.productMarket.products;

import org.productMarket.enums.ProductCategory;
import org.productMarket.exceptions.NegativeDeliveryPrice;

import java.math.BigDecimal;
import java.time.LocalDate;

public class NonEdibleProduct extends Product {

    public NonEdibleProduct(String name, BigDecimal deliveryPrice, LocalDate localDate) throws NegativeDeliveryPrice {
        super(name, deliveryPrice, ProductCategory.NON_EDIBLE, localDate);
    }
}
