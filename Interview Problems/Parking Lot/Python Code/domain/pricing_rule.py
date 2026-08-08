import uuid
from enum import Enum, auto
from domain.vehicle import VehicleType

class RuleType(Enum):
    FLAT = auto()
    HOURLY = auto()

class PricingRule:
    def __init__(self, vehicle_type: VehicleType, rate_per_hour: float, flat_rate: float, rule_type: RuleType):
        self.id = uuid.uuid4()
        self.vehicle_type = vehicle_type
        self.rate_per_hour = rate_per_hour
        self.flat_rate = flat_rate
        self.rule_type = rule_type

    def update_flat_rate(self, flat_rate: float):
        self.flat_rate = flat_rate

    def update_hourly_rate(self, rate_per_hour: float):
        self.rate_per_hour = rate_per_hour

    def update_rule_type(self, rule_type: RuleType):
        self.rule_type = rule_type

    def __repr__(self):
        return f"PricingRule(vehicle_type={self.vehicle_type.name}, flat_rate={self.flat_rate}, rate_per_hour={self.rate_per_hour}, rule_type={self.rule_type.name})"
