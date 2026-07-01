package service;

import domain.ParkingSlot;
import domain.Ticket;
import domain.Vehicle;
import repository.TicketRepository;

import java.util.Optional;
import java.util.UUID;

public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
        System.out.println("[SERVICE] TicketService initialized");
    }

    public Ticket generateTicket(Vehicle vehicle, ParkingSlot slot) {
        System.out.println("[SERVICE] Generating ticket for vehicle: " + vehicle.getId()
                + " | Slot: " + slot.getId());
        Ticket ticket = new Ticket(vehicle.getId(), slot.getId());
        ticketRepository.save(ticket);
        System.out.println("[SERVICE] Ticket generated: " + ticket.getId());
        return ticket;
    }

    public Optional<Ticket> getTicket(UUID ticketId) {
        System.out.println("[SERVICE] Fetching ticket: " + ticketId);
        return ticketRepository.findById(ticketId);
    }

    public void deactivateTicket(UUID ticketId) {
        System.out.println("[SERVICE] Deactivating ticket: " + ticketId);
        ticketRepository.deactivateTicket(ticketId);
    }
}
