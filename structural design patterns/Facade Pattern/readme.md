# Facade Design Pattern

## Introduction

The Facade Design Pattern is a structural design pattern that provides a simplified interface to a complex subsystem.

It hides the complexity of business logic using multiple classes and exposes a single, easy-to-use interface for the client.

Instead of interacting with multiple subsystem classes directly, the client communicates only with the Facade.

In the Facade Pattern, there are typically two main parts:

1. Subsystem Classes
   - These are the complex classes that perform the actual work.
   - Example: `DVDPlayer`, `Projector`, `SoundSystem`

2. Facade
   - Provides a simplified interface to control the subsystem.
   - Example: `HomeTheaterFacade`

Example:
Instead of calling each subsystem manually:

```cpp
DVDPlayer dvd("Inception");
dvd.play();

Projector projector("HDMI");
projector.setInput();

SoundSystem sound(10);
sound.setVolume();
```

The client can simply call:

```cpp
HomeTheaterFacade homeTheater("Inception", 10, "HDMI");
homeTheater.startHomeTheater();
```

---

## Problem it Solves

Complex subsystems often require multiple steps and complex business logic to perform a single task.

Example:

```cpp
DVDPlayer dvd("Inception");
dvd.play();

Projector projector("HDMI");
projector.setInput();

SoundSystem sound(10);
sound.setVolume();
```

Issues:

- Client must know details of all subsystem classes
- Code becomes harder to maintain
- Tight coupling between client and subsystem
- Increased complexity in client code
- Repeated logic across different clients

---

## How Facade Helps

Facade provides a unified class that simplifies subsystem usage:

```cpp
HomeTheaterFacade* homeTheater = new HomeTheaterFacade("Inception", 10, "HDMI");

homeTheater->startHomeTheater();
```

## Structure

Components of Facade Pattern:

1. Facade
   - Provides simplified interface
   - Coordinates subsystem objects
   - Example: `HomeTheaterFacade`

2. Subsystem Classes
   - Perform actual operations
   - Examples:
     - `DVDPlayer`
     - `Projector`
     - `SoundSystem`

3. Client
   - Uses only the Facade
   - Does not directly interact with subsystem classes
   - Example: `main()`

### Tip: Always depend on interfaces rather than concrete implementations when using the Facade Pattern. This allows for greater flexibility and easier maintenance in the future.

## Example Flow

```cpp
HomeTheaterFacade* homeTheater = new HomeTheaterFacade("Inception", 10, "HDMI");

homeTheater->startHomeTheater();
```

Output:
Playing movie: Inception
Projector input set to: HDMI
Sound System volume set to: 10
Home Theater System Started...

Steps:

1. Client creates Facade object
2. Facade internally creates subsystem objects
3. Facade coordinates calls between subsystems
4. Client interacts only with the Facade
5. Subsystem complexity is hidden from client

---

## When to Use

- System has many complex classes
- Want a simple interface for a complex workflow
- Reduce dependencies between client and subsystem
- Improve code readability
- Encapsulate workflow logic
- Layered architecture design

---

## When Not to Use

- System is already simple
- Adds unnecessary abstraction
- Client needs direct access to subsystem features
- Too many facades can make design confusing

---

## Advantages

- Simplifies complex systems
- Reduces coupling between client and subsystem
- Improves code readability
- Centralizes workflow logic
- Easier to maintain and extend
- Follows Single Responsibility Principle
- Supports Open/Closed Principle

---

## Disadvantages

- Facade can become overly complex if too many responsibilities are added
- Hides subsystems functionalities from the client
- Can introduce extra layer of abstraction

---

## Summary

Use the Facade Pattern when working with complex subsystems that require multiple steps to perform a task.

Facade provides a simple, high-level interface that hides internal complexity and improves code organization, maintainability, and readability.
