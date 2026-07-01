package controller;

import domain.Receipt;
import domain.Ticket;
import service.PaymentService;
import service.PricingService;
import service.ReceiptService;
import service.SlotService;
import service.TicketService;

import java.util.Optional;
import java.util.UUID;

public class ExitController {

    private final TicketService ticketService;
    private final PricingService pricingService;
    private final PaymentService paymentService;
    private final ReceiptService receiptService;
    private final SlotService slotService;

    public ExitController(TicketService ticketService,
                          PricingService pricingService,
                          PaymentService paymentService,
                          ReceiptService receiptService,
                          SlotService slotService) {
        this.ticketService = ticketService;
        this.pricingService = pricingService;
        this.paymentService = paymentService;
        this.receiptService = receiptService;
        this.slotService = slotService;
        System.out.println("[CONTROLLER] ExitController initialized");
    }

    public ExitResult exitVehicle(UUID ticketId) {
        System.out.println("[CONTROLLER] Vehicle exit request - Ticket: " + ticketId);

        try {
            // 1. Retrieve ticket
            Optional<Ticket> ticketOpt = ticketService.getTicket(ticketId);
            if (ticketOpt.isEmpty()) {
                return new ExitResult(false, null, 0.0, "Ticket not found");
            }

            Ticket ticket = ticketOpt.get();

            // 2. Guard: ticket must still be active
            if (!ticket.isActive()) {
                return new ExitResult(false, null, 0.0, "Ticket is not active");
            }

            // 3. Calculate fee
            double fee = pricingService.calculateFee(ticket);
            System.out.println("[CONTROLLER] Fee calculated: ₹" + fee);

            // 4. Process payment with retry (max 3 attempts)
            // Slot is intentionally NOT released until payment succeeds
            boolean paymentSuccess = paymentService.processPaymentWithRetry(ticketId, fee, 3);
            if (!paymentSuccess) {
                return new ExitResult(false, null, fee, "Payment failed after retries");
            }

            // 5. Generate receipt and mark as paid
            Receipt receipt = receiptService.generateReceipt(ticket, fee);
            receiptService.markReceiptAsPaid(receipt);

            // 6. Release slot — only after confirmed payment
            slotService.releaseSlot(ticket.getSlotId());

            // 7. Deactivate ticket to prevent reuse
            ticketService.deactivateTicket(ticketId);

            System.out.println("[CONTROLLER] Vehicle exit successful - Receipt: " + receipt.getId());
            return new ExitResult(true, receipt.getId(), fee, "Exit successful");

        } catch (Exception e) {
            System.out.println("[CONTROLLER] Vehicle exit failed: " + e.getMessage());
            return new ExitResult(false, null, 0.0, e.getMessage());
        }
    }

    public String generateReceiptText(UUID ticketId) {
        Optional<Ticket> ticketOpt = ticketService.getTicket(ticketId);
        if (ticketOpt.isEmpty()) return "Ticket not found: " + ticketId;

        Ticket ticket = ticketOpt.get();
        double fee = pricingService.calculateFee(ticket);
        Receipt receipt = receiptService.generateReceipt(ticket, fee);
        return receiptService.generateReceiptText(receipt, ticket);
    }

    // ─── Inner DTO ────────────────────────────────────────────────────────────

    public static class ExitResult {
        private final boolean success;
        private final UUID receiptId;
        private final double fee;
        private final String message;

        public ExitResult(boolean success, UUID receiptId, double fee, String message) {
            this.success = success;
            this.receiptId = receiptId;
            this.fee = fee;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public UUID getReceiptId() { return receiptId; }
        public double getFee() { return fee; }
        public String getMessage() { return message; }

        @Override
        public String toString() {
            return "ExitResult{success=" + success
                    + ", receiptId=" + receiptId
                    + ", fee=₹" + fee
                    + ", message=" + message + "}";
        }
    }
}
