package domain;

import java.util.UUID;

public class Payment {

    public enum PaymentStatus {
        PENDING, SUCCESS, FAILED
    }

    public enum PaymentGateway {
        RAZORPAY, STRIPE
    }

    private final UUID id;
    private final UUID ticketId;
    private final double amount;
    private final PaymentGateway gateway;
    private PaymentStatus status;

    public Payment(UUID ticketId, double amount, PaymentGateway gateway) {
        this.id = UUID.randomUUID();
        this.ticketId = ticketId;
        this.amount = amount;
        this.gateway = gateway;
        this.status = PaymentStatus.PENDING;
    }

    public void markAsSuccess() { this.status = PaymentStatus.SUCCESS; }
    public void markAsFailed() { this.status = PaymentStatus.FAILED; }

    public UUID getId() { return id; }
    public UUID getTicketId() { return ticketId; }
    public double getAmount() { return amount; }
    public PaymentGateway getGateway() { return gateway; }
    public PaymentStatus getStatus() { return status; }

    @Override
    public String toString() {
        return "Payment{id=" + id + ", ticketId=" + ticketId
                + ", amount=" + amount + ", gateway=" + gateway + ", status=" + status + "}";
    }
}
