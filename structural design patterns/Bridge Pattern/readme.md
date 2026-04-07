# Bridge Design Pattern

## Introduction

The Bridge Design Pattern is a structural design pattern that separates an abstraction from its implementation so that the two can vary independently.

It helps reduce tight coupling between high-level logic and low-level platform-specific implementations.

Instead of creating multiple subclasses for every possible combination, Bridge composes objects together using interfaces.

This improves flexibility, scalability, and maintainability.

---

## Problem it Solves

Sometimes a class needs to support multiple variations of both:

- **Abstraction (high-level behavior)**
- **Implementation (low-level details)**

If we try to handle all combinations using inheritance:

```cpp
class SDMobilePlayer {};
class SDWebPlayer {};
class SDSmartTVPlayer {};

class HDMobilePlayer {};
class HDWebPlayer {};
class HDSmartTVPlayer {};

class UltraHDMobilePlayer {};
class UltraHDWebPlayer {};
class UltraHDSmartTVPlayer {};
```

As combinations increase, the number of classes grows rapidly.

Problems caused:

- Class explosion
- Difficult to maintain
- Code duplication
- Hard to extend new variations
- Violates Open/Closed Principle

---

## How Bridge Helps

Bridge separates abstraction and implementation into different class hierarchies.

Instead of inheritance combinations, objects are composed dynamically.

Example:

```cpp
VideoPlayer* player = new MobilePlayer();
VideoQuality* quality = new HDQuality(player);

quality->play();
```

Both dimensions can change independently:

- Add new video quality without modifying player classes
- Add new device type without modifying quality classes

### Benefits

- Reduces class explosion
- Promotes composition over inheritance
- Supports independent extensibility
- Improves maintainability
- Follows Open/Closed Principle

---

## Structure of Bridge Pattern

Bridge divides system into two hierarchies:

### 1. Implementor Interface

Defines interface for implementation classes.

Example:

```cpp
class VideoPlayer {
public:
virtual void play() = 0;
};
```

---

### 2. Concrete Implementors

Provide platform-specific implementations.

Example:

```cpp
class SmartTVPlayer : public VideoPlayer {};
class MobilePlayer : public VideoPlayer {};
class WebPlayer : public VideoPlayer {};
```

Each class implements the play functionality differently.

---

### 3. Abstraction

Maintains reference to implementor object.

Delegates work to implementation.

Example:

```cpp
class VideoQuality {
protected:
VideoPlayer* player;

public:
virtual void play() = 0;
};
```

---

### 4. Refined Abstractions

Provide different variations of abstraction.

Example:

```cpp
class SDQuality : public VideoQuality {};
class HDQuality : public VideoQuality {};
class UltraHDQuality : public VideoQuality {};
```

Each quality delegates actual playing to VideoPlayer.

---

## Example Flow

HDQuality -> MobilePlayer

Execution flow:

1. Create implementation object (MobilePlayer)
2. Inject it into abstraction (HDQuality)
3. Call play()
4. Abstraction delegates work to implementation

Output example:

```
Playing video in High Definition (HD) quality
Playing video on Mobile Device
```

---

## When to Use Bridge Pattern

- When abstraction and implementation both vary
- When inheritance causes too many subclasses
- When platform-specific behavior changes frequently
- When you want to separate interface from implementation
- When following Open/Closed Principle

### Common real-world examples:

- Video players across devices
- Remote control and devices
- Database drivers
- Payment gateways
- Graphics rendering APIs
- Cross-platform UI frameworks

---

## When NOT to Use Bridge Pattern

- When abstraction and implementation are unlikely to change
- When simple inheritance works fine
- When design becomes unnecessarily complex
- When only one dimension varies

---

## Advantages

- Reduces class explosion
- Separates abstraction and implementation
- Improves flexibility
- Easier to extend new features
- Promotes clean architecture
- Supports runtime composition

---

## Disadvantages

- Increases number of classes
- Adds initial design complexity
- Requires understanding of composition
- May be overkill for simple systems

---

## Summary

Bridge Pattern decouples abstraction from implementation so both can evolve independently.

It is useful when systems need to support multiple combinations of behaviors without creating large inheritance hierarchies.

By using composition, the pattern keeps code flexible, scalable, and maintainable.
