package repository;

import domain.Ticket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class TicketRepository {

    private final Map<UUID, Ticket> store = new HashMap<>();

    public void save(Ticket ticket) {
        store.put(ticket.getId(), ticket);
        System.out.println("[REPO] Ticket saved: " + ticket.getId());
    }

    public Optional<Ticket> findById(UUID ticketId) {
        return Optional.ofNullable(store.get(ticketId));
    }

    public List<Ticket> findActiveTickets() {
        return store.values().stream()
                .filter(Ticket::isActive)
                .collect(Collectors.toList());
    }

    /**
     * Deactivates a ticket to prevent it being reused for a second exit.
     */
    public void deactivateTicket(UUID ticketId) {
        Ticket ticket = store.get(ticketId);
        if (ticket == null) throw new IllegalArgumentException("Ticket not found: " + ticketId);
        ticket.deactivate();
        System.out.println("[REPO] Ticket deactivated: " + ticketId);
    }

    public void update(Ticket ticket) {
        if (!store.containsKey(ticket.getId())) {
            throw new IllegalArgumentException("Ticket not found: " + ticket.getId());
        }
        store.put(ticket.getId(), ticket);
    }
}
