package productMarketTests;

import org.junit.Assert;
import org.junit.Test;
import org.productMarket.customers.Customer;
import org.productMarket.exceptions.NegativeDeliveryPrice;
import org.productMarket.exceptions.NegativeSellingPrice;
import org.productMarket.exceptions.NegativeStackOfMoney;
import org.productMarket.exceptions.NotEnoughMoneyToBuyProduct;
import org.productMarket.products.EdibleProduct;
import org.productMarket.products.Product;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CustomerTest {

    @Test
    public void testCreateCustomerWithValidValues() throws NegativeStackOfMoney {
        Customer customer = new Customer("Peter", BigDecimal.valueOf(1000));

        Assert.assertEquals("Peter", customer.getName());
        Assert.assertEquals(BigDecimal.valueOf(1000), customer.getMoney());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNameWithNullThrows() throws NegativeStackOfMoney {
        Customer customer = new Customer(null, BigDecimal.valueOf(1000));
    }

    @Test(expected = NullPointerException.class)
    public void testSetNameWithEmptySpaceThrows() throws NegativeStackOfMoney {
        Customer customer = new Customer("", BigDecimal.valueOf(1000));
    }

    @Test(expected = NullPointerException.class)
    public void testSetNameWithWhiteSpaceThrows() throws NegativeStackOfMoney {
        Customer customer = new Customer(" ", BigDecimal.valueOf(1000));
    }

    @Test(expected = NegativeStackOfMoney.class)
    public void testSetMoneyStackWithNegativeNumberThrows() throws NegativeStackOfMoney {
        Customer customer = new Customer("Peter", BigDecimal.valueOf(-1000));
    }

    @Test(expected = NegativeStackOfMoney.class)
    public void testSetMoneyStackWithZeroThrows() throws NegativeStackOfMoney {
        Customer customer = new Customer("Peter", BigDecimal.valueOf(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetBoughtProductsAddNewProductThrows() throws NegativeStackOfMoney, NegativeDeliveryPrice {
        Customer customer = new Customer("Peter", BigDecimal.valueOf(1000));
        customer.getBoughtProducts().put(new EdibleProduct("Banana", BigDecimal.valueOf(12.7), LocalDate.of(2023, 5, 30)), 3);
    }

    @Test
    public void testCanBuyProductWithValidValues() throws NegativeStackOfMoney, NegativeDeliveryPrice, NotEnoughMoneyToBuyProduct, NegativeSellingPrice {
        Customer customer = new Customer("Peter", BigDecimal.valueOf(1000));
        Product product = new EdibleProduct("Banana", BigDecimal.valueOf(12.7), LocalDate.of(2023, 5, 30));
        product.setSellingPrice(BigDecimal.valueOf(30));

        Assert.assertTrue(customer.canBuyProduct(product, 3));
    }

    @Test(expected = NotEnoughMoneyToBuyProduct.class)
    public void testCanBuyProductWithNotEnoughMoneyThrows() throws NegativeStackOfMoney, NegativeDeliveryPrice, NotEnoughMoneyToBuyProduct, NegativeSellingPrice {
        Customer customer = new Customer("Peter", BigDecimal.valueOf(50));
        Product product = new EdibleProduct("Banana", BigDecimal.valueOf(12.7), LocalDate.of(2023, 5, 30));
        product.setSellingPrice(BigDecimal.valueOf(30));

        customer.canBuyProduct(product, 3);
    }

    @Test
    public void testBuyProductWithValidValues() throws NegativeStackOfMoney, NegativeDeliveryPrice, NotEnoughMoneyToBuyProduct, NegativeSellingPrice {
        Customer customer = new Customer("Peter", BigDecimal.valueOf(100));
        Product product = new EdibleProduct("Banana", BigDecimal.valueOf(12.7), LocalDate.of(2023, 5, 30));
        product.setSellingPrice(BigDecimal.valueOf(30));

        customer.buyProduct(product, 3);

        Assert.assertEquals(1, customer.getBoughtProducts().size());
        Assert.assertEquals(BigDecimal.valueOf(10), customer.getMoney());
    }


}
