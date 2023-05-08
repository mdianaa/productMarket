package org.productMarket.products;

import org.productMarket.enums.ProductCategory;
import org.productMarket.exceptions.NegativeDeliveryPrice;

import java.math.BigDecimal;

public class NonEdibleProduct extends Product {

    public NonEdibleProduct(String name, BigDecimal deliveryPrice) throws NegativeDeliveryPrice {
        super(name, deliveryPrice, ProductCategory.NON_EDIBLE, null);
    }
}
