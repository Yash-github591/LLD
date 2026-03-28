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

// Example implementation of the Prototype Pattern in C++
#include <bits/stdc++.h>
using namespace std;

// Base class for shapes
class Shape {
public:
    virtual Shape* clone() = 0; // Pure virtual method for cloning
    virtual void draw() = 0; // Pure virtual method for drawing
};

// Concrete class for Circle
class Circle : public Shape {
public:
    Circle() {
        cout << "Circle created." << endl;
    }

    Circle(const Circle& other) {
        cout << "Circle cloned." << endl;
    }

    Shape* clone() override {
        return new Circle(*this); // Clone the current object
    }

    void draw() override {
        cout << "Drawing a Circle." << endl;
    }
};

// Concrete class for Square
class Square : public Shape {
public:
    Square() {
        cout << "Square created." << endl;
    }

    Square(const Square& other) {
        cout << "Square cloned." << endl;
    }

    Shape* clone() override {
        return new Square(*this); // Clone the current object
    }

    void draw() override {
        cout << "Drawing a Square." << endl;
    }
};

// Prototype registry to manage prototypes
class ShapeRegistry {
private:
    unordered_map<string, Shape*> prototypes; // Map to store prototypes
public:
    void registerPrototype(const string& name, Shape* prototype) {
        prototypes[name] = prototype; // Register a prototype
    }

    Shape* getPrototype(const string& name) {
        if (prototypes.find(name) != prototypes.end()) {
            return prototypes[name]->clone(); // Return a clone of the prototype
        }
        return nullptr; // Return nullptr if prototype not found
    }
};

// Main function to demonstrate the Prototype Pattern
int main() {
    // Create a shape registry to manage our prototypes
    ShapeRegistry registry;

    // Create prototypes and register them
    Circle* circlePrototype = new Circle();
    Square* squarePrototype = new Square();
    registry.registerPrototype("Circle", circlePrototype);
    registry.registerPrototype("Square", squarePrototype);

    // Clone prototypes and use them
    Shape* shape1 = registry.getPrototype("Circle");
    shape1->draw(); // Output: Drawing a Circle.

    Shape* shape2 = registry.getPrototype("Square");
    shape2->draw(); // Output: Drawing a Square.

    return 0;
}