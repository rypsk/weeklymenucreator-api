package com.rypsk.weeklymenucreator.api.controller;

import com.rypsk.weeklymenucreator.api.model.dto.DishRequest;
import com.rypsk.weeklymenucreator.api.model.dto.DishResponse;
import com.rypsk.weeklymenucreator.api.service.DishService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dish")
public class DishController {

    private static final String ID = "id";
    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishResponse> getDishById(@PathVariable @Min(1) Long id) {
        DishResponse dish = dishService.getDishById(id);
        return ResponseEntity.ok(dish);
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<DishResponse> updateDish(@PathVariable @Min(1) Long id, @RequestBody DishRequest request) {
        DishResponse updatedDish = dishService.updateDish(id, request);
        return ResponseEntity.ok(updatedDish);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable @Min(1) Long id) {
        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PostMapping("/user/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DishResponse> createDishForUserId(@RequestBody DishRequest request, @PathVariable(ID) @Min(1) Long userId) {
        DishResponse dish = dishService.createDishForUserId(request);
        return ResponseEntity.ok(dish);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<DishResponse>> getDishesByUserId(@PathVariable(ID) @Min(1) Long userId) {
        List<DishResponse> dishes = dishService.getDishesByUserId(userId);
        return ResponseEntity.ok(dishes);
    }
}
