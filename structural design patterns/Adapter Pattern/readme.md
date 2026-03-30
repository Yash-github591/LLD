# Adapter Design Pattern

## Introduction
The Adapter Design Pattern is a structural design pattern that allows two incompatible interfaces to work together.  

It acts as a bridge between an existing class (Adaptee) and the interface expected by the client (Target).

In the Adapter Pattern, there are typically three main components:
1. Target Interface: This is the interface that the client expects to work with. 
   It defines the methods that the client will use.
2. Adaptee: This is the existing class that has an incompatible interface.
3. Adapter: This is the class that adapts the Adaptee's interface to match the Target Interface.

Example:
`LegacyRectangle` has method `drawRectangle()`, but client expects `Shape::draw()`.  
`RectangleAdapter` converts `draw()` calls into `drawRectangle()` calls.

---

## Problem it Solves
Existing classes may not match the interface required by new code.

Example:

LegacyRectangle rectangle;
rectangle.drawRectangle(); // incompatible with Shape interface

Issues:
- Cannot use existing class directly
- Interface mismatch
- Modifying legacy code is risky
- Tight coupling between client and implementation

---

## How Adapter Helps
Adapter wraps the incompatible class and exposes the expected interface:

```cpp
Shape* shape = new RectangleAdapter();
shape->draw(); // internally calls LegacyRectangle::drawRectangle()
```
Benefits:
- Reuse existing code without modification
- Provides compatibility between interfaces
- Promotes loose coupling
- Improves flexibility and extensibility

---

## Structure
Components of Adapter Pattern:

1. Target Interface
   - Interface expected by the client
   - Example: `Shape` with `draw()`

2. Adaptee
   - Existing class with incompatible interface
   - Example: `LegacyRectangle` with `drawRectangle()`

3. Adapter
   - Converts Target interface into Adaptee interface
   - Example: `RectangleAdapter` implements `Shape`

4. Client
   - Uses objects through Target interface
   - Example: `DrawService`

---

## Example Flow
```cpp
Circle circle;
DrawService drawCircle(&circle);
drawCircle.drawShape(); 
// Output: Drawing a circle.

RectangleAdapter rectangleAdapter;
DrawService drawRectangle(&rectangleAdapter);
drawRectangle.drawShape();
// Output: Drawing a rectangle using LegacyRectangle.
```
Steps:
1. Client interacts with `Shape` interface
2. Adapter implements `Shape`
3. Adapter internally calls `LegacyRectangle`
4. Client remains unaware of interface differences

---

## When to Use
- Integrating legacy code
- Interfaces do not match
- Reuse existing classes
- Want loose coupling between client and implementation
- Need compatibility between libraries

---

## When Not to Use
- Interfaces already compatible
- New system design can directly follow common interface
- Adds unnecessary complexity

---

## Advantages
- Reuse legacy code
- Improves maintainability
- Promotes flexibility
- Follows Open/Closed Principle
- Client code remains unchanged

---

## Disadvantages
- Extra layer of abstraction
- Increases number of classes
- Can slightly increase complexity

---

## Summary
Use Adapter Pattern when two incompatible interfaces need to work together without modifying existing code.  
It provides a clean and maintainable way to integrate legacy systems into modern applications.