package com.vinitthada.test;

import com.vinitthada.app.exception.DuplicateIngredientException;
import com.vinitthada.app.exception.IllegalIngredientsException;
import com.vinitthada.app.exception.InvalidOrderException;
import com.vinitthada.app.factory.BeverageFactory;

import org.junit.Assert;
import org.junit.Test;

public class BeverageAppTest {


    BeverageFactory factory = new BeverageFactory();

    @Test(expected = InvalidOrderException.class)
    public void testForBlankOrder() {
        String order = "";
        Assert.assertEquals(0.0d, factory.getInvoiceFromOrder(order), 0.0d);
    }

    @Test
    public void testForNormalOrder1() {
        Assert.assertEquals(2.5d, factory.getInvoiceFromOrder("Chai,-milk,-water"), 0.0d);
    }

    @Test()
    public void testForNormalOrder2() {
        String order = "Chai,        -milk,     -water, Mojito,-mint, Banana Smoothie, Strawberry Shake";
        Assert.assertEquals(22.5d, factory.getInvoiceFromOrder(order), 0.0d);
    }


    @Test
    public void testForOrderWithNoExclusions() {
        String order = "Chai, Coffee";
        Assert.assertEquals(9.0d, factory.getInvoiceFromOrder(order), 0.0d);
    }

    @Test(expected = InvalidOrderException.class)
    public void testForOrderWithAllExclusions() {
        String order = "Coffee,     -milk,-sugar, -water";
        Assert.assertEquals(0.0d, factory.getInvoiceFromOrder(order), 0.0d);
    }


    @Test(expected = InvalidOrderException.class)
    public void testOrderWithInvalidOrder() {
        String order = "paani,tee";
        Assert.assertEquals(0.0d, factory.getInvoiceFromOrder(order), 0.0d);
    }

    @Test(expected = DuplicateIngredientException.class)
    public void testOrderWithDuplicateIngredients() {
        String order = "Chai,  -water,-water, Coffee, Mojito";
        Assert.assertEquals(0.0d, factory.getInvoiceFromOrder(order), 0.0d);

    }

    @Test(expected = IllegalIngredientsException.class)
    public void testIllegalIngredientInOrder() {
        String order = "Chai,-money,-water, Coffee, Mojito";
        Assert.assertEquals(0.0d, factory.getInvoiceFromOrder(order), 0.0d);
    }

}