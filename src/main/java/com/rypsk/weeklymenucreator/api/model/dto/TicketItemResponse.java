package com.rypsk.weeklymenucreator.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "DTO for ticket item response")
public record TicketItemResponse(
        @Schema(description = "Ticket item ID", example = "1")
        Long id,

        @Schema(description = "Name of the product", example = "Apples")
        String productName,

        @Schema(description = "Price of the product", example = "2.99")
        BigDecimal price,

        @Schema(description = "Quantity of the product", example = "3")
        int quantity,

        @Schema(description = "Total price (price * quantity)", example = "8.97")
        BigDecimal totalPrice
) {
    public TicketItemResponse(Long id, String productName, BigDecimal price, int quantity) {
        this(id, productName, price, quantity,
                price != null ? price.multiply(BigDecimal.valueOf(quantity)) : null);
    }
}
