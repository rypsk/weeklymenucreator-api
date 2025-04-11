package com.rypsk.weeklymenucreator.api.controller;

import com.rypsk.weeklymenucreator.api.model.dto.*;
import com.rypsk.weeklymenucreator.api.service.UserService;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final String ID = "id";
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/me/weekly-menus/auto-generate")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<WeeklyMenuResponse> autoGenerateWeeklyMenu(@RequestBody AutoGenerateWeeklyMenuRequest request) {
        WeeklyMenuResponse generatedMenu = userService.autoGenerateWeeklyMenuForMe(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(generatedMenu);
    }

    @PostMapping("/me/weekly-menus")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<WeeklyMenuResponse> createWeeklyMenuForMe(@RequestBody WeeklyMenuRequest request) {
        return ResponseEntity.ok(userService.createWeeklyMenuForMe(request));
    }

    @PostMapping("/{id}/weekly-menus")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<WeeklyMenuResponse> createWeeklyMenuForUser(@RequestBody WeeklyMenuRequest request, @PathVariable(ID) @Min(1) Long userId) {
        return ResponseEntity.ok(userService.createWeeklyMenuForUser(request, userId));
    }

    @GetMapping("/me/weekly-menus")
    public ResponseEntity<List<WeeklyMenuResponse>> getWeeklyMenusForMe() {
        return ResponseEntity.ok(userService.getWeeklyMenusForMe());
    }

    @GetMapping("/{id}/weekly-menus")
    public ResponseEntity<List<WeeklyMenuResponse>> getWeeklyMenusForUser(@PathVariable(ID) @Min(1) Long userId) {
        return ResponseEntity.ok(userService.getWeeklyMenusForUser(userId));
    }


    @PostMapping("/me/daily-menus")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DailyMenuResponse> createDailyMenuForMe(@RequestBody DailyMenuRequest request) {
        return ResponseEntity.ok(userService.createDailyMenuForMe(request));
    }

    @PostMapping("/{id}/daily-menus")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DailyMenuResponse> createDailyMenuForUser(@RequestBody DailyMenuRequest request, @PathVariable(ID) @Min(1) Long userId) {
        return ResponseEntity.ok(userService.createDailyMenuForUser(request, userId));
    }

    @GetMapping("/me/daily-menus")
    public ResponseEntity<List<DailyMenuResponse>> getDailyMenusForMe() {
        return ResponseEntity.ok(userService.getDailyMenusForMe());
    }

    @GetMapping("/{id}/daily-menus")
    public ResponseEntity<List<DailyMenuResponse>> getDailyMenusForUser(@PathVariable(ID) @Min(1) Long userId) {
        return ResponseEntity.ok(userService.getDailyMenusForUser(userId));
    }


    @PostMapping("/me/dishes")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DishResponse> createDishForMe(@RequestBody DishRequest request) {
        return ResponseEntity.ok(userService.createDishForMe(request));
    }

    @PostMapping("/{id}/dishes")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DishResponse> createDishForUser(@RequestBody DishRequest request, @PathVariable(ID) @Min(1) Long userId) {
        return ResponseEntity.ok(userService.createDishForUser(request, userId));
    }

    @GetMapping("/me/dishes")
    public ResponseEntity<List<DishResponse>> getDishesForMe() {
        return ResponseEntity.ok(userService.getDishesForMe());
    }

    @GetMapping("/{id}/dishes")
    public ResponseEntity<List<DishResponse>> getDishesForUser(@PathVariable(ID) @Min(1) Long userId) {
        return ResponseEntity.ok(userService.getDishesForUser(userId));
    }

    @PostMapping("/me/recipes")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RecipeResponse> createRecipeForMe(@RequestBody RecipeRequest request) {
        return ResponseEntity.ok(userService.createRecipeForMe(request));
    }

    @PostMapping("/{id}/recipes")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<List<RecipeResponse>> createRecipeForUser(@RequestBody RecipeRequest request, @PathVariable(ID) @Min(1) Long userId) {
        return ResponseEntity.ok(Collections.singletonList(userService.createRecipeForUser(request, userId)));
    }

    @GetMapping("/me/recipes")
    public ResponseEntity<List<RecipeResponse>> getRecipesForMe() {
        return ResponseEntity.ok(userService.getRecipesForMe());
    }

    @GetMapping("/{id}/recipes")
    public ResponseEntity<List<RecipeResponse>> getRecipesForUser(@PathVariable(ID) @Min(1) Long userId) {
        return ResponseEntity.ok(userService.getRecipesForUser(userId));
    }

    @PostMapping("/me/ingredients")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<IngredientResponse> createIngredientForMe(@RequestBody IngredientRequest request) {
        return ResponseEntity.ok(userService.createIngredientForMe(request));
    }

    @PostMapping("/{id}/ingredients")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<IngredientResponse> createIngredientForUser(@RequestBody IngredientRequest request, @PathVariable(ID) @Min(1) Long userId) {
        return ResponseEntity.ok(userService.createIngredientForUser(request, userId));
    }

    @GetMapping("/me/ingredients")
    public ResponseEntity<List<IngredientResponse>> getIngredientsForMe() {
        return ResponseEntity.ok(userService.getIngredientsForMe());
    }

    @GetMapping("/{id}/ingredients")
    public ResponseEntity<List<IngredientResponse>> getIngredientsForUser(@PathVariable(ID) @Min(1) Long userId) {
        return ResponseEntity.ok(userService.getIngredientsForUser(userId));
    }

}
