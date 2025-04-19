package com.rypsk.weeklymenucreator.api.service;

import com.rypsk.weeklymenucreator.api.model.dto.TicketRequest;
import com.rypsk.weeklymenucreator.api.model.dto.TicketResponse;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service for managing tickets
 */
public interface TicketService {

    /**
     * Get a ticket by ID
     *
     * @param id the ticket ID
     * @return the ticket response
     */
    TicketResponse getTicket(@Min(1) Long id);

    /**
     * Create a new ticket
     *
     * @param request the ticket request
     * @return the created ticket response
     */
    TicketResponse createTicket(TicketRequest request);

    /**
     * Update a ticket
     *
     * @param id      the ticket ID
     * @param request the ticket request
     * @return the updated ticket response
     */
    TicketResponse updateTicket(@Min(1) Long id, TicketRequest request);

    /**
     * Delete a ticket
     *
     * @param id the ticket ID
     */
    void deleteTicket(@Min(1) Long id);

    /**
     * Get all tickets for a user
     *
     * @param userId the user ID
     * @return list of ticket responses
     */
    List<TicketResponse> getTicketsForUser(Long userId);

    /**
     * Get all tickets for a store
     *
     * @param storeName the store name
     * @return list of ticket responses
     */
    List<TicketResponse> getTicketsByStore(String storeName);

    /**
     * Get all tickets for a date
     *
     * @param date the purchase date
     * @return list of ticket responses
     */
    List<TicketResponse> getTicketsByDate(LocalDate date);

    /**
     * Compare two tickets and return the differences
     *
     * @param ticketId1 the first ticket ID
     * @param ticketId2 the second ticket ID
     * @return map of differences between the tickets
     */
    Map<String, Object> compareTickets(@Min(1) Long ticketId1, @Min(1) Long ticketId2);
}