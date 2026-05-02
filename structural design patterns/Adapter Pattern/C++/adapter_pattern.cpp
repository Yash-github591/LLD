/*
In this example, we have a Target Interface called "Shape" that defines a method "draw()".
We also have an Adaptee class called "LegacyRectangle" that has a different interface 
with a method "drawRectangle()". The Adapter class "RectangleAdapter" implements the 
Target Interface and adapts the Adaptee's interface to match it.

When the client calls the "draw()" method on the Adapter, it internally calls the 
"drawRectangle()" method of the Adaptee, allowing the two incompatible interfaces to 
work together seamlessly.
*/

#include <bits/stdc++.h>
using namespace std;

// Target Interface
class Shape {
public:
    virtual void draw() = 0;
};

// Concrete implementation of the Target Interface
class Circle : public Shape {
public:
    void draw() override {
        cout << "Drawing a circle." << endl;
    }
};

// Adaptee or Existing class with an incompatible interface
class LegacyRectangle {
public:
    void drawRectangle() {
        cout << "Drawing a rectangle using LegacyRectangle." << endl;
    }
};

// Adapter or Wrapper class that implements the Target Interface and adapts the Adaptee's interface
class RectangleAdapter : public Shape {
private:
    /*
    The RectangleAdapter class has a private member variable that holds a pointer 
    to the LegacyRectangle object.
    */
    LegacyRectangle* legacyRectangle;
public:
    RectangleAdapter() {
        // Initialize the legacy rectangle object in the constructor
        this->legacyRectangle = new LegacyRectangle();
    }
    
    void draw() override {
        this->legacyRectangle->drawRectangle();
    }
};

// Client code
// Draw Service class that uses the Target Interface
class DrawService {
    Shape* shape;
public: 
    // Constructor to set the shape
    DrawService(Shape* shape){
        this->shape = shape;
    }

    // Method to draw the shape
    void drawShape() {
        this->shape->draw();
    }
};

int main() {
    // Create a Circle object and use it with the DrawService
    Circle circle;
    DrawService drawCircle(&circle);
    drawCircle.drawShape();

    // Create a RectangleAdapter object and use it with the DrawService
    RectangleAdapter rectangleAdapter;
    DrawService drawRectangle(&rectangleAdapter);
    drawRectangle.drawShape();

    return 0;
}
