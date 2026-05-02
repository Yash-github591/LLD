/*
Factory Pattern in C++
It is a creational design pattern that allows client to create objects through a 
helper (factory) class, not directly with new keyword.

Client asks factory for a product type; factory returns the right class.

This seperates the creation of objects from their usage, making code more 
flexible and reusable and keeps code simpler and easier to change when adding 
new product types.
*/

#include <bits/stdc++.h>
using namespace std;

// Creating an interface for the product named Logistics
class Logistics {
public:
    virtual void deliver() = 0; // pure virtual function
    virtual ~Logistics() {} // virtual destructor for proper cleanup
};

// Creating a concrete class named Air
class Air : public Logistics {
public:
    void deliver() override {
        cout << "Delivering by air" << endl;
    }
};

// Creating a concrete class named Road
class Road : public Logistics {
public:
    void deliver() override {
        cout << "Delivering by road" << endl;
    }
};

// Creating a factory class named LogisticsFactory
class LogisticsFactory {
public:

    /*
        Function to create logistics objects based on the mode of delivery
        It takes a string parameter 'mode' which specifies the type of logistics 
        (e.g., "air" or "road") and returns a pointer to a Logistics object.

        NOTE: The function is static because:
        1. It does not require an instance of the LogisticsFactory class to be called.
        2. It can be called directly using the class name, which is convenient for a
            factory method that is meant to create objects without needing to instantiate 
            the factory itself.
    */
    static Logistics* createLogistics(const string& mode) {
        // Based on the mode, return the appropriate logistics object

        if (mode == "air") {
            return new Air();
        } 
        
        if (mode == "road") {
            return new Road();
        }
        
        // If the mode is not recognized, return nullptr or throw an exception
        return nullptr; // or throw an exception
    }
};

/* Client code: LogisticsFactory is used to create objects of the Logistics interface
                named Logistics Service class. The client code is decoupled from the concrete classes (Air and Road)
                and only interacts with the Logistics interface, making it easier to add new logistics types in the
                future without modifying the client code.
*/

// Creating a logistics service class named LogisticsService
class LogisticsService {
public:
    void deliver(const string& mode) {
        Logistics* logistics = LogisticsFactory::createLogistics(mode);
        if (logistics) {
            logistics->deliver();
            delete logistics; // Clean up the created object
        } else {
            cout << "Invalid delivery mode: " << mode << endl;
        }
    }
};

// Main function to demonstrate the factory pattern
int main() {
    LogisticsService service;
    service.deliver("air");  // Output: Delivering by air
    service.deliver("road"); // Output: Delivering by road
    service.deliver("sea");  // Output: Invalid delivery mode: sea

    return 0;
}