# Thread Pools and Executors in Java

---

# Why Not Create Threads Manually?

Creating threads manually for every task may work in small programs, but it becomes dangerous in real-world systems.

## Problems with Manual Thread Creation

### 1. Thread Explosion

If thousands of requests arrive simultaneously and every request creates a new thread:

- The OS cannot efficiently manage that many threads.
- Too many active threads reduce performance.
- System responsiveness decreases drastically.

---

### 2. Memory Issues

Every thread requires its own memory stack.

Typically:

- One thread ≈ 1 MB stack memory (default)

Creating thousands of threads may quickly exhaust system memory.

Example:

```text
10,000 threads × 1 MB = 10 GB memory
```

---

### 3. Thread Leaks

Sometimes threads are:

- Created
- Started
- But never terminated properly

This causes resource leaks and eventually crashes the application.

---

### 4. Context Switching Overhead

The CPU must constantly switch between threads.

This process is called:

## Context Switching

If too many threads exist:

- CPU spends more time switching threads
- Less time doing actual useful work

Performance becomes worse instead of better.

---

# Better Solution → Thread Pools

Instead of creating threads repeatedly:

- Create a fixed pool of worker threads
- Reuse them for multiple tasks

This is exactly what the Executor Framework provides.

---

# Real-Life Analogy

Instead of:

- Hiring a new chef for every customer

Use:

- A fixed kitchen staff that continuously handles incoming orders.

---

# Introduction to Executor Framework

The Executor Framework is a high-level replacement for manually managing threads.

It decouples:

| Responsibility  | Meaning                    |
| --------------- | -------------------------- |
| Task Submission | What work should be done   |
| Task Execution  | How and when the task runs |

---

# Main Benefits

- Thread reuse
- Better performance
- Easier concurrency management
- Controlled resource usage
- Built-in scheduling support

---

# Creating a Fixed Thread Pool

```java
ExecutorService executor =
        Executors.newFixedThreadPool(10);
```

This creates:

- A pool containing 10 reusable worker threads.

---

# Email Service Example

This example demonstrates:

- Using a Fixed Thread Pool
- Reusing worker threads
- Asynchronous task execution
- Graceful shutdown

Code is available in:

```text
EmailServiceExample.java
```

---

# Submitting Tasks

The Executor Framework mainly provides two ways to submit tasks.

---

# 1. execute(Runnable)

```java
executor.execute(() -> task);
```

## Characteristics

- Fire and forget
- No result returned
- Cannot track completion directly

Used when:

- You simply want the task to run.

---

# 2. submit(Runnable | Callable)

```java
executor.submit(() -> task);
```

## Characteristics

- Returns a Future object
- Supports result tracking
- Supports cancellation
- Supports asynchronous computation

---

# Future

A `Future` represents the result of an asynchronous computation.

Using `Future`, we can:

- Wait for task completion
- Retrieve results
- Cancel tasks
- Check task status

---

# Future Example

This example demonstrates:

- submit()
- Callable
- Future
- Blocking using get()

Code is available in:

```text
FutureExample.java
```

---

# Thread Starvation and Fairness

---

# Thread Starvation

Thread starvation occurs when:

- Long-running tasks occupy all available threads
- Smaller or important tasks keep waiting indefinitely

Example:

- Heavy report generation blocks
- Fast payment-processing tasks

---

# Fairness

Fairness ensures:

- Every task eventually gets CPU time
- No task waits forever

---

# Solutions

## 1. Priority Queues

Important tasks get executed first.

Example:

- Payment processing gets higher priority
- Analytics tasks get lower priority

---

## 2. Separate Thread Pools

Use different pools for different workloads.

Example:

```java
ExecutorService emailPool =
        Executors.newFixedThreadPool(5);

ExecutorService reportPool =
        Executors.newFixedThreadPool(2);
```

This prevents:

- Heavy tasks from blocking lightweight tasks.

---

# Types of Thread Pools

---

# 1. Fixed Thread Pool

## Mechanism

- Reuses N fixed threads.

## Best For

- Predictable workload
- CPU-bound tasks
- Controlled resource usage

Example:

```java
ExecutorService executor =
        Executors.newFixedThreadPool(5);
```

---

# 2. Cached Thread Pool

## Mechanism

- Creates threads as needed
- Reuses idle threads

## Best For

- Bursty traffic
- Short-lived asynchronous tasks

Example:

```java
ExecutorService executor =
        Executors.newCachedThreadPool();
```

---

# 3. Scheduled Thread Pool

## Mechanism

- Supports delayed execution
- Supports periodic execution

## Best For

- Background jobs
- Repeated maintenance tasks

Example:

```java
ScheduledExecutorService executor =
        Executors.newScheduledThreadPool(2);
```

---

# Scheduled Task Example

This example demonstrates:

- Periodic task execution
- Delayed execution
- ScheduledExecutorService

Code is available in:

```text
ScheduledExample.java
```

---

# Quick Comparison Table

| Feature        | Fixed Pool      | Cached Pool       | Scheduled Pool   |
| -------------- | --------------- | ----------------- | ---------------- |
| Thread Count   | Constant        | Dynamic           | Fixed/Dynamic    |
| Queue Type     | Unbounded Queue | Synchronous Queue | Delayed Queue    |
| Best For       | CPU-bound work  | Bursty tasks      | Periodic tasks   |
| Resource Usage | Controlled      | Can grow rapidly  | Controlled       |
| Task Type      | Stable workload | Short-lived tasks | Delayed/Repeated |

---

# Summary

The Executor Framework provides:

- Efficient thread management
- Thread reuse
- Better scalability
- Simpler concurrency APIs

Main Concepts Covered:

- Fixed Thread Pool
- Cached Thread Pool
- Scheduled Thread Pool
- execute()
- submit()
- Future
- Thread starvation
- Fairness
- Proper shutdown handling

The Executor Framework is the preferred way to handle multithreading in modern Java applications.
