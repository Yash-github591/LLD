# State Design Pattern

## Introduction

The **State Design Pattern** is a behavioral design pattern that allows an object to **change its behavior when its internal state changes**.

Instead of using large conditional statements (if-else or switch), the behavior is delegated to different **state objects**, making the code more modular and maintainable.

The object appears to change its class dynamically as its state changes.

---

## Key Components

1. **Context**  
   Maintains a reference to the current state and delegates behavior to it.

2. **State Interface**  
   Declares common methods (`next()`, `cancel()`) that all concrete states must implement.

3. **Concrete States**  
   Implement specific behavior for each state and handle transitions between states.

---

## Problem it Solves

Without the State Pattern, behavior is often controlled using multiple conditional statements:

```cpp
if(state == "OrderPlaced") {
    // do something
} else if(state == "Preparing") {
    // do something else
}
```

Issues:

- Hard to maintain and extend
- Violates Open/Closed Principle
- Business logic scattered across conditions
- Difficult to manage complex state transitions

---

## How State Pattern Helps

The State Pattern moves state-specific behavior into separate classes.

```cpp
order->next();
order->cancel();
```

Key idea:

- Context delegates work to current state
- Each state decides what happens next
- State transitions are handled internally

Benefits:

- Eliminates complex conditional logic
- Encapsulates behavior per state
- Makes transitions explicit and organized
- Improves scalability and readability

---

## Structure

### 1. State Interface

Defines behavior common to all states.

```cpp
class OrderState {
public:
    virtual void next(OrderContext* orderContext) = 0;
    virtual void cancel(OrderContext* orderContext) = 0;
    virtual string getStateName() = 0;
};
```

---

### 2. Concrete States

Each state implements behavior and transitions.

```cpp
class OrderPlacedState : public OrderState {
public:
    void next(OrderContext* orderContext) override;
    void cancel(OrderContext* orderContext) override;
};
```

```cpp
class PreparingState : public OrderState {
public:
    void next(OrderContext* orderContext) override;
    void cancel(OrderContext* orderContext) override;
};
```

```cpp
class OutForDeliveryState : public OrderState {
public:
    void next(OrderContext* orderContext) override;
    void cancel(OrderContext* orderContext) override;
};
```

```cpp
class DeliveredState : public OrderState {
public:
    void next(OrderContext* orderContext) override;
    void cancel(OrderContext* orderContext) override;
};
```

```cpp
class CancelledState : public OrderState {
public:
    void next(OrderContext* orderContext) override;
    void cancel(OrderContext* orderContext) override;
};
```

---

### 3. Context Class

Maintains the current state and delegates behavior.

```cpp
class OrderContext {
private:
    OrderState* currentState;

public:
    OrderContext();

    void setState(OrderState* state);
    void next();
    void cancel();
    string getStateName();
};
```

---

## Example Flow

```cpp
OrderContext order;

order.next();    // OrderPlaced → Preparing
order.next();    // Preparing → OutForDelivery
order.cancel();  // OutForDelivery → Cancelled
```

Execution steps:

1. Order starts in **OrderPlacedState**
2. Calling `next()` moves it to **PreparingState**
3. Another `next()` moves it to **OutForDeliveryState**
4. Calling `cancel()` transitions to **CancelledState**
5. Each state controls its own transitions

---

## State Transitions (Food Delivery Example)

| Current State  | Action | Next State     |
| -------------- | ------ | -------------- |
| OrderPlaced    | next   | Preparing      |
| OrderPlaced    | cancel | Cancelled      |
| Preparing      | next   | OutForDelivery |
| Preparing      | cancel | Cancelled      |
| OutForDelivery | next   | Delivered      |
| OutForDelivery | cancel | Not Allowed    |
| Delivered      | any    | No Change      |
| Cancelled      | any    | No Change      |

---

## State vs Strategy Pattern

Both **State** and **Strategy** are behavioral design patterns and have a similar structure, but their **intent and usage are different**.

### 🔹 Key Differences

| Feature             | State Pattern                           | Strategy Pattern                     |
| ------------------- | --------------------------------------- | ------------------------------------ |
| **Intent**          | Change behavior based on internal state | Choose algorithm at runtime          |
| **Focus**           | State transitions                       | Algorithm selection                  |
| **Control**         | State itself decides next transition    | Client decides which strategy to use |
| **Behavior Change** | Automatic (based on state)              | Manual (set by client)               |
| **Relationship**    | States are tightly coupled              | Strategies are independent           |
| **Example**         | Order lifecycle (Placed → Delivered)    | Sorting (QuickSort, MergeSort)       |

---

### 🔹 Code Perspective

#### State Pattern

```cpp
order->next();   // State internally decides next behavior
```

- Behavior changes automatically
- Logic is inside state classes

#### Strategy Pattern

```cpp
context->setStrategy(new ConcreteStrategy());
context->execute();
```

- Client chooses the strategy
- No internal transitions

---

### 🔹 When to Use Which

**Use State Pattern when:**

- Object behavior depends on its state
- State transitions are important
- You want to eliminate complex conditionals

**Use Strategy Pattern when:**

- You need multiple interchangeable algorithms
- Client should control behavior selection
- No state transitions are required

---

## When to Use

- When an object’s behavior depends on its state
- When there are many conditional statements based on state
- When transitions between states are well-defined
- When you want to follow the Open/Closed Principle

---

## Advantages

- Removes complex conditional logic
- Encapsulates state-specific behavior
- Makes code more readable and maintainable
- Easy to add new states without modifying existing code
- Promotes Single Responsibility Principle

---

## Disadvantages

- Increases number of classes
- Slightly more complex structure
- Can be overkill for simple state logic

---

## Summary

The State Design Pattern allows an object to **change its behavior dynamically** by delegating responsibilities to different state classes.

It replaces conditional logic with polymorphism and is ideal for systems where **state transitions drive behavior**, while Strategy is better suited for **choosing algorithms independently**.
