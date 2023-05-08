package productMarketTests;

import org.junit.Assert;
import org.junit.Test;
import org.productMarket.cashiers.Cashier;
import org.productMarket.counters.CashDesk;
import org.productMarket.exceptions.*;

public class CashierTest {

    @Test
    public void testSetNameWithValidValues() {
        Cashier cashier = new Cashier("Peter", SalaryCategory.MEDIUM_POSITION);
        Assert.assertEquals("Peter", cashier.getName());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNameWithBlankSpace() {
        Cashier cashier = new Cashier("", SalaryCategory.MEDIUM_POSITION);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNameWithNull() {
        Cashier cashier = new Cashier(null, SalaryCategory.MEDIUM_POSITION);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNameWithWhiteSpace() {
        Cashier cashier = new Cashier(" ", SalaryCategory.MEDIUM_POSITION);
    }

    @Test
    public void testCanWorkOnCounterWhichIsNotOccupied() throws NotAbleToWorkOnSecondCounter {
        CashDesk cashDesk = new CashDesk();
        Cashier cashier = new Cashier("Peter", SalaryCategory.MEDIUM_POSITION);

        Assert.assertTrue(cashier.workOnCounter(cashDesk));
    }

    @Test(expected = NotAbleToWorkOnSecondCounter.class)
    public void testCanWorkOnCounterWhileWorkingOnAnotherCounter() throws NotAbleToWorkOnSecondCounter {
        CashDesk cashDesk1 = new CashDesk();
        CashDesk cashDesk2 = new CashDesk();
        Cashier cashier = new Cashier("Peter", SalaryCategory.MEDIUM_POSITION);

        cashier.workOnCounter(cashDesk1);
        cashier.workOnCounter(cashDesk2);
    }

    @Test
    public void testCanWorkOnCounterWhichIsAlreadyOccupied() throws NotAbleToWorkOnSecondCounter {
        CashDesk cashDesk = new CashDesk();
        Cashier cashier = new Cashier("Peter", SalaryCategory.MEDIUM_POSITION);

        cashDesk.setCashDeskOccupied(true);

        Assert.assertFalse(cashier.workOnCounter(cashDesk));
    }

    @Test
    public void testLeaveCurrentCounter() throws NotAbleToWorkOnSecondCounter {
        CashDesk cashDesk = new CashDesk();
        Cashier cashier = new Cashier("Peter", SalaryCategory.MEDIUM_POSITION);

        cashier.workOnCounter(cashDesk);
        cashier.leaveCurrentCounter();

        Assert.assertNull(cashier.getCurrentCounter());
        Assert.assertFalse(cashDesk.isOccupied());
    }

    //TODO
    // tests for scanProduct() !!!!!
    // tests for handInReceipt() !!!!
}
