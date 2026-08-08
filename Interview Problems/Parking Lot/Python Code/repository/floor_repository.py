from typing import Dict, Optional
from domain.floor import Floor

class FloorRepository:
    def __init__(self):
        self._store: Dict[int, Floor] = {}

    def save(self, floor: Floor):
        self._store[floor.floor_number] = floor
        print(f"[REPO] Floor saved: {floor.floor_number}")

    def find_by_floor_number(self, floor_number: int) -> Optional[Floor]:
        return self._store.get(floor_number)

    def exists_by_floor_number(self, floor_number: int) -> bool:
        return floor_number in self._store

    def find_all(self) -> Dict[int, Floor]:
        return self._store
