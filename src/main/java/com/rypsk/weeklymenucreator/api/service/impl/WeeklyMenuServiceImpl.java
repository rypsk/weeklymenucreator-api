package com.rypsk.weeklymenucreator.api.service.impl;

import com.rypsk.weeklymenucreator.api.model.dto.AutoGenerateWeeklyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuResponse;
import com.rypsk.weeklymenucreator.api.model.entity.DailyMenu;
import com.rypsk.weeklymenucreator.api.model.entity.Dish;
import com.rypsk.weeklymenucreator.api.model.entity.User;
import com.rypsk.weeklymenucreator.api.model.entity.WeeklyMenu;
import com.rypsk.weeklymenucreator.api.model.enumeration.DayOfWeek;
import com.rypsk.weeklymenucreator.api.model.enumeration.DietType;
import com.rypsk.weeklymenucreator.api.model.enumeration.DishType;
import com.rypsk.weeklymenucreator.api.model.enumeration.FoodType;
import com.rypsk.weeklymenucreator.api.repository.DailyMenuRepository;
import com.rypsk.weeklymenucreator.api.repository.UserRepository;
import com.rypsk.weeklymenucreator.api.repository.WeeklyMenuRepository;
import com.rypsk.weeklymenucreator.api.service.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WeeklyMenuServiceImpl implements WeeklyMenuService {

    private final WeeklyMenuRepository weeklyMenuRepository;
    private final UserRepository userRepository;
    private final DishService dishService;
    private final EmailService emailService;
    private final DailyMenuRepository dailyMenuRepository;
    private final UserService userService;
    private final ExportService exportService;

    public WeeklyMenuServiceImpl(WeeklyMenuRepository weeklyMenuRepository, UserRepository userRepository, DishServiceImpl dishService, EmailService emailService, DailyMenuRepository dailyMenuRepository, UserService userService, ExportService exportService) {
        this.weeklyMenuRepository = weeklyMenuRepository;
        this.userRepository = userRepository;
        this.dishService = dishService;
        this.emailService = emailService;
        this.dailyMenuRepository = dailyMenuRepository;
        this.userService = userService;
        this.exportService = exportService;
    }

    @Override
    public WeeklyMenuResponse getWeeklyMenu(Long id) {
        User user = userService.getCurrentUser();
        WeeklyMenu weeklyMenu = weeklyMenuRepository.findById(id).orElseThrow(() -> new RuntimeException("Weekly menu not found."));
        if (!weeklyMenu.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot get this weeklyMenu.");
        }
        return mapToResponse(weeklyMenu);
    }

    @Override
    @Transactional
    public WeeklyMenuResponse updateWeeklyMenu(Long id, WeeklyMenuRequest request) {
        User user = userService.getCurrentUser();
        WeeklyMenu weeklyMenu = weeklyMenuRepository.findById(id).orElseThrow(() -> new RuntimeException("Weekly menu not found."));
        if (!weeklyMenu.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot modify this weeklyMenu.");
        }
        List<DailyMenu> dailyMenus = dailyMenuRepository.findAllById(request.dailyMenuIds());
        weeklyMenu.setStartDate(request.startDate());
        weeklyMenu.setEndDate(request.endDate());
        weeklyMenu.setDailyMenus(dailyMenus);

        WeeklyMenu updatedMenu = weeklyMenuRepository.save(weeklyMenu);
        return mapToResponse(updatedMenu);
    }

    @Override
    public void deleteWeeklyMenu(Long id) {
        User user = userService.getCurrentUser();
        WeeklyMenu weeklyMenu = weeklyMenuRepository.findById(id).orElseThrow(() -> new RuntimeException("Dish not found"));
        if (!weeklyMenu.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot delete this weeklyMenu.");
        }
        weeklyMenuRepository.delete(weeklyMenu);
    }

    @Override
    @Transactional
    public WeeklyMenuResponse createWeeklyMenuForUser(WeeklyMenuRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<DailyMenu> dailyMenus = dailyMenuRepository.findAllById(request.dailyMenuIds());
        WeeklyMenu weeklyMenu = new WeeklyMenu();
        weeklyMenu.setUser(user);
        weeklyMenu.setStartDate(request.startDate());
        weeklyMenu.setEndDate(request.endDate());
        weeklyMenu.setDailyMenus(dailyMenus);
        WeeklyMenu saved = weeklyMenuRepository.save(weeklyMenu);
        return mapToResponse(saved);
    }

    public List<WeeklyMenuResponse> getWeeklyMenusForUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Collection<WeeklyMenu> weeklyMenus = weeklyMenuRepository.findByUserId(userId);
        return weeklyMenus.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public WeeklyMenuResponse createWeeklyMenuForMe(WeeklyMenuRequest request) {
        User user = userService.getCurrentUser();
        return createWeeklyMenuForUser(request, user.getId());
    }

    @Override
    public List<WeeklyMenuResponse> getWeeklyMenusForMe() {
        User user = userService.getCurrentUser();
        return getWeeklyMenusForUser(user.getId());
    }

    @Override
    public WeeklyMenuResponse autoGenerateWeeklyMenuForMe(AutoGenerateWeeklyMenuRequest request) {
        User user = userService.getCurrentUser();
        return autoGenerateWeeklyMenuForUser(request, user.getId());
    }

    @Override
    public WeeklyMenuResponse autoGenerateWeeklyMenuForUser(AutoGenerateWeeklyMenuRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        boolean allowRepeat = request.allowRepeat();

        LocalDate startDate = request.startDate();
        LocalDate endDate = request.endDate();

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
        }
        DietType dietType = request.dietType();
        int weekMenuDays = startDate.until(endDate).getDays() + 1;
        int totalMeals = weekMenuDays * request.dishTypes().size();

        if (request.preferences() != null) {
            int requestedMeals = request.preferences().values().stream().mapToInt(Integer::intValue).sum();
            if (requestedMeals > totalMeals) {
                throw new IllegalArgumentException("The total number of preferred meals (" + requestedMeals +
                        ") exceeds the number of available meals (" + totalMeals + ").");
            }
        }
        Set<DishType> dishTypes = request.dishTypes();

        List<FoodType> foodPreferences = new ArrayList<>();
        assert request.preferences() != null;
        request.preferences().forEach((foodType, count) -> {
            for (int i = 0; i < count; i++) {
                foodPreferences.add(foodType);
            }
        });

        while (foodPreferences.size() < weekMenuDays) {
            foodPreferences.addAll(request.preferences().keySet());
        }

        Collections.shuffle(foodPreferences);

        WeeklyMenu weeklyMenu = new WeeklyMenu();
        List<DailyMenu> dailyMenus = new ArrayList<>();

        for (int i = 0; i < weekMenuDays; i++) {
            DailyMenu dailyMenu = new DailyMenu();
            LocalDate today = startDate.plusDays(i);
            dailyMenu.setDate(today);
            dailyMenu.setDayOfWeek(DayOfWeek.valueOf(today.getDayOfWeek().name()));
            FoodType foodType = foodPreferences.get(i);
            List<Dish> dishes = getDishes(userId, dishTypes, dietType, foodType, allowRepeat);
            dailyMenu.setDishes(dishes);
            dailyMenus.add(dailyMenu);
            dailyMenu.setUser(user);
        }

        weeklyMenu.setDailyMenus(dailyMenus);
        weeklyMenu.setUser(user);
        weeklyMenu.setStartDate(startDate);
        weeklyMenu.setEndDate(endDate);

        WeeklyMenu savedWeeklyMenu = weeklyMenuRepository.save(weeklyMenu);

        return mapToResponse(savedWeeklyMenu);
    }

    @Override
    public WeeklyMenuResponse autoGenerateWeeklyMenuForUser(Long userId) {
        LocalDate startDate = LocalDate.now().with(java.time.DayOfWeek.MONDAY);
        LocalDate endDate = startDate.plusDays(7);
        boolean allowRepeat = true;
        Set<DishType> dishTypes = new HashSet<>();
        dishTypes.add(DishType.BREAKFAST);
        dishTypes.add(DishType.LUNCH);
        dishTypes.add(DishType.DINNER);
        AutoGenerateWeeklyMenuRequest request = new AutoGenerateWeeklyMenuRequest(startDate, endDate, dishTypes, null, null, allowRepeat);
        return autoGenerateWeeklyMenuForUser(request, userId);
    }

    private List<Dish> getDishes(Long userId, Set<DishType> dishTypes, DietType dietType, FoodType foodType, boolean allowRepeat) {
        List<Dish> dishes = new ArrayList<>();
        Set<Long> usedDishIds = new HashSet<>();

        for (DishType dishType : dishTypes) {
            if (dishType == DishType.BREAKFAST || dishType == DishType.LUNCH || dishType == DishType.DINNER) {
                Dish dish = getDishForType(dishType, userId, dietType, foodType, allowRepeat, usedDishIds);
                if (dish != null) {
                    dishes.add(dish);
                    if (!allowRepeat) {
                        usedDishIds.add(dish.getId());
                    }
                }
            }
        }

        return dishes;
    }

    private Dish getDishForType(DishType dishType, Long userId, DietType dietType, FoodType foodType, boolean allowRepeat, Set<Long> usedDishIds) {
        if (dietType != null) {
            return foodType != null
                    ? getDishByDietTypeAndDishTypeAndFoodType(dishType, dietType, userId, foodType, allowRepeat, usedDishIds)
                    : getDishByDietTypeAndDishType(dishType, dietType, userId, allowRepeat, usedDishIds);
        } else {
            return foodType != null
                    ? getDishByDishTypeAndFoodType(dishType, userId, foodType, allowRepeat, usedDishIds)
                    : getDishByDishType(dishType, userId, allowRepeat, usedDishIds);
        }
    }

    private Dish getDishByDietTypeAndDishTypeAndFoodType(DishType dishType, DietType dietType, Long userId, FoodType foodType, boolean allowRepeat, Set<Long> usedDishIds) {
        List<Dish> availableDishes = dishService.getDishesByDietTypeAndDishTypeAndFoodType(dietType, dishType, userId, foodType);
        if (availableDishes.isEmpty()) {
            throw new IllegalStateException("No dishes found for " + dishType + " and " + dietType + " for user " + userId);
        }
        return filterAndSelect(availableDishes, allowRepeat, usedDishIds, dishType, userId);
    }

    private Dish getDishByDietTypeAndDishType(DishType dishType, DietType dietType, Long userId, boolean allowRepeat, Set<Long> usedDishIds) {
        List<Dish> availableDishes = dishService.getDishesByDietTypeAndDishType(dietType, dishType, userId);
        if (availableDishes.isEmpty()) {
            throw new IllegalStateException("No dishes found for " + dishType + " and " + dietType + " for user " + userId);
        }
        return filterAndSelect(availableDishes, allowRepeat, usedDishIds, dishType, userId);
    }

    private Dish getDishByDishTypeAndFoodType(DishType dishType, Long userId, FoodType foodType, boolean allowRepeat, Set<Long> usedDishIds) {
        List<Dish> availableDishes = dishService.getDishesByDishTypeAndFoodType(dishType, userId, foodType);
        if (availableDishes.isEmpty()) {
            throw new IllegalStateException("No dishes found for " + dishType + " for user " + userId);
        }
        return filterAndSelect(availableDishes, allowRepeat, usedDishIds, dishType, userId);
    }

    private Dish getDishByDishType(DishType dishType, Long userId, boolean allowRepeat, Set<Long> usedDishIds) {
        List<Dish> availableDishes = dishService.getDishesByDishType(dishType, userId);
        if (availableDishes.isEmpty()) {
            throw new IllegalStateException("No dishes found for " + dishType + " for user " + userId);
        }
        return filterAndSelect(availableDishes, allowRepeat, usedDishIds, dishType, userId);
    }

    private Dish filterAndSelect(List<Dish> dishes, boolean allowRepeat, Set<Long> usedDishIds, DishType dishType, Long userId) {
        List<Dish> filtered = allowRepeat
                ? dishes
                : dishes.stream().filter(d -> !usedDishIds.contains(d.getId())).toList();

        if (filtered.isEmpty()) {
            throw new IllegalStateException("No available dishes for " + dishType + " with current filters for user " + userId);
        }

        return filtered.get(new Random().nextInt(filtered.size()));
    }


    private WeeklyMenuResponse mapToResponse(WeeklyMenu weeklyMenu) {
        return new WeeklyMenuResponse(
                weeklyMenu.getId(),
                weeklyMenu.getUser().getId(),
                weeklyMenu.getStartDate(),
                weeklyMenu.getEndDate(),
                weeklyMenu.getDailyMenus()
        );
    }


}
