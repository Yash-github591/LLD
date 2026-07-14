# Factory Design Pattern

## Introduction

The Factory Design Pattern is a creational design pattern that creates objects through a factory method instead of directly using `new`.

The client asks the factory for an object, and the factory decides which class to create. This keeps code flexible and loosely coupled.

---

## Problem it Solves

Direct object creation makes the client dependent on concrete classes:

```python
if mode == "air":
    return Air()
if mode == "road":
    return Road()
```

### Issues

- Tight coupling with concrete classes
- Hard to add new types
- Code needs modification when requirements change

---

## How Factory Helps

Object creation is handled by a factory:

```python
Logistics logistics=LogisticsFactory.createLogistics(mode);
```

### Benefits

- Reduces coupling
- Easy to extend
- Centralized object creation
- Cleaner and maintainable code

---

## When to Use Factory Pattern

- When object type is decided at runtime
- When you want flexible and extendable code
- When many classes implement the same interface
- When you want to reduce dependency on concrete classes

---

## When NOT to Use Factory Pattern

- When there is only one class to create
- When object creation is very simple
- When it adds unnecessary complexity

---

## Summary

Use Factory Pattern when object creation depends on conditions and you want flexible, scalable code.
