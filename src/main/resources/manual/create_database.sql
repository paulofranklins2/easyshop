USE sys;

# ---------------------------------------------------------------------- #
# Target DBMS:           MySQL                                           #
# Project name:          EasyShop                                        #
# ---------------------------------------------------------------------- #
DROP DATABASE IF EXISTS easyshop;

CREATE DATABASE IF NOT EXISTS easyshop;

USE easyshop;

# ---------------------------------------------------------------------- #
# Tables                                                                 #
# ---------------------------------------------------------------------- #

CREATE TABLE users
(
    user_id         INT          NOT NULL AUTO_INCREMENT,
    username        VARCHAR(50)  NOT NULL,
    hashed_password VARCHAR(255) NOT NULL,
    role            VARCHAR(50)  NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE profiles
(
    user_id     INT          NOT NULL,
    first_name  VARCHAR(50)  NOT NULL,
    last_name   VARCHAR(50)  NOT NULL,
    phone       VARCHAR(20)  NOT NULL,
    email       VARCHAR(200) NOT NULL,
    address     VARCHAR(200) NOT NULL,
    city        VARCHAR(50)  NOT NULL,
    state       VARCHAR(50)  NOT NULL,
    zip         VARCHAR(20)  NOT NULL,
    photo_url   VARCHAR(255),
    profile_url VARCHAR(255),
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE categories
(
    category_id INT          NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    PRIMARY KEY (category_id)
);

CREATE TABLE products
(
    product_id  INT            NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100)   NOT NULL,
    price       DECIMAL(10, 2) NOT NULL,
    category_id INT            NOT NULL,
    description TEXT,
    color       VARCHAR(20),
    image_url   VARCHAR(200),
    stock       INT            NOT NULL DEFAULT 0,
    featured    BOOL           NOT NULL DEFAULT 0,
    PRIMARY KEY (product_id),
    FOREIGN KEY (category_id) REFERENCES categories (category_id)
);

CREATE TABLE orders
(
    order_id        INT            NOT NULL AUTO_INCREMENT,
    user_id         INT            NOT NULL,
    date            DATETIME       NOT NULL,
    address         VARCHAR(100)   NOT NULL,
    city            VARCHAR(50)    NOT NULL,
    state           VARCHAR(50)    NOT NULL,
    zip             VARCHAR(20)    NOT NULL,
    shipping_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    PRIMARY KEY (order_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE order_line_items
(
    order_line_item_id INT            NOT NULL AUTO_INCREMENT,
    order_id           INT            NOT NULL,
    product_id         INT            NOT NULL,
    sales_price        DECIMAL(10, 2) NOT NULL,
    quantity           INT            NOT NULL,
    discount           DECIMAL(10, 2) NOT NULL DEFAULT 0,
    date               DATETIME       NOT NULL,
    PRIMARY KEY (order_line_item_id),
    FOREIGN KEY (order_id) REFERENCES orders (order_id),
    FOREIGN KEY (product_id) REFERENCES products (product_id)
);

-- new tables
CREATE TABLE shopping_cart
(
    user_id    INT NOT NULL,
    product_id INT NOT NULL,
    quantity   INT NOT NULL DEFAULT 1,
    PRIMARY KEY (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (product_id) REFERENCES products (product_id)
);
