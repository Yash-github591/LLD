/*
In this example, we have a Target Interface called "Shape" that defines a method "draw()".
We also have an Adaptee class called "LegacyRectangle" that has a different interface 
with a method "drawRectangle()". The Adapter class "RectangleAdapter" implements the 
Target Interface and adapts the Adaptee's interface to match it.

When the client calls the "draw()" method on the Adapter, it internally calls the 
"drawRectangle()" method of the Adaptee, allowing the two incompatible interfaces to 
work together seamlessly.
*/

import java.util.*;

// Target Interface
interface Shape{
    public void draw();
}

// Concrete implementation of the target interface
class Circle implements Shape{
    @Override
    public void draw(){
        System.out.println("Drawing a circle");
    }
}

// Adaptee or Existing class with an incompatible interface
class LegacyRectangle{
    public void drawRectangle(){
        System.out.println("Drawing a rectangle using LegacyRectangle");
    }
}

// Adapter or Wrapper class that implements the target interface and adapts adaptee's interafce
class RectangleAdapter implements Shape{
    /*
    The RectangleAdapter class has a private member variable that holds a pointer 
    to the LegacyRectangle object.
    */
    private LegacyRectangle legacyRectangle = new LegacyRectangle();

    @Override
    public void draw(){
        this.legacyRectangle.drawRectangle();
    }
}

// Client code
// Draw service class that uses the target interface
class DrawService{
    private Shape shape;

    // Constructor to set the shape
    DrawService(Shape shape){
        this.shape=shape;
    }

    // Method to draw the shape
    public void drawShape(){
        this.shape.draw();
    }
}

public class Main{
    public static void main(String[] args){
        // Create a circle object and use it with the DrawService
        Circle circle = new Circle();
        DrawService drawCircle = new DrawService(circle);
        drawCircle.drawShape();

        // Create a RectancleAdapter object and use it with the DrawService
        RectangleAdapter rectangleAdapter = new RectangleAdapter();
        DrawService drawRectangle = new DrawService(rectangleAdapter);
        drawRectangle.drawShape();
    }
}