INSERT INTO users (name, email, mobile_number, pwd, role, create_dt) VALUES ('Happy','happy@gmail.com','9876548337', '$2y$12$oRRbkNfwuR8ug4MlzH5FOeui.//1mkd.RsOAJMbykTSupVy.x/vb2', 'admin', CURDATE());
INSERT INTO users (name, email, mobile_number, pwd, role, create_dt) VALUES ('Jayati', 'jayati@gmail.com', '3163392606', '$2a$10$0KbKjtLjBTeMZC4cym7apuesK3sq6sXkUVcFaloSFVTifjJNc6jJW', 'customer', CURDATE());
INSERT INTO authorities (user_id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO authorities (user_id, name) VALUES (2, 'ROLE_CUSTOMER');

insert into public.ingredients (id,ingredient,created_at,updated_at) values (1,'Tea',CURDATE(),CURDATE());
insert into public.ingredients (id,ingredient,created_at,updated_at) values (2,'Coffee',CURDATE(),CURDATE());
insert into public.ingredients (id,ingredient,created_at,updated_at) values (3,'Sugar',CURDATE(),CURDATE());
insert into public.ingredients (id,ingredient,created_at,updated_at) values (4,'Milk',CURDATE(),CURDATE());


insert into public.recipes (id,instructions,name,number_of_servings,type,created_at,updated_at) values (1,'boil milk,water,tea and add sugar','Tea',5,'VEGETARIAN',CURDATE(),CURDATE());
insert into public.recipes (id,instructions,name,number_of_servings,type,created_at,updated_at) values (2,'boil milk,water,COFFEE and DONOT add sugar','coffee',5,'VEGETARIAN',CURDATE(),CURDATE());

insert into public.recipe_ingredient(recipe_id,ingredient_id) values (1,1);
insert into public.recipe_ingredient(recipe_id,ingredient_id) values (1,3);
insert into public.recipe_ingredient(recipe_id,ingredient_id) values (1,4);
insert into public.recipe_ingredient(recipe_id,ingredient_id) values (2,2);
insert into public.recipe_ingredient(recipe_id,ingredient_id) values (2,3);
insert into public.recipe_ingredient(recipe_id,ingredient_id) values (2,4);