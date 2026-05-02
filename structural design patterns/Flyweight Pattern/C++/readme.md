# Flyweight Design Pattern

## Introduction

The Flyweight Design Pattern is a structural design pattern that minimizes memory usage by sharing as much data as possible with similar objects.

It is particularly useful when we have a large number of objects that share common data.

In the Flyweight Pattern, we typically have two types of objects:
1. Flyweight: This is the shared object that contains the common state. It is immutable and can be shared among multiple clients.
2. Client: This is the object that uses the Flyweight. It contains the unique state that is not shared.

The Flyweight Pattern is implemented using a factory class that manages the creation and sharing of Flyweight objects.

---

## Problem it Solves

Sometimes we create many objects that have similar data, leading to high memory usage.

Example problem:
If we create thousands of tree objects in a forest simulation, each with the same type, color, and texture:

```cpp
class Tree {
    string name;    // Same for many trees
    string color;   // Same for many trees
    string texture; // Same for many trees
    int x, y;       // Unique per tree
};
```

Problems caused:

- Memory waste from duplicate data
- High memory consumption
- Slower performance
- Resource inefficiency

---

## How Flyweight Helps

Flyweight allows sharing common data among multiple objects while keeping unique data separate.

Example:

```cpp
// Shared state (Flyweight)
TreeType* oakType = factory.getTreeType("Oak", "Green", "Rough");

// Unique state (Client)
Tree* tree1 = new Tree(1, 1, oakType);
Tree* tree2 = new Tree(2, 2, oakType); // Reuses same TreeType
```

Each Flyweight:

- Contains shared (intrinsic) state
- Is immutable and reusable
- Managed by a factory

Result:

Memory efficient tree forest with shared tree types.

### Benefits

- Significant memory reduction
- Improved performance
- Faster object creation
- Efficient resource usage
- Separation of shared and unique state

---

## Structure of Flyweight Pattern

### 1. Flyweight (Interface/Abstract)

Defines the interface for flyweight objects and shared state.

Example:

```cpp
class TreeType {
public:
    virtual void draw(int x, int y) = 0;
};
```

---

### 2. Concrete Flyweight

Implements the Flyweight interface and stores shared state.

Example:

```cpp
class ConcreteTreeType : public TreeType {
private:
    string name;    // shared state
    string color;   // shared state
    string texture; // shared state
public:
    ConcreteTreeType(string n, string c, string t)
        : name(n), color(c), texture(t) {}
    
    void draw(int x, int y) override {
        cout << "Tree: " << name << ", Color: " << color 
             << ", Texture: " << texture << ", Position: (" 
             << x << ", " << y << ")" << endl;
    }
};
```

---

### 3. Flyweight Factory

Manages creation and sharing of Flyweight objects.

Example:

```cpp
class TreeFactory {
private:
    unordered_map<string, TreeType*> treeTypes;
public:
    TreeType* getTreeType(string name, string color, string texture) {
        string key = name + "_" + color + "_" + texture;
        
        if (treeTypes.find(key) == treeTypes.end()) {
            treeTypes[key] = new ConcreteTreeType(name, color, texture);
        }
        return treeTypes[key];
    }
};
```

---

### 4. Client

Uses Flyweight objects and stores unique state.

Example:

```cpp
class Tree {
private:
    TreeType* type; // shared state (Flyweight)
    int x, y;       // unique state
public:
    Tree(int x, int y, TreeType* type) : x(x), y(y), type(type) {}
    
    void draw() {
        type->draw(x, y); // delegate to Flyweight
    }
};
```

---

## Example Flow

Forest simulation with shared tree types:

```cpp
Forest forest;
forest.plantTree(1, 1, "Oak", "Green", "Rough");
forest.plantTree(2, 2, "Pine", "Green", "Smooth");
forest.plantTree(3, 3, "Oak", "Green", "Rough"); // Reuses Oak type
forest.draw();
```

Execution flow:

1. Create factory for managing shared objects
2. Request flyweight from factory (creates if needed)
3. Create client object with flyweight + unique state
4. Use client object (delegates to flyweight)

Each tree shares the same TreeType object when they have identical properties.

---

## When to Use Flyweight Pattern

- When you have a large number of similar objects that can share some of their state
- When you want to reduce memory usage by sharing common data among objects
- When the objects can be divided into intrinsic (shared) and extrinsic (unique) states
- When most object state can be made extrinsic
- When many objects can be replaced by fewer shared objects

### Common real-world examples:

- Text editors (character objects sharing font data)
- Game development (terrain tiles, particles)
- GUI components (shared icons, styles)
- Database connection pooling
- String interning in programming languages

---

## When NOT to Use Flyweight Pattern

- When objects don't have significant shared state
- When unique state dominates object data
- When memory is not a concern
- When sharing would complicate the design unnecessarily
- When objects are mutable and sharing could cause issues

---

## Advantages

- Reduces memory usage by sharing common data
- Improves performance by reducing the number of objects created
- Faster object creation and access due to shared data
- Promotes efficient resource utilization
- Clear separation of shared and unique state

---

## Disadvantages

- Can make the code more complex due to the separation of shared and unique states
- Requires careful management of the shared state to avoid unintended side effects
- Can lead to tight coupling between the Flyweight and the Client if not designed properly
- Factory management adds overhead
- Debugging can be more complex with shared objects

---

## Summary

Flyweight Pattern allows minimizing memory usage by sharing common data among multiple objects.

It is useful when objects have significant shared state and memory efficiency is important, while keeping the design maintainable and scalable.