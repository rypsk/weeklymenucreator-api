-- USERS
INSERT INTO USERS (ID, USERNAME, PASSWORD, IS_ENABLED, IS_ACCOUNT_NON_EXPIRED, IS_ACCOUNT_NON_LOCKED, IS_CREDENTIALS_NON_EXPIRED, ROLE) VALUES
(1, 'a1', '$2a$10$iT63nNLQ8uJFbpvIegrdqeU67S5S4bmZBsFKBz16wfq5k1M0nHU0O',  1, 1, 1,1, 'ROLE_USER'),
(2, 'a2', '$2a$10$iT63nNLQ8uJFbpvIegrdqeU67S5S4bmZBsFKBz16wfq5k1M0nHU0O', 1, 1, 0,0, 'ROLE_ADMIN');

-- RECIPES
INSERT INTO recipe (id, name, description, difficulty, user_id)
VALUES
    (1, 'Lentil Soup', 'Healthy lentil soup.', 'EASY', 1),
    (2, 'Salmon Grill', 'Grilled salmon with herbs.', 'MEDIUM', 1),
    (3, 'Avocado Toast', 'Toasted bread with avocado.', 'EASY', 1);

-- INGREDIENTS
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES
    (1, 'Lentils', '200g', 1, 1),
    (2, 'Water', '1L', 1, 1),
    (3, 'Salmon', '150g', 2, 1),
    (4, 'Olive Oil', '1 tbsp', 2, 1),
    (5, 'Avocado', '1 unit', 3, 1),
    (6, 'Bread', '2 slices', 3, 1);

-- DISHES
INSERT INTO dish (id, name, description, recipe_id, dish_type, food_type, diet_type, user_id)
VALUES
    (1, 'Greek Yogurt Bowl', 'A delicious breakfast dish called Greek Yogurt Bowl.', 2, 'BREAKFAST', 'MEAT', 'VEGETARIAN', 1),
    (2, 'Vegan Buddha Bowl', 'A delicious lunch dish called Vegan Buddha Bowl.', 3, 'LUNCH', 'FISH', 'PALEO', 1),
    (3, 'Beef Stir-fry', 'A delicious dinner dish called Beef Stir-fry.', 1, 'DINNER', 'MEAT', 'MEDITERRANEAN', 1),
    (4, 'Greek Yogurt Bowl', 'A delicious breakfast dish called Greek Yogurt Bowl.', 2, 'BREAKFAST', 'FISH', 'MEAT', 1),
    (5, 'Pasta Primavera', 'A delicious lunch dish called Pasta Primavera.', 3, 'LUNCH', 'LEGUMES', 'GLUTEN_FREE', 1),
    (6, 'Beef Stir-fry', 'A delicious dinner dish called Beef Stir-fry.', 1, 'DINNER', 'FISH', 'MEDITERRANEAN', 1),
    (7, 'Greek Yogurt Bowl', 'A delicious breakfast dish called Greek Yogurt Bowl.', 2, 'BREAKFAST', 'LEGUMES', 'VEGETARIAN', 1),
    (8, 'Vegan Buddha Bowl', 'A delicious lunch dish called Vegan Buddha Bowl.', 3, 'LUNCH', 'FISH', 'VEGAN', 1),
    (9, 'Baked Salmon', 'A delicious dinner dish called Baked Salmon.', 1, 'DINNER', 'LEGUMES', 'MEAT', 1),
    (10, 'Avocado Toast', 'A delicious breakfast dish called Avocado Toast.', 2, 'BREAKFAST', 'CEREALS', 'VEGAN', 1),
    (11, 'Lentil Soup Dish', 'Lunch dish with lentils.', 1, 'LUNCH', 'LEGUMES', 'VEGAN', 1),
    (12, 'Grilled Salmon Dish', 'Dinner fish dish.', 2, 'DINNER', 'FISH', 'MEDITERRANEAN', 1),
    (13, 'Avocado Toast Dish', 'Healthy vegan breakfast.', 3, 'BREAKFAST', 'VEGETABLES', 'VEGAN', 1);

-- DAILY MENUS
INSERT INTO daily_menu (id, day_of_week, date, user_id)
VALUES (1, 'MONDAY', '2025-04-14', 1);

INSERT INTO daily_menu (id, day_of_week, date, user_id)
VALUES (3, 'WEDNESDAY', '2025-04-16', 1);

INSERT INTO daily_menu (id, day_of_week, date, user_id)
VALUES (2, 'TUESDAY', '2025-04-15', 1);



-- WEEKLY MENU
INSERT INTO weekly_menu (id, start_date, end_date, user_id)
VALUES (1, '2025-04-14', '2025-04-20', 1);

-- RELATION: WeeklyMenu -> DailyMenus
UPDATE daily_menu SET weekly_menu_id = 1 WHERE id IN (1, 2, 3);