import uuid
from repository.payment_repository import PaymentRepository
from adapter.payment_adapters import PaymentGatewayAdapter, RazorpayAdapter, StripeAdapter
from domain.payment import Payment, PaymentGateway

class PaymentService:
    def __init__(self, payment_repo: PaymentRepository):
        self.payment_repository = payment_repo
        self.default_gateway: PaymentGatewayAdapter = RazorpayAdapter()
        print("[SERVICE] PaymentService initialized with default gateway: Razorpay")

    def process_payment(self, ticket_id: uuid.UUID, amount: float) -> bool:
        print(f"[SERVICE] Processing payment for ticket: {ticket_id} | Amount: ₹{amount}")
        payment = Payment(ticket_id, amount, PaymentGateway.RAZORPAY)
        self.payment_repository.save(payment)

        success = self.default_gateway.pay(ticket_id, amount)

        if success:
            payment.mark_as_success()
        else:
            payment.mark_as_failed()

        self.payment_repository.update(payment)
        print(f"[SERVICE] Payment processed with status: {'SUCCESS' if success else 'FAILED'}")
        return success

    def process_payment_with_retry(self, ticket_id: uuid.UUID, amount: float, max_retries: int) -> bool:
        print(f"[SERVICE] Processing payment with retry for ticket: {ticket_id} | Max retries: {max_retries}")

        for attempt in range(1, max_retries + 1):
            print(f"[SERVICE] Payment attempt {attempt} of {max_retries} via {self.default_gateway.get_gateway_name()}")

            success = self.process_payment(ticket_id, amount)
            if success:
                print(f"[SERVICE] Payment successful on attempt {attempt}")
                return True

            if attempt < max_retries:
                self.default_gateway = StripeAdapter()
                print("[SERVICE] Switching to Stripe gateway for retry")

        print(f"[SERVICE] All {max_retries} payment attempts failed for ticket: {ticket_id}")
        return False

    def set_default_gateway(self, gateway: PaymentGatewayAdapter):
        self.default_gateway = gateway
        print(f"[SERVICE] Gateway switched to: {gateway.get_gateway_name()}")
