/*
Strategy Pattern is a behavioral design pattern that enables selecting an algorithm's 
behavior at runtime. 

It defines a family of algorithms, encapsulates each one, and makes them interchangeable. 

This pattern allows the algorithm to vary independently from clients that use it.

In C++, the Strategy Pattern can be implemented using abstract classes or 
interfaces to define the common behavior of the algorithms, and concrete classes to 
implement specific algorithms.
*/

/*
In this example, we will implement a simple strategy pattern for a ride Matching 
Strategy in a ride-sharing application. We will have different strategies for 
matching riders with drivers based on different criteria such as distance, surge 
pricing, and airport queue priority.
*/

#include <bits/stdc++.h>
using namespace std;

// Strategy Interface: MatchingStrategy
class MatchingStrategy {
public:
    virtual void match(string location) = 0; // Pure virtual function
};

// Concrete Strategy 1: NearestDriverStrategy
class NearestDriverStrategy : public MatchingStrategy {
public:
    void match(string location) override {
        cout << "Matching rider with the nearest driver at " << location << endl;
    }
};

// Concrete Strategy 2: SurgePriorityStrategy
class SurgePriorityStrategy : public MatchingStrategy {
public:
    void match(string location) override {
        cout << "Matching rider with a driver based on surge pricing at " << location << endl;
    }
};

// Concrete Strategy 3: AirportQueueStrategy
class AirportQueueStrategy : public MatchingStrategy {
public:
    void match(string location) override {
        cout << "Matching rider with a driver based on airport queue priority at " << location << endl;
    }
};


// Context: MatchingService
class RideMatchingService {
private:
    MatchingStrategy* strategy; // Pointer to the current strategy
public:
    // Constructor to set the strategy
    RideMatchingService(MatchingStrategy* strategy){
        this->strategy = strategy;
    } 
    void setStrategy(MatchingStrategy* newStrategy) { // Method to change the strategy at runtime
        this->strategy = newStrategy;
    }
    
    // Method to execute the strategy
    void matchRider(string location) { 
        this->strategy->match(location);
    }
};


// Client code to demonstrate the Strategy Pattern
int main() {
    // Using NearestDriverStrategy
    RideMatchingService rideMatchingService1 = RideMatchingService(new NearestDriverStrategy()); 
    rideMatchingService1.matchRider("Downtown");

    // Changing strategy to SurgePriorityStrategy
    RideMatchingService rideMatchingService2 = RideMatchingService(new SurgePriorityStrategy());
    rideMatchingService2.matchRider("Airport");
    return 0;
}