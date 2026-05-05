# 🧵 Creating and Managing Threads in Java (Beginner to Deep Dive)

---

## 📌 Problem Statement

We want to simulate a real-world flow:

```
Place Order → Send SMS → Send Email → Calculate ETA
```

### ❓ Why use threads?

If done sequentially:

- SMS → 1 sec
- Email → 1.5 sec
- ETA → 2 sec

👉 Total = **4.5 seconds**

But:

- These tasks are **independent**
- They can run **in parallel**

👉 Threads help us **reduce total execution time**

### 🧠 Intuition

Think of this like a restaurant:

- One chef doing everything → slow
- Multiple chefs handling different tasks → faster

Threads are like **workers executing tasks simultaneously**

---

# 🧠 High-Level Design

| Task  | Type     | Reason           |
| ----- | -------- | ---------------- |
| SMS   | Runnable | No result needed |
| Email | Runnable | No result needed |
| ETA   | Callable | Needs result     |

### 🧠 Why this design?

- SMS & Email → we just trigger them, no need to wait for output
- ETA → we **must return a value**, so we use Callable

👉 Choosing the right abstraction is important for **clean design and performance**

---

# ⚙️ Thread Class (Core Concept)

## 💡 What is a Thread?

A thread is the **smallest unit of execution** inside a program.

- Every Java program starts with **one thread → main thread**
- When you create more threads → your program becomes **multithreaded**

### 🧠 Key Idea

A process (your Java program) can have **multiple threads running inside it**

---

## 🔧 How Thread Works Internally

When you call:

```java
Thread t = new Thread(task);
t.start();
```

Steps:

1. JVM requests OS to create a new thread
2. OS allocates resources (stack, registers, etc.)
3. JVM schedules the thread
4. JVM internally calls `run()` method
5. Code executes independently

### 🧠 Important Insight

- JVM **does not guarantee exact execution order**
- Threads are scheduled by OS → execution is **non-deterministic**

---

## ⚠️ `start()` vs `run()`

### ❌ Wrong

```java
t.run();
```

- Just a normal method call
- Runs on **main thread**
- No parallelism

---

### ✅ Correct

```java
t.start();
```

- Creates a new thread
- Calls `run()` internally
- Runs in parallel

---

### 🧠 Why this matters

This is one of the **most common beginner mistakes**.

👉 If you don’t use `start()`, you are **not using multithreading at all**

---

## 🧱 Ways to Create Threads

### 1. Extending Thread (Not Recommended)

```java
class MyThread extends Thread {
    public void run() {
        System.out.println("Running...");
    }
}
```

❌ Problems:

- Java does not support multiple inheritance
- You tightly couple **task logic with thread execution**
- Harder to reuse logic

---

### 2. Using Runnable (Recommended ✅)

```java
Thread t = new Thread(new Task());
```

✔ Separation of concerns  
✔ Better design  
✔ Reusable logic  
✔ Works with thread pools

---

## 🧠 Key Idea

```
Thread = HOW to run
Runnable = WHAT to run
```

👉 Always try to **separate execution from logic**

---

# 🔁 Runnable Interface (Fire-and-Forget Tasks)

## 💡 Definition

```java
public interface Runnable {
    void run();
}
```

---

## 📦 Example

```java
class SMSThreadRunnable implements Runnable {
    public void run() {
        System.out.println("SMS sent");
    }
}
```

---

## 🔍 What Runnable Represents

Runnable is simply:

👉 “A unit of work that needs to be executed”

It does not care:

- Who executes it
- When it executes

---

## 🔥 Characteristics

- No return value ❌
- Cannot throw checked exceptions ❌
- Lightweight and simple ✅

---

## 🧠 Use Case

```
Fire and Forget Tasks
```

Examples:

- Sending notifications
- Logging
- Background cleanup jobs

👉 You trigger them and move on

---

## ⏳ Waiting for Runnable Tasks

```java
thread.join();
```

👉 Makes the **main thread wait** until this thread finishes

### 🧠 Why needed?

Even fire-and-forget tasks sometimes must complete before program exits

