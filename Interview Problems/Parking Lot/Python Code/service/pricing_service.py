import math
import uuid
from datetime import datetime
from typing import Optional
from domain.ticket import Ticket
from domain.vehicle import VehicleType
from repository.pricing_rule_repository import PricingRuleRepository

class PricingService:
    def __init__(self, pricing_repo: PricingRuleRepository):
        self.pricing_rule_repository = pricing_repo
        print("[SERVICE] PricingService initialized")

    def calculate_fee(self, ticket: Ticket, vehicle_type: VehicleType = VehicleType.CAR, exit_time: Optional[datetime] = None) -> float:
        if exit_time is None:
            exit_time = datetime.now()

        print(f"[SERVICE] Calculating fee for ticket: {ticket.id}")
        rule = self.pricing_rule_repository.find_by_vehicle_type(vehicle_type)
        if not rule:
            raise RuntimeError(f"No pricing rule found for vehicle type: {vehicle_type.name}")

        flat_fee = rule.flat_rate
        hourly_fee = self._calculate_hourly_fee(ticket, rule.rate_per_hour, exit_time)

        final_fee = min(flat_fee, hourly_fee)
        print(f"[SERVICE] Flat fee: ₹{flat_fee} | Hourly fee: ₹{hourly_fee} | Final fee: ₹{final_fee}")
        return final_fee

    def _calculate_hourly_fee(self, ticket: Ticket, rate_per_hour: float, exit_time: datetime) -> float:
        entry_time = ticket.entry_time
        duration = exit_time - entry_time
        minutes = int(duration.total_seconds() / 60)

        hours = math.ceil(minutes / 60.0)
        hours = max(hours, 1.0)

        fee = hours * rate_per_hour
        print(f"[SERVICE] Entry: {entry_time} | Exit: {exit_time} | Minutes parked: {minutes} | Hours billed: {hours} | Rate: ₹{rate_per_hour}/hr | Hourly fee: ₹{fee}")
        return fee
