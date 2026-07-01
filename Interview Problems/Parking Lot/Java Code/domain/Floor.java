package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Floor {

    private final UUID id;
    private final int floorNumber;
    private final List<ParkingSlot> slots;

    public Floor(int floorNumber) {
        this.id = UUID.randomUUID();
        this.floorNumber = floorNumber;
        this.slots = new ArrayList<>();
    }

    public void addSlot(ParkingSlot slot) {
        slots.add(slot);
    }

    public List<ParkingSlot> getAvailableSlots(Vehicle.VehicleType type) {
        List<ParkingSlot> available = new ArrayList<>();
        for (ParkingSlot slot : slots) {
            if (slot.getSlotType() == type && !slot.isOccupied()) {
                available.add(slot);
            }
        }
        return available;
    }

    public UUID getId() { return id; }
    public int getFloorNumber() { return floorNumber; }
    public List<ParkingSlot> getSlots() { return slots; }

    @Override
    public String toString() {
        return "Floor{id=" + id + ", number=" + floorNumber + ", slots=" + slots.size() + "}";
    }
}
