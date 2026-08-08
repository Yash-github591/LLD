from typing import Optional, Dict
from domain.floor import Floor
from domain.parking_slot import ParkingSlot
from domain.vehicle import VehicleType
from domain.pricing_rule import PricingRule, RuleType
from repository.floor_repository import FloorRepository
from repository.slot_repository import SlotRepository
from repository.pricing_rule_repository import PricingRuleRepository

class AdminService:
    def __init__(self, floor_repo: FloorRepository, slot_repo: SlotRepository, pricing_repo: PricingRuleRepository):
        self.floor_repository = floor_repo
        self.slot_repository = slot_repo
        self.pricing_rule_repository = pricing_repo
        print("[SERVICE] AdminService initialized")

    def add_floor(self, floor_number: int) -> Floor:
        print(f"[ADMIN] Adding floor: {floor_number}")
        if self.floor_repository.exists_by_floor_number(floor_number):
            raise ValueError(f"Floor {floor_number} already exists")
        floor = Floor(floor_number)
        self.floor_repository.save(floor)
        print(f"[ADMIN] Floor {floor_number} added successfully")
        return floor

    def add_slot(self, floor_number: int, slot_type: VehicleType) -> ParkingSlot:
        print(f"[ADMIN] Adding slot of type {slot_type.name} to floor {floor_number}")
        floor = self.floor_repository.find_by_floor_number(floor_number)
        if not floor:
            raise ValueError(f"Floor not found: {floor_number}")

        slot = ParkingSlot(slot_type, floor_number)
        self.slot_repository.save(slot)
        floor.add_slot(slot)

        print(f"[ADMIN] Slot added: {slot.id} on floor {floor_number}")
        return slot

    def update_pricing(self, vehicle_type: VehicleType, rate_per_hour: float, flat_rate: float):
        print(f"[ADMIN] Updating pricing for: {vehicle_type.name}")
        existing = self.pricing_rule_repository.find_by_vehicle_type(vehicle_type)
        if existing:
            existing.update_hourly_rate(rate_per_hour)
            existing.update_flat_rate(flat_rate)
            self.pricing_rule_repository.update(existing)
        else:
            rule = PricingRule(vehicle_type, rate_per_hour, flat_rate, RuleType.HOURLY)
            self.pricing_rule_repository.save(rule)
        print(f"[ADMIN] Pricing updated for {vehicle_type.name} | Hourly: ₹{rate_per_hour} | Flat: ₹{flat_rate}")

    def update_flat_pricing(self, vehicle_type: VehicleType, flat_rate: float):
        print(f"[ADMIN] Updating flat rate for: {vehicle_type.name} to ₹{flat_rate}")
        rule = self.pricing_rule_repository.find_by_vehicle_type(vehicle_type)
        if not rule:
            raise ValueError(f"No pricing rule for: {vehicle_type.name}")
        rule.update_flat_rate(flat_rate)
        self.pricing_rule_repository.update(rule)

    def update_hourly_pricing(self, vehicle_type: VehicleType, rate_per_hour: float):
        print(f"[ADMIN] Updating hourly rate for: {vehicle_type.name} to ₹{rate_per_hour}")
        rule = self.pricing_rule_repository.find_by_vehicle_type(vehicle_type)
        if not rule:
            raise ValueError(f"No pricing rule for: {vehicle_type.name}")
        rule.update_hourly_rate(rate_per_hour)
        self.pricing_rule_repository.update(rule)

    def view_parking_status(self):
        print("\n========== PARKING STATUS ==========")
        floors = self.floor_repository.find_all()
        if not floors:
            print("No floors configured.")
        for floor_num, floor in floors.items():
            slots = floor.slots
            occupied = sum(1 for s in slots if s.is_occupied)
            print(f"Floor {floor.floor_number} | Total slots: {len(slots)} | Occupied: {occupied} | Available: {len(slots) - occupied}")
        print("=====================================\n")
