package com.rypsk.weeklymenucreator.api.service.impl;

import com.rypsk.weeklymenucreator.api.model.dto.DishRequest;
import com.rypsk.weeklymenucreator.api.model.dto.DishResponse;
import com.rypsk.weeklymenucreator.api.model.entity.Dish;
import com.rypsk.weeklymenucreator.api.model.entity.User;
import com.rypsk.weeklymenucreator.api.model.enumeration.DietType;
import com.rypsk.weeklymenucreator.api.model.enumeration.DishType;
import com.rypsk.weeklymenucreator.api.repository.DishRepository;
import com.rypsk.weeklymenucreator.api.repository.UserRepository;
import com.rypsk.weeklymenucreator.api.service.DishService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final UserRepository userRepository;

    public DishServiceImpl(DishRepository dishRepository, UserRepository userRepository) {
        this.dishRepository = dishRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public DishResponse getDish(Long id) {
        User user = getCurrentUser();
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found."));
        if (!dish.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot get this dish.");
        }
        return mapToResponse(dish);
    }

    @Override
    public DishResponse updateDish(Long id, DishRequest request) {
        User user = getCurrentUser();
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
        User user = getCurrentUser();
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
    public List<Dish> getDishesByDietTypeAndDishType(DietType dietType, DishType dishType, Long userId) {
        return dishRepository.findByDietTypeAndDishTypeAndUserId(dietType, dishType, userId);
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
