package com.rypsk.weeklymenucreator.api.controller;

import com.rypsk.weeklymenucreator.api.model.dto.TicketItemRequest;
import com.rypsk.weeklymenucreator.api.model.dto.TicketItemResponse;
import com.rypsk.weeklymenucreator.api.model.dto.TicketRequest;
import com.rypsk.weeklymenucreator.api.model.dto.TicketResponse;
import com.rypsk.weeklymenucreator.api.service.TicketItemService;
import com.rypsk.weeklymenucreator.api.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tickets")
@Tag(name = "Ticket", description = "Ticket management APIs")
public class TicketController {

    private final TicketService ticketService;
    private final TicketItemService ticketItemService;

    public TicketController(TicketService ticketService, TicketItemService ticketItemService) {
        this.ticketService = ticketService;
        this.ticketItemService = ticketItemService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a ticket by ID", description = "Returns a ticket by its ID")
    public ResponseEntity<TicketResponse> getTicket(
            @Parameter(description = "Ticket ID", required = true)
            @PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(ticketService.getTicket(id));
    }

    @PostMapping
    @Operation(summary = "Create a new ticket", description = "Creates a new ticket and returns it")
    public ResponseEntity<TicketResponse> createTicket(
            @Parameter(description = "Ticket details", required = true)
            @Valid @RequestBody TicketRequest request) {
        return new ResponseEntity<>(ticketService.createTicket(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a ticket", description = "Updates a ticket and returns it")
    public ResponseEntity<TicketResponse> updateTicket(
            @Parameter(description = "Ticket ID", required = true)
            @PathVariable @Min(1) Long id,
            @Parameter(description = "Updated ticket details", required = true)
            @Valid @RequestBody TicketRequest request) {
        return ResponseEntity.ok(ticketService.updateTicket(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a ticket", description = "Deletes a ticket by its ID")
    public ResponseEntity<Void> deleteTicket(
            @Parameter(description = "Ticket ID", required = true)
            @PathVariable @Min(1) Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get tickets by user", description = "Returns all tickets for a specific user")
    public ResponseEntity<List<TicketResponse>> getTicketsByUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.getTicketsForUser(userId));
    }

    @GetMapping("/store/{storeName}")
    @Operation(summary = "Get tickets by store", description = "Returns all tickets for a specific store")
    public ResponseEntity<List<TicketResponse>> getTicketsByStore(
            @Parameter(description = "Store name", required = true)
            @PathVariable String storeName) {
        return ResponseEntity.ok(ticketService.getTicketsByStore(storeName));
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Get tickets by date", description = "Returns all tickets for a specific date")
    public ResponseEntity<List<TicketResponse>> getTicketsByDate(
            @Parameter(description = "Purchase date (format: yyyy-MM-dd)", required = true)
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ticketService.getTicketsByDate(date));
    }

    @GetMapping("/compare")
    @Operation(summary = "Compare two tickets", description = "Returns a comparison between two tickets")
    public ResponseEntity<Map<String, Object>> compareTickets(
            @Parameter(description = "First ticket ID", required = true)
            @RequestParam @Min(1) Long ticket1Id,
            @Parameter(description = "Second ticket ID", required = true)
            @RequestParam @Min(1) Long ticket2Id) {
        return ResponseEntity.ok(ticketService.compareTickets(ticket1Id, ticket2Id));
    }

    // Ticket Item endpoints
    @GetMapping("/{ticketId}/items")
    @Operation(summary = "Get all items for a ticket", description = "Returns all items for a specific ticket")
    public ResponseEntity<List<TicketItemResponse>> getTicketItems(
            @Parameter(description = "Ticket ID", required = true)
            @PathVariable @Min(1) Long ticketId) {
        return ResponseEntity.ok(ticketItemService.getTicketItemsByTicket(ticketId));
    }

    @PostMapping("/{ticketId}/items")
    @Operation(summary = "Add an item to a ticket", description = "Adds a new item to a ticket and returns it")
    public ResponseEntity<TicketItemResponse> addTicketItem(
            @Parameter(description = "Ticket ID", required = true)
            @PathVariable @Min(1) Long ticketId,
            @Parameter(description = "Ticket item details", required = true)
            @Valid @RequestBody TicketItemRequest request) {
        return new ResponseEntity<>(ticketItemService.createTicketItem(ticketId, request), HttpStatus.CREATED);
    }

    @GetMapping("/items/{id}")
    @Operation(summary = "Get a ticket item by ID", description = "Returns a ticket item by its ID")
    public ResponseEntity<TicketItemResponse> getTicketItem(
            @Parameter(description = "Ticket item ID", required = true)
            @PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(ticketItemService.getTicketItem(id));
    }

    @PutMapping("/items/{id}")
    @Operation(summary = "Update a ticket item", description = "Updates a ticket item and returns it")
    public ResponseEntity<TicketItemResponse> updateTicketItem(
            @Parameter(description = "Ticket item ID", required = true)
            @PathVariable @Min(1) Long id,
            @Parameter(description = "Updated ticket item details", required = true)
            @Valid @RequestBody TicketItemRequest request) {
        return ResponseEntity.ok(ticketItemService.updateTicketItem(id, request));
    }

    @DeleteMapping("/items/{id}")
    @Operation(summary = "Delete a ticket item", description = "Deletes a ticket item by its ID")
    public ResponseEntity<Void> deleteTicketItem(
            @Parameter(description = "Ticket item ID", required = true)
            @PathVariable @Min(1) Long id) {
        ticketItemService.deleteTicketItem(id);
        return ResponseEntity.noContent().build();
    }
}