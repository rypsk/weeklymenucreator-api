package com.rypsk.weeklymenucreator.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "DTO for ticket response")
public record TicketResponse(
        @Schema(description = "Ticket ID", example = "1")
        Long id,

        @Schema(description = "Name of the store where the purchase was made", example = "Walmart")
        String storeName,

        @Schema(description = "Date when the purchase was made", example = "2024-01-20")
        LocalDate purchaseDate,

        @Schema(description = "ID of the user who made the purchase", example = "1")
        Long userId,

        @Schema(description = "List of purchased items")
        List<TicketItemResponse> items
) {
}
