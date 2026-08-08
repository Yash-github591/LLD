import uuid
from typing import Dict, Optional
from domain.vehicle import Vehicle

class VehicleRepository:
    def __init__(self):
        self._store: Dict[uuid.UUID, Vehicle] = {}

    def save(self, vehicle: Vehicle):
        self._store[vehicle.id] = vehicle
        print(f"[REPO] Vehicle saved: {vehicle.id}")

    def find_by_id(self, vehicle_id: uuid.UUID) -> Optional[Vehicle]:
        return self._store.get(vehicle_id)

    def find_by_license_plate(self, license_plate: str) -> Optional[Vehicle]:
        for v in self._store.values():
            if v.license_plate.lower() == license_plate.lower():
                return v
        return None
