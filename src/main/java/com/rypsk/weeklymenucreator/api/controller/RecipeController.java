package com.rypsk.weeklymenucreator.api.controller;

import com.rypsk.weeklymenucreator.api.model.dto.RecipeRequest;
import com.rypsk.weeklymenucreator.api.model.dto.RecipeResponse;
import com.rypsk.weeklymenucreator.api.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recipes")
@Tag(name = "Recipe", description = "Recipe management APIs")
public class RecipeController {

    private static final String ID = "id";
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a recipe by ID", description = "Returns a recipe by its ID")
    public ResponseEntity<RecipeResponse> getRecipe(
            @Parameter(description = "Recipe ID", required = true)
            @PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(recipeService.getRecipe(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a recipe", description = "Updates a recipe and returns it")
    public ResponseEntity<RecipeResponse> updateRecipe(
            @Parameter(description = "Recipe ID", required = true)
            @PathVariable @Min(1) Long id,
            @Parameter(description = "Updated recipe details", required = true)
            @RequestBody RecipeRequest request) {
        return ResponseEntity.ok(recipeService.updateRecipe(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a recipe", description = "Deletes a recipe by its ID")
    public ResponseEntity<Void> deleteRecipe(
            @Parameter(description = "Recipe ID", required = true)
            @PathVariable @Min(1) Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/public")
    @Operation(summary = "Get public recipes", description = "Returns all public recipes")
    public ResponseEntity<List<RecipeResponse>> getPublicRecipes() {
        return ResponseEntity.ok(recipeService.getPublicRecipes());
    }

    @PostMapping("/users/me")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a recipe for current user", description = "Creates a new recipe for the current user and returns it")
    public ResponseEntity<RecipeResponse> createRecipeForMe(
            @Parameter(description = "Recipe details", required = true)
            @RequestBody RecipeRequest request) {
        return ResponseEntity.ok(recipeService.createRecipeForMe(request));
    }

    @PostMapping("/users/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a recipe for a user", description = "Creates a new recipe for a specific user and returns it")
    public ResponseEntity<RecipeResponse> createRecipeForUser(
            @Parameter(description = "Recipe details", required = true)
            @RequestBody RecipeRequest request,
            @Parameter(description = "User ID", required = true)
            @PathVariable(ID) @Min(1) Long userId) {
        return ResponseEntity.ok(recipeService.createRecipeForUser(request, userId));
    }

    @GetMapping("/users/me")
    @Operation(summary = "Get recipes for current user", description = "Returns all recipes for the current user")
    public ResponseEntity<List<RecipeResponse>> getRecipesForMe() {
        return ResponseEntity.ok(recipeService.getRecipesForMe());
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Get recipes for a user", description = "Returns all recipes for a specific user")
    public ResponseEntity<List<RecipeResponse>> getRecipesForUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable(ID) @Min(1) Long userId) {
        return ResponseEntity.ok(recipeService.getRecipesForUser(userId));
    }

    @GetMapping("/users/me/available")
    @Operation(summary = "Get available recipes for current user", description = "Returns all available recipes for the current user based on their ingredients")
    public ResponseEntity<List<RecipeResponse>> getAvailableRecipesForMe() {
        return ResponseEntity.ok(recipeService.getAvailableRecipesForMe());
    }

    @GetMapping("/users/{id}/available")
    @Operation(summary = "Get available recipes for a user", description = "Returns all available recipes for a specific user based on their ingredients")
    public ResponseEntity<List<RecipeResponse>> getAvailableRecipesForUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable(ID) @Min(1) Long userId) {
        return ResponseEntity.ok(recipeService.getAvailableRecipesForUser(userId));
    }
}
