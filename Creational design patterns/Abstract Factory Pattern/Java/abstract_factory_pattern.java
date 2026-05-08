/*
Abstract Factory Pattern is a creational design pattern that provides an interface for creating 
families of related or dependent objects without specifying their concrete classes. 

It is often described as a "factory of factories" because it abstracts the process of object 
creation by defining a super-factory interface that creates other factories at runtime.

Core Components:
1. Abstract Factory: An interface declaring a set of methods for creating each of the abstract 
    products (e.g., RegionFactory).
2. Concrete Factories: Classes that implement the abstract factory's methods to produce specific 
    variants of products (e.g., IndianFactory, USFactory).
3. Abstract Products: Interfaces or abstract classes for a set of related components (e.g., PaymentGateway,
     Invoice).
4. Concrete Products: Specific implementations of the abstract products, grouped by variants (e.g., 
    RazorpayGateway, PayUGateway).
5. Client: Uses only the interfaces declared by the abstract factory and abstract products to 
    interact with the objects, keeping the code decoupled from concrete implementations.
*/

/*
In this example, we will implement an Abstract Factory Pattern for a payment processing system that 
supports multiple regions. We will have two regions: India and the US, each with its own payment 
gateway and invoice system.
*/

import java.util.*;

/*=========================================================
    ABSTRACT PRODUCT 1: PaymentGateway
    Common interface for all payment gateway types
=========================================================*/

interface PaymentGateway{
  // Function to implement region specific logic
  public void processPayment(double amount);
}



/*=========================================================
    CONCRETE PRODUCTS FOR INDIA
=========================================================*/

// Razorpay implementation for Indian payments
class RazorpayGateway implements PaymentGateway{
  @Override
  public void processPayment(double amount){
    System.out.println("Processing INR payments vis Razorpay: "+amount);
  }
}

// PayU implementation for Indian payments
class PayUGateway implements PaymentGateway{
  @Override
  public void processPayment(double amount){
    System.out.println("Processing INR payment via payU: "+amount);
  }
}

/*=========================================================
    CONCRETE PRODUCTS FOR US
=========================================================*/
// Stripe implementation for US payments
class StripeGateway implements PaymentGateway{
  @Override
  public void processPayment(double amount){
    System.out.println("Processing USD payment via Stripe: "+amount);
  }
}

// PayPal implementation for US payments
class PaypalGateway implements PaymentGateway{
  @Override
  public void processPayment(double amount){
    System.out.println("Processing USD payment via Paypal: "+amount);
  }
}

/*=========================================================
    ABSTRACT PRODUCT 2: Invoice
    Common interface for invoice generation
=========================================================*/
interface Invoice{
  // Each region will generate invoices differently
  public void generateInvoice();
}

/*=========================================================
    CONCRETE INVOICE PRODUCTS
=========================================================*/
// GST-compliant invoice for India 
class GSTInvoice implements Invoice{
  @Override
  public void generateInvoice(){
    System.out.println("Generating GST Invoice for India");
  }
}

// US tax-compliant invoice 
class USInvoice implements Invoice{
  @Override
  public void generateInvoice(){
    System.out.println("Generating US-compliant Invoice");
  }
}

/*=========================================================
    ABSTRACT FACTORY
    Creates a family of related products:
    1. PaymentGateway
    2. Invoice
=========================================================*/
interface RegionFactory{
  // Creates payment gateway based on regin + gateway type 
  PaymentGateway createPaymentGateway(String gatewayType);
  
  // Creates regin-specific invoice 
  Invoice createInvoice();
}

/*=========================================================
    CONCRETE FACTORY: INDIA
=========================================================*/
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

/*=========================================================
    CONCRETE FACTORY: US
=========================================================*/
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

/*=========================================================
    CLIENT CLASS: CheckoutService
    Uses abstract factory without knowing concrete classes
=========================================================*/
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

// Driver Code 
public class Main {
    public static void main(String[] args) throws Exception {
      // India region checkout using Razorpay
      CheckoutService checkoutService_1 = new CheckoutService(new IndianFactory(),"razorpay");
      checkoutService_1.completeOrder(100.0);
      
      System.out.println("......................................");
      
      // US region checkout using PayPal 
      CheckoutService checkoutService_2 = new CheckoutService(new USFactory(),"paypal");
      checkoutService_2.completeOrder(23.4);
    }
}