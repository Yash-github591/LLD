/* 
Decorator Pattern is a structural design pattern that allows behavior to be 
added to an individual object, either statically or dynamically, without 
affecting the behavior of other objects from the same class. 

It is typically used to extend the functionalities of classes in a flexible 
and reusable way.

The Decorator Pattern can be implemented using inheritance and composition.
We create a base class (Component) and then derive concrete components and decorators 
from it.
*/

/*
In this example, we have a base class `Coffee` which defines the interface for our coffee objects.
Next, we have a `SimpleCoffee` class that implements the `Coffee` interface. 
Then we have a `CoffeeDecorator` class that also implements the `Coffee` interface and contains a pointer to a `Coffee` object. 
Finally, we have concrete decorators like `MilkDecorator` and `SugarDecorator` that extend the functionality of the 
coffee by adding milk and sugar respectively.
*/


import java.util.*;

// Coffee interface
interface Coffee{
  double getCost();
  String getDescription();
}

// SimpleCoffee class: A concrete component that implements the Coffee interface
class SimpleCoffee implements Coffee{
  @Override
  public double getCost(){
    return 5.0; // Base cost of simple coffee 
  }
  
  @Override
  public String getDescription(){
    return "Simple Coffee";
  }
}

// Cappuccino class: Another concrete component that implements the Coffee interface
class Cappuccino implements Coffee{
  @Override
  public double getCost(){
    return 7.0; // Base cost of Cappuccino
  }
  
  @Override
  public String getDescription(){
    return "Cappuccino";
  }
}

// CoffeeDecorator class: An abstract decorator class that implements the Coffee interface and contains a pointer to a Coffee object
abstract class CoffeeDecorator implements Coffee{
  // Object of coffee being decorated 
  protected Coffee decoratedCoffee; 
  
  // Constructor that takes a Coffee object to decorate 
  public CoffeeDecorator(Coffee coffee){
    this.decoratedCoffee=coffee;
  }
  
  @Override
  public double getCost(){
    return this.decoratedCoffee.getCost();
  }
  
  @Override
  public String getDescription(){
    return this.decoratedCoffee.getDescription();
  }
}

// MilkDecorator class: A concrete decorator that adds milk to the coffee
class MilkDecorator extends CoffeeDecorator{
  // Constructor to initialise the decorated coffee object 
  public MilkDecorator(Coffee coffee){
    super(coffee);
  }
  
  @Override
  public double getCost(){
    return super.getCost() + 1.0; // Adding milk cost 
  }
  
  @Override
  public String getDescription(){
    return super.getDescription() + ", Milk";
  }
}

// SugarDecorator class: A concrete decorator that adds sugar to the coffee
class SugarDecorator extends CoffeeDecorator{
  // Constructor to initialise the decorated coffee object 
  public SugarDecorator(Coffee coffee){
    super(coffee);
  }
  
  @Override
  public double getCost(){
    return super.getCost() + 0.5; // Adding cost of sugar
  }
  
  @Override
  public String getDescription(){
    return super.getDescription() + ", Sugar";
  }
}

/*
Main function to demonstrate the Decorator Pattern. This function demostrates
how we can use the decorators in production level code. We create a simple 
coffee and then decorate it with milk and sugar,
*/
public class Main{
  public static void main(String[] args){
    // Example 1: Simple Coffee
    Coffee coffee1 = new SimpleCoffee();
    System.out.println(coffee1.getDescription() + " -> Cost: $" + coffee1.getCost());
      
    // Example 2: Simple Coffee + Milk
    Coffee coffee2 = new MilkDecorator(new SimpleCoffee());
    System.out.println(coffee2.getDescription() + " -> Cost: $" + coffee2.getCost());
    
    // Example 3: Simple Coffee + Milk + Sugar
    Coffee coffee3 = new SugarDecorator(new MilkDecorator(new SimpleCoffee()));
    System.out.println(coffee3.getDescription() + " -> Cost: $" + coffee3.getCost());
    
    // Example 4: Cappuccino + Sugar
    Coffee coffee4 = new SugarDecorator(new Cappuccino());
    System.out.println(coffee4.getDescription() + " -> Cost: $" + coffee4.getCost());
    
    // Example 5: Cappuccino + Milk + Sugar
    Coffee coffee5 = new SugarDecorator(new MilkDecorator(new Cappuccino()));
    System.out.println(coffee5.getDescription() + " -> Cost: $" + coffee5.getCost());
  }
}