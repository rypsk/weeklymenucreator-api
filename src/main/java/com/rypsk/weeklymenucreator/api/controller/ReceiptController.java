package com.rypsk.weeklymenucreator.api.controller;

import com.rypsk.weeklymenucreator.api.model.dto.ReceiptRequest;
import com.rypsk.weeklymenucreator.api.model.dto.ReceiptResponse;
import com.rypsk.weeklymenucreator.api.service.ReceiptService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/receipt")
public class ReceiptController {

    private static final String ID = "id";
    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceiptResponse> getReceipt(@PathVariable @Min(1) Long id){
        return ResponseEntity.ok(receiptService.getReceipt(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReceiptResponse> updateReceipt(@PathVariable @Min(1) Long id, @RequestBody ReceiptRequest request){
        return ResponseEntity.ok(receiptService.updateReceipt(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReceipt(@PathVariable @Min(1) Long id){
        receiptService.deleteReceipt(id);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PostMapping("/user/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReceiptResponse> createReceiptForUser(@RequestBody ReceiptRequest request, @PathVariable(ID) @Min(1) Long userId) {
        return ResponseEntity.ok(receiptService.createReceiptForUser(request, userId));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ReceiptResponse>> getReceiptsByUser(@PathVariable(ID) @Min(1) Long userId) {
        return ResponseEntity.ok(receiptService.getReceiptsByUser(userId));
    }
}
