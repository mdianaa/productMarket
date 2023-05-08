package productMarketTests;

import org.junit.Assert;
import org.junit.Test;
import org.productMarket.enums.ProductCategory;
import org.productMarket.exceptions.NegativeDeliveryPrice;
import org.productMarket.exceptions.NegativeSellingPrice;
import org.productMarket.products.EdibleProduct;
import org.productMarket.products.NonEdibleProduct;
import org.productMarket.products.Product;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductTest {

    @Test
    public void testCreateEdibleProductWithValidValues() throws NegativeDeliveryPrice {
        Product product = new EdibleProduct("Banana", BigDecimal.valueOf(12.7), LocalDate.of(2023, 5, 30));

        Assert.assertEquals("Banana", product.getName());
        Assert.assertEquals(BigDecimal.valueOf(12.7), product.getDeliveryPrice());
        Assert.assertEquals(LocalDate.of(2023, 5, 30), product.getDateOfExpiry());
        Assert.assertEquals(ProductCategory.EDIBLE, product.getType());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNameToEdibleProductWithNullThrows() throws NegativeDeliveryPrice {
       new EdibleProduct(null, BigDecimal.valueOf(12.7), LocalDate.of(2023, 5, 30));
    }

    @Test(expected = NullPointerException.class)
    public void testSetNameToEdibleProductWithBlankSpaceThrows() throws NegativeDeliveryPrice {
        new EdibleProduct("", BigDecimal.valueOf(12.7), LocalDate.of(2023, 5, 30));
    }

    @Test(expected = NullPointerException.class)
    public void testSetNameToEdibleProductWithWhiteSpaceThrows() throws NegativeDeliveryPrice {
        new EdibleProduct(" ", BigDecimal.valueOf(12.7), LocalDate.of(2023, 5, 30));
    }

    @Test(expected = NegativeDeliveryPrice.class)
    public void testSetDeliveryPriceToEdibleProductWithNegativeNumberThrows() throws NegativeDeliveryPrice {
        new EdibleProduct("Banana", BigDecimal.valueOf(-12.7), LocalDate.of(2023, 5, 30));
    }

    @Test(expected = NegativeDeliveryPrice.class)
    public void testSetDeliveryPriceToEdibleProductWithZeroThrows() throws NegativeDeliveryPrice {
        new EdibleProduct("Banana", BigDecimal.valueOf(0), LocalDate.of(2023, 5, 30));
    }

    @Test
    public void testSetSellingPriceToEdibleProduct() throws NegativeDeliveryPrice, NegativeSellingPrice {
        Product product = new EdibleProduct("Banana", BigDecimal.valueOf(10), LocalDate.of(2023, 5, 30));

        product.setSellingPrice(BigDecimal.valueOf(20));

        Assert.assertEquals(BigDecimal. valueOf(20), product.getSellingPrice());
    }

    @Test(expected = NegativeSellingPrice.class)
    public void testSetSellingPriceToEdibleProductWithZerThrows() throws NegativeDeliveryPrice, NegativeSellingPrice {
        Product product = new EdibleProduct("Banana", BigDecimal.valueOf(10), LocalDate.of(2023, 5, 30));

        product.setSellingPrice(BigDecimal.valueOf(0));
    }

    @Test(expected = NegativeSellingPrice.class)
    public void testSetSellingPriceToEdibleProductNegativeNumberThrows() throws NegativeDeliveryPrice, NegativeSellingPrice {
        Product product = new EdibleProduct("Banana", BigDecimal.valueOf(10), LocalDate.of(2023, 5, 30));

        product.setSellingPrice(BigDecimal.valueOf(-2));
    }

    @Test(expected = NullPointerException.class)
    public void testSetExpiryDateToEdibleProductNegativeNumberThrows() throws NegativeDeliveryPrice, NegativeSellingPrice {
        new EdibleProduct("Banana", BigDecimal.valueOf(10), null);
    }

    @Test
    public void testCreateNonEdibleProductWithValidValues() throws NegativeDeliveryPrice {
        Product product = new NonEdibleProduct("Bag", BigDecimal.valueOf(5.0));

        Assert.assertEquals("Bag", product.getName());
        Assert.assertEquals(BigDecimal.valueOf(5.0), product.getDeliveryPrice());
        Assert.assertEquals(ProductCategory.NON_EDIBLE, product.getType());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNameToNonEdibleProductWithNullThrows() throws NegativeDeliveryPrice {
        new NonEdibleProduct(null, BigDecimal.valueOf(5.0));
    }

    @Test(expected = NullPointerException.class)
    public void testSetNameToNonEdibleProductWithBlankSpaceThrows() throws NegativeDeliveryPrice {
        new NonEdibleProduct("", BigDecimal.valueOf(5.0));
    }

    @Test(expected = NullPointerException.class)
    public void testSetNameToNonEdibleProductWithWhiteSpaceThrows() throws NegativeDeliveryPrice {
        new NonEdibleProduct(" ", BigDecimal.valueOf(5.0));
    }

    @Test(expected = NegativeDeliveryPrice.class)
    public void testSetDeliveryPriceToNonEdibleProductWithNegativeNumberThrows() throws NegativeDeliveryPrice {
        new NonEdibleProduct("Bag", BigDecimal.valueOf(-5.0));
    }

    @Test(expected = NegativeDeliveryPrice.class)
    public void testSetDeliveryPriceToNonEdibleProductWithZeroThrows() throws NegativeDeliveryPrice {
        new NonEdibleProduct("Bag", BigDecimal.valueOf(0));
    }

    @Test
    public void testSetSellingPriceToNonEdibleProduct() throws NegativeDeliveryPrice, NegativeSellingPrice {
        Product product = new NonEdibleProduct("Bag", BigDecimal.valueOf(5));

        product.setSellingPrice(BigDecimal.valueOf(7));

        Assert.assertEquals(BigDecimal.valueOf(7), product.getSellingPrice());
    }

    @Test(expected = NegativeSellingPrice.class)
    public void testSetSellingPriceToNonEdibleProductWithZeroThrows() throws NegativeDeliveryPrice, NegativeSellingPrice {
        Product product = new NonEdibleProduct("Bag", BigDecimal.valueOf(5));

        product.setSellingPrice(BigDecimal.valueOf(0));
    }

    @Test(expected = NegativeSellingPrice.class)
    public void testSetSellingPriceToNonEdibleProductWithNegativeNumberThrows() throws NegativeDeliveryPrice, NegativeSellingPrice {
        Product product = new NonEdibleProduct("Bag", BigDecimal.valueOf(5));

        product.setSellingPrice(BigDecimal.valueOf(-4));
    }
}
