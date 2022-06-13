INSERT INTO public.recipe (recipe_id, name) VALUES (101, 'pudding recipe');
INSERT INTO public.recipe (recipe_id, name) VALUES (201, 'Cereal with milk');
INSERT INTO public.recipe (recipe_id, name) VALUES (301, 'Fry chicken');

INSERT INTO public.ingredient (ingredient_id, name) VALUES (101, 'milk');
INSERT INTO public.ingredient (ingredient_id, name) VALUES (201, 'vanilla extract');
INSERT INTO public.ingredient (ingredient_id, name) VALUES (301, 'Cereal');
INSERT INTO public.ingredient (ingredient_id, name) VALUES (401, 'oil');
INSERT INTO public.ingredient (ingredient_id, name) VALUES (501, 'chicken');


INSERT INTO public.recipe_ingredient (recipe_ingredient_id, quantity, ingredient_id, recipe_id) VALUES (101, '1L', 101, 101);
INSERT INTO public.recipe_ingredient (recipe_ingredient_id, quantity, ingredient_id, recipe_id) VALUES (201, null, 201, 101);
INSERT INTO public.recipe_ingredient (recipe_ingredient_id, quantity, ingredient_id, recipe_id) VALUES (301, '0.3L', 101, 201);
INSERT INTO public.recipe_ingredient (recipe_ingredient_id, quantity, ingredient_id, recipe_id) VALUES (401, '200g', 301, 201);
INSERT INTO public.recipe_ingredient (recipe_ingredient_id, quantity, ingredient_id, recipe_id) VALUES (501, '50mL', 401, 301);
INSERT INTO public.recipe_ingredient (recipe_ingredient_id, quantity, ingredient_id, recipe_id) VALUES (601, '500g', 501, 301);