package com.rypsk.weeklymenucreator.api.service.impl;

import com.rypsk.weeklymenucreator.api.model.dto.*;
import com.rypsk.weeklymenucreator.api.model.entity.User;
import com.rypsk.weeklymenucreator.api.repository.UserRepository;
import com.rypsk.weeklymenucreator.api.service.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final WeeklyMenuService weeklyMenuService;
    private final DailyMenuService dailyMenuService;
    private final DishService dishService;
    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    public UserServiceImpl(UserRepository userRepository,
                           WeeklyMenuServiceImpl weeklyMenuService,
                           DailyMenuServiceImpl dailyMenuService,
                           DishServiceImpl dishService,
                           RecipeServiceImpl recipeService,
                           IngredientServiceImpl ingredientService) {
        this.userRepository = userRepository;
        this.weeklyMenuService = weeklyMenuService;
        this.dailyMenuService = dailyMenuService;
        this.dishService = dishService;
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public WeeklyMenuResponse autoGenerateWeeklyMenuForMe(AutoGenerateWeeklyMenuRequest request) {
        Long userId = getCurrentUser().getId();
        return weeklyMenuService.autoGenerateWeeklyMenuForUser(request, userId);
    }

    @Override
    public WeeklyMenuResponse autoGenerateWeeklyMenuForUser(AutoGenerateWeeklyMenuRequest request, Long userId) {
        return weeklyMenuService.autoGenerateWeeklyMenuForUser(request, userId);
    }

    @Override
    public WeeklyMenuResponse createWeeklyMenuForMe(WeeklyMenuRequest request) {
        Long userId = getCurrentUser().getId();
        return weeklyMenuService.createWeeklyMenuForUser(request, userId);
    }

    @Override
    public WeeklyMenuResponse createWeeklyMenuForUser(WeeklyMenuRequest request, Long userId) {
        return weeklyMenuService.createWeeklyMenuForUser(request, userId);
    }

    @Override
    public List<WeeklyMenuResponse> getWeeklyMenusForMe() {
        Long userId = getCurrentUser().getId();
        return weeklyMenuService.getWeeklyMenusForUser(userId);
    }

    @Override
    public List<WeeklyMenuResponse> getWeeklyMenusForUser(Long userId) {
        return weeklyMenuService.getWeeklyMenusForUser(userId);
    }

    @Override
    public DailyMenuResponse createDailyMenuForMe(DailyMenuRequest request) {
        Long userId = getCurrentUser().getId();
        return dailyMenuService.createDailyMenuForUser(request, userId);
    }

    @Override
    public DailyMenuResponse createDailyMenuForUser(DailyMenuRequest request, Long userId) {
        return dailyMenuService.createDailyMenuForUser(request, userId);
    }

    @Override
    public List<DailyMenuResponse> getDailyMenusForMe() {
        Long userId = getCurrentUser().getId();
        return dailyMenuService.getDailyMenusForUser(userId);
    }

    @Override
    public List<DailyMenuResponse> getDailyMenusForUser(Long userId) {
        return dailyMenuService.getDailyMenusForUser(userId);
    }

    @Override
    public DishResponse createDishForMe(DishRequest request) {
        Long userId = getCurrentUser().getId();
        return dishService.createDishForUser(request, userId);
    }

    @Override
    public DishResponse createDishForUser(DishRequest request, Long userId) {
        return dishService.createDishForUser(request, userId);
    }

    @Override
    public List<DishResponse> getDishesForMe() {
        Long userId = getCurrentUser().getId();
        return dishService.getDishesForUser(userId);
    }

    @Override
    public List<DishResponse> getDishesForUser(Long userId) {
        return dishService.getDishesForUser(userId);
    }

    @Override
    public RecipeResponse createRecipeForMe(RecipeRequest request) {
        Long userId = getCurrentUser().getId();
        return recipeService.createRecipeForUser(request, userId);
    }

    @Override
    public RecipeResponse createRecipeForUser(RecipeRequest request, Long userId) {
        return recipeService.createRecipeForUser(request, userId);
    }

    @Override
    public List<RecipeResponse> getRecipesForMe() {
        Long userId = getCurrentUser().getId();
        return recipeService.getRecipesForUser(userId);
    }

    @Override
    public List<RecipeResponse> getRecipesForUser(Long userId) {
        return recipeService.getRecipesForUser(userId);
    }

    @Override
    public List<RecipeResponse> getAvailableRecipesForMe() {
        Long userId = getCurrentUser().getId();
        return recipeService.getAvailableRecipesForUser(userId);
    }

    @Override
    public List<RecipeResponse> getAvailableRecipesForUser(Long userId) {
        return recipeService.getAvailableRecipesForUser(userId);
    }

    @Override
    public IngredientResponse createIngredientForMe(IngredientRequest request) {
        Long userId = getCurrentUser().getId();
        return ingredientService.createIngredientForUser(request, userId);
    }

    @Override
    public IngredientResponse createIngredientForUser(IngredientRequest request, Long userId) {
        return ingredientService.createIngredientForUser(request, userId);
    }

    @Override
    public List<IngredientResponse> getIngredientsForMe() {
        Long userId = getCurrentUser().getId();
        return ingredientService.getIngredientesForUser(userId);
    }

    @Override
    public List<IngredientResponse> getIngredientsForUser(Long userId) {
        return ingredientService.getIngredientesForUser(userId);
    }


}
