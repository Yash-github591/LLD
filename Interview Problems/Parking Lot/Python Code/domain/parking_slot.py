import uuid
from domain.vehicle import VehicleType

class ParkingSlot:
    def __init__(self, slot_type: VehicleType, floor_number: int):
        self.id = uuid.uuid4()
        self.slot_type = slot_type
        self.floor_number = floor_number
        self.is_occupied = False

    def occupy(self):
        if self.is_occupied:
            raise RuntimeError(f"Slot {self.id} is already occupied")
        self.is_occupied = True

    def release(self):
        if not self.is_occupied:
            raise RuntimeError(f"Slot {self.id} is not occupied")
        self.is_occupied = False

    def __repr__(self):
        return f"ParkingSlot(id={self.id}, type={self.slot_type.name}, floor={self.floor_number}, occupied={self.is_occupied})"
