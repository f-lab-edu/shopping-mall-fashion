CREATE TABLE users (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    password  VARCHAR(255) NOT NULL,
    real_name VARCHAR(45) NOT NULL,
    cellphone_number VARCHAR(45) NOT NULL,
    nickname VARCHAR(45) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    withdrawal BOOLEAN DEFAULT FALSE,
    is_email_verified BOOLEAN DEFAULT FALSE,
    is_phone_number_verified BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id),
    UNIQUE (email),
    UNIQUE (cellphone_number),
    UNIQUE (nickname)
);

CREATE TABLE admins (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    user_id BIGINT(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    INDEX fk_user_admin (user_id)
);

CREATE TABLE stores (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    name VARCHAR(45) NOT NULL,
    logo VARCHAR(255),
    description VARCHAR(1000),
    business_registration_number VARCHAR(45) NOT NULL,
    review_cnt BIGINT DEFAULT 0,
    manager_id BIGINT(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sale_state ENUM('PREPARING', 'ON_SALE', 'ON_STOPPAGE', 'WITHDRAWAL') NOT NULL DEFAULT 'PREPARING',
    PRIMARY KEY (id),
    FOREIGN KEY (manager_id) REFERENCES users (id),
    INDEX fk_stores_user (manager_id),
    UNIQUE (name),
    UNIQUE (business_registration_number)
);

CREATE TABLE crews (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    password  VARCHAR(255) NOT NULL,
    name VARCHAR(45) NOT NULL,
    cellphone_number VARCHAR(45) NOT NULL,
    approved BOOLEAN DEFAULT FALSE,
    withdrawal BOOLEAN DEFAULT FALSE,
    store_id BIGINT(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (store_id) REFERENCES stores (id),
    INDEX fk_store_crews (store_id),
    UNIQUE (email),
    UNIQUE (cellphone_number)
);

CREATE TABLE roles (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    role ENUM('USER', 'ITEM_MANAGER', 'STOCK_MANAGER', 'CREW_MANAGER', 'STORE_MANAGER_BEFORE_APPROVAL', 'STORE_MANAGER', 'ADMIN') NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (role)
);

CREATE TABLE authorities (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    authority ENUM('ROLE_USER', 'ITEM_MANAGEMENT', 'STOCK_MANAGEMENT', 'CREW_MANAGEMENT', 'STORE_MANAGEMENT', 'STORE_APPROVAL', 'ROLE_ADMIN') NOT NULL DEFAULT 'ROLE_USER',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (authority)
);

CREATE TABLE role_authorities (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    role_id BIGINT(20) NOT NULL,
    authority_id BIGINT(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (role_id) REFERENCES roles (id),
    FOREIGN KEY (authority_id) REFERENCES authorities (id),
    INDEX fk_role_authorities (role_id),
    INDEX fk_authorities (authority_id)
);

CREATE TABLE crew_roles (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    crew_id BIGINT(20) NOT NULL,
    role_id BIGINT(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (crew_id) REFERENCES crews (id),
    FOREIGN KEY (role_id) REFERENCES roles (id),
    INDEX fk_crew (crew_id),
    INDEX fk_role (role_id)
);

CREATE TABLE refresh_tokens (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    token VARCHAR(255) NOT NULL,
    expiration DATETIME NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_token (token)
);

CREATE TABLE addresses (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    recipient_name VARCHAR(45) NOT NULL,
    road_address  VARCHAR(100) NOT NULL,
    address_detail VARCHAR(255) NOT NULL,
    zipcode VARCHAR(255) NOT NULL,
    recipient_cellphone VARCHAR(45) NOT NULL,
    user_id BIGINT(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    INDEX fk_address_user (user_id)
);

CREATE TABLE large_categories (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    name VARCHAR(45) NOT NULL,
    sub_category_cnt INT NOT NULL DEFAULT 0,
    item_cnt INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE(name)
);

CREATE TABLE categories (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    name VARCHAR(45) NOT NULL,
    item_count INT NOT NULL DEFAULT 0,
    large_category_id BIGINT(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (large_category_id) REFERENCES large_categories (id),
    INDEX fk_large_category (large_category_id),
    UNIQUE(name)
);

CREATE TABLE items (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    original_price INT NOT NULL,
    sale_price INT NOT NULL,
    description VARCHAR(1000),
    sex ENUM('UNISEX', 'WOMEN', 'MEN') NOT NULL,
    sale_state ENUM('PREPARING', 'ON_SALE', 'TEMPORARILY_SOLD_OUT', 'SOLD_OUT', 'END_OF_PRODUCTION') NOT NULL DEFAULT 'PREPARING',
    sale_state_changed_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    store_id BIGINT(20) NOT NULL,
    category_id BIGINT(20) NOT NULL,
    lastly_modified_by BIGINT(20) NOT NULL,
    order_count BIGINT NOT NULL DEFAULT 0,
    view_count BIGINT NOT NULL DEFAULT 0,
    review_count BIGINT NOT NULL DEFAULT 0,
    review_grade FLOAT NOT NULL DEFAULT 0,
    sale_grade BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (store_id) REFERENCES stores (id),
    FOREIGN KEY (category_id) REFERENCES categories (id),
    FOREIGN KEY (lastly_modified_by) REFERENCES crews (id),
    INDEX fk_items_store (store_id),
    INDEX fk_items_category (category_id)
);

CREATE TABLE item_options (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    name VARCHAR(45) NOT NULL,
    size VARCHAR(10) NOT NULL,
    option_value VARCHAR(25),
    item_id BIGINT(20) NOT NULL,
    sale_state ENUM('PREPARING', 'ON_SALE', 'TEMPORARILY_SOLD_OUT', 'SOLD_OUT', 'END_OF_PRODUCTION') NOT NULL DEFAULT 'PREPARING',
    sale_state_changed_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    stocks_count BIGINT NOT NULL DEFAULT 0,
    order_count BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (item_id) REFERENCES items (id),
    UNIQUE (item_id, size, option_value),
    INDEX fk_options_item (item_id)
);

CREATE TABLE item_thumbnail_images (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    url VARCHAR(225) NOT NULL,
    item_id BIGINT(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (item_id) REFERENCES items (id),
    INDEX fk_item_thumbnail_images_item (item_id)
);

CREATE TABLE item_images (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    url VARCHAR(225) NOT NULL,
    item_id BIGINT(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (item_id) REFERENCES items (id),
    INDEX fk_item_images_item (item_id)
);

CREATE TABLE item_description_images (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    url VARCHAR(225) NOT NULL,
    image_order INT default 1,
    item_id BIGINT(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (item_id) REFERENCES items (id),
    INDEX fk_description_images_item (item_id)
);

CREATE TABLE item_option_images (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    url VARCHAR(225) NOT NULL,
    item_option_id BIGINT(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (item_option_id) REFERENCES item_options (id),
    INDEX fk_images_item_option (item_option_id)
);

CREATE TABLE orders (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_status ENUM('PREPARING', 'COMPLETED', 'CANCELLED') DEFAULT 'PREPARING' NOT NULL,
    payment_status ENUM('ON_PAYMENT', 'PAYMENT_CANCELLED', 'PAID', 'PAYMENT_FAILED', 'ON_REFUND', 'REFUND_COMPLETE') DEFAULT 'ON_PAYMENT' NOT NULL,
    delivery_status ENUM('PREPARING', 'ON_DELIVERY', 'DELIVERED') DEFAULT 'PREPARING' NOT NULL,
    item_option_id BIGINT NOT NULL,
    order_amount INT NOT NULL,
    total_price INT NOT NULL,
    discounted_amount INT NOT NULL,
    payment_amount INT NOT NULL,
    orderer_id BIGINT NOT NULL,
    recipient_name VARCHAR(20) NOT NULL,
    road_address VARCHAR(100) NOT NULL,
    address_detail VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (item_option_id) REFERENCES item_options (id),
    FOREIGN KEY (orderer_id) REFERENCES users (id),
    INDEX fk_orders_item_option (item_option_id),
    INDEX fk_orders_orderer (orderer_id)
);

CREATE TABLE coupons (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    amounts BIGINT,
    discount_type ENUM('FIXED_PRICE_DISCOUNT', 'RATE_DISCOUNT') NOT NULL,
    discount_amount INT NOT NULL DEFAULT 0,
    discount_unit ENUM('PERCENTAGE', 'KRW') NOT NULL,
    validation_in_minutes BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_coupons (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    coupon_id BIGINT,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    used_item_id BIGINT,
    used_order_id BIGINT,
    expired_at DATETIME NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (coupon_id) REFERENCES coupons (id),
    INDEX idx_coupons_used_item (used_item_id),
    INDEX idx_coupons_used_order (used_order_id),
    INDEX idx_coupons_user (user_id)
);

CREATE TABLE new_store_register_requests (
    id BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    requester_name VARCHAR(45) NOT NULL,
    requester_email VARCHAR(45) NOT NULL,
    requester_phone_number VARCHAR(45) NOT NULL,
    business_registration_number VARCHAR(45) NOT NULL,
    approved BOOLEAN DEFAULT FALSE,
    approved_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Insert users
INSERT INTO users (email, password, real_name, cellphone_number, nickname, created_at, updated_at, withdrawal)
VALUES
    ('user1@example.com', '$2a$10$VRXYNlRyf6TQP1akzgq6f.P0RngL/5i5r21BG1MkOTDL9nFyZQCbu', 'User One', '01012345621', 'userone', NOW(), NOW(), false),
    ('user2@example.com', '$2a$10$VRXYNlRyf6TQP1akzgq6f.P0RngL/5i5r21BG1MkOTDL9nFyZQCbu', 'User Two', '01012345671', 'usertwo', NOW(), NOW(), false),
    ('user3@example.com', '$2a$10$VRXYNlRyf6TQP1akzgq6f.P0RngL/5i5r21BG1MkOTDL9nFyZQCbu', 'User Three', '01012345672', 'userthree', NOW(), NOW(), false),
    ('user4@example.com', '$2a$10$VRXYNlRyf6TQP1akzgq6f.P0RngL/5i5r21BG1MkOTDL9nFyZQCbu', 'User Four', '01012345673', 'userfour', NOW(), NOW(), false),
    ('user5@example.com', '$2a$10$VRXYNlRyf6TQP1akzgq6f.P0RngL/5i5r21BG1MkOTDL9nFyZQCbu', 'User Five', '01012345674', 'userfive', NOW(), NOW(), false),
    ('user6@example.com', '$2a$10$VRXYNlRyf6TQP1akzgq6f.P0RngL/5i5r21BG1MkOTDL9nFyZQCbu', 'User Six', '01012345675', 'usersix', NOW(), NOW(), false),
    ('user7@example.com', '$2a$10$VRXYNlRyf6TQP1akzgq6f.P0RngL/5i5r21BG1MkOTDL9nFyZQCbu', 'User Seven', '01012345676', 'userseven', NOW(), NOW(), false),
    ('user8@example.com', '$2a$10$VRXYNlRyf6TQP1akzgq6f.P0RngL/5i5r21BG1MkOTDL9nFyZQCbu', 'User Eight', '01012345677', 'usereight', NOW(), NOW(), false),
    ('user9@example.com', '$2a$10$VRXYNlRyf6TQP1akzgq6f.P0RngL/5i5r21BG1MkOTDL9nFyZQCbu', 'User Nine', '01012345678', 'usernine', NOW(), NOW(), false),
    ('user10@example.com', '$2a$10$VRXYNlRyf6TQP1akzgq6f.P0RngL/5i5r21BG1MkOTDL9nFyZQCbu', 'User Ten', '01012345679', 'userten', NOW(), NOW(), false);


-- Insert admins
INSERT INTO admins (user_id) VALUES (1);

-- Insert stores
INSERT INTO stores (name, logo, description, business_registration_number, review_cnt, manager_id, created_at, updated_at, sale_state)
VALUES
    ('Store One', 'logo1.png', 'A great store for various itemOptions.', '1234567890', 10, 1, NOW(), NOW(), 'ON_SALE'),
    ('Store Two', 'logo2.png', 'Best quality itemOptions at affordable prices.', '2345678901', 5, 3, NOW(), NOW(), 'ON_SALE'),
    ('Store Three', 'logo3.png', 'Your go-to store for all essentials.', '3456789012', 8, 3, NOW(), NOW(), 'ON_SALE'),
    ('Store Four', 'logo4.png', 'The ultimate destination for stylish items.', '4567890123', 12, 2, NOW(), NOW(), 'ON_SALE'),
    ('Store Five', 'logo5.png', 'Affordable items for everyone.', '5678901234', 15, 1, NOW(), NOW(), 'ON_SALE'),
    ('Store Six', 'logo6.png', 'Discover unique items at our store.', '6789012345', 6, 2, NOW(), NOW(), 'ON_SALE'),
    ('Store Seven', 'logo7.png', 'The best place for exclusive collections.', '7890123456', 20, 3, NOW(), NOW(), 'ON_SALE'),
    ('Store Eight', 'logo8.png', 'Shop quality items at great prices.', '8901234567', 14, 1, NOW(), NOW(), 'ON_SALE'),
    ('Store Nine', 'logo9.png', 'Your neighborhood store for everything.', '9012345678', 18, 2, NOW(), NOW(), 'ON_SALE'),
    ('Store Ten', 'logo10.png', 'Leading store in innovation and trends.', '0123456789', 25, 3, NOW(), NOW(), 'ON_SALE');


-- Insert roles
INSERT INTO roles (role) VALUES ('USER'), ('ITEM_MANAGER'), ('STOCK_MANAGER'), ('CREW_MANAGER'), ('STORE_MANAGER_BEFORE_APPROVAL'), ('STORE_MANAGER'), ('ADMIN');

-- Insert authorities
INSERT INTO authorities (authority) VALUES ('ROLE_USER'), ('ITEM_MANAGEMENT'), ('STOCK_MANAGEMENT'), ('CREW_MANAGEMENT'), ('STORE_MANAGEMENT'), ('STORE_APPROVAL'), ('ROLE_ADMIN');

-- Insert role_authorities
INSERT INTO role_authorities (role_id, authority_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4),
    (5, 5),
    (6, 2),
    (6, 3),
    (6, 4),
    (6, 5),
    (7, 2),
    (7, 3),
    (7, 4),
    (7, 5),
    (7, 6),
    (7, 7);

-- Insert crews
INSERT INTO crews (email, password, name, cellphone_number, approved, withdrawal, store_id, created_at, updated_at)
VALUES
    ('storeManager@store1.com', '$2a$10$VRXYNlRyf6TQP1akzgq6f.P0RngL/5i5r21BG1MkOTDL9nFyZQCbu', 'Crew One', '01012345672', true, false, 1, NOW(), NOW()),
    ('crewManager@store1.com', '$2a$10$VRXYNlRyf6TQP1akzgq6f.P0RngL/5i5r21BG1MkOTDL9nFyZQCbu', 'Crew One', '01022345672', true, false, 1, NOW(), NOW()),
    ('stockManager@store1.com', '$2a$10$VRXYNlRyf6TQP1akzgq6f.P0RngL/5i5r21BG1MkOTDL9nFyZQCbu', 'Crew One', '01032345672', true, false, 1, NOW(), NOW()),
    ('itemManager@store1.com', '$2a$10$VRXYNlRyf6TQP1akzgq6f.P0RngL/5i5r21BG1MkOTDL9nFyZQCbu', 'Crew One', '01014345672', true, false, 1, NOW(), NOW()),
    ('storeManager@store2.com', '$2a$10$VRXYNlRyf6TQP1akzgq6f.P0RngL/5i5r21BG1MkOTDL9nFyZQCbu', 'Crew Two', '01052345673', true, false, 2, NOW(), NOW()),
    ('itemManager@store3.com', '$2a$10$VRXYNlRyf6TQP1akzgq6f.P0RngL/5i5r21BG1MkOTDL9nFyZQCbu', 'Crew Two', '01052311673', true, false, 3, NOW(), NOW());

-- Insert crew_roles
INSERT INTO crew_roles (crew_id, role_id, created_at, updated_at)
VALUES
    (1, 6, NOW(), NOW()),
    (1, 4, NOW(), NOW()),
    (1, 3, NOW(), NOW()),
    (1, 2, NOW(), NOW()),
    (2, 4, NOW(), NOW()),
    (3, 3, NOW(), NOW()),
    (4, 2, NOW(), NOW()),
    (5, 6, NOW(), NOW()),
    (5, 4, NOW(), NOW()),
    (5, 3, NOW(), NOW()),
    (5, 2, NOW(), NOW()),
    (6, 5, NOW(), NOW());


-- Insert refresh tokens
INSERT INTO refresh_tokens (token, expiration, created_at, updated_at)
VALUES
    ('eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJVU0VSIHVzZXIxQGV4YW1wbGUuY29tIiwiZXhwIjoxNzIxNDM3MjIwfQ.8x2AnrRJijnudTNNXhJmLsFXX9lY206FyNnKff4OOZWjUBpfSvo4Hq3Gv_gWu24rJ_E5QylWMYsHPrwhnS_hFw',
     '2024-12-31 23:59:59', NOW(), NOW()),
    ('eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJDUkVXIHN0b3JlTWFuYWdlckBzdG9yZTEuY29tIiwiZXhwIjoxNzIxNDM3MzQ0fQ.XKNt8pvw0I865MTTp2yFO3cTEyH8qtVUq6D0IAIHM4ZcPsmpbL_lHcaVXrI6Y3nQEkLF4bM2jJgDUHJxR7gX-A',
     '2024-12-31 23:59:59', NOW(), NOW()),
    ('eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJDUkVXIGNyZXdNYW5hZ2VyQHN0b3JlMS5jb20iLCJleHAiOjE3MjE0Mzc0MTV9.0g1qppe7GMKLcKlxvxVJXZsOzM9-NwlDZtA6qvEtadzwEbtOEkRHBHqT_9LJlvmxicKECK63omZWfsrPn96n6A',
     '2024-12-31 23:59:59', NOW(), NOW()),
    ('eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJDUkVXIHN0b2NrTWFuYWdlckBzdG9yZTEuY29tIiwiZXhwIjoxNzIxNDM3NDY5fQ.yGrXQCC76lxMJmi-lnOhZgkzmiVFbKOWTwiMoBL13zwXxbyvv3FYlxbc6wHPBMfUXCcgPqEfxUjfSRtdp01NVg',
     '2024-12-31 23:59:59', NOW(), NOW()),
    ('eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJDUkVXIGl0ZW1NYW5hZ2VyQHN0b3JlMS5jb20iLCJleHAiOjE3MjE0Mzc1MzB9.SsLacLuUcjFoq6KKJ46quFyZKD3mOFPfEWNu2ZYqjZBAvIFGH2n5wakUlOc69-SSlsitfFYj8MOzRHxd0JGhtA',
     '2024-12-31 23:59:59', NOW(), NOW()),
    ('eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJDUkVXIGl0ZW1NYW5hZ2VyQHN0b3JlMy5jb20iLCJleHAiOjE3MjEyOTAzNjF9.gWIfZuFYY7LsXYfWAxEpHUPjLK5XZA93FHZBPbaV2EdmFoP-4LEfpPTj0Xsf_mcaCvFfeJIsON3-YkmNW_BhSQ',
     '2023-12-31 23:59:59', NOW(), NOW());

-- Insert addresses
INSERT INTO addresses (recipient_name, road_address, address_detail, zipcode, recipient_cellphone, user_id)
VALUES
    ('홍길동', '대한로 111', '101동 101호', '12345', '01051619759', 1),
    ('김철수', '서울로 222', '202동 202호', '54321', '01012345678', 2),
    ('이영희', '부산로 333', '303동 303호', '67890', '01087654321', 3);

-- Insert large categories
INSERT INTO large_categories (name, sub_category_cnt, item_cnt) VALUES
    ('Shirts', 2, 200),
    ('Pants', 2, 150),
    ('Jackets', 2, 100),
    ('Dresses', 2, 120),
    ('Skirts', 2, 80),
    ('Blouses', 2, 90);

-- Insert medium categories
INSERT INTO categories (name, item_count, large_category_id) VALUES
    ('Casual Shirts', 100, 1),
    ('Formal Shirts', 100, 1),
    ('Short Pants', 50, 2),
    ('Blue Jeans', 100, 2),
    ('Leather Jackets', 50, 3),
    ('Denim Jackets', 50, 3),
    ('Summer Dresses', 60, 4),
    ('Evening Gowns', 60, 4),
    ('Mini Skirts', 40, 5),
    ('Long Skirts', 40, 5),
    ('Casual Blouses', 45, 6),
    ('Formal Blouses', 45, 6);

INSERT INTO items (name, original_price, sale_price, description, sex, sale_state, store_id, category_id, lastly_modified_by, order_count, view_count, review_count, review_grade, sale_grade, created_at, updated_at) VALUES
    ('Item 1', 26192, 24972, 'Description for Item 1', 'WOMEN', 'ON_SALE', 9, 8, 4, 476, 3706, 10, 4.3, 1, '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Item 2', 19634, 17835, 'Description for Item 2', 'UNISEX', 'TEMPORARILY_SOLD_OUT', 8, 1, 3, 947, 8738, 75, 3.1, 2, '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Item 3', 30750, 28524, 'Description for Item 3', 'UNISEX', 'ON_SALE', 3, 6, 4, 212, 4690, 12, 3.1, 1, '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Item 4', 48344, 47146, 'Description for Item 4', 'WOMEN', 'ON_SALE', 3, 5, 4, 248, 5481, 38, 3.9, 1, '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Item 5', 17227, 15243, 'Description for Item 5', 'WOMEN', 'END_OF_PRODUCTION', 3, 4, 1, 106, 9907, 60, 4.8, 3, '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Item 6', 38945, 37424, 'Description for Item 6', 'WOMEN', 'PREPARING', 9, 3, 2, 231, 266, 67, 3.2, 1, '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Item 7', 44886, 43291, 'Description for Item 7', 'UNISEX', 'SOLD_OUT', 8, 7, 1, 396, 3736, 22, 3.6, 1, '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Item 8', 16951, 16367, 'Description for Item 8', 'WOMEN', 'ON_SALE', 7, 4, 5, 160, 5419, 74, 4.5, 3, '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Item 9', 37493, 34781, 'Description for Item 9', 'UNISEX', 'END_OF_PRODUCTION', 3, 10, 4, 190, 5716, 70, 3.7, 3, '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Item 10', 14183, 12538, 'Description for Item 10', 'MEN', 'SOLD_OUT', 4, 10, 4, 757, 1886, 19, 4.0, 4, '2024-07-25 15:17:21', '2024-07-25 15:17:21');

INSERT INTO item_options (name, size, option_value, item_id, sale_state, stocks_count, order_count, sale_state_changed_time, created_at, updated_at) VALUES
    ('Option 1', 'M', 'Color: Blue', 1, 'TEMPORARILY_SOLD_OUT', 15, 42, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 2', 'XL', 'Color: Red', 1, 'ON_SALE', 27, 16, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 3', 'XL', 'Color: Black', 1, 'PREPARING', 88, 15, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 4', 'XXL', 'Color: Yellow', 2, 'PREPARING', 70, 17, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 5', 'XL', 'Color: Yellow', 2, 'END_OF_PRODUCTION', 69, 7, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 6', 'M', 'Color: Yellow', 2, 'END_OF_PRODUCTION', 39, 21, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 7', 'M', 'Color: Black', 2, 'PREPARING', 93, 34, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 8', 'L', 'Color: Red', 3, 'PREPARING', 40, 37, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 9', 'M', 'Color: Green', 3, 'TEMPORARILY_SOLD_OUT', 56, 26, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 10', 'XL', 'Color: Red', 3, 'PREPARING', 38, 42, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 11', 'S', 'Color: Red', 3, 'END_OF_PRODUCTION', 50, 3, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 12', 'L', 'Color: Green', 3, 'SOLD_OUT', 94, 7, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 13', 'S', 'Color: Black', 4, 'SOLD_OUT', 16, 7, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 14', 'S', 'Color: Blue', 4, 'TEMPORARILY_SOLD_OUT', 78, 17, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 15', 'XXL', 'Color: Black', 4, 'PREPARING', 85, 45, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 16', 'M', 'Color: Red', 5, 'SOLD_OUT', 13, 36, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 17', 'M', 'Color: Black', 5, 'TEMPORARILY_SOLD_OUT', 80, 38, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 18', 'L', 'Color: Yellow', 5, 'ON_SALE', 86, 42, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 19', 'XXL', 'Color: Black', 5, 'END_OF_PRODUCTION', 23, 23, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 20', 'XL', 'Color: Blue', 6, 'SOLD_OUT', 84, 16, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 21', 'XXL', 'Color: Red', 6, 'END_OF_PRODUCTION', 53, 15, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 22', 'S', 'Color: Green', 6, 'TEMPORARILY_SOLD_OUT', 31, 46, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 23', 'S', 'Color: Red', 6, 'ON_SALE', 73, 3, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 24', 'L', 'Color: Green', 6, 'SOLD_OUT', 54, 36, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 25', 'XL', 'Color: Red', 6, 'PREPARING', 59, 4, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 26', 'XXL', 'Color: Green', 7, 'END_OF_PRODUCTION', 30, 43, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 27', 'M', 'Color: Black', 7, 'PREPARING', 56, 22, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 28', 'S', 'Color: Blue', 7, 'PREPARING', 75, 26, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 29', 'M', 'Color: Yellow', 7, 'END_OF_PRODUCTION', 4, 22, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 30', 'L', 'Color: Black', 7, 'TEMPORARILY_SOLD_OUT', 87, 12, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 31', 'M', 'Color: Yellow', 8, 'END_OF_PRODUCTION', 6, 16, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 32', 'L', 'Color: Blue', 8, 'PREPARING', 27, 3, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 33', 'S', 'Color: Yellow', 8, 'TEMPORARILY_SOLD_OUT', 43, 49, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 34', 'XXL', 'Color: Red', 8, 'TEMPORARILY_SOLD_OUT', 17, 26, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 35', 'XL', 'Color: Yellow', 8, 'ON_SALE', 80, 9, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 36', 'XXL', 'Color: Yellow', 8, 'PREPARING', 36, 48, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 37', 'XXL', 'Color: Yellow', 9, 'TEMPORARILY_SOLD_OUT', 12, 12, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 38', 'XL', 'Color: Green', 9, 'END_OF_PRODUCTION', 74, 8, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 39', 'XL', 'Color: Red', 9, 'END_OF_PRODUCTION', 37, 40, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 40', 'L', 'Color: Blue', 10, 'ON_SALE', 13, 23, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 41', 'XL', 'Color: Blue', 10, 'END_OF_PRODUCTION', 41, 7, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 42', 'M', 'Color: Blue', 10, 'TEMPORARILY_SOLD_OUT', 22, 20, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 43', 'M', 'Color: Green', 10, 'ON_SALE', 80, 8, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21'),
    ('Option 44', 'XL', 'Color: Green', 10, 'SOLD_OUT', 51, 3, '2024-07-25 15:17:21', '2024-07-25 15:17:21', '2024-07-25 15:17:21');