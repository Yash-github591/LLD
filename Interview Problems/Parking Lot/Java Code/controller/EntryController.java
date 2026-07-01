package controller;

import domain.ParkingSlot;
import domain.Ticket;
import domain.Vehicle;
import repository.VehicleRepository;
import service.SlotService;
import service.TicketService;

import java.util.Optional;

public class EntryController {

    private final SlotService slotService;
    private final TicketService ticketService;
    private final VehicleRepository vehicleRepository;

    public EntryController(SlotService slotService,
                           TicketService ticketService,
                           VehicleRepository vehicleRepository) {
        this.slotService = slotService;
        this.ticketService = ticketService;
        this.vehicleRepository = vehicleRepository;
        System.out.println("[CONTROLLER] EntryController initialized");
    }

    public EntryResult enterVehicle(String licensePlate, Vehicle.VehicleType vehicleType) {
        System.out.println("[CONTROLLER] Vehicle entry request - Plate: " + licensePlate
                + " | Type: " + vehicleType);
        try {
            // Reuse existing vehicle record or register a new one
            Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate)
                    .orElseGet(() -> {
                        Vehicle v = new Vehicle(licensePlate, vehicleType);
                        vehicleRepository.save(v);
                        return v;
                    });

            // Allocate slot (throws if none available)
            ParkingSlot slot = slotService.allocateSlot(vehicleType);

            // Generate and persist ticket
            Ticket ticket = ticketService.generateTicket(vehicle, slot);

            System.out.println("[CONTROLLER] Vehicle entry successful - Ticket: " + ticket.getId());
            return new EntryResult(true, ticket, "Entry successful");

        } catch (Exception e) {
            System.out.println("[CONTROLLER] Vehicle entry failed: " + e.getMessage());
            return new EntryResult(false, null, e.getMessage());
        }
    }

    // ─── Inner DTO ────────────────────────────────────────────────────────────

    public static class EntryResult {
        private final boolean success;
        private final Ticket ticket;
        private final String message;

        public EntryResult(boolean success, Ticket ticket, String message) {
            this.success = success;
            this.ticket = ticket;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public Ticket getTicket() { return ticket; }
        public String getMessage() { return message; }

        @Override
        public String toString() {
            return "EntryResult{success=" + success
                    + ", ticketId=" + (ticket != null ? ticket.getId() : "null")
                    + ", message=" + message + "}";
        }
    }
}
