/* 
Decorator Pattern is a structural design pattern that allows behavior to be 
added to an individual object, either statically or dynamically, without 
affecting the behavior of other objects from the same class. 

It is typically used to extend the functionalities of classes in a flexible 
and reusable way.

In C++, the Decorator Pattern can be implemented using inheritance and composition.
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

#include <bits/stdc++.h>
using namespace std;

// Coffee interface
class Coffee {
public:
    virtual double getCost() = 0;
    virtual string getDescription() = 0;
    virtual ~Coffee() {}
};

// SimpleCoffee class: A concrete component that implements the Coffee interface
class SimpleCoffee : public Coffee {
public:
    double getCost() override {
        return 5.0; // Base cost of simple coffee
    }

    string getDescription() override {
        return "Simple Coffee";
    }
};

// Cappuccino class: Another concrete component that implements the Coffee interface
class Cappuccino : public Coffee {
public:
    double getCost() override {
        return 7.0; // Base cost of cappuccino
    }

    string getDescription() override {
        return "Cappuccino";
    }
};

// CoffeeDecorator class: An abstract decorator class that implements the Coffee interface and contains a pointer to a Coffee object
class CoffeeDecorator : public Coffee {
protected:
    Coffee* decoratedCoffee; // Pointer to the coffee being decorated
public:
    // Constructor that takes a Coffee object to decorate
    CoffeeDecorator(Coffee* coffee){
        this->decoratedCoffee = coffee;
    }

    double getCost() override {
        return this->decoratedCoffee->getCost();
    }
    string getDescription() override {
        return this->decoratedCoffee->getDescription();
    }
};

// MilkDecorator class: A concrete decorator that adds milk to the coffee
class MilkDecorator : public CoffeeDecorator {
public:
    // Call the base class constructor to initialize the decorated coffee object
    MilkDecorator(Coffee* coffee) : CoffeeDecorator(coffee) {}

    double getCost() override {
        return CoffeeDecorator::getCost() + 1.0; // Adding cost of milk
    }

    string getDescription() override {
        return CoffeeDecorator::getDescription() + ", Milk";
    }
};

// SugarDecorator class: A concrete decorator that adds sugar to the coffee
class SugarDecorator : public CoffeeDecorator {
public:    
    // Call the base class constructor to initialize the decorated coffee object
    SugarDecorator(Coffee* coffee) : CoffeeDecorator(coffee) {}

    double getCost() override {
        return CoffeeDecorator::getCost() + 0.5; // Adding cost of sugar
    }

    string getDescription() override {
        return CoffeeDecorator::getDescription() + ", Sugar";
    }
};

/*
Main function to demonstrate the Decorator Pattern. This function demostrates
how we can use the decorators in production level code. We create a simple 
coffee and then decorate it with milk and sugar,
*/

int main() {

    // Example 1: Simple Coffee
    Coffee* coffee1 = new SimpleCoffee();
    cout << coffee1->getDescription() << " -> Cost: $" 
         << coffee1->getCost() << endl;

    // Example 2: Simple Coffee + Milk
    Coffee* coffee2 = new MilkDecorator(new SimpleCoffee());
    cout << coffee2->getDescription() << " -> Cost: $" 
         << coffee2->getCost() << endl;

    // Example 3: Simple Coffee + Milk + Sugar
    Coffee* coffee3 = new SugarDecorator(
                        new MilkDecorator(
                            new SimpleCoffee()));
    cout << coffee3->getDescription() << " -> Cost: $" 
         << coffee3->getCost() << endl;

    // Example 4: Cappuccino + Sugar
    Coffee* coffee4 = new SugarDecorator(new Cappuccino());
    cout << coffee4->getDescription() << " -> Cost: $" 
         << coffee4->getCost() << endl;

    // Example 5: Cappuccino + Milk + Sugar
    Coffee* coffee5 = new SugarDecorator(
                        new MilkDecorator(
                            new Cappuccino()));
    cout << coffee5->getDescription() << " -> Cost: $" 
         << coffee5->getCost() << endl;

    return 0;
}