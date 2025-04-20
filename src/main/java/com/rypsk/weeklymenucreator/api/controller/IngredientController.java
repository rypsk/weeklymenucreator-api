package com.rypsk.weeklymenucreator.api.controller;

import com.rypsk.weeklymenucreator.api.model.dto.IngredientRequest;
import com.rypsk.weeklymenucreator.api.model.dto.IngredientResponse;
import com.rypsk.weeklymenucreator.api.service.IngredientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ingredients")
@Tag(name = "Ingredient", description = "Ingredient management APIs")
public class IngredientController {

    private static final String ID = "id";
    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an ingredient by ID", description = "Returns an ingredient by its ID")
    public ResponseEntity<IngredientResponse> getIngredient(
            @Parameter(description = "Ingredient ID", required = true)
            @PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(ingredientService.getIngredient(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an ingredient", description = "Updates an ingredient and returns it")
    public ResponseEntity<IngredientResponse> updateIngredient(
            @Parameter(description = "Ingredient ID", required = true)
            @PathVariable @Min(1) Long id,
            @Parameter(description = "Updated ingredient details", required = true)
            @RequestBody IngredientRequest request) {
        return ResponseEntity.ok(ingredientService.updateIngredient(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an ingredient", description = "Deletes an ingredient by its ID")
    public ResponseEntity<Void> deleteIngredient(
            @Parameter(description = "Ingredient ID", required = true)
            @PathVariable @Min(1) Long id) {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/users/me")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create an ingredient for current user", description = "Creates a new ingredient for the current user and returns it")
    public ResponseEntity<IngredientResponse> createIngredientForMe(
            @Parameter(description = "Ingredient details", required = true)
            @RequestBody IngredientRequest request) {
        return ResponseEntity.ok(ingredientService.createIngredientForMe(request));
    }

    @PostMapping("/users/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create an ingredient for a user", description = "Creates a new ingredient for a specific user and returns it")
    public ResponseEntity<IngredientResponse> createIngredientForUser(
            @Parameter(description = "Ingredient details", required = true)
            @RequestBody IngredientRequest request,
            @Parameter(description = "User ID", required = true)
            @PathVariable(ID) @Min(1) Long userId) {
        return ResponseEntity.ok(ingredientService.createIngredientForUser(request, userId));
    }

    @GetMapping("/users/me")
    @Operation(summary = "Get ingredients for current user", description = "Returns all ingredients for the current user")
    public ResponseEntity<List<IngredientResponse>> getIngredientsForMe() {
        return ResponseEntity.ok(ingredientService.getIngredientsForMe());
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Get ingredients for a user", description = "Returns all ingredients for a specific user")
    public ResponseEntity<List<IngredientResponse>> getIngredientsForUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable(ID) @Min(1) Long userId) {
        return ResponseEntity.ok(ingredientService.getIngredientsForUser(userId));
    }
}
