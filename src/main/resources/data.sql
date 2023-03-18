INSERT INTO users (name, email, mobile_number, pwd, role, create_dt) VALUES ('Happy','happy@gmail.com','9876548337', '$2y$12$oRRbkNfwuR8ug4MlzH5FOeui.//1mkd.RsOAJMbykTSupVy.x/vb2', 'admin', CURDATE());
INSERT INTO users (name, email, mobile_number, pwd, role, create_dt) VALUES ('Jayati', 'jayati@gmail.com', '3163392606', '$2a$10$0KbKjtLjBTeMZC4cym7apuesK3sq6sXkUVcFaloSFVTifjJNc6jJW', 'customer', CURDATE());
INSERT INTO authorities (user_id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO authorities (user_id, name) VALUES (2, 'ROLE_CUSTOMER');
