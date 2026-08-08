import uuid
from domain.vehicle import VehicleType
from domain.parking_slot import ParkingSlot
from repository.slot_repository import SlotRepository

class SlotService:
    def __init__(self, slot_repo: SlotRepository):
        self.slot_repository = slot_repo
        print("[SERVICE] SlotService initialized")

    def allocate_slot(self, vehicle_type: VehicleType) -> ParkingSlot:
        print(f"[SERVICE] Allocating slot for vehicle type: {vehicle_type.name}")
        available_slot = self.slot_repository.find_available_slot(vehicle_type)
        if not available_slot:
            raise RuntimeError(f"No available slot for vehicle type: {vehicle_type.name}")

        available_slot.occupy()
        self.slot_repository.update(available_slot)
        print(f"[SERVICE] Slot allocated: {available_slot.id} on floor {available_slot.floor_number}")
        return available_slot

    def release_slot(self, slot_id: uuid.UUID):
        print(f"[SERVICE] Releasing slot: {slot_id}")
        slot = self.slot_repository.find_by_id(slot_id)
        if not slot:
            raise ValueError(f"Slot not found: {slot_id}")

        slot.release()
        self.slot_repository.update(slot)
        print(f"[SERVICE] Slot released: {slot_id}")
