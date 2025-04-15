package com.rypsk.weeklymenucreator.api.service;

import com.rypsk.weeklymenucreator.api.model.dto.RecipeRequest;
import com.rypsk.weeklymenucreator.api.model.dto.RecipeResponse;
import jakarta.validation.constraints.Min;

import java.util.List;

public interface RecipeService {
    RecipeResponse getRecipe(@Min(1) Long id);

    RecipeResponse updateRecipe(@Min(1) Long id, RecipeRequest request);

    void deleteRecipe(@Min(1) Long id);

    RecipeResponse createRecipeForUser(RecipeRequest request, Long userId);

    List<RecipeResponse> getRecipesForUser(Long userId);

    List<RecipeResponse> getAvailableRecipesForUser(Long userId);

    List<RecipeResponse> getPublicRecipes();
}
