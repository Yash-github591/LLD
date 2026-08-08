from typing import Optional
from domain.vehicle import Vehicle, VehicleType
from domain.ticket import Ticket
from repository.vehicle_repository import VehicleRepository
from service.slot_service import SlotService
from service.ticket_service import TicketService

class EntryResult:
    def __init__(self, success: bool, ticket: Optional[Ticket], message: str):
        self.success = success
        self.ticket = ticket
        self.message = message

    def __repr__(self):
        ticket_id = self.ticket.id if self.ticket else "null"
        return f"EntryResult(success={self.success}, ticketId={ticket_id}, message='{self.message}')"

class EntryController:
    def __init__(self, slot_service: SlotService, ticket_service: TicketService, vehicle_repo: VehicleRepository):
        self.slot_service = slot_service
        self.ticket_service = ticket_service
        self.vehicle_repository = vehicle_repo
        print("[CONTROLLER] EntryController initialized")

    def enter_vehicle(self, license_plate: str, vehicle_type: VehicleType) -> EntryResult:
        print(f"[CONTROLLER] Vehicle entry request - Plate: {license_plate} | Type: {vehicle_type.name}")
        try:
            vehicle = self.vehicle_repository.find_by_license_plate(license_plate)
            if not vehicle:
                vehicle = Vehicle(license_plate, vehicle_type)
                self.vehicle_repository.save(vehicle)

            slot = self.slot_service.allocate_slot(vehicle_type)
            ticket = self.ticket_service.generate_ticket(vehicle, slot)

            print(f"[CONTROLLER] Vehicle entry successful - Ticket: {ticket.id}")
            return EntryResult(True, ticket, "Entry successful")

        except Exception as e:
            print(f"[CONTROLLER] Vehicle entry failed: {str(e)}")
            return EntryResult(False, None, str(e))
