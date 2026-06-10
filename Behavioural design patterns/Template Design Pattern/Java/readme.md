# Template Design Pattern

## Introduction

The **Template Design Pattern** is a behavioral design pattern that defines the **skeleton of an algorithm** in a base class while allowing subclasses to override specific steps **without changing the overall structure**.

Instead of rewriting the entire algorithm, subclasses only customize certain parts, ensuring consistency and code reuse.

This pattern is useful when multiple classes share the same workflow but differ in some steps.

---

## Problem it Solves

When multiple classes implement similar algorithms with slight variations, code duplication becomes a problem.

Example:

```java
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

```java
abstract class NotificationSender {
    // Template method
    public void sendNotification(String recipient, String message);

    // Common steps with default implementations (can be overridden if needed)
    protected void postSendAnalysis(String recipient);

    // Common step 1: Rate limit check
    private void rateLimitCheck(String recipient);

    // Common step 2: Validate recipient
    private void validateRecipient(String recipient);

    // Common step 3: Format message
    private String formatMessage(String message);

    // Common step 4: Pre-send audit
    private void preSendAudit(String recipient, String message);

    // Methods to be implemented by subclasses
    // (Must be protected in Java so subclasses can see them)
    protected abstract String composeMessage(String formattedMessage);
    protected abstract void sendMessage(String recipient, String composedMessage);
}
```

---

### 2. Concrete Classes

Implement the variable parts of the algorithm.

#### Email Notification

```java
class EmailNotification extends NotificationSender {
    // Specific implementation for composing email message
    @Override
    protected String composeMessage(String formattedMessage) {
        return "Email: " + formattedMessage; // Specific composition for email
    }

    // Specific implementation for sending email
    @Override
    protected void sendMessage(String recipient, String composedMessage) {
        System.out.println("Sending email to " + recipient + ": " + composedMessage);
    }
}
```

---

#### SMS Notification

```java
class SMSNotification extends NotificationSender {

    // Specific implementation for composing SMS message
    @Override
    protected String composeMessage(String formattedMessage) {
        return "SMS: " + formattedMessage; // Specific composition for SMS
    }

    // Specific implementation for sending SMS
    @Override
    protected void sendMessage(String recipient, String composedMessage) {
        System.out.println("Sending SMS to " + recipient + ": " + composedMessage);
    }

    // Optionally override post-send analysis for SMS
    @Override
    protected void postSendAnalysis(String recipient) {
        System.out.println("Performing SMS-specific post-send analysis for " + recipient);
    }
}
```

---

## Example Flow

```java
NotificationSender emailSender = new EmailNotification();
emailSender.sendNotification("user@example.com", "Hello, this is an email notification!");

System.out.println("\n--------------------------\n");

NotificationSender smsSender = new SMSNotification();
smsSender.sendNotification("1234567890", "Hello, this is an SMS notification!");
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
