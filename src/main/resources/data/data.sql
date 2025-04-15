-- USERS
INSERT INTO USERS (username, password, is_auto_email_enabled, is_enabled, is_account_non_expired, is_account_non_locked,
                   is_credentials_non_expired, role)
VALUES ('rypsk@hotmail.com', '$2a$10$iT63nNLQ8uJFbpvIegrdqeU67S5S4bmZBsFKBz16wfq5k1M0nHU0O', 1, 1, 1, 1, 1,
        'ROLE_USER'),
       ('a2', '$2a$10$iT63nNLQ8uJFbpvIegrdqeU67S5S4bmZBsFKBz16wfq5k1M0nHU0O', 0, 1, 1, 0, 0, 'ROLE_ADMIN');

-- RECIPES
INSERT INTO recipe (id, name, description, difficulty, user_id, is_public)
VALUES (1, 'Beef Stir-fry Recipe', 'A stir-fry recipe with beef and vegetables.', 'MEDIUM', 1, 1),
       (2, 'Greek Yogurt Bowl Recipe', 'A healthy bowl of Greek yogurt with toppings.', 'EASY', 1, 1),
       (3, 'Vegan Buddha Bowl Recipe', 'A colorful bowl with grains, veggies, and sauce.', 'EASY', 1, 1),
       (4, 'Veggie Omelette Recipe', 'Omelette with assorted fresh vegetables.', 'EASY', 1, 1),
       (5, 'Salmon Salad Recipe', 'Fresh salad topped with grilled salmon.', 'EASY', 1, 1),
       (6, 'Grilled Chicken Breast Recipe', 'Simple grilled chicken with herbs.', 'EASY', 1, 1),
       (7, 'Tofu Stir-fry Recipe', 'Tofu sautéed with soy sauce and veggies.', 'EASY', 1, 1),
       (8, 'Protein Pancakes Recipe', 'Fluffy pancakes made with protein powder.', 'MEDIUM', 1, 1),
       (9, 'Hummus Wrap Recipe', 'Tortilla wrap filled with hummus and veggies.', 'EASY', 1, 1),
       (10, 'Stuffed Zucchini Recipe', 'Zucchini halves filled with cheese and veggies.', 'MEDIUM', 1, 1),
       (11, 'Egg White Scramble Recipe', 'Light egg white scramble with spinach.', 'EASY', 1, 1),
       (12, 'Quinoa & Veggies Recipe', 'Quinoa mixed with stir-fried vegetables.', 'EASY', 1, 1),
       (13, 'Grilled Tuna Steak Recipe', 'Seared tuna with lemon and garlic.', 'HARD', 1, 1),
       (14, 'Banana Smoothie Recipe', 'Smoothie made with banana and almond milk.', 'EASY', 1, 1),
       (15, 'Falafel Plate Recipe', 'Falafel served with veggies and tahini.', 'MEDIUM', 1, 1),
       (16, 'Beef Skewers Recipe', 'Grilled skewers with marinated beef cubes.', 'MEDIUM', 1, 1),
       (17, 'Oats & Berries Recipe', 'Warm oatmeal topped with mixed berries.', 'EASY', 1, 1),
       (18, 'Eggplant Parmigiana Recipe', 'Breaded eggplant with tomato and cheese.', 'HARD', 1, 1),
       (19, 'Lamb Stew Recipe', 'Hearty stew with lamb and vegetables.', 'HARD', 1, 1),
       (20, 'Greek Yogurt with Nuts Recipe', 'Greek yogurt with walnuts and honey.', 'EASY', 1, 1),
       (21, 'Vegetable Curry Recipe', 'Spicy curry made with assorted vegetables.', 'MEDIUM', 1, 1),
       (22, 'Baked Haddock Recipe', 'Oven-baked white fish with lemon.', 'EASY', 1, 1),
       (23, 'Mango Chia Pudding Recipe', 'Chia seeds soaked in mango puree.', 'EASY', 1, 1),
       (24, 'Spaghetti Lentil Bolognese Recipe', 'Lentil-based sauce served over pasta.', 'MEDIUM', 1, 1),
       (25, 'Turkey Breast Recipe', 'Juicy roasted turkey breast.', 'MEDIUM', 1, 1),
       (26, 'Coconut Oatmeal Recipe', 'Creamy oatmeal with coconut milk.', 'EASY', 1, 1),
       (27, 'Chickpea Bowl Recipe', 'Chickpeas with greens and grains.', 'EASY', 1, 1),
       (28, 'Grilled Swordfish Recipe', 'Swordfish steaks grilled with herbs.', 'HARD', 1, 1),
       (29, 'Avocado Toast Recipe', 'Toasted bread topped with mashed avocado.', 'EASY', 1, 1),
       (30, 'Mixed Bean Chili Recipe', 'Spicy chili with a mix of beans.', 'MEDIUM', 1, 1),
       (31, 'Pork Medallions Recipe', 'Pan-seared pork medallions.', 'MEDIUM', 1, 1),
       (32, 'Berries Parfait Recipe', 'Layered parfait with yogurt and berries.', 'EASY', 1, 1),
       (33, 'Veggie Burger Recipe', 'Vegetarian patty served on a bun.', 'MEDIUM', 1, 1),
       (34, 'Sea Bass Fillet Recipe', 'Fillet of sea bass grilled to perfection.', 'HARD', 1, 1),
       (35, 'Almond Porridge Recipe', 'Oat porridge with almond butter.', 'EASY', 1, 1),
       (36, 'Couscous Salad Recipe', 'Cold salad with couscous and veggies.', 'EASY', 1, 1),
       (37, 'Herbed Chicken Thighs Recipe', 'Chicken thighs baked with fresh herbs.', 'MEDIUM', 1, 1),
       (38, 'Apple Cinnamon Oats Recipe', 'Oats cooked with apples and cinnamon.', 'EASY', 1, 1),
       (39, 'Spinach Quiche Recipe', 'Baked quiche with spinach and cheese.', 'MEDIUM', 1, 1),
       (40, 'Pan-Seared Mahi Mahi Recipe', 'Fish fillet seared with garlic butter.', 'HARD', 1, 1),
       (41, 'Choco Protein Shake Recipe', 'Chocolate-flavored protein drink.', 'EASY', 1, 1),
       (42, 'Red Lentil Tacos Recipe', 'Taco filling made with red lentils.', 'MEDIUM', 1, 1),
       (43, 'Roast Duck Breast Recipe', 'Duck breast roasted to a crispy finish.', 'HARD', 1, 1),
       (44, 'Zucchini Fritters Recipe', 'Fried patties made with shredded zucchini.', 'MEDIUM', 1, 1),
       (45, 'Salmon Avocado Bowl Recipe', 'Rice bowl with salmon and avocado.', 'MEDIUM', 1, 1),
       (46, 'Tofu Breakfast Wrap Recipe', 'Wrap with tofu scramble and veggies.', 'EASY', 1, 1),
       (47, 'Turkey Lettuce Wraps Recipe', 'Ground turkey in crisp lettuce cups.', 'EASY', 1, 1),
       (48, 'Egg Muffins Recipe', 'Mini egg omelets baked in muffin tins.', 'EASY', 1, 1),
       (49, 'Sweet Potato Curry Recipe', 'Curry made with chunks of sweet potato.', 'MEDIUM', 1, 1),
       (50, 'Chickpea Shakshuka Recipe', 'Chickpeas in spiced tomato sauce.', 'MEDIUM', 1, 1),
       (51, 'Spinach Mushroom Omelette Recipe', 'Omelette filled with spinach and mushrooms.', 'EASY', 1, 1),
       (52, 'Grilled Halibut Recipe', 'Halibut fillet grilled with lemon zest.', 'HARD', 1, 1),
       (53, 'Lentil Pita Sandwich Recipe', 'Pita stuffed with seasoned lentils.', 'EASY', 1, 1),
       (54, 'Avocado Banana Smoothie Recipe', 'Smoothie with avocado and banana.', 'EASY', 1, 1),
       (55, 'Stuffed Bell Peppers Recipe', 'Bell peppers filled with rice and veggies.', 'MEDIUM', 1, 1),
       (56, 'Miso Soup with Tofu Recipe', 'Japanese soup with tofu and seaweed.', 'EASY', 1, 1),
       (57, 'Cottage Cheese Bowl Recipe', 'Bowl with cottage cheese, fruits, and seeds.', 'EASY', 1, 1),
       (58, 'Tilapia Fillet Recipe', 'Baked tilapia with paprika.', 'EASY', 1, 1),
       (59, 'Vegetable Stir-fry Noodles Recipe', 'Noodles with stir-fried veggies.', 'MEDIUM', 1, 1),
       (60, 'Green Smoothie Recipe', 'Smoothie with spinach, banana, and apple.', 'EASY', 1, 1),
       (61, 'Lentil Soup Recipe', 'Warm soup made with lentils and spices.', 'EASY', 1, 1),
       (62, 'Grilled Salmon Recipe', 'Simple grilled salmon with herbs.', 'EASY', 1, 1),
       (63, 'Avocado Toast Dish Recipe', 'Toast topped with avocado and seeds.', 'EASY', 1, 1),
       (64, 'Chickpea Salad Recipe', 'Cold salad with chickpeas and tomatoes.', 'EASY', 1, 1),
       (65, 'Tofu Scramble Recipe', 'Scrambled tofu with veggies.', 'EASY', 1, 1),
       (66, 'Grilled Chicken Recipe', 'Chicken grilled with spices.', 'EASY', 1, 1),
       (67, 'Oatmeal Delight Recipe', 'Creamy oatmeal with cinnamon.', 'EASY', 1, 1),
       (68, 'Quinoa Bowl Recipe', 'Bowl of quinoa with vegetables.', 'EASY', 1, 1),
       (69, 'Stuffed Peppers Recipe', 'Bell peppers filled with meat or rice.', 'MEDIUM', 1, 1),
       (70, 'Scrambled Eggs Recipe', 'Classic scrambled eggs with seasoning.', 'EASY', 1, 1),
       (71, 'Falafel Wrap Recipe', 'Wrap with falafel, lettuce, and sauce.', 'EASY', 1, 1),
       (72, 'Lamb Chops Recipe', 'Grilled lamb chops with rosemary.', 'HARD', 1, 1),
       (73, 'Smoothie Bowl Recipe', 'Thick smoothie topped with fruit and seeds.', 'EASY', 1, 1),
       (74, 'Rice and Beans Recipe', 'Classic rice and black beans.', 'EASY', 1, 1),
       (75, 'Baked Cod Recipe', 'Cod fillet baked with garlic butter.', 'EASY', 1, 1),
       (76, 'Chia Pudding Recipe', 'Chia seeds soaked in almond milk.', 'EASY', 1, 1),
       (77, 'Veggie Tacos Recipe', 'Tacos filled with grilled vegetables.', 'EASY', 1, 1),
       (78, 'Turkey Meatballs Recipe', 'Meatballs made with lean turkey.', 'MEDIUM', 1, 1),
       (79, 'Spinach Omelette Recipe', 'Omelette with sautéed spinach.', 'EASY', 1, 1),
       (80, 'Zucchini Noodles Recipe', 'Zoodles served with pesto.', 'EASY', 1, 1),
       (81, 'Grilled Shrimp Recipe', 'Shrimp grilled with garlic butter.', 'MEDIUM', 1, 1),
       (82, 'Banana Pancakes Recipe', 'Pancakes made with banana batter.', 'EASY', 1, 1),
       (83, 'Lentil Stew Recipe', 'Rich stew made with lentils.', 'MEDIUM', 1, 1),
       (84, 'Chicken Curry Recipe', 'Spicy chicken curry.', 'MEDIUM', 1, 1),
       (85, 'Fruit Salad Recipe', 'Mixed fruits in a bowl.', 'EASY', 1, 1),
       (86, 'Black Bean Burger Recipe', 'Burger with black bean patty.', 'MEDIUM', 1, 1),
       (87, 'Steak Fajitas Recipe', 'Grilled steak with bell peppers.', 'MEDIUM', 1, 1),
       (88, 'Almond Butter Toast Recipe', 'Toast spread with almond butter.', 'EASY', 1, 1),
       (89, 'Grilled Veggie Wrap Recipe', 'Wrap filled with grilled vegetables.', 'EASY', 1, 1),
       (90, 'Tilapia Filet Recipe', 'Tilapia cooked with herbs.', 'EASY', 1, 1),
       (91, 'Egg Muffins (Again) Recipe', 'Savory egg muffins with ham.', 'EASY', 1, 1),
       (92, 'Hummus Bowl Recipe', 'Hummus served with pita and veggies.', 'EASY', 1, 1),
       (93, 'Roasted Duck Recipe', 'Duck roasted to golden brown.', 'HARD', 1, 1),
       (94, 'Veggie Omelette (Again) Recipe', 'Fluffy omelette with veggies.', 'EASY', 1, 1),
       (95, 'Salmon Salad (Again) Recipe', 'Light salad with grilled salmon.', 'EASY', 1, 1),
       (96, 'Grilled Chicken Breast (Again) Recipe', 'Simple grilled chicken filet.', 'EASY', 1, 1),
       (97, 'Tofu Stir-fry (Again) Recipe', 'Tofu and veggies in stir-fry sauce.', 'EASY', 1, 1),
       (98, 'Protein Pancakes (Again) Recipe', 'Protein-rich banana pancakes.', 'MEDIUM', 1, 1),
       (99, 'Hummus Wrap (Again) Recipe', 'Wrap with hummus and greens.', 'EASY', 1, 1),
       (100, 'Stuffed Zucchini (Again) Recipe', 'Zucchini stuffed with grains.', 'MEDIUM', 1, 1),
       (101, 'Grilled Steak with Vegetables Recipe',
        'A recipe for grilling steak and serving it with a side of vegetables.', 'HARD', 1, 1),
       (102, 'Chicken Alfredo Recipe', 'A rich and creamy pasta dish made with chicken and Alfredo sauce.', 'MEDIUM', 1,
        1),
       (103, 'Vegetable Stir-fry with Tofu Recipe',
        'A simple stir-fry made with a variety of vegetables and tofu, in soy sauce.', 'EASY', 1, 1),
       (104, 'Salmon with Asparagus Recipe',
        'A healthy recipe for grilling salmon and pairing it with fresh asparagus.', 'MEDIUM', 1, 1),
       (105, 'Beef Wellington Recipe', 'A luxurious recipe for making Beef Wellington, wrapped in puff pastry.', 'HARD',
        1, 1),
       (106, 'Vegan Tacos with Guacamole Recipe',
        'A recipe for preparing vegan tacos filled with vegetables and guacamole.', 'EASY', 1, 1),
       (107, 'Lentil and Sweet Potato Stew Recipe',
        'A hearty stew made with lentils and sweet potatoes, perfect for a cold evening.', 'MEDIUM', 1, 1),
       (108, 'Chickpea Curry Recipe',
        'A flavorful and creamy curry made with chickpeas and coconut milk, served with rice or bread.', 'MEDIUM', 1,
        1),
       (109, 'Grilled Portobello Mushrooms Recipe',
        'A simple recipe for grilling Portobello mushrooms and serving with balsamic glaze.', 'EASY', 1, 1),
       (110, 'Zucchini Noodles with Pesto Recipe',
        'A light and fresh recipe for zucchini noodles tossed with a vegan pesto sauce.', 'EASY', 1, 1);


