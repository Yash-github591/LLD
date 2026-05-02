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

#include <bits/stdc++.h>
using namespace std;

/*=========================================================
    ABSTRACT PRODUCT 1: PaymentGateway
    Common interface for all payment gateway types
=========================================================*/
class PaymentGateway {
public:
    // Pure virtual function forces derived classes
    // to implement region-specific payment logic
    virtual void processPayment(double amount) = 0;

    // Virtual destructor ensures proper cleanup
    virtual ~PaymentGateway() = default;
};

/*=========================================================
    CONCRETE PRODUCTS FOR INDIA
=========================================================*/

// Razorpay implementation for Indian payments
class RazorPayGateway : public PaymentGateway {
public:
    void processPayment(double amount) override {
        cout << "Processing INR payment via Razorpay: " << amount << endl;
    }
};

// PayU implementation for Indian payments
class PayUGateway : public PaymentGateway {
public:
    void processPayment(double amount) override {
        cout << "Processing INR payment via PayU: " << amount << endl;
    }
};

/*=========================================================
    CONCRETE PRODUCTS FOR US
=========================================================*/

// Stripe implementation for US payments
class StripeGateway : public PaymentGateway {
public:
    void processPayment(double amount) override {
        cout << "Processing USD payment via Stripe: " << amount << endl;
    }
};

// PayPal implementation for US payments
class PaypalGateway : public PaymentGateway {
public:
    void processPayment(double amount) override {
        cout << "Processing USD payment via PayPal: " << amount << endl;
    }
};

/*=========================================================
    ABSTRACT PRODUCT 2: Invoice
    Common interface for invoice generation
=========================================================*/
class Invoice {
public:
    // Each region will generate invoices differently
    virtual void generateInvoice() = 0;

    virtual ~Invoice() = default;
};

/*=========================================================
    CONCRETE INVOICE PRODUCTS
=========================================================*/

// GST-compliant invoice for India
class GSTInvoice : public Invoice {
public:
    void generateInvoice() override {
        cout << "Generating GST Invoice for India." << endl;
    }
};

// US tax-compliant invoice
class USInvoice : public Invoice {
public:
    void generateInvoice() override {
        cout << "Generating US-compliant Invoice." << endl;
    }
};

/*=========================================================
    ABSTRACT FACTORY
    Creates a family of related products:
    1. PaymentGateway
    2. Invoice
=========================================================*/
class RegionFactory {
public:
    // Creates payment gateway based on region + gateway type
    virtual PaymentGateway* createPaymentGateway(string gatewayType) = 0;

    // Creates region-specific invoice
    virtual Invoice* createInvoice() = 0;

    virtual ~RegionFactory() = default;
};

/*=========================================================
    CONCRETE FACTORY: INDIA
=========================================================*/
class IndiaFactory : public RegionFactory {
public:
    PaymentGateway* createPaymentGateway(string gatewayType) override {
        // Factory decides which concrete class to instantiate
        if (gatewayType == "razorpay") {
            return new RazorPayGateway();
        }
        else if (gatewayType == "payu") {
            return new PayUGateway();
        }

        // Unsupported gateway case
        cout << "Unsupported payment gateway in India: " << gatewayType << endl;
        return nullptr;
    }

    Invoice* createInvoice() override {
        return new GSTInvoice();
    }
};

/*=========================================================
    CONCRETE FACTORY: US
=========================================================*/
class USFactory : public RegionFactory {
public:
    PaymentGateway* createPaymentGateway(string gatewayType) override {
        if (gatewayType == "stripe") {
            return new StripeGateway();
        }
        else if (gatewayType == "paypal") {
            return new PaypalGateway();
        }

        cout << "Unsupported payment gateway in US: " << gatewayType << endl;
        return nullptr;
    }

    Invoice* createInvoice() override {
        return new USInvoice();
    }
};

/*=========================================================
    CLIENT CLASS: CheckoutService
    Uses abstract factory without knowing concrete classes
=========================================================*/
class CheckOutService {
private:
    PaymentGateway* paymentGateway;  // Region-specific payment gateway
    Invoice* invoice;                // Region-specific invoice

public:
    // Constructor receives a factory object dynamically
    CheckOutService(RegionFactory* factory, string gatewayType) {
        // Factory creates appropriate products
        paymentGateway = factory->createPaymentGateway(gatewayType);
        invoice = factory->createInvoice();
    }

    // Handles full checkout process
    void completeOrder(double amount) {
        // Safety check in case gateway creation failed
        if (paymentGateway) {
            paymentGateway->processPayment(amount);
            invoice->generateInvoice();
        }
    }

    // Destructor prevents memory leaks
    ~CheckOutService() {
        delete paymentGateway;
        delete invoice;
    }
};

/*=========================================================
    DRIVER CODE
=========================================================*/
int main() {

    // India region checkout using Razorpay
    CheckOutService* checkOutService_1 =
        new CheckOutService(new IndiaFactory(), "razorpay");

    checkOutService_1->completeOrder(100.0);

    cout << "--------------------------" << endl;

    // US region checkout using PayPal
    CheckOutService* checkOutService_2 =
        new CheckOutService(new USFactory(), "paypal");

    checkOutService_2->completeOrder(23);

    /*--------------------------------------------
        Cleanup dynamically allocated memory
    --------------------------------------------*/
    delete checkOutService_1;
    delete checkOutService_2;

    return 0;
}
