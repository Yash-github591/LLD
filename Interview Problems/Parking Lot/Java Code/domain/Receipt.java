package domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Receipt {

    public enum PaymentStatus {
        PENDING, SUCCESS, FAILED
    }

    private final UUID id;
    private final UUID ticketId;
    private final double totalFee;
    private final LocalDateTime exitTime;
    private PaymentStatus paymentStatus;

    public Receipt(UUID ticketId, double totalFee) {
        this.id = UUID.randomUUID();
        this.ticketId = ticketId;
        this.totalFee = totalFee;
        this.exitTime = LocalDateTime.now();
        this.paymentStatus = PaymentStatus.PENDING;
    }

    public void markAsPaid() {
        this.paymentStatus = PaymentStatus.SUCCESS;
    }

    public void markAsFailed() {
        this.paymentStatus = PaymentStatus.FAILED;
    }

    public UUID getId() { return id; }
    public UUID getTicketId() { return ticketId; }
    public double getTotalFee() { return totalFee; }
    public LocalDateTime getExitTime() { return exitTime; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }

    @Override
    public String toString() {
        return "Receipt{id=" + id + ", ticketId=" + ticketId
                + ", fee=" + totalFee + ", status=" + paymentStatus + "}";
    }
}
