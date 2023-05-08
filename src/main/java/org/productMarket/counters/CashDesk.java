package org.productMarket.counters;

import org.productMarket.cashiers.Cashier;

public class CashDesk {

    private Cashier cashier;


    public CashDesk(Cashier cashier) {
        this.cashier = cashier;
    }

    public void changeCashier(Cashier cashier) {
        this.cashier = cashier;
    }

    public Cashier getCashier() {
        return cashier;
    }
}
