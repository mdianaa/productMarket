package org.productMarket.cashiers;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public class Cashier implements Comparable<Cashier>, Serializable {

    private String name;
    private String id;
    private BigDecimal salary;

    public Cashier(String name, BigDecimal salary) {
        this.setName(name);
        this.id = UUID.randomUUID().toString();
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new NullPointerException("Name cannot be null, blank or white space!");
        }
        this.name = name;
    }

    @Override
    public String toString() {
        return "\tName: " + name + System.lineSeparator() +
                "\tID: " + id + System.lineSeparator();
    }

    @Override
    public int compareTo(Cashier o) {
        if (this.id.compareTo(o.id) == 0) {
            return 0;
        }
        return -1;
    }
}
