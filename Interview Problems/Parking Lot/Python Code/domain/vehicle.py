import uuid
from enum import Enum, auto

class VehicleType(Enum):
    BIKE = auto()
    CAR = auto()
    TRUCK = auto()
    EV = auto()

class Vehicle:
    def __init__(self, license_plate: str, vehicle_type: VehicleType):
        self.id = uuid.uuid4()
        self.license_plate = license_plate
        self.vehicle_type = vehicle_type

    def __repr__(self):
        return f"Vehicle(id={self.id}, plate='{self.license_plate}', type={self.vehicle_type.name})"
