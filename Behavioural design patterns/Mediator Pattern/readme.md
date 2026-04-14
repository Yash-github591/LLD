# Mediator Design Pattern

## Introduction

The **Mediator Design Pattern** is a behavioral design pattern that defines an object that **encapsulates how a set of objects interact**.

Instead of objects communicating directly with each other, they communicate through a **mediator**, which centralizes control and coordination.

This helps reduce dependencies between objects, making the system more flexible, maintainable, and easier to extend.

---

## Key Components

1. **Mediator Interface**  
   Declares methods used for communication between colleague objects.  
   Example:
   - `broadcastChange()`
   - `join()`

2. **Concrete Mediator**  
   Implements the mediator interface and controls the interaction between colleagues.

3. **Colleague (User)**  
   Defines the interface for objects that communicate through the mediator.

4. **Concrete Colleague (User Implementation)**  
   Implements communication logic and interacts with the mediator instead of other objects directly.

---

## Problem it Solves

When multiple objects communicate directly with each other, it creates a complex web of dependencies.

Example:

```cpp
alice.sendMessageTo(bob);
bob.sendMessageTo(charlie);
charlie.sendMessageTo(alice);
```

Issues:

- Tight coupling between objects
- Hard to maintain and extend
- Complex communication logic spread across classes
- Difficult to reuse components independently

---

## How Mediator Helps

Mediator centralizes communication between objects.

```cpp
alice.makeChange("Update 1");
```

Internally:

- User sends change to mediator
- Mediator distributes the change to all other users

Key idea:

- Objects **do not communicate directly**
- All communication goes through the mediator

Benefits:

- Reduces coupling between objects
- Centralizes communication logic
- Easier to modify interaction rules
- Improves code maintainability

---

## Structure

### 1. Mediator Interface

Defines communication methods.

```cpp
class DocumentSessionMediator {
public:
    virtual void broadcastChange(string change, User *sender) = 0;
    virtual void join(User *user) = 0;
};
```

---

### 2. Concrete Mediator

Manages users and coordinates communication.

```cpp
class CollaborativeDocument : public DocumentSessionMediator {
    vector<User*> users;

public:
    void broadcastChange(string change, User *sender) override {
        for (User *user : users) {
            if (user->getName() != sender->getName()) {
                user->receiveChange(change, sender);
            }
        }
    }

    void join(User *user) override {
        users.push_back(user);
    }
};
```

---

### 3. Colleague Class (User)

Represents participants interacting via mediator.

```cpp
class User {
    string name;
    DocumentSessionMediator *mediator;

public:
    User(string name, DocumentSessionMediator *mediator) {
        this->name = name;
        this->mediator = mediator;
    }

    void makeChange(string change) {
        cout << name << " makes a change: " << change << endl;
        mediator->broadcastChange(change, this);
    }

    void receiveChange(string change, User *sender) {
        cout << name << " receives change: " << change
             << " from " << sender->getName() << endl;
    }

    string getName() {
        return name;
    }
};
```

---

## Example Flow

```cpp
CollaborativeDocument doc;

User alice("Alice", &doc);
User bob("Bob", &doc);
User charlie("Charlie", &doc);

doc.join(&alice);
doc.join(&bob);
doc.join(&charlie);

alice.makeChange("Added introduction section.");
bob.makeChange("Corrected typos.");
charlie.makeChange("Added conclusion.");
```

Execution steps:

1. Mediator (document session) is created
2. Users join the session
3. A user makes a change
4. The mediator receives the change
5. Mediator broadcasts it to all other users
6. Other users receive the update

---

## When to Use

- When many objects interact in complex ways
- When you want to reduce direct dependencies between objects
- When communication logic should be centralized
- When object relationships are difficult to manage

---

## Advantages

- Reduces tight coupling between objects
- Centralizes communication logic
- Improves maintainability and readability
- Makes system easier to extend
- Supports Single Responsibility Principle

---

## Disadvantages

- Mediator can become a single point of failure
- Adds an extra layer of abstraction
- Harder to debug if mediator logic grows large

---

## Summary

Mediator Pattern centralizes communication between objects using a **mediator object**.

It reduces direct dependencies between components and simplifies interactions, making the system more flexible, scalable, and easier to maintain.
