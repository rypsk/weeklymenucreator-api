package com.rypsk.weeklymenucreator.api.repository;

import com.rypsk.weeklymenucreator.api.model.entity.Ticket;
import com.rypsk.weeklymenucreator.api.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for Ticket entities
 */
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /**
     * Find all tickets for a specific user
     *
     * @param user the user
     * @return list of tickets
     */
    List<Ticket> findByUser(User user);

    /**
     * Find all tickets for a specific user ID
     *
     * @param userId the user ID
     * @return list of tickets
     */
    List<Ticket> findByUserId(Long userId);

    /**
     * Find all tickets for a specific store name
     *
     * @param storeName the store name
     * @return list of tickets
     */
    List<Ticket> findByStoreName(String storeName);

    /**
     * Find all tickets for a specific purchase date
     *
     * @param purchaseDate the purchase date
     * @return list of tickets
     */
    List<Ticket> findByPurchaseDate(LocalDate purchaseDate);

    /**
     * Find all tickets for a specific user and purchase date
     *
     * @param userId       the user ID
     * @param purchaseDate the purchase date
     * @return list of tickets
     */
    List<Ticket> findByUserIdAndPurchaseDate(Long userId, LocalDate purchaseDate);
}