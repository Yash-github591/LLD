package main;

import controller.AdminController;
import controller.EntryController;
import controller.ExitController;
import domain.Ticket;
import domain.Vehicle;
import repository.*;
import service.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("\n========== INITIALIZING PARKING LOT SYSTEM ==========\n");

        // ── Repositories ──────────────────────────────────────────────────────
        VehicleRepository vehicleRepo       = new VehicleRepository();
        SlotRepository slotRepo             = new SlotRepository();
        FloorRepository floorRepo           = new FloorRepository();
        TicketRepository ticketRepo         = new TicketRepository();
        PricingRuleRepository pricingRepo   = new PricingRuleRepository();
        PaymentRepository paymentRepo       = new PaymentRepository();

        // ── Services ──────────────────────────────────────────────────────────
        SlotService slotService             = new SlotService(slotRepo);
        TicketService ticketService         = new TicketService(ticketRepo);
        PricingService pricingService       = new PricingService(pricingRepo);
        PaymentService paymentService       = new PaymentService(paymentRepo);
        ReceiptService receiptService       = new ReceiptService();
        AdminService adminService           = new AdminService(floorRepo, slotRepo, pricingRepo);

        // ── Controllers ───────────────────────────────────────────────────────
        AdminController adminController     = new AdminController(adminService);
        EntryController entryController     = new EntryController(slotService, ticketService, vehicleRepo);
        ExitController exitController       = new ExitController(
                ticketService, pricingService, paymentService, receiptService, slotService);

        // ══════════════════════════════════════════════════════════════════════
        System.out.println("\n========== ADMIN SETUP ==========\n");

        // Add floors
        adminController.addFloor(1);
        adminController.addFloor(2);

        // Add slots
        adminController.addSlot(1, Vehicle.VehicleType.CAR);
        adminController.addSlot(1, Vehicle.VehicleType.CAR);
        adminController.addSlot(1, Vehicle.VehicleType.BIKE);
        adminController.addSlot(2, Vehicle.VehicleType.TRUCK);
        adminController.addSlot(2, Vehicle.VehicleType.EV);

        // Set up pricing rules
        adminController.updatePricing(Vehicle.VehicleType.CAR,   50.0, 200.0);
        adminController.updatePricing(Vehicle.VehicleType.BIKE,  20.0, 80.0);
        adminController.updatePricing(Vehicle.VehicleType.TRUCK, 100.0, 400.0);
        adminController.updatePricing(Vehicle.VehicleType.EV,    40.0, 150.0);

        adminController.viewParkingStatus();

        // ══════════════════════════════════════════════════════════════════════
        System.out.println("\n========== ENTRY FLOW ==========\n");

        EntryController.EntryResult entryResult = entryController.enterVehicle("MH12AB1234", Vehicle.VehicleType.CAR);
        System.out.println("Entry result: " + entryResult);

        if (!entryResult.isSuccess()) {
            System.out.println("Entry failed, exiting demo.");
            return;
        }

        Ticket ticket = entryResult.getTicket();
        System.out.println("Ticket issued: " + ticket);

        adminController.viewParkingStatus();

        // Simulate some time passing while the car is parked
        System.out.println("\n[DEMO] Simulating 2 seconds of parking time...\n");
        Thread.sleep(2000);

        // ══════════════════════════════════════════════════════════════════════
        System.out.println("\n========== EXIT FLOW ==========\n");

        ExitController.ExitResult exitResult = exitController.exitVehicle(ticket.getId());
        System.out.println("Exit result: " + exitResult);

        // Print the human-readable receipt
        System.out.println(exitController.generateReceiptText(ticket.getId()));

        adminController.viewParkingStatus();

        // ══════════════════════════════════════════════════════════════════════
        System.out.println("\n========== EDGE CASE: REUSE DEACTIVATED TICKET ==========\n");

        ExitController.ExitResult replayResult = exitController.exitVehicle(ticket.getId());
        System.out.println("Replay result (should fail): " + replayResult);
    }
}