-- INGREDIENTS
-- Ingredientes para la receta "Beef Stir-fry Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (1, 'Beef', '300g', 1, 1),
       (2, 'Bell Peppers', '1', 1, 1),
       (3, 'Carrot', '1', 1, 1),
       (4, 'Soy Sauce', '2 tbsp', 1, 1);

-- Ingredientes para la receta "Greek Yogurt Bowl Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (5, 'Greek Yogurt', '1 cup', 2, 1),
       (6, 'Honey', '1 tbsp', 2, 1),
       (7, 'Walnuts', '1/4 cup', 2, 1),
       (8, 'Berries', '1/4 cup', 2, 1);

-- Ingredientes para la receta "Vegan Buddha Bowl Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (9, 'Quinoa', '1/2 cup', 3, 1),
       (10, 'Chickpeas', '1/2 cup', 3, 1),
       (11, 'Avocado', '1/2', 3, 1),
       (12, 'Tahini', '1 tbsp', 3, 1);

-- Ingredientes para la receta "Veggie Omelette Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (13, 'Eggs', '2', 4, 1),
       (14, 'Bell Pepper', '1/2', 4, 1),
       (15, 'Spinach', '1/4 cup', 4, 1),
       (16, 'Onion', '1/4', 4, 1);

-- Ingredientes para la receta "Salmon Salad Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (17, 'Salmon Fillet', '200g', 5, 1),
       (18, 'Mixed Greens', '1 cup', 5, 1),
       (19, 'Olive Oil', '1 tbsp', 5, 1),
       (20, 'Lemon', '1/2', 5, 1);

-- Ingredientes para la receta "Grilled Chicken Breast Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (21, 'Chicken Breast', '1 piece', 6, 1),
       (22, 'Garlic', '2 cloves', 6, 1),
       (23, 'Rosemary', '1 sprig', 6, 1),
       (24, 'Olive Oil', '1 tbsp', 6, 1);

-- Ingredientes para la receta "Tofu Stir-fry Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (25, 'Tofu', '200g', 7, 1),
       (26, 'Broccoli', '1 cup', 7, 1),
       (27, 'Soy Sauce', '2 tbsp', 7, 1),
       (28, 'Sesame Oil', '1 tbsp', 7, 1);

