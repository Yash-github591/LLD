package service;

import domain.Floor;
import domain.ParkingSlot;
import domain.PricingRule;
import domain.Vehicle;
import repository.FloorRepository;
import repository.PricingRuleRepository;
import repository.SlotRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AdminService {

    private final FloorRepository floorRepository;
    private final SlotRepository slotRepository;
    private final PricingRuleRepository pricingRuleRepository;

    public AdminService(FloorRepository floorRepository,
                        SlotRepository slotRepository,
                        PricingRuleRepository pricingRuleRepository) {
        this.floorRepository = floorRepository;
        this.slotRepository = slotRepository;
        this.pricingRuleRepository = pricingRuleRepository;
        System.out.println("[SERVICE] AdminService initialized");
    }

    public Floor addFloor(int floorNumber) {
        System.out.println("[ADMIN] Adding floor: " + floorNumber);
        if (floorRepository.existsByFloorNumber(floorNumber)) {
            throw new IllegalArgumentException("Floor " + floorNumber + " already exists");
        }
        Floor floor = new Floor(floorNumber);
        floorRepository.save(floor);
        System.out.println("[ADMIN] Floor " + floorNumber + " added successfully");
        return floor;
    }

    public ParkingSlot addSlot(int floorNumber, Vehicle.VehicleType slotType) {
        System.out.println("[ADMIN] Adding slot of type " + slotType + " to floor " + floorNumber);

        Floor floor = floorRepository.findByFloorNumber(floorNumber)
                .orElseThrow(() -> new IllegalArgumentException("Floor not found: " + floorNumber));

        ParkingSlot slot = new ParkingSlot(slotType, floorNumber);
        slotRepository.save(slot);
        floor.addSlot(slot);

        System.out.println("[ADMIN] Slot added: " + slot.getId() + " on floor " + floorNumber);
        return slot;
    }

    public void updatePricing(Vehicle.VehicleType vehicleType, double ratePerHour, double flatRate) {
        System.out.println("[ADMIN] Updating pricing for: " + vehicleType);
        Optional<PricingRule> existing = pricingRuleRepository.findByVehicleType(vehicleType);
        if (existing.isPresent()) {
            existing.get().updateHourlyRate(ratePerHour);
            existing.get().updateFlatRate(flatRate);
            pricingRuleRepository.update(existing.get());
        } else {
            PricingRule rule = new PricingRule(vehicleType, ratePerHour, flatRate, PricingRule.RuleType.HOURLY);
            pricingRuleRepository.save(rule);
        }
        System.out.println("[ADMIN] Pricing updated for " + vehicleType
                + " | Hourly: ₹" + ratePerHour + " | Flat: ₹" + flatRate);
    }

    public void updateFlatPricing(Vehicle.VehicleType vehicleType, double flatRate) {
        System.out.println("[ADMIN] Updating flat rate for: " + vehicleType + " to ₹" + flatRate);
        PricingRule rule = pricingRuleRepository.findByVehicleType(vehicleType)
                .orElseThrow(() -> new IllegalArgumentException("No pricing rule for: " + vehicleType));
        rule.updateFlatRate(flatRate);
        pricingRuleRepository.update(rule);
    }

    public void updateHourlyPricing(Vehicle.VehicleType vehicleType, double ratePerHour) {
        System.out.println("[ADMIN] Updating hourly rate for: " + vehicleType + " to ₹" + ratePerHour);
        PricingRule rule = pricingRuleRepository.findByVehicleType(vehicleType)
                .orElseThrow(() -> new IllegalArgumentException("No pricing rule for: " + vehicleType));
        rule.updateHourlyRate(ratePerHour);
        pricingRuleRepository.update(rule);
    }

    public void viewParkingStatus() {
        System.out.println("\n========== PARKING STATUS ==========");
        Map<Integer, Floor> floors = floorRepository.findAll();
        if (floors.isEmpty()) {
            System.out.println("No floors configured.");
        }
        for (Map.Entry<Integer, Floor> entry : floors.entrySet()) {
            Floor floor = entry.getValue();
            List<ParkingSlot> slots = floor.getSlots();
            long occupied = slots.stream().filter(ParkingSlot::isOccupied).count();
            System.out.println("Floor " + floor.getFloorNumber()
                    + " | Total slots: " + slots.size()
                    + " | Occupied: " + occupied
                    + " | Available: " + (slots.size() - occupied));
        }
        System.out.println("=====================================\n");
    }
}
