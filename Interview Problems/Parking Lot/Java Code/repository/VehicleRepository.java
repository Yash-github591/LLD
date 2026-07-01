package repository;

import domain.Vehicle;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class VehicleRepository {

    private final Map<UUID, Vehicle> store = new HashMap<>();

    public void save(Vehicle vehicle) {
        store.put(vehicle.getId(), vehicle);
        System.out.println("[REPO] Vehicle saved: " + vehicle.getId());
    }

    public Optional<Vehicle> findById(UUID vehicleId) {
        return Optional.ofNullable(store.get(vehicleId));
    }

    public Optional<Vehicle> findByLicensePlate(String licensePlate) {
        return store.values().stream()
                .filter(v -> v.getLicensePlate().equalsIgnoreCase(licensePlate))
                .findFirst();
    }
}
