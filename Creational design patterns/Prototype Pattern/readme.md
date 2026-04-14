# Prototype Design Pattern

## Introduction

The Prototype Design Pattern is a creational design pattern that creates new objects by copying (cloning) existing objects instead of creating them from scratch using `new`.

Each object provides a `clone()` method that returns a copy of itself. This reduces the cost of creating complex or expensive objects.

---

## Problem it Solves

Direct object creation can be expensive or complex:

```cpp
Circle* c1 = new Circle();   // expensive creation
Circle* c2 = new Circle();   // repeating same process
Circle* c3 = new Circle();
```

### Issues

- Expensive object creation process
- Duplicate initialization code
- Hard to create many similar objects efficiently
- Tight dependency on concrete classes

---

## How Prototype Helps

Objects are copied from an existing prototype:

```cpp
Shape* shape1 = registry.getPrototype("Circle");
Shape* shape2 = registry.getPrototype("Circle");

Each prototype knows how to clone itself:

Shape* clone() override {
    return new Circle(*this);
}
```

### Benefits

- Avoids expensive object creation
- Reduces duplicate initialization code
- Easy to create multiple similar objects
- Promotes loose coupling
- Improves performance when object creation is costly

---

## When to Use Prototype Pattern

- When object creation is expensive or complex
- When many similar objects are required
- When object configuration should be reused
- When you want to avoid subclassing for object creation
- When objects should be created at runtime by copying

---

## When NOT to Use Prototype Pattern

- When objects are simple to create
- When deep copying is difficult to implement
- When object structure frequently changes
- When cloning introduces unnecessary complexity

---

## Summary

Use Prototype Pattern when object creation is costly and many similar objects are needed. Instead of creating objects repeatedly, clone existing ones to improve performance and flexibility.
