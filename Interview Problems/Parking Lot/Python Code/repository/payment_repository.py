import uuid
from typing import Dict, List, Optional
from domain.payment import Payment

class PaymentRepository:
    def __init__(self):
        self._store: Dict[uuid.UUID, Payment] = {}
        self._ticket_to_payments: Dict[uuid.UUID, List[uuid.UUID]] = {}

    def save(self, payment: Payment):
        self._store[payment.id] = payment
        if payment.ticket_id not in self._ticket_to_payments:
            self._ticket_to_payments[payment.ticket_id] = []
        self._ticket_to_payments[payment.ticket_id].append(payment.id)
        print(f"[REPO] Payment saved: {payment.id} for ticket: {payment.ticket_id}")

    def find_by_id(self, payment_id: uuid.UUID) -> Optional[Payment]:
        return self._store.get(payment_id)

    def find_by_ticket_id(self, ticket_id: uuid.UUID) -> Optional[Payment]:
        payment_ids = self._ticket_to_payments.get(ticket_id, [])
        if not payment_ids:
            return None
        last_id = payment_ids[-1]
        return self._store.get(last_id)

    def find_all_by_ticket_id(self, ticket_id: uuid.UUID) -> List[Payment]:
        payment_ids = self._ticket_to_payments.get(ticket_id, [])
        return [self._store[pid] for pid in payment_ids if pid in self._store]

    def update(self, payment: Payment):
        if payment.id not in self._store:
            raise ValueError(f"Payment not found: {payment.id}")
        self._store[payment.id] = payment
        print(f"[REPO] Payment updated: {payment.id} | Status: {payment.status.name}")
