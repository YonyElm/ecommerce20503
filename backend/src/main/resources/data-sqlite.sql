-- Insert roles
INSERT OR IGNORE INTO roles (role_name) VALUES ('ADMIN');
INSERT OR IGNORE INTO roles (role_name) VALUES ('CUSTOMER');

-- Insert users
INSERT OR IGNORE INTO users (username, password_hash, email, full_name)
VALUES 
  ('admin', '$2a$10$XngNY998NVUE6wdUiNQCZuSuhztMhIplgHmHRVpy2IFpEylGU/qba', 'admin@example.com', 'Admin User'),
  ('john_doe', '$2a$10$jn788Bba8CVfoubYTq9lyeJ4rQ/ym8e5Ecd022Ka.0gjg19qPeleO', 'john@example.com', 'John Doe');

-- Assign roles
INSERT OR IGNORE INTO user_roles (user_id, role_id)
VALUES 
  (1, 1), -- admin is ADMIN
  (2, 2); -- john_doe is CUSTOMER

-- Insert categories
INSERT OR IGNORE INTO categories (name, description)
VALUES 
  ('Books', 'Fiction, non-fiction, academic books'),
  ('Electronics', 'Devices and accessories');

-- Insert products
INSERT OR IGNORE INTO products (name, description, price, category_id)
VALUES 
  ('Java 7th Edition', 'A book about Java Programing Language', 42.50, 1),
  ('Headphones', 'Over-ear headphones', 199.99, 2);

-- Insert inventory
INSERT OR IGNORE INTO inventory (product_id, quantity)
VALUES 
  (1, 10),
  (2, 5);

-- Insert shopping cart for john_doe
INSERT OR IGNORE INTO shopping_cart (user_id)
VALUES (2);

-- Insert cart items
INSERT OR IGNORE INTO cart_items (cart_id, product_id, quantity)
VALUES 
  (1, 1, 2),
  (1, 2, 1);

-- Insert an order
INSERT OR IGNORE INTO orders (user_id, total_amount, status)
VALUES (2, 284.99, 'PENDING');

-- Insert order items
INSERT OR IGNORE INTO order_items (order_id, product_id, quantity, price_per_unit)
VALUES 
  (1, 1, 2, 42.50),
  (1, 2, 1, 199.99);
