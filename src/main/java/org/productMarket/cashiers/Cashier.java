package org.productMarket.cashiers;

import org.productMarket.counters.CashDesk;
import org.productMarket.customers.Customer;
import org.productMarket.exceptions.InsufficientQuantityOfProduct;
import org.productMarket.exceptions.NonExistingProduct;
import org.productMarket.exceptions.NotAbleToWorkOnSecondCounter;
import org.productMarket.exceptions.NotEnoughMoneyToBuyProduct;
import org.productMarket.markets.Market;
import org.productMarket.products.Product;
import org.productMarket.receipts.Receipt;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class Cashier implements Serializable {

    private String name;
    private String id;
    private BigDecimal salary;

    public Cashier(String name, BigDecimal salary) {
        this.setName(name);
        this.id = UUID.randomUUID().toString();
        this.salary = salary;
    }

    public boolean workOnCounter(CashDesk cashDesk) throws NotAbleToWorkOnSecondCounter {
        if (this.currentCashDesk != null) {
            throw new NotAbleToWorkOnSecondCounter("The cashier is already working on a counter!");
        }
        if (!cashDesk.isOccupied()) {
            cashDesk.setCashDeskOccupied(true);
            this.currentCashDesk = cashDesk;
            return true;
        }
        return false;
    }

    public void leaveCurrentCounter() {
        this.currentCashDesk.setCashDeskOccupied(false);
        this.currentCashDesk = null;
    }

    public BigDecimal scanProducts(Map<Product, Integer> products, Customer customer, Market market) {
        // are the current products available in the market and is the quantity enough
        products = products.entrySet()
                .stream()
                .filter(p -> {
            try {
                return market.isAvailable(p.getKey());
            } catch (NonExistingProduct e) {
                System.out.println(e.getMessage());
            }
                    return false;
                }).filter(p -> {
            try {
                return market.isEnoughQuantity(p.getKey(), p.getValue());
            } catch (InsufficientQuantityOfProduct | NonExistingProduct e) {
                System.out.println(e.getMessage());
            }
                    return false;
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> v, HashMap::new));

        // does the customer have enough money to buy the current products and if not, he cannot buy them
        products = products.entrySet()
                .stream()
                .filter(p -> {
                    try {
                        return customer.canBuyProduct(p.getKey(), p.getValue());
                    } catch (NotEnoughMoneyToBuyProduct e) {
                        System.out.println(e.getMessage());
                    }
                    return false;
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> v, HashMap::new));

        // if yes, the market sell the products
        products.forEach(market::sellProduct);

        // the customer buys the products
        products.forEach(customer::buyProduct);

        //calculate total price from all the bought products
        BigDecimal totalPrice = products.keySet().stream().map(Product::getSellingPrice).reduce(BigDecimal.valueOf(0), BigDecimal::add);
        return totalPrice;

    }

    public Receipt handInReceipt(Customer customer, BigDecimal totalPrice, Market market) {

        Receipt receipt = new Receipt(this, LocalDate.now(), customer.getBoughtProducts(), totalPrice);

        market.setTotalPriceFromAllReceipts(market.getTotalPriceFromAllReceipts().add(receipt.getTotalAmount()));
        market.addReceipt(receipt);

        return receipt;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getSalaryType() {
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
        return "Cashier{" +
                "name='" + name + '\'' +
                ", salaryType=" + salaryType +
                ", currentCounter=" + currentCashDesk +
                '}';
    }
}
