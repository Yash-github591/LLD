# Observer Design Pattern

## Introduction

The **Observer Design Pattern** is a behavioral design pattern in which an object, called the **subject**, maintains a list of dependent objects called **observers**, and **notifies them automatically** whenever its state changes.

Instead of repeatedly checking for updates manually, observers are automatically informed when something important happens in the subject.

This pattern is widely used in **event-driven systems**, **notification services**, and **real-time update mechanisms**.

### Key Components

1. **Subject**  
   Maintains a list of observers and provides methods to:
   - `subscribe()` → add an observer
   - `unsubscribe()` → remove an observer
   - `notify()` → inform all observers about changes

2. **Observer Interface**  
   Declares the update method that observers must implement:
   - `update()` → receives notification from subject

3. **Concrete Subject**  
   Implements the subject logic and triggers notifications when state changes.

4. **Concrete Observers**  
   Implement the observer interface to receive updates.

---

## Problem it Solves

When multiple objects depend on the state of another object, tightly coupling them makes the system difficult to maintain and extend.

Example problem:

```java
class YoutubeChannel {
    public void uploadVideo(string title) {
        System.out.println("Notify Alice")
        System.out.println("Notify Bob")
        System.out.println("Notify Email Subscribers")
    }
}
```

Issues:

- Hardcoded notification logic
- Difficult to add new subscriber types
- Violates Open/Closed Principle
- High coupling between classes

---

## How Observer Helps

Observer pattern **decouples** the subject from observers by introducing a subscription mechanism.

```java
channel.subscribe(subscriber);
channel.uploadVideo("New Video");
```

Key idea:

- Observers register themselves to the subject
- Subject does not need to know observer details
- Subject simply broadcasts updates to all observers
- New observer types can be added easily

Benefits:

- Loose coupling between subject and observers
- Easy to add new subscriber types
- Supports dynamic subscription and removal
- Improves scalability and maintainability

---

## Structure

### 1. Observer Interface

Defines update method for receiving notifications.

```java
interface Subscriber {
    public void update(String videoTitle);
}
```

---

### 2. Subject Class

Maintains list of observers and notifies them.

```java
class YoutubeChannel {
    private ArrayList<Subscriber> subscribers = new ArrayList<>();

    public void subscribe(Subscriber subscriber);
    public void unsubscribe(Subscriber subscriber);
    public void notify(String videoTitle);
}
```

---

### 3. Concrete Subject

Triggers notifications when a new video is uploaded.

```java
void YoutubeChannel.uploadVideo(String videoTitle) {
    System.out.println("Uploading video: "+videoTitle);
    notify(videoTitle);
}
```

---

### 4. Concrete Observers

Observers implement update method to receive notifications.

```java
class MobileSubscriber implements Subscriber{
    @Override
    public void update(String videoTitle){
        System.out.println("");
        System.out.println("Mobile Subscriber "+name+" received notification: New video uploaded -" +videoTitle);
    }
}
```

```java
class EmailSubscriber implements Subscriber{
    @Override
    public void update(String videoTitle){
        System.out.println("");
        System.out.println("Email Subscriber "+email+" received notification: New video uploaded -"+videoTitle);
    }
}
```

---

## Example Flow

```java
YoutubeChannel channel = new YoutubeChannel("Tech Channel");

Subscriber mobileSub = new MobileSubscriber("Alice");
Subscriber emailSub = new EmailSubscriber("alice@example.com");

channel.subscribe(mobileSub);
channel.subscribe(emailSub);

channel.uploadVideo("Observer Pattern in C++");
```

Execution steps:

1. Client creates the subject (YoutubeChannel)
2. Observers subscribe to the subject
3. Subject state changes (new video uploaded)
4. Subject notifies all observers
5. Observers receive update via update() method

---

## When to Use

- When multiple objects depend on the state of another object
- When changes in one object should automatically notify others
- When you want to reduce tight coupling between classes
- When implementing event-based systems

---

## Advantages

- Promotes loose coupling
- Easy to extend with new observers
- Supports runtime subscription changes
- Follows Open/Closed Principle
- Improves code flexibility

---

## Disadvantages

- Can lead to many small classes
- Debugging may become harder with many observers
- Notification order is not always guaranteed

---

## Summary

Observer Pattern defines a **one-to-many dependency** between objects so that when one object changes state, all its dependents are notified automatically.

It helps build **scalable, loosely coupled, and maintainable systems**, especially useful in **event-driven architectures** and **notification systems**.
