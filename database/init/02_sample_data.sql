-- Sample data for ShoppingMall
-- Password: 'password123' (BCrypt hashed)

-- Insert sample users
INSERT INTO users (username, password, email, full_name, phone, role) VALUES
('admin', '$2a$10$XYZ123ABC...', 'admin@shoppingmall.com', 'Admin User', '010-1234-5678', 'ADMIN'),
('john_doe', '$2a$10$XYZ123ABC...', 'john@example.com', 'John Doe', '010-2222-3333', 'USER'),
('jane_smith', '$2a$10$XYZ123ABC...', 'jane@example.com', 'Jane Smith', '010-4444-5555', 'USER');

-- Insert categories
INSERT INTO categories (name, description, parent_id) VALUES
('Electronics', 'Electronic devices and accessories', NULL),
('Clothing', 'Apparel and fashion items', NULL),
('Books', 'Books and educational materials', NULL),
('Home & Garden', 'Home improvement and garden supplies', NULL);

-- Insert sub-categories
INSERT INTO categories (name, description, parent_id) VALUES
('Smartphones', 'Mobile phones and accessories', 1),
('Laptops', 'Laptop computers', 1),
('Men''s Clothing', 'Clothing for men', 2),
('Women''s Clothing', 'Clothing for women', 2);

-- Insert sample products
INSERT INTO products (name, description, price, stock_quantity, category_id, image_url, is_active) VALUES
-- Electronics
('iPhone 15 Pro', 'Latest Apple smartphone with A17 Pro chip', 1299.99, 50, 5, 'https://example.com/iphone15.jpg', TRUE),
('Samsung Galaxy S24', 'Flagship Android smartphone', 1199.99, 45, 5, 'https://example.com/galaxys24.jpg', TRUE),
('MacBook Pro 16"', 'Professional laptop with M3 Pro chip', 2499.99, 20, 6, 'https://example.com/macbookpro.jpg', TRUE),
('Dell XPS 15', 'High-performance Windows laptop', 1899.99, 15, 6, 'https://example.com/dellxps.jpg', TRUE),

-- Clothing
('Men''s Cotton T-Shirt', 'Comfortable casual t-shirt', 29.99, 100, 7, 'https://example.com/tshirt.jpg', TRUE),
('Men''s Jeans', 'Classic blue denim jeans', 79.99, 80, 7, 'https://example.com/jeans.jpg', TRUE),
('Women''s Summer Dress', 'Elegant summer dress', 89.99, 60, 8, 'https://example.com/dress.jpg', TRUE),
('Women''s Blazer', 'Professional business blazer', 149.99, 40, 8, 'https://example.com/blazer.jpg', TRUE),

-- Books
('Clean Code', 'Robert C. Martin - Software engineering book', 49.99, 200, 3, 'https://example.com/cleancode.jpg', TRUE),
('Design Patterns', 'Gang of Four - Classic design patterns', 54.99, 150, 3, 'https://example.com/designpatterns.jpg', TRUE),

-- Home & Garden
('Coffee Maker', 'Automatic drip coffee maker', 89.99, 30, 4, 'https://example.com/coffeemaker.jpg', TRUE),
('Vacuum Cleaner', 'Powerful cordless vacuum', 299.99, 25, 4, 'https://example.com/vacuum.jpg', TRUE);

-- Create sample carts for users
INSERT INTO carts (user_id) VALUES (2), (3);

-- Add items to John's cart (user_id = 2)
INSERT INTO cart_items (cart_id, product_id, quantity) VALUES
(1, 1, 1),  -- iPhone 15 Pro
(1, 5, 2);  -- 2x T-Shirts

-- Add items to Jane's cart (user_id = 3)
INSERT INTO cart_items (cart_id, product_id, quantity) VALUES
(2, 3, 1),  -- MacBook Pro
(2, 9, 1);  -- Clean Code book

-- Create sample orders
INSERT INTO orders (user_id, order_number, total_amount, status, shipping_address, shipping_phone, order_date) VALUES
(2, 'ORD-2025-0001', 1329.98, 'DELIVERED', '123 Main St, Seoul, South Korea', '010-2222-3333', '2025-01-01 10:00:00'),
(3, 'ORD-2025-0002', 2549.98, 'SHIPPED', '456 Oak Ave, Busan, South Korea', '010-4444-5555', '2025-01-05 14:30:00');

-- Order items for first order
INSERT INTO order_items (order_id, product_id, product_name, quantity, price, subtotal) VALUES
(1, 1, 'iPhone 15 Pro', 1, 1299.99, 1299.99),
(1, 5, 'Men''s Cotton T-Shirt', 1, 29.99, 29.99);

-- Order items for second order
INSERT INTO order_items (order_id, product_id, product_name, quantity, price, subtotal) VALUES
(2, 3, 'MacBook Pro 16"', 1, 2499.99, 2499.99),
(2, 9, 'Clean Code', 1, 49.99, 49.99);

-- Sample addresses
INSERT INTO addresses (user_id, recipient_name, phone, address_line1, address_line2, city, state, postal_code, country, is_default) VALUES
(2, 'John Doe', '010-2222-3333', '123 Main St', 'Apt 4B', 'Seoul', 'Seoul', '06000', 'South Korea', TRUE),
(3, 'Jane Smith', '010-4444-5555', '456 Oak Ave', NULL, 'Busan', 'Busan', '48000', 'South Korea', TRUE);
