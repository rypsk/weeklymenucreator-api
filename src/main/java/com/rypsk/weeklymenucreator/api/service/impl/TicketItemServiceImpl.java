package com.rypsk.weeklymenucreator.api.service.impl;

import com.rypsk.weeklymenucreator.api.model.dto.TicketItemRequest;
import com.rypsk.weeklymenucreator.api.model.dto.TicketItemResponse;
import com.rypsk.weeklymenucreator.api.model.entity.Ticket;
import com.rypsk.weeklymenucreator.api.model.entity.TicketItem;
import com.rypsk.weeklymenucreator.api.repository.TicketItemRepository;
import com.rypsk.weeklymenucreator.api.repository.TicketRepository;
import com.rypsk.weeklymenucreator.api.service.TicketItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.Min;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketItemServiceImpl implements TicketItemService {

    private final TicketItemRepository ticketItemRepository;
    private final TicketRepository ticketRepository;

    public TicketItemServiceImpl(TicketItemRepository ticketItemRepository, TicketRepository ticketRepository) {
        this.ticketItemRepository = ticketItemRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public TicketItemResponse getTicketItem(@Min(1) Long id) {
        TicketItem ticketItem = ticketItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket item not found with id: " + id));
        return mapToResponse(ticketItem);
    }

    @Override
    @Transactional
    public TicketItemResponse createTicketItem(@Min(1) Long ticketId, TicketItemRequest request) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + ticketId));

        TicketItem ticketItem = new TicketItem();
        ticketItem.setProductName(request.productName());
        ticketItem.setPrice(request.price());
        ticketItem.setQuantity(request.quantity());
        ticketItem.setTicket(ticket);

        TicketItem savedTicketItem = ticketItemRepository.save(ticketItem);

        // Update the ticket's items list
        ticket.addItem(savedTicketItem);
        ticketRepository.save(ticket);

        return mapToResponse(savedTicketItem);
    }

    @Override
    @Transactional
    public TicketItemResponse updateTicketItem(@Min(1) Long id, TicketItemRequest request) {
        TicketItem ticketItem = ticketItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket item not found with id: " + id));

        ticketItem.setProductName(request.productName());
        ticketItem.setPrice(request.price());
        ticketItem.setQuantity(request.quantity());

        TicketItem updatedTicketItem = ticketItemRepository.save(ticketItem);
        return mapToResponse(updatedTicketItem);
    }

    @Override
    @Transactional
    public void deleteTicketItem(@Min(1) Long id) {
        TicketItem ticketItem = ticketItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket item not found with id: " + id));

        // Remove the item from the ticket's items list
        Ticket ticket = ticketItem.getTicket();
        if (ticket != null) {
            ticket.removeItem(ticketItem);
            ticketRepository.save(ticket);
        }

        ticketItemRepository.deleteById(id);
    }

    @Override
    public List<TicketItemResponse> getTicketItemsByTicket(@Min(1) Long ticketId) {
        List<TicketItem> ticketItems = ticketItemRepository.findByTicketId(ticketId);
        return ticketItems.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketItemResponse> getTicketItemsByProductName(String productName) {
        List<TicketItem> ticketItems = ticketItemRepository.findByProductName(productName);
        return ticketItems.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketItemResponse> getTicketItemsByMinPrice(BigDecimal price) {
        List<TicketItem> ticketItems = ticketItemRepository.findByPriceGreaterThanEqual(price);
        return ticketItems.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketItemResponse> getTicketItemsByMaxPrice(BigDecimal price) {
        List<TicketItem> ticketItems = ticketItemRepository.findByPriceLessThanEqual(price);
        return ticketItems.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private TicketItemResponse mapToResponse(TicketItem ticketItem) {
        return new TicketItemResponse(
                ticketItem.getId(),
                ticketItem.getProductName(),
                ticketItem.getPrice(),
                ticketItem.getQuantity()
        );
    }
}