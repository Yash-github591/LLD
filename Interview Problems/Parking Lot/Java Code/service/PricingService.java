package service;

import domain.PricingRule;
import domain.Ticket;
import domain.Vehicle;
import repository.PricingRuleRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class PricingService {

    private final PricingRuleRepository pricingRuleRepository;

    public PricingService(PricingRuleRepository pricingRuleRepository) {
        this.pricingRuleRepository = pricingRuleRepository;
        System.out.println("[SERVICE] PricingService initialized");
    }

    public double calculateFee(Ticket ticket) {
        return calculateFee(ticket, Vehicle.VehicleType.CAR, LocalDateTime.now());
    }

    /**
     * Full version: accepts vehicleType and a caller-supplied exitTime.
     * Prefer this overload in production — passing exitTime in makes the method
     * testable and avoids clock-skew issues from calling LocalDateTime.now() internally.
     */
    public double calculateFee(Ticket ticket, Vehicle.VehicleType vehicleType, LocalDateTime exitTime) {
        System.out.println("[SERVICE] Calculating fee for ticket: " + ticket.getId());

        Optional<PricingRule> rule = pricingRuleRepository.findByVehicleType(vehicleType);
        if (rule.isEmpty()) {
            throw new IllegalStateException("No pricing rule found for vehicle type: " + vehicleType);
        }

        PricingRule pricingRule = rule.get();

        double flatFee = pricingRule.getFlatRate();
        double hourlyFee = calculateHourlyFee(ticket, pricingRule.getRatePerHour(), exitTime);

        // Business rule: always charge the lesser of flat vs hourly
        double finalFee = Math.min(flatFee, hourlyFee);

        System.out.println("[SERVICE] Flat fee: ₹" + flatFee
                + " | Hourly fee: ₹" + hourlyFee
                + " | Final fee: ₹" + finalFee);

        return finalFee;
    }

    /**
     * Calculates the hourly fee for a parking session.
     *
     * Rules:
     *  - Duration is measured in minutes from entryTime to exitTime.
     *  - Partial hours are rounded UP (61 min = 2 hours billed).
     *  - Minimum charge is 1 hour, regardless of how short the stay was.
     *
     * @param ticket      the active ticket with entryTime
     * @param ratePerHour the hourly rate from the pricing rule
     * @param exitTime    the exit timestamp, passed by the caller to avoid clock-skew
     * @return            the computed hourly fee
     */
    private double calculateHourlyFee(Ticket ticket, double ratePerHour, LocalDateTime exitTime) {
        LocalDateTime entryTime = ticket.getEntryTime();

        long minutes = ChronoUnit.MINUTES.between(entryTime, exitTime);

        // Math.ceil on (minutes / 60.0) rounds any partial hour up to a full hour
        // The .0 is critical: without it, Java does integer division before ceil gets to round
        double hours = Math.ceil(minutes / 60.0);

        // Minimum charge of 1 hour even for very short stays
        hours = Math.max(hours, 1.0);

        double fee = hours * ratePerHour;

        System.out.println("[SERVICE] Entry: " + entryTime
                + " | Exit: " + exitTime
                + " | Minutes parked: " + minutes
                + " | Hours billed: " + hours
                + " | Rate: ₹" + ratePerHour + "/hr"
                + " | Hourly fee: ₹" + fee);

        return fee;
    }
}
