package productMarketTests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.productMarket.cashiers.Cashier;
import org.productMarket.exceptions.NegativeDeliveryPrice;
import org.productMarket.exceptions.NegativeSellingPrice;
import org.productMarket.products.EdibleProduct;
import org.productMarket.products.Product;
import org.productMarket.receipts.Receipt;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ReceiptTest {

    private Map<Product, Integer> products;

    @Before
    public void createProductsMap() throws NegativeDeliveryPrice, NegativeSellingPrice {
        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 5, 30));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 5, 30));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 5, 30));

        products = new HashMap<>();

        product1.setSellingPrice(BigDecimal.valueOf(5));
        product2.setSellingPrice(BigDecimal.valueOf(6));
        product3.setSellingPrice(BigDecimal.valueOf(3));

        products.put(product1, 3);
        products.put(product2, 3);
        products.put(product3, 3);
    }

    @Test
    public void testCreateValidReceipt() {
        Cashier cashier = new Cashier("Peter", BigDecimal.valueOf(1000));
        Receipt receipt = new Receipt(cashier, products);

        Assert.assertEquals(cashier, receipt.getCashier());
        Assert.assertEquals(products, receipt.getProducts());
    }

    @Test
    public void testSetCashierWithNull() {
        String error = null;

        try {
            new Receipt(null, products);
        } catch (NullPointerException e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Cashier cannot be null!", error);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetProductsToAddANewProduct() throws NegativeDeliveryPrice {
        Cashier cashier = new Cashier("Peter", BigDecimal.valueOf(1000));
        Receipt receipt = new Receipt(cashier, products);
        Product product = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 5, 10));

        receipt.getProducts().put(product, 3);
    }
}
