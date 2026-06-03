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

```java
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

```java
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

```java
interface DocumentSessionMediator{
    public void broadCastChange(String change,User sender);
    public void join(User user);
}
```

---

### 2. Concrete Mediator

Manages users and coordinates communication.

```java
class CollaborativeDocument implements DocumentSessionMediator{
    private ArrayList<User> users = new ArrayList<>();

    // Broadcast changes to all users except sender
    @Override
    public void broadCastChange(String change, User sender){
        for(int i=0; i<users.size(); i++){
            User user=users.get(i);
            if(user.getName()!=sender.getName()){
                user.receiveChange(change,sender);
            }
        }
    }

    // Add a user to the session
    @Override
    public void join(User user){
        users.add(user);
    }
}
```

---

### 3. Colleague Class (User)

Represents participants interacting via mediator.

```java
class User{
    private String name;
    private DocumentSessionMediator mediator;

    public User(String name, DocumentSessionMediator mediator){
        this.name=name;
        this.mediator=mediator;
    }

    public void makeChange(String change){
        System.out.println("\n"+name + " makes a change "+ change);
        mediator.broadCastChange(change, this);
    }

    public void receiveChange(String change, User sender){
        System.out.println(name+" receives change "+change+" from "+sender.getName());
    }

    public String getName(){
        return name;
    }
}
```

---

## Example Flow

```java
CollaborativeDocument doc = new CollaborativeDocument();

User alice = new User("Alice",doc);
User bob = new User("Bob",doc);
User charlie = new User("Charlie",doc);

doc.join(alice);
doc.join(bob);
doc.join(charlie);

alice.makeChange("Added instruction section.");
bob.makeChange("Corrected typos in introduction.");
charlie.makeChange("Added conclusion section");
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
