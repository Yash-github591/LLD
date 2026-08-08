import uuid
from typing import Optional
from service.ticket_service import TicketService
from service.pricing_service import PricingService
from service.payment_service import PaymentService
from service.receipt_service import ReceiptService
from service.slot_service import SlotService

class ExitResult:
    def __init__(self, success: bool, receipt_id: Optional[uuid.UUID], fee: float, message: str):
        self.success = success
        self.receipt_id = receipt_id
        self.fee = fee
        self.message = message

    def __repr__(self):
        return f"ExitResult(success={self.success}, receiptId={self.receipt_id}, fee=₹{self.fee:.2f}, message='{self.message}')"

class ExitController:
    def __init__(self, ticket_service: TicketService, pricing_service: PricingService,
                 payment_service: PaymentService, receipt_service: ReceiptService,
                 slot_service: SlotService):
        self.ticket_service = ticket_service
        self.pricing_service = pricing_service
        self.payment_service = payment_service
        self.receipt_service = receipt_service
        self.slot_service = slot_service
        print("[CONTROLLER] ExitController initialized")

    def exit_vehicle(self, ticket_id: uuid.UUID) -> ExitResult:
        print(f"[CONTROLLER] Vehicle exit request - Ticket: {ticket_id}")
        try:
            ticket = self.ticket_service.get_ticket(ticket_id)
            if not ticket:
                return ExitResult(False, None, 0.0, "Ticket not found")

            if not ticket.is_active:
                return ExitResult(False, None, 0.0, "Ticket is not active")

            fee = self.pricing_service.calculate_fee(ticket)
            print(f"[CONTROLLER] Fee calculated: ₹{fee}")

            payment_success = self.payment_service.process_payment_with_retry(ticket_id, fee, 3)
            if not payment_success:
                return ExitResult(False, None, fee, "Payment failed after retries")

            receipt = self.receipt_service.generate_receipt(ticket, fee)
            self.receipt_service.mark_receipt_as_paid(receipt)

            self.slot_service.release_slot(ticket.slot_id)
            self.ticket_service.deactivate_ticket(ticket_id)

            print(f"[CONTROLLER] Vehicle exit successful - Receipt: {receipt.id}")
            return ExitResult(True, receipt.id, fee, "Exit successful")

        except Exception as e:
            print(f"[CONTROLLER] Vehicle exit failed: {str(e)}")
            return ExitResult(False, None, 0.0, str(e))

    def generate_receipt_text(self, ticket_id: uuid.UUID) -> str:
        ticket = self.ticket_service.get_ticket(ticket_id)
        if not ticket:
            return f"Ticket not found: {ticket_id}"

        fee = self.pricing_service.calculate_fee(ticket)
        receipt = self.receipt_service.generate_receipt(ticket, fee)
        return self.receipt_service.generate_receipt_text(receipt, ticket)
