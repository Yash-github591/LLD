# Java Concurrency and Synchronization Tools

## Introduction

In modern multi-threaded applications, managing shared resources is critical. Java provides advanced concurrency utilities in the `java.util.concurrent` package that offer more flexibility, power, and performance than the basic `synchronized` keyword.

Instead of relying solely on intrinsic monitor locks, developers use **Explicit Locks** and **Semaphores** to control thread access, manage resource limits, and optimize for specific traffic patterns like read-heavy workloads.

### Key Components

1. **ReentrantLock** An explicit lock that allows a thread to acquire the same lock multiple times (reentrancy). It provides advanced features like fairness, timeouts, and interruption handling.

2. **ReadWriteLock** A specialized lock interface that separates access into **Read** (shared) and **Write** (exclusive) locks, significantly optimizing performance for read-heavy data.

3. **Semaphore** A permit-based concurrency control primitive that limits the number of threads accessing a resource to a fixed value ($N$).

4. **Condition Variables** Associated with locks to provide a more nuanced way for threads to wait for specific conditions (similar to wait/notify).

---

## Problem it Solves: Race Conditions

When multiple threads access and modify shared data simultaneously, a **Race Condition** occurs. The final outcome depends on the unpredictable timing of thread execution, leading to data corruption.

**Example: Bank Management System (BMS)**

```java
// Problematic code without synchronization
private int balance = 150;

public void withdraw(int amount) {
    if (balance >= amount) { // Multiple threads could pass this check simultaneously
        balance -= amount;    // Over-withdrawal occurs
    }
}

```

**Issues with standard `synchronized`:**

- **No Timeout:** Threads may block forever if a lock is never released.
- **Uninterruptible:** A thread waiting for a synchronized block cannot be interrupted.
- **No Fairness:** No guarantee on which thread gets the lock next (potential starvation).
- **Block-Scoped:** Cannot acquire a lock in one method and release it in another.

---

## How Explicit Locks Help

Explicit locks provide a **standard and consistent way** to handle complex synchronization scenarios by giving the developer manual control over the lock lifecycle.

```java
lock.lock();
try {
    // Critical Section: Access shared resource
} finally {
    lock.unlock(); // Always release in finally block to prevent deadlocks
}

```

**Key Benefits:**

- **Manual Control:** Explicit `lock()` and `unlock()` calls.
- **Non-blocking attempts:** `tryLock()` returns false immediately if the lock is busy.
- **Patience window:** `tryLock(time, unit)` waits for a lock before giving up.
- **Fairness Policy:** Optionally grants the lock to the longest-waiting thread.

---

## Structure & Implementations

### 1. ReentrantLock (Mutual Exclusion)

Implements basic mutual exclusion with "reentrant" capabilities, meaning a thread can re-acquire the same lock it already holds without deadlocking itself.

```java
class TicketBooking {
    private int availableSeats = 1;
    private final ReentrantLock lock = new ReentrantLock();

    public void bookTicket(String user) {
        lock.lock();
        try {
            if (availableSeats > 0) {
                availableSeats--;
                System.out.println(user + " successfully booked.");
            }
        } finally {
            lock.unlock();
        }
    }
}

```

---

### 2. TryLock with Timeout

Used to prevent system hangs by allowing a thread to "give up" if the resource is unavailable.

```java
public void bookWithTimeout(String user) {
    boolean acquired = false;
    try {
        acquired = lock.tryLock(2, TimeUnit.SECONDS);
        if (acquired) {
            // Modify shared data
        } else {
            System.out.println("Server busy, try again later.");
        }
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    } finally {
        if (acquired) lock.unlock();
    }
}

```

---

### 3. ReadWriteLock (Optimization)

Allows multiple readers to access data simultaneously but requires exclusive access for writers.

```java
class StockData {
    private double price = 100.0;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public void readPrice() {
        lock.readLock().lock(); // Multiple threads can execute this concurrently
        try { System.out.println("Price: " + price); }
        finally { lock.readLock().unlock(); }
    }

    public void updatePrice(double newPrice) {
        lock.writeLock().lock(); // Blocks all readers and other writers
        try { price = newPrice; }
        finally { lock.writeLock().unlock(); }
    }
}

```

---

### 4. Semaphore (Resource Throttling)

Limits access based on a number of **permits**. Ideal for limiting concurrent connections.

```java
class TUFPlusAccount {
    private final Semaphore deviceSlots = new Semaphore(3); // Max 3 concurrent devices

    public void login(String user) {
        if (deviceSlots.tryAcquire()) {
            System.out.println(user + " logged in.");
        } else {
            System.out.println("Login failed: Maximum device limit reached.");
        }
    }

    public void logout() {
        deviceSlots.release(); // Returns a permit to the pool
    }
}

```

---

## Comparison Summary

| Feature             | Monitor (`synchronized`) | ReentrantLock     | Semaphore           |
| ------------------- | ------------------------ | ----------------- | ------------------- |
| **Concurrency**     | 1 Thread                 | 1 Thread          | **N Threads**       |
| **Reentrant**       | Yes                      | Yes               | No                  |
| **Timeout Support** | No                       | Yes               | Yes                 |
| **Fairness Option** | No                       | Yes               | Yes                 |
| **Ownership**       | Implicit (Thread)        | Explicit (Thread) | None (Permit-based) |

---

## When to Use

- Use **synchronized** for very simple, block-level locking where performance isn't a bottleneck.
- Use **ReentrantLock** for complex logic, requirement of fairness, or when you need to attempt a lock without blocking indefinitely.
- Use **ReadWriteLock** for caches or data structures that are read thousands of times but updated rarely.
- Use **Semaphore** to throttle resource usage, such as limiting database connections or API rate limiting.

---

## Summary

Java Concurrency tools provide a **robust framework for building thread-safe applications**. While `synchronized` is a simple starting point, `ReentrantLock`, `ReadWriteLock`, and `Semaphore` offer the granular control necessary for high-performance, enterprise-grade systems by separating traversal/access logic from the shared resource itself.
