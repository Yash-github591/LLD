package service;

import domain.Receipt;
import domain.Ticket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ReceiptService {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public ReceiptService() {
        System.out.println("[SERVICE] ReceiptService initialized");
    }

    public Receipt generateReceipt(Ticket ticket, double fee) {
        System.out.println("[SERVICE] Generating receipt for ticket: " + ticket.getId());
        Receipt receipt = new Receipt(ticket.getId(), fee);
        System.out.println("[SERVICE] Receipt generated: " + receipt.getId() + " with fee: ₹" + fee);
        return receipt;
    }

    public void markReceiptAsPaid(Receipt receipt) {
        System.out.println("[SERVICE] Marking receipt as paid: " + receipt.getId());
        receipt.markAsPaid();
        System.out.println("[SERVICE] Receipt marked as paid successfully");
    }

    public String generateReceiptText(Receipt receipt, Ticket ticket) {
        System.out.println("[SERVICE] Generating receipt text for: " + receipt.getId());

        // Use the receipt's own exitTime (set at construction) for consistency
        LocalDateTime exitTime = receipt.getExitTime();
        long minutesParked = ChronoUnit.MINUTES.between(ticket.getEntryTime(), exitTime);
        long hoursParked = minutesParked / 60;
        long remainingMinutes = minutesParked % 60;

        StringBuilder receiptText = new StringBuilder();
        receiptText.append("\n");
        receiptText.append("=====================================\n");
        receiptText.append("         \uD83E\uDDFE PARKING RECEIPT         \n");
        receiptText.append("=====================================\n");
        receiptText.append(String.format("%-14s: %s%n", "Receipt ID",  receipt.getId()));
        receiptText.append(String.format("%-14s: %s%n", "Ticket ID",   ticket.getId()));
        receiptText.append(String.format("%-14s: %s%n", "Slot ID",     ticket.getSlotId()));
        receiptText.append("-------------------------------------\n");
        receiptText.append(String.format("%-14s: %s%n", "Entry Time",  ticket.getEntryTime().format(FORMATTER)));
        receiptText.append(String.format("%-14s: %s%n", "Exit Time",   exitTime.format(FORMATTER)));
        receiptText.append(String.format("%-14s: %dh %dm%n", "Duration", hoursParked, remainingMinutes));
        receiptText.append("-------------------------------------\n");
        receiptText.append(String.format("%-14s: \u20B9%.2f%n", "Total Fee",     receipt.getTotalFee()));
        receiptText.append(String.format("%-14s: %s%n",  "Status",         receipt.getPaymentStatus()));
        receiptText.append("=====================================\n");
        receiptText.append("   Thank you for parking with us!   \n");
        receiptText.append("=====================================\n");

        System.out.println("[SERVICE] Receipt text generated successfully");
        return receiptText.toString();
    }
}
