-- Insert roles
INSERT OR IGNORE INTO roles (id, role_name)
VALUES
  (1, 'ADMIN'),
  (2, 'CUSTOMER'),
  (3, 'SELLER');

-- Insert users
INSERT OR IGNORE INTO users (id, email, password_hash, full_name)
VALUES
  (1, 'admin@example.com', '$2a$10$XngNY998NVUE6wdUiNQCZuSuhztMhIplgHmHRVpy2IFpEylGU/qba', 'Admin User'),
  (2, 'john@example.com', '$2a$10$jn788Bba8CVfoubYTq9lyeJ4rQ/ym8e5Ecd022Ka.0gjg19qPeleO', 'John Doe');

-- Assign roles
INSERT OR IGNORE INTO user_roles (user_id, role_id)
VALUES
  (1, 1), -- admin is ADMIN
  (2, 2); -- john_doe is CUSTOMER

-- Insert categories
INSERT OR IGNORE INTO categories (id, name, description)
VALUES
    (1, 'Books', 'Fiction, non-fiction, academic books'),
    (2, 'Electronics', 'Devices and accessories'),
    (3, 'Home Goods', 'Items for the house and garden'),
    (4, 'Clothing & Apparel', 'Fashion, accessories, and wear');

-- Insert products
INSERT OR IGNORE INTO products (id, name, description, price, category_id, seller_id, image_url, is_active)
VALUES
    (1, 'Java 7th Edition', 'A book about Java Programming Language', 42.50, 1, 1, '/product_assets/java_7th_1.jpeg', true),
    (2, 'Headphones', 'Over-ear headphones', 199.99, 2, 1, '/product_assets/headphones_1.jpeg', true),
    (3, 'Advanced Python Guide', 'A comprehensive guide to advanced Python programming', 55.00, 1, 1, '/product_assets/python_book.jpeg', true),
    (4, 'The Art of Cooking', 'Recipe book for culinary enthusiasts', 30.00, 1, 1, '/product_assets/cooking_book.jpeg', true),
    (5, 'Space Exploration History', 'A journey through the history of space exploration', 25.00, 1, 1, '/product_assets/space_book.jpeg', true),
    (6, 'Introduction to AI', 'Fundamental concepts of Artificial Intelligence', 48.00, 1, 1, '/product_assets/ai_book.jpeg', true),
    (7, 'Classic Literature Anthology', 'Collection of timeless literary works', 35.00, 1, 1, '/product_assets/classic_lit.jpeg', true),
    (8, 'Wireless Earbuds', 'Noise-cancelling earbuds with long battery life', 79.99, 2, 1, '/product_assets/earbuds.jpeg', true),
    (9, 'Smartwatch', 'Fitness tracker and notification hub', 120.00, 2, 1, '/product_assets/smartwatch.jpeg', true),
    (10, 'Portable Bluetooth Speaker', 'Compact speaker with powerful sound', 45.00, 2, 1, '/product_assets/bluetooth_speaker.jpeg', true),
    (11, 'USB-C Hub', 'Multi-port adapter for modern laptops', 29.99, 2, 1, '/product_assets/usbc_hub.jpeg', true),
    (12, 'Gaming Mouse', 'High-precision gaming mouse with customizable RGB', 60.00, 2, 1, '/product_assets/gaming_mouse.jpeg', true),
    (13, 'Coffee Maker', 'Automatic drip coffee maker with timer', 85.00, 3, 1, '/product_assets/coffee_maker.jpeg', true),
    (14, 'Air Fryer', 'Healthy cooking appliance for crispy meals', 99.99, 3, 1, '/product_assets/air_fryer.jpeg', true),
    (15, 'Smart LED Light Bulb', 'Dimmable smart bulb with color changing capabilities', 15.00, 3, 1, '/product_assets/smart_bulb.jpeg', true),
    (16, 'Robotic Vacuum Cleaner', 'Automated vacuum with smart mapping', 250.00, 3, 1, '/product_assets/robot_vacuum.jpeg', true),
    (17, 'Blender', 'High-speed blender for smoothies and shakes', 70.00, 3, 1, '/product_assets/blender.jpeg', true),
    (18, 'Men''s Casual T-Shirt', 'Comfortable cotton t-shirt for everyday wear', 20.00, 4, 1, '/product_assets/men_shirt.jpeg', true),
    (19, 'Women''s Denim Jeans', 'Classic fit denim jeans', 50.00, 4, 1, '/product_assets/women_jeans.jpeg', true),
    (20, 'Unisex Hoodie', 'Warm and stylish hoodie with front pocket', 40.00, 4, 1, '/product_assets/unisex_hoodie.jpeg', true),
    (21, 'Running Shoes', 'Lightweight athletic shoes for jogging', 85.00, 4, 1, '/product_assets/running_shoes.jpeg', true),
    (22, 'Winter Scarf', 'Soft wool scarf for cold weather', 25.00, 4, 1, '/product_assets/winter_scarf.jpeg', true);

-- Insert inventory
INSERT OR IGNORE INTO inventory (product_id, quantity)
VALUES
    (1, 10), (2, 5),
    (3, 15), (4, 20), (5, 12), (6, 18), (7, 25),
    (8, 30), (9, 10), (10, 40), (11, 50), (12, 15),
    (13, 8), (14, 11), (15, 60), (16, 0), (17, 13),
    (18, 100), (19, 70), (20, 80), (21, 25), (22, 35);

-- Insert shopping cart
INSERT OR IGNORE INTO shopping_cart (id, user_id)
VALUES
   (1, 1),
   (2, 2);

-- Insert cart items
INSERT OR IGNORE INTO cart_items (id, cart_id, product_id, quantity)
VALUES
  (1, 1, 1, 2),
  (2, 1, 2, 1);

-- Insert payment method for John
INSERT OR IGNORE INTO payments (id, user_id, name)
VALUES
  (1, 1, 'Visa Ending 3456'),
  (2, 2, 'Visa Ending 1234');

-- Insert shipping address for John
INSERT OR IGNORE INTO addresses (
  id, user_id, full_name, address_line1, city, postal_code, country, phone_number)
VALUES
  (1, 1, 'Admin Admin', '555 Second Street', 'SpringPool', '56789', 'USA', '111-9823'),
  (2, 2, 'John Doe', '123 Main Street', 'Springfield', '12345', 'USA', '555-1234');

-- Insert order
INSERT OR IGNORE INTO orders (id, user_id, total_amount, address_id, payment_id)
VALUES
  (1, 2, 284.99, 1, 1),
  (2, 1, 284.99, 1, 1);

-- Insert order items
INSERT OR IGNORE INTO order_items (id, order_id, product_id, quantity, price_per_unit)
VALUES
  (1, 1, 1, 2, 42.50),
  (2, 1, 2, 1, 199.99),
  (3, 2, 1, 2, 42.50),
  (4, 2, 2, 1, 199.99);

-- Insert order item statuses (Timed in UNIX TIMESTAMPS)
INSERT OR IGNORE INTO order_item_status (id, order_item_id, status, updated_at)
VALUES
  (1, 1, 'processing', 1705333800000), -- 2024-01-15 10:30:00 UTC (in milliseconds)
  (2, 2, 'shipped', 1705424400000),    -- 2024-01-16 14:20:00 UTC
  (3, 3, 'delivered', 1705183500000),  -- 2024-01-14 16:45:00 UTC
  (4, 4, 'processing', 1705502100000); -- 2024-01-17 09:15:00 UTC