-- Ingredientes para la receta "Protein Pancakes Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (29, 'Protein Powder', '1 scoop', 8, 1),
       (30, 'Oats', '1/2 cup', 8, 1),
       (31, 'Egg', '1', 8, 1),
       (32, 'Banana', '1/2', 8, 1);

-- Ingredientes para la receta "Hummus Wrap Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (33, 'Hummus', '1/4 cup', 9, 1),
       (34, 'Whole Wheat Wrap', '1', 9, 1),
       (35, 'Cucumber', '1/4', 9, 1),
       (36, 'Lettuce', '1 leaf', 9, 1);

-- Ingredientes para la receta "Stuffed Zucchini Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (37, 'Zucchini', '2', 10, 1),
       (38, 'Cheese', '1/4 cup', 10, 1),
       (39, 'Tomato', '1/2', 10, 1),
       (40, 'Garlic', '1 clove', 10, 1);

-- Ingredientes para la receta "Egg White Scramble Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (41, 'Egg Whites', '4', 11, 1),
       (42, 'Spinach', '1/2 cup', 11, 1),
       (43, 'Tomato', '1', 11, 1),
       (44, 'Olive Oil', '1 tsp', 11, 1);

-- Ingredientes para la receta "Quinoa & Veggies Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (45, 'Quinoa', '1/2 cup', 12, 1),
       (46, 'Bell Pepper', '1', 12, 1),
       (47, 'Spinach', '1/2 cup', 12, 1),
       (48, 'Garlic', '1 clove', 12, 1);

-- Ingredientes para la receta "Grilled Tuna Steak Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (49, 'Tuna Steak', '1 piece', 13, 1),
       (50, 'Lemon', '1/2', 13, 1),
       (51, 'Garlic', '1 clove', 13, 1),
       (52, 'Olive Oil', '1 tbsp', 13, 1);

-- Ingredientes para la receta "Banana Smoothie Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (53, 'Banana', '1', 14, 1),
       (54, 'Almond Milk', '1 cup', 14, 1),
       (55, 'Peanut Butter', '1 tbsp', 14, 1),
       (56, 'Honey', '1 tsp', 14, 1);

-- Ingredientes para la receta "Falafel Plate Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (57, 'Falafel', '4 pieces', 15, 1),
       (58, 'Tahini Sauce', '2 tbsp', 15, 1),
       (59, 'Cucumber', '1/2', 15, 1),
       (60, 'Lettuce', '1 leaf', 15, 1);

-- Ingredientes para la receta "Beef Skewers Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (61, 'Beef Cubes', '300g', 16, 1),
       (62, 'Bell Pepper', '1', 16, 1),
       (63, 'Onion', '1', 16, 1),
       (64, 'Soy Sauce', '1 tbsp', 16, 1);

-- Ingredientes para la receta "Oats & Berries Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (65, 'Oats', '1/2 cup', 17, 1),
       (66, 'Mixed Berries', '1/4 cup', 17, 1),
       (67, 'Honey', '1 tbsp', 17, 1),
       (68, 'Almond Milk', '1/2 cup', 17, 1);

-- Ingredientes para la receta "Eggplant Parmigiana Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (69, 'Eggplant', '2', 18, 1),
       (70, 'Tomato Sauce', '1 cup', 18, 1),
       (71, 'Mozzarella', '1/4 cup', 18, 1),
       (72, 'Parmesan', '2 tbsp', 18, 1);

-- Ingredientes para la receta "Lamb Stew Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (73, 'Lamb', '300g', 19, 1),
       (74, 'Carrot', '1', 19, 1),
       (75, 'Onion', '1', 19, 1),
       (76, 'Tomato', '1', 19, 1);
-- Ingredientes para la receta "Greek Yogurt with Nuts Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (77, 'Greek Yogurt', '1 cup', 20, 1),
       (78, 'Walnuts', '1/4 cup', 20, 1),
       (79, 'Honey', '1 tbsp', 20, 1),
       (80, 'Almonds', '1/4 cup', 20, 1);

-- Ingredientes para la receta "Vegetable Curry Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (81, 'Curry Powder', '2 tbsp', 21, 1),
       (82, 'Cauliflower', '1/2', 21, 1),
       (83, 'Chickpeas', '1 cup', 21, 1),
       (84, 'Coconut Milk', '1 cup', 21, 1);

-- Ingredientes para la receta "Baked Haddock Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (85, 'Haddock Fillet', '1 piece', 22, 1),
       (86, 'Lemon', '1', 22, 1),
       (87, 'Olive Oil', '1 tbsp', 22, 1),
       (88, 'Paprika', '1 tsp', 22, 1);

-- Ingredientes para la receta "Mango Chia Pudding Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (89, 'Chia Seeds', '2 tbsp', 23, 1),
       (90, 'Mango Puree', '1/4 cup', 23, 1),
       (91, 'Almond Milk', '1/2 cup', 23, 1),
       (92, 'Honey', '1 tbsp', 23, 1);

-- Ingredientes para la receta "Spaghetti Lentil Bolognese Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (93, 'Lentils', '1 cup', 24, 1),
       (94, 'Spaghetti', '100g', 24, 1),
       (95, 'Tomato Sauce', '1 cup', 24, 1),
       (96, 'Onion', '1', 24, 1);

-- Ingredientes para la receta "Turkey Breast Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (97, 'Turkey Breast', '200g', 25, 1),
       (98, 'Garlic', '2 cloves', 25, 1),
       (99, 'Rosemary', '1 sprig', 25, 1),
       (100, 'Olive Oil', '1 tbsp', 25, 1);

-- Ingredientes para la receta "Coconut Oatmeal Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (101, 'Oats', '1/2 cup', 26, 1),
       (102, 'Coconut Milk', '1/2 cup', 26, 1),
       (103, 'Honey', '1 tbsp', 26, 1),
       (104, 'Cinnamon', '1/2 tsp', 26, 1);

-- Ingredientes para la receta "Chickpea Bowl Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (105, 'Chickpeas', '1/2 cup', 27, 1),
       (106, 'Cucumber', '1/2', 27, 1),
       (107, 'Tomato', '1/2', 27, 1),
       (108, 'Olive Oil', '1 tbsp', 27, 1);

-- Ingredientes para la receta "Grilled Swordfish Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (109, 'Swordfish', '200g', 28, 1),
       (110, 'Lemon', '1', 28, 1),
       (111, 'Olive Oil', '1 tbsp', 28, 1),
       (112, 'Rosemary', '1 sprig', 28, 1);

-- Ingredientes para la receta "Avocado Toast Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (113, 'Avocado', '1', 29, 1),
       (114, 'Whole Wheat Bread', '2 slices', 29, 1),
       (115, 'Lemon', '1/2', 29, 1),
       (116, 'Chili Flakes', '1 tsp', 29, 1);

-- Ingredientes para la receta "Mixed Bean Chili Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (117, 'Mixed Beans', '1 cup', 30, 1),
       (118, 'Tomato Sauce', '1 cup', 30, 1),
       (119, 'Onion', '1', 30, 1),
       (120, 'Chili Powder', '1 tbsp', 30, 1);

-- Ingredientes para la receta "Pork Medallions Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (121, 'Pork Medallions', '2 pieces', 31, 1),
       (122, 'Rosemary', '1 sprig', 31, 1),
       (123, 'Garlic', '2 cloves', 31, 1),
       (124, 'Olive Oil', '1 tbsp', 31, 1);

-- Ingredientes para la receta "Berries Parfait Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (125, 'Greek Yogurt', '1 cup', 32, 1),
       (126, 'Berries', '1/4 cup', 32, 1),
       (127, 'Honey', '1 tbsp', 32, 1),
       (128, 'Granola', '1/4 cup', 32, 1);

-- Ingredientes para la receta "Veggie Burger Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (129, 'Vegetarian Patty', '1', 33, 1),
       (130, 'Lettuce', '1 leaf', 33, 1),
       (131, 'Tomato', '1 slice', 33, 1),
       (132, 'Whole Wheat Bun', '1', 33, 1);

-- Ingredientes para la receta "Sea Bass Fillet Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (133, 'Sea Bass Fillet', '200g', 34, 1),
       (134, 'Lemon', '1/2', 34, 1),
       (135, 'Olive Oil', '1 tbsp', 34, 1),
       (136, 'Rosemary', '1 sprig', 34, 1);

-- Ingredientes para la receta "Almond Porridge Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (137, 'Almond Butter', '1 tbsp', 35, 1),
       (138, 'Oats', '1/2 cup', 35, 1),
       (139, 'Almond Milk', '1/2 cup', 35, 1),
       (140, 'Honey', '1 tbsp', 35, 1);

-- Ingredientes para la receta "Couscous Salad Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (141, 'Couscous', '1/2 cup', 36, 1),
       (142, 'Cucumber', '1/4', 36, 1),
       (143, 'Tomato', '1/2', 36, 1),
       (144, 'Olive Oil', '1 tbsp', 36, 1);
