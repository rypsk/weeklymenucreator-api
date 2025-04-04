package com.rypsk.weeklymenucreator.api.service;

import com.rypsk.weeklymenucreator.api.model.dto.ReceiptRequest;
import com.rypsk.weeklymenucreator.api.model.dto.ReceiptResponse;
import jakarta.validation.constraints.Min;

import java.util.List;

public interface ReceiptService {
    ReceiptResponse getReceipt(@Min(1) Long id);

    ReceiptResponse updateReceipt(@Min(1) Long id, ReceiptRequest request);

    void deleteReceipt(@Min(1) Long id);

    ReceiptResponse createReceiptForUser(ReceiptRequest request, @Min(1) Long userId);

    List<ReceiptResponse> getReceiptsByUser(@Min(1) Long userId);
}
