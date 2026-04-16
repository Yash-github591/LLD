## Multithreading and Concurrency in C++

1. Program: A program is an executable(eg, chrome.exe) file that contains code(eg, chrome.cpp) that
   can be run on a computer. It is a static entity that resides on disk and can be executed
   multiple times.

2. Process: A process is an instance of a program that is currently running on a computer.

3. Thread: Thread is the smallest executable unit of a process. Threads are sub-tasks of processes.

---

### What are cores in CPU ?

A CPU core is an individual processing unit within a computer's central processing unit (CPU) that reads and executes instructions independently. Its like mini CPU inside CPU.

Key Details About CPU Cores:

1. Independent Processors: Each core acts as a separate, smaller processor that can work on different tasks at the same time.

2. Multi-core Advantages: More cores allow for better multitasking and faster performance when running complex applications like video editing or gaming, as described on the HP® Tech Takes page.

3. Cores vs. Threads: While cores are physical hardware, threads are virtual cores created by technologies like hyper-threading, which allow a single physical core to manage multiple tasks at once.

4. Types: Common configurations include dual-core (2), quad-core (4), hexa-core (6), and octa-core (8), as explained by Lenovo.

Analogy: If the CPU is a kitchen, a core is an individual chef. A single-core processor has one chef, while a quad-core processor has four chefs working simultaneously, allowing them to prepare meals faster, as suggested by this Reddit post.

---

### What is context switching ?

Context Switching is the process where the CPU stops executing one thread/process, save its state, and switches to another.

#### How a context switch happens ?

- The CPU saves the current thread's context.
- Loads the next thread's context.
- Resumes execution of new thead

Switching is managed by the thread scheduler.

- Takes time to save/load states
- Performance degradation due to high threads

---

### What is Multithreading ?

Multithreading is the ability of a program to run multiple threads(independent tasks) concurrently, either truly in parallel(multi cores) or via context switching(single core).

Each thread:

- Runs independently
- Shares the same memory space
- Performs a specific task

#### Why use ?

- Better performance
- Non blocking
- Resource sharing
- Scalability in backend systems

---

| Concurrency                                                                                | Parallelism                                                                 |
| ------------------------------------------------------------------------------------------ | --------------------------------------------------------------------------- |
| Means multiple tasks making progress over time, but not necessarily at the same exact time | Means multiple tasks executing at the exact same time and on multiple cores |
| Can work with one core                                                                     | Can work with multiple cores                                                |
| Tasks appear to run at the same time                                                       | Tasks actually run at the same time                                         |
| Focus is on structure — how to manage many tasks                                           | Focus is on execution — how to complete many tasks faster                   |

---

### Comparision between process and thread.

| Process                                 | Thread                                          |
| --------------------------------------- | ----------------------------------------------- |
| Independent program in execution        | Subunit of a process                            |
| Has its own memory                      | Shares memory with other threads in the process |
| Fully isolated                          | Not isolated                                    |
| Communication is complex (IPC, Sockets) | Communication is easy (shared memory)           |
| Overhead is heavy                       | Overhead is light                               |
| One process crash doesn't affect others | One thread crash may affect other threads       |
| Example: PostgreSQL                     | Example: Chrome tabs, Uber backend              |

---

#### When to use thread ?

- Tasks need to share data → Threads can directly access shared memory, making data exchange fast and simple without complex communication mechanisms.
- Low overhead is important → Threads are lightweight and faster to create, switch, and manage compared to processes.
- Tasks are part of the same layer → Threads work well when tasks belong to the same logical unit or module of an application.
- High performance needed → Threading enables parallel execution within a process, improving responsiveness and throughput.
- Tightly coupled behavior → Threads are ideal when tasks are closely related and frequently interact with each other.

#### When to use process ?

- Tasks require isolation → Processes run in separate memory spaces, ensuring strong isolation between different tasks.
- One process crash shouldn't affect others → A failure in one process does not directly impact other running processes.
- Security boundaries needed → Processes provide better security by restricting direct access to each other's memory and resources.
- Different tech stacks → Processes allow running components written in different languages or frameworks independently.
- Resource limits needed → Each process can have controlled resource usage (CPU, memory), improving system stability.
- Used by different users → Processes are suitable when multiple users or systems interact independently without sharing internal state.

---

### Fault tolerance

It is the ability of a system to keep functioning even when some components fail, ensuring minimal or no impact on user experience.  
It works by detecting failures, containing their impact, and recovering automatically.

Real life → A plane continues flying even if one engine fails due to built-in redundancy.

- Redundancy → Duplicate critical components so a backup can take over if one fails.
- Graceful degradation → System reduces functionality instead of completely failing.
- Self-healing → Automatically detects and recovers from failures without manual intervention.
- Error containment → Prevents failures from spreading to other parts of the system.

---

### Isolation

It means separating components so they operate independently, preventing one component’s behavior or failure from affecting others.  
It ensures safer, more predictable, and secure system design.

- Memory separation → Each component has its own memory space to avoid unintended interference.
- Failure containment → Errors in one component do not propagate to others.
- Security boundaries → Restricts access between components to protect sensitive data and operations.
- Predictable behavior → Independent execution ensures consistent and reliable system performance.

---
