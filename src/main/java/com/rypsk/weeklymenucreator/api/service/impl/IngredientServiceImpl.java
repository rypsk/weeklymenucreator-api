package com.rypsk.weeklymenucreator.api.service.impl;

import com.rypsk.weeklymenucreator.api.model.dto.IngredientRequest;
import com.rypsk.weeklymenucreator.api.model.dto.IngredientResponse;
import com.rypsk.weeklymenucreator.api.model.entity.Ingredient;
import com.rypsk.weeklymenucreator.api.model.entity.User;
import com.rypsk.weeklymenucreator.api.repository.IngredientRepository;
import com.rypsk.weeklymenucreator.api.repository.UserRepository;
import com.rypsk.weeklymenucreator.api.service.IngredientService;
import com.rypsk.weeklymenucreator.api.service.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public IngredientServiceImpl(IngredientRepository ingredientRepository, UserRepository userRepository, UserService userService) {
        this.ingredientRepository = ingredientRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public IngredientResponse getIngredient(Long id) {
        User user = userService.getCurrentUser();
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found."));
        if (!ingredient.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot get this ingredient.");
        }
        return mapToResponse(ingredient);
    }

    @Override
    public IngredientResponse updateIngredient(Long id, IngredientRequest request) {
        User user = userService.getCurrentUser();
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found."));
        if (!ingredient.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot get this ingredient.");
        }
        ingredient.setName(request.name());
        ingredient.setQuantity(request.quantity());
        return mapToResponse(ingredientRepository.save(ingredient));
    }

    @Override
    public void deleteIngredient(Long id) {
        User user = userService.getCurrentUser();
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found"));
        if (!ingredient.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot get this ingredient.");
        }
        ingredientRepository.delete(ingredient);
    }

    @Override
    public IngredientResponse createIngredientForUser(IngredientRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
        Ingredient ingredient = new Ingredient();
        ingredient.setName(request.name());
        ingredient.setQuantity(request.quantity());
        ingredient.setUser(user);
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        return mapToResponse(savedIngredient);
    }

    @Override
    public List<IngredientResponse> getIngredientsForUser(Long userId) {
        Collection<Ingredient> ingredients = ingredientRepository.findByUserId(userId);
        return ingredients.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public IngredientResponse createIngredientForMe(IngredientRequest request) {
        User user = userService.getCurrentUser();
        Ingredient ingredient = new Ingredient();
        ingredient.setName(request.name());
        ingredient.setQuantity(request.quantity());
        ingredient.setUser(user);
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        return mapToResponse(savedIngredient);
    }

    @Override
    public List<IngredientResponse> getIngredientsForMe() {
        User user = userService.getCurrentUser();
        Collection<Ingredient> ingredients = ingredientRepository.findByUserId(user.getId());
        return ingredients.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private IngredientResponse mapToResponse(Ingredient ingredient) {
        return new IngredientResponse(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getQuantity(),
                ingredient.getUser().getId()
        );
    }
}
