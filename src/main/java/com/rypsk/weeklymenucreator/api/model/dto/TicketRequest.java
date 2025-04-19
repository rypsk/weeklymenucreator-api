package com.rypsk.weeklymenucreator.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "DTO for creating a new purchase ticket")
public record TicketRequest(
        @Schema(description = "Name of the store where the purchase was made", example = "Walmart")
        @NotBlank(message = "Store name is required")
        String storeName,

        @Schema(description = "Date when the purchase was made", example = "2024-01-20")
        @NotNull(message = "Purchase date is required")
        LocalDate purchaseDate,

        @Schema(description = "List of purchased items")
        @NotEmpty(message = "Items list cannot be empty")
        @Valid
        List<TicketItemRequest> items,

        @Schema(description = "ID of the user who made the purchase", example = "1")
        @NotNull(message = "User ID is required")
        @Positive(message = "User ID must be positive")
        Long userId
) {
}
