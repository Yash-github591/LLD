from typing import Dict, Optional
from domain.vehicle import VehicleType
from domain.pricing_rule import PricingRule

class PricingRuleRepository:
    def __init__(self):
        self._store: Dict[VehicleType, PricingRule] = {}

    def save(self, rule: PricingRule):
        self._store[rule.vehicle_type] = rule
        print(f"[REPO] PricingRule saved for: {rule.vehicle_type.name}")

    def find_by_vehicle_type(self, vehicle_type: VehicleType) -> Optional[PricingRule]:
        return self._store.get(vehicle_type)

    def update(self, rule: PricingRule):
        if rule.vehicle_type not in self._store:
            raise ValueError(f"No pricing rule for vehicle type: {rule.vehicle_type.name}")
        self._store[rule.vehicle_type] = rule
        print(f"[REPO] PricingRule updated for: {rule.vehicle_type.name}")
