package com.rypsk.weeklymenucreator.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "DTO for creating a new ticket item")
public record TicketItemRequest(
        @Schema(description = "Name of the product", example = "Apples")
        @NotBlank(message = "Product name is required")
        String productName,

        @Schema(description = "Price of the product", example = "2.99")
        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        BigDecimal price,

        @Schema(description = "Quantity of the product", example = "3")
        @Positive(message = "Quantity must be positive")
        int quantity
) {
}
