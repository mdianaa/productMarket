package productMarketTests;

import org.junit.Assert;
import org.junit.Test;
import org.productMarket.customers.Customer;
import org.productMarket.exceptions.NegativeDeliveryPrice;
import org.productMarket.exceptions.NegativeSellingPrice;
import org.productMarket.exceptions.NegativeStackOfMoney;
import org.productMarket.products.EdibleProduct;
import org.productMarket.products.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CustomerTest {

    @Test
    public void testCreateCustomerWithValidValues() {
        Customer customer = null;
        String error = null;

        try {
            customer = new Customer("Peter", BigDecimal.valueOf(1000));
        } catch (NullPointerException | NegativeStackOfMoney e) {
            error = e.getMessage();
        }

        Assert.assertNull(error);
        Assert.assertNotNull(customer);
        Assert.assertEquals("Peter", customer.getName());
        Assert.assertEquals(BigDecimal.valueOf(1000), customer.getMoney());
    }

    @Test
    public void testSetNameWithNullThrows() throws NegativeStackOfMoney {
        String error = null;

        try {
            new Customer(null, BigDecimal.valueOf(1000));
        } catch (NullPointerException e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Name cannot be null, blank space or whitespace!", error);
    }

    @Test
    public void testSetNameWithEmptySpaceThrows() throws NegativeStackOfMoney {
        String error = null;

        try {
            new Customer("", BigDecimal.valueOf(1000));
        } catch (NullPointerException e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Name cannot be null, blank space or whitespace!", error);
    }

    @Test
    public void testSetNameWithWhiteSpaceThrows() throws NegativeStackOfMoney {
        String error = null;

        try {
            new Customer(" ", BigDecimal.valueOf(1000));
        } catch (NullPointerException e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Name cannot be null, blank space or whitespace!", error);
    }

    @Test
    public void testSetMoneyStackWithNegativeNumberThrows() {
        String error = null;

        try {
            new Customer("Peter", BigDecimal.valueOf(-1000));
        } catch (NegativeStackOfMoney e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Money cannot be zero or negative number!", error);
    }

    @Test
    public void testSetMoneyStackWithZeroThrows() {
        String error = null;

        try {
            new Customer("Peter", BigDecimal.valueOf(0));
        } catch (NegativeStackOfMoney e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Money cannot be zero or negative number!", error);
    }

    @Test
    public void testCanBuyProductWithValidValuesReturnsTrue() throws NegativeStackOfMoney, NegativeDeliveryPrice, NegativeSellingPrice {
        Customer customer = new Customer("Peter", BigDecimal.valueOf(1000));
        Product product = new EdibleProduct("Banana", BigDecimal.valueOf(12.7), LocalDate.of(2023, 5, 30));

        product.setSellingPrice(BigDecimal.valueOf(30));

        Assert.assertTrue(customer.canBuyProduct(product, 3));
    }

    @Test
    public void testCanBuyProductWithNotEnoughMoneyReturnsFalse() throws NegativeStackOfMoney, NegativeDeliveryPrice, NegativeSellingPrice {
        Customer customer = new Customer("Peter", BigDecimal.valueOf(50));
        Product product = new EdibleProduct("Banana", BigDecimal.valueOf(12.7), LocalDate.of(2023, 5, 30));
        product.setSellingPrice(BigDecimal.valueOf(30));

        Assert.assertFalse(customer.canBuyProduct(product, 3));
    }

    @Test
    public void testDecreaseMoneyWithTwoProducts() throws NegativeStackOfMoney, NegativeDeliveryPrice, NegativeSellingPrice {
        Customer customer = new Customer("Peter", BigDecimal.valueOf(50));
        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 5, 30));
        Product product2 = new EdibleProduct("Strawberry", BigDecimal.valueOf(4), LocalDate.of(2023, 5, 30));
        product1.setSellingPrice(BigDecimal.valueOf(5));
        product2.setSellingPrice(BigDecimal.valueOf(6));
        Map<Product, Integer> shoppingList = new HashMap<>();
        shoppingList.put(product1, 4);
        shoppingList.put(product2, 3);

        for (Map.Entry<Product, Integer> product : shoppingList.entrySet()) {
            customer.decreaseMoney(product.getKey(), product.getValue());
        }

        Assert.assertEquals(BigDecimal. valueOf(12), customer.getMoney());
    }

}
