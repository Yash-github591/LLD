package controller;

import domain.Floor;
import domain.ParkingSlot;
import domain.Vehicle;
import service.AdminService;

public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
        System.out.println("[CONTROLLER] AdminController initialized");
    }

    public Floor addFloor(int floorNumber) {
        System.out.println("[CONTROLLER] Admin request: add floor " + floorNumber);
        return adminService.addFloor(floorNumber);
    }

    public ParkingSlot addSlot(int floorNumber, Vehicle.VehicleType slotType) {
        System.out.println("[CONTROLLER] Admin request: add slot of type " + slotType + " to floor " + floorNumber);
        return adminService.addSlot(floorNumber, slotType);
    }

    public void updatePricing(Vehicle.VehicleType vehicleType, double ratePerHour, double flatRate) {
        System.out.println("[CONTROLLER] Admin request: update pricing for " + vehicleType);
        adminService.updatePricing(vehicleType, ratePerHour, flatRate);
    }

    public void updateFlatPricing(Vehicle.VehicleType vehicleType, double flatRate) {
        System.out.println("[CONTROLLER] Admin request: update flat rate for " + vehicleType);
        adminService.updateFlatPricing(vehicleType, flatRate);
    }

    public void updateHourlyPricing(Vehicle.VehicleType vehicleType, double ratePerHour) {
        System.out.println("[CONTROLLER] Admin request: update hourly rate for " + vehicleType);
        adminService.updateHourlyPricing(vehicleType, ratePerHour);
    }

    public void viewParkingStatus() {
        System.out.println("[CONTROLLER] Admin request: view parking status");
        adminService.viewParkingStatus();
    }
    
    public void initializeParkingLot() {
        System.out.println("[ADMIN] Initializing parking lot...");

        // Floors
        addFloor(1);
        addFloor(2);

        // Slots
        addSlot(1, Vehicle.VehicleType.CAR);
        addSlot(1, Vehicle.VehicleType.CAR);
        addSlot(1, Vehicle.VehicleType.BIKE);
        addSlot(2, Vehicle.VehicleType.TRUCK);
        addSlot(2, Vehicle.VehicleType.EV);

        // Pricing rules
        updatePricing(Vehicle.VehicleType.CAR,   50.0, 200.0);
        updatePricing(Vehicle.VehicleType.BIKE,  20.0,  80.0);
        updatePricing(Vehicle.VehicleType.TRUCK, 100.0, 400.0);
        updatePricing(Vehicle.VehicleType.EV,    40.0, 150.0);

        System.out.println("[ADMIN] Parking lot initialized successfully");
    }

    public void getParkingStatus() {
        adminService.viewParkingStatus();
    }
}
