package org.productMarket.counters;

import org.productMarket.cashiers.Cashier;
import org.productMarket.exceptions.NoSoldProducts;
import org.productMarket.markets.Market;
import org.productMarket.products.Product;
import org.productMarket.receipts.Receipt;

import java.util.Map;

public class CashDesk {

    private Cashier cashier;

    public CashDesk(Cashier cashier) {
        this.cashier = cashier;
    }

    public void changeCashier(Cashier cashier) {
        this.cashier = cashier;
    }

    public Receipt payForProducts(Map<Product, Integer> products) throws NoSoldProducts {
        // generate receipt
        Receipt receipt = new Receipt(this.cashier, products);

        // update products in market
        products.forEach(Market::decreaseProductQuantity);

        // calculate income from sold products
        Market.income = Market.income.add(Market.calculateCurrentIncome(products));

        // add receipt to the market
        Market.addReceipt(receipt);

        return receipt;
    }

    public Cashier getCashier() {
        return cashier;
    }
}
