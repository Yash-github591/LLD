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

```java
Light light = new Light();
light.on();
light.off();

AC ac = new AC();
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

```java
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

```java
interface Command{
    public void execute();
    public void undo();
}
```

---

### 2. Receiver Classes

These classes perform the actual business logic.

#### Light Receiver

```java
class Light{
    public void on(){
        System.out.println("Light is on");
    }

    public void off(){
        System.out.println("Light is off");
    }
}
```

#### AC Receiver

```java
class AC{
    public void on(){
        System.out.println("AC is on");
    }

    public void off(){
        System.out.println("AC is off");
    }
}
```

---

### 3. Concrete Command Classes

Concrete commands bind receiver actions to command interface.

#### Light ON Command

```java
class LightOnCommand implements Command{
    private Light light;

    public LightOnCommand(Light light){
        this.light=light;
    }

    @Override
    public void execute(){
        light.on();
    }

    @Override
    public void undo(){
        light.off();
    }
}
```

#### Light OFF Command

```java
class LightOffCommand implements Command{
    private Light light;

    public LightOffCommand(Light light){
        this.light=light;
    }

    @Override
    public void execute(){
        light.off();
    }

    @Override
    public void undo(){
        light.on();
    }
}
```

#### AC ON Command

```java
class ACOnCommand implements Command{
    private AC ac;

    public ACOnCommand(AC ac){
        this.ac=ac;
    }

    @Override
    public void execute(){
        ac.on();
    }

    @Override
    public void undo(){
        ac.off();
    }
}
```

#### AC OFF Command

```java
class ACOffCommand implements Command{
    private AC ac;

    public ACOffCommand(AC ac){
        this.ac=ac;
    }

    @Override
    public void execute(){
        ac.off();
    }

    @Override
    public void undo(){
        ac.on();
    }
}
```

---

### 4. Invoker Class

Invoker triggers commands without knowing implementation details.

```java
class RemoteControl{
    // Assuming we have 4 buttons
    private Command[] buttons = new Command[4];

    // Stack to keep track of command history for undo functionality
    private Stack<Command> commandHistory = new Stack<>();

    public void setCommand(int slot, Command command){
        if(slot>=0 && slot<buttons.length){
            buttons[slot]=command;
        }
    }

    public void pressButton(int slot){
        if(slot>=0 && slot<buttons.length && buttons[slot]!=null){
            buttons[slot].execute();
            commandHistory.push(buttons[slot]);
        }
        else{
            System.out.println("No command assigned to this button");
        }
    }

    public void pressUndo(){
        if(!commandHistory.isEmpty()){
            Command lastCommand = commandHistory.pop();
            lastCommand.undo();
        }
        else{
            System.out.println("No command to undo");
        }
    }
}
```

---

## Example Flow

```java
Light livingRoomLight = new Light();
AC livingRoomAC = new AC();

// Creating command objects for the light and AC
LightOnCommand lightOn = new LightOnCommand(livingRoomLight);
LightOffCommand lightOff = new LightOffCommand(livingRoomLight);
ACOnCommand acOn = new ACOnCommand(livingRoomAC);
ACOffCommand acOff = new ACOffCommand(livingRoomAC);

// Setting up the remote control with commands
RemoteControl remote = new RemoteControl();
remote.setCommand(0, lightOn);
remote.setCommand(1, lightOff);
remote.setCommand(2, acOn);
remote.setCommand(3, acOff);

// Simulating button presses on the remote control
remote.pressButton(0); // Turn on the light
remote.pressButton(1); // Turn off the light
remote.pressButton(2); // Turn on the AC
remote.pressButton(3); // Turn off the AC

// Simulating button presses on the remote control
remote.pressButton(0); // Turn on the light
remote.pressButton(2); // Turn on the AC
remote.pressUndo(); // Undo the last command (turn off the AC)
remote.pressButton(1); // Turn off the light
remote.pressUndo(); // Undo the last command (turn on the light)
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
