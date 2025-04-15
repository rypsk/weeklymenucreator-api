package com.rypsk.weeklymenucreator.api.service.impl;

import com.rypsk.weeklymenucreator.api.model.dto.RecipeRequest;
import com.rypsk.weeklymenucreator.api.model.dto.RecipeResponse;
import com.rypsk.weeklymenucreator.api.model.entity.Recipe;
import com.rypsk.weeklymenucreator.api.model.entity.User;
import com.rypsk.weeklymenucreator.api.repository.RecipeRepository;
import com.rypsk.weeklymenucreator.api.repository.UserRepository;
import com.rypsk.weeklymenucreator.api.service.RecipeService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public RecipeResponse getRecipe(Long id) {
        User user = getCurrentUser();
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found."));
        if (!recipe.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot get this recipe.");
        }
        return mapToResponse(recipe);
    }

    @Override
    public RecipeResponse updateRecipe(Long id, RecipeRequest request) {
        User user = getCurrentUser();
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found."));
        if (!recipe.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot modify this recipe.");
        }
        recipe.setName(request.name());
        recipe.setDescription(request.description());
        recipe.setDifficulty(request.difficulty());
        recipe.setIngredients(request.ingredients());
        return mapToResponse(recipeRepository.save(recipe));
    }

    @Override
    public void deleteRecipe(Long id) {
        User user = getCurrentUser();
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found"));
        if (!recipe.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot delete this recipe.");
        }
        recipeRepository.delete(recipe);
    }

    @Override
    public RecipeResponse createRecipeForUser(RecipeRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found."));
        Recipe recipe = new Recipe();
        recipe.setName(request.name());
        recipe.setDescription(request.description());
        recipe.setDifficulty(request.difficulty());
        recipe.setIngredients(request.ingredients());
        recipe.setUser(user);
        return mapToResponse(recipeRepository.save(recipe));
    }

    @Override
    public List<RecipeResponse> getRecipesForUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found."));
        return recipeRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<RecipeResponse> getAvailableRecipesForUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found."));
        return recipeRepository.findAvailableForUser(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<RecipeResponse> getPublicRecipes() {
        return recipeRepository.findAllByIsPublic(true)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private RecipeResponse mapToResponse(Recipe recipe) {
        return new RecipeResponse(
                recipe.getId(),
                recipe.getName(),
                recipe.getDescription(),
                recipe.getIngredients(),
                recipe.getDifficulty(),
                recipe.getSeasons(),
                recipe.isPublic(),
                recipe.getUser().getUsername()
        );
    }
}
