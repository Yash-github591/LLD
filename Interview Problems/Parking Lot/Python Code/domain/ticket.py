import uuid
from datetime import datetime

class Ticket:
    def __init__(self, vehicle_id: uuid.UUID, slot_id: uuid.UUID):
        self.id = uuid.uuid4()
        self.vehicle_id = vehicle_id
        self.slot_id = slot_id
        self.entry_time = datetime.now()
        self.is_active = True

    def deactivate(self):
        if not self.is_active:
            raise RuntimeError(f"Ticket {self.id} is already inactive")
        self.is_active = False

    def __repr__(self):
        return f"Ticket(id={self.id}, vehicle_id={self.vehicle_id}, slot_id={self.slot_id}, entry={self.entry_time}, active={self.is_active})"