---

## 🧠 Analogy

```
Runnable = Recipe
Thread = Cook
```

- Recipe defines WHAT to do
- Cook actually executes it

---

# 📞 Callable Interface (Result-Oriented Tasks)

## 💡 Why Callable?

Runnable cannot:

- Return values ❌
- Throw checked exceptions ❌

👉 But many real-world tasks need results

Example:

- Database query
- API call
- ETA calculation

---

## ✅ Callable Definition

```java
public interface Callable<V> {
    V call() throws Exception;
}
```

---

## 📦 Example

```java
class ETACalculator implements Callable<String> {
    public String call() {
        return "ETA is 30 mins";
    }
}
```

---

## 🔍 What Callable Represents

👉 A task that:

- Executes asynchronously
- Produces a result

---

## ⚠️ Important Limitation

Callable cannot run directly:

```java
new Thread(new ETACalculator()); ❌
```

Because Thread only accepts Runnable

---

## ✅ Solution: FutureTask

```java
FutureTask<String> task = new FutureTask<>(new ETACalculator("BLR"));
Thread t = new Thread(task);
t.start();
```

---

## 📥 Getting Result

```java
String result = task.get();
```

👉 Behavior:

- If result is ready → returns immediately
- If not → waits (blocking call)

---

## 🧠 Analogy

```
Callable = Task that produces result
Future = Ticket to collect result later
```

- You submit task
- Continue doing work
- Collect result when needed

---

# ⚔️ Runnable vs Callable

| Feature      | Runnable      | Callable     |
| ------------ | ------------- | ------------ |
| Method       | run()         | call()       |
| Return value | ❌ No         | ✅ Yes       |
| Exceptions   | ❌ Limited    | ✅ Supported |
| Use case     | Fire & Forget | Result-based |

---

# 🔄 Thread Lifecycle

## 📊 Stages

```
NEW → RUNNABLE → RUNNING → WAITING/BLOCKED → TERMINATED
```

---

## 📌 Detailed Explanation

### NEW

```java
Thread t = new Thread(...);
```

Thread is created but not started

---

### RUNNABLE

```java
t.start();
```

Thread is ready and waiting for CPU

---

### RUNNING

- Thread is actively executing `run()`

---

### WAITING / BLOCKED

```java
Thread.sleep(1000);
thread.join();
```

Thread is paused or waiting

---

### TERMINATED

- Execution is complete
- Thread cannot be restarted

---

## 🧠 Important Insight

Thread scheduling is handled by OS → **you cannot predict exact execution order**

---

# 🔗 Full Flow of Execution

```java
System.out.println("Order placed...");
```

### Start Threads

```java
smsThread.start();
emailThread.start();
etaThread.start();
```

---

### Parallel Execution

- SMS running independently
- Email running independently
- ETA calculation happening

---

### Wait for Completion

```java
smsThread.join();
emailThread.join();
```

👉 Ensures SMS & Email complete

---

### Get Result

```java
String eta = etaTask.get();
```

👉 Waits and retrieves result

---

### Final Output

```
Order placed...
SMS sent
Email sent
Received ETA: 30 mins
All tasks completed
```

---

# 🧠 Deep Insights

## ❓ Why not always use Callable?

- Slightly heavier than Runnable
- Requires Future handling
- Not needed if no result

---

## ❓ Why not always create Threads manually?

- Thread creation is expensive
- Too many threads → performance issues
- Hard to manage lifecycle

---

## ❓ Why Runnable is widely used?

- Simple
- Efficient
- Works seamlessly with thread pools

---

# 🧠 Final Mental Model

```
Task (Runnable / Callable)
        ↓
Wrapper (FutureTask if needed)
        ↓
Thread (Execution)
        ↓
Result (Future.get())
```

---

# ✅ Summary

- Threads enable parallel execution
- Runnable = task without result
- Callable = task with result
- FutureTask bridges Callable with Thread
- join() waits for threads
- get() waits for result
- Thread lifecycle explains execution flow
- Always separate **task logic** from **execution mechanism**

---
