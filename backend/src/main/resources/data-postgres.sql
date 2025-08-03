-- Insert roles
INSERT INTO roles (id, role_name)
VALUES
    (1, 'ADMIN'),
    (2, 'CUSTOMER'),
    (3, 'SELLER')
ON CONFLICT DO NOTHING;

-- Insert users
INSERT INTO users (id, email, password_hash, full_name)
VALUES
    (1, 'admin@example.com', '$2a$10$XngNY998NVUE6wdUiNQCZuSuhztMhIplgHmHRVpy2IFpEylGU/qba', 'Admin User'),
    (2, 'john@example.com', '$2a$10$jn788Bba8CVfoubYTq9lyeJ4rQ/ym8e5Ecd022Ka.0gjg19qPeleO', 'John Doe')
ON CONFLICT DO NOTHING;

-- Assign roles
INSERT INTO user_roles (user_id, role_id)
VALUES
    (1, 1),
    (2, 2)
ON CONFLICT DO NOTHING;

-- Insert categories
INSERT INTO categories (id, name, description)
VALUES
    (1, 'Books', 'Fiction, non-fiction, academic books'),
    (2, 'Electronics', 'Devices and accessories')
ON CONFLICT DO NOTHING;

-- Insert products
INSERT INTO products (id, name, description, price, category_id, seller_id, image_url, is_active)
VALUES
    (1, 'Java 7th Edition', 'A book about Java Programming Language', 42.50, 1, 1, '/product_assets/java_7th_1.jpeg', TRUE),
    (2, 'Headphones', 'Over-ear headphones', 199.99, 2, 1, '/product_assets/headphones_1.jpeg', TRUE)
ON CONFLICT DO NOTHING;

-- Insert inventory
INSERT INTO inventory (product_id, quantity)
VALUES
    (1, 10),
    (2, 5)
ON CONFLICT DO NOTHING;

-- Insert shopping cart
INSERT INTO shopping_cart (id, user_id)
VALUES
    (1, 1),
    (2, 2)
ON CONFLICT DO NOTHING;

-- Insert cart items
INSERT INTO cart_items (id, cart_id, product_id, quantity)
VALUES
    (1, 1, 1, 2),
    (2, 1, 2, 1)
ON CONFLICT DO NOTHING;

-- Insert payment method for John
INSERT INTO payments (id, user_id, name)
VALUES
    (1, 1, 'Visa Ending 3456'),
    (2, 2, 'Visa Ending 1234')
ON CONFLICT DO NOTHING;

-- Insert shipping address for John
INSERT INTO addresses (
    id, user_id, full_name, address_line1, city, postal_code, country, phone_number
)
VALUES
    (1, 1, 'Admin Admin', '555 Second Street', 'SpringPool', '56789', 'USA', '111-9823'),
    (2, 2, 'John Doe', '123 Main Street', 'Springfield', '12345', 'USA', '555-1234')
ON CONFLICT DO NOTHING;

-- Insert order
INSERT INTO orders (id, user_id, total_amount, address_id, payment_id)
VALUES
    (1, 2, 284.99, 1, 1),
    (2, 1, 284.99, 1, 1)
ON CONFLICT DO NOTHING;

-- Insert order items
INSERT INTO order_items (id, order_id, product_id, quantity, price_per_unit)
VALUES
    (1, 1, 1, 2, 42.50),
    (2, 1, 2, 1, 199.99),
    (3, 2, 1, 2, 42.50),
    (4, 2, 2, 1, 199.99)
ON CONFLICT DO NOTHING;

-- Insert order item statuses
INSERT INTO order_item_status (id, order_item_id, status, updated_at)
VALUES
    (1, 1, 'processing', TO_TIMESTAMP(1705333800000 / 1000.0)),
    (2, 2, 'shipped', TO_TIMESTAMP(1705424400000 / 1000.0)),
    (3, 3, 'delivered', TO_TIMESTAMP(1705183500000 / 1000.0)),
    (4, 4, 'processing', TO_TIMESTAMP(1705502100000 / 1000.0))
ON CONFLICT DO NOTHING;

-----

-- Reset sequences after inserting initial data with specific IDs

-- Reset USERS sequence
SELECT SETVAL(pg_get_serial_sequence('users', 'id'), (SELECT COALESCE(MAX(id), 0) + 1 FROM users), false);

-- Reset ROLES sequence
SELECT SETVAL(pg_get_serial_sequence('roles', 'id'), (SELECT COALESCE(MAX(id), 0) + 1 FROM roles), false);

-- Reset CATEGORIES sequence
SELECT SETVAL(pg_get_serial_sequence('categories', 'id'), (SELECT COALESCE(MAX(id), 0) + 1 FROM categories), false);

-- Reset PRODUCTS sequence
SELECT SETVAL(pg_get_serial_sequence('products', 'id'), (SELECT COALESCE(MAX(id), 0) + 1 FROM products), false);

-- Reset SHOPPING_CART sequence
SELECT SETVAL(pg_get_serial_sequence('shopping_cart', 'id'), (SELECT COALESCE(MAX(id), 0) + 1 FROM shopping_cart), false);

-- Reset CART_ITEMS sequence
SELECT SETVAL(pg_get_serial_sequence('cart_items', 'id'), (SELECT COALESCE(MAX(id), 0) + 1 FROM cart_items), false);

-- Reset PAYMENTS sequence
SELECT SETVAL(pg_get_serial_sequence('payments', 'id'), (SELECT COALESCE(MAX(id), 0) + 1 FROM payments), false);

-- Reset ADDRESSES sequence
SELECT SETVAL(pg_get_serial_sequence('addresses', 'id'), (SELECT COALESCE(MAX(id), 0) + 1 FROM addresses), false);

-- Reset ORDERS sequence
SELECT SETVAL(pg_get_serial_sequence('orders', 'id'), (SELECT COALESCE(MAX(id), 0) + 1 FROM orders), false);

-- Reset ORDER_ITEMS sequence
SELECT SETVAL(pg_get_serial_sequence('order_items', 'id'), (SELECT COALESCE(MAX(id), 0) + 1 FROM order_items), false);

-- Reset ORDER_ITEM_STATUS sequence
SELECT SETVAL(pg_get_serial_sequence('order_item_status', 'id'), (SELECT COALESCE(MAX(id), 0) + 1 FROM order_item_status), false);