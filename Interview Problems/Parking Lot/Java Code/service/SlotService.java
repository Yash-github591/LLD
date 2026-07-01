package service;

import domain.ParkingSlot;
import domain.Vehicle;
import repository.SlotRepository;

import java.util.Optional;
import java.util.UUID;

public class SlotService {

    private final SlotRepository slotRepository;

    public SlotService(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
        System.out.println("[SERVICE] SlotService initialized");
    }

    /**
     * Finds and marks as occupied the first available slot matching the vehicle type.
     * NOTE: In production, the find + occupy must be a single atomic DB transaction
     * (SELECT FOR UPDATE) to prevent concurrent threads picking the same slot.
     */
    public ParkingSlot allocateSlot(Vehicle.VehicleType vehicleType) {
        System.out.println("[SERVICE] Allocating slot for vehicle type: " + vehicleType);

        Optional<ParkingSlot> availableSlot = slotRepository.findAvailableSlot(vehicleType);
        if (availableSlot.isEmpty()) {
            throw new IllegalStateException("No available slot for vehicle type: " + vehicleType);
        }

        ParkingSlot slot = availableSlot.get();
        slot.occupy();
        slotRepository.update(slot);

        System.out.println("[SERVICE] Slot allocated: " + slot.getId() + " on floor " + slot.getFloorNumber());
        return slot;
    }

    public void releaseSlot(UUID slotId) {
        System.out.println("[SERVICE] Releasing slot: " + slotId);

        ParkingSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Slot not found: " + slotId));

        slot.release();
        slotRepository.update(slot);
        System.out.println("[SERVICE] Slot released: " + slotId);
    }
}
