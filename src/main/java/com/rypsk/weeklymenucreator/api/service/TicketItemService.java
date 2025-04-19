package com.rypsk.weeklymenucreator.api.service;

import com.rypsk.weeklymenucreator.api.model.dto.TicketItemRequest;
import com.rypsk.weeklymenucreator.api.model.dto.TicketItemResponse;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service for managing ticket items
 */
public interface TicketItemService {

    /**
     * Get a ticket item by ID
     *
     * @param id the ticket item ID
     * @return the ticket item response
     */
    TicketItemResponse getTicketItem(@Min(1) Long id);

    /**
     * Create a new ticket item for a ticket
     *
     * @param ticketId the ticket ID
     * @param request  the ticket item request
     * @return the created ticket item response
     */
    TicketItemResponse createTicketItem(@Min(1) Long ticketId, TicketItemRequest request);

    /**
     * Update a ticket item
     *
     * @param id      the ticket item ID
     * @param request the ticket item request
     * @return the updated ticket item response
     */
    TicketItemResponse updateTicketItem(@Min(1) Long id, TicketItemRequest request);

    /**
     * Delete a ticket item
     *
     * @param id the ticket item ID
     */
    void deleteTicketItem(@Min(1) Long id);

    /**
     * Get all ticket items for a ticket
     *
     * @param ticketId the ticket ID
     * @return list of ticket item responses
     */
    List<TicketItemResponse> getTicketItemsByTicket(@Min(1) Long ticketId);

    /**
     * Get all ticket items for a product name
     *
     * @param productName the product name
     * @return list of ticket item responses
     */
    List<TicketItemResponse> getTicketItemsByProductName(String productName);

    /**
     * Get all ticket items with a price greater than or equal to the specified price
     *
     * @param price the minimum price
     * @return list of ticket item responses
     */
    List<TicketItemResponse> getTicketItemsByMinPrice(BigDecimal price);

    /**
     * Get all ticket items with a price less than or equal to the specified price
     *
     * @param price the maximum price
     * @return list of ticket item responses
     */
    List<TicketItemResponse> getTicketItemsByMaxPrice(BigDecimal price);
}