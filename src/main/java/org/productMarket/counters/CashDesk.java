package org.productMarket.counters;

import org.productMarket.cashiers.Cashier;
import org.productMarket.products.Product;
import org.productMarket.receipts.Receipt;
import org.productMarket.utils.ReceiptUtil;

import java.io.Serializable;
import java.util.Map;

public class CashDesk implements Serializable {

    private Cashier cashier;

    public CashDesk(Cashier cashier) {
        this.setCashier(cashier);
    }

    public void changeCashier(Cashier cashier) {
        this.cashier = cashier;
    }

    public Receipt generateReceipt(Map<Product, Integer> products) {
        // generate receipt
        Receipt receipt = new Receipt(this.cashier, products);

        // write the content from the receipt in a file
        ReceiptUtil.saveReceipt(receipt);

       return receipt;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public void setCashier(Cashier cashier) {
        if (cashier == null) {
            throw new NullPointerException("Cashier cannot be null!");
        }
        this.cashier = cashier;
    }

    @Override
    public String toString() {
        return "CashDesk{" +
                "cashier=" + cashier +
                '}';
    }
}