-- Ingredientes para la receta "Herbed Chicken Thighs Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (145, 'Chicken Thighs', '2 pieces', 37, 1),
       (146, 'Rosemary', '1 sprig', 37, 1),
       (147, 'Garlic', '2 cloves', 37, 1),
       (148, 'Olive Oil', '1 tbsp', 37, 1);

-- Ingredientes para la receta "Apple Cinnamon Oats Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (149, 'Oats', '1/2 cup', 38, 1),
       (150, 'Apple', '1', 38, 1),
       (151, 'Cinnamon', '1/2 tsp', 38, 1),
       (152, 'Honey', '1 tbsp', 38, 1);

-- Ingredientes para la receta "Spinach Quiche Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (153, 'Spinach', '1 cup', 39, 1),
       (154, 'Eggs', '3', 39, 1),
       (155, 'Cheese', '1/2 cup', 39, 1),
       (156, 'Milk', '1/4 cup', 39, 1);

-- Ingredientes para la receta "Pan-Seared Mahi Mahi Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (157, 'Mahi Mahi Fillet', '200g', 40, 1),
       (158, 'Garlic', '2 cloves', 40, 1),
       (159, 'Butter', '1 tbsp', 40, 1),
       (160, 'Lemon', '1', 40, 1);

-- Ingredientes para la receta "Choco Protein Shake Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (161, 'Chocolate Protein Powder', '1 scoop', 41, 1),
       (162, 'Almond Milk', '1 cup', 41, 1),
       (163, 'Banana', '1', 41, 1),
       (164, 'Cocoa Powder', '1 tbsp', 41, 1);

-- Ingredientes para la receta "Red Lentil Tacos Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (165, 'Red Lentils', '1/2 cup', 42, 1),
       (166, 'Taco Shells', '4', 42, 1),
       (167, 'Avocado', '1', 42, 1),
       (168, 'Lettuce', '1 leaf', 42, 1);

-- Ingredientes para la receta "Roast Duck Breast Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (169, 'Duck Breast', '2 pieces', 43, 1),
       (170, 'Rosemary', '1 sprig', 43, 1),
       (171, 'Garlic', '2 cloves', 43, 1),
       (172, 'Olive Oil', '1 tbsp', 43, 1);

-- Ingredientes para la receta "Zucchini Fritters Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (173, 'Zucchini', '2', 44, 1),
       (174, 'Flour', '1/2 cup', 44, 1),
       (175, 'Eggs', '2', 44, 1),
       (176, 'Olive Oil', '1 tbsp', 44, 1);

-- Ingredientes para la receta "Salmon Avocado Bowl Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (177, 'Salmon', '200g', 45, 1),
       (178, 'Avocado', '1', 45, 1),
       (179, 'Brown Rice', '1/2 cup', 45, 1),
       (180, 'Cucumber', '1/2', 45, 1);

-- Ingredientes para la receta "Tofu Breakfast Wrap Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (181, 'Tofu', '200g', 46, 1),
       (182, 'Whole Wheat Wrap', '1', 46, 1),
       (183, 'Spinach', '1/2 cup', 46, 1),
       (184, 'Olive Oil', '1 tbsp', 46, 1);

-- Ingredientes para la receta "Turkey Lettuce Wraps Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (185, 'Ground Turkey', '200g', 47, 1),
       (186, 'Lettuce', '4 leaves', 47, 1),
       (187, 'Garlic', '2 cloves', 47, 1),
       (188, 'Olive Oil', '1 tbsp', 47, 1);

-- Ingredientes para la receta "Egg Muffins Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (189, 'Eggs', '6', 48, 1),
       (190, 'Ham', '1 slice', 48, 1),
       (191, 'Cheese', '1/4 cup', 48, 1),
       (192, 'Spinach', '1/2 cup', 48, 1);

-- Ingredientes para la receta "Sweet Potato Curry Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (193, 'Sweet Potato', '2', 49, 1),
       (194, 'Coconut Milk', '1/2 cup', 49, 1),
       (195, 'Curry Powder', '1 tbsp', 49, 1),
       (196, 'Garlic', '2 cloves', 49, 1);

-- Ingredientes para la receta "Chickpea Shakshuka Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (197, 'Chickpeas', '1/2 cup', 50, 1),
       (198, 'Tomato', '2', 50, 1),
       (199, 'Eggs', '2', 50, 1),
       (200, 'Olive Oil', '1 tbsp', 50, 1);

-- Ingredientes para la receta "Spinach Mushroom Omelette Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (201, 'Eggs', '2', 51, 1),
       (202, 'Spinach', '1/2 cup', 51, 1),
       (203, 'Mushrooms', '1/4 cup', 51, 1),
       (204, 'Cheese', '1/4 cup', 51, 1);

-- Ingredientes para la receta "Grilled Halibut Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (205, 'Halibut Fillet', '200g', 52, 1),
       (206, 'Lemon', '1/2', 52, 1),
       (207, 'Garlic', '2 cloves', 52, 1),
       (208, 'Olive Oil', '1 tbsp', 52, 1);

-- Ingredientes para la receta "Lentil Pita Sandwich Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (209, 'Lentils', '1/2 cup', 53, 1),
       (210, 'Pita Bread', '1', 53, 1),
       (211, 'Cucumber', '1/2', 53, 1),
       (212, 'Tahini', '1 tbsp', 53, 1);

-- Ingredientes para la receta "Avocado Banana Smoothie Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (213, 'Avocado', '1', 54, 1),
       (214, 'Banana', '1', 54, 1),
       (215, 'Almond Milk', '1 cup', 54, 1),
       (216, 'Honey', '1 tbsp', 54, 1);

-- Ingredientes para la receta "Stuffed Bell Peppers Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (217, 'Bell Peppers', '2', 55, 1),
       (218, 'Rice', '1/2 cup', 55, 1),
       (219, 'Tomato', '1', 55, 1),
       (220, 'Cheese', '1/4 cup', 55, 1);
-- Ingredientes para la receta "Miso Soup with Tofu Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (221, 'Miso Paste', '1 tbsp', 56, 1),
       (222, 'Tofu', '100g', 56, 1),
       (223, 'Seaweed', '1 tbsp', 56, 1),
       (224, 'Green Onion', '1 stalk', 56, 1);

-- Ingredientes para la receta "Cottage Cheese Bowl Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (225, 'Cottage Cheese', '1 cup', 57, 1),
       (226, 'Fruit', '1/2 cup', 57, 1),
       (227, 'Chia Seeds', '1 tbsp', 57, 1),
       (228, 'Honey', '1 tbsp', 57, 1);

-- Ingredientes para la receta "Tilapia Fillet Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (229, 'Tilapia Fillet', '200g', 58, 1),
       (230, 'Paprika', '1 tsp', 58, 1),
       (231, 'Lemon', '1/2', 58, 1),
       (232, 'Olive Oil', '1 tbsp', 58, 1);

-- Ingredientes para la receta "Vegetable Stir-fry Noodles Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (233, 'Noodles', '1 cup', 59, 1),
       (234, 'Bell Pepper', '1', 59, 1),
       (235, 'Carrot', '1', 59, 1),
       (236, 'Soy Sauce', '1 tbsp', 59, 1);

-- Ingredientes para la receta "Green Smoothie Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (237, 'Spinach', '1 cup', 60, 1),
       (238, 'Banana', '1', 60, 1),
       (239, 'Apple', '1', 60, 1),
       (240, 'Almond Milk', '1 cup', 60, 1);

-- Ingredientes para la receta "Lentil Soup Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (241, 'Lentils', '1/2 cup', 61, 1),
       (242, 'Carrot', '1', 61, 1),
       (243, 'Celery', '1 stalk', 61, 1),
       (244, 'Garlic', '2 cloves', 61, 1);

-- Ingredientes para la receta "Grilled Salmon Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (245, 'Salmon', '200g', 62, 1),
       (246, 'Lemon', '1/2', 62, 1),
       (247, 'Garlic', '2 cloves', 62, 1),
       (248, 'Olive Oil', '1 tbsp', 62, 1);

-- Ingredientes para la receta "Avocado Toast Dish Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (249, 'Avocado', '1', 63, 1),
       (250, 'Bread', '2 slices', 63, 1),
       (251, 'Chili Flakes', '1 tsp', 63, 1),
       (252, 'Olive Oil', '1 tbsp', 63, 1);

-- Ingredientes para la receta "Chickpea Salad Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (253, 'Chickpeas', '1/2 cup', 64, 1),
       (254, 'Tomato', '1', 64, 1),
       (255, 'Cucumber', '1/2', 64, 1),
       (256, 'Lemon', '1/2', 64, 1);

-- Ingredientes para la receta "Tofu Scramble Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (257, 'Tofu', '200g', 65, 1),
       (258, 'Spinach', '1/2 cup', 65, 1),
       (259, 'Tomato', '1', 65, 1),
       (260, 'Olive Oil', '1 tbsp', 65, 1);

