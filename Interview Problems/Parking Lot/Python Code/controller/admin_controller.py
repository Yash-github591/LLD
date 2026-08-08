from domain.vehicle import VehicleType
from service.admin_service import AdminService

class AdminController:
    def __init__(self, admin_service: AdminService):
        self.admin_service = admin_service
        print("[CONTROLLER] AdminController initialized")

    def add_floor(self, floor_number: int):
        print(f"[CONTROLLER] Admin request: add floor {floor_number}")
        return self.admin_service.add_floor(floor_number)

    def add_slot(self, floor_number: int, slot_type: VehicleType):
        print(f"[CONTROLLER] Admin request: add slot of type {slot_type.name} to floor {floor_number}")
        return self.admin_service.add_slot(floor_number, slot_type)

    def update_pricing(self, vehicle_type: VehicleType, rate_per_hour: float, flat_rate: float):
        print(f"[CONTROLLER] Admin request: update pricing for {vehicle_type.name}")
        self.admin_service.update_pricing(vehicle_type, rate_per_hour, flat_rate)

    def update_flat_pricing(self, vehicle_type: VehicleType, flat_rate: float):
        print(f"[CONTROLLER] Admin request: update flat rate for {vehicle_type.name}")
        self.admin_service.update_flat_pricing(vehicle_type, flat_rate)

    def update_hourly_pricing(self, vehicle_type: VehicleType, rate_per_hour: float):
        print(f"[CONTROLLER] Admin request: update hourly rate for {vehicle_type.name}")
        self.admin_service.update_hourly_pricing(vehicle_type, rate_per_hour)

    def view_parking_status(self):
        print("[CONTROLLER] Admin request: view parking status")
        self.admin_service.view_parking_status()

    def initialize_parking_lot(self):
        print("[ADMIN] Initializing parking lot...")
        self.add_floor(1)
        self.add_floor(2)

        self.add_slot(1, VehicleType.CAR)
        self.add_slot(1, VehicleType.CAR)
        self.add_slot(1, VehicleType.BIKE)
        self.add_slot(2, VehicleType.TRUCK)
        self.add_slot(2, VehicleType.EV)

        self.update_pricing(VehicleType.CAR, 50.0, 200.0)
        self.update_pricing(VehicleType.BIKE, 20.0, 80.0)
        self.update_pricing(VehicleType.TRUCK, 100.0, 400.0)
        self.update_pricing(VehicleType.EV, 40.0, 150.0)
        print("[ADMIN] Parking lot initialized successfully")
