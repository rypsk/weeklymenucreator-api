package com.rypsk.weeklymenucreator.api.service.impl;

import com.rypsk.weeklymenucreator.api.model.dto.DailyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.DailyMenuResponse;
import com.rypsk.weeklymenucreator.api.model.entity.DailyMenu;
import com.rypsk.weeklymenucreator.api.model.entity.Dish;
import com.rypsk.weeklymenucreator.api.model.entity.User;
import com.rypsk.weeklymenucreator.api.repository.DailyMenuRepository;
import com.rypsk.weeklymenucreator.api.repository.DishRepository;
import com.rypsk.weeklymenucreator.api.repository.UserRepository;
import com.rypsk.weeklymenucreator.api.service.DailyMenuService;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DailyMenuServiceImpl implements DailyMenuService {

    private final DailyMenuRepository dailyMenuRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;

    public DailyMenuServiceImpl(DailyMenuRepository dailyMenuRepository, UserRepository userRepository, DishRepository dishRepository) {
        this.dailyMenuRepository = dailyMenuRepository;
        this.userRepository = userRepository;
        this.dishRepository = dishRepository;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public DailyMenuResponse getDailyMenu(Long id) {
        User user = getCurrentUser();
        DailyMenu dailyMenu = dailyMenuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Daily menu not found"));
        if (!dailyMenu.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot get this dailyMenu.");
        }
        return mapToResponse(dailyMenu);
    }

    @Override
    @Transactional
    public DailyMenuResponse updateDailyMenu(Long id, DailyMenuRequest request) {
        User user = getCurrentUser();
        DailyMenu dailyMenu = dailyMenuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Daily menu not found"));
        if (!dailyMenu.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot modify this dailyMenu.");
        }
        List<Dish> dishes = dishRepository.findAllById(request.dishIds());
        dailyMenu.setDayOfWeek(request.dayOfWeek());
        dailyMenu.setDishes(dishes);
        DailyMenu updatedMenu = dailyMenuRepository.save(dailyMenu);
        return mapToResponse(updatedMenu);
    }

    @Override
    public void deleteDailyMenu(Long id) {
        User user = getCurrentUser();
        DailyMenu dailyMenu = dailyMenuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found"));
        if (!dailyMenu.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot delete this dailyMenu.");
        }
        dailyMenuRepository.delete(dailyMenu);
    }

    @Override
    public DailyMenuResponse createDailyMenuForUser(DailyMenuRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<Dish> dishes = dishRepository.findAllById(request.dishIds());
        DailyMenu dailyMenu = new DailyMenu();
        dailyMenu.setDayOfWeek(request.dayOfWeek());
        dailyMenu.setDishes(dishes);
        dailyMenu.setUser(user);
        DailyMenu savedMenu = dailyMenuRepository.save(dailyMenu);
        return mapToResponse(savedMenu);
    }

    @Override
    public List<DailyMenuResponse> getDailyMenusForUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return dailyMenuRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private DailyMenuResponse mapToResponse(DailyMenu dailyMenu) {
        return new DailyMenuResponse(
                dailyMenu.getId(),
                dailyMenu.getUser().getId(),
                dailyMenu.getDayOfWeek(),
                dailyMenu.getDishes()
        );
    }
}
