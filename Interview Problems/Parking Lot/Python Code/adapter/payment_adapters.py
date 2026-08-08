from abc import ABC, abstractmethod
import uuid

class PaymentGatewayAdapter(ABC):
    @abstractmethod
    def pay(self, ticket_id: uuid.UUID, amount: float) -> bool:
        pass

    @abstractmethod
    def get_gateway_name(self) -> str:
        pass

class RazorpayAdapter(PaymentGatewayAdapter):
    def pay(self, ticket_id: uuid.UUID, amount: float) -> bool:
        print(f"[RAZORPAY] Processing payment of ₹{amount} for ticket {ticket_id}")
        # Simulate gateway call
        return True

    def get_gateway_name(self) -> str:
        return "Razorpay"

class StripeAdapter(PaymentGatewayAdapter):
    def pay(self, ticket_id: uuid.UUID, amount: float) -> bool:
        print(f"[STRIPE] Processing payment of ₹{amount} for ticket {ticket_id}")
        # Simulate gateway call
        return True

    def get_gateway_name(self) -> str:
        return "Stripe"
