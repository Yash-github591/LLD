# Abstract Factory Design Pattern

## Introduction

The **Abstract Factory Design Pattern** is a creational design pattern that provides an interface for creating **families of related or dependent objects** without specifying their concrete classes.

It is often called a **Factory of Factories** because instead of directly creating objects, the client works with a factory that creates multiple related products.

This pattern is useful when a system must support multiple product variants (such as regional settings, themes, operating systems, or database vendors) while keeping client code independent from concrete implementations.

### Key Components

1. **Abstract Factory**  
   Declares methods for creating abstract products.

2. **Concrete Factories**  
   Implement the abstract factory and create specific product families.

3. **Abstract Products**  
   Common interfaces for product types.

4. **Concrete Products**  
   Specific implementations of abstract products.

5. **Client**  
   Uses only abstract interfaces and remains decoupled from actual implementations.

---

## Problem it Solves

When client code directly creates concrete objects, it becomes tightly coupled to specific implementations.

Example:

```cpp
PaymentGateway* gateway = new RazorPayGateway();
Invoice* invoice = new GSTInvoice();
```

Issues:

- Client depends on concrete classes
- Hard to switch product families (India → US)
- Violates Open/Closed Principle
- Code becomes difficult to maintain as variants increase

---

## How Abstract Factory Helps

Abstract Factory groups related product creation under one factory.

```cpp
RegionFactory* factory = new IndiaFactory();

PaymentGateway* gateway = factory->createPaymentGateway("razorpay");
Invoice* invoice = factory->createInvoice();
```

Key idea:

- Factory decides which concrete objects to create
- Client uses interfaces only
- Entire product family can be switched easily

Benefits:

- Decouples client from object creation
- Ensures compatible product families
- Makes code scalable and maintainable
- Supports Dependency Inversion Principle

---

## Real-World Example (Payment Processing System)

In this implementation:

### India Factory creates:

- Razorpay / PayU
- GST Invoice

### US Factory creates:

- Stripe / PayPal
- US Invoice

This ensures region-specific payment systems are grouped correctly.

---

# Structure

## 1. Abstract Product: PaymentGateway

Defines common payment behavior.

```cpp
class PaymentGateway {
public:
    virtual void processPayment(double amount) = 0;
    virtual ~PaymentGateway() = default;
};
```

---

## 2. Concrete Payment Products

### India:

```cpp
class RazorPayGateway : public PaymentGateway {
public:
    void processPayment(double amount) override {
        cout << "Processing INR payment via Razorpay: " << amount << endl;
    }
};
```

```cpp
class PayUGateway : public PaymentGateway {
public:
    void processPayment(double amount) override {
        cout << "Processing INR payment via PayU: " << amount << endl;
    }
};
```

### US:

```cpp
class StripeGateway : public PaymentGateway {
public:
    void processPayment(double amount) override {
        cout << "Processing USD payment via Stripe: " << amount << endl;
    }
};
```

```cpp
class PaypalGateway : public PaymentGateway {
public:
    void processPayment(double amount) override {
        cout << "Processing USD payment via PayPal: " << amount << endl;
    }
};
```

---

## 3. Abstract Product: Invoice

Defines invoice generation behavior.

```cpp
class Invoice {
public:
    virtual void generateInvoice() = 0;
    virtual ~Invoice() = default;
};
```

---

## 4. Concrete Invoice Products

```cpp
class GSTInvoice : public Invoice {
public:
    void generateInvoice() override {
        cout << "Generating GST Invoice for India." << endl;
    }
};
```

```cpp
class USInvoice : public Invoice {
public:
    void generateInvoice() override {
        cout << "Generating US-compliant Invoice." << endl;
    }
};
```

---

## 5. Abstract Factory

Declares creation methods for product families.

```cpp
class RegionFactory {
public:
    virtual PaymentGateway* createPaymentGateway(string gatewayType) = 0;
    virtual Invoice* createInvoice() = 0;
    virtual ~RegionFactory() = default;
};
```

---

## 6. Concrete Factory: India

```cpp
class IndiaFactory : public RegionFactory {
public:
    PaymentGateway* createPaymentGateway(string gatewayType) override {
        if (gatewayType == "razorpay")
            return new RazorPayGateway();
        else if (gatewayType == "payu")
            return new PayUGateway();

        return nullptr;
    }

    Invoice* createInvoice() override {
        return new GSTInvoice();
    }
};
```

---

## 7. Concrete Factory: US

```cpp
class USFactory : public RegionFactory {
public:
    PaymentGateway* createPaymentGateway(string gatewayType) override {
        if (gatewayType == "stripe")
            return new StripeGateway();
        else if (gatewayType == "paypal")
            return new PaypalGateway();

        return nullptr;
    }

    Invoice* createInvoice() override {
        return new USInvoice();
    }
};
```

---

## 8. Client Class

Client remains independent from concrete implementations.

```cpp
class CheckOutService {
private:
    PaymentGateway* paymentGateway;
    Invoice* invoice;

public:
    CheckOutService(RegionFactory* factory, string gatewayType) {
        paymentGateway = factory->createPaymentGateway(gatewayType);
        invoice = factory->createInvoice();
    }

    void completeOrder(double amount) {
        if (paymentGateway) {
            paymentGateway->processPayment(amount);
            invoice->generateInvoice();
        }
    }

    ~CheckOutService() {
        delete paymentGateway;
        delete invoice;
    }
};
```

---

## Example Flow

```cpp
CheckOutService* indiaCheckout =
    new CheckOutService(new IndiaFactory(), "razorpay");

indiaCheckout->completeOrder(100.0);

CheckOutService* usCheckout =
    new CheckOutService(new USFactory(), "paypal");

usCheckout->completeOrder(23);
```

Execution Steps:

1. Client selects a factory based on region
2. Factory creates region-specific payment gateway
3. Factory creates matching invoice system
4. Checkout service processes payment
5. Correct invoice is generated automatically

---

## When to Use

- When your system must support multiple product families
- When products within a family must be used together
- When object creation logic should be centralized
- When adding new product families should not affect client code

---

## Advantages

- Easy switching between product families
- Promotes consistency between related products
- Supports scalability
- Follows Open Closed principles
- Reduces tight coupling
- Centralised object creation logic

---

## Disadvantages

- Increases number of classes
- Can become complex for small systems
- Adding new product types may require modifying all factories

---

## Factory Method vs Abstract Factory

### Factory Method:

Creates **one product**

```cpp
VehicleFactory -> createVehicle()
```

### Abstract Factory:

Creates **multiple related products**

```cpp
RegionFactory -> createPaymentGateway()
             -> createInvoice()
```

---

## Summary

The **Abstract Factory Pattern** provides a way to create **families of related objects** while keeping client code independent from concrete implementations.

In this payment system example:

- IndiaFactory → Razorpay/PayU + GST Invoice
- USFactory → Stripe/PayPal + US Invoice

This pattern is ideal when building scalable systems that support multiple environments, configurations, or regional variations.
