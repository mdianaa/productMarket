package productMarketTests;

import org.junit.Assert;
import org.junit.Test;
import org.productMarket.cashiers.Cashier;
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
    public void testCreateMarketWithValidValues() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue {
        Market market = new Market("BMB", 10, 5, 3, 13);

        Assert.assertEquals("BMB", market.getName());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNameWithNullThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue {
        new Market(null, 10, 5, 3, 13);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNameWithBlankSpaceThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue {
        new Market("", 10, 5, 3, 13);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNameWithWhiteSpaceThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue {
        new Market(" ", 10, 5, 3, 13);
    }

    @Test(expected = NegativeProductMarkup.class)
    public void testSetEdibleProductsMarkupWithNegativeValueThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue {
        new Market("BMB", -2, 5, 3, 13);
    }

    @Test(expected = NegativeProductMarkup.class)
    public void testSetEdibleProductsMarkupWithZeroThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue {
        new Market("BMB", 0, 5, 3, 13);
    }

    @Test(expected = NegativeProductMarkup.class)
    public void testSetNonEdibleProductsMarkupWithNegativeValueThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue {
        new Market("BMB", 10, -2, 3, 13);
    }

    @Test(expected = NegativeProductMarkup.class)
    public void testSetNonEdibleProductsMarkupWithZeroThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue {
        new Market("BMB", 10, 0, 3, 13);
    }

    @Test(expected = NegativeDays.class)
    public void testSetDaysLeftTillExpiryWithNegativeValueThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue {
        new Market("BMB", 10, 5, -2, 13);
    }

    @Test
    public void testSetDaysLeftTillExpiryWithZero() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue {
        Market market = new Market("BMB", 10, 5, 0, 13);

        Assert.assertEquals(0, market.getDaysLeftTillExpiry());
    }

    @Test(expected = NegativeDiscountValue.class)
    public void testSetDiscountWithNegativeNumberThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue {
        new Market("BMB", 10, 5, 3, -10);
    }

    @Test(expected = NegativeDiscountValue.class)
    public void testSetDiscountWithZeroThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue {
        new Market("BMB", 10, 5, 3, 0);
    }

    @Test
    public void testAddDeliveredProductWithValidQuantity() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice, InvalidQuantityOfProduct {
        Market market = new Market("BMB", 10, 5, 3, 10);

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 5, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 5, 10));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 5, 10));

        market.addDeliveredProduct(product1, 3);
        market.addDeliveredProduct(product2, 3);
        market.addDeliveredProduct(product3, 3);

        Assert.assertEquals(3, market.getDeliveredProducts().size());
        Assert.assertEquals(3, market.getProductsInStock().size());
    }

    @Test(expected = InvalidQuantityOfProduct.class)
    public void testAddDeliveredProductWithNegativeQuantityNumberThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice, InvalidQuantityOfProduct {
        Market market = new Market("BMB", 10, 5, 3, 10);

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 5, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 5, 10));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 5, 10));

        market.addDeliveredProduct(product1, -3);
        market.addDeliveredProduct(product2, 3);
        market.addDeliveredProduct(product3, 3);
    }

    @Test(expected = InvalidQuantityOfProduct.class)
    public void testAddDeliveredProductWithZeroQuantityNumberThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice, InvalidQuantityOfProduct {
        Market market = new Market("BMB", 10, 5, 3, 10);

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 5, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 5, 10));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 5, 10));

        market.addDeliveredProduct(product1, 0);
        market.addDeliveredProduct(product2, 3);
        market.addDeliveredProduct(product3, 3);
    }

    @Test
    public void testIsForDiscountWithDaysLessThanDaysLeftTillDiscount() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice {
        Market market = new Market("BMB", 10, 5, 3, 10);
        Product product = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 5, 10));

        Assert.assertTrue(market.isForDiscount(product));
    }

    @Test
    public void testIsForDiscountWithZeroDaysLeftTillExpiry() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice {
        Market market = new Market("BMB", 10, 5, 3, 10);
        Product product = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 5, 8));

        Assert.assertTrue(market.isForDiscount(product));
    }

    @Test
    public void testIsForDiscountWithDaysMoreThanDaysLeftTillExpiry() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice {
        Market market = new Market("BMB", 10, 5, 3, 10);
        Product product = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 5, 30));

        Assert.assertFalse(market.isForDiscount(product));
    }

    @Test
    public void testIsEnoughQuantityReturnsTrue() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice, InvalidQuantityOfProduct, InsufficientQuantityOfProduct, NonExistingProduct {
        Market market = new Market("BMB", 10, 5, 3, 10);

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 5, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 5, 10));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 5, 10));

        market.addDeliveredProduct(product1, 3);
        market.addDeliveredProduct(product2, 3);
        market.addDeliveredProduct(product3, 3);

        Assert.assertTrue(market.isEnoughQuantity(product1, 3));
    }

    @Test(expected = NonExistingProduct.class)
    public void testIsEnoughQuantityWithNonExistingProductThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice, InvalidQuantityOfProduct, InsufficientQuantityOfProduct, NonExistingProduct {
        Market market = new Market("BMB", 10, 5, 3, 10);

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 5, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 5, 10));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 5, 10));

        market.addDeliveredProduct(product1, 3);
        market.addDeliveredProduct(product2, 3);

        market.isEnoughQuantity(product3, 3);
    }

    @Test(expected = InsufficientQuantityOfProduct.class)
    public void testIsEnoughQuantityWithInsufficientAmountThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice, InvalidQuantityOfProduct, InsufficientQuantityOfProduct, NonExistingProduct {
        Market market = new Market("BMB", 10, 5, 3, 10);

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 5, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 5, 10));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 5, 10));

        market.addDeliveredProduct(product1, 1);
        market.addDeliveredProduct(product2, 3);
        market.addDeliveredProduct(product3, 3);

        market.isEnoughQuantity(product1, 3);
    }

    @Test
    public void testIsAvailableReturnsTrue() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice, InvalidQuantityOfProduct, InsufficientQuantityOfProduct, NonExistingProduct {
        Market market = new Market("BMB", 10, 5, 3, 10);

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 5, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 5, 10));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 5, 10));

        market.addDeliveredProduct(product1, 3);
        market.addDeliveredProduct(product2, 3);
        market.addDeliveredProduct(product3, 3);

        Assert.assertTrue(market.isAvailable(product1));
        Assert.assertTrue(market.isAvailable(product2));
        Assert.assertTrue(market.isAvailable(product3));
    }

    @Test(expected = NonExistingProduct.class)
    public void testIsAvailableWithNonExistingProductThrows() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice, InvalidQuantityOfProduct, InsufficientQuantityOfProduct, NonExistingProduct {
        Market market = new Market("BMB", 10, 5, 3, 10);

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 5, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 5, 10));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 5, 10));

        market.addDeliveredProduct(product1, 3);
        market.addDeliveredProduct(product2, 3);

        market.isAvailable(product3);
    }

    @Test
    public void testSellExistingProductWithAllAvailableQuantity() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice, InvalidQuantityOfProduct, InsufficientQuantityOfProduct, NonExistingProduct {
        Market market = new Market("BMB", 10, 5, 3, 10);

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 5, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 5, 10));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 5, 10));

        market.addDeliveredProduct(product1, 3);
        market.addDeliveredProduct(product2, 3);
        market.addDeliveredProduct(product3, 3);

        market.sellProduct(product1, 3);

        Assert.assertFalse(market.getProductsInStock().containsKey(product1));
        Assert.assertTrue(market.getSoldProducts().containsKey(product1));
        Assert.assertEquals(3, (int) market.getSoldProducts().get(product1));
    }

    @Test
    public void testSellExistingProductWithEnoughQuantity() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice, InvalidQuantityOfProduct, InsufficientQuantityOfProduct, NonExistingProduct {
        Market market = new Market("BMB", 10, 5, 3, 10);

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 5, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 5, 10));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 5, 10));

        market.addDeliveredProduct(product1, 3);
        market.addDeliveredProduct(product2, 3);
        market.addDeliveredProduct(product3, 3);

        market.sellProduct(product1, 1);

        Assert.assertTrue(market.getProductsInStock().containsKey(product1));
        Assert.assertEquals(2, (int) market.getProductsInStock().get(product1));
        Assert.assertTrue(market.getSoldProducts().containsKey(product1));
        Assert.assertEquals(1, (int) market.getSoldProducts().get(product1));
    }

    @Test
    public void testAddCashier() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue {
        Market market = new Market("BMB", 10, 5, 3, 10);
        Cashier cashier = new Cashier("Peter", SalaryCategory.MEDIUM_POSITION);

        market.addCashier(cashier);

        Assert.assertTrue(market.getCashiers().contains(cashier));
    }

    @Test
    public void testAddReceipt() throws NegativeDays, NegativeProductMarkup, NegativeDiscountValue, NegativeDeliveryPrice {
        Market market = new Market("BMB", 10, 5, 3, 10);
        Cashier cashier = new Cashier("Peter", SalaryCategory.MEDIUM_POSITION);

        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 5, 10));
        Product product2 = new EdibleProduct("Apple", BigDecimal.valueOf(4), LocalDate.of(2023, 5, 10));
        Product product3 = new EdibleProduct("Bread", BigDecimal.valueOf(1), LocalDate.of(2023, 5, 10));

        Map<Product, Integer> products = new HashMap<>();
        products.put(product1, 3);
        products.put(product2, 3);
        products.put(product3, 3);

        Receipt receipt = new Receipt(cashier, LocalDate.now(), products, BigDecimal.valueOf(24));

        market.addReceipt(receipt);

        Assert.assertTrue(market.getReceipts().contains(receipt));
    }

    //TODO
    // tests for expenses, income, profit !!!!!
    // tests for defining selling price and price with discount !!!

}
