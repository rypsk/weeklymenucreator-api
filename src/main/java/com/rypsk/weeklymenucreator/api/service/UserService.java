package com.rypsk.weeklymenucreator.api.service;

import com.rypsk.weeklymenucreator.api.model.dto.*;
import jakarta.validation.constraints.Min;

import java.util.List;

public interface UserService {

    WeeklyMenuResponse autoGenerateWeeklyMenuForUser(AutoGenerateWeeklyMenuRequest request, Long userId);

    WeeklyMenuResponse autoGenerateWeeklyMenuForMe(AutoGenerateWeeklyMenuRequest request);

    WeeklyMenuResponse createWeeklyMenuForMe(WeeklyMenuRequest request);

    WeeklyMenuResponse createWeeklyMenuForUser(WeeklyMenuRequest request, @Min(1) Long userId);

    List<WeeklyMenuResponse> getWeeklyMenusForMe();

    List<WeeklyMenuResponse> getWeeklyMenusForUser(@Min(1) Long userId);

    DailyMenuResponse createDailyMenuForMe(DailyMenuRequest request);

    DailyMenuResponse createDailyMenuForUser(DailyMenuRequest request, @Min(1) Long userId);

    List<DailyMenuResponse> getDailyMenusForMe();

    List<DailyMenuResponse> getDailyMenusForUser(@Min(1) Long userId);

    DishResponse createDishForMe(DishRequest request);

    DishResponse createDishForUser(DishRequest request, @Min(1) Long userId);

    List<DishResponse> getDishesForMe();

    List<DishResponse> getDishesForUser(@Min(1) Long userId);

    RecipeResponse createRecipeForMe(RecipeRequest request);

    RecipeResponse createRecipeForUser(RecipeRequest request, @Min(1) Long userId);

    List<RecipeResponse> getRecipesForMe();

    List<RecipeResponse> getRecipesForUser(@Min(1) Long userId);

    IngredientResponse createIngredientForMe(IngredientRequest request);

    IngredientResponse createIngredientForUser(IngredientRequest request, @Min(1) Long userId);

    List<IngredientResponse> getIngredientsForMe();

    List<IngredientResponse> getIngredientsForUser(@Min(1) Long userId);

}
