package org.productMarket;

import org.productMarket.cashiers.Cashier;
import org.productMarket.counters.CashDesk;
import org.productMarket.exceptions.*;
import org.productMarket.markets.Market;
import org.productMarket.products.EdibleProduct;
import org.productMarket.products.NonEdibleProduct;
import org.productMarket.products.Product;
import org.productMarket.receipts.Receipt;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws NegativeDeliveryPrice, InvalidQuantityOfProduct {

        Cashier cashier1 = new Cashier("Peter", BigDecimal.valueOf(1000));
        Cashier cashier2 = new Cashier("Ivan", BigDecimal.valueOf(1000));
        Cashier cashier3 = new Cashier("Alex", BigDecimal.valueOf(1000));

        CashDesk cashDesk1 = new CashDesk(cashier1);
        CashDesk cashDesk2 = new CashDesk(cashier2);
        CashDesk cashDesk3 = new CashDesk(cashier3);

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(2), LocalDate.of(2023, 5, 18));
        Product product2 = new NonEdibleProduct("Box", BigDecimal.valueOf(4));
        Product product3 = new EdibleProduct("Chocolate", BigDecimal.valueOf(3), LocalDate.of(2023, 6, 12));
        Product product4 = new NonEdibleProduct("T-Shirt", BigDecimal.valueOf(20));

        Market.addDeliveredProduct(product1, 3);
        Market.addDeliveredProduct(product2, 3);
        Market.addDeliveredProduct(product3, 5);
        Market.addDeliveredProduct(product4, 2);
        Market.addDeliveredProduct(product4, 10);

        Map<Product, Integer> shoppingList = new HashMap<>();
        shoppingList.put(new EdibleProduct("Banana", BigDecimal.valueOf(2), LocalDate.of(2023, 5, 18)), 2);
        shoppingList.put(new NonEdibleProduct("Box", BigDecimal.valueOf(3)), 3);
        shoppingList.put(new EdibleProduct("Banitza", BigDecimal.valueOf(2), LocalDate.of(2023, 5, 18)), 2);
        shoppingList.put(new EdibleProduct("Apple", BigDecimal.valueOf(2), LocalDate.of(2023, 5, 18)), 3);

        try {
            Receipt receipt = Market.sellProducts(shoppingList, cashDesk1);
            receipt.issueReceipt();
        } catch (NonExistingProduct | InsufficientQuantityOfProduct | NoSoldProducts e) {
            System.out.println(e.getMessage());
        }

    }
}