package com.rypsk.weeklymenucreator.api.controller;

import com.rypsk.weeklymenucreator.api.model.dto.IngredientRequest;
import com.rypsk.weeklymenucreator.api.model.dto.IngredientResponse;
import com.rypsk.weeklymenucreator.api.service.IngredientService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ingredient")
public class IngredientController {

    private static final String ID = "id";
    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientResponse> getIngredient(@PathVariable @Min(1) Long id){
        return ResponseEntity.ok(ingredientService.getIngredient(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientResponse> updateIngredient(@PathVariable @Min(1) Long id, @RequestBody IngredientRequest request){
        return ResponseEntity.ok(ingredientService.updateIngredient(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable @Min(1) Long id){
        ingredientService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PostMapping("/user/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<IngredientResponse> createIngredientForUser(@RequestBody IngredientRequest request, @PathVariable(ID) @Min(1) Long userId) {
        return ResponseEntity.ok(ingredientService.createIngredientForUser(request, userId));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<IngredientResponse>> getIngredientsByUser(@PathVariable(ID) @Min(1) Long userId) {
        return ResponseEntity.ok(ingredientService.getIngredientsByUser(userId));
    }
}
