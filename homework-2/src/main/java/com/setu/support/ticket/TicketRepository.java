package com.setu.support.ticket;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TicketRepository {
    private final ConcurrentHashMap<UUID, Ticket> tickets = new ConcurrentHashMap<>();

    public Ticket save(Ticket ticket) {
        tickets.put(ticket.id(), ticket);
        return ticket;
    }

    public Optional<Ticket> findById(UUID id) {
        return Optional.ofNullable(tickets.get(id));
    }

    public List<Ticket> findAll() {
        return new ArrayList<>(tickets.values());
    }

    public boolean delete(UUID id) {
        return tickets.remove(id) != null;
    }

    public void clear() {
        tickets.clear();
    }
}
