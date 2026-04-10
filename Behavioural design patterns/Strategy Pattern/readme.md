# Strategy Design Pattern

## Introduction

The **Strategy Design Pattern** is a behavioral design pattern that allows selecting an object’s behavior at runtime.

It defines a **family of algorithms**, encapsulates each one, and makes them **interchangeable**. The client can dynamically choose which algorithm to use without modifying existing code.

This pattern helps achieve **loose coupling** between the client and the algorithm implementation.

In C++, the Strategy Pattern is commonly implemented using **abstract classes (interfaces)** and **polymorphism**.

---

## Problem it Solves

When multiple variations of an algorithm exist, developers often use conditional statements such as:

```cpp
if(strategy == "nearest") {
    // logic for nearest driver
}
else if(strategy == "surge") {
    // logic for surge pricing
}
else if(strategy == "airport") {
    // logic for airport queue
}
```

Issues:

- Violates **Open/Closed Principle**
- Hard to extend with new algorithms
- Code becomes difficult to maintain
- Logic becomes tightly coupled with the client
- Large conditional statements reduce readability

---

## How Strategy Pattern Helps

Strategy Pattern extracts algorithms into **separate classes** and makes them interchangeable.

```cpp
MatchingStrategy* strategy = new NearestDriverStrategy();
RideMatchingService service(strategy);

service.matchRider("Downtown");
```

Key idea:

- Define a common interface for all strategies
- Each algorithm is implemented in a separate class
- Context class delegates the work to the strategy
- Strategy can be changed at runtime

Benefits:

- Eliminates large conditional statements
- Makes algorithms interchangeable
- Improves flexibility and scalability
- Follows Open/Closed Principle
- Promotes composition over inheritance

---

## Structure

### 1. Strategy Interface

Defines the common behavior for all strategies.

```cpp
class MatchingStrategy {
public:
    virtual void match(string location) = 0;
};
```

---

### 2. Concrete Strategies

Implement different variations of the algorithm.

#### Nearest Driver Strategy

```cpp
class NearestDriverStrategy : public MatchingStrategy {
public:
    void match(string location) override {
        cout << "Matching rider with the nearest driver at " << location << endl;
    }
};
```

#### Surge Priority Strategy

```cpp
class SurgePriorityStrategy : public MatchingStrategy {
public:
    void match(string location) override {
        cout << "Matching rider with a driver based on surge pricing at " << location << endl;
    }
};
```

#### Airport Queue Strategy

```cpp
class AirportQueueStrategy : public MatchingStrategy {
public:
    void match(string location) override {
        cout << "Matching rider with a driver based on airport queue priority at " << location << endl;
    }
};
```

Each strategy encapsulates a specific matching logic.

---

### 3. Context Class

Maintains a reference to a strategy object and delegates execution to it.

```cpp
class RideMatchingService {
private:
    MatchingStrategy* strategy;

public:
    RideMatchingService(MatchingStrategy* strategy){
        this->strategy = strategy;
    }

    void setStrategy(MatchingStrategy* newStrategy){
        this->strategy = newStrategy;
    }

    void matchRider(string location){
        strategy->match(location);
    }
};
```

Responsibilities:

- Stores the current strategy
- Allows changing strategy at runtime
- Delegates execution to strategy object

---

## Example Flow

```cpp
RideMatchingService rideMatchingService1 =
    RideMatchingService(new NearestDriverStrategy());

rideMatchingService1.matchRider("Downtown");


RideMatchingService rideMatchingService2 =
    RideMatchingService(new SurgePriorityStrategy());

rideMatchingService2.matchRider("Airport");
```

Execution steps:

1. Client selects a strategy implementation
2. Strategy object is passed to context
3. Context delegates the work to the strategy
4. Strategy executes its specific algorithm
5. Strategy can be changed dynamically if needed

---

## Real World Analogy

Think of **Google Maps navigation**:

- Fastest Route Strategy
- Shortest Route Strategy
- Avoid Tolls Strategy

The user chooses a preference, and the navigation system changes the algorithm accordingly without modifying the core system.

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
