package org.productMarket.customers;

import org.productMarket.exceptions.NegativeStackOfMoney;
import org.productMarket.products.Product;

import java.io.Serializable;
import java.math.BigDecimal;

public class Customer implements Serializable {

    private String name;
    private BigDecimal money;


    public Customer(String name, BigDecimal money) throws NegativeStackOfMoney {
        this.setName(name);
        this.setMoney(money);
    }

    public boolean canBuyProduct(Product product, Integer quantity) {
        BigDecimal totalPrice = product.getSellingPrice().multiply(BigDecimal.valueOf(quantity));
        if (totalPrice.compareTo(this.money) >= 1) {
            return false;
        }

        return true;
    }

    public void decreaseMoney(Product product, int quantity) {
        this.money = this.money.subtract(product.getSellingPrice().multiply(BigDecimal.valueOf(quantity)));
    }

    public String getName() {
        return name;
    }

    public BigDecimal getMoney() {
        return money;
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
                '}';
    }
}
