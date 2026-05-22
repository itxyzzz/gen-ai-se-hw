package com.setu.support.ticket;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
public class TicketController {
    private final TicketService ticketService;
    private final TicketImportService ticketImportService;

    public TicketController(TicketService ticketService, TicketImportService ticketImportService) {
        this.ticketService = ticketService;
        this.ticketImportService = ticketImportService;
    }

    @PostMapping("/tickets")
    @ResponseStatus(HttpStatus.CREATED)
    public Ticket create(@RequestBody CreateTicketRequest request) {
        return ticketService.create(request);
    }

    @PostMapping("/tickets/import")
    public TicketImportSummary importTickets(
        @RequestParam MultipartFile file,
        @RequestParam(required = false) String format
    ) {
        return ticketImportService.importFile(file, format);
    }

    @PostMapping("/tickets/{id}/auto-classify")
    public ClassificationResponse autoClassify(@PathVariable UUID id) {
        return ticketService.autoClassify(id);
    }

    @GetMapping("/tickets")
    public List<Ticket> list(
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String priority,
        @RequestParam(required = false) String status,
        @RequestParam(name = "customer_id", required = false) String customerId,
        @RequestParam(name = "customer_email", required = false) String customerEmail,
        @RequestParam(name = "assigned_to", required = false) String assignedTo,
        @RequestParam(required = false) String source,
        @RequestParam(required = false) String tag
    ) {
        return ticketService.findFiltered(category, priority, status, customerId, customerEmail, assignedTo, source, tag);
    }

    @GetMapping("/tickets/{id}")
    public Ticket get(@PathVariable UUID id) {
        return ticketService.findById(id);
    }

    @PutMapping("/tickets/{id}")
    public Ticket update(@PathVariable UUID id, @RequestBody UpdateTicketRequest request) {
        return ticketService.update(id, request);
    }

    @DeleteMapping("/tickets/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        ticketService.delete(id);
    }
}
