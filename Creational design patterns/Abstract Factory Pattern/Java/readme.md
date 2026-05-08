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

```java
PaymentGateway gateway = new RazorPayGateway();
Invoice invoice = new GSTInvoice();
```

Issues:

- Client depends on concrete classes
- Hard to switch product families (India → US)
- Violates Open/Closed Principle
- Code becomes difficult to maintain as variants increase

---

## How Abstract Factory Helps

Abstract Factory groups related product creation under one factory.

```java
RegionFactory factory = new IndiaFactory();

PaymentGateway gateway = factory.createPaymentGateway("razorpay");
Invoice invoice = factory.createInvoice();
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

```java
interface PaymentGateway{
  // Function to implement region specific logic
  public void processPayment(double amount);
}
```

---

## 2. Concrete Payment Products

### India:

```java
class RazorpayGateway implements PaymentGateway{
  @Override
  public void processPayment(double amount){
    System.out.println("Processing INR payments vis Razorpay: "+amount);
  }
}
```

```java
class PayUGateway implements PaymentGateway{
  @Override
  public void processPayment(double amount){
    System.out.println("Processing INR payment via payU: "+amount);
  }
}
```

### US:

```java
class StripeGateway implements PaymentGateway{
  @Override
  public void processPayment(double amount){
    System.out.println("Processing USD payment via Stripe: "+amount);
  }
}
```

```java
class PaypalGateway implements PaymentGateway{
  @Override
  public void processPayment(double amount){
    System.out.println("Processing USD payment via Paypal: "+amount);
  }
}
```

---

## 3. Abstract Product: Invoice

Defines invoice generation behavior.

```java
interface Invoice{
  // Each region will generate invoices differently
  public void generateInvoice();
}
```

---

## 4. Concrete Invoice Products

```java
class GSTInvoice implements Invoice{
  @Override
  public void generateInvoice(){
    System.out.println("Generating GST Invoice for India");
  }
}
```

```java
class USInvoice implements Invoice{
  @Override
  public void generateInvoice(){
    System.out.println("Generating US-compliant Invoice");
  }
}
```

---

## 5. Abstract Factory

Declares creation methods for product families.

```java
interface RegionFactory{
  // Creates payment gateway based on regin + gateway type
  PaymentGateway createPaymentGateway(String gatewayType);

  // Creates regin-specific invoice
  Invoice createInvoice();
}
```

---

## 6. Concrete Factory: India

```java
class IndianFactory implements RegionFactory{
  @Override
  public PaymentGateway createPaymentGateway(String gatewayType){
    // Factory decides which concrete class to instantiate
    if(gatewayType=="razorpay"){
      return new RazorpayGateway();
    }
    else if(gatewayType=="payu"){
      return new PayUGateway();
    }

    // Unsupported gateway case
    System.out.println("Unsupported payment gateway in India: "+gatewayType);
    return null;
  }

  @Override
  public Invoice createInvoice(){
    return new GSTInvoice();
  }
}
```

---

## 7. Concrete Factory: US

```java
class USFactory implements RegionFactory{
  @Override
  public PaymentGateway createPaymentGateway(String gatewayType){
    if(gatewayType=="stripe"){
      return new StripeGateway();
    }
    else if(gatewayType=="paypal"){
      return new PaypalGateway();
    }

    // Unsupported gateway case
    System.out.println("Unsupported payment gateway in US: "+gatewayType);
    return null;
  }

  @Override
  public Invoice createInvoice(){
    return new USInvoice();
  }
}
```

---

## 8. Client Class

Client remains independent from concrete implementations.

```java
class CheckoutService{
  private PaymentGateway paymentGateway; // Region-specific payment gateway
  private Invoice invoice; // Region-specific invoice

  // Constructor receives a factory object dynamically
  public CheckoutService(RegionFactory factory, String gatewayType){
    // Factory creates appropriate products
    paymentGateway=factory.createPaymentGateway(gatewayType);
    invoice=factory.createInvoice();
  }

  // Handles full checkout process
  void completeOrder(double amount){
    // Safety check in case gateway creation failed
    if(paymentGateway!=null){
      paymentGateway.processPayment(amount);
      invoice.generateInvoice();
    }
  }
}
```

---

## Example Flow

```java
CheckOutService indiaCheckout = new CheckOutService(new IndiaFactory(), "razorpay");
indiaCheckout.completeOrder(100.0);

CheckOutService usCheckout = new CheckOutService(new USFactory(), "paypal");
usCheckout.completeOrder(23);
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

```java
VehicleFactory.createVehicle()
```

### Abstract Factory:

Creates **multiple related products**

```java
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