-- Ingredientes para la receta "Grilled Chicken Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (261, 'Chicken Breast', '200g', 66, 1),
       (262, 'Paprika', '1 tsp', 66, 1),
       (263, 'Garlic', '2 cloves', 66, 1),
       (264, 'Olive Oil', '1 tbsp', 66, 1);

-- Ingredientes para la receta "Oatmeal Delight Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (265, 'Oats', '1/2 cup', 67, 1),
       (266, 'Cinnamon', '1/2 tsp', 67, 1),
       (267, 'Banana', '1', 67, 1),
       (268, 'Almond Milk', '1 cup', 67, 1);

-- Ingredientes para la receta "Quinoa Bowl Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (269, 'Quinoa', '1/2 cup', 68, 1),
       (270, 'Avocado', '1', 68, 1),
       (271, 'Cucumber', '1/2', 68, 1),
       (272, 'Olive Oil', '1 tbsp', 68, 1);

-- Ingredientes para la receta "Stuffed Peppers Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (273, 'Bell Peppers', '2', 69, 1),
       (274, 'Ground Meat', '200g', 69, 1),
       (275, 'Rice', '1/2 cup', 69, 1),
       (276, 'Cheese', '1/4 cup', 69, 1);

-- Ingredientes para la receta "Scrambled Eggs Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (277, 'Eggs', '2', 70, 1),
       (278, 'Milk', '1/4 cup', 70, 1),
       (279, 'Salt', 'to taste', 70, 1),
       (280, 'Pepper', 'to taste', 70, 1);
-- Ingredientes para la receta "Falafel Wrap Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (281, 'Falafel', '4 pieces', 71, 1),
       (282, 'Lettuce', '1 leaf', 71, 1),
       (283, 'Tomato', '1 slice', 71, 1),
       (284, 'Tahini Sauce', '1 tbsp', 71, 1);

-- Ingredientes para la receta "Lamb Chops Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (285, 'Lamb Chops', '2 pieces', 72, 1),
       (286, 'Rosemary', '1 sprig', 72, 1),
       (287, 'Garlic', '2 cloves', 72, 1),
       (288, 'Olive Oil', '1 tbsp', 72, 1);

-- Ingredientes para la receta "Smoothie Bowl Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (289, 'Frozen Fruit', '1/2 cup', 73, 1),
       (290, 'Greek Yogurt', '1/4 cup', 73, 1),
       (291, 'Chia Seeds', '1 tbsp', 73, 1),
       (292, 'Granola', '1/4 cup', 73, 1);

-- Ingredientes para la receta "Rice and Beans Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (293, 'Rice', '1 cup', 74, 1),
       (294, 'Black Beans', '1/2 cup', 74, 1),
       (295, 'Garlic', '2 cloves', 74, 1),
       (296, 'Cumin', '1 tsp', 74, 1);

-- Ingredientes para la receta "Baked Cod Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (297, 'Cod Fillet', '200g', 75, 1),
       (298, 'Garlic Butter', '1 tbsp', 75, 1),
       (299, 'Lemon', '1/2', 75, 1),
       (300, 'Parsley', '1 tbsp', 75, 1);

-- Ingredientes para la receta "Chia Pudding Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (301, 'Chia Seeds', '3 tbsp', 76, 1),
       (302, 'Almond Milk', '1 cup', 76, 1),
       (303, 'Honey', '1 tbsp', 76, 1),
       (304, 'Berries', '1/4 cup', 76, 1);

-- Ingredientes para la receta "Veggie Tacos Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (305, 'Tortillas', '2', 77, 1),
       (306, 'Grilled Vegetables', '1/2 cup', 77, 1),
       (307, 'Salsa', '2 tbsp', 77, 1),
       (308, 'Avocado', '1/4', 77, 1);

-- Ingredientes para la receta "Turkey Meatballs Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (309, 'Ground Turkey', '200g', 78, 1),
       (310, 'Breadcrumbs', '1/4 cup', 78, 1),
       (311, 'Egg', '1', 78, 1),
       (312, 'Parmesan Cheese', '2 tbsp', 78, 1);

-- Ingredientes para la receta "Spinach Omelette Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (313, 'Eggs', '3', 79, 1),
       (314, 'Spinach', '1/2 cup', 79, 1),
       (315, 'Cheese', '2 tbsp', 79, 1),
       (316, 'Salt', 'to taste', 79, 1);

-- Ingredientes para la receta "Zucchini Noodles Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (317, 'Zucchini', '2', 80, 1),
       (318, 'Pesto Sauce', '1/4 cup', 80, 1),
       (319, 'Parmesan Cheese', '2 tbsp', 80, 1),
       (320, 'Olive Oil', '1 tbsp', 80, 1);

-- Ingredientes para la receta "Grilled Shrimp Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (321, 'Shrimp', '200g', 81, 1),
       (322, 'Garlic Butter', '2 tbsp', 81, 1),
       (323, 'Lemon', '1/2', 81, 1),
       (324, 'Parsley', '1 tbsp', 81, 1);

-- Ingredientes para la receta "Banana Pancakes Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (325, 'Banana', '1', 82, 1),
       (326, 'Eggs', '2', 82, 1),
       (327, 'Flour', '1/2 cup', 82, 1),
       (328, 'Baking Powder', '1 tsp', 82, 1);

-- Ingredientes para la receta "Lentil Stew Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (329, 'Lentils', '1 cup', 83, 1),
       (330, 'Carrot', '2', 83, 1),
       (331, 'Potatoes', '2', 83, 1),
       (332, 'Garlic', '2 cloves', 83, 1);

-- Ingredientes para la receta "Chicken Curry Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (333, 'Chicken Breast', '200g', 84, 1),
       (334, 'Curry Powder', '1 tbsp', 84, 1),
       (335, 'Coconut Milk', '1 cup', 84, 1),
       (336, 'Rice', '1 cup', 84, 1);

-- Ingredientes para la receta "Fruit Salad Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (337, 'Mixed Fruits', '1 cup', 85, 1),
       (338, 'Mint', '2 leaves', 85, 1),
       (339, 'Lemon', '1/4', 85, 1),
       (340, 'Honey', '1 tbsp', 85, 1);

-- Ingredientes para la receta "Black Bean Burger Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (341, 'Black Beans', '1 cup', 86, 1),
       (342, 'Breadcrumbs', '1/2 cup', 86, 1),
       (343, 'Egg', '1', 86, 1),
       (344, 'Spices', '1 tbsp', 86, 1);

-- Ingredientes para la receta "Steak Fajitas Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (345, 'Steak', '200g', 87, 1),
       (346, 'Bell Peppers', '2', 87, 1),
       (347, 'Onions', '1', 87, 1),
       (348, 'Tortillas', '2', 87, 1);
-- Ingredientes para la receta "Almond Butter Toast Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (429, 'Almond Butter', '2 tbsp', 88, 1),
       (430, 'Whole Wheat Bread', '2 slices', 88, 1),
       (431, 'Honey', '1 tsp', 88, 1),
       (432, 'Cinnamon', '1/4 tsp', 88, 1);

-- Ingredientes para la receta "Grilled Veggie Wrap Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (433, 'Tortilla', '1', 89, 1),
       (434, 'Zucchini', '1/4', 89, 1),
       (435, 'Bell Pepper', '1/2', 89, 1),
       (436, 'Hummus', '2 tbsp', 89, 1);

-- Ingredientes para la receta "Tilapia Filet Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (437, 'Tilapia Fillet', '200g', 90, 1),
       (438, 'Lemon', '1/2', 90, 1),
       (439, 'Olive Oil', '1 tbsp', 90, 1),
       (440, 'Garlic', '2 cloves', 90, 1);
-- Ingredientes para la receta "Egg Muffins (Again) Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (349, 'Eggs', '4', 91, 1),
       (350, 'Ham', '2 slices', 91, 1),
       (351, 'Spinach', '1/4 cup', 91, 1),
       (352, 'Cheese', '2 tbsp', 91, 1);

-- Ingredientes para la receta "Hummus Bowl Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (353, 'Hummus', '1/2 cup', 92, 1),
       (354, 'Pita Bread', '2 pieces', 92, 1),
       (355, 'Cucumber', '1/4', 92, 1),
       (356, 'Tomato', '1', 92, 1);

-- Ingredientes para la receta "Roasted Duck Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (357, 'Duck Breast', '2 pieces', 93, 1),
       (358, 'Garlic', '3 cloves', 93, 1),
       (359, 'Rosemary', '1 sprig', 93, 1),
       (360, 'Olive Oil', '2 tbsp', 93, 1);

-- Ingredientes para la receta "Veggie Omelette (Again) Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (361, 'Eggs', '3', 94, 1),
       (362, 'Onions', '1/4 cup', 94, 1),
       (363, 'Tomato', '1/4', 94, 1),
       (364, 'Cheese', '2 tbsp', 94, 1);

