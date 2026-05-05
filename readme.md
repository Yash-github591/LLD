# 🧩 Design Patterns in Java and C++

A curated collection of classic object-oriented design patterns organized into Behavioural, Creational, and Structural categories.

---

## Prerequisites

- A Programming Language(Java/C++).
- Object Oriented Programming(OOPs) concepts:
  - [Java OOPs tutorial](https://medium.com/@punitmudgal/oop-in-java-from-zero-to-lld-ready-9697462bc7a7)
  - [C++ OOPs tutorial](https://medium.com/@bangermadhur/master-lld-fundamentals-of-oops-05a2f9e36ab9)

## Categories

- **Behavioural design patterns**: manage communication and responsibility between objects.
- **Creational design patterns**: simplify object creation and initialization.
- **Structural design patterns**: compose classes and objects into larger flexible structures.

---

## Present Design Patterns

### 🔁 Behavioural

- **Command Pattern** – Turns a request into a stand-alone object containing all information about the request. This transformation decouples the object that invokes the operation from the one that knows how to perform it.
- **Iterator Pattern** – Provides a standard way to traverse elements of a collection sequentially without exposing its internal representation or structure.
- **Observer Pattern** – Establishes a one-to-many relationship where multiple dependent objects automatically get notified and updated when the subject’s state changes.
- **Strategy Pattern** – Enables selecting and switching between different algorithms or behaviors at runtime without altering the client code using them.
- **Template Method Pattern** – Defines the overall structure of an algorithm in a base class while allowing subclasses to modify specific steps without changing the algorithm’s flow.
- **State Pattern** – Allows an object to change its behavior when its internal state changes, making it appear as if the object has changed its class.
- **Chain of Responsibility Pattern** – Allows a request to be passed along a chain of potential handlers until it is processed.
- **Visitor Pattern** – Allows you to add new operations to an existing object structure without modifying the classes of the objects in that structure.
- **Mediator Pattern** – Reduces complex, direct dependencies between objects by forcing them to communicate indirectly through a centralized mediator object.
- **Memento Pattern** – Used to capture and save an object's internal state, allowing it to be restored later without violating encapsulation.

---

### 🏗️ Creational

- **Abstract Factory Pattern** – Provides an interface for creating families of related or dependent objects without specifying their concrete classes, ensuring consistency among created objects.
- **Builder Pattern** – Separates the construction of a complex object from its representation, allowing the same construction process to create different variations of the object.
- **Factory Pattern** – Defines a method for creating objects, allowing subclasses to decide which class to instantiate, promoting loose coupling between classes.
- **Prototype Pattern** – Creates new objects by copying an existing instance, improving performance when object creation is costly or complex.
- **Singleton Pattern** – Ensures that a class has only one instance throughout the application and provides a global point of access to that instance.

---

### 🧱 Structural

- **Adapter Pattern** – Converts one interface into another expected by clients, allowing classes with incompatible interfaces to work together seamlessly.
- **Bridge Pattern** – Decouples abstraction from its implementation, enabling both to vary independently and making the system more flexible and extensible.
- **Composite Pattern** – Organizes objects into tree structures to represent part-whole hierarchies, allowing clients to treat individual objects and compositions uniformly.
- **Decorator Pattern** – Dynamically adds new responsibilities or behavior to objects without modifying their existing code, following the open/closed principle.
- **Facade Pattern** – Provides a simplified and unified interface to a complex subsystem, making it easier for clients to interact with the system.
- **Flyweight Pattern** – Optimizes memory usage by sharing common parts of object state among multiple objects instead of storing duplicate data.
- **Proxy Pattern** – Acts as a placeholder or intermediary for another object to control access, add functionality, or delay object creation.

---
