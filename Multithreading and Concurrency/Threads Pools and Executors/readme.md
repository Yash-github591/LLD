# Concurrency in C++: Executors, Thread Pools, and Best Practices

---

## 1. Why NOT to Create Threads Manually in Real-World Systems

C++ provides `std::thread`, but directly creating threads for every task is not scalable.

### Problems with Manual Thread Creation:

- **Expensive Operation**
  - Thread creation/destruction involves OS overhead
- **No Reuse**
  - Each task creates a new thread → inefficient
- **Unbounded Thread Growth**
  - Too many threads can crash the system
- **Difficult Lifecycle Management**
  - Must handle `join()` / `detach()` manually
- **Complex Synchronization**
- **Poor Error Handling**

### Example (Bad Practice):

```cpp
for (int i = 0; i < 10000; i++) {
    std::thread([](){
        // task
    }).detach(); // dangerous
}
```

> ⚠️ This can exhaust system resources.

---

## 2. Introduction to Executor Framework (C++ Perspective)

C++ does not have a built-in executor framework like Java (pre-C++23), but the concept exists.

### What is an Executor?

> An **Executor** manages a pool of threads and executes submitted tasks.

### Conceptual Model:

```
Task → Queue → Thread Pool → Execution
```

### How C++ Achieves This:

- Custom thread pools
- `std::async`
- Task queues with worker threads

### Libraries Providing Executor-like Features:

- Boost.Asio
- Intel TBB
- Folly (Facebook)
- C++23 `std::execution` (still evolving)

---

## 3. Methods to Submit Tasks

### 3.1 Using `std::async`

```cpp
auto future = std::async(std::launch::async, [](){
    return 42;
});
```

- ✔ Simple to use
- ❌ No control over thread pooling

---

### 3.2 Using a Thread Pool (Recommended)

```cpp
threadPool.submit([](){
    // task
});
```

Typical components:

- `std::queue<std::function<void()>>`
- `std::mutex`
- `std::condition_variable`

---

### 3.3 Using `std::packaged_task` + `std::future`

```cpp
std::packaged_task<int()> task([](){ return 10; });
auto future = task.get_future();

std::thread(std::move(task)).detach();
```

- ✔ Supports returning results
- ❌ Still manual thread management unless pooled

---

## 4. Shutting Down Executors

Improper shutdown can cause:

- Memory leaks
- Zombie threads
- Undefined behavior

### Proper Shutdown Steps:

1. Stop accepting new tasks
2. Complete existing tasks
3. Notify all worker threads
4. Join all threads

### Example:

```cpp
stop = true;
condition.notify_all();

for (auto &t : workers) {
    t.join();
}
```

---

### Types of Shutdown:

#### Graceful Shutdown

- Finish all queued tasks before stopping

#### Immediate Shutdown

- Stop instantly (may drop tasks)

---

## 5. Thread Starvation and Fairness

### Thread Starvation

> When some tasks never get CPU time because others dominate execution.

#### Causes:

- Long-running or infinite tasks
- Poor scheduling
- High-priority tasks blocking others

#### Example:

```cpp
while(true) {
    // blocks thread forever
}
```

---

### Fairness

> Ensuring all tasks get a fair chance to execute.

#### Techniques:

- FIFO queues
- Priority queues
- Work-stealing algorithms

> ⚠️ Most basic C++ thread pools are not strongly fair by default.

---

## 6. When to Use: Fixed vs Cached vs Scheduled Thread Pools

C++ does not provide these directly, but you can implement them.

---

### 6.1 Fixed Thread Pool

#### Concept:

- Fixed number of worker threads
- If a new task comes, it is added in the queue until a thread becomes available to take up that task

#### Use When:

- CPU-bound tasks
- Predictable workload

#### Example:

```cpp
ThreadPool pool(4); // 4 threads
```

#### Pros:

- Controlled resource usage
- Stable performance

#### Cons:

- Tasks may queue up under heavy load

---

### 6.2 Cached Thread Pool

#### Concept:

- Dynamically grows and shrinks thread count
- When a new task comes and all the threads are occupied, a new thread gets created for that task and added into the pool
- When a thread becomes idle for a certain time, it gets removed from the pool

#### Use When:

- I/O-bound tasks
- Many short-lived tasks

#### Pros:

- Flexible
- Handles sudden bursts

#### Cons:

- Risk of creating too many threads

---

### 6.3 Scheduled Thread Pool

#### Concept:

- Executes tasks after a delay or periodically

#### Implementation in C++:

- `std::chrono`
- `std::this_thread::sleep_for`
- Timer-based threads

#### Example:

```cpp
std::this_thread::sleep_for(std::chrono::seconds(5));
```

#### Use When:

- Background jobs
- Periodic tasks
- Timers

---

## Final Summary

| Concept         | Key Idea                  |
| --------------- | ------------------------- |
| Manual Threads  | Not scalable              |
| Executor        | Manages threads and tasks |
| Task Submission | `async`, thread pool      |
| Shutdown        | Must be handled carefully |
| Starvation      | Tasks may never execute   |
| Fairness        | Balanced scheduling       |
| Fixed Pool      | Stable, CPU-bound         |
| Cached Pool     | Flexible, I/O-heavy       |
| Scheduled Pool  | Time-based execution      |

---

## Conclusion

In modern C++ systems, **thread pools and executor-like designs** are essential for:

- Performance
- Scalability
- Resource control

Avoid raw thread creation unless absolutely necessary, and prefer structured concurrency patterns.

---

### Threads Pool in C++

[Threads Pool in C++](https://medium.com/@bhushanrane1992/getting-started-with-c-thread-pool-b6d1102da99a)

---
