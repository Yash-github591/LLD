package repository;

import domain.Payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class PaymentRepository {

    // A ticket may have multiple payment attempts, so store as List per ticketId
    private final Map<UUID, Payment> store = new HashMap<>();
    private final Map<UUID, List<UUID>> ticketToPayments = new HashMap<>();

    public void save(Payment payment) {
        store.put(payment.getId(), payment);
        ticketToPayments
            .computeIfAbsent(payment.getTicketId(), k -> new ArrayList<>())
            .add(payment.getId());
        System.out.println("[REPO] Payment saved: " + payment.getId() + " for ticket: " + payment.getTicketId());
    }

    public Optional<Payment> findById(UUID paymentId) {
        return Optional.ofNullable(store.get(paymentId));
    }

    /**
     * Returns the most recent payment attempt for a ticket.
     * Used during retry to check if a prior attempt succeeded (to avoid double-charging).
     */
    public Optional<Payment> findByTicketId(UUID ticketId) {
        List<UUID> paymentIds = ticketToPayments.get(ticketId);
        if (paymentIds == null || paymentIds.isEmpty()) return Optional.empty();
        // Return the last attempt
        UUID lastId = paymentIds.get(paymentIds.size() - 1);
        return Optional.ofNullable(store.get(lastId));
    }

    public List<Payment> findAllByTicketId(UUID ticketId) {
        List<UUID> paymentIds = ticketToPayments.getOrDefault(ticketId, new ArrayList<>());
        return paymentIds.stream()
                .map(store::get)
                .collect(Collectors.toList());
    }

    public void update(Payment payment) {
        if (!store.containsKey(payment.getId())) {
            throw new IllegalArgumentException("Payment not found: " + payment.getId());
        }
        store.put(payment.getId(), payment);
        System.out.println("[REPO] Payment updated: " + payment.getId() + " | Status: " + payment.getStatus());
    }
}
