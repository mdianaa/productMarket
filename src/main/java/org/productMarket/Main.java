package org.productMarket;

import org.productMarket.cashiers.Cashier;
import org.productMarket.counters.CashDesk;
import org.productMarket.customers.Customer;
import org.productMarket.exceptions.*;
import org.productMarket.markets.Market;
import org.productMarket.products.EdibleProduct;
import org.productMarket.products.NonEdibleProduct;
import org.productMarket.products.Product;
import org.productMarket.receipts.Receipt;
import org.productMarket.utils.ReceiptUtil;
import org.productMarket.utils.SerializerUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        try {
            Market market1 = new Market(50, 50, 3, 15);
            Customer customer1 = new Customer("Ivan", BigDecimal.valueOf(1000));

            Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(2), LocalDate.of(2023, 7, 10));
            Product product2 = new NonEdibleProduct("Eye cream", BigDecimal.valueOf(4), LocalDate.of(2023, 7, 10));
            Product product3 = new EdibleProduct("Chocolate", BigDecimal.valueOf(3), LocalDate.of(2023, 7, 12));
            Product product4 = new NonEdibleProduct("T-Shirt", BigDecimal.valueOf(20), LocalDate.of(2023, 7, 10));

            market1.addDeliveredProduct(product2, 10);
            market1.addDeliveredProduct(product1, 10);
            market1.addDeliveredProduct(product3, 10);
            market1.addDeliveredProduct(product4, 10);
            market1.addDeliveredProduct(product4, 2);

            Cashier cashier1 = new Cashier("Peter", BigDecimal.valueOf(1000));
            Cashier cashier2 = new Cashier("Ivan", BigDecimal.valueOf(1000));
            Cashier cashier3 = new Cashier("Alex", BigDecimal.valueOf(1000));

            CashDesk cashDesk1 = new CashDesk(cashier1);
            CashDesk cashDesk2 = new CashDesk(cashier2);
            CashDesk cashDesk3 = new CashDesk(cashier3);

            market1.addCashDesk(cashDesk1);
            market1.addCashDesk(cashDesk2);
            market1.addCashDesk(cashDesk3);

            market1.addCashier(cashier1);
            market1.addCashier(cashier2);
            market1.addCashier(cashier3);

            Map<String, Integer> shoppingList1 = new HashMap<>();
            shoppingList1.put("Banana", 2);
            shoppingList1.put("Eye cream", 3);
            shoppingList1.put("Banitza", 2);
            shoppingList1.put("Apple", 3);

            shoppingList1 = market1.checkProductsAvailability(shoppingList1);

            if (shoppingList1 != null) {
                Receipt receipt1 = market1.sellProducts(shoppingList1, cashDesk1, customer1);
                ReceiptUtil.readReceipt("receipt_" + receipt1.getSerialNumber() + ".txt");
                try {
                    SerializerUtil.serializeReceipt("receipt_" + receipt1.getSerialNumber() + "_serialized.cer", receipt1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            Map<String, Integer> shoppingList2 = new HashMap<>();
            shoppingList2.put("Banana", 2);
            shoppingList2.put("Eye cream", 3);
            shoppingList2.put("Chocolate", 5);
            shoppingList2.put("Apple", 3);

            shoppingList2 = market1.checkProductsAvailability(shoppingList2);

            if (shoppingList2 != null) {
                Receipt receipt1 = market1.sellProducts(shoppingList2, cashDesk1, customer1);
                ReceiptUtil.readReceipt("receipt_" + receipt1.getSerialNumber() + ".txt");
            }

            cashDesk1.changeCashier(cashier2);

            System.out.println("Income: " + market1.getIncome());
            System.out.println("Expenses: " + market1.getExpenses());
            System.out.println("Profit: " + market1.calculateProfit());

        } catch (NegativeProductMarkup | NegativeDiscountValue | NegativeDays | NegativeStackOfMoney |
                 NegativeDeliveryPrice | NonSellableExpiredProduct | InvalidQuantityOfProduct | NegativeSellingPrice e) {
            System.out.println(e.getMessage());
        }

    }
}