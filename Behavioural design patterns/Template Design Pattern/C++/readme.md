# Template Design Pattern

## Introduction

The **Template Design Pattern** is a behavioral design pattern that defines the **skeleton of an algorithm** in a base class while allowing subclasses to override specific steps **without changing the overall structure**.

Instead of rewriting the entire algorithm, subclasses only customize certain parts, ensuring consistency and code reuse.

This pattern is useful when multiple classes share the same workflow but differ in some steps.

---

## Problem it Solves

When multiple classes implement similar algorithms with slight variations, code duplication becomes a problem.

Example:

```cpp
// Email sending logic
validate();
format();
sendEmail();
log();

// SMS sending logic
validate();
format();
sendSMS();
log();
```

Issues:

- Duplicate logic across classes
- Hard to maintain consistency
- Changes must be updated everywhere
- Violates DRY (Don't Repeat Yourself)

---

## How Template Pattern Helps

The Template Pattern moves the common algorithm structure to a base class and allows subclasses to define only the varying parts.

```text
sendNotification()
   ↓
rateLimitCheck()
   ↓
validateRecipient()
   ↓
formatMessage()
   ↓
preSendAudit()
   ↓
composeMessage()   ← (Subclass)
   ↓
sendMessage()      ← (Subclass)
   ↓
postSendAnalysis() ← (Optional override)
```

Key idea:

- Base class defines the **template method**
- Some steps are implemented in base class
- Some steps are declared as abstract
- Subclasses override only required parts

Benefits:

- Eliminates code duplication
- Enforces a consistent workflow
- Improves maintainability

---

## Structure

### 1. Abstract Class (Template)

Defines the template method and common steps.

```cpp
class NotificationSender {
public:
    void sendNotification(string recipient, string message);

protected:
    virtual void postSendAnalysis(string recipient);

private:
    void rateLimitCheck(string recipient);
    void validateRecipient(string recipient);
    string formatMessage(string message);
    void preSendAudit(string recipient, string message);

    virtual string composeMessage(string formattedMessage) = 0;
    virtual void sendMessage(string recipient, string composedMessage) = 0;
};
```

---

### 2. Concrete Classes

Implement the variable parts of the algorithm.

#### Email Notification

```cpp
class EmailNotification : public NotificationSender {
public:
    string composeMessage(string formattedMessage) override {
        return "Email: " + formattedMessage;
    }

    void sendMessage(string recipient, string composedMessage) override {
        cout << "Sending email to " << recipient << ": " << composedMessage << endl;
    }
};
```

---

#### SMS Notification

```cpp
class SMSNotification : public NotificationSender {
public:
    string composeMessage(string formattedMessage) override {
        return "SMS: " + formattedMessage;
    }

    void sendMessage(string recipient, string composedMessage) override {
        cout << "Sending SMS to " << recipient << ": " << composedMessage << endl;
    }

protected:
    void postSendAnalysis(string recipient) override {
        cout << "Performing SMS-specific post-send analysis for " << recipient << endl;
    }
};
```

---

## Example Flow

```cpp
NotificationSender* emailSender = new EmailNotification();
emailSender->sendNotification("user@example.com", "Hello Email!");

NotificationSender* smsSender = new SMSNotification();
smsSender->sendNotification("1234567890", "Hello SMS!");
```

Execution steps:

1. Client creates a concrete object
2. Calls the template method `sendNotification()`
3. Base class executes common steps:
   - Rate limit check
   - Validation
   - Formatting
   - Auditing
4. Subclass provides:
   - Message composition
   - Sending logic
5. Base class completes with post-processing

---

## Key Concepts

- **Template Method** → Defines the algorithm structure
- **Abstract Methods** → Must be implemented by subclasses
- **Hooks (Optional Overrides)** → Can be overridden if needed
- **Encapsulation of Workflow** → Algorithm flow remains fixed

---

## When to Use

- When multiple classes follow the same algorithm structure
- When only certain steps differ
- When you want to enforce a fixed workflow
- When code reuse is important

---

## Advantages

- Promotes code reuse
- Ensures consistent behavior
- Follows Open/Closed Principle
- Reduces duplication
- Improves maintainability

---

## Disadvantages

- Can lead to rigid design
- Increases inheritance complexity
- Hard to change algorithm structure later
- Overuse may lead to deep class hierarchies

---

## Summary

The Template Design Pattern defines a **fixed algorithm structure** in a base class while allowing subclasses to customize specific steps.

It helps in building **clean, reusable, and maintainable systems** by separating common workflow from variable behavior.
