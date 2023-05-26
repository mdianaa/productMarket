package org.productMarket.utils;

import org.productMarket.products.Product;
import org.productMarket.receipts.Receipt;

import java.io.*;
import java.math.BigDecimal;
import java.util.Map;

public class ReceiptUtil {

    public static void saveReceipt(Receipt receipt) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("receipt_" + receipt.getSerialNumber() + ".txt"));

            writer.write("Receipt serial number: " + receipt.getSerialNumber()+ "\n");
            writer.write("Date of issue: " + receipt.getDateOfIssue()+ "\n");
            writer.write("Cashier: \n" + receipt.getCashier()+ "\n");
            writer.write("Products: \n");
            for (Map.Entry<Product, Integer> entry : receipt.getProducts().entrySet()) {
                writer.write("\t" + entry.getValue() + "x " + entry.getKey().getName() + " - " +
                        entry.getKey().getSellingPrice().multiply(BigDecimal.valueOf(entry.getValue())) + "\n");
            }
            writer.write("\nTotal receipt price: " + receipt.getTotalPrice());

            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void readReceipt(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println();
            reader.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
