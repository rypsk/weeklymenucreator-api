package com.rypsk.weeklymenucreator.api.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.rypsk.weeklymenucreator.api.model.entity.*;
import com.rypsk.weeklymenucreator.api.repository.WeeklyMenuRepository;
import com.rypsk.weeklymenucreator.api.service.ExportService;
import com.rypsk.weeklymenucreator.api.service.UserService;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ExportServiceImpl implements ExportService {

    private final WeeklyMenuRepository weeklyMenuRepository;
    private final UserService userService;

    public ExportServiceImpl(WeeklyMenuRepository weeklyMenuRepository, UserService userService) {
        this.weeklyMenuRepository = weeklyMenuRepository;
        this.userService = userService;
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

            java.util.List<DailyMenu> dailyMenus = weeklyMenu.getDailyMenus();
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

        Map<String, java.util.List<Ingredient>> grouped = new HashMap<>();

        for (DailyMenu dailyMenu : weeklyMenu.getDailyMenus()) {
            for (Dish dish : dailyMenu.getDishes()) {
                Recipe recipe = dish.getRecipe();
                if (recipe != null && recipe.getIngredients() != null) {
                    for (Ingredient ingredient : recipe.getIngredients()) {
                        grouped.computeIfAbsent(ingredient.getName(), k -> new java.util.ArrayList<>()).add(ingredient);
                    }
                }
            }
        }

        for (Map.Entry<String, java.util.List<Ingredient>> entry : grouped.entrySet()) {
            String name = entry.getKey();
            java.util.List<Ingredient> ingList = entry.getValue();
            for (Ingredient ing : ingList) {
                sb.append(name).append(",");
                sb.append(ing.getQuantity()).append(",");
                sb.append("unit\n"); // Puedes agregar `ing.getUnit()` si lo tienes como campo
            }
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
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
}
