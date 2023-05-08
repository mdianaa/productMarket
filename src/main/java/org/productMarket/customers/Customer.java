package org.productMarket.customers;

import org.productMarket.exceptions.NegativeStackOfMoney;
import org.productMarket.exceptions.NotEnoughMoneyToBuyProduct;
import org.productMarket.products.Product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

public class Customer implements Serializable {

    private String name;
    private BigDecimal money;
    private Map<Product, Integer> boughtProducts;

    public Customer(String name, BigDecimal money) throws NegativeStackOfMoney {
        this.setName(name);
        this.setMoney(money);
        this.boughtProducts = new HashMap<>();
    }

    public void buyProduct(Product product, Integer quantity) {
        this.boughtProducts.putIfAbsent(product, 0);
        this.boughtProducts.put(product, this.boughtProducts.get(product) + quantity);
        this.money = this.money.subtract(product.getSellingPrice().multiply(BigDecimal.valueOf(quantity)));
    }

    public boolean canBuyProduct(Product product, Integer quantity) throws NotEnoughMoneyToBuyProduct {
        BigDecimal totalPrice = product.getSellingPrice().multiply(BigDecimal.valueOf(quantity));

        if (totalPrice.compareTo(this.money) >= 1) {
            throw new NotEnoughMoneyToBuyProduct(String.format("Not enough money to buy the product: %s", product.getName()));
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public Map<Product, Integer> getBoughtProducts() {
        return Collections.unmodifiableMap(boughtProducts);
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new NullPointerException("Name cannot be null, blank space or whitespace!");
        }
        this.name = name;
    }

    public void setMoney(BigDecimal money) throws NegativeStackOfMoney {
        if (money.compareTo(BigDecimal.valueOf(0)) <= 0) {
            throw new NegativeStackOfMoney("Money cannot be zero or negative number!");
        }
        this.money = money;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", money=" + money +
                ", boughtProducts=" + boughtProducts +
                '}';
    }
}
