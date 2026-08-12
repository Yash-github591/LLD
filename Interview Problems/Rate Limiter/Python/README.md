# Rate Limiter - Low Level Design (Python)

A production-oriented, interview-ready Low-Level Design (LLD) for a rate limiter.

The design supports multiple rate-limiting algorithms, user-tier policies, thread-safe state updates, an in-memory store, and a clean storage abstraction that can be backed by Redis in a distributed deployment.

---

## Table of Contents

1. [Problem Statement](#problem-statement)
2. [How to Approach the Design](#how-to-approach-the-design)
3. [Functional Requirements](#functional-requirements)
4. [Non-Functional Requirements](#non-functional-requirements)
5. [Assumptions](#assumptions)
6. [Core Entities](#core-entities)
7. [Interaction Flow](#interaction-flow)
8. [Class Structure](#class-structure)
9. [Design Patterns](#design-patterns)
10. [Algorithms](#algorithms)
11. [Algorithm Comparison](#algorithm-comparison)
12. [Thread Safety and Concurrency](#thread-safety-and-concurrency)
13. [Distributed Rate Limiting](#distributed-rate-limiting)
14. [API Contract](#api-contract)
15. [Edge Cases](#edge-cases)
16. [Complexity Analysis](#complexity-analysis)
17. [Testing Strategy](#testing-strategy)
18. [Design Principles](#design-principles)
19. [Key Implementation Decisions](#key-implementation-decisions)
20. [Project Structure](#project-structure)
21. [How to Run](#how-to-run)
22. [Possible Extensions](#possible-extensions)

---

## Problem Statement

Design a rate limiter that controls how frequently a client can access an API.

For example:

```text
FREE user:
10 requests / 60 seconds

PREMIUM user:
100 requests / 60 seconds
```

When the limit is exceeded, the request is rejected.

The design should allow multiple rate-limiting algorithms without changing the service layer.

---

## How to Approach the Design

The design follows the same requirements-first approach that is useful for LLD interviews:

1. **Requirements** — define what the system does and how well it must do it.
2. **Entities** — identify the objects and state involved.
3. **Interaction flow** — describe what happens for every request.
4. **Class structure** — assign responsibilities.
5. **Patterns** — introduce abstractions only where they solve a real problem.
6. **Concurrency and failure cases** — ask what happens when requests arrive together or state is lost.
7. **Distributed design** — determine what changes when multiple application instances share the workload.

---

## Functional Requirements

### Request validation

- Accept a request identified by a client/user ID.
- Determine the user's rate-limit policy.
- Apply the configured rate-limiting algorithm.
- Return whether the request is allowed or rejected.
- Return useful metadata such as remaining requests and retry time.

### Multiple algorithms

Support:

- Token Bucket
- Fixed Window
- Sliding Window Log

The base `RateLimiter` abstraction makes additional algorithms easy to add.

### User tiers

The system supports different policies for different tiers.

Example:

```text
FREE     -> Token Bucket -> 10 / 60 sec
PREMIUM  -> Fixed Window -> 100 / 60 sec
```

### Configuration

A policy contains:

- maximum requests
- window duration
- algorithm

### Concurrency

Multiple threads may call the rate limiter for the same user simultaneously.

The check/update operation must be atomic.

### Independent users

State for user A must never consume user B's quota.

---

## Non-Functional Requirements

### Low latency

Rate-limit checks should be very fast because they execute before application business logic.

### High throughput

The rate limiter should support many concurrent requests.

### Thread safety

Concurrent requests must not bypass the configured limit.

### Scalability

The design should support many users and multiple application instances.

### Availability

The rate limiter should have a defined behavior when its shared state store is unavailable.

For a strict rate limiter, failing closed is safer for protecting downstream services. For availability-first systems, a controlled fail-open policy may be appropriate.

### Extensibility

Adding a new algorithm should not require changes to the service layer.

### Consistency

In a distributed deployment, all application instances should see the same rate-limit state.

---

## Assumptions

- A client is identified by a stable `user_id`.
- Limits are evaluated independently for each user.
- The example uses seconds as the time unit.
- The in-memory implementation is intended for a single process.
- A distributed deployment should use a shared store such as Redis.
- Redis updates for a production implementation must be atomic, typically using Lua scripts or equivalent server-side atomic operations.

---

## Core Entities

### User

```text
User
├── user_id
└── tier
```

### RateLimitPolicy

```text
RateLimitPolicy
├── algorithm
├── max_requests
└── window_in_seconds
```

This is the business policy. It is deliberately separated from the algorithm implementation.

### RateLimitResult

```text
RateLimitResult
├── allowed
├── limit
├── remaining
├── retry_after_seconds
└── message
```

This is a response object, not persistent rate-limit state.

### Token Bucket State

```text
TokenBucketState
├── tokens
└── last_refill_time
```

### Sliding Window State

```text
SlidingWindowState
└── request timestamps
```

### RateLimitStore

A storage abstraction used by algorithms to keep state.

```text
RateLimitStore
      |
      +-- InMemoryRateLimitStore
      |
      +-- RedisRateLimitStore (production/distributed)
```

---

## Interaction Flow

### Generic request flow

```text
Client
  |
  v
RateLimiterService
  |
  +--> identify policy
  |
  +--> RateLimiterFactory
  |
  v
RateLimiter
  |
  +--> RateLimitStore
  |
  v
RateLimitResult
  |
  +--> allowed -> continue to API
  |
  +--> rejected -> HTTP 429
```

### Token Bucket flow

```text
Request
  |
  v
Acquire user lock
  |
  v
Read bucket state
  |
  v
Calculate refill
  |
  v
Consume one token if available
  |
  +--> token available -> ALLOW
  |
  +--> no token -> REJECT
```

### Fixed Window flow

```text
Request
  |
  v
Find current window
  |
  v
Read request count
  |
  +--> new window -> reset count
  |
  +--> existing window -> increment if below limit
```

### Sliding Window Log flow

```text
Request
  |
  v
Remove timestamps outside the window
  |
  v
Count remaining timestamps
  |
  +--> below limit -> append timestamp and ALLOW
  |
  +--> at limit -> REJECT
```

---

## Class Structure

```text
                         RateLimiter
                             ^
                             |
              +--------------+--------------+
              |              |              |
       FixedWindow      SlidingWindow    TokenBucket
        RateLimiter         Log           RateLimiter
              |              |              |
              +--------------+--------------+
                             |
                       RateLimitStore
                             ^
                             |
                 +-----------+-----------+
                 |                       |
          InMemoryStore             RedisStore
```

Service/policy layer:

```text
User
 |
 v
RateLimiterService
 |
 +--> RateLimitPolicy
 |
 +--> RateLimiterFactory
 |
 +--> RateLimiter
```

---

## Design Patterns

### Strategy Pattern

`RateLimiter` is the strategy interface.

```python
allow_request(user_id)
```

Concrete strategies:

- `TokenBucketRateLimiter`
- `FixedWindowRateLimiter`
- `SlidingWindowLogRateLimiter`

The service does not need to know the algorithm's internal details.

### Factory Pattern

`RateLimiterFactory` creates the correct strategy based on `RateLimitType`.

### Dependency Inversion

Algorithms depend on `RateLimitStore`, not directly on a dictionary or Redis client.

This makes the algorithm testable and allows storage to change without rewriting the algorithm.

### Repository/Store Abstraction

`RateLimitStore` hides persistence details.

The current project includes a thread-safe in-memory implementation. A Redis implementation can be introduced without changing the service API.

---

## Algorithms

### 1. Fixed Window

Example:

```text
5 requests / 10 seconds

0 ---------------- 10 ---------------- 20
       window 1              window 2
```

The counter resets when a new window starts.

**Pros**
- Very simple
- O(1) state per user
- O(1) request processing

**Cons**
- Boundary burst problem

Example:

```text
12:00:59 -> 5 requests
12:01:00 -> 5 requests
```

Ten requests can happen almost immediately even though the configured limit is five per minute.

---

### 2. Sliding Window Log

Store timestamps of recent requests:

```text
[1001, 1004, 1008, 1012]
```

When a request arrives:

1. Remove timestamps older than the window.
2. Count the remaining timestamps.
3. Reject if the limit is reached.
4. Otherwise append the new timestamp.

**Pros**
- Accurate sliding-window behavior
- No fixed-window boundary burst

**Cons**
- More memory for high traffic
- Requires timestamp cleanup

---

### 3. Token Bucket

A bucket contains tokens.

```text
capacity = 5

[ token ][ token ][ token ][ token ][ token ]
```

Each request consumes one token.

Tokens are replenished gradually.

```text
10 requests / 60 sec
=> refill interval = 6 sec/token
```

**Pros**
- Supports controlled bursts
- O(1) state per user
- Good general-purpose algorithm

**Cons**
- More state/calculation than fixed window

---

## Algorithm Comparison

| Algorithm | State | Burst Support | Accuracy | Memory |
|---|---|---:|---:|---:|
| Fixed Window | Counter | High at boundary | Medium | Low |
| Sliding Window Log | Timestamps | Low/controlled | High | High |
| Token Bucket | Tokens + timestamp | Controlled | High for rate control | Low |

---

## Thread Safety and Concurrency

This is a critical part of the design.

Consider:

```text
available tokens = 1
```

Two threads arrive together:

```text
Thread A -> check tokens -> 1
Thread B -> check tokens -> 1
```

If both then decrement independently, two requests may be allowed using one token.

Therefore:

```text
check + refill + update
```

must be one atomic critical section.

The implementation uses a lock per user:

```python
with store.lock_for(user_id):
    # read
    # calculate
    # update
```

A per-user lock is preferable to one global lock because:

```text
User A -> can proceed independently
User B -> can proceed independently
```

Only concurrent operations for the same user contend with each other.

---

## Distributed Rate Limiting

The in-memory implementation works for one application process:

```text
             Application
                  |
            In-memory state
```

It is not sufficient for:

```text
              Load Balancer
             /      |      \
            v       v       v
         Server1 Server2 Server3
             \      |      /
              \     |     /
                  Redis
```

Without shared state, a user can bypass the intended limit by sending requests to different servers.

### Production design

```text
API Server 1 ----\
API Server 2 -----+----> Redis
API Server 3 ----/
```

The Redis store should perform state transitions atomically.

For Token Bucket, the ideal Redis operation is:

```text
read bucket
calculate refill
consume token
write bucket
return result
```

as one server-side atomic operation, typically a Lua script.

For Fixed Window, an atomic `INCR` plus an appropriate TTL strategy can be used.

For Sliding Window Log, a sorted set with timestamp scores is a common Redis representation.

---

## API Contract

A useful application-facing result is:

```json
{
  "allowed": false,
  "limit": 10,
  "remaining": 0,
  "retry_after_seconds": 4,
  "message": "Rate limit exceeded"
}
```

An HTTP API can map this to:

```text
200 -> allowed
429 -> rejected
```

Useful headers:

```text
X-RateLimit-Limit
X-RateLimit-Remaining
Retry-After
```

---

## Edge Cases

### 1. Two concurrent requests

The same user's state must be updated atomically.

### 2. Multiple users

User A's requests must never consume User B's quota.

### 3. New user

The first request should initialize the user's state correctly.

### 4. User becomes inactive

In-memory state should eventually be evicted in a long-running service.

A distributed implementation can use Redis TTLs where appropriate.

### 5. Server restart

In-memory state is lost.

This is acceptable for a demo but not necessarily for a strict distributed production requirement.

### 6. Shared-store failure

Choose and document one policy:

```text
FAIL CLOSED -> reject to protect downstream systems
FAIL OPEN   -> allow to preserve availability
```

The correct choice depends on business requirements.

### 7. Clock changes

Time-based algorithms should use a monotonic clock where possible for local elapsed-time calculations.

The implementation uses an injectable `Clock` abstraction so the behavior is testable and the time source can be changed.

### 8. Configuration changes

Changing limits while users already have state requires an explicit policy.

For example, a token bucket can cap existing tokens to the new capacity.

### 9. Huge user population

State must not grow forever.

Use eviction/TTL in a production implementation.

---

## Complexity Analysis

Let `U` be the number of active users and `R` the number of requests retained by a sliding-window user.

### Fixed Window

```text
Time:  O(1)
Space: O(U)
```

### Token Bucket

```text
Time:  O(1)
Space: O(U)
```

### Sliding Window Log

```text
Time:  Amortized O(1)
Space: O(R)
```

The sliding log may consume significantly more memory under high request rates.

---

## Testing Strategy

The project includes tests for:

### Functional behavior

- requests within the limit
- requests above the limit
- independent users
- new windows
- token refill
- sliding-window expiry

### Concurrency

For:

```text
limit = 10
20 simultaneous requests
```

exactly 10 should be accepted.

### Deterministic time

Algorithms receive a `Clock` abstraction so tests can advance time without sleeping.

This makes tests fast and deterministic.

---

## Design Principles

### Single Responsibility Principle

- `RateLimiterService` handles orchestration.
- `RateLimitPolicy` represents configuration.
- Each algorithm handles one rate-limiting strategy.
- `RateLimitStore` handles state storage.
- `RateLimiterFactory` handles object creation.

### Open/Closed Principle

A new algorithm can be introduced by implementing `RateLimiter` and adding it to the factory.

Existing service code does not need to change.

### Dependency Inversion Principle

Algorithms depend on the `RateLimitStore` abstraction.

They do not need to know whether state is stored in memory or Redis.

### Interface Segregation

The abstractions expose only operations required by their consumers.

---

## Key Implementation Decisions

### Why per-user locks?

A global lock would serialize unrelated users.

Per-user locking allows:

```text
User A -> lock A
User B -> lock B
```

at the same time.

### Why separate policy from limiter?

This prevents business configuration from becoming tightly coupled to algorithm code.

```text
Policy:
10 requests / 60 sec

Algorithm:
Token Bucket
```

These are separate concepts.

### Why a store abstraction?

It lets the same algorithm work with:

```text
InMemoryRateLimitStore
RedisRateLimitStore
```

without changing the rate limiter's public behavior.

### Why an injectable clock?

Time is difficult to test when directly calling `time.time()` everywhere.

Injecting `Clock` lets tests control time deterministically.

---

## Project Structure

```text
rate_limiter_lld_python/
│
├── enums/
│   ├── __init__.py
│   ├── rate_limit_type.py
│   └── user_tier.py
│
├── model/
│   ├── __init__.py
│   ├── rate_limit_policy.py
│   ├── rate_limit_result.py
│   └── user.py
│
├── clock/
│   ├── __init__.py
│   └── clock.py
│
├── store/
│   ├── __init__.py
│   └── rate_limit_store.py
│
├── limiter/
│   ├── __init__.py
│   ├── rate_limiter.py
│   ├── fixed_window_rate_limiter.py
│   ├── sliding_window_log_rate_limiter.py
│   └── token_bucket_rate_limiter.py
│
├── factory/
│   ├── __init__.py
│   └── rate_limiter_factory.py
│
├── service/
│   ├── __init__.py
│   └── rate_limiter_service.py
│
├── tests/
│   ├── __init__.py
│   └── test_rate_limiter.py
│
├── main.py
└── README.md
```

---

## How to Run

No external dependency is required for the in-memory implementation.

```bash
python main.py
```

Run tests:

```bash
python -m unittest discover -s tests -v
```

---

## Possible Extensions

1. Redis-backed distributed store.
2. Sliding Window Counter.
3. Leaky Bucket.
4. IP-based rate limiting.
5. API-key-based rate limiting.
6. Endpoint-specific policies.
7. Multiple limits simultaneously, e.g.:
   - 10/sec
   - 100/min
   - 10,000/day
8. Dynamic policy configuration.
9. Rate-limit metrics.
10. Prometheus/OpenTelemetry integration.
11. Administrative policy management.
12. Fail-open/fail-closed configuration.
13. Distributed locking or atomic Redis scripts.
14. Local + distributed two-level rate limiting.

---

## Interview Summary

The core design can be summarized as:

```text
                         Client
                           |
                           v
                  RateLimiterService
                           |
                     RateLimitPolicy
                           |
                    RateLimiterFactory
                           |
             +-------------+-------------+
             |             |             |
             v             v             v
        Fixed Window  Sliding Window  Token Bucket
             |             |             |
             +-------------+-------------+
                           |
                    RateLimitStore
                      /          \
                     /            \
              In-Memory          Redis
```

The important LLD concepts demonstrated here are:

- Strategy Pattern
- Factory Pattern
- Dependency Inversion
- Store/Repository abstraction
- Thread safety
- Per-user locking
- Deterministic testing
- Distributed-system considerations
- Algorithm trade-offs
