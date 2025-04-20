package com.rypsk.weeklymenucreator.api.controller;

import com.rypsk.weeklymenucreator.api.model.dto.DishRequest;
import com.rypsk.weeklymenucreator.api.model.dto.DishResponse;
import com.rypsk.weeklymenucreator.api.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dishes")
@Tag(name = "Dish", description = "Dish management APIs")
public class DishController {

    private static final String ID = "id";
    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a dish by ID", description = "Returns a dish by its ID")
    public ResponseEntity<DishResponse> getDish(
            @Parameter(description = "Dish ID", required = true)
            @PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(dishService.getDish(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a dish", description = "Updates a dish and returns it")
    public ResponseEntity<DishResponse> updateDish(
            @Parameter(description = "Dish ID", required = true)
            @PathVariable @Min(1) Long id,
            @Parameter(description = "Updated dish details", required = true)
            @RequestBody DishRequest request) {
        return ResponseEntity.ok(dishService.updateDish(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a dish", description = "Deletes a dish by its ID")
    public ResponseEntity<Void> deleteDish(
            @Parameter(description = "Dish ID", required = true)
            @PathVariable @Min(1) Long id) {
        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/me")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a dish for current user", description = "Creates a new dish for the current user and returns it")
    public ResponseEntity<DishResponse> createDishForMe(
            @Parameter(description = "Dish details", required = true)
            @RequestBody DishRequest request) {
        return ResponseEntity.ok(dishService.createDishForMe(request));
    }

    @PostMapping("/users/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a dish for a user", description = "Creates a new dish for a specific user and returns it")
    public ResponseEntity<DishResponse> createDishForUser(
            @Parameter(description = "Dish details", required = true)
            @RequestBody DishRequest request,
            @Parameter(description = "User ID", required = true)
            @PathVariable(ID) @Min(1) Long userId) {
        return ResponseEntity.ok(dishService.createDishForUser(request, userId));
    }

    @GetMapping("/me")
    @Operation(summary = "Get dishes for current user", description = "Returns all dishes for the current user")
    public ResponseEntity<List<DishResponse>> getDishesForMe() {
        return ResponseEntity.ok(dishService.getDishesForMe());
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Get dishes for a user", description = "Returns all dishes for a specific user")
    public ResponseEntity<List<DishResponse>> getDishesForUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable(ID) @Min(1) Long userId) {
        return ResponseEntity.ok(dishService.getDishesForUser(userId));
    }

}
