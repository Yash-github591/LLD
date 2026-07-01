package repository;

import domain.Floor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FloorRepository {

    // Keyed by floorNumber for fast lookup
    private final Map<Integer, Floor> store = new HashMap<>();

    public void save(Floor floor) {
        store.put(floor.getFloorNumber(), floor);
        System.out.println("[REPO] Floor saved: " + floor.getFloorNumber());
    }

    public Optional<Floor> findByFloorNumber(int floorNumber) {
        return Optional.ofNullable(store.get(floorNumber));
    }

    public boolean existsByFloorNumber(int floorNumber) {
        return store.containsKey(floorNumber);
    }

    public Map<Integer, Floor> findAll() {
        return store;
    }
}
