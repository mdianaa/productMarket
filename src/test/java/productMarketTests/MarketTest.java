package productMarketTests;

import org.junit.Assert;
import org.junit.Test;
import org.productMarket.cashiers.Cashier;
import org.productMarket.counters.CashDesk;
import org.productMarket.customers.Customer;
import org.productMarket.exceptions.*;
import org.productMarket.markets.Market;
import org.productMarket.products.EdibleProduct;
import org.productMarket.products.Product;
import org.productMarket.receipts.Receipt;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class MarketTest {

    @Test
    public void testCreateValidMarket() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue {
        Market market = null;
        String error = null;

        try {
            market = new Market(10, 5, 3, 13);
        } catch (NullPointerException e) {
            error = e.getMessage();
        }

        Assert.assertNull(error);
        Assert.assertNotNull(market);
    }

    @Test
    public void testSetEdibleProductsMarkupWithNegativeValueThrows() throws NegativeDays, NegativeDiscountValue {
        String error = null;

        try {
            new Market(-2, 5, 3, 13);
        } catch (NegativeProductMarkup e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Product markup cannot be zero or negative number!", error);
    }

    @Test
    public void testSetEdibleProductsMarkupWithZeroThrows() throws NegativeDays, NegativeDiscountValue {
        String error = null;

        try {
            new Market(0, 5, 3, 13);
        } catch (NegativeProductMarkup e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Product markup cannot be zero or negative number!", error);
    }

    @Test
    public void testSetNonEdibleProductsMarkupWithNegativeValueThrows() throws NegativeDays, NegativeDiscountValue {
        String error = null;

        try {
            new Market(10, -5, 3, 13);
        } catch (NegativeProductMarkup e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Product markup cannot be zero or negative number!", error);
    }

    @Test
    public void testSetNonEdibleProductsMarkupWithZeroThrows() throws NegativeDays, NegativeDiscountValue {
        String error = null;

        try {
            new Market(10, 0, 3, 13);
        } catch (NegativeProductMarkup e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Product markup cannot be zero or negative number!", error);
    }

    @Test
    public void testSetDaysLeftTillExpiryWithNegativeValueThrows() throws NegativeProductMarkup, NegativeDiscountValue {
        String error = null;

        try {
            new Market(10, 5, -2, 13);
        } catch (NegativeDays e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Days left till expiry cannot be zero or negative number!", error);
    }

    @Test
    public void testSetDaysLeftTillExpiryWithZero() throws NegativeProductMarkup, NegativeDiscountValue {
        String error = null;

        try {
            new Market(10, 5, 0, 13);
        } catch (NegativeDays e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Days left till expiry cannot be zero or negative number!", error);
    }

    @Test
    public void testSetDiscountWithNegativeNumberThrows() throws NegativeDays, NegativeProductMarkup {
        String error = null;

        try {
            new Market(10, 5, 2, -2);
        } catch (NegativeDiscountValue e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Discount cannot be zero or negative number!", error);
    }

    @Test
    public void testSetDiscountWithZeroThrows() throws NegativeDays, NegativeProductMarkup {
        String error = null;

        try {
            new Market(10, 5, 2, 0);
        } catch (NegativeDiscountValue e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Discount cannot be zero or negative number!", error);
    }

    @Test
    public void testSetIncome() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue {
        Market market = new Market(10, 5, 3, 5);
        market.setIncome(BigDecimal.valueOf(2000));

        Assert.assertEquals(BigDecimal.valueOf(2000), market.getIncome());
    }

    @Test
    public void testAddDeliveredProductWithValidQuantity() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice, InvalidQuantityOfProduct, NonSellableExpiredProduct, NegativeSellingPrice {
        Market market = new Market(50, 5, 4, 10);

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 7, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 6, 2));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 7, 10));

        market.addDeliveredProduct(product1, 3);
        market.addDeliveredProduct(product2, 3);
        market.addDeliveredProduct(product3, 3);

        Assert.assertEquals(3, market.getProductsInStock().size());
        Assert.assertEquals(BigDecimal.valueOf(24), market.getExpenses());
        Assert.assertEquals(BigDecimal.valueOf(4.50), product1.getSellingPrice());
        Assert.assertEquals(BigDecimal.valueOf(5.40), product2.getSellingPrice());
    }

    @Test
    public void testAddDeliveredProductWithNegativeQuantityNumberThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice, InvalidQuantityOfProduct, NonSellableExpiredProduct, NegativeSellingPrice {
        Market market = new Market(10, 5, 3, 10);

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 7, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 7, 10));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 7, 10));

        String error = null;

        try {
            market.addDeliveredProduct(product1, -3);
            market.addDeliveredProduct(product2, 3);
            market.addDeliveredProduct(product3, 3);
        } catch (InvalidQuantityOfProduct e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Quantity must be a positive number!", error);
    }

    @Test
    public void testAddDeliveredProductWithZeroQuantityNumberThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice, InvalidQuantityOfProduct, NonSellableExpiredProduct, NegativeSellingPrice {
        Market market = new Market(10, 5, 3, 10);

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 7, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 7, 10));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 7, 10));

        String error = null;

        try {
            market.addDeliveredProduct(product1, 0);
            market.addDeliveredProduct(product2, 3);
            market.addDeliveredProduct(product3, 3);
        } catch (InvalidQuantityOfProduct e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Quantity must be a positive number!", error);
    }

    @Test
    public void testSellProducts() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice, NonSellableExpiredProduct, InvalidQuantityOfProduct, NegativeSellingPrice, NegativeStackOfMoney {
        Market market = new Market(50, 5, 3, 10);
        Cashier cashier = new Cashier("Peter", BigDecimal.valueOf(1000));
        CashDesk cashDesk = new CashDesk(cashier);
        Customer customer = new Customer("Ivan", BigDecimal.valueOf(100));

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 7, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 7, 10));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 7, 10));

        market.addDeliveredProduct(product1, 3);
        market.addDeliveredProduct(product2, 3);
        market.addDeliveredProduct(product3, 3);

        Map<String, Integer> shoppingList = new HashMap<>();
        shoppingList.put("Banana", 2);
        shoppingList.put("Apple", 2);

        market.sellProducts(shoppingList, cashDesk, customer);

        Assert.assertEquals(1, market.getReceipts().size());
        Assert.assertEquals(1, (int) market.getProductsInStock().get(product1));
        Assert.assertEquals(1, (int) market.getProductsInStock().get(product2));
    }

    @Test
    public void testCheckProductsAvailability() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice, NonSellableExpiredProduct, InvalidQuantityOfProduct, NegativeSellingPrice {
        Market market = new Market(10, 5, 3, 10);

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 7, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 7, 10));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 7, 10));

        market.addDeliveredProduct(product1, 3);
        market.addDeliveredProduct(product2, 3);
        market.addDeliveredProduct(product3, 3);

        Map<String, Integer> shoppingList = new HashMap<>();
        shoppingList.put("Banana", 2);
        shoppingList.put("Apple", 2);

        Map<String, Integer> availableProducts = market.checkProductsAvailability(shoppingList);
        Assert.assertEquals(2, availableProducts.size());
        Assert.assertTrue(shoppingList.containsKey("Banana"));
        Assert.assertTrue(shoppingList.containsKey("Apple"));
    }

    @Test
    public void testCheckProductsAvailabilityWithNonAvailableProducts() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice, NonSellableExpiredProduct, InvalidQuantityOfProduct, NegativeSellingPrice {
        Market market = new Market(10, 5, 3, 10);

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 7, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 7, 10));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 7, 10));

        market.addDeliveredProduct(product1, 3);
        market.addDeliveredProduct(product2, 3);
        market.addDeliveredProduct(product3, 3);

        Map<String, Integer> shoppingList = new HashMap<>();
        shoppingList.put("Tomato", 2);
        shoppingList.put("Chocolate", 2);

        Map<String, Integer> availableProducts = market.checkProductsAvailability(shoppingList);
        Assert.assertNull(availableProducts);
    }

    @Test
    public void testCheckProductsAvailabilityWithOneAvailableProduct() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice, NonSellableExpiredProduct, InvalidQuantityOfProduct, NegativeSellingPrice {
        Market market = new Market(10, 5, 3, 10);

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 7, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 7, 10));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 7, 10));

        market.addDeliveredProduct(product1, 3);
        market.addDeliveredProduct(product2, 3);
        market.addDeliveredProduct(product3, 3);

        Map<String, Integer> shoppingList = new HashMap<>();
        shoppingList.put("Banana", 2);
        shoppingList.put("Chocolate", 2);

        Map<String, Integer> availableProducts = market.checkProductsAvailability(shoppingList);
        Assert.assertEquals(1, availableProducts.size());
        Assert.assertTrue(shoppingList.containsKey("Banana"));
    }

    @Test
    public void testAddCashier() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue {
        Market market = new Market(10, 5, 3, 10);
        Cashier cashier = new Cashier("Peter", BigDecimal.valueOf(1000));

        market.addCashier(cashier);

        Assert.assertTrue(market.getCashiers().contains(cashier));
    }

    @Test
    public void testAddCashDesk() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue {
        Market market = new Market(10, 5, 3, 10);
        Cashier cashier = new Cashier("Peter", BigDecimal.valueOf(1000));
        CashDesk cashDesk = new CashDesk(cashier);

        market.addCashDesk(cashDesk);

        Assert.assertTrue(market.getCashDesks().contains(cashDesk));
    }

    @Test
    public void testAddReceipt() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice, NegativeSellingPrice {
        Market market = new Market(10, 5, 3, 10);
        Cashier cashier = new Cashier("Peter", BigDecimal.valueOf(1000));

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 5, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 5, 10));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 5, 10));

        product1.setSellingPrice(BigDecimal.valueOf(5));
        product2.setSellingPrice(BigDecimal.valueOf(6));
        product3.setSellingPrice(BigDecimal.valueOf(3));

        Map<Product, Integer> products = new HashMap<>();
        products.put(product1, 3);
        products.put(product2, 3);
        products.put(product3, 3);

        Receipt receipt = new Receipt(cashier, products);

        market.addReceipt(receipt);

        Assert.assertTrue(market.getReceipts().contains(receipt));
    }
}
