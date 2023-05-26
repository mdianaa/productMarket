package productMarketTests;

import org.junit.Assert;
import org.junit.Test;
import org.productMarket.cashiers.Cashier;
import org.productMarket.counters.CashDesk;
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

    @Test(expected = NegativeProductMarkup.class)
    public void testSetEdibleProductsMarkupWithNegativeValueThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue {
        new Market(-2, 5, 3, 13);
    }

    @Test(expected = NegativeProductMarkup.class)
    public void testSetEdibleProductsMarkupWithZeroThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue {
        new Market(0, 5, 3, 13);
    }

    @Test(expected = NegativeProductMarkup.class)
    public void testSetNonEdibleProductsMarkupWithNegativeValueThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue {
        new Market(10, -2, 3, 13);
    }

    @Test(expected = NegativeProductMarkup.class)
    public void testSetNonEdibleProductsMarkupWithZeroThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue {
        new Market(10, 0, 3, 13);
    }

    @Test(expected = NegativeDays.class)
    public void testSetDaysLeftTillExpiryWithNegativeValueThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue {
        new Market(10, 5, -2, 13);
    }

    @Test
    public void testSetDaysLeftTillExpiryWithZero() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue {
        Market market = new Market(10, 5, 0, 13);

        Assert.assertEquals(0, market.getDaysLeftTillExpiry());
    }

    @Test(expected = NegativeDiscountValue.class)
    public void testSetDiscountWithNegativeNumberThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue {
        new Market(10, 5, 3, -10);
    }

    @Test(expected = NegativeDiscountValue.class)
    public void testSetDiscountWithZeroThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue {
        new Market(10, 5, 3, 0);
    }

    @Test
    public void testSetIncome() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue {
        Market market = new Market(10, 5, 3, 5);
        market.setIncome(BigDecimal.valueOf(2000));

        Assert.assertEquals(BigDecimal.valueOf(2000), market.getIncome());
    }

    @Test
    public void testAddDeliveredProductWithValidQuantity() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice, InvalidQuantityOfProduct, NonSellableExpiredProduct, NegativeSellingPrice {
        Market market = new Market(10, 5, 3, 10);

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 7, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 7, 10));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 7, 10));

        market.addDeliveredProduct(product1, 3);
        market.addDeliveredProduct(product2, 3);
        market.addDeliveredProduct(product3, 3);

        Assert.assertEquals(3, market.getProductsInStock().size());
        Assert.assertEquals(BigDecimal.valueOf(24), market.getExpenses());
    }

    @Test(expected = InvalidQuantityOfProduct.class)
    public void testAddDeliveredProductWithNegativeQuantityNumberThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice, InvalidQuantityOfProduct, NonSellableExpiredProduct, NegativeSellingPrice {
        Market market = new Market(10, 5, 3, 10);

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 7, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 7, 10));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 7, 10));

        market.addDeliveredProduct(product1, -3);
        market.addDeliveredProduct(product2, 3);
        market.addDeliveredProduct(product3, 3);
    }

    @Test(expected = InvalidQuantityOfProduct.class)
    public void testAddDeliveredProductWithZeroQuantityNumberThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice, InvalidQuantityOfProduct, NonSellableExpiredProduct, NegativeSellingPrice {
        Market market = new Market(10, 5, 3, 10);

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 7, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 7, 10));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 7, 10));

        market.addDeliveredProduct(product1, 0);
        market.addDeliveredProduct(product2, 3);
        market.addDeliveredProduct(product3, 3);
    }










    @Test
    public void testIsForDiscountWithDaysLessThanDaysLeftTillDiscount() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice {
        Market market = new Market(10, 5, 3, 10);
        Product product = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 5, 27));

        Assert.assertTrue(market.isForDiscount(product));
    }

    @Test
    public void testIsForDiscountWithZeroDaysLeftTillExpiry() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice {
        Market market = new Market(10, 5, 3, 10);
        Product product = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 5, 26));

        Assert.assertTrue(market.isForDiscount(product));
    }

    @Test
    public void testIsForDiscountWithDaysMoreThanDaysLeftTillExpiry() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice {
        Market market = new Market(10, 5, 3, 10);
        Product product = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 7, 30));

        Assert.assertFalse(market.isForDiscount(product));
    }

    @Test
    public void testIsAvailableReturnsTrue() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice, InvalidQuantityOfProduct, InsufficientQuantityOfProduct, NonExistingProduct, NonSellableExpiredProduct, NegativeSellingPrice {
        Market market = new Market(10, 5, 3, 10);

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 7, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 7, 10));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 7, 10));

        market.addDeliveredProduct(product1, 3);
        market.addDeliveredProduct(product2, 3);
        market.addDeliveredProduct(product3, 3);

        Assert.assertTrue(market.isAvailable("Banana", 3));
        Assert.assertTrue(market.isAvailable("Apple", 3));
        Assert.assertTrue(market.isAvailable("Bread", 3));
    }

    @Test
    public void testIsAvailableWithNonExistingProductReturnsFalse() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice, InvalidQuantityOfProduct, InsufficientQuantityOfProduct, NonExistingProduct, NonSellableExpiredProduct, NegativeSellingPrice {
        Market market = new Market(10, 5, 3, 10);

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 7, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 7, 10));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 7, 10));

        market.addDeliveredProduct(product1, 3);
        market.addDeliveredProduct(product2, 3);

        Assert.assertFalse(market.isAvailable("Bread", 3));
    }

    @Test(expected = InsufficientQuantityOfProduct.class)
    public void testIsAvailableWithNotEnoughQuantityOfExistingProductThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice, InvalidQuantityOfProduct, InsufficientQuantityOfProduct, NonExistingProduct, NonSellableExpiredProduct, NegativeSellingPrice {
        Market market = new Market(10, 5, 3, 10);

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 7, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 7, 10));

        market.addDeliveredProduct(product1, 3);
        market.addDeliveredProduct(product2, 3);

        market.isAvailable("Apple", 5);
    }

    @Test
    public void testSellExistingProductWithAllAvailableQuantity() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice, InvalidQuantityOfProduct, InsufficientQuantityOfProduct, NonExistingProduct, NonSellableExpiredProduct, NegativeSellingPrice {
        Market market = new Market(10, 5, 3, 10);

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 7, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 7, 10));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 7, 10));

        market.addDeliveredProduct(product1, 3);
        market.addDeliveredProduct(product2, 3);
        market.addDeliveredProduct(product3, 3);

        market.decreaseProductQuantity(product1, 3);

        Assert.assertFalse(market.getProductsInStock().containsKey(product1));
    }

    @Test
    public void testSellExistingProductWithEnoughQuantity() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice, InvalidQuantityOfProduct, InsufficientQuantityOfProduct, NonExistingProduct, NonSellableExpiredProduct, NegativeSellingPrice {
        Market market = new Market(10, 5, 3, 10);

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 7, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 7, 10));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 7, 10));

        market.addDeliveredProduct(product1, 3);
        market.addDeliveredProduct(product2, 3);
        market.addDeliveredProduct(product3, 3);

        market.decreaseProductQuantity(product1, 1);

        Assert.assertTrue(market.getProductsInStock().containsKey(product1));
        Assert.assertEquals(2, (int) market.getProductsInStock().get(product1));
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

    @Test
    public void testCalculateProfit() {

    }

    @Test
    public void testCalculateCurrentIncome() {

    }

}
