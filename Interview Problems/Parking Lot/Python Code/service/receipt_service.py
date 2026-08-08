import uuid
from datetime import datetime
from domain.ticket import Ticket
from domain.receipt import Receipt

class ReceiptService:
    DATE_FORMAT = "%d-%m-%Y %H:%M:%S"

    def __init__(self):
        print("[SERVICE] ReceiptService initialized")

    def generate_receipt(self, ticket: Ticket, fee: float) -> Receipt:
        print(f"[SERVICE] Generating receipt for ticket: {ticket.id}")
        receipt = Receipt(ticket.id, fee)
        print(f"[SERVICE] Receipt generated: {receipt.id} with fee: ₹{fee}")
        return receipt

    def mark_receipt_as_paid(self, receipt: Receipt):
        print(f"[SERVICE] Marking receipt as paid: {receipt.id}")
        receipt.mark_as_paid()
        print("[SERVICE] Receipt marked as paid successfully")

    def generate_receipt_text(self, receipt: Receipt, ticket: Ticket) -> str:
        print(f"[SERVICE] Generating receipt text for: {receipt.id}")
        exit_time = receipt.exit_time
        duration = exit_time - ticket.entry_time
        minutes_parked = int(duration.total_seconds() / 60)
        hours_parked = minutes_parked // 60
        remaining_minutes = minutes_parked % 60

        lines = [
            "",
            "=====================================",
            "         🧾 PARKING RECEIPT         ",
            "=====================================",
            f"{'Receipt ID':<14}: {receipt.id}",
            f"{'Ticket ID':<14}: {ticket.id}",
            f"{'Slot ID':<14}: {ticket.slot_id}",
            "-------------------------------------",
            f"{'Entry Time':<14}: {ticket.entry_time.strftime(self.DATE_FORMAT)}",
            f"{'Exit Time':<14}: {exit_time.strftime(self.DATE_FORMAT)}",
            f"{'Duration':<14}: {hours_parked}h {remaining_minutes}m",
            "-------------------------------------",
            f"{'Total Fee':<14}: ₹{receipt.total_fee:.2f}",
            f"{'Status':<14}: {receipt.payment_status.name}",
            "=====================================",
            "   Thank you for parking with us!   ",
            "=====================================",
            ""
        ]
        print("[SERVICE] Receipt text generated successfully")
        return "\n".join(lines)
