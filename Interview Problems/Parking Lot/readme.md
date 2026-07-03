# Parking Lot System Design

A low-level design (LLD) for a smart parking lot management system. This document walks through requirements gathering, entity modeling, interaction flows, and a layered class structure, then closes with general guidance for approaching object-oriented system design problems like this one.

## Table of contents

- [How to read this document](#how-to-read-this-document)
- [Functional requirements](#functional-requirements)
- [Non-functional requirements](#non-functional-requirements)
- [Edge cases](#edge-cases)
- [Step 1: Identify core entities](#step-1-identify-core-entities)
- [Step 2: Discuss interaction flow](#step-2-discuss-interaction-flow)
- [Step 3: Class structure and relationships](#step-3-class-structure-and-relationships)
- [Step 4: OOP principles and design patterns](#step-4-oop-principles-and-design-patterns)
- [Step 5: Core use cases and method call chains](#step-5-core-use-cases-and-method-call-chains)
- [Step 6: Handling edge cases](#step-6-handling-edge-cases)
- [Step 7: Class diagrams and UML relationships](#step-7-class-diagrams-and-uml-relationships)
- [Step 8: Key implementation decisions](#step-8-key-implementation-decisions)
- [Design principles applied](#design-principles-applied)
- [General guidance for designing systems like this](#general-guidance-for-designing-systems-like-this)
- [Possible extensions](#possible-extensions)

## How to read this document

This design follows a deliberate sequence, which mirrors how a system design interview or a real design doc should be structured:

1. **Requirements** — agree on what the system must do (functional) and how well it must do it (non-functional) before writing any code or drawing any boxes.
2. **Entities** — extract the nouns in the problem and turn them into a data model.
3. **Interaction flow** — walk through the verbs: what actually happens, in order, for each major use case.
4. **Class structure** — only once the flow is clear, assign responsibilities to controllers, services, and repositories.

Skipping straight to step 4 is the most common mistake in system design — it produces classes that look reasonable individually but don't compose into a coherent flow.

## Functional requirements

Functional requirements describe **what the system does** — the observable behaviors a user or admin can trigger.

**Entry flow**

- Vehicle arrives at the gate
- Generate ticket and assign slot based on vehicle type
- Mark slot as occupied
- Return `EntryResult` with success/failure status

**Exit flow**

- User presents ticket at exit
- Calculate fee based on pricing rules (minimum of flat and hourly pricing)
- Process payment through payment gateway
- Release slot and generate receipt
- Return `ExitResult` with success/failure status

**Admin configurations**

- Add/edit/delete floors and slots
- Define pricing rules based on vehicle type (both flat and hourly rates)
- Update flat and hourly pricing for vehicle types
- View current parking status

> **Operational note:** consider cases like server failure, where a human may need to override instead of the automated gates working by themselves. This single line in the requirements is the seed for the manual-override behavior captured later in the edge cases and in `AdminController`.

**Why this matters:** every functional requirement here maps directly to one controller method later (`enterVehicle`, `exitVehicle`, `addFloor`, etc.). If a requirement doesn't eventually show up as a method signature somewhere, either the requirement was dropped or it's been silently folded into another method — both are worth catching early.

## Non-functional requirements

Non-functional requirements (NFRs) describe **how well** the system must behave — qualities that cut across every feature rather than living in a single flow. They are often what separates a "this compiles" design from a production-ready one, and in interviews they're frequently where the real signal is — anyone can list CRUD endpoints, but defending a consistency or latency choice shows engineering judgment.

| Requirement   | Description                                                          | Where it shows up in the design                                                                                                               |
| ------------- | -------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------- |
| Scalability   | Must support multiple parking lots and thousands of slots            | Floor/slot model is hierarchical and indexable by vehicle type, not a flat list scanned linearly                                              |
| Consistency   | Strong consistency for slot allocation and release                   | `SlotRepository.findAvailableSlot()` needs row-level locking or a transactional allocate-and-mark-occupied step                               |
| Availability  | High availability for entry/exit even during payment gateway failure | Payment failure doesn't block exit entirely — see "payment failure during exit" edge case                                                     |
| Latency       | Low latency (<500ms) for ticket generation and exit processing       | Favors indexed lookups (`findAvailableSlot`, `findByTicketId`) over full scans; pricing calculation is pure in-memory math, not a remote call |
| Extensibility | Easily add new vehicle types, pricing strategies, or gateways        | Enum-based `VehicleType`, `PaymentGatewayAdapter` interface, isolated `PricingService`                                                        |
| Security      | Role-based access for admin actions                                  | `AdminController` methods are the only ones that mutate floors/slots/pricing — a natural boundary for an authorization check                  |

**Why this matters:** NFRs often pull in opposite directions. Strong consistency on slot allocation (lock the row, confirm, then release the lock) trades against low latency (locks add wait time). The design here resolves that tension by keeping the locked critical section as small as possible — just the allocate-and-mark-occupied step — while keeping fee calculation and ticket generation outside of it.

## Edge cases

Edge cases are the failure and boundary conditions a design must survive, not just the happy path. They're usually surfaced by asking "what happens if step X fails or arrives twice?" for every step in the interaction flow.

- **Payment failure during exit** — retry and hold the slot. The slot is _not_ released until payment succeeds, so a vehicle can't be double-booked into a slot it's still physically occupying.
- **Ticket lost** — allow admin override. This is the human-in-the-loop path referenced in the functional requirements; the admin needs a way to manually close out a ticket without the physical artifact.
- **Clock skew** — system time validation. Since `entryTime` and `exitTime` drive billing, any disagreement between the entry kiosk's clock and the exit kiosk's clock could under- or over-charge a driver — worth validating against a single trusted time source (e.g. the database server's clock) rather than trusting each kiosk locally.
- **Slot state mismatch** — periodic reconciliation. Distributed or multi-process systems can drift: a slot might be marked occupied with no corresponding active ticket, or vice versa. A background reconciliation job comparing `findActiveTickets()` against occupied slots catches and corrects this drift.

**Why this matters:** every edge case above eventually shows up as either a repository method (`findActiveTickets`, `findByTicketId`) or a piece of orchestration logic in a service. Edge cases that can't be traced to a concrete method are a sign the design isn't actually handling them yet — it's just listing them.

## Step 1: Identify core entities

The first concrete step in any object-oriented design is finding the **nouns** in the requirements and deciding which ones deserve to be first-class entities versus which are just fields on something else.

### Domain models

**Vehicle**

- `id`: UUID [PK]
- `licensePlate`: String
- `vehicleType`: Enum (BIKE, CAR, TRUCK, EV)

**ParkingSlot**

- `id`: UUID [PK]
- `slotType`: Enum (BIKE, CAR, TRUCK, EV)
- `isOccupied`: boolean
- `floorNumber`: int

**Floor**

- `id`: UUID [PK]
- `floorNumber`: int
- `slots`: List\<ParkingSlot\>

**Ticket**

- `id`: UUID [PK]
- `vehicleId`: UUID [FK → Vehicle.id]
- `slotId`: UUID [FK → ParkingSlot.id]
- `entryTime`: Timestamp
- `isActive`: boolean

**Receipt**

- `id`: UUID [PK]
- `ticketId`: UUID [FK → Ticket.id]
- `exitTime`: Timestamp
- `totalFee`: Double
- `paymentStatus`: Enum (PENDING, SUCCESS, FAILED)

**PricingRule**

- `id`: UUID [PK]
- `vehicleType`: Enum
- `ratePerHour`: Double
- `flatRate`: Double
- `ruleType`: Enum (FLAT, HOURLY)

**Payment**

- `id`: UUID [PK]
- `ticketId`: UUID [FK → Ticket.id]
- `amount`: Double
- `gateway`: Enum (RAZORPAY, STRIPE)
- `status`: Enum (PENDING, SUCCESS, FAILED)

### DTOs

DTOs (data transfer objects) are deliberately kept separate from domain entities. They describe the **shape of a response**, not a persisted record — `EntryResult` is never written to a table.

**EntryResult**

- `success`: boolean
- `ticket`: Ticket (if successful)
- `message`: String

**ExitResult**

- `success`: boolean
- `receipt`: Receipt (if successful)
- `message`: String

**Why a separate Ticket and Receipt rather than one combined record?** A `Ticket` represents an open, in-progress parking session — it exists from entry until exit. A `Receipt` represents a closed, billed session. Splitting them means an active ticket is never accidentally treated as billable, and a receipt is immutable once created (it has no `isActive` flag to flip). This is a small modeling decision, but it's the kind of decision that prevents an entire category of bugs (a half-paid session being double-charged) just by the data model refusing to represent the bad state.

**Why does `Payment` exist separately from `Receipt`?** A receipt is produced once exit succeeds; a payment attempt can happen multiple times if it fails and retries. Keeping `Payment` as its own append-only log (one row per attempt) means the system has an audit trail of every gateway call, while `Receipt` stays a single clean summary record.

## Step 2: Discuss interaction flow

Before assigning methods to classes, it's worth narrating each flow in plain language — almost like a sequence diagram in prose. This step catches missing entities or methods before they become a class-design problem.

**Entry flow**
Driver enters → gets a slot → gets a ticket

**Exit flow**
Driver exits → shows the ticket → price computed (minimum of flat and hourly pricing) → pays the amount (with retries if it fails) → gets the receipt → slot released → ticket deactivated to avoid multiple entry

**Admin flow**
Admin requests to add floor, add slots, or update pricing

**Why "minimum of flat and hourly pricing"?** This single design choice in the exit flow is worth dwelling on, because it's the kind of detail that's easy to gloss over and hard to retrofit. It implies `PricingService` must compute _both_ numbers and compare them — not pick one strategy up front — which in turn means `PricingRule` needs both `flatRate` and `ratePerHour` available simultaneously, which is exactly how the entity was modeled in Step 1. Tracing this detail from requirement → flow → entity → service is a good way to sanity-check the whole design end to end.

**Why deactivate the ticket on exit, not just release the slot?** Releasing the slot makes it available again; deactivating the ticket makes the ticket unusable for a second exit. Without this, a driver (or a bug, or a replayed request) could present the same ticket twice and trigger two payments and two slot releases for one parking session.

## Step 3: Class structure and relationships

Only after the flow is settled does it make sense to assign responsibilities to classes. This is also where layering decisions get made.

### Architecture layers

```
Client/UI → Controller Layer (HTTP/API) → Service Layer → Repository Layer → Domain Layer
```

- **Controller layer** — handles HTTP requests, validates input shape, and translates between wire format and service calls. It should contain effectively no business logic.
- **Service layer** — contains business logic: slot allocation rules, fee calculation, payment retry orchestration. This is where the "interesting" code lives.
- **Repository layer** — abstracts database access behind method calls like `findById` or `findAvailableSlot`. Nothing above this layer should know whether the data lives in Postgres, DynamoDB, or an in-memory map.
- **Domain layer** — pure entities and enums, with no dependency on any other layer.

**Why layer it this way?** Each layer can only call the layer directly below it, never skip a layer or call sideways. This constraint is what makes the system testable (you can swap a repository for an in-memory fake without touching service logic) and what makes the extensibility NFR achievable (a new payment gateway only requires a new adapter, not changes to the service or controller).

### Controllers

**EntryController**

```java
EntryResult enterVehicle(String licensePlate, VehicleType vehicleType)
```

**ExitController**

```java
ExitResult exitVehicle(UUID ticketId)
```

**AdminController**

```java
void addFloor(int floorNumber)
void addSlot(int floorNumber, VehicleType slotType)
void updatePricing(VehicleType vehicleType, double ratePerHour, double flatRate)
void updateFlatPricing(VehicleType vehicleType, double flatRate)
void updateHourlyPricing(VehicleType vehicleType, double ratePerHour)
```

Note that `updatePricing` updates both rates at once, while `updateFlatPricing` and `updateHourlyPricing` allow a partial update. This isn't redundant — it lets an admin UI offer "edit just the hourly rate" without forcing the caller to first fetch and resend the flat rate too.

### Services

**TicketService**

```java
Ticket generateTicket(Vehicle vehicle, ParkingSlot slot)
Ticket getTicket(UUID ticketId)
```

Creates and stores a ticket on entry. `getTicket` is the lookup used at the start of the exit flow.

**SlotService**

```java
ParkingSlot allocateSlot(VehicleType vehicleType)
void releaseSlot(UUID slotId)
```

The center of the consistency NFR. `allocateSlot` must find a free, matching-type slot and mark it occupied as a single atomic operation — otherwise two vehicles arriving at the same instant could both be handed the same slot. `releaseSlot` flips `isOccupied` back to false on exit, but only after payment succeeds (see edge cases).

**PricingService**

```java
double calculateFee(Ticket ticket) // Returns minimum of flat and hourly pricing
```

Looks up the relevant `PricingRule` and returns whichever of flat or hourly pricing is cheaper for that session.

**PaymentService**

```java
boolean processPayment(UUID ticketId, double amount)
```

Delegates to whichever `PaymentGatewayAdapter` is configured. Returns a boolean so the orchestrating flow (exit) can decide whether to retry, rather than the payment service itself deciding how many times to retry — that's a workflow decision, not a payment-gateway decision.

**ReceiptService**

```java
Receipt generateReceipt(Ticket ticket, double fee, boolean paymentSuccess)
```

Generates and persists the receipt after payment resolves, recording the outcome either way for audit purposes.

**AdminService**

```java
void addFloor(int floorNumber)
void addSlot(int floorNumber, VehicleType slotType)
void updatePricing(VehicleType vehicleType, double ratePerHour, double flatRate)
void updateFlatPricing(VehicleType vehicleType, double flatRate)
void updateHourlyPricing(VehicleType vehicleType, double ratePerHour)
```

### Repositories

**TicketRepository**

```java
void save(Ticket ticket)
Ticket findById(UUID ticketId)
List<Ticket> findActiveTickets()
void deactivateTicket(UUID ticketId)
```

`findActiveTickets()` exists specifically to support the slot-state-mismatch reconciliation job — without it, there'd be no way to compare "tickets the system thinks are open" against "slots the system thinks are occupied."

**SlotRepository**

```java
void save(ParkingSlot slot)
ParkingSlot findById(UUID slotId)
ParkingSlot findAvailableSlot(VehicleType vehicleType)
```

`findAvailableSlot` is the single most concurrency-sensitive method in the entire system. In practice this is implemented with `SELECT ... FOR UPDATE` (pessimistic locking) or an optimistic compare-and-swap on `isOccupied`, never a plain read-then-write — a plain read-then-write is exactly how race conditions slip into parking lot systems under load.

**FloorRepository**

```java
void save(Floor floor)
Floor findByFloorNumber(int floorNumber)
```

**PricingRuleRepository**

```java
void save(PricingRule rule)
PricingRule findByVehicleType(VehicleType vehicleType)
```

**PaymentRepository**

```java
void save(Payment payment)
Payment findByTicketId(UUID ticketId)
```

`findByTicketId` lets the system check for a prior in-flight or failed attempt before charging again, which prevents double-charging a driver when a gateway call times out without a clear success/failure response.

### Payment gateway interface (Adapter pattern)

```java
interface PaymentGatewayAdapter {
    boolean pay(UUID ticketId, double amount)
}

class RazorpayAdapter implements PaymentGatewayAdapter { ... }
class StripeAdapter  implements PaymentGatewayAdapter { ... }
```

`PaymentService` depends only on the `PaymentGatewayAdapter` interface, never on `RazorpayAdapter` or `StripeAdapter` directly. Adding PayPal later means writing one new class and wiring it up — zero changes to `PaymentService`, `ExitController`, or anything else. This is the extensibility NFR made concrete.

### End-to-end call flow

```
EntryController  → SlotService (allocate) + TicketService (generate)
ExitController   → TicketService (get) + PricingService (fee) + PaymentService (pay)
                  + ReceiptService (receipt) + SlotService (release)
AdminController  → AdminService

PaymentService → PaymentGatewayAdapter → RazorpayAdapter / StripeAdapter → External gateway
```

Notice that `ExitController` touches five different services in sequence. This is normal — the controller (or a thin orchestrating service just below it) is the natural place for cross-cutting sequencing logic like "compute fee, then pay, then on success generate receipt and release the slot."

## Step 4: OOP principles and design patterns

### Design patterns used

**1. Adapter Pattern** — integrating with different payment gateways (Razorpay, Stripe)

`PaymentGatewayAdapter` is a single interface with one method: `pay(UUID ticketId, double amount)`. `RazorpayAdapter` and `StripeAdapter` both implement it, each translating that common call into whatever their specific SDK or API requires.

```
PaymentService → PaymentGatewayAdapter (interface)
                       ↑                ↑
                RazorpayAdapter    StripeAdapter
```

`PaymentService` never imports Razorpay or Stripe directly. Adding PayPal tomorrow is one new class — nothing else changes. This is exactly what makes the Extensibility NFR achievable in practice.

**2. Repository Pattern** — data access abstraction

Every entity has a dedicated repository (`TicketRepository`, `SlotRepository`, etc.) that hides how and where data is stored. Services call `ticketRepository.findById(id)` — they have no idea if the backing store is Postgres, DynamoDB, Redis, or a `HashMap`. This is what makes the system testable: swap any repository for an in-memory fake and all service logic tests without touching a real database.

**3. Service Layer Pattern** — business logic separation

Each service owns one business domain: `PricingService` calculates fees, `SlotService` manages slot state, `PaymentService` handles payment orchestration. Controllers are thin wires between the HTTP layer and the services — they do no computation. This separation means a bug in fee calculation is always in `PricingService`, never somewhere else.

### OOP principles applied

**1. Interface Segregation** — separate responsibilities by interface (`PaymentGatewayAdapter`)

The interface is kept minimal — just `pay()` and `getGatewayName()`. Implementations only need to fulfill those two contracts, nothing more. This keeps adapter classes small and focused.

**2. Dependency Inversion** — services depend on interfaces, not concrete implementations

`PaymentService` holds a reference of type `PaymentGatewayAdapter`, not `RazorpayAdapter`. `SlotService` takes a `SlotRepository` in its constructor, not a specific DB client. High-level modules (services) never depend on low-level modules (adapters, DB drivers) directly — both depend on the abstraction in the middle. This is what makes `setDefaultGateway()` and in-memory repository swapping work cleanly.

**3. Single Responsibility** — each class has one clear purpose

`PricingService` calculates fees. `ReceiptService` generates receipts. `TicketService` manages tickets. A change to receipt formatting never risks breaking fee calculation because the code for those two concerns lives in completely separate classes.

**4. Open/Closed** — easy to extend with new vehicle types, pricing strategies, payment gateways

The system is open for extension (add `VAN` to the `VehicleType` enum, add a `PaypalAdapter`) but closed for modification (adding those doesn't require changing `PricingService`, `PaymentService`, or any existing adapter). The enums and interface boundaries are the extension points, designed in from the start.

**5. Encapsulation** — domain objects encapsulate their data and behavior

`ParkingSlot` doesn't expose a `setIsOccupied(boolean)` setter — it exposes `occupy()` and `release()`, both of which enforce their own preconditions (can't occupy an already-occupied slot, can't release a free one). `Ticket` exposes `deactivate()` rather than `setIsActive(false)`. `Payment` exposes `markAsSuccess()` and `markAsFailed()`. The domain objects own their state transitions — no external class can put them into an invalid state by setting a raw field.

### How they work together

These aren't independent choices — they reinforce each other:

- Encapsulation in the domain layer makes the Repository safe (repos persist objects whose state is always valid)
- Repository Pattern makes DIP possible (services depend on the repo interface, not a DB)
- DIP makes the Adapter Pattern work (PaymentService depends on the adapter interface, not Razorpay)
- SRP keeps each class small enough to understand and test in isolation
- Open/Closed is the result of all the above — when each class has one job and depends on interfaces, adding new behavior rarely requires modifying existing classes

---

## Step 5: Core use cases and method call chains

This step ties together everything from the design into concrete, ordered method-call chains — essentially the actual call stack that fires for each use case. Writing these out explicitly is what catches ordering bugs before they're coded.

**Entry use case**

```
enterVehicle()
  → SlotService.allocateSlot()
  → TicketService.generateTicket()
  → TicketRepository.save()
  → return EntryResult
```

`allocateSlot()` must succeed before `generateTicket()` is even called — you can't issue a ticket for a slot that doesn't exist yet.

**Exit use case**

```
exitVehicle()
  → TicketService.getTicket()
  → PricingService.calculateFee()
  → PaymentService.processPayment()
      → PaymentGatewayAdapter.pay()
  → ReceiptService.generateReceipt()
  → SlotService.releaseSlot()          ← only after payment succeeds
  → TicketService.deactivateTicket()   ← prevents ticket reuse
  → return ExitResult
```

The order is deliberate. `releaseSlot()` comes _after_ `generateReceipt()` and only after confirmed payment. If payment fails, the chain stops before `releaseSlot()` — the slot stays occupied, the vehicle stays in the lot, and the driver can retry. `deactivateTicket()` is last, making the ticket unusable for a second exit even if the same ID is presented again.

**Admin use cases**

```
addFloor()      → AdminService → FloorRepository.save()
addSlot()       → AdminService → SlotRepository.save()
updatePricing() → AdminService → PricingRuleRepository.save()
```

These are simpler, single-hop chains — admin controller methods go straight through `AdminService` to the relevant repository's `save()`. There's no multi-step orchestration because admin actions are independent writes, not multi-stage workflows.

---

## Step 6: Handling edge cases

### Edge case solutions and implementation strategies

**1. Exit without ticket → admin override via `AdminController`**

A driver loses their ticket, or the kiosk scanner fails. Without an override path, the vehicle is stuck — the gate won't open and the slot stays marked occupied forever. A dedicated admin endpoint can manually close out a ticket by license plate lookup, release the slot, and generate a receipt with a manually-set fee. This is why `AdminController` exists as a separate controller rather than folding admin operations into `EntryController` or `ExitController`.

_Implementation:_ special admin endpoints for manual operations through `AdminController` (e.g. `forceExitByPlate(licensePlate)`).

**2. Payment failed → `PaymentGatewayAdapter` returns boolean, handle failure in `PaymentService`**

Rather than throwing an exception on payment failure, the adapter returns a simple `boolean`. `PaymentService` interprets that boolean and decides what to do — retry with the same gateway, switch to Stripe, or give up after `maxRetries`. Crucially, the slot is _not_ released on payment failure. The vehicle stays physically in the lot, so keeping the slot occupied is the correct state.

_Implementation:_ `processPaymentWithRetry(ticketId, fee, maxRetries)` — retries up to N times, switching from Razorpay to Stripe on the second attempt.

**3. Vehicle type mismatch → verify at entry and exit through `SlotService`**

A TRUCK trying to park in a BIKE slot is prevented at allocation time — `findAvailableSlot(vehicleType)` filters on `slotType == vehicleType`, so a mismatched slot is never returned. At exit, the slot type can be cross-checked against the vehicle type on the ticket as a sanity guard before releasing.

_Implementation:_ `SlotRepository.findAvailableSlot(VehicleType)` enforces type matching at the DB query level, not in application logic — mismatches are impossible to allocate, not just caught after the fact.

**4. Time mismatch → use system clock consistently across all services**

`PricingService.calculateHourlyFee()` and `ReceiptService.generateReceiptText()` both need timestamps. If each calls `LocalDateTime.now()` independently, clock drift between kiosks means the entry and exit times could come from different clocks, producing a wrong duration and a wrong fee.

_Implementation:_ a centralized time service — a single source of truth for the current timestamp that all services use. In production, typically the database server's clock, or a shared `Clock` object injected into every service that needs it. The `calculateFee(Ticket, VehicleType, LocalDateTime exitTime)` overload already accepts an externally-supplied timestamp for this reason.

**5. Slot inconsistency → run periodic reconciliation service**

Under real-world conditions — process crashes, network timeouts, failed transactions — the state of `isOccupied` can drift out of sync with reality. A slot might show occupied with no active ticket, or an active ticket might reference a slot marked free.

_Implementation:_ a background `ReconciliationService` that runs on a schedule (e.g. every 5 minutes), calls `TicketRepository.findActiveTickets()`, cross-checks each ticket's `slotId` against `SlotRepository.findAllOccupied()`, and flags or auto-corrects any mismatches.

### Summary table

| Edge case             | Root cause                              | Strategy                                   | Where in code                                     |
| --------------------- | --------------------------------------- | ------------------------------------------ | ------------------------------------------------- |
| Exit without ticket   | Physical ticket lost or scanner failure | Admin manual override endpoint             | `AdminController.forceExitByPlate()`              |
| Payment failed        | Gateway down or timeout                 | Retry with fallback gateway, hold slot     | `PaymentService.processPaymentWithRetry()`        |
| Vehicle type mismatch | Wrong slot type allocated               | Type-safe slot allocation query            | `SlotRepository.findAvailableSlot(vehicleType)`   |
| Clock skew            | Kiosk clocks out of sync                | Centralized time service / inject exitTime | `PricingService.calculateFee(..., LocalDateTime)` |
| Slot inconsistency    | Crash or partial transaction            | Periodic reconciliation background job     | `ReconciliationService` (scheduled task)          |

---

## Step 7: Class diagrams and UML relationships

When drawing a class diagram, three types of relationships communicate the intent and ownership between classes. Using the right one isn't just notation — it tells the reader about lifecycle, replaceability, and dependency.

### 1. Association — "I work with you"

A class _uses_ another class by holding a reference to it, but neither owns the other. Both exist independently. This is the most general relationship.

**In this codebase:**

```
ExitController ——> PaymentService
```

`ExitController` calls `PaymentService` to process a payment. It holds a reference, but `PaymentService` exists independently — it could be used by other controllers too. If `ExitController` is destroyed, `PaymentService` lives on.

**UML notation:** plain arrow `——>`

---

### 2. Aggregation — "I have you, but you are not mine"

A class _contains_ another class, but the contained object has an independent lifecycle. It can exist without the container. This is a "has-a" relationship where ownership is shared or weak.

**In this codebase:**

```
Floor <>——> ParkingSlot
```

A `Floor` has a `List<ParkingSlot>`. But a `ParkingSlot` is created separately by `AdminService` and added to the floor afterward. If a floor were removed, the slots could conceptually be reassigned. The slot doesn't belong _exclusively_ to the floor.

**UML notation:** open diamond `<>——>`

---

### 3. Composition — "You are mine and only mine"

The strongest relationship. The contained object's lifecycle is _entirely controlled_ by the container — created by it, and destroyed with it. It cannot exist independently.

**In this codebase:**

```
Ticket ◆——> entryTime (LocalDateTime)
Receipt ◆——> exitTime (LocalDateTime)
```

`entryTime` is stamped inside the `Ticket` constructor and has no meaning outside of a ticket. It can't be shared, transferred, or exist on its own. Same for `exitTime` on `Receipt`.

**UML notation:** filled diamond `◆——>`

---

### Comparison table

| Relationship | Ownership          | Lifecycle of contained object           | Example in codebase                    |
| ------------ | ------------------ | --------------------------------------- | -------------------------------------- |
| Association  | None               | Fully independent                       | `ExitController` uses `PaymentService` |
| Aggregation  | Weak / shared      | Independent — can outlive the container | `Floor` has `List<ParkingSlot>`        |
| Composition  | Strong / exclusive | Dependent — dies with the parent        | `Ticket` owns its `entryTime`          |

### Why this matters

Getting these relationships right is what makes a class diagram useful for communication, not just documentation. Saying `Floor <>——> ParkingSlot` tells a reader "slots can be moved between floors or exist without one." Saying `Ticket ◆——> entryTime` tells a reader "don't try to share or transfer this — it belongs to exactly one ticket and always will." A diagram where every relationship is drawn as a plain arrow conveys no ownership information at all.

---

## Step 8: Key implementation decisions

These are the non-obvious choices that came out of the code walkthrough — the kind of details that separate a design that compiles from one that's production-aware.

### Fee calculation — `calculateHourlyFee`

The hourly fee calculation has three rules baked in, each deliberate:

**Round up partial hours with `Math.ceil(minutes / 60.0)`** — parking lots bill partial hours as full hours. 61 minutes = 2 hours billed. The `.0` on `60.0` is critical: without it, Java performs integer division before `ceil` gets a chance, so 61 / 60 = 1 (not 1.016...) and `ceil(1)` = 1 — silently under-charging by a full hour.

**Minimum charge of 1 hour via `Math.max(hours, 1.0)`** — a driver who parks for 3 minutes still pays for a full hour. This is a business rule that lives in the fee calculation, not in the pricing rule entity, because it applies universally regardless of vehicle type or rate.

**Pass `exitTime` as a parameter, don't call `LocalDateTime.now()` inside the method** — calling `now()` internally makes the method untestable (you can't control time in a test) and vulnerable to the clock-skew edge case. The cleaner signature `calculateFee(Ticket, VehicleType, LocalDateTime exitTime)` lets the caller supply the timestamp from a trusted source.

### Payment — `processPayment` and `processPaymentWithRetry`

**Save the payment record before calling the gateway** — the `Payment` row is persisted with PENDING status _before_ `defaultGateway.pay()` is called. If the process crashes mid-call, there's still an audit record. This prevents silent failures from disappearing entirely.

**Each retry attempt creates its own `Payment` row** — `processPaymentWithRetry` calls `processPayment` on each attempt, and `processPayment` saves a new row each time. This gives a complete per-attempt audit trail rather than a single overwritten record, which is essential for dispute resolution.

**Gateway switches on retry: Razorpay → Stripe after the first failure** — if Razorpay is down, retrying against Razorpay again is pointless. Switching to Stripe on the second attempt is a practical resilience pattern. One caveat: the current implementation mutates `defaultGateway` on the instance, meaning after a retry-triggered switch, `PaymentService` permanently uses Stripe. Whether that's intentional (sticky switch) or a side effect is worth making explicit in production.

**`setDefaultGateway()` as a public setter** — this is a testing seam (inject a mock adapter without changing the constructor) and a runtime hook for admin-controlled gateway switching.

### Receipt — two-method design

`generateReceipt()` and `markReceiptAsPaid()` are kept as two separate calls rather than one combined method. This is intentional: a receipt can be created in PENDING state before payment is confirmed, giving the system an audit record even if the subsequent `markAsPaid` call never arrives (due to a crash between the two calls). The receipt exists; its status reflects the true payment outcome.

### Ticket deactivation — why it's the last step in exit

`deactivateTicket()` is called after slot release, not before. The ordering matters: if deactivation were called first and then the slot release failed, the ticket would be permanently unusable but the slot would still be marked occupied — a stuck state. Releasing the slot first, then deactivating the ticket, means the worst case on failure is a still-active ticket against a now-free slot, which is recoverable (the driver can retry exit).

### `ParkingLotSimulation` — design of the demo runner

The simulation class (`ParkingLotSimulation.java`) demonstrates a pattern worth noting for any demo or integration test entrypoint:

**Phases are clearly labelled and sequenced** — INITIALIZATION → ENTRY → EXIT → ADMIN → FINAL STATUS. This mirrors the real operational lifecycle of the system and makes the output readable as a log.

**Exit simulation queries the repository, not memory** — active tickets are fetched via `ticketRepository.findActiveTickets()` rather than tracking returned ticket IDs from the entry calls. This demonstrates that the system is genuinely state-driven: you don't need in-memory handles to tickets; the repository is the source of truth.

**Helper methods per phase** — `simulateVehicleEntry`, `simulateVehicleExit`, `simulateAdminOperations` keep `main()` readable as a high-level script with details pushed into named helpers. This is a good habit for any integration test or simulation entrypoint.

**`initializeParkingLot()` on `AdminController`** — instead of repeating setup inline in `main()`, a dedicated initialization method seeds floors, slots, and pricing rules in one call. This makes the simulation self-contained and the setup reusable if tests need the same starting state.

---

## Design principles applied

- **Single Responsibility Principle (SRP)** — each controller and service owns exactly one concern (entry, exit, admin, ticketing, slot allocation, pricing, payment, receipts). A change to pricing logic never requires touching `SlotService`.
- **Dependency Inversion Principle (DIP)** — each layer depends on an abstraction in the layer below (an interface or a repository method signature), never on a concrete class. This is what makes mocking and testing each layer in isolation possible.
- **Adapter pattern** — `PaymentGatewayAdapter` decouples payment orchestration from any specific provider's SDK or API shape.
- **Strategy pattern (candidate)** — `PricingService.calculateFee()` is currently a simple min(flat, hourly) calculation, but as more pricing models appear (e.g. surge pricing, subscription passes, first-hour-free promotions), this is a natural place to introduce a `PricingStrategy` interface with one implementation per rule type, selected via `PricingRule.ruleType`.
- **Open/Closed Principle** — new vehicle types, gateways, or pricing rules can be added by adding new enum values and new adapter/strategy implementations, without modifying existing service code.

## General guidance for designing systems like this

The parking lot problem is a well-worn example precisely because it forces every core LLD skill into a small, bounded space. The process used above generalizes to most object-oriented system design problems — booking systems, vending machines, elevator systems, ride-sharing dispatch, and so on. A few transferable habits:

**1. Always separate functional from non-functional requirements explicitly, in writing, before designing anything.** It's tempting to jump straight from a one-line prompt ("design a parking lot") into drawing classes. Forcing yourself to write down NFRs first (consistency, latency, availability, scalability, extensibility, security) gives you a checklist to validate every later decision against. If you can't say which NFR a design choice serves, it's worth asking whether the choice is actually load-bearing or just a habit.

**2. Find entities before behavior.** List every noun in the problem statement, then sort them into "this is a real entity with identity and a lifecycle" (Vehicle, Ticket, Slot) versus "this is just a value/attribute" (license plate, fee amount). A common beginner mistake is over-modeling — making `Fee` or `Status` their own classes with no real behavior or identity, when they're better as a field or enum.

**3. Always ask "what happens if this fails or happens twice?" for every step.** This is the cheapest way to generate a useful edge-case list. Payment can fail. A network call can be retried by an upstream client. Two requests can race. A clock can be wrong. Walking the happy path and asking this question at each arrow is more reliable than trying to brainstorm edge cases from scratch.

**4. Narrate the flow in prose before writing method signatures.** "Driver enters, gets a slot, gets a ticket" is a sentence, not a UML diagram — but writing that sentence is what reveals that slot allocation has to happen _before_ ticket generation, which is exactly the dependency `EntryController` needs to encode. Skipping this step and going straight to class diagrams tends to produce technically-plausible classes that don't actually compose into the right sequence.

**5. Let identified concurrency points drive your locking strategy, not the other way around.** Don't reach for "use a distributed lock" or "use optimistic concurrency" as a default. Find the one or two operations that are genuinely contested under load (here, `findAvailableSlot`) and reason about locking specifically there. Most of the system (pricing math, receipt generation) needs no locking at all.

**6. Keep DTOs and domain entities separate, even when they look similar.** `EntryResult` and `Ticket` overlap in content but serve different purposes — one is a wire-format response, the other is a persisted record. Collapsing them tends to leak persistence concerns into the API layer (e.g. exposing internal IDs or DB-only fields to clients) or, worse, accidentally persisting a partial/transient result.

**7. Design interfaces around what varies, not around what exists today.** `PaymentGatewayAdapter` exists because "which payment gateway" is something the business is likely to change or extend. Don't introduce an interface for things that aren't expected to vary (e.g. there's no `VehicleRepositoryAdapter` interface here, because swapping how vehicles are stored isn't a stated requirement) — premature abstraction adds indirection without adding flexibility where it's actually needed.

**8. Validate the design by tracing a requirement all the way through.** Pick one functional requirement (e.g. "calculate fee based on minimum of flat and hourly pricing") and trace it: which controller method receives the request, which service computes it, which repository supplies the data, which entity holds the fields. If the trace has a gap, that's a missing method or a missing field, and it's far cheaper to find that gap on paper than after writing the implementation.

**9. Treat the admin/configuration surface as a first-class flow, not an afterthought.** It's easy to spend all the design energy on the "interesting" entry/exit flows and bolt on admin operations at the end. Here, admin operations (pricing updates, floor/slot management) directly shape what `PricingService` and `SlotService` need to support, so designing them in parallel — not after — avoids rework.

**10. Revisit NFRs against the finished design as a final check.** Once the classes exist, go back through the NFR table and ask, for each row, "what specifically in this design satisfies this?" If the honest answer is "nothing yet," that's a gap to close (e.g. role-based access for `AdminController` was named as a requirement but isn't yet enforced anywhere in the method signatures above — it would need an authorization check, likely at the controller layer or via a middleware/decorator).

## Possible extensions

These aren't part of the current design, but are natural follow-up questions an interviewer or a real product roadmap might raise, and are worth thinking through using the same process above:

- **Multiple parking lots** — the current `Floor`/`ParkingSlot` model is scoped to a single lot. Supporting multiple lots means introducing a `ParkingLot` entity above `Floor`, and most repository methods would need a `lotId` parameter.
- **Reservations** — allowing a driver to reserve a slot in advance changes `allocateSlot` from "find any free matching slot" to "find a free slot not already reserved for this time window," which has real implications for the consistency model.
- **Dynamic/surge pricing** — would replace the simple min(flat, hourly) calculation with a `PricingStrategy` interface, as noted under the Strategy pattern above.
- **Notifications** — sending a receipt by SMS/email after exit would be a good candidate for an event-driven extension (publish a `VehicleExited` event, let a separate notification service subscribe to it) rather than adding notification logic directly into `ExitController` or `ReceiptService`.
