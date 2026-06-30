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
