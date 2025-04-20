package com.rypsk.weeklymenucreator.api.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.rypsk.weeklymenucreator.api.model.dto.AutoGenerateWeeklyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuRequest;
import com.rypsk.weeklymenucreator.api.model.dto.WeeklyMenuResponse;
import com.rypsk.weeklymenucreator.api.model.entity.*;
import com.rypsk.weeklymenucreator.api.model.enumeration.DayOfWeek;
import com.rypsk.weeklymenucreator.api.model.enumeration.DietType;
import com.rypsk.weeklymenucreator.api.model.enumeration.DishType;
import com.rypsk.weeklymenucreator.api.model.enumeration.FoodType;
import com.rypsk.weeklymenucreator.api.repository.DailyMenuRepository;
import com.rypsk.weeklymenucreator.api.repository.UserRepository;
import com.rypsk.weeklymenucreator.api.repository.WeeklyMenuRepository;
import com.rypsk.weeklymenucreator.api.service.DishService;
import com.rypsk.weeklymenucreator.api.service.EmailService;
import com.rypsk.weeklymenucreator.api.service.UserService;
import com.rypsk.weeklymenucreator.api.service.WeeklyMenuService;
import jakarta.mail.MessagingException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeeklyMenuServiceImpl implements WeeklyMenuService {

    private final WeeklyMenuRepository weeklyMenuRepository;
    private final UserRepository userRepository;
    private final DishService dishService;
    private final EmailService emailService;
    private final DailyMenuRepository dailyMenuRepository;
    private final UserService userService;

    public WeeklyMenuServiceImpl(WeeklyMenuRepository weeklyMenuRepository, UserRepository userRepository, DishServiceImpl dishService, EmailService emailService, DailyMenuRepository dailyMenuRepository, UserService userService) {
        this.weeklyMenuRepository = weeklyMenuRepository;
        this.userRepository = userRepository;
        this.dishService = dishService;
        this.emailService = emailService;
        this.dailyMenuRepository = dailyMenuRepository;
        this.userService = userService;
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

    @Override
    public ResponseEntity<byte[]> exportWeeklyMenu(Long id, String format) {
        User user = userService.getCurrentUser();
        WeeklyMenu weeklyMenu = weeklyMenuRepository.findById(id).orElseThrow(() -> new RuntimeException("Weekly menu not found."));
        if (!weeklyMenu.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot export this weeklyMenu.");
        }
        byte[] content;
        MediaType mediaType;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String start = weeklyMenu.getStartDate().format(formatter);
        String end = weeklyMenu.getEndDate().format(formatter);
        String filename = "weekly-menu_" + start + "_" + end + "." + format.toLowerCase();

        switch (format.toLowerCase()) {
            case "pdf" -> {
                content = exportToPdf(weeklyMenu);
                mediaType = MediaType.APPLICATION_PDF;
            }
            case "excel", "xlsx" -> {
                content = exportToExcel(weeklyMenu);
                mediaType = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            }
            case "json" -> {
                content = exportToJson(weeklyMenu);
                mediaType = MediaType.APPLICATION_JSON;
            }
            case "csv" -> {
                content = exportToCsv(weeklyMenu);
                mediaType = MediaType.TEXT_PLAIN;
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
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Weekly Menu");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Dish Type");

            List<DailyMenu> dailyMenus = weeklyMenu.getDailyMenus();
            for (int i = 0; i < dailyMenus.size(); i++) {
                headerRow.createCell(i + 1).setCellValue(dailyMenus.get(i).getDayOfWeek().toString());
            }

            Set<String> dishTypes = new HashSet<>();
            for (DailyMenu dailyMenu : dailyMenus) {
                for (Dish dish : dailyMenu.getDishes()) {
                    dishTypes.add(dish.getDishType().name());
                }
            }

            int rowIdx = 1;
            for (String dishType : dishTypes) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(dishType);

                for (int i = 0; i < dailyMenus.size(); i++) {
                    DailyMenu dailyMenu = dailyMenus.get(i);
                    Optional<Dish> matchingDish = dailyMenu.getDishes().stream()
                            .filter(d -> d.getDishType().name().equals(dishType))
                            .findFirst();

                    if (matchingDish.isPresent()) {
                        row.createCell(i + 1).setCellValue(matchingDish.get().getName());
                    } else {
                        row.createCell(i + 1).setCellValue("");
                    }
                }
            }

            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Error generating Excel file", e);
        }
    }

    private byte[] exportToJson(WeeklyMenu weeklyMenu) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            return mapper.writeValueAsBytes(weeklyMenu);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error generating JSON", e);
        }
    }

    private byte[] exportToCsv(WeeklyMenu weeklyMenu) {
        StringBuilder sb = new StringBuilder();

        sb.append("WEEKLY MENU\n");
        sb.append("Day,Date,Dish Type,Dish Name,Dish Description,Food Type\n");

        for (DailyMenu dailyMenu : weeklyMenu.getDailyMenus()) {
            for (Dish dish : dailyMenu.getDishes()) {
                sb.append(dailyMenu.getDayOfWeek()).append(",");
                sb.append(dailyMenu.getDate()).append(",");
                sb.append(dish.getDishType()).append(",");
                sb.append("\"").append(dish.getName()).append("\",");
                sb.append("\"").append(dish.getDescription() != null ? dish.getDescription() : "").append("\",");
                sb.append(dish.getFoodType()).append("\n");
            }
        }

        sb.append("\nSHOPPING LIST\n");
        sb.append("Ingredient,Quantity,Unit\n");

        Map<String, List<Ingredient>> grouped = new HashMap<>();

        for (DailyMenu dailyMenu : weeklyMenu.getDailyMenus()) {
            for (Dish dish : dailyMenu.getDishes()) {
                Recipe recipe = dish.getRecipe();
                if (recipe != null && recipe.getIngredients() != null) {
                    for (Ingredient ingredient : recipe.getIngredients()) {
                        grouped.computeIfAbsent(ingredient.getName(), k -> new ArrayList<>()).add(ingredient);
                    }
                }
            }
        }

        for (Map.Entry<String, List<Ingredient>> entry : grouped.entrySet()) {
            String name = entry.getKey();
            List<Ingredient> ingList = entry.getValue();
            for (Ingredient ing : ingList) {
                sb.append(name).append(",");
                sb.append(ing.getQuantity()).append(",");
                sb.append("unit\n"); // Puedes agregar `ing.getUnit()` si lo tienes como campo
            }
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void sendWeeklyMenuByEmail(Long id, User user) {
        if (user == null) {
            user = userService.getCurrentUser();
        }
        WeeklyMenu weeklyMenu = weeklyMenuRepository.findById(id).orElseThrow(() -> new RuntimeException("Weekly menu not found."));
        if (!weeklyMenu.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot send by email this weeklyMenu.");
        }
        byte[] pdfBytes = exportToPdf(weeklyMenu);

        Attachment attachment = new Attachment("weekly-menu-" + weeklyMenu.getId() + ".pdf", "application/pdf", pdfBytes);

        String subject = "Your Weekly Menu (ID: " + weeklyMenu.getId() + ")";
        String body = "Here is your weekly menu as requested.\n\n";
        body += generateShoppingList(weeklyMenu);


        try {
            emailService.sendEmail(user.getEmail(), subject, body, List.of(attachment));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    private String generateShoppingList(WeeklyMenu weeklyMenu) {
        Map<String, String> ingredientMap = new LinkedHashMap<>();

        for (DailyMenu dailyMenu : weeklyMenu.getDailyMenus()) {
            for (Dish dish : dailyMenu.getDishes()) {
                for (Ingredient ingredient : dish.getRecipe().getIngredients()) {
                    ingredientMap.putIfAbsent(
                            ingredient.getName(),
                            ""
                    );

//                    ingredientMap.merge(
//                            ingredient.getName(),
//                            ingredient.getQuantity(),
//                            (existing, added) -> existing + " + " + added
//                    );
                }
            }
        }

        StringBuilder sb = new StringBuilder("Shopping List:\n\n");
        ingredientMap.forEach((name, qty) -> sb.append("- ").append(name).append(" ").append(qty).append("\n"));
        return sb.toString();
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
