package productMarketTests;

import org.junit.Assert;
import org.junit.Test;
import org.productMarket.cashiers.Cashier;
import org.productMarket.counters.CashDesk;
import org.productMarket.exceptions.NegativeDeliveryPrice;
import org.productMarket.exceptions.NegativeSellingPrice;
import org.productMarket.products.EdibleProduct;
import org.productMarket.products.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CashDeskTest {

    @Test
    public void testCreateCashDesk() {
        Cashier cashier = new Cashier("Ivan", BigDecimal.valueOf(1000));
        CashDesk cashDesk = new CashDesk(cashier);

        Assert.assertEquals(cashier, cashDesk.getCashier());
    }

    @Test
    public void testChangeCashierWhenCashDeskHasAlreadyOneCashier() {
        Cashier cashier1 = new Cashier("Ivan", BigDecimal.valueOf(1000));
        CashDesk cashDesk = new CashDesk(cashier1);

        Cashier cashier2 = new Cashier("Peter", BigDecimal.valueOf(2000));
        cashDesk.changeCashier(cashier2);

        Assert.assertEquals(cashier2, cashDesk.getCashier());
    }

    @Test
    public void testGenerateReceipt() throws NegativeDeliveryPrice, NegativeSellingPrice {
        Cashier cashier = new Cashier("Ivan", BigDecimal.valueOf(1000));
        CashDesk cashDesk = new CashDesk(cashier);
        Product product1 = new EdibleProduct("Banana", BigDecimal.valueOf(3), LocalDate.of(2023, 5, 30));
        Product product2 = new EdibleProduct("Strawberry", BigDecimal.valueOf(4), LocalDate.of(2023, 5, 30));
        product1.setSellingPrice(BigDecimal.valueOf(5));
        product2.setSellingPrice(BigDecimal.valueOf(6));
        Map<Product, Integer> shoppingList = new HashMap<>();
        shoppingList.put(product1, 4);
        shoppingList.put(product2, 3);

        Assert.assertEquals(cashDesk.getCashier(), cashDesk.generateReceipt(shoppingList).getCashier());
        Assert.assertTrue(cashDesk.generateReceipt(shoppingList).getProducts().containsKey(product1));
        Assert.assertTrue(cashDesk.generateReceipt(shoppingList).getProducts().containsKey(product2));
    }

}
