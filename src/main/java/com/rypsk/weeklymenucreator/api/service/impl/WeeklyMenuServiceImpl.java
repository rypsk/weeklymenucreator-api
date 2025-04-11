package com.rypsk.weeklymenucreator.api.service.impl;

import com.rypsk.weeklymenucreator.api.model.dto.AutoGenerateWeeklyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuResponse;
import com.rypsk.weeklymenucreator.api.model.entity.DailyMenu;
import com.rypsk.weeklymenucreator.api.model.entity.Dish;
import com.rypsk.weeklymenucreator.api.model.entity.User;
import com.rypsk.weeklymenucreator.api.model.entity.WeeklyMenu;
import com.rypsk.weeklymenucreator.api.model.enumeration.DietType;
import com.rypsk.weeklymenucreator.api.model.enumeration.DishType;
import com.rypsk.weeklymenucreator.api.repository.UserRepository;
import com.rypsk.weeklymenucreator.api.repository.WeeklyMenuRepository;
import com.rypsk.weeklymenucreator.api.service.WeeklyMenuService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WeeklyMenuServiceImpl implements WeeklyMenuService {

    private final WeeklyMenuRepository weeklyMenuRepository;
    private final UserRepository userRepository;
    private final DishServiceImpl dishService;

    public WeeklyMenuServiceImpl(WeeklyMenuRepository weeklyMenuRepository, UserRepository userRepository, DishServiceImpl dishService) {
        this.weeklyMenuRepository = weeklyMenuRepository;
        this.userRepository = userRepository;
        this.dishService = dishService;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public WeeklyMenuResponse getWeeklyMenu(Long id) {
        User user = getCurrentUser();
        WeeklyMenu weeklyMenu = weeklyMenuRepository.findById(id).orElseThrow(() -> new RuntimeException("Weekly menu not found."));
        if (!weeklyMenu.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot get this weeklyMenu.");
        }
        return mapToResponse(weeklyMenu);
    }

    @Override
    @Transactional
    public WeeklyMenuResponse updateWeeklyMenu(Long id, WeeklyMenuRequest weeklyMenuRequest) {
        User user = getCurrentUser();
        WeeklyMenu weeklyMenu = weeklyMenuRepository.findById(id).orElseThrow(() -> new RuntimeException("Weekly menu not found."));
        if (!weeklyMenu.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot modify this weeklyMenu.");
        }
        weeklyMenu.setStartDate(weeklyMenuRequest.startDate());
        weeklyMenu.setEndDate(weeklyMenuRequest.endDate());
        weeklyMenu.setDailyMenus(weeklyMenuRequest.dailyMenus());

        WeeklyMenu updatedMenu = weeklyMenuRepository.save(weeklyMenu);
        return mapToResponse(updatedMenu);
    }

    @Override
    public void deleteWeeklyMenu(Long id) {
        User user = getCurrentUser();
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
        WeeklyMenu weeklyMenu = new WeeklyMenu();
        weeklyMenu.setUser(user);
        weeklyMenu.setStartDate(request.startDate());
        weeklyMenu.setEndDate(request.endDate());
        weeklyMenu.setDailyMenus(request.dailyMenus());
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
    public WeeklyMenuResponse autoGenerateWeeklyMenuForUser(AutoGenerateWeeklyMenuRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        LocalDate startDate = request.startDate();
        LocalDate endDate = request.endDate();
        DietType dietType = request.dietType();
        int weekMenuDays = startDate.until(endDate).getDays();
        Set<DishType> dishTypes = request.dishTypes();

        WeeklyMenu weeklyMenu = new WeeklyMenu();
        List<DailyMenu> dailyMenus= new ArrayList<>();

        for (int i = 0; i < weekMenuDays; i++) {
            DailyMenu dailyMenu = new DailyMenu();
            LocalDate today = startDate.plusDays(i);
            dailyMenu.setDate(today);

            List<Dish> dishes = getDishes(userId, dishTypes, dietType);
            dailyMenu.setDishes(dishes);
            dailyMenus.add(dailyMenu);
        }

        weeklyMenu.setDailyMenus(dailyMenus);
        weeklyMenu.setUser(user);
        weeklyMenu.setStartDate(startDate);
        weeklyMenu.setEndDate(endDate);

        WeeklyMenu savedWeeklyMenu = weeklyMenuRepository.save(weeklyMenu);

        return mapToResponse(savedWeeklyMenu);
    }

    private List<Dish> getDishes(Long userId, Set<DishType> dishTypes, DietType dietType) {
        List<Dish> dishes = new ArrayList<>();
        if(dishTypes.contains(DishType.BREAKFAST)){
            Dish breakfastDish = getDishByType(DishType.BREAKFAST, dietType, userId);
            dishes.add(breakfastDish);
        }
        if(dishTypes.contains(DishType.LUNCH)){
            Dish lunchDish = getDishByType(DishType.LUNCH, dietType, userId);
            dishes.add(lunchDish);
        }
        if(dishTypes.contains(DishType.DINNER)){
            Dish dinnerDish = getDishByType(DishType.DINNER, dietType, userId);
            dishes.add(dinnerDish);
        }
        return dishes;
    }

    private Dish getDishByType(DishType dishType, DietType dietType, Long userId) {
        List<Dish> availableDishes = dishService.getDishesByDietTypeAndDishType(dietType, dishType, userId);
        return availableDishes.get(new Random().nextInt(availableDishes.size()));
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
