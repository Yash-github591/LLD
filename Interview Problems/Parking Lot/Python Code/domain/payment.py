import uuid
from enum import Enum, auto

class PaymentStatus(Enum):
    PENDING = auto()
    SUCCESS = auto()
    FAILED = auto()

class PaymentGateway(Enum):
    RAZORPAY = auto()
    STRIPE = auto()

class Payment:
    def __init__(self, ticket_id: uuid.UUID, amount: float, gateway: PaymentGateway):
        self.id = uuid.uuid4()
        self.ticket_id = ticket_id
        self.amount = amount
        self.gateway = gateway
        self.status = PaymentStatus.PENDING

    def mark_as_success(self):
        self.status = PaymentStatus.SUCCESS

    def mark_as_failed(self):
        self.status = PaymentStatus.FAILED

    def __repr__(self):
        return f"Payment(id={self.id}, ticket_id={self.ticket_id}, amount={self.amount}, gateway={self.gateway.name}, status={self.status.name})"
