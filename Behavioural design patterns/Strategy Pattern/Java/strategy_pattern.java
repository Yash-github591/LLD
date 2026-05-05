/*
Strategy Pattern is a behavioral design pattern that enables selecting an algorithm's 
behavior at runtime. 

It defines a family of algorithms, encapsulates each one, and makes them interchangeable. 

This pattern allows the algorithm to vary independently from clients that use it.

The Strategy Pattern can be implemented using abstract classes or 
interfaces to define the common behavior of the algorithms, and concrete classes to 
implement specific algorithms.
*/

/*
In this example, we will implement a simple strategy pattern for a ride Matching 
Strategy in a ride-sharing application. We will have different strategies for 
matching riders with drivers based on different criteria such as distance, surge 
pricing, and airport queue priority.
*/

import java.util.*;

// Strategy Interface: MatchingStrategy
interface MatchingStrategy{
    void match(String location);
}

// Concrete Strategy 1: NearestDriverStrategy
class NearestDriverStrategy implements MatchingStrategy{
    @Override
    public void match(String location){
        System.out.println("Matching rider with the nearest driver at "+location);
    }
}

// Concrete Strategy 2: SurgePriorityStrategy
class SurgePriorityStrategy implements MatchingStrategy{
    @Override
    public void match(String location){
        System.out.println("Matching rider with a driver based on surge pricing at "+location);
    }
}

// Concrete Strategy 3: AirportQueueStrategy
class AirportQueueStrategy implements MatchingStrategy{
    @Override
    public void match(String location){
        System.out.println("Matching rider with a driver based on airport queue priority at "+location);
    }
}

// Context: MatchingService
class RideMatchingService{
    private MatchingStrategy strategy; // Instance of the current strategy

    // Constructor to set the strategy
    RideMatchingService(MatchingStrategy strategy){
        this.strategy=strategy;
    }

    // Method to change the strategy at runtime    
    public void setStrategy(MatchingStrategy newStrategy){
        this.strategy=newStrategy;
    }

    // Method to execute the strategy
    public void matchRider(String location){
        strategy.match(location);
    }
}

// Client code to demonstrate the Strategy Pattern
public class Main {
    public static void main(String[] args) {
        // Using NearestDriverStrategy
        RideMatchingService rideMatchingService_1= new RideMatchingService(new NearestDriverStrategy());
        rideMatchingService_1.matchRider("Downtown");

        // Changing strategy to SurgePriorityStrategy
        RideMatchingService rideMatchingService_2= new RideMatchingService(new SurgePriorityStrategy());
        rideMatchingService_2.matchRider("Airport");
    }
}