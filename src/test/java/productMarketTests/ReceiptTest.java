package productMarketTests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.productMarket.cashiers.Cashier;
import org.productMarket.exceptions.NegativeDeliveryPrice;
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
    public void createProductsMap() throws NegativeDeliveryPrice {
        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 5, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 5, 10));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 5, 10));

        products = new HashMap<>();
        products.put(product1, 3);
        products.put(product2, 3);
        products.put(product3, 3);
    }

    @Test
    public void testCreateValidReceipt() throws NegativeDeliveryPrice {
        Cashier cashier = new Cashier("Peter", SalaryCategory.MEDIUM_POSITION);

        Receipt receipt = new Receipt(cashier, LocalDate.now(), products, BigDecimal.valueOf(24));

        Assert.assertEquals(cashier, receipt.getCashier());
        Assert.assertEquals(products, receipt.getProducts());
    }

    @Test(expected = NullPointerException.class)
    public void testSetCashierWithNull() {
        Receipt receipt = new Receipt(null, LocalDate.now(), products, BigDecimal.valueOf(24));
    }

    @Test(expected = NullPointerException.class)
    public void testSetDateOfIssueWithNull() {
        Cashier cashier = new Cashier("Peter", SalaryCategory.MEDIUM_POSITION);
        Receipt receipt = new Receipt(cashier, null, products, BigDecimal.valueOf(24));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetProductsToAddANewProduct() throws NegativeDeliveryPrice {
        Cashier cashier = new Cashier("Peter", SalaryCategory.MEDIUM_POSITION);
        Receipt receipt = new Receipt(cashier, LocalDate.now(), products, BigDecimal.valueOf(24));
        Product product = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 5, 10));

        receipt.getProducts().put(product, 3);
    }


}
