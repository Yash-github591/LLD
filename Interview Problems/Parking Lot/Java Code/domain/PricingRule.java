package domain;

import java.util.UUID;

public class PricingRule {

    public enum RuleType {
        FLAT, HOURLY
    }

    private final UUID id;
    private final Vehicle.VehicleType vehicleType;
    private double ratePerHour;
    private double flatRate;
    private RuleType ruleType;

    public PricingRule(Vehicle.VehicleType vehicleType, double ratePerHour, double flatRate, RuleType ruleType) {
        this.id = UUID.randomUUID();
        this.vehicleType = vehicleType;
        this.ratePerHour = ratePerHour;
        this.flatRate = flatRate;
        this.ruleType = ruleType;
    }

    public void updateFlatRate(double flatRate) { this.flatRate = flatRate; }
    public void updateHourlyRate(double ratePerHour) { this.ratePerHour = ratePerHour; }
    public void updateRuleType(RuleType ruleType) { this.ruleType = ruleType; }

    public UUID getId() { return id; }
    public Vehicle.VehicleType getVehicleType() { return vehicleType; }
    public double getRatePerHour() { return ratePerHour; }
    public double getFlatRate() { return flatRate; }
    public RuleType getRuleType() { return ruleType; }

    @Override
    public String toString() {
        return "PricingRule{vehicleType=" + vehicleType + ", flatRate=" + flatRate
                + ", ratePerHour=" + ratePerHour + ", ruleType=" + ruleType + "}";
    }
}
