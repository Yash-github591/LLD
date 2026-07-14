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

```python
PaymentGateway gateway = RazorPayGateway()
Invoice invoice = GSTInvoice()
```

Issues:

- Client depends on concrete classes
- Hard to switch product families (India → US)
- Violates Open/Closed Principle
- Code becomes difficult to maintain as variants increase

---

## How Abstract Factory Helps

Abstract Factory groups related product creation under one factory.

```python
RegionFactory factory = IndiaFactory()

PaymentGateway gateway = factory.createPaymentGateway("razorpay")
Invoice invoice = factory.createInvoice()
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

```python
class PaymentGateway(ABC):
    @abstractmethod
    def process_payment(self, amount: float):
        pass
```

---

## 2. Concrete Payment Products

### India:

```python
class RazorpayGateway(PaymentGateway):
    def process_payment(self, amount: float):
        print(f"Processing INR payments via Razorpay: {amount}")
```

```python
class PayUGateway(PaymentGateway):
    def process_payment(self, amount: float):
        print(f"Processing INR payment via PayU: {amount}")
```

### US:

```python
class StripeGateway(PaymentGateway):
    def process_payment(self, amount: float):
        print(f"Processing USD payment via Stripe: {amount}")
```

```python
class PaypalGateway(PaymentGateway):
    def process_payment(self, amount: float):
        print(f"Processing USD payment via Paypal: {amount}")
```

---

## 3. Abstract Product: Invoice

Defines invoice generation behavior.

```python
class Invoice(ABC):
    @abstractmethod
    def generate_invoice(self):
        pass
```

---

## 4. Concrete Invoice Products

```python
class GSTInvoice(Invoice):
    def generate_invoice(self):
        print("Generating GST Invoice for India")
```

```python
class USInvoice(Invoice):
    def generate_invoice(self):
        print("Generating US-compliant Invoice")
```

---

## 5. Abstract Factory

Declares creation methods for product families.

```python
class RegionFactory(ABC):
    @abstractmethod
    def create_payment_gateway(self, gateway_type: str) -> PaymentGateway:
        pass

    @abstractmethod
    def create_invoice(self) -> Invoice:
        pass
```

---

## 6. Concrete Factory: India

```python
class IndianFactory(RegionFactory):
    def create_payment_gateway(self, gateway_type: str) -> PaymentGateway:
        if gateway_type == "razorpay":
            return RazorpayGateway()
        elif gateway_type == "payu":
            return PayUGateway()

        # Unsupported gateway case
        print(f"Unsupported payment gateway in India: {gateway_type}")
        return None

    def create_invoice(self) -> Invoice:
        return GSTInvoice()
```

---

## 7. Concrete Factory: US

```python
class USFactory(RegionFactory):
    def create_payment_gateway(self, gateway_type: str) -> PaymentGateway:
        if gateway_type == "stripe":
            return StripeGateway()
        elif gateway_type == "paypal":
            return PaypalGateway()

        # Unsupported gateway case
        print(f"Unsupported payment gateway in US: {gateway_type}")
        return None

    def create_invoice(self) -> Invoice:
        return USInvoice()
```

---

## 8. Client Class

Client remains independent from concrete implementations.

```python
class CheckoutService:
    def __init__(self, factory: RegionFactory, gateway_type: str):
        # Factory creates appropriate products
        self.payment_gateway = factory.create_payment_gateway(gateway_type)
        self.invoice = factory.create_invoice()

    def complete_order(self, amount: float):
        # Safety check in case gateway creation failed
        if self.payment_gateway is not None:
            self.payment_gateway.process_payment(amount)
            self.invoice.generate_invoice()
```

---

## Example Flow

```python
# India region checkout using Razorpay
checkout_service_1 = CheckoutService(IndianFactory(), "razorpay")
checkout_service_1.complete_order(100.0)

# US region checkout using PayPal
checkout_service_2 = CheckoutService(USFactory(), "paypal")
checkout_service_2.complete_order(23.4)
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

```python
VehicleFactory.createVehicle()
```

### Abstract Factory:

Creates **multiple related products**

```python
RegionFactory -> createPaymentGateway() -> createInvoice()
```

---

## Summary

The **Abstract Factory Pattern** provides a way to create **families of related objects** while keeping client code independent from concrete implementations.

In this payment system example:

- IndiaFactory → Razorpay/PayU + GST Invoice
- USFactory → Stripe/PayPal + US Invoice

This pattern is ideal when building scalable systems that support multiple environments, configurations, or regional variations.
