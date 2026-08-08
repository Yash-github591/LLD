import time
from domain.vehicle import VehicleType
from repository.vehicle_repository import VehicleRepository
from repository.slot_repository import SlotRepository
from repository.floor_repository import FloorRepository
from repository.ticket_repository import TicketRepository
from repository.pricing_rule_repository import PricingRuleRepository
from repository.payment_repository import PaymentRepository

from service.slot_service import SlotService
from service.ticket_service import TicketService
from service.pricing_service import PricingService
from service.payment_service import PaymentService
from service.receipt_service import ReceiptService
from service.admin_service import AdminService

from controller.admin_controller import AdminController
from controller.entry_controller import EntryController
from controller.exit_controller import ExitController

def main():
    print("\n========== INITIALIZING PARKING LOT SYSTEM ==========\n")

    # Repositories
    vehicle_repo = VehicleRepository()
    slot_repo = SlotRepository()
    floor_repo = FloorRepository()
    ticket_repo = TicketRepository()
    pricing_repo = PricingRuleRepository()
    payment_repo = PaymentRepository()

    # Services
    slot_service = SlotService(slot_repo)
    ticket_service = TicketService(ticket_repo)
    pricing_service = PricingService(pricing_repo)
    payment_service = PaymentService(payment_repo)
    receipt_service = ReceiptService()
    admin_service = AdminService(floor_repo, slot_repo, pricing_repo)

    # Controllers
    admin_controller = AdminController(admin_service)
    entry_controller = EntryController(slot_service, ticket_service, vehicle_repo)
    exit_controller = ExitController(ticket_service, pricing_service, payment_service, receipt_service, slot_service)

    print("\n========== ADMIN SETUP ==========\n")
    admin_controller.add_floor(1)
    admin_controller.add_floor(2)

    admin_controller.add_slot(1, VehicleType.CAR)
    admin_controller.add_slot(1, VehicleType.CAR)
    admin_controller.add_slot(1, VehicleType.BIKE)
    admin_controller.add_slot(2, VehicleType.TRUCK)
    admin_controller.add_slot(2, VehicleType.EV)

    admin_controller.update_pricing(VehicleType.CAR, 50.0, 200.0)
    admin_controller.update_pricing(VehicleType.BIKE, 20.0, 80.0)
    admin_controller.update_pricing(VehicleType.TRUCK, 100.0, 400.0)
    admin_controller.update_pricing(VehicleType.EV, 40.0, 150.0)

    admin_controller.view_parking_status()

    print("\n========== ENTRY FLOW ==========\n")
    entry_result = entry_controller.enter_vehicle("MH12AB1234", VehicleType.CAR)
    print("Entry result:", entry_result)

    if not entry_result.success:
        print("Entry failed, exiting demo.")
        return

    ticket = entry_result.ticket
    print("Ticket issued:", ticket)

    admin_controller.view_parking_status()

    print("\n[DEMO] Simulating 2 seconds of parking time...\n")
    time.sleep(2)

    print("\n========== EXIT FLOW ==========\n")
    exit_result = exit_controller.exit_vehicle(ticket.id)
    print("Exit result:", exit_result)

    print(exit_controller.generate_receipt_text(ticket.id))

    admin_controller.view_parking_status()

    print("\n========== EDGE CASE: REUSE DEACTIVATED TICKET ==========
")
    replay_result = exit_controller.exit_vehicle(ticket.id)
    print("Replay result (should fail):", replay_result)

if __name__ == "__main__":
    main()
