import uuid
from datetime import datetime
from domain.payment import PaymentStatus

class Receipt:
    def __init__(self, ticket_id: uuid.UUID, total_fee: float):
        self.id = uuid.uuid4()
        self.ticket_id = ticket_id
        self.total_fee = total_fee
        self.exit_time = datetime.now()
        self.payment_status = PaymentStatus.PENDING

    def mark_as_paid(self):
        self.payment_status = PaymentStatus.SUCCESS

    def mark_as_failed(self):
        self.payment_status = PaymentStatus.FAILED

    def __repr__(self):
        return f"Receipt(id={self.id}, ticket_id={self.ticket_id}, fee={self.total_fee}, status={self.payment_status.name})"
