package com.rypsk.weeklymenucreator.api.service.impl;

import com.rypsk.weeklymenucreator.api.model.dto.DishRequest;
import com.rypsk.weeklymenucreator.api.model.dto.DishResponse;
import com.rypsk.weeklymenucreator.api.model.entity.Dish;
import com.rypsk.weeklymenucreator.api.model.entity.User;
import com.rypsk.weeklymenucreator.api.model.enumeration.DietType;
import com.rypsk.weeklymenucreator.api.model.enumeration.DishType;
import com.rypsk.weeklymenucreator.api.model.enumeration.FoodType;
import com.rypsk.weeklymenucreator.api.repository.DishRepository;
import com.rypsk.weeklymenucreator.api.repository.UserRepository;
import com.rypsk.weeklymenucreator.api.service.DishService;
import com.rypsk.weeklymenucreator.api.service.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public DishServiceImpl(DishRepository dishRepository, UserRepository userRepository, UserService userService) {
        this.dishRepository = dishRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public DishResponse getDish(Long id) {
        User user = userService.getCurrentUser();
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found."));
        if (!dish.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot get this dish.");
        }
        return mapToResponse(dish);
    }

    @Override
    public DishResponse updateDish(Long id, DishRequest request) {
        User user = userService.getCurrentUser();
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found."));
        if (!dish.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot modify this dish.");
        }
        dish.setName(request.name());
        dish.setDescription(request.description());
        dish.setRecipe(request.recipe());
        dish.setFoodType(request.foodType());
        return mapToResponse(dishRepository.save(dish));
    }

    @Override
    public void deleteDish(Long id) {
        User user = userService.getCurrentUser();
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found"));
        if (!dish.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot delete this dish.");
        }
        dishRepository.delete(dish);
    }

    @Override
    public DishResponse createDishForUser(DishRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Dish dish = new Dish();
        dish.setName(request.name());
        dish.setDescription(request.description());
        dish.setRecipe(request.recipe());
        dish.setFoodType(request.foodType());
        dish.setUser(user);
        Dish savedDish = dishRepository.save(dish);
        return mapToResponse(savedDish);
    }

    @Override
    public List<DishResponse> getDishesForUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return dishRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<Dish> getDishesByDietTypeAndDishTypeAndFoodType(DietType dietType, DishType dishType, Long userId, FoodType foodType) {
        return dishRepository.findByDietTypeAndDishTypeAndUserIdAndFoodType(dietType, dishType, userId, foodType);
    }

    @Override
    public List<Dish> getDishesByDietTypeAndDishType(DietType dietType, DishType dishType, Long userId) {
        return dishRepository.findByDietTypeAndDishTypeAndUserId(dietType, dishType, userId);
    }

    @Override
    public List<Dish> getDishesByDishTypeAndFoodType(DishType dishType, Long userId, FoodType foodType) {
        return dishRepository.findByDishTypeAndUserIdAndFoodType(dishType, userId, foodType);
    }

    @Override
    public List<Dish> getDishesByDishType(DishType dishType, Long userId) {
        return dishRepository.findByDishTypeAndUserId(dishType, userId);
    }

    @Override
    public DishResponse createDishForMe(DishRequest request) {
        User user = userService.getCurrentUser();
        return createDishForUser(request, user.getId());
    }

    @Override
    public List<DishResponse> getDishesForMe() {
        User user = userService.getCurrentUser();
        return getDishesForUser(user.getId());
    }

    private DishResponse mapToResponse(Dish dish) {
        return new DishResponse(
                dish.getId(),
                dish.getName(),
                dish.getDescription(),
                dish.getRecipe(),
                dish.getFoodType(),
                dish.getUser().getId()
        );
    }
}
