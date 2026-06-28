# Database Design and Integration in Low-Level Design (LLD)

## Use-Case: Razorpay Payment System

This repository contains comprehensive documentation and architectural patterns for mapping system requirements to database schemas and production-ready object models within a payment gateway system.

---

## Table of Contents

1. [What is an ER Model?](https://www.google.com/search?q=%231-what-is-an-er-model)
2. [How to Design Tables from Requirements](https://www.google.com/search?q=%232-how-to-design-tables-from-requirements)
3. [Mapping ER Model to Class Model](https://www.google.com/search?q=%233-mapping-er-model-to-class-model)
4. [What is a DAO (Data Access Object)?](https://www.google.com/search?q=%234-what-is-a-dao-data-access-object)
5. [What is a Repository?](https://www.google.com/search?q=%235-what-is-a-repository)
6. [Real-World Enhancements and Practices](https://www.google.com/search?q=%236-real-world-enhancements-and-practices)

---

## 1. What is an ER Model?

An **Entity-Relationship (ER) Model** represents real-world **entities**, their specific **attributes** (properties or characteristics), and the **relationships** governing them. It acts as the structural foundation for designing your physical database schema and mapping requirements to low-level software components.

### Why it Matters in LLD:

- **Translates Product Requirements:** Converts raw text specifications from a Product Requirements Document (PRD) into structured data schemas.
- **Structural Bridge:** Bridges the gap between real-world business entities, relational database tables, and Object-Oriented programming models.

### Problem Statement: Payment Gateway System

To demonstrate the architectural flow, we analyze a core payment system scenario:

1. A **User** initiates a payment to a **Merchant**.
2. The payment sequence might **Succeed** or **Fail**.
3. A successful authorization results in a **Transaction**.
4. A completed **Transaction** may have one or many **Refunds** associated with it.
5. Payments can be processed via multiple discrete **Payment Methods** (e.g., `CARD`, `UPI`, `NETBANKING`).

### Entity-Attribute Schema Matrix

| Entity             | Attributes (Columns)                                                           | Key Classification                | Description & Field Boundaries                         |
| ------------------ | ------------------------------------------------------------------------------ | --------------------------------- | ------------------------------------------------------ |
| **User**           | `user_id`, `name`, `email`                                                     | `user_id` (**PK**)                | Represents the consumer initiating the payment action. |
| **Merchant**       | `merchant_id`, `business_name`                                                 | `merchant_id` (**PK**)            | Represents the business account receiving funds.       |
| **PaymentRequest** | `payment_request_id`, `user_id`, `merchant_id`, `amount`, `currency`, `status` | `payment_request_id` (**PK**)<br> |

<br>`user_id` (**FK**)<br>

<br>`merchant_id` (**FK**) | Logs the initial intent to pay. Captures multi-state lifecycles (`PENDING`, `SUCCESS`, `FAILED`). |
| **Transaction** | `transaction_id`, `payment_request_id`, `payment_method_id`, `status`, `time` | `transaction_id` (**PK**)<br>

<br>`payment_request_id` (**FK**)<br>

<br>`payment_method_id` (**FK**) | Tracks the ledger state of actual fund settlement movements across a gateway provider. |
| **Refund** | `refund_id`, `transaction_id`, `amount`, `reason` | `refund_id` (**PK**)<br>

<br>`transaction_id` (**FK**) | Documents post-transaction reverse settlements. Supports multiple partial reversals. |
| **PaymentMethod** | `payment_method_id`, `type`, `details` | `payment_method_id` (**PK**) | Decouples specific channel profiles (e.g., encrypted card tokens, UPI VAs) from payment records. |

---

## 2. How to Design Tables from Requirements

Transforming plain-text business specifications into a rigorous relational schema follows a standardized linguistic framework mapping English grammar components directly to database structural mechanics:

### The Linguistic Translation Framework

1. **Nouns $\rightarrow$ Entities / Tables:** Any unique operational object or actor specified in requirements (e.g., _User_, _Merchant_, _Payment Method_) becomes a distinct table.
2. **Properties $\rightarrow$ Columns / Attributes:** Qualifying traits belonging to a noun (e.g., _business_name_, _upi_id_, _email_) become individual data-typed table columns.
3. **Verbs / Actions $\rightarrow$ Relationships:** Direct interactions connecting distinct entities (e.g., "User _initiates_ payment", "Transaction _belongs to_ request") determine table joins.
4. **Adjectives / Quantifiers $\rightarrow$ Cardinality:** Quantified constraints (e.g., "_One_ user", "_Many_ transactions") dictate structural cardinality parameters.

### Relational Mapping & Cardinality Matrix

- **One User $\rightarrow$ Many Payment Requests ($1:N$):** A customer account can create multiple payment requests over time, but an explicit payment request is strictly owned by one user.
- _Database Impact:_ `user_id` resides as a Foreign Key ($FK$) in the `PaymentRequest` table.

- **One Merchant $\rightarrow$ Many Payment Requests ($1:N$):** A merchant receives transactions from thousands of requests, but a specific request routes to a single merchant.
- _Database Impact:_ `merchant_id` resides as a Foreign Key ($FK$) in the `PaymentRequest` table.

- **One Payment Request $\rightarrow$ Many Transactions ($1:N$):** Crucial LLD design choice. If a gateway times out or network drops, a user retries the process. The high-level `PaymentRequest` remains the same, but multiple sequential `Transaction` records (e.g., `Attempt 1: Failed`, `Attempt 2: Success`) trace back to it.
- _Database Impact:_ `payment_request_id` resides as a Foreign Key ($FK$) in the `Transaction` table.

- **One Transaction $\rightarrow$ Many Refunds ($1:N$):** To support item-level returns or fractional chargebacks, a single successful transaction supports multiple sequential partial refunds.
- _Database Impact:_ `transaction_id` resides as a Foreign Key ($FK$) in the `Refund` table.

- **One Payment Method $\rightarrow$ Many Transactions ($1:N$):** A saved instrument (e.g., a specific card token account) can be re-used across an infinite history of unique payments.
- _Database Impact:_ `payment_method_id` resides as a Foreign Key ($FK$) in the `Transaction` table.

---

## 3. Mapping ER Model to Class Model

To interact with underlying databases using pure Object-Oriented paradigms without fracturing software clean layout rules, developers map schemas directly into stateful entity object models.

### Core Conversion Concept:

- **Every table** maps to a separate **Class**.
- **Every column** maps to an **Instance Field** matching the database primitive data type.
- **Foreign Keys ($FK$)** map directly to **Object References** or **Collections** instead of raw ID values.

### OOP Relationship Translation:

- **Many-to-One ($N:1$):** Represented by adding a direct **Instance Reference** of the parent class inside the child class. For example, instead of storing a raw integer `user_id` inside `PaymentRequest`, the class contains an explicit instance member `User user`.
- **One-to-Many ($1:N$):** Represented by exposing an active **Collection / List** of child instances within the parent class structure. For instance, `class Transaction` maintains a `List<Refund> refunds` member variable.

### Complete LLD Class Blueprint (Java)

```java
import java.util.List;

class User {
    private String id;
    private String name;
    private String email;
    private List<PaymentRequest> paymentRequests; // 1:N List reference

    // Getters and Setters
}

class Merchant {
    private String id;
    private String businessName;
    private List<PaymentRequest> paymentRequests; // 1:N List reference

    // Getters and Setters
}

class PaymentRequest {
    private String id;
    private User user;                 // N:1 Direct Object Reference (FK Replacement)
    private Merchant merchant;         // N:1 Direct Object Reference (FK Replacement)
    private double amount;
    private String currency;
    private String status;
    private List<Transaction> transactions; // 1:N Retries List reference

    // Getters and Setters
}

class Transaction {
    private String id;
    private PaymentRequest paymentRequest; // N:1 Parent reference
    private PaymentMethod paymentMethod;   // N:1 Configuration reference
    private String status;
    private long transactionTime;
    private List<Refund> refunds;          // 1:N Partial Refunds List reference

    // Getters and Setters
}

class Refund {
    private String id;
    private Transaction transaction;       // N:1 Parent reference
    private double amount;
    private String reason;

    // Getters and Setters
}

class PaymentMethod {
    private String id;
    private String type; // e.g., "CARD", "UPI"
    private String detailsJson;

    // Getters and Setters
}

```

---

## 4. What is a DAO (Data Access Object)?

A **DAO (Data Access Object)** is a structural enterprise pattern where a dedicated, single-purpose class encapsulates the basic CRUD operations for one specific relational database table.

### Primary Purposes:

- **Encapsulates Data Operations:** Completely isolates raw SQL generation, connection pools, and platform-specific framework APIs away from system business logic layers.
- **Separation of Concerns:** Insulates higher-level architecture components from physical database structures. If a column name changes, only the isolated DAO layer code updates.

```java
// Explicit, single-table structural CRUD Interface boundary
interface PaymentRequestDAO {
    void save(PaymentRequest pr);
    PaymentRequest findById(String paymentRequestId);
    void updateStatus(String paymentRequestId, String newStatus);
}

```

### Architectural Constraints of DAOs:

- **Table-Centric Bound:** A DAO maps to one specific table structure. It does _not_ support building highly nested object graph linkages across distinct tables natively.
- **Too Low-Level for Domain Flows:** High-level enterprise interactions usually span across discrete tables. Fetching a full transaction ledger alongside its multi-tier partial refund collections requires executing multi-table choreography that raw DAOs are not designed to coordinate.

---

## 5. What is a Repository?

A **Repository** is a domain-centric abstraction layer sitting directly above your DAO infrastructure. It acts as an in-memory collection gateway managing complex structural **aggregates**—logical clusters of multi-table database records treated as one entity.

```
[ Service Layer ]
        │
        ▼
[ PaymentRepository ] ── (Orchestrates Business Intent)
        │
        ├─► [ PaymentRequestDAO ] ──► (Table: payment_request)
        ├─► [ TransactionDAO ]    ──► (Table: transaction)
        └─► [ RefundDAO ]         ──► (Table: refund)

```

### Strategic Architectural Directives:

- **Encapsulates Joins:** The repository handles execution pathways, joining separate table contents inside its layout boundaries.
- **No SQL in Service Layer:** Business components must never contain embedded SQL strings or ORM fetch structures. Services request data through intent-driven business boundaries, returning clean Data Transfer Objects (DTOs) or populated domain models.

```java
// Pure business-intent API abstractions detached from simple raw CRUD operations
interface PaymentRepository {
    void createPayment(String userId, String merchantId, double amount, String currency);
    FullPaymentDetailsDTO getFullPaymentDetails(String paymentRequestId);
    UserPaymentHistoryDTO getUserPaymentHistory(String userId, int page, int size);
    void markPaymentAsFailed(String paymentRequestId);
    void refundTransaction(String transactionId, double amount, String reason);
}

```

### Underlying Execution Mechanics of `getFullPaymentDetails`:

When an application service requests unified billing data graph properties via `getFullPaymentDetails(id)`, the Repository coordinates multiple low-level components:

1. Calls `PaymentRequestDAO.findById(id)` to retrieve core request headers.
2. Calls `TransactionDAO.findByRequestId(id)` to fetch complete gateway transaction histories.
3. Calls `RefundDAO.findByTransactionId(...)` to query matching partial transaction reversal lines.
4. Compiles individual table datasets into an atomic, coherent, highly structured domain entity aggregate or DTO returned to upper system boundaries.

---

## 6. Real-World Enhancements and Practices

Scaling distributed ledger state machines to secure million-user workflows requires applying precise low-level strategies to mitigate concurrent database failure risks:

| Production System Concern          | LLD Concrete Strategy                                          | Technical Execution Mechanism                                                                                                                                                                                        |
| ---------------------------------- | -------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Duplicate Payments**             | Use an **Idempotency Key** inside `PaymentRequest` structures. | A client-side unique hash string is validated against database logs prior to execution. Duplicate submissions matching active hashes are rejected instantly, neutralizing race conditions.                           |
| **Atomicity Fractures**            | Wrap operations inside ACID compliant **Transaction Blocks**.  | Interlocking operations (e.g., executing a balance deduction alongside logging audit status rows) are bound together. Any single node crash forces an automatic rollback of the full batch block.                    |
| **Dashboard Query Exhaustion**     | Enforce Server-Side **Pagination**.                            | Restricts mass record scans by exposing structured offset limits (e.g., `page`, `size`). Protects operational memory footprints against memory exhaustion.                                                           |
| **Query Explosion (N+1 Issue)**    | Force **Eager Fetching** / Explicit `JOIN` optimization.       | Ensures nested relational collections (e.g., loading 50 requests along with their underlying sub-transaction lines) are pulled using an optimized single-trip batch SQL `JOIN`, avoiding sequential looping queries. |
| **Sub-Millisecond Scaling Issues** | Apply Targeted **Individual and Composite Indexing**.          | Creates localized B-Tree fast lookup shortcuts matching heavy lookup patterns (e.g., setting a composite lookup index targeting `(merchant_id, status)` properties), eliminating sequential table scans.             |
