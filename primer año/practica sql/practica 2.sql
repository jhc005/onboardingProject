/*Ejercicio2.1*/

INSERT INTO categories (name) VALUES
('Electronica'),
('Ropa'),
('Libros');

INSERT INTO products (name, description, price, stock, category_id) VALUES
('Smartphone', 'Smartphone with OLED display', 599.99, 50, 1),             
('Bluetooth Headphones', 'Wireless headphones with noise cancellation', 89.50, 30, 1), 
('T-Shirt', 'Unisex cotton T-shirt', 14.99, 100, 2),                 
('Jeans', 'Denim jeans for men', 39.90, 40, 2),                          
('Programming Book', 'Learn SQL step by step', 29.95, 25, 3);              


INSERT INTO customers (first_name, last_name, email, phone, active) VALUES
('Laura', 'Gomez', 'laura.gomez@example.com', '123456789', TRUE),
('Carlos', 'Ruiz', 'carlos.ruiz@example.com', '987654321', TRUE),
('Marta', 'Lopez', 'marta.lopez@example.com', '1122334455', FALSE);

INSERT INTO orders (customer_id, status, total) VALUES
(1, 'Completed', 614.98),  
(2, 'Pending', 119.45);    


/*Ejercicio2.2*/

/*productos de mayor a menor*/ 
SELECT *
FROM products
ORDER BY price DESC;


/*productos especificos*/
SELECT p.id,p.`name`,p.`description`,p.price,p.stock
FROM products p
JOIN categories c ON p.category_id=c.id
WHERE c.name ='Electronica';

/*clientes registrados*/
SELECT c.id,c.first_name,c.last_name,c.registration_date
FROM customers c
WHERE c.registration_date >= NOW()- INTERVAL 1 MONTH;

/*detalle especifico pedido*/
SELECT o.id AS order_id,o.order_date,o.`status`,o.total,c.first_name,c.last_name,c.email,od.product_id,p.`name` AS product_name,(od.quantity*od.unit_price)AS line_total
FROM orders o
JOIN customers c ON o.customer_id=c.id
JOIN order_details od ON o.id=od.order_id
JOIN products p ON od.product_id= p.id
WHERE o.id = 1;

/*consulta precalculada*/
CREATE VIEW order_full_details AS 
SELECT o.id AS order_id,o.order_date,o.`status`,o.total,c.first_name,c.last_name,c.email,od.product_id,p.`name` AS product_name,(od.quantity*od.unit_price)AS line_total
FROM orders o
JOIN customers c ON o.customer_id=c.id
JOIN order_details od ON o.id=od.order_id
JOIN products p ON od.product_id= p.id

-- para utilizarla 
SELECT * FROM order_full_details
WHERE order_id = 2;

/*total ventas categoria*/
SELECT c.`name` AS category_name,SUM(od.quantity*od.unit_price) AS total_sales
FROM order_details od
JOIN products p ON od.product_id=p.id
JOIN categories c ON p.category_id=c.id
GROUP BY c.name
ORDER BY total_sales DESC;

/*consulta precalculada*/
CREATE VIEW categorie_total_sales AS 
SELECT c.`name` AS category_name,SUM(od.quantity*od.unit_price) AS total_sales
FROM order_details od
JOIN products p ON od.product_id=p.id
JOIN categories c ON p.category_id=c.id
GROUP BY c.name;

/*para utilizarla*/
SELECT *FROM categorie_total_sales
WHERE category_name='Electronica';


/*Ejercicio2.3*/
/*aumentar precio un 10%*/
UPDATE products 
SET price = price*1.10
WHERE category_id=(
SELECT c.id
FROM categories c
WHERE c.`name`='Electronica'
);

/*pendiente a enviado*/
UPDATE orders o
SET o.`status`='Completed'
WHERE o.`status`='Pending';

/*eliminar un producto*/
DELETE 
FROM products p
WHERE p.`name`='T-shirt';

/*transaccion*/
START TRANSACTION;

INSERT INTO orders (id,customer_id, status, total)
VALUES(4,1,'Pending',500);


INSERT INTO order_details (order_id, product_id, quantity, unit_price)
VALUES
(4, 1, 1, 300),
(5, 3, 2, 200);


COMMIT;
