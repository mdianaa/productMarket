package productMarketTests;

import org.junit.Assert;
import org.junit.Test;
import org.productMarket.enums.ProductCategory;
import org.productMarket.exceptions.NegativeDeliveryPrice;
import org.productMarket.exceptions.NegativeSellingPrice;
import org.productMarket.exceptions.NonSellableExpiredProduct;
import org.productMarket.products.EdibleProduct;
import org.productMarket.products.NonEdibleProduct;
import org.productMarket.products.Product;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductTest {

    @Test
    public void testCreateEdibleProductWithValidValues() throws NegativeDeliveryPrice {
        Product product = null;
        String error = null;
        try {
            product = new EdibleProduct("Banana", BigDecimal.valueOf(12.7), LocalDate.of(2023, 5, 30));
        } catch (NullPointerException e) {
            error = e.getMessage();
        }

        Assert.assertNull(error);
        Assert.assertNotNull(product);
        Assert.assertEquals("Banana", product.getName());
        Assert.assertEquals(BigDecimal.valueOf(12.7), product.getDeliveryPrice());
        Assert.assertEquals(LocalDate.of(2023, 5, 30), product.getDateOfExpiry());
        Assert.assertEquals(ProductCategory.EDIBLE, product.getType());
    }

    @Test
    public void testSetNameToEdibleProductWithNullThrows() throws NegativeDeliveryPrice {
        String error = null;
        try {
            new EdibleProduct(null, BigDecimal.valueOf(12.7), LocalDate.of(2023, 5, 30));
        } catch (NullPointerException e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Name cannot be null, blank or white space!", error);
    }

    @Test
    public void testSetNameToEdibleProductWithBlankSpaceThrows() throws NegativeDeliveryPrice {
        String error = null;
        try {
            new EdibleProduct("", BigDecimal.valueOf(12.7), LocalDate.of(2023, 5, 30));
        } catch (NullPointerException e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Name cannot be null, blank or white space!", error);
    }

    @Test
    public void testSetNameToEdibleProductWithWhiteSpaceThrows() throws NegativeDeliveryPrice {
        String error = null;
        try {
            new EdibleProduct(" ", BigDecimal.valueOf(12.7), LocalDate.of(2023, 5, 30));
        } catch (NullPointerException e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Name cannot be null, blank or white space!", error);
    }

    @Test
    public void testSetDeliveryPriceToEdibleProductWithNegativeNumberThrows() {
        String error = null;
        try {
            new EdibleProduct("Banana", BigDecimal.valueOf(-12.7), LocalDate.of(2023, 5, 30));
        } catch (NegativeDeliveryPrice e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Delivery price cannot be zero or negative number!", error);
    }

    @Test
    public void testSetDeliveryPriceToEdibleProductWithZeroThrows() {
        String error = null;
        try {
            new EdibleProduct("Banana", BigDecimal.valueOf(0), LocalDate.of(2023, 5, 30));
        } catch (NegativeDeliveryPrice e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Delivery price cannot be zero or negative number!", error);
    }

    @Test
    public void testSetSellingPriceToEdibleProduct() throws NegativeDeliveryPrice {
        Product product = new EdibleProduct("Banana", BigDecimal.valueOf(10), LocalDate.of(2023, 5, 30));
        String error = null;

        try {
            product.setSellingPrice(BigDecimal.valueOf(20));
        } catch (NegativeSellingPrice e) {
            error = e.getMessage();
        }

        Assert.assertNull(error);
        Assert.assertEquals(BigDecimal. valueOf(20), product.getSellingPrice());
    }

    @Test
    public void testSetSellingPriceToEdibleProductWithZerThrows() throws NegativeDeliveryPrice {
        Product product = new EdibleProduct("Banana", BigDecimal.valueOf(10), LocalDate.of(2023, 5, 30));
        String error = null;

        try {
            product.setSellingPrice(BigDecimal.valueOf(0));
        } catch (NegativeSellingPrice e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Selling price cannot be zero or negative number!", error);
    }

    @Test
    public void testSetSellingPriceToEdibleProductNegativeNumberThrows() throws NegativeDeliveryPrice {
        Product product = new EdibleProduct("Banana", BigDecimal.valueOf(10), LocalDate.of(2023, 5, 30));
        String error = null;

        try {
            product.setSellingPrice(BigDecimal.valueOf(-2));
        } catch (NegativeSellingPrice e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Selling price cannot be zero or negative number!", error);
    }

    @Test
    public void testSetExpiryDateToEdibleProductNegativeNumberThrows() throws NegativeDeliveryPrice {
        String error = null;
        try {
            new EdibleProduct("Banana", BigDecimal.valueOf(10), null);
        } catch (NullPointerException e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Date of expiry cannot be null for edible products!", error);
    }

    @Test
    public void testIsExpiredToEdibleProductWhenProductIsNotExpired() throws NegativeDeliveryPrice {
        Product product = new EdibleProduct("Banana", BigDecimal.valueOf(10), LocalDate.of(2023, 7, 30));

        String error = null;
        try {
            product.isExpired();
        } catch (NonSellableExpiredProduct e) {
            error = e.getMessage();
        }

        Assert.assertNull(error);
    }

    @Test
    public void testIsExpiredToEdibleProductWhenProductIsExpired() throws NegativeDeliveryPrice {
        Product product = new EdibleProduct("Banana", BigDecimal.valueOf(10), LocalDate.of(2023, 5, 16));

        String error = null;
        try {
            product.isExpired();
        } catch (NonSellableExpiredProduct e) {
            error = e.getMessage();
        }

        Assert.assertEquals("This product is expired with 10 days!", error);
    }

    @Test
    public void testCreateNonEdibleProductWithValidValues() throws NegativeDeliveryPrice {
        Product product = null;
        String error = null;
        try {
            product = new NonEdibleProduct("Eye cream", BigDecimal.valueOf(5.0), LocalDate.of(2023, 7, 16));
        } catch (NullPointerException e) {
            error = e.getMessage();
        }

        Assert.assertNull(error);
        Assert.assertNotNull(product);
        Assert.assertEquals("Eye cream", product.getName());
        Assert.assertEquals(BigDecimal.valueOf(5.0), product.getDeliveryPrice());
        Assert.assertEquals(ProductCategory.NON_EDIBLE, product.getType());
    }

    @Test
    public void testSetNameToNonEdibleProductWithNullThrows() throws NegativeDeliveryPrice {
        String error = null;
        try {
            new NonEdibleProduct(null, BigDecimal.valueOf(5.0), LocalDate.of(2023, 7, 16));
        } catch (NullPointerException e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Name cannot be null, blank or white space!", error);
    }

    @Test
    public void testSetNameToNonEdibleProductWithBlankSpaceThrows() throws NegativeDeliveryPrice {
        String error = null;
        try {
            new NonEdibleProduct("", BigDecimal.valueOf(5.0), LocalDate.of(2023, 7, 16));
        } catch (NullPointerException e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Name cannot be null, blank or white space!", error);
    }

    @Test
    public void testSetNameToNonEdibleProductWithWhiteSpaceThrows() throws NegativeDeliveryPrice {
        String error = null;
        try {
            new NonEdibleProduct(" ", BigDecimal.valueOf(5.0), LocalDate.of(2023, 7, 16));
        } catch (NullPointerException e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Name cannot be null, blank or white space!", error);
    }

    @Test
    public void testSetDeliveryPriceToNonEdibleProductWithNegativeNumberThrows() {
        String error = null;
        try {
            new NonEdibleProduct("Eye cream", BigDecimal.valueOf(-2), LocalDate.of(2023, 7, 16));
        } catch (NegativeDeliveryPrice e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Delivery price cannot be zero or negative number!", error);
    }

    @Test
    public void testSetDeliveryPriceToNonEdibleProductWithZeroThrows() {
        String error = null;
        try {
            new NonEdibleProduct("Eye cream", BigDecimal.valueOf(0), LocalDate.of(2023, 7, 16));
        } catch (NegativeDeliveryPrice e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Delivery price cannot be zero or negative number!", error);
    }

    @Test
    public void testSetSellingPriceToNonEdibleProduct() throws NegativeDeliveryPrice {
        Product product = new NonEdibleProduct("Eye cream", BigDecimal.valueOf(5), LocalDate.of(2023, 7, 16));
        String error = null;

        try {
            product.setSellingPrice(BigDecimal.valueOf(7));
        } catch (NegativeSellingPrice e) {
            error = e.getMessage();
        }

        Assert.assertNull(error);
    }

    @Test
    public void testSetSellingPriceToNonEdibleProductWithZeroThrows() throws NegativeDeliveryPrice {
        Product product = new NonEdibleProduct("Eye cream", BigDecimal.valueOf(5), LocalDate.of(2023, 7, 16));
        String error = null;

        try {
            product.setSellingPrice(BigDecimal.valueOf(0));
        } catch (NegativeSellingPrice e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Selling price cannot be zero or negative number!", error);
    }

    @Test
    public void testSetSellingPriceToNonEdibleProductWithNegativeNumberThrows() throws NegativeDeliveryPrice {
        Product product = new NonEdibleProduct("Eye cream", BigDecimal.valueOf(5), LocalDate.of(2023, 7, 16));
        String error = null;

        try {
            product.setSellingPrice(BigDecimal.valueOf(-4));
        } catch (NegativeSellingPrice e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Selling price cannot be zero or negative number!", error);
    }

    @Test
    public void testSetExpiryDateToNonEdibleProductNegativeNumberThrows() throws NegativeDeliveryPrice {
        String error = null;
        try {
            new NonEdibleProduct("Banana", BigDecimal.valueOf(10), null);
        } catch (NullPointerException e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Date of expiry cannot be null for edible products!", error);
    }

    @Test
    public void testIsExpiredToNonEdibleProductWithNoExpiryDate() throws NegativeDeliveryPrice {
        Product product = new NonEdibleProduct("Eye cream", BigDecimal.valueOf(5), LocalDate.of(2023, 7, 16));

        String error = null;
        try {
            product.isExpired();
        } catch (NonSellableExpiredProduct e) {
            error = e.getMessage();
        }

        Assert.assertNull(error);
    }
}
