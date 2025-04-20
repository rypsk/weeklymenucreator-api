package com.rypsk.weeklymenucreator.api.service;

import com.rypsk.weeklymenucreator.api.model.dto.DishRequest;
import com.rypsk.weeklymenucreator.api.model.dto.DishResponse;
import com.rypsk.weeklymenucreator.api.model.entity.Dish;
import com.rypsk.weeklymenucreator.api.model.enumeration.DietType;
import com.rypsk.weeklymenucreator.api.model.enumeration.DishType;
import com.rypsk.weeklymenucreator.api.model.enumeration.FoodType;
import jakarta.validation.constraints.Min;

import java.util.List;

public interface DishService {
    DishResponse getDish(@Min(1) Long id);

    DishResponse updateDish(@Min(1) Long id, DishRequest request);

    void deleteDish(@Min(1) Long id);

    DishResponse createDishForMe(DishRequest request);

    DishResponse createDishForUser(DishRequest request, Long userId);

    List<DishResponse> getDishesForMe();

    List<DishResponse> getDishesForUser(Long userId);

    List<Dish> getDishesByDietTypeAndDishType(DietType dietType, DishType dishType, Long userId);

    List<Dish> getDishesByDishType(DishType dishType, Long userId);

    List<Dish> getDishesByDishTypeAndFoodType(DishType dishType, Long userId, FoodType foodType);

    List<Dish> getDishesByDietTypeAndDishTypeAndFoodType(DietType dietType, DishType dishType, Long userId, FoodType foodType);
}
