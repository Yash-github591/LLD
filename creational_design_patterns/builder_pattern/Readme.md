# Builder Design Pattern

## Introduction
The Builder Design Pattern is a creational pattern that builds a complex object step-by-step.  
It separates construction from representation.

Example:
BurgerMeal built via `BurgerMeal::BurgerBuilder` with required (`bunType`, `patty`) and optional (`hasCheese`, `toppings`, `side`, `drink`) fields.

---

## Problem it Solves
Hard-to-manage constructors with many params:

```cpp
BurgerMeal meal("Sesame", "Potato", true, {"Lettuce","Tomato"}, "Onion Rings", "Milkshake");
```

Issues:
- Long parameter lists
- Order confusion
- Many overloads
- Hard maintenance

---

## How Builder Helps
Fluent builder:

```cpp
BurgerMeal meal = BurgerMeal::BurgerBuilder("Sesame","Potato")
    .setCheese(true)
    .addTopping("Lettuce")
    .addTopping("Tomato")
    .setSide("Onion Rings")
    .setDrink("Milkshake")
    .build();
```

Benefits:
- Readable API
- Required enforced in builder constructor
- Optional set as needed
- Immutable final object

---

## When to Use
- Many params
- Optional fields
- Configurable combinations
- Avoid telescoping constructors

---

## When Not to Use
- Simple objects
- Few fields
- Unnecessary overhead

---

## Summary
Use Builder for complex object construction with clear, maintainable client code.