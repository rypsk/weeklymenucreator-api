package com.rypsk.weeklymenucreator.api.service.impl;

import com.rypsk.weeklymenucreator.api.model.dto.ReceiptRequest;
import com.rypsk.weeklymenucreator.api.model.dto.ReceiptResponse;
import com.rypsk.weeklymenucreator.api.model.entity.Receipt;
import com.rypsk.weeklymenucreator.api.repository.ReceiptRepository;
import com.rypsk.weeklymenucreator.api.repository.UserRepository;
import com.rypsk.weeklymenucreator.api.service.ReceiptService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReceiptServiceImpl implements ReceiptService {
    
    private final ReceiptRepository receiptRepository;
    private final UserRepository userRepository;

    public ReceiptServiceImpl(ReceiptRepository receiptRepository, UserRepository userRepository) {
        this.receiptRepository = receiptRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ReceiptResponse getReceipt(Long id) {
        Receipt receipt = receiptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found."));
        return mapToResponse(receipt);
    }

    @Override
    public ReceiptResponse updateReceipt(Long id, ReceiptRequest request) {
        Receipt receipt = receiptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found."));
        receipt.setName(request.name());
        receipt.setDescription(request.description());
        receipt.setDifficulty(request.difficulty());
        receipt.setIngredients(request.ingredients());
        receipt.setDishType(request.dishType());
        receipt.setDietType(request.dietType());
        return mapToResponse(receiptRepository.save(receipt));
    }

    @Override
    public void deleteReceipt(Long id) {
        if (!receiptRepository.existsById(id)){
            throw new RuntimeException("Dish not found.");
        }
        receiptRepository.deleteById(id);
    }

    @Override
    public ReceiptResponse createReceiptForUser(ReceiptRequest request, Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));
        var receipt = new Receipt();
        receipt.setName(request.name());
        receipt.setDescription(request.description());
        receipt.setDifficulty(request.difficulty());
        receipt.setIngredients(request.ingredients());
        receipt.setDishType(request.dishType());
        receipt.setDietType(request.dietType());
        receipt.setUser(user);
        return mapToResponse(receiptRepository.save(receipt));
    }

    @Override
    public List<ReceiptResponse> getReceiptsByUser(Long userId) {
        return receiptRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    private ReceiptResponse mapToResponse(Receipt receipt){
        return new ReceiptResponse(
                receipt.getId(),
                receipt.getName(),
                receipt.getDescription(),
                receipt.getIngredients(),
                receipt.getDifficulty(),
                receipt.getSeasons(),
                receipt.getDishType(),
                receipt.getDietType()
        );
    }
}
