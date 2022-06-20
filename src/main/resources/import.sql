INSERT INTO public.recipe (recipe_id, name) VALUES (101, 'Vanilla pudding');
INSERT INTO public.recipe (recipe_id, name) VALUES (102, 'Cereal with milk');
INSERT INTO public.recipe (recipe_id, name) VALUES (103, 'Fry chicken');
INSERT INTO public.recipe (recipe_id, name) VALUES (104, 'Hot chocolate');

INSERT INTO public.ingredient (ingredient_id, name) VALUES (101, 'milk');
INSERT INTO public.ingredient (ingredient_id, name) VALUES (102, 'vanilla extract');
INSERT INTO public.ingredient (ingredient_id, name) VALUES (103, 'Cereal');
INSERT INTO public.ingredient (ingredient_id, name) VALUES (104, 'oil');
INSERT INTO public.ingredient (ingredient_id, name) VALUES (105, 'chicken');
INSERT INTO public.ingredient (ingredient_id, name) VALUES (106, 'cacao');


INSERT INTO public.recipe_ingredient (recipe_ingredient_id, quantity, ingredient_id, recipe_id) VALUES (101, '1L', 101, 101);
INSERT INTO public.recipe_ingredient (recipe_ingredient_id, quantity, ingredient_id, recipe_id) VALUES (102, null, 102, 101);
INSERT INTO public.recipe_ingredient (recipe_ingredient_id, quantity, ingredient_id, recipe_id) VALUES (103, '200mL', 101, 102);
INSERT INTO public.recipe_ingredient (recipe_ingredient_id, quantity, ingredient_id, recipe_id) VALUES (104, '100g', 103, 102);
INSERT INTO public.recipe_ingredient (recipe_ingredient_id, quantity, ingredient_id, recipe_id) VALUES (105, '25mL', 104, 103);
INSERT INTO public.recipe_ingredient (recipe_ingredient_id, quantity, ingredient_id, recipe_id) VALUES (106, '500g', 105, 103);
INSERT INTO public.recipe_ingredient (recipe_ingredient_id, quantity, ingredient_id, recipe_id) VALUES (107, '200ml', 101, 104);
INSERT INTO public.recipe_ingredient (recipe_ingredient_id, quantity, ingredient_id, recipe_id) VALUES (108, '40g', 106, 104);