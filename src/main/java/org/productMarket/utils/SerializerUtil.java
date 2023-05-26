package org.productMarket.utils;

import java.io.*;

public class SerializerUtil {

    public static <T> void serializeReceipt(String filePath, T item) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(item);
        }
    }
    public static <T extends Serializable> T deserializeReceipt(String filePath)
            throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (T) ois.readObject();
        }
    }
}
