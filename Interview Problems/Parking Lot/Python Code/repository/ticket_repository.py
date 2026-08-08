import uuid
from typing import Dict, List, Optional
from domain.ticket import Ticket

class TicketRepository:
    def __init__(self):
        self._store: Dict[uuid.UUID, Ticket] = {}

    def save(self, ticket: Ticket):
        self._store[ticket.id] = ticket
        print(f"[REPO] Ticket saved: {ticket.id}")

    def find_by_id(self, ticket_id: uuid.UUID) -> Optional[Ticket]:
        return self._store.get(ticket_id)

    def find_active_tickets(self) -> List[Ticket]:
        return [t for t in self._store.values() if t.is_active]

    def deactivate_ticket(self, ticket_id: uuid.UUID):
        ticket = self._store.get(ticket_id)
        if not ticket:
            raise ValueError(f"Ticket not found: {ticket_id}")
        ticket.deactivate()
        print(f"[REPO] Ticket deactivated: {ticket_id}")

    def update(self, ticket: Ticket):
        if ticket.id not in self._store:
            raise ValueError(f"Ticket not found: {ticket.id}")
        self._store[ticket.id] = ticket
