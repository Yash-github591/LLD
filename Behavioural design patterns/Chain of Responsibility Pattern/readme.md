# Chain of Responsibility Design Pattern

## Introduction

The **Chain of Responsibility Design Pattern** is a behavioral design pattern that allows a request to pass through a chain of handlers **until one of them processes it**.

Instead of sending a request directly to a specific receiver, the request is passed along multiple handlers, giving each one a chance to handle it.

This helps **decouple the sender from the receiver**, making the system more flexible and extensible.

---

## Key Components

1. **Handler (Abstract Class)**  
   Declares:
   - A method to handle requests
   - A reference to the next handler in the chain

2. **Concrete Handlers**  
   Implement the handling logic for specific types of requests.  
   If unable to handle, they forward the request to the next handler.

3. **Client**  
   Initiates the request and sets up the chain of handlers.

---

## Problem it Solves

When a request can be handled by multiple possible receivers, tightly coupling the sender to a specific receiver reduces flexibility.

Example:

```cpp
if(request == "technical") {
    // handle technical
} else if(request == "billing") {
    // handle billing
} else if(request == "general") {
    // handle general
}
```

Issues:

- Violates Open/Closed Principle
- Hard to extend (adding new handlers requires modifying existing code)
- Large conditional statements
- Tight coupling between sender and receiver

---

## How Chain of Responsibility Helps

Instead of using conditionals, requests are passed through a chain of handlers.

```cpp
technicalHandler.handleRequest("billing");
```

Flow:

- Request starts at the first handler
- Each handler decides:
  - Handle it → stop chain
  - Pass it → forward to next handler
- Continues until handled or chain ends

Benefits:

- Removes complex conditionals
- Decouples sender and receiver
- Easy to add/remove handlers
- Promotes cleaner and scalable design

---

## Structure

### 1. Handler (Abstract Class)

Defines the interface and link to next handler.

```cpp
class SupportHandler {
protected:
    SupportHandler* nextHandler;

public:
    virtual void handleRequest(const string& request) = 0;

    void setNextHandler(SupportHandler* next) {
        this->nextHandler = next;
    }
};
```

---

### 2. Concrete Handlers

Each handler processes a specific request type.

#### Technical Support Handler

```cpp
class TechnicalSupportHandler : public SupportHandler {
public:
    void handleRequest(const string& request) override {
        if (request == "technical") {
            cout << "Technical Support Handler is handling the request." << endl;
        } else if (nextHandler != nullptr) {
            nextHandler->handleRequest(request);
        } else {
            cout << "No handler available for the request." << endl;
        }
    }
};
```

---

#### Billing Support Handler

```cpp
class BillingSupportHandler : public SupportHandler {
public:
    void handleRequest(const string& request) override {
        if (request == "billing") {
            cout << "Billing Support Handler is handling the request." << endl;
        } else if (nextHandler != nullptr) {
            nextHandler->handleRequest(request);
        } else {
            cout << "No handler available for the request." << endl;
        }
    }
};
```

---

#### General Support Handler

```cpp
class GeneralSupportHandler : public SupportHandler {
public:
    void handleRequest(const string& request) override {
        if (request == "general") {
            cout << "General Support Handler is handling the request." << endl;
        } else if (nextHandler != nullptr) {
            nextHandler->handleRequest(request);
        } else {
            cout << "No handler available for the request." << endl;
        }
    }
};
```

---

#### Delivery Support Handler

```cpp
class DeliverySupportHandler : public SupportHandler {
public:
    void handleRequest(const string& request) override {
        if (request == "delivery") {
            cout << "Delivery Support Handler is handling the request." << endl;
        } else if (nextHandler != nullptr) {
            nextHandler->handleRequest(request);
        } else {
            cout << "No handler available for the request." << endl;
        }
    }
};
```

---

## Example Flow

```cpp
TechnicalSupportHandler technicalHandler;
BillingSupportHandler billingHandler;
GeneralSupportHandler generalHandler;
DeliverySupportHandler deliveryHandler;

// Setting up chain
technicalHandler.setNextHandler(&billingHandler);
billingHandler.setNextHandler(&generalHandler);
generalHandler.setNextHandler(&deliveryHandler);

// Requests
technicalHandler.handleRequest("technical");
technicalHandler.handleRequest("billing");
technicalHandler.handleRequest("general");
technicalHandler.handleRequest("delivery");
technicalHandler.handleRequest("unknown");
```

Execution steps:

1. Client creates all handlers
2. Handlers are linked to form a chain
3. Request is sent to the first handler
4. Each handler checks if it can process the request
5. If not, it forwards the request to the next handler
6. If no handler processes it, a fallback message is shown

---

## When to Use

- When multiple objects can handle a request
- When you want to avoid large conditional statements
- When the request sender should not know the receiver
- When handlers need to be dynamically arranged

---

## Advantages

- Reduces coupling between sender and receiver
- Follows Open/Closed Principle
- Easy to extend by adding new handlers
- Improves code readability and maintainability
- Flexible request processing flow

---

## Disadvantages

- Request might go unhandled
- Debugging can be harder due to chain flow
- Performance overhead if chain is long
- No guarantee which handler will process the request

---

## Summary

Chain of Responsibility Pattern allows a request to pass through a **chain of handlers**, where each handler gets a chance to process it.

It eliminates complex conditionals, improves flexibility, and makes the system easier to extend and maintain.
