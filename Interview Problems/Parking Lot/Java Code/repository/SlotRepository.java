package repository;

import domain.ParkingSlot;
import domain.Vehicle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class SlotRepository {

    private final Map<UUID, ParkingSlot> store = new HashMap<>();

    public void save(ParkingSlot slot) {
        store.put(slot.getId(), slot);
        System.out.println("[REPO] ParkingSlot saved: " + slot.getId());
    }

    public Optional<ParkingSlot> findById(UUID slotId) {
        return Optional.ofNullable(store.get(slotId));
    }

    /**
     * Finds the first available (unoccupied) slot matching the given vehicle type.
     * In production this must be wrapped in a DB transaction with SELECT FOR UPDATE
     * to prevent two concurrent threads allocating the same slot.
     */
    public Optional<ParkingSlot> findAvailableSlot(Vehicle.VehicleType vehicleType) {
        return store.values().stream()
                .filter(s -> s.getSlotType() == vehicleType && !s.isOccupied())
                .findFirst();
    }

    public List<ParkingSlot> findAllOccupied() {
        return store.values().stream()
                .filter(ParkingSlot::isOccupied)
                .collect(Collectors.toList());
    }

    public void update(ParkingSlot slot) {
        if (!store.containsKey(slot.getId())) {
            throw new IllegalArgumentException("Slot not found: " + slot.getId());
        }
        store.put(slot.getId(), slot);
    }
}
