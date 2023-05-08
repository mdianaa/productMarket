package org.productMarket.utils;

import org.productMarket.products.Product;
import org.productMarket.receipts.Receipt;

import java.io.*;
import java.util.Map;

public class ReceiptUtil {

    public File saveReceipt(Receipt receipt) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("receipt_" + receipt.getSerialNumber() + ".txt"));

            writer.write("Receipt serial number: " + receipt.getSerialNumber()+ "\n");
            writer.write("Cashier: " + receipt.getCashier()+ "\n");
            writer.write("Date of issue: " + receipt.getDateOfIssue()+ "\n");
            writer.write("Products: \n");
            for (Map.Entry<Product, Integer> entry : receipt.getProducts().entrySet()) {
                writer.write(entry.getKey().getName() + " x " + entry.getValue() );
            }
            writer.write("Total price: " + receipt.getTotalAmount());

            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public File readReceipt(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            String line;
            while(reader.readLine() != null) {
                System.out.println(reader.read());
            }

            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
