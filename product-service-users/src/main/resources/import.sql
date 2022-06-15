INSERT INTO users (password, enabled, name, last_name, age, email, cell_phone, created_at) VALUES ('12345', 1, 'brayan', 'bertrand', '22', 'almibar2@gmail.com', '3320614311', NOW())
INSERT INTO users (password, enabled, name, last_name, age, email, cell_phone, created_at) VALUES ('12345', 1, 'guillermo', 'bertrand', '19', 'guillermo_bh@gmail.com', '3320614312', NOW())

INSERT INTO roles (authority) VALUES ('ROLE_USER');
INSERT INTO roles (authority) VALUES ('ROLE_ADMIN');

INSERT INTO user_roles (user_id, role_id) VALUES (1,1);
INSERT INTO user_roles (user_id, role_id) VALUES (2,1);
INSERT INTO user_roles (user_id, role_id) VALUES (2,2);