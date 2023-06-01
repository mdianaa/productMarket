package productMarketTests;

import org.junit.Assert;
import org.junit.Test;
import org.productMarket.cashiers.Cashier;

import java.math.BigDecimal;

public class CashierTest {

    @Test
    public void testSetNameWithValidValues() {
        Cashier cashier = null;
        String error = null;

        try {
            cashier = new Cashier("Peter", BigDecimal.valueOf(1000));
        } catch (NullPointerException e) {
            error = e.getMessage();
        }

        Assert.assertNull(error);
        Assert.assertNotNull(cashier);
        Assert.assertEquals("Peter", cashier.getName());
    }

    @Test
    public void testSetNameWithBlankSpaceThrows() {
        String error = null;

        try {
            new Cashier("", BigDecimal.valueOf(1000));
        } catch (NullPointerException e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Name cannot be null, blank or white space!", error);
    }

    @Test
    public void testSetNameWithNullThrows() {
        String error = null;

        try {
            new Cashier(null, BigDecimal.valueOf(1000));
        } catch (NullPointerException e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Name cannot be null, blank or white space!", error);
    }

    @Test
    public void testSetNameWithWhiteSpaceThrows() {
        String error = null;

        try {
            new Cashier(" ", BigDecimal.valueOf(1000));
        } catch (NullPointerException e) {
            error = e.getMessage();
        }

        Assert.assertEquals("Name cannot be null, blank or white space!", error);
    }
}
