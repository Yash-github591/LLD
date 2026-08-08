import uuid
from typing import Dict, List, Optional
from domain.vehicle import VehicleType
from domain.parking_slot import ParkingSlot

class SlotRepository:
    def __init__(self):
        self._store: Dict[uuid.UUID, ParkingSlot] = {}

    def save(self, slot: ParkingSlot):
        self._store[slot.id] = slot
        print(f"[REPO] ParkingSlot saved: {slot.id}")

    def find_by_id(self, slot_id: uuid.UUID) -> Optional[ParkingSlot]:
        return self._store.get(slot_id)

    def find_available_slot(self, vehicle_type: VehicleType) -> Optional[ParkingSlot]:
        for slot in self._store.values():
            if slot.slot_type == vehicle_type and not slot.is_occupied:
                return slot
        return None

    def find_all_occupied(self) -> List[ParkingSlot]:
        return [slot for slot in self._store.values() if slot.is_occupied]

    def update(self, slot: ParkingSlot):
        if slot.id not in self._store:
            raise ValueError(f"Slot not found: {slot.id}")
        self._store[slot.id] = slot
