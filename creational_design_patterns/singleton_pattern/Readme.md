# Singleton Design Pattern

## Introduction
The Singleton Design Pattern ensures that a class has only one object (instance) and provides a global way to access it.

It is useful when exactly one object is needed to control shared resources.

---

## Problem it Solves
Sometimes we need only one instance of a class (e.g., configuration, logger, database connection).

Creating multiple objects can cause:
- Inconsistent data
- Extra memory usage
- Unexpected behavior

Example of unwanted multiple objects:

```cpp
Logger obj1;
Logger obj2; // multiple instances created
```

---

## How Singleton Helps
Singleton restricts object creation and provides a single shared instance:

```cpp
Logger* obj = Logger::getInstance();
```

### Benefits
- Ensures only one instance exists
- Provides global access point
- Saves memory
- Useful for shared resources

---

## When to Use Singleton Pattern
- When only one object is needed
- When a shared resource is used across the program
- For logging, configuration, caching, database connection
- When global access is required

---

## When NOT to Use Singleton Pattern
- When multiple instances may be needed later
- When global state makes testing difficult
- When it creates tight coupling
- When simple object creation is sufficient

---

## Summary
Use Singleton when exactly one object is required across the application.