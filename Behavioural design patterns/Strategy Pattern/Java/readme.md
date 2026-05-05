# Strategy Design Pattern (Java)

## Introduction

The **Strategy Design Pattern** is a behavioral design pattern that allows selecting an object’s behavior at runtime.

It defines a **family of algorithms**, encapsulates each one, and makes them **interchangeable**. The client can dynamically choose which algorithm to use without modifying existing code.

This pattern helps achieve **loose coupling** between the client and the algorithm implementation.

In Java, the Strategy Pattern is commonly implemented using **interfaces** and **polymorphism**.

---

## Problem it Solves

When multiple variations of an algorithm exist, developers often use conditional statements such as:

```java
if(strategy.equals("nearest")) {
    // logic for nearest driver
}
else if(strategy.equals("surge")) {
    // logic for surge pricing
}
else if(strategy.equals("airport")) {
    // logic for airport queue
}
```

### Issues:

- Violates **Open/Closed Principle**
- Hard to extend with new algorithms
- Code becomes difficult to maintain
- Logic becomes tightly coupled with the client
- Large conditional statements reduce readability

---

## How Strategy Pattern Helps

Strategy Pattern extracts algorithms into **separate classes** and makes them interchangeable.

```java
MatchingStrategy strategy = new NearestDriverStrategy();
RideMatchingService service = new RideMatchingService(strategy);

service.matchRider("Downtown");
```

### Key Idea:

- Define a common interface for all strategies
- Each algorithm is implemented in a separate class
- Context class delegates the work to the strategy
- Strategy can be changed at runtime

### Benefits:

- Eliminates large conditional statements
- Makes algorithms interchangeable
- Improves flexibility and scalability
- Follows Open/Closed Principle
- Promotes composition over inheritance

---

## Structure

### 1. Strategy Interface

Defines the common behavior for all strategies.

```java
interface MatchingStrategy {
    void match(String location);
}
```

---

### 2. Concrete Strategies

Implement different variations of the algorithm.

#### Nearest Driver Strategy

```java
class NearestDriverStrategy implements MatchingStrategy {
    @Override
    public void match(String location) {
        System.out.println("Matching rider with the nearest driver at " + location);
    }
}
```

#### Surge Priority Strategy

```java
class SurgePriorityStrategy implements MatchingStrategy {
    @Override
    public void match(String location) {
        System.out.println("Matching rider with a driver based on surge pricing at " + location);
    }
}
```

#### Airport Queue Strategy

```java
class AirportQueueStrategy implements MatchingStrategy {
    @Override
    public void match(String location) {
        System.out.println("Matching rider with a driver based on airport queue priority at " + location);
    }
}
```

Each strategy encapsulates a specific matching logic.

---

### 3. Context Class

Maintains a reference to a strategy object and delegates execution to it.

```java
class RideMatchingService {
    private MatchingStrategy strategy;

    RideMatchingService(MatchingStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(MatchingStrategy newStrategy) {
        this.strategy = newStrategy;
    }

    public void matchRider(String location) {
        strategy.match(location);
    }
}
```

### Responsibilities:

- Stores the current strategy
- Allows changing strategy at runtime
- Delegates execution to strategy object

---

## Example Flow

```java
public class Main {
    public static void main(String[] args) {

        RideMatchingService rideMatchingService1 =
            new RideMatchingService(new NearestDriverStrategy());

        rideMatchingService1.matchRider("Downtown");


        RideMatchingService rideMatchingService2 =
            new RideMatchingService(new SurgePriorityStrategy());

        rideMatchingService2.matchRider("Airport");
    }
}
```

### Execution Steps:

1. Client selects a strategy implementation
2. Strategy object is passed to context
3. Context delegates the work to the strategy
4. Strategy executes its specific algorithm
5. Strategy can be changed dynamically if needed

---

## Real World Analogy

Think of a **ride-sharing app**:

- Nearest Driver Strategy
- Surge Pricing Strategy
- Airport Queue Strategy

The system selects the appropriate strategy based on conditions (location, demand, etc.) without modifying the core matching service.

---

## When to Use

- When multiple variations of an algorithm exist
- When conditional statements are frequently used to select behavior
- When algorithms may change independently of client code
- When runtime selection of behavior is required
- When you want to follow Open/Closed Principle

---

## Advantages

- Eliminates large conditional statements
- Promotes Open/Closed Principle
- Improves flexibility
- Encapsulates algorithm implementation
- Easy to add new strategies
- Encourages composition over inheritance

---

## Disadvantages

- Increases number of classes
- Client must understand differences between strategies
- Slight increase in complexity for simple logic

---

## Summary

Strategy Pattern allows selecting different algorithms at runtime by encapsulating them into separate classes.

It improves flexibility, maintainability, and scalability by keeping algorithm logic independent from the client that uses it.
