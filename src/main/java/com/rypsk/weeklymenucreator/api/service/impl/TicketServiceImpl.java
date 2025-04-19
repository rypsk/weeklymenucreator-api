package com.rypsk.weeklymenucreator.api.service.impl;

import com.rypsk.weeklymenucreator.api.model.dto.TicketItemResponse;
import com.rypsk.weeklymenucreator.api.model.dto.TicketRequest;
import com.rypsk.weeklymenucreator.api.model.dto.TicketResponse;
import com.rypsk.weeklymenucreator.api.model.entity.Ticket;
import com.rypsk.weeklymenucreator.api.model.entity.TicketItem;
import com.rypsk.weeklymenucreator.api.model.entity.User;
import com.rypsk.weeklymenucreator.api.repository.TicketRepository;
import com.rypsk.weeklymenucreator.api.repository.UserRepository;
import com.rypsk.weeklymenucreator.api.service.TicketService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.Min;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketServiceImpl(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TicketResponse getTicket(@Min(1) Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + id));
        return mapToResponse(ticket);
    }

    @Override
    @Transactional
    public TicketResponse createTicket(TicketRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + request.userId()));

        Ticket ticket = new Ticket();
        ticket.setStoreName(request.storeName());
        ticket.setPurchaseDate(request.purchaseDate());
        ticket.setUser(user);

        Ticket savedTicket = ticketRepository.save(ticket);

        // Items will be added by the TicketItemService

        return mapToResponse(savedTicket);
    }

    @Override
    @Transactional
    public TicketResponse updateTicket(@Min(1) Long id, TicketRequest request) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + id));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + request.userId()));

        ticket.setStoreName(request.storeName());
        ticket.setPurchaseDate(request.purchaseDate());
        ticket.setUser(user);

        Ticket updatedTicket = ticketRepository.save(ticket);

        // Items will be handled by the TicketItemService

        return mapToResponse(updatedTicket);
    }

    @Override
    @Transactional
    public void deleteTicket(@Min(1) Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new EntityNotFoundException("Ticket not found with id: " + id);
        }
        ticketRepository.deleteById(id);
    }

    @Override
    public List<TicketResponse> getTicketsForUser(Long userId) {
        List<Ticket> tickets = ticketRepository.findByUserId(userId);
        return tickets.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketResponse> getTicketsByStore(String storeName) {
        List<Ticket> tickets = ticketRepository.findByStoreName(storeName);
        return tickets.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketResponse> getTicketsByDate(LocalDate date) {
        List<Ticket> tickets = ticketRepository.findByPurchaseDate(date);
        return tickets.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> compareTickets(@Min(1) Long ticketId1, @Min(1) Long ticketId2) {
        Ticket ticket1 = ticketRepository.findById(ticketId1)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + ticketId1));

        Ticket ticket2 = ticketRepository.findById(ticketId2)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + ticketId2));

        Map<String, Object> comparison = new HashMap<>();

        // Compare basic ticket information
        comparison.put("ticket1Id", ticket1.getId());
        comparison.put("ticket2Id", ticket2.getId());
        comparison.put("sameStore", Objects.equals(ticket1.getStoreName(), ticket2.getStoreName()));
        comparison.put("samePurchaseDate", Objects.equals(ticket1.getPurchaseDate(), ticket2.getPurchaseDate()));
        comparison.put("sameUser", Objects.equals(ticket1.getUser().getId(), ticket2.getUser().getId()));

        // Compare items
        List<TicketItem> items1 = ticket1.getItems();
        List<TicketItem> items2 = ticket2.getItems();

        // Items only in ticket1
        List<String> onlyInTicket1 = items1.stream()
                .filter(item1 -> items2.stream()
                        .noneMatch(item2 -> Objects.equals(item2.getProductName(), item1.getProductName())))
                .map(TicketItem::getProductName)
                .collect(Collectors.toList());

        // Items only in ticket2
        List<String> onlyInTicket2 = items2.stream()
                .filter(item2 -> items1.stream()
                        .noneMatch(item1 -> Objects.equals(item1.getProductName(), item2.getProductName())))
                .map(TicketItem::getProductName)
                .collect(Collectors.toList());

        // Items in both tickets but with different prices or quantities
        List<Map<String, Object>> differentItems = new ArrayList<>();

        for (TicketItem item1 : items1) {
            Optional<TicketItem> matchingItem = items2.stream()
                    .filter(item2 -> Objects.equals(item2.getProductName(), item1.getProductName()))
                    .findFirst();

            if (matchingItem.isPresent()) {
                TicketItem item2 = matchingItem.get();
                if (!Objects.equals(item1.getPrice(), item2.getPrice()) ||
                        item1.getQuantity() != item2.getQuantity()) {

                    Map<String, Object> diff = new HashMap<>();
                    diff.put("productName", item1.getProductName());
                    diff.put("ticket1Price", item1.getPrice());
                    diff.put("ticket2Price", item2.getPrice());
                    diff.put("ticket1Quantity", item1.getQuantity());
                    diff.put("ticket2Quantity", item2.getQuantity());

                    differentItems.add(diff);
                }
            }
        }

        comparison.put("onlyInTicket1", onlyInTicket1);
        comparison.put("onlyInTicket2", onlyInTicket2);
        comparison.put("differentItems", differentItems);

        // Calculate total price difference
        double totalPrice1 = items1.stream()
                .mapToDouble(item -> item.getPrice().doubleValue() * item.getQuantity())
                .sum();

        double totalPrice2 = items2.stream()
                .mapToDouble(item -> item.getPrice().doubleValue() * item.getQuantity())
                .sum();

        comparison.put("ticket1TotalPrice", totalPrice1);
        comparison.put("ticket2TotalPrice", totalPrice2);
        comparison.put("priceDifference", Math.abs(totalPrice1 - totalPrice2));

        return comparison;
    }

    private TicketResponse mapToResponse(Ticket ticket) {
        List<TicketItemResponse> itemResponses = ticket.getItems().stream()
                .map(item -> new TicketItemResponse(
                        item.getId(),
                        item.getProductName(),
                        item.getPrice(),
                        item.getQuantity()))
                .collect(Collectors.toList());

        return new TicketResponse(
                ticket.getId(),
                ticket.getStoreName(),
                ticket.getPurchaseDate(),
                ticket.getUser().getId(),
                itemResponses
        );
    }
}