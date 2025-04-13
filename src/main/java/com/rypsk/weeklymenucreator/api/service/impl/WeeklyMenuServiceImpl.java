package com.rypsk.weeklymenucreator.api.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
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
import com.rypsk.weeklymenucreator.api.repository.UserRepository;
import com.rypsk.weeklymenucreator.api.repository.WeeklyMenuRepository;
import com.rypsk.weeklymenucreator.api.service.WeeklyMenuService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
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
        int weekMenuDays = startDate.until(endDate).getDays()+1;
        Set<DishType> dishTypes = request.dishTypes();

        WeeklyMenu weeklyMenu = new WeeklyMenu();
        List<DailyMenu> dailyMenus= new ArrayList<>();

        for (int i = 0; i < weekMenuDays; i++) {
            DailyMenu dailyMenu = new DailyMenu();
            LocalDate today = startDate.plusDays(i);
            dailyMenu.setDate(today);
            dailyMenu.setDayOfWeek(DayOfWeek.valueOf(today.getDayOfWeek().name()));
            List<Dish> dishes = getDishes(userId, dishTypes, dietType);
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
        if (availableDishes.isEmpty()) {
            throw new IllegalStateException("No dishes found for " + dishType + " and " + dietType + " for user " + userId);
        }
        return availableDishes.get(new Random().nextInt(availableDishes.size()));
    }

    @Override
    public ResponseEntity<byte[]> exportWeeklyMenu(Long id, String format) {
        User user = getCurrentUser();
        WeeklyMenu weeklyMenu = weeklyMenuRepository.findById(id).orElseThrow(() -> new RuntimeException("Weekly menu not found."));
        if (!weeklyMenu.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot export this weeklyMenu.");
        }
        byte[] content;
        MediaType mediaType;
        String filename = "weekly-menu." + format.toLowerCase();

        switch (format.toLowerCase()) {
            case "pdf" -> {
                content = exportToPdf(weeklyMenu);
                mediaType = MediaType.APPLICATION_PDF;
            }
            case "excel", "xlsx" -> {
                content = exportToExcel(weeklyMenu);
                mediaType = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            }
            default -> throw new IllegalArgumentException("Unsupported format: " + format);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setContentDispositionFormData("attachment", filename);

        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }

    private byte[] exportToPdf(WeeklyMenu weeklyMenu) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            document.add(new Paragraph("Weekly Menu: " + weeklyMenu.getId()));
            document.add(new Paragraph("Start Date: " + weeklyMenu.getStartDate()));
            document.add(new Paragraph("End Date: " + weeklyMenu.getEndDate()));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(weeklyMenu.getDailyMenus().size() + 1);

            table.addCell("Dish Type");
            for (DailyMenu dailyMenu : weeklyMenu.getDailyMenus()) {
                table.addCell(dailyMenu.getDayOfWeek().toString());
            }

            Set<String> dishTypes = new HashSet<>();
            for (DailyMenu dailyMenu : weeklyMenu.getDailyMenus()) {
                for (Dish dish : dailyMenu.getDishes()) {
                    dishTypes.add(String.valueOf(dish.getDishType()));
                }
            }

            for (String dishType : dishTypes) {
                table.addCell(dishType);
                for (DailyMenu dailyMenu : weeklyMenu.getDailyMenus()) {
                    boolean foundDish = false;
                    for (Dish dish : dailyMenu.getDishes()) {
                        if (String.valueOf(dish.getDishType()).equals(dishType)) {
                            table.addCell(dish.getName());
                            foundDish = true;
                            break;
                        }
                    }
                    if (!foundDish) {
                        table.addCell("");
                    }
                }
            }

            document.add(table);
            document.close();
            return byteArrayOutputStream.toByteArray();

        } catch (DocumentException e) {
            throw new RuntimeException("Error generating PDF", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] exportToExcel(WeeklyMenu weeklyMenu) {
        return null;
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
