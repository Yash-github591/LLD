/*
The Builder Pattern is a creational design pattern that allows for the step-by-step construction of 
complex objects. It separates the construction of an object from its representation, 
allowing the same construction process to create different representations.
*/

/* 
Code for Builder pattern for creating a BurgerMeal with different components like bunType, patty,
hasCheese, toppings, etc. NOTE: bunType and patty are mandatory components while other components
are optional.

The Builder class will have methods to set each component and a method to build the final 
BurgerMeal object.
*/

import java.util.*;

class BurgerMeal{
    // Required components
    private String bunType;
    private String patty;

    // Optional components
    private boolean hasCheese;
    private ArrayList<String> toppings = new ArrayList<>();
    private String side;
    private String drink;

    // BurgerMeal constructor for object creation 
    private BurgerMeal(Builder Builder){
        this.bunType=Builder.bunType;
        this.patty=Builder.patty;
        this.hasCheese=Builder.hasCheese;
        this.toppings=Builder.toppings;
        this.side=Builder.side;
        this.drink=Builder.drink;
    }

    // Function to display the details of the BurgerMeal
    public void display(){
        System.out.println("\nBurger Meal:");
        System.out.println("Bun Type: "+bunType);
        System.out.println("Patty: "+patty);
        System.out.println("Has Cheese: "+(hasCheese ? "Yes":"No"));
        System.out.println("Toppings:");
        
        for(var topping:toppings){
            System.out.println(topping+" ");
        }
        System.out.println("Side: "+side);
        System.out.println("Drink: "+drink);
    }

    // Static Builder class to construct the BurgerMeal object
    public static class Builder{
        /* 
        Declare the components as private members of the Builder class
        because they will be initialized through the constructor of the Builder 
        class and should not be modified directly.*/

        // Required components
        private String bunType;
        private String patty;

        // Optional components with default values
        private boolean hasCheese=false;
        private ArrayList<String> toppings=new ArrayList<>();
        private String side="Fries";
        private String drink="Soda";

        // Constructor for the Builder class to initialize required components
        Builder(String bunType,String patty){
            this.bunType=bunType;
            this.patty=patty;
        }

        // Method to set the optional component hasCheese
        public Builder setCheese(boolean hasCheese){
            this.hasCheese=hasCheese;
            return this; // Return the Builder object for method chaining
        }

        // Method to add a topping to the toppings list
        Builder addTopping(String topping){
            this.toppings.add(topping);
            return this; // Return the Builder object for method chaining
        }

        // Method to set the optional component side
        public Builder setSide(String side){
            this.side=side;
            return this; // Return the Builder object for method chaining
        }

        // Method to set the optional component drink
        public Builder setDrink(String drink){
            this.drink=drink;
            return this; // Return the Builder object for method chaining
        }

        // Method to build and return the final BurgerMeal object
        public BurgerMeal build(){
            return new BurgerMeal(this); // Pass the Builder object to the BurgerMeal constructor
        }
    }
}

// Main function to demonstrate the Builder pattern
public class Main {
    public static void main(String[] args) {
        // Create a BurgerMeal using the Builder pattern
        BurgerMeal meal_1= new BurgerMeal.Builder("Sesame","Potato").setCheese(true).addTopping("Tomato").setSide("Onion Rings").setDrink("MilkShake").build();

        // Display the details of the created BurgerMeal
        meal_1.display();

        // Create a BurgerMeal using the Builder pattern
        BurgerMeal meal_2= new BurgerMeal.Builder("Whole Wheat","Chicken").addTopping("Pickles").setSide("Salad").setDrink("Water").build();

        // Display the details of the created BurgerMeal
        meal_2.display();

    }
}