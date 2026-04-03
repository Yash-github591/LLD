# Decorator Design Pattern

## Introduction

The Decorator Design Pattern is a structural design pattern that allows behavior or responsibilities to be added to an individual object dynamically without modifying its original code.

It provides a flexible alternative to subclassing for extending functionality.

Decorator wraps an existing object and adds new behavior before or after delegating the request to the original object.

---

## Problem it Solves

Sometimes we want to add optional features to objects without creating many subclasses.

Example problem:
If we try to represent every coffee combination using inheritance:

```cpp
class SimpleCoffee {};
class MilkCoffee {};
class SugarCoffee {};
class MilkSugarCoffee {};
class CappuccinoWithMilk {};
class CappuccinoWithSugar {};
class CappuccinoMilkSugar {};
```

As features increase, number of classes increases rapidly, making the system hard to maintain.

Problems caused:

- Class explosion
- Duplicate code
- Hard to extend new features
- Difficult to maintain

---

## How Decorator Helps

Decorator allows adding features dynamically by wrapping objects.

Example:

```cpp
Coffee* coffee =
new SugarDecorator(
new MilkDecorator(
new Cappuccino()));
```

Each decorator:

- implements the same interface
- wraps another object
- adds new behavior

Result:

Cappuccino + Milk + Sugar

### Benefits

- Flexible design
- Avoids too many subclasses
- Features can be combined dynamically
- Follows Open/Closed Principle
- Easy to extend new decorators

---

## Structure of Decorator Pattern

### 1. Component (Interface)

Defines common interface for objects and decorators.

Example:

```cpp
class Coffee {
public:
virtual double getCost() = 0;
virtual string getDescription() = 0;
};
```

---

### 2. Concrete Component

Base object to which new functionality can be added.

Example:

```cpp
class SimpleCoffee : public Coffee {};
class Cappuccino : public Coffee {};
```

---

### 3. Decorator (Abstract Class)

Contains reference to component object and implements same interface.

Example:

```cpp
class CoffeeDecorator : public Coffee {
protected:
Coffee* decoratedCoffee;
};
```

---

### 4. Concrete Decorators

Add additional behavior to the component.

Example:

```cpp
class MilkDecorator : public CoffeeDecorator {};
class SugarDecorator : public CoffeeDecorator {};
```

---

## Example Flow

SimpleCoffee -> MilkDecorator -> SugarDecorator

Execution flow:

1. Create base object
2. Wrap with decorator
3. Wrap with another decorator
4. Call final object methods

Each decorator adds its own behavior.

---

## When to Use Decorator Pattern

- When behavior needs to be added dynamically
- When features can be combined in many ways
- When subclassing would create too many classes
- When you want flexible runtime configuration
- When following Open/Closed Principle

### Common real-world examples:

- Coffee toppings
- Pizza toppings
- Logging frameworks
- Middleware in web frameworks
- Java IO Streams
- GUI components (scrollbars, borders)

---

## When NOT to Use Decorator Pattern

- When object behavior is fixed
- When no dynamic feature combination is needed
- When design becomes overly complex
- When simple inheritance is sufficient
- When performance overhead of wrapping is not acceptable

---

## Advantages

- Highly flexible
- Easy to add new decorators
- Avoids subclass explosion
- Promotes reusable code
- Supports runtime feature addition

---

## Disadvantages

- Many small classes created
- Can make debugging harder
- Object structure becomes complex
- Order of decorators may affect result

---

## Summary

Decorator Pattern allows adding new functionality to objects dynamically without modifying existing code.

It is useful when objects need flexible combinations of features while keeping code maintainable and scalable.
