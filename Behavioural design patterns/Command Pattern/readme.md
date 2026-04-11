# Command Design Pattern

## Introduction

The **Command Design Pattern** is a behavioral design pattern that **encapsulates a request as an object**, allowing you to parameterize clients with different requests, delay or queue operations, and support undo/redo functionality.

Instead of calling methods directly on objects, the request is wrapped inside a **command object**. This separates the object that invokes the operation from the object that performs it.

This improves flexibility, extensibility, and maintainability of the code.

---

## Key Components

1. **Command Interface**  
   Declares methods for executing operations.
   - `execute()` → performs the action
   - `undo()` → reverses the action

2. **Concrete Command**  
   Implements the Command interface and defines the binding between a Receiver and an action.

3. **Receiver**  
   The object that performs the actual work when the command is executed.

4. **Invoker**  
   Responsible for triggering the command. It does not know how the action is performed.

5. **Client**  
   Creates and configures command objects.

---

## Problem it Solves

When a class directly calls methods of another class, the code becomes tightly coupled.

Example:

```cpp
Light light;
light.on();
light.off();

AC ac;
ac.on();
ac.off();
```

Issues:

- Client directly depends on concrete classes
- Hard to add undo/redo functionality
- Difficult to extend system with new operations
- Cannot easily queue or log operations
- Violates Open/Closed Principle

---

## How Command Pattern Helps

Command pattern converts requests into objects so they can be:

- Stored in data structures
- Queued for later execution
- Logged for auditing
- Undone or redone
- Parameterized dynamically

Example:

```cpp
remote.pressButton(0);
remote.pressButton(2);
remote.pressUndo();
```

Key idea:

- Invoker triggers command
- Command calls receiver
- Client configures commands
- Undo functionality becomes easy

Benefits:

- Decouples sender from receiver
- Supports undo/redo operations
- Supports macro commands
- Makes code flexible and extensible

---

## Structure

### 1. Command Interface

Defines operations that every command must implement.

```cpp
class Command {
public:
    virtual void execute() = 0;
    virtual void undo() = 0;
};
```

---

### 2. Receiver Classes

These classes perform the actual business logic.

#### Light Receiver

```cpp
class Light {
public:
    void on() {
        cout << "Light is ON" << endl;
    }

    void off() {
        cout << "Light is OFF" << endl;
    }
};
```

#### AC Receiver

```cpp
class AC {
public:
    void on() {
        cout << "AC is ON" << endl;
    }

    void off() {
        cout << "AC is OFF" << endl;
    }
};
```

---

### 3. Concrete Command Classes

Concrete commands bind receiver actions to command interface.

#### Light ON Command

```cpp
class LightOnCommand : public Command {
    Light light;

public:
    LightOnCommand(Light light) {
        this->light = light;
    }

    void execute() override {
        light.on();
    }

    void undo() override {
        light.off();
    }
};
```

#### Light OFF Command

```cpp
class LightOffCommand : public Command {
    Light light;

public:
    LightOffCommand(Light light) {
        this->light = light;
    }

    void execute() override {
        light.off();
    }

    void undo() override {
        light.on();
    }
};
```

#### AC ON Command

```cpp
class ACOnCommand : public Command {
    AC ac;

public:
    ACOnCommand(AC ac) {
        this->ac = ac;
    }

    void execute() override {
        ac.on();
    }

    void undo() override {
        ac.off();
    }
};
```

#### AC OFF Command

```cpp
class ACOffCommand : public Command {
    AC ac;

public:
    ACOffCommand(AC ac) {
        this->ac = ac;
    }

    void execute() override {
        ac.off();
    }

    void undo() override {
        ac.on();
    }
};
```

---

### 4. Invoker Class

Invoker triggers commands without knowing implementation details.

```cpp
class RemoteControl {
    vector<Command*> buttons = vector<Command*>(4);
    stack<Command*> commandHistory;

public:

    void setCommand(int slot, Command* command) {
        buttons[slot] = command;
    }

    void pressButton(int slot) {
        if(buttons[slot]) {
            buttons[slot]->execute();
            commandHistory.push(buttons[slot]);
        }
        else {
            cout << "No command assigned" << endl;
        }
    }

    void pressUndo() {
        if(!commandHistory.empty()) {
            commandHistory.top()->undo();
            commandHistory.pop();
        }
        else {
            cout << "Nothing to undo" << endl;
        }
    }
};
```

---

## Example Flow

```cpp
Light livingRoomLight;
AC livingRoomAC;

LightOnCommand lightOn(livingRoomLight);
LightOffCommand lightOff(livingRoomLight);

ACOnCommand acOn(livingRoomAC);
ACOffCommand acOff(livingRoomAC);

RemoteControl remote;

remote.setCommand(0, &lightOn);
remote.setCommand(1, &lightOff);
remote.setCommand(2, &acOn);
remote.setCommand(3, &acOff);

remote.pressButton(0);
remote.pressButton(2);

remote.pressUndo();

remote.pressButton(1);

remote.pressUndo();
```

Execution steps:

1. Client creates receiver objects (Light, AC)
2. Client creates command objects and binds receivers
3. Commands are assigned to invoker buttons
4. Invoker executes commands without knowing logic
5. Commands call receiver methods
6. Command history enables undo functionality

---

## When to Use

- When you want to parameterize objects with operations
- When you need undo/redo functionality
- When you want to queue or log operations
- When you want to decouple sender and receiver
- When implementing macro commands
- When designing menu systems or remote controls

---

## Real World Examples

- Remote control buttons
- GUI menu actions
- Transaction systems
- Job scheduling systems
- Undo/Redo operations in editors
- Task queues

---

## Advantages

- Decouples sender and receiver
- Supports undo and redo operations
- Easy to add new commands
- Follows Open/Closed Principle
- Commands can be stored or queued
- Improves maintainability

---

## Disadvantages

- Increases number of classes
- Can add complexity for simple operations
- Requires careful design for undo logic

---

## Summary

Command Pattern **encapsulates requests as objects**, allowing flexible execution, undo functionality, and decoupling between the sender and receiver.

It is widely used in systems that require **action history, transaction support, or flexible request handling**.
