package repository;

import domain.PricingRule;
import domain.Vehicle;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PricingRuleRepository {

    // Keyed by VehicleType for direct O(1) lookup — one rule per vehicle type
    private final Map<Vehicle.VehicleType, PricingRule> store = new HashMap<>();

    public void save(PricingRule rule) {
        store.put(rule.getVehicleType(), rule);
        System.out.println("[REPO] PricingRule saved for: " + rule.getVehicleType());
    }

    public Optional<PricingRule> findByVehicleType(Vehicle.VehicleType vehicleType) {
        return Optional.ofNullable(store.get(vehicleType));
    }

    public void update(PricingRule rule) {
        if (!store.containsKey(rule.getVehicleType())) {
            throw new IllegalArgumentException("No pricing rule for vehicle type: " + rule.getVehicleType());
        }
        store.put(rule.getVehicleType(), rule);
        System.out.println("[REPO] PricingRule updated for: " + rule.getVehicleType());
    }
}
