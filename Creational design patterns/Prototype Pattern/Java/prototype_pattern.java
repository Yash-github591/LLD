/*
Prototype Pattern is a creational design pattern that allows you to create new objects 
by copying existing ones, rather than creating new instances from scratch. This can be 
particularly useful when the process of creating a new object is expensive or complex.

In the Prototype Pattern, you typically define a base class (or interface) that 
declares a method for cloning itself. Concrete classes then implement this method 
to return a copy of themselves. 

Also a prototype registry can be used to store and manage prototypes, allowing you to 
easily retrieve and clone them when needed.

This way, you can create new objects by cloning existing ones, which can be more 
efficient than creating new instances from scratch.
*/

/*
We will create a simple example of a shape prototype. We will have a base class `Shape`
with a virtual clone method, and two concrete classes `Circle` and `Square` that 
implement the clone method.

Also, we will create a `ShapeRegistry` class to manage our prototypes. This registry 
will allow us to register prototypes and retrieve them for cloning. 
*/

// Example implementation of the Prototype Pattern in Java
import java.util.*;

// Base Class for shapes
interface Shape{
    public Shape clone(); // Method for cloning
    public void draw(); // Pure virtual method for drawing
}

// Concrete class for circle
class Circle implements Shape{
    public Circle(){
        System.out.println("Circle created");
    }

    // Copy constructor
    public Circle(Circle other){
        System.out.println("Circle cloned");
    }

    @Override
    public Shape clone(){
        return new Circle(this); // Clone the current object
    }

    @Override
    public void draw(){
        System.out.println("Drawing a circle");
    }
}

// Concrete class for square
class Square implements Shape{
    public Square(){
        System.out.println("Square created");
    }

    public Square(Square other){
        System.out.println("Square cloned");
    }

    @Override
    public Shape clone(){
        return new Square(this); // clone the object
    }

    @Override
    public void draw(){
        System.out.println("Drawing a square");
    }
}

// Prototype registry to manage prototypes
class ShapeRegistry{
    // Map to store prototypes
    private Map<String,Shape> prototypes = new HashMap<>();

    // Register a prototype
    public void registerPrototype(String name, Shape prototype){
        prototypes.put(name,prototype);
    }

    // Retrieve and clone a prototype
    Shape getPrototype(String name){
        if(prototypes.containsKey(name)){
            // Return a clone of the prototype
            return prototypes.get(name).clone();
        }
        return null; // Return null if prototype not found
    }
}

// Main function to demonstrate the Prototype Pattern
public class Main {
    public static void main(String[] args) {
        // Create a shape registry to manage prototypes
        ShapeRegistry registry = new ShapeRegistry();
    
        // Create prototypes
        Circle circlePrototype = new Circle();
        Square squarePrototype = new Square();
    
        // Register prototypes
        registry.registerPrototype("Circle", circlePrototype);
        registry.registerPrototype("Square", squarePrototype);
    
        // Clone and use Circle prototype
        Shape shape1 = registry.getPrototype("Circle");
        shape1.draw();
    
        // Clone and use Square prototype
        Shape shape2 = registry.getPrototype("Square");
        shape2.draw();
    }
}