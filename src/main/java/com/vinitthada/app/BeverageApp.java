package com.vinitthada.app;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vinitthada.app.factory.BeverageFactory;

public class BeverageApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeverageApp.class);

    public static void main(String[] args) {

        BeverageFactory factory = new BeverageFactory();

        //Modify the below string and run the program to calculate your order cost
        //String order = " Chai ,-milk, -water";
        //String order =  "Coffee";
        String order =  "Mojito,-soda";
        //String order =  "Banana smoothie";
        //String order =  "Strawberry shake";

        final double cost = factory.getInvoiceFromOrder(order);

        LOGGER.info("Your total cost is ${}", cost);
    }
}