-- Ingredientes para la receta "Salmon Salad (Again) Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (365, 'Salmon', '150g', 95, 1),
       (366, 'Mixed Greens', '1 cup', 95, 1),
       (367, 'Lemon', '1/2', 95, 1),
       (368, 'Olive Oil', '1 tbsp', 95, 1);

-- Ingredientes para la receta "Grilled Chicken Breast (Again) Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (369, 'Chicken Breast', '200g', 96, 1),
       (370, 'Olive Oil', '1 tbsp', 96, 1),
       (371, 'Lemon', '1/2', 96, 1),
       (372, 'Rosemary', '1 sprig', 96, 1);

-- Ingredientes para la receta "Tofu Stir-fry (Again) Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (373, 'Tofu', '200g', 97, 1),
       (374, 'Soy Sauce', '2 tbsp', 97, 1),
       (375, 'Bell Peppers', '1/2 cup', 97, 1),
       (376, 'Broccoli', '1/4 cup', 97, 1);

-- Ingredientes para la receta "Protein Pancakes (Again) Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (377, 'Protein Powder', '1/2 scoop', 98, 1),
       (378, 'Banana', '1', 98, 1),
       (379, 'Eggs', '2', 98, 1),
       (380, 'Almond Milk', '1/4 cup', 98, 1);

-- Ingredientes para la receta "Hummus Wrap (Again) Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (381, 'Hummus', '3 tbsp', 99, 1),
       (382, 'Tortilla', '1', 99, 1),
       (383, 'Cucumber', '1/4', 99, 1),
       (384, 'Lettuce', '1 leaf', 99, 1);

-- Ingredientes para la receta "Stuffed Zucchini (Again) Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (385, 'Zucchini', '2', 100, 1),
       (386, 'Rice', '1/2 cup', 100, 1),
       (387, 'Tomato', '1/4', 100, 1),
       (388, 'Cheese', '2 tbsp', 100, 1);

-- Ingredientes para la receta "Grilled Steak with Vegetables Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (389, 'Steak', '250g', 101, 1),
       (390, 'Bell Peppers', '1/2 cup', 101, 1),
       (391, 'Onions', '1/4', 101, 1),
       (392, 'Olive Oil', '1 tbsp', 101, 1);

-- Ingredientes para la receta "Chicken Alfredo Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (393, 'Chicken Breast', '200g', 102, 1),
       (394, 'Alfredo Sauce', '1/2 cup', 102, 1),
       (395, 'Pasta', '1 cup', 102, 1),
       (396, 'Parmesan Cheese', '2 tbsp', 102, 1);

-- Ingredientes para la receta "Vegetable Stir-fry with Tofu Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (397, 'Tofu', '200g', 103, 1),
       (398, 'Carrot', '1', 103, 1),
       (399, 'Bell Peppers', '1/2 cup', 103, 1),
       (400, 'Soy Sauce', '2 tbsp', 103, 1);

-- Ingredientes para la receta "Salmon with Asparagus Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (401, 'Salmon', '200g', 104, 1),
       (402, 'Asparagus', '1/2 bunch', 104, 1),
       (403, 'Lemon', '1/2', 104, 1),
       (404, 'Olive Oil', '1 tbsp', 104, 1);

-- Ingredientes para la receta "Beef Wellington Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (405, 'Beef Tenderloin', '500g', 105, 1),
       (406, 'Puff Pastry', '2 sheets', 105, 1),
       (407, 'Mushrooms', '1/2 cup', 105, 1),
       (408, 'Dijon Mustard', '1 tbsp', 105, 1);

-- Ingredientes para la receta "Vegan Tacos with Guacamole Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (409, 'Tortillas', '2', 106, 1),
       (410, 'Guacamole', '2 tbsp', 106, 1),
       (411, 'Lettuce', '1 leaf', 106, 1),
       (412, 'Tomato', '1/4', 106, 1);

-- Ingredientes para la receta "Lentil and Sweet Potato Stew Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (413, 'Lentils', '1 cup', 107, 1),
       (414, 'Sweet Potatoes', '2', 107, 1),
       (415, 'Carrot', '1', 107, 1),
       (416, 'Onions', '1/2', 107, 1);

-- Ingredientes para la receta "Chickpea Curry Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (417, 'Chickpeas', '1 cup', 108, 1),
       (418, 'Coconut Milk', '1 cup', 108, 1),
       (419, 'Curry Powder', '1 tbsp', 108, 1),
       (420, 'Rice', '1 cup', 108, 1);

-- Ingredientes para la receta "Grilled Portobello Mushrooms Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (421, 'Portobello Mushrooms', '2 caps', 109, 1),
       (422, 'Balsamic Vinegar', '2 tbsp', 109, 1),
       (423, 'Garlic', '2 cloves', 109, 1),
       (424, 'Olive Oil', '1 tbsp', 109, 1);

-- Ingredientes para la receta "Zucchini Noodles with Pesto Recipe"
INSERT INTO ingredient (id, name, quantity, recipe_id, user_id)
VALUES (425, 'Zucchini', '2', 110, 1),
       (426, 'Pesto Sauce', '1/4 cup', 110, 1),
       (427, 'Parmesan Cheese', '2 tbsp', 110, 1),
       (428, 'Olive Oil', '1 tbsp', 110, 1);

