# Abstract Factory Pattern is a creational design pattern that provides an interface for creating 
# families of related or dependent objects without specifying their concrete classes. 

# It is often described as a "factory of factories" because it abstracts the process of object 
# creation by defining a super-factory interface that creates other factories at runtime.

# Core Components:
# 1. Abstract Factory: An interface declaring a set of methods for creating each of the abstract 
#     products (e.g., RegionFactory).
# 2. Concrete Factories: Classes that implement the abstract factory's methods to produce specific 
#     variants of products (e.g., IndianFactory, USFactory).
# 3. Abstract Products: Interfaces or abstract classes for a set of related components (e.g., PaymentGateway,
#      Invoice).
# 4. Concrete Products: Specific implementations of the abstract products, grouped by variants (e.g., 
#     RazorpayGateway, PayUGateway).
# 5. Client: Uses only the interfaces declared by the abstract factory and abstract products to 
#     interact with the objects, keeping the code decoupled from concrete implementations.



# In this example, we will implement an Abstract Factory Pattern for a payment processing system that 
# supports multiple regions. We will have two regions: India and the US, each with its own payment 
# gateway and invoice system.

from abc import ABC, abstractmethod

# =========================================================
#   ABSTRACT PRODUCT 1: PaymentGateway
#   Common interface for all payment gateway types
# =========================================================
class PaymentGateway(ABC):
    @abstractmethod
    def process_payment(self, amount: float):
        pass


# =========================================================
#   CONCRETE PRODUCTS FOR INDIA
# =========================================================
class RazorpayGateway(PaymentGateway):
    def process_payment(self, amount: float):
        print(f"Processing INR payments via Razorpay: {amount}")

class PayUGateway(PaymentGateway):
    def process_payment(self, amount: float):
        print(f"Processing INR payment via PayU: {amount}")


# =========================================================
#   CONCRETE PRODUCTS FOR US
# =========================================================
class StripeGateway(PaymentGateway):
    def process_payment(self, amount: float):
        print(f"Processing USD payment via Stripe: {amount}")

class PaypalGateway(PaymentGateway):
    def process_payment(self, amount: float):
        print(f"Processing USD payment via Paypal: {amount}")


# =========================================================
#   ABSTRACT PRODUCT 2: Invoice
#   Common interface for invoice generation
# =========================================================
class Invoice(ABC):
    @abstractmethod
    def generate_invoice(self):
        pass


# =========================================================
#   CONCRETE INVOICE PRODUCTS
# =========================================================
class GSTInvoice(Invoice):
    def generate_invoice(self):
        print("Generating GST Invoice for India")

class USInvoice(Invoice):
    def generate_invoice(self):
        print("Generating US-compliant Invoice")


# =========================================================
#   ABSTRACT FACTORY
#   Creates a family of related products:
#   1. PaymentGateway
#   2. Invoice
# =========================================================
class RegionFactory(ABC):
    @abstractmethod
    def create_payment_gateway(self, gateway_type: str) -> PaymentGateway:
        pass
    
    @abstractmethod
    def create_invoice(self) -> Invoice:
        pass


# =========================================================
#   CONCRETE FACTORY: INDIA
# =========================================================
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


# =========================================================
#   CONCRETE FACTORY: US
# =========================================================
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


# =========================================================
#   CLIENT CLASS: CheckoutService
#   Uses abstract factory without knowing concrete classes
# =========================================================
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


# Driver Code
if __name__ == "__main__":
    # India region checkout using Razorpay
    checkout_service_1 = CheckoutService(IndianFactory(), "razorpay")
    checkout_service_1.complete_order(100.0)
    
    print("......................................")
    
    # US region checkout using PayPal 
    checkout_service_2 = CheckoutService(USFactory(), "paypal")
    checkout_service_2.complete_order(23.4)