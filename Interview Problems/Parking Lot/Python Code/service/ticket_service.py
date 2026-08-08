import uuid
from typing import Optional
from domain.vehicle import Vehicle
from domain.parking_slot import ParkingSlot
from domain.ticket import Ticket
from repository.ticket_repository import TicketRepository

class TicketService:
    def __init__(self, ticket_repo: TicketRepository):
        self.ticket_repository = ticket_repo
        print("[SERVICE] TicketService initialized")

    def generate_ticket(self, vehicle: Vehicle, slot: ParkingSlot) -> Ticket:
        print(f"[SERVICE] Generating ticket for vehicle: {vehicle.id} | Slot: {slot.id}")
        ticket = Ticket(vehicle.id, slot.id)
        self.ticket_repository.save(ticket)
        print(f"[SERVICE] Ticket generated: {ticket.id}")
        return ticket

    def get_ticket(self, ticket_id: uuid.UUID) -> Optional[Ticket]:
        print(f"[SERVICE] Fetching ticket: {ticket_id}")
        return self.ticket_repository.find_by_id(ticket_id)

    def deactivate_ticket(self, ticket_id: uuid.UUID):
        print(f"[SERVICE] Deactivating ticket: {ticket_id}")
        self.ticket_repository.deactivate_ticket(ticket_id)