-- DISHES
INSERT INTO dish (id, name, description, recipe_id, dish_type, food_type, diet_type, user_id)
VALUES (1, 'Greek Yogurt Bowl', 'A delicious breakfast dish called Greek Yogurt Bowl.', 2, 'BREAKFAST', 'MEAT',
        'VEGETARIAN', 1),
       (2, 'Vegan Buddha Bowl', 'A delicious lunch dish called Vegan Buddha Bowl.', 3, 'LUNCH', 'FISH', 'PALEO', 1),
       (3, 'Beef Stir-fry', 'A delicious dinner dish called Beef Stir-fry.', 1, 'DINNER', 'MEAT', 'MEDITERRANEAN', 1),
       (4, 'Greek Yogurt Bowl', 'A delicious breakfast dish called Greek Yogurt Bowl.', 2, 'BREAKFAST', 'FISH', 'MEAT',
        1),
       (5, 'Pasta Primavera', 'A delicious lunch dish called Pasta Primavera.', 3, 'LUNCH', 'LEGUMES', 'GLUTEN_FREE',
        1),
       (6, 'Beef Stir-fry', 'A delicious dinner dish called Beef Stir-fry.', 1, 'DINNER', 'FISH', 'MEDITERRANEAN', 1),
       (7, 'Greek Yogurt Bowl', 'A delicious breakfast dish called Greek Yogurt Bowl.', 2, 'BREAKFAST', 'LEGUMES',
        'VEGETARIAN', 1),
       (8, 'Vegan Buddha Bowl', 'A delicious lunch dish called Vegan Buddha Bowl.', 3, 'LUNCH', 'FISH', 'VEGAN', 1),
       (9, 'Baked Salmon', 'A delicious dinner dish called Baked Salmon.', 1, 'DINNER', 'LEGUMES', 'MEAT', 1),
       (10, 'Avocado Toast', 'A delicious breakfast dish called Avocado Toast.', 2, 'BREAKFAST', 'CEREALS', 'VEGAN', 1),
       (11, 'Lentil Soup Dish', 'Lunch dish with lentils.', 1, 'LUNCH', 'LEGUMES', 'VEGAN', 1),
       (12, 'Grilled Salmon Dish', 'Dinner fish dish.', 2, 'DINNER', 'FISH', 'MEDITERRANEAN', 1),
       (13, 'Avocado Toast Dish', 'Healthy vegan breakfast.', 3, 'BREAKFAST', 'VEGETABLES', 'VEGAN', 1),
       (14, 'Chickpea Salad', 'A delicious lunch dish called Chickpea Salad.', 1, 'LUNCH', 'LEGUMES', 'VEGAN', 1),
       (15, 'Tofu Scramble', 'A delicious breakfast dish called Tofu Scramble.', 2, 'BREAKFAST', 'VEGETABLES', 'VEGAN',
        1),
       (16, 'Grilled Chicken', 'A delicious dinner dish called Grilled Chicken.', 3, 'DINNER', 'MEAT', 'GLUTEN_FREE',
        1),
       (17, 'Oatmeal Delight', 'A delicious breakfast dish called Oatmeal Delight.', 1, 'BREAKFAST', 'VEGETABLES',
        'VEGETARIAN', 1),
       (18, 'Quinoa Bowl', 'A delicious lunch dish called Quinoa Bowl.', 2, 'LUNCH', 'LEGUMES', 'MEDITERRANEAN', 1),
       (19, 'Stuffed Peppers', 'A delicious dinner dish called Stuffed Peppers.', 3, 'DINNER', 'VEGETABLES',
        'VEGETARIAN', 1),
       (20, 'Scrambled Eggs', 'A delicious breakfast dish called Scrambled Eggs.', 1, 'BREAKFAST', 'MEAT',
        'GLUTEN_FREE', 1),
       (21, 'Falafel Wrap', 'A delicious lunch dish called Falafel Wrap.', 2, 'LUNCH', 'LEGUMES', 'VEGAN', 1),
       (22, 'Lamb Chops', 'A delicious dinner dish called Lamb Chops.', 3, 'DINNER', 'MEAT', 'PALEO', 1),
       (23, 'Smoothie Bowl', 'A delicious breakfast dish called Smoothie Bowl.', 1, 'BREAKFAST', 'VEGETABLES', 'VEGAN',
        1),
       (24, 'Rice and Beans', 'A delicious lunch dish called Rice and Beans.', 2, 'LUNCH', 'LEGUMES', 'VEGETARIAN', 1),
       (25, 'Baked Cod', 'A delicious dinner dish called Baked Cod.', 3, 'DINNER', 'FISH', 'MEDITERRANEAN', 1),
       (26, 'Chia Pudding', 'A delicious breakfast dish called Chia Pudding.', 1, 'BREAKFAST', 'VEGETABLES', 'VEGAN',
        1),
       (27, 'Veggie Tacos', 'A delicious lunch dish called Veggie Tacos.', 2, 'LUNCH', 'VEGETABLES', 'VEGAN', 1),
       (28, 'Turkey Meatballs', 'A delicious dinner dish called Turkey Meatballs.', 3, 'DINNER', 'MEAT', 'PALEO', 1),
       (29, 'Spinach Omelette', 'A delicious breakfast dish called Spinach Omelette.', 1, 'BREAKFAST', 'VEGETABLES',
        'VEGETARIAN', 1),
       (30, 'Zucchini Noodles', 'A delicious lunch dish called Zucchini Noodles.', 2, 'LUNCH', 'VEGETABLES',
        'GLUTEN_FREE', 1),
       (31, 'Grilled Shrimp', 'A delicious dinner dish called Grilled Shrimp.', 3, 'DINNER', 'FISH', 'MEDITERRANEAN',
        1),
       (32, 'Banana Pancakes', 'A delicious breakfast dish called Banana Pancakes.', 1, 'BREAKFAST', 'VEGETABLES',
        'VEGETARIAN', 1),
       (33, 'Lentil Stew', 'A delicious lunch dish called Lentil Stew.', 2, 'LUNCH', 'LEGUMES', 'VEGAN', 1),
       (34, 'Chicken Curry', 'A delicious dinner dish called Chicken Curry.', 3, 'DINNER', 'MEAT', 'GLUTEN_FREE', 1),
       (35, 'Fruit Salad', 'A delicious breakfast dish called Fruit Salad.', 1, 'BREAKFAST', 'VEGETABLES', 'VEGAN', 1),
       (36, 'Black Bean Burger', 'A delicious lunch dish called Black Bean Burger.', 2, 'LUNCH', 'LEGUMES',
        'VEGETARIAN', 1),
       (37, 'Steak Fajitas', 'A delicious dinner dish called Steak Fajitas.', 3, 'DINNER', 'MEAT', 'MEDITERRANEAN', 1),
       (38, 'Almond Butter Toast', 'A delicious breakfast dish called Almond Butter Toast.', 1, 'BREAKFAST',
        'VEGETABLES', 'VEGAN', 1),
       (39, 'Grilled Veggie Wrap', 'A delicious lunch dish called Grilled Veggie Wrap.', 2, 'LUNCH', 'VEGETABLES',
        'VEGAN', 1),
       (40, 'Tilapia Filet', 'A delicious dinner dish called Tilapia Filet.', 3, 'DINNER', 'FISH', 'PALEO', 1),
       (41, 'Egg Muffins', 'A delicious breakfast dish called Egg Muffins.', 1, 'BREAKFAST', 'MEAT', 'GLUTEN_FREE', 1),
       (42, 'Hummus Bowl', 'A delicious lunch dish called Hummus Bowl.', 2, 'LUNCH', 'LEGUMES', 'VEGAN', 1),
       (43, 'Roasted Duck', 'A delicious dinner dish called Roasted Duck.', 3, 'DINNER', 'MEAT', 'MEDITERRANEAN', 1),
       (44, 'Veggie Omelette', 'A delicious breakfast dish called Veggie Omelette.', 4, 'BREAKFAST', 'VEGETABLES',
        'VEGETARIAN', 2),
       (45, 'Salmon Salad', 'A delicious lunch dish called Salmon Salad.', 5, 'LUNCH', 'FISH', 'MEDITERRANEAN', 2),
       (46, 'Grilled Chicken Breast', 'A delicious dinner dish called Grilled Chicken Breast.', 6, 'DINNER', 'MEAT',
        'GLUTEN_FREE', 2),
       (47, 'Tofu Stir-fry', 'A delicious dinner dish called Tofu Stir-fry.', 7, 'DINNER', 'VEGETABLES', 'VEGAN', 2),
       (48, 'Protein Pancakes', 'A delicious breakfast dish called Protein Pancakes.', 8, 'BREAKFAST', 'VEGETABLES',
        'PALEO', 2),
       (49, 'Hummus Wrap', 'A delicious lunch dish called Hummus Wrap.', 9, 'LUNCH', 'LEGUMES', 'VEGAN', 2),
       (50, 'Stuffed Zucchini', 'A delicious dinner dish called Stuffed Zucchini.', 10, 'DINNER', 'VEGETABLES',
        'VEGETARIAN', 2),
       (51, 'Egg White Scramble', 'A delicious breakfast dish called Egg White Scramble.', 11, 'BREAKFAST', 'MEAT',
        'GLUTEN_FREE', 2),
       (52, 'Quinoa & Veggies', 'A delicious lunch dish called Quinoa & Veggies.', 12, 'LUNCH', 'LEGUMES', 'VEGAN', 2),
       (53, 'Grilled Tuna Steak', 'A delicious dinner dish called Grilled Tuna Steak.', 13, 'DINNER', 'FISH', 'PALEO',
        2),
       (54, 'Banana Smoothie', 'A delicious breakfast dish called Banana Smoothie.', 14, 'BREAKFAST', 'VEGETABLES',
        'VEGAN', 2),
       (55, 'Falafel Plate', 'A delicious lunch dish called Falafel Plate.', 15, 'LUNCH', 'LEGUMES', 'VEGETARIAN', 2),
       (56, 'Beef Skewers', 'A delicious dinner dish called Beef Skewers.', 16, 'DINNER', 'MEAT', 'MEDITERRANEAN', 2),
       (57, 'Oats & Berries', 'A delicious breakfast dish called Oats & Berries.', 17, 'BREAKFAST', 'VEGETABLES',
        'VEGAN', 2),
       (58, 'Eggplant Parmigiana', 'A delicious lunch dish called Eggplant Parmigiana.', 18, 'LUNCH', 'VEGETABLES',
        'VEGETARIAN', 2),
       (59, 'Lamb Stew', 'A delicious dinner dish called Lamb Stew.', 19, 'DINNER', 'MEAT', 'PALEO', 2),
       (60, 'Greek Yogurt with Nuts', 'A delicious breakfast dish with Greek yogurt and nuts.', 20, 'BREAKFAST',
        'VEGETABLES', 'GLUTEN_FREE', 2),
       (61, 'Vegetable Curry', 'A delicious lunch dish called Vegetable Curry.', 21, 'LUNCH', 'VEGETABLES', 'VEGAN', 2),
       (62, 'Baked Haddock', 'A delicious dinner dish called Baked Haddock.', 22, 'DINNER', 'FISH', 'MEDITERRANEAN', 2),
       (63, 'Mango Chia Pudding', 'A delicious breakfast dish called Mango Chia Pudding.', 23, 'BREAKFAST',
        'VEGETABLES', 'VEGAN', 2),
       (64, 'Spaghetti Lentil Bolognese', 'A delicious lunch dish called Spaghetti Lentil Bolognese.', 24, 'LUNCH',
        'LEGUMES', 'VEGAN', 2),
       (65, 'Turkey Breast', 'A delicious dinner dish called Turkey Breast.', 25, 'DINNER', 'MEAT', 'GLUTEN_FREE', 2),
       (66, 'Coconut Oatmeal', 'A delicious breakfast dish called Coconut Oatmeal.', 26, 'BREAKFAST', 'VEGETABLES',
        'VEGAN', 2),
       (67, 'Chickpea Bowl', 'A delicious lunch dish called Chickpea Bowl.', 27, 'LUNCH', 'LEGUMES', 'VEGETARIAN', 2),
       (68, 'Grilled Swordfish', 'A delicious dinner dish called Grilled Swordfish.', 28, 'DINNER', 'FISH', 'PALEO', 2),
       (69, 'Avocado Toast', 'A delicious breakfast dish called Avocado Toast.', 29, 'BREAKFAST', 'VEGETABLES', 'VEGAN',
        2),
       (70, 'Mixed Bean Chili', 'A delicious lunch dish called Mixed Bean Chili.', 30, 'LUNCH', 'LEGUMES', 'VEGAN', 2),
       (71, 'Pork Medallions', 'A delicious dinner dish called Pork Medallions.', 31, 'DINNER', 'MEAT', 'MEDITERRANEAN',
        2),
       (72, 'Berries Parfait', 'A delicious breakfast dish called Berries Parfait.', 32, 'BREAKFAST', 'VEGETABLES',
        'VEGETARIAN', 2),
       (73, 'Veggie Burger', 'A delicious lunch dish called Veggie Burger.', 33, 'LUNCH', 'VEGETABLES', 'GLUTEN_FREE',
        2),
       (74, 'Sea Bass Fillet', 'A delicious dinner dish called Sea Bass Fillet.', 34, 'DINNER', 'FISH', 'MEDITERRANEAN',
        2),
       (75, 'Almond Porridge', 'A delicious breakfast dish called Almond Porridge.', 35, 'BREAKFAST', 'VEGETABLES',
        'VEGAN', 2),
       (76, 'Couscous Salad', 'A delicious lunch dish called Couscous Salad.', 36, 'LUNCH', 'LEGUMES', 'VEGETARIAN', 2),
       (77, 'Herbed Chicken Thighs', 'A delicious dinner dish called Herbed Chicken Thighs.', 37, 'DINNER', 'MEAT',
        'PALEO', 2),
       (78, 'Apple Cinnamon Oats', 'A delicious breakfast dish called Apple Cinnamon Oats.', 38, 'BREAKFAST',
        'VEGETABLES', 'VEGETARIAN', 2),
       (79, 'Spinach Quiche', 'A delicious lunch dish called Spinach Quiche.', 39, 'LUNCH', 'VEGETABLES', 'GLUTEN_FREE',
        2),
       (80, 'Pan-Seared Mahi Mahi', 'A delicious dinner dish called Pan-Seared Mahi Mahi.', 40, 'DINNER', 'FISH',
        'MEDITERRANEAN', 2),
       (81, 'Choco Protein Shake', 'A delicious breakfast drink called Choco Protein Shake.', 41, 'BREAKFAST',
        'VEGETABLES', 'PALEO', 2),
       (82, 'Red Lentil Tacos', 'A delicious lunch dish called Red Lentil Tacos.', 42, 'LUNCH', 'LEGUMES', 'VEGAN', 2),
       (83, 'Roast Duck Breast', 'A delicious dinner dish called Roast Duck Breast.', 43, 'DINNER', 'MEAT',
        'MEDITERRANEAN', 2),
       (84, 'Zucchini Fritters', 'A delicious lunch dish called Zucchini Fritters.', 44, 'LUNCH', 'VEGETABLES',
        'VEGETARIAN', 2),
       (85, 'Salmon Avocado Bowl', 'A delicious dinner dish called Salmon Avocado Bowl.', 45, 'DINNER', 'FISH',
        'GLUTEN_FREE', 2),
       (86, 'Tofu Breakfast Wrap', 'A delicious breakfast dish called Tofu Breakfast Wrap.', 46, 'BREAKFAST', 'LEGUMES',
        'VEGAN', 2),
       (87, 'Turkey Lettuce Wraps', 'A delicious lunch dish called Turkey Lettuce Wraps.', 47, 'LUNCH', 'MEAT', 'PALEO',
        2),
       (88, 'Egg Muffins', 'A delicious breakfast dish called Egg Muffins.', 48, 'BREAKFAST', 'MEAT', 'KETO', 2),
       (89, 'Sweet Potato Curry', 'A delicious dinner dish called Sweet Potato Curry.', 49, 'DINNER', 'VEGETABLES',
        'VEGAN', 2),
       (90, 'Chickpea Shakshuka', 'A delicious lunch dish called Chickpea Shakshuka.', 50, 'LUNCH', 'LEGUMES',
        'VEGETARIAN', 2),
       (91, 'Spinach Mushroom Omelette', 'A delicious breakfast dish called Spinach Mushroom Omelette.', 51,
        'BREAKFAST', 'VEGETABLES', 'GLUTEN_FREE', 2),
       (92, 'Grilled Halibut', 'A delicious dinner dish called Grilled Halibut.', 52, 'DINNER', 'FISH', 'MEDITERRANEAN',
        2),
       (93, 'Lentil Pita Sandwich', 'A delicious lunch dish called Lentil Pita Sandwich.', 53, 'LUNCH', 'LEGUMES',
        'VEGAN', 2),
       (94, 'Avocado Banana Smoothie', 'A delicious breakfast drink called Avocado Banana Smoothie.', 54, 'BREAKFAST',
        'VEGETABLES', 'VEGAN', 2),
       (95, 'Stuffed Bell Peppers', 'A delicious dinner dish called Stuffed Bell Peppers.', 55, 'DINNER', 'VEGETABLES',
        'VEGETARIAN', 2),
       (96, 'Miso Soup with Tofu', 'A delicious lunch dish called Miso Soup with Tofu.', 56, 'LUNCH', 'LEGUMES',
        'VEGAN', 2),
       (97, 'Cottage Cheese Bowl', 'A delicious breakfast dish called Cottage Cheese Bowl.', 57, 'BREAKFAST',
        'VEGETABLES', 'KETO', 2),
       (98, 'Tilapia Fillet', 'A delicious dinner dish called Tilapia Fillet.', 58, 'DINNER', 'FISH', 'GLUTEN_FREE', 2),
       (99, 'Vegetable Stir-fry Noodles', 'A delicious lunch dish called Vegetable Stir-fry Noodles.', 59, 'LUNCH',
        'VEGETABLES', 'MEDITERRANEAN', 2),
       (100, 'Green Smoothie', 'A delicious breakfast drink called Green Smoothie.', 60, 'BREAKFAST', 'VEGETABLES',
        'VEGAN', 2),
       (101, 'Grilled Steak with Vegetables', 'Juicy grilled steak served with roasted vegetables.', 101, 'DINNER',
        'MEAT', 'MEDITERRANEAN', 1),
       (102, 'Chicken Alfredo', 'Creamy pasta with chicken and Alfredo sauce.', 102, 'DINNER', 'MEAT', 'GLUTEN_FREE',
        1),
       (103, 'Vegetable Stir-fry with Tofu', 'Stir-fried vegetables and tofu in soy sauce.', 103, 'DINNER',
        'VEGETABLES', 'VEGAN', 1),
       (104, 'Salmon with Asparagus', 'Grilled salmon fillet served with fresh asparagus.', 104, 'DINNER', 'FISH',
        'MEDITERRANEAN', 1),
       (105, 'Beef Wellington', 'A delicious beef fillet wrapped in puff pastry with mushrooms.', 105, 'DINNER', 'MEAT',
        'PALEO', 1),
       (106, 'Vegan Tacos with Guacamole',
        'Delicious soft tacos filled with fresh vegetables and topped with guacamole.', 106, 'DINNER', 'VEGETABLES',
        'VEGAN', 1),
       (107, 'Lentil and Sweet Potato Stew', 'A hearty stew made with lentils, sweet potatoes, and spices.', 107,
        'DINNER', 'LEGUMES', 'VEGAN', 1),
       (108, 'Chickpea Curry', 'A rich and creamy curry made with chickpeas, coconut milk, and spices.', 108, 'DINNER',
        'LEGUMES', 'VEGAN', 1),
       (109, 'Grilled Portobello Mushrooms', 'Grilled Portobello mushrooms served with a balsamic glaze.', 109,
        'DINNER', 'VEGETABLES', 'VEGAN', 1),
       (110, 'Zucchini Noodles with Pesto', 'Zucchini noodles tossed with a fresh basil pesto sauce.', 110, 'DINNER',
        'VEGETABLES', 'VEGAN', 1);

-- DAILY MENUS
INSERT INTO daily_menu (day_of_week, date, user_id)
VALUES ('MONDAY', '2025-04-14', 1);

INSERT INTO daily_menu (day_of_week, date, user_id)
VALUES ('WEDNESDAY', '2025-04-16', 1);

INSERT INTO daily_menu (day_of_week, date, user_id)
VALUES ('TUESDAY', '2025-04-15', 1);

INSERT INTO daily_menu_dishes (daily_menu_id, dishes_id)
VALUES (1, 1),
       (1, 2),
       (1, 3);

INSERT INTO daily_menu_dishes (daily_menu_id, dishes_id)
VALUES (2, 1),
       (2, 2),
       (2, 3);

INSERT INTO daily_menu_dishes (daily_menu_id, dishes_id)
VALUES (3, 1),
       (3, 2),
       (3, 3);

-- WEEKLY MENU
INSERT INTO weekly_menu (start_date, end_date, user_id)
VALUES ('2025-04-14', '2025-04-20', 1);

-- RELATION: WeeklyMenu -> DailyMenus
UPDATE daily_menu
SET weekly_menu_id = 1
WHERE id IN (1, 2, 3);