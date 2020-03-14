package com.vinitthada.app.factory;

import com.vinitthada.app.bootstrap.DataLoader;
import com.vinitthada.app.exception.DuplicateIngredientException;
import com.vinitthada.app.exception.IllegalIngredientsException;
import com.vinitthada.app.exception.InvalidOrderException;

import java.util.*;
import java.util.stream.Collectors;

public class BeverageFactory {

	//This stores the combination of Items with allowed exclusions
    Map<String, List<String>> beveragesMap = DataLoader.getBeveragesMap();

    //This stores the price of Items
    Map<String, Double> itemRates = DataLoader.getItemRates();

    //This stores the price of exclusions
    Map<String, Double> ingredientRates = DataLoader.getIngredientRates();

    //get cost of each item in order and total them
    public double getInvoiceFromOrder(String order) {
        double cost = 0.0d;
        List<String> orderItems = getItemsFromOrder(order.trim());
        for (String item : orderItems) {
            List<String> itemWithIngredients = getIngredientFromItem(item);
            checkIfValidOrder(item);
            cost = cost + calculateInvoice(itemWithIngredients);
        }
        return cost;
    }

    //if ordered item not present in DataLoader or empty order returns false
    private void checkIfValidOrder(String item) {
        List<String> itemIngredients = getIngredientFromItem(item);

        //case: if order does not contains valid menu item
        if (!beveragesMap.containsKey(itemIngredients.get(0)))
            throw new InvalidOrderException("Invalid item ordered -> " + item + " .Order again with valid menu item..!!");

        List<String> allIngredients = beveragesMap.get(itemIngredients.get(0)); // Beverage name is always at index 0
        // check to ensure items are there in list

        // case: when duplicate ingredients are present in order
        Optional<String> duplicateIngredient = itemIngredients.stream().filter(i -> Collections.frequency(itemIngredients, i) > 1).findFirst();
        if (duplicateIngredient.isPresent())
            throw new DuplicateIngredientException("Duplicate ingredient not allowed -> " + duplicateIngredient.get());

        List<String> ingredients = itemIngredients.subList(1, itemIngredients.size());
        boolean validIngredients = ingredients.stream().allMatch(t -> allIngredients.stream().anyMatch(t::contains));

        // it checks if an invalid ingredient is passed to the order
        if (!validIngredients)
            throw new IllegalIngredientsException("Invalid ingredient in order.Please check and order again..!!");

        //Order has no typo, now check if all ingredients are not excluded in an order
        //Logic is to compare the order items and { (total exclusions + 1) +1} for main including main item,
        if (itemIngredients.size() == allIngredients.size() + 1)
            throw new InvalidOrderException("Invalid Order..!! You cannot exclude all items from " + item);

    }

    //Get Item and exclusion prices from DataLoader and return cost for each item in order
    private Double calculateInvoice(List<String> itemWithIngredients) {
        Double itemValue = itemRates.get(itemWithIngredients.get(0));
        Double ingredientsValue = 0.0d;
        if (itemWithIngredients.size() > 1)
            for (int i = 1; i < itemWithIngredients.size(); i++) {
                ingredientsValue = ingredientsValue + ingredientRates.get(itemWithIngredients.get(i));
            }
        return itemValue - ingredientsValue;
    }

    //This returns a List which has order with exclusions at each index, all in Lower cases
    //Chai, -milk, -water, Mojito, Coffee, -milk, -sugar will have will result below
    //"Chai, -milk, -water" "Mojito" "Coffee, -milk, -sugar"
    private List<String> getItemsFromOrder(String order1) {
        return Arrays.stream(order1.split("(?!,\\s*-),")).map(String::trim).map(String::toLowerCase).collect(Collectors.toList());
    }

    //Replaces '-' from the order
    private List<String> getIngredientFromItem(String item) {
        return Arrays.stream(item.split(",")).map(s -> s.replace("-", "")).map(String::trim).collect(Collectors.toList());
    }

}
