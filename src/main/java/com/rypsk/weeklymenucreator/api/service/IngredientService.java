package com.rypsk.weeklymenucreator.api.service;

import com.rypsk.weeklymenucreator.api.model.dto.IngredientRequest;
import com.rypsk.weeklymenucreator.api.model.dto.IngredientResponse;
import jakarta.validation.constraints.Min;

import java.util.List;

public interface IngredientService {
    IngredientResponse getIngredient(@Min(1) Long id);

    IngredientResponse updateIngredient(@Min(1) Long id, IngredientRequest request);

    void deleteIngredient(@Min(1) Long id);

    IngredientResponse createIngredientForMe(IngredientRequest request);

    IngredientResponse createIngredientForUser(IngredientRequest request, Long userId);

    List<IngredientResponse> getIngredientsForMe();

    List<IngredientResponse> getIngredientsForUser(Long userId);
}
