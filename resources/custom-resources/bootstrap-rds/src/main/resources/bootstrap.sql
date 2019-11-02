-- Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
-- 
-- Permission is hereby granted, free of charge, to any person obtaining a copy of this
-- software and associated documentation files (the "Software"), to deal in the Software
-- without restriction, including without limitation the rights to use, copy, modify,
-- merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
-- permit persons to whom the Software is furnished to do so.
-- 
-- THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
-- INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
-- PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
-- HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
-- OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
-- SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

-- Load up the UUID data type
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE category (
	category_id SERIAL PRIMARY KEY,
	category VARCHAR(255) NOT NULL UNIQUE CHECK (category <> '')
);

CREATE TABLE product (
	product_id SERIAL PRIMARY KEY,
	sku VARCHAR(32) NOT NULL UNIQUE CHECK (sku <> ''),
	product VARCHAR(255) NOT NULL UNIQUE CHECK (product <> ''),
	price DECIMAL(9,2) NOT NULL
);

CREATE TABLE product_categories (
	product_id INT NOT NULL REFERENCES product (product_id) ON DELETE CASCADE ON UPDATE CASCADE,
	category_id INT NOT NULL REFERENCES category (category_id) ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT product_categories_pk PRIMARY KEY (product_id, category_id)
);

CREATE TABLE purchaser (
	purchaser_id SERIAL PRIMARY KEY,
	first_name VARCHAR(64),
	last_name VARCHAR(64),
	UNIQUE(first_name, last_name)
);

CREATE TABLE order_fulfillment (
	order_fulfillment_id SERIAL PRIMARY KEY,
	order_date DATE NOT NULL,
	ship_date DATE,
	purchaser_id INTEGER NOT NULL REFERENCES purchaser (purchaser_id) ON DELETE RESTRICT ON UPDATE CASCADE,
	ship_to_line1 VARCHAR(128),
	ship_to_line2 VARCHAR(128),
	ship_to_city VARCHAR(128),
	ship_to_state VARCHAR(128),
	ship_to_postal_code VARCHAR(128),
	bill_to_line1 VARCHAR(128),
	bill_to_line2 VARCHAR(128),
	bill_to_city VARCHAR(128),
	bill_to_state VARCHAR(128),
	bill_to_postal_code VARCHAR(128)
);

CREATE TABLE order_line_item (
	order_line_item_id SERIAL PRIMARY KEY,
	order_fulfillment_id INT NOT NULL REFERENCES order_fulfillment (order_fulfillment_id) ON DELETE RESTRICT ON UPDATE CASCADE,
	product_id INT NOT NULL REFERENCES product (product_id) ON DELETE RESTRICT ON UPDATE CASCADE,
	quantity INT NOT NULL CHECK (quantity > 0),
	unit_purchase_price DECIMAL(9, 2) NOT NULL
);

INSERT INTO category (category) VALUES ('Basketball'), ('Football'), ('Baseball'), ('Soccer');
INSERT INTO product (sku, product, price) VALUES ('ABC', 'ABC Product', 35.00), ('DEF', 'DEF Product', 100.00), ('123', '123 Product', 17.99), ('456', '456 Product', 2897.67);
INSERT INTO product_categories (product_id, category_id) VALUES (1, 3), (2, 1), (3, 4), (4, 2);

INSERT INTO purchaser (first_name, last_name) VALUES ('Santa', 'Claus'), ('Easter', 'Bunny'), ('Tooth', 'Fairy');
INSERT INTO order_fulfillment (purchaser_id, order_date, ship_to_line1, ship_to_city, ship_to_state, ship_to_postal_code, bill_to_line1, bill_to_city, bill_to_state, bill_to_postal_code)
VALUES
(1, CURRENT_DATE - INTERVAL '30 day', 'Kris Kringle Road', 'North Pole City', 'North Pole', '12345', 'Kris Kringle Road', 'North Pole City', 'North Pole', '12345'),
(2, CURRENT_DATE - INTERVAL '10 day', 'Egg Painting Lane', 'Easter Island City', 'Easter Island', '12345', 'Egg Painting Lane', 'Easter Island City', 'Easter Island', '12345'),
(3, CURRENT_DATE - INTERVAL '1 day', 'Tooth Change Exchange Circle', 'Fairy City', 'Dreams', '12345', 'Tooth Change Exchange Circle', 'Fairy City', 'Dreams', '12345');

INSERT INTO order_line_item (order_fulfillment_id, product_id, quantity, unit_purchase_price)
SELECT 1, product_id, 2, price FROM product WHERE sku = 'ABC'
UNION
SELECT 1, product_id, 1, price FROM product WHERE sku = 'DEF'
UNION
SELECT 2, product_id, 1, price FROM product WHERE sku = '123'
UNION
SELECT 2, product_id, 1, price FROM product WHERE sku = '456'
UNION
SELECT 3, product_id, 10, price FROM product WHERE sku = '123'
;


