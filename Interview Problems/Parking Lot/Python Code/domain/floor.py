import uuid
from typing import List
from domain.vehicle import VehicleType
from domain.parking_slot import ParkingSlot

class Floor:
    def __init__(self, floor_number: int):
        self.id = uuid.uuid4()
        self.floor_number = floor_number
        self.slots: List[ParkingSlot] = []

    def add_slot(self, slot: ParkingSlot):
        self.slots.append(slot)

    def get_available_slots(self, vehicle_type: VehicleType) -> List[ParkingSlot]:
        return [slot for slot in self.slots if slot.slot_type == vehicle_type and not slot.is_occupied]

    def __repr__(self):
        return f"Floor(id={self.id}, number={self.floor_number}, slots={len(self.slots)})"
