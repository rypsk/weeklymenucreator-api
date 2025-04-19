package com.rypsk.weeklymenucreator.api.repository;

import com.rypsk.weeklymenucreator.api.model.entity.Ticket;
import com.rypsk.weeklymenucreator.api.model.entity.TicketItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository for TicketItem entities
 */
public interface TicketItemRepository extends JpaRepository<TicketItem, Long> {

    /**
     * Find all ticket items for a specific ticket
     *
     * @param ticket the ticket
     * @return list of ticket items
     */
    List<TicketItem> findByTicket(Ticket ticket);

    /**
     * Find all ticket items for a specific ticket ID
     *
     * @param ticketId the ticket ID
     * @return list of ticket items
     */
    List<TicketItem> findByTicketId(Long ticketId);

    /**
     * Find all ticket items for a specific product name
     *
     * @param productName the product name
     * @return list of ticket items
     */
    List<TicketItem> findByProductName(String productName);

    /**
     * Find all ticket items with a price greater than or equal to the specified price
     *
     * @param price the minimum price
     * @return list of ticket items
     */
    List<TicketItem> findByPriceGreaterThanEqual(BigDecimal price);

    /**
     * Find all ticket items with a price less than or equal to the specified price
     *
     * @param price the maximum price
     * @return list of ticket items
     */
    List<TicketItem> findByPriceLessThanEqual(BigDecimal price);

    /**
     * Find all ticket items with a quantity greater than or equal to the specified quantity
     *
     * @param quantity the minimum quantity
     * @return list of ticket items
     */
    List<TicketItem> findByQuantityGreaterThanEqual(int quantity);
}