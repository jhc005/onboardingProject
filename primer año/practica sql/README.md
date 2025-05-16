# Ejercicios de Práctica SQL

## Descripción
Este repositorio contiene ejercicios de práctica SQL enfocados en la creación y manipulación de una base de datos para un sistema de tienda en línea.

## Estructura del Proyecto

### Práctica 1: Estructura de la Base de Datos (`practica1.sql`)

#### Esquema de la Base de Datos
La base de datos `online_store` contiene las siguientes tablas:

##### Tabla: categories
- `id` - *INT* - Clave Primaria, Auto-incremento
- `name` - *VARCHAR(100)* - Nombre de la categoría

##### Tabla: products
- `id` - *INT* - Clave Primaria, Auto-incremento
- `name` - *VARCHAR(100)* - Nombre del producto
- `description` - *TEXT* - Descripción del producto
- `price` - *DECIMAL(10,2)* - Precio
- `stock` - *INT* - Cantidad en inventario
- `category_id` - *INT* - Clave Foránea a categorías
- `update_date` - *DATETIME* - Fecha de actualización

##### Tabla: customers
- `id` - *INT* - Clave Primaria, Auto-incremento
- `first_name` - *VARCHAR(100)* - Nombre
- `last_name` - *VARCHAR(100)* - Apellido
- `email` - *VARCHAR(150)* - Correo electrónico único
- `phone` - *VARCHAR(20)* - Teléfono
- `registration_date` - *DATETIME* - Fecha de registro
- `active` - *BOOLEAN* - Estado del cliente

##### Tabla: orders
- `id` - *INT* - Clave Primaria, Auto-incremento
- `customer_id` - *INT* - Clave Foránea a clientes
- `order_date` - *DATETIME* - Fecha del pedido
- `status` - *VARCHAR(50)* - Estado del pedido
- `total` - *DECIMAL(10,2)* - Total del pedido

##### Tabla: order_details
- `order_id` - *INT* - Clave Primaria/Foránea a pedidos
- `product_id` - *INT* - Clave Primaria/Foránea a productos
- `quantity` - *INT* - Cantidad
- `unit_price` - *DECIMAL(10,2)* - Precio unitario

### Práctica 2: Consultas SQL (`practica2.sql`)

#### Contenido
- Inserción de datos de ejemplo
- Consultas de selección ordenadas
- Consultas con joins
- Creación de vistas
- Actualizaciones y eliminaciones
- Manejo de transacciones

## Relaciones entre Tablas
```
categorías 1 --- * productos
clientes    1 --- * pedidos
pedidos     1 --- * detalles_pedido
productos   1 --- * detalles_pedido
```

## Objetivos de Aprendizaje
- [x] Crear y modificar esquemas de bases de datos
- [x] Trabajar con claves primarias y foráneas
- [x] Manejar diferentes tipos de datos
- [x] Modificar estructuras de tablas existentes
- [x] Crear relaciones entre tablas
- [x] Realizar consultas complejas
- [x] Gestionar transacciones

## Tecnologías Utilizadas
- MySQL
- SQL DDL (Lenguaje de Definición de Datos)
- SQL DML (Lenguaje de Manipulación de Datos)

## Modificaciones Realizadas
1. Se agregó campo `active` (BOOLEAN) a tabla customers
2. Se agregó campo `update_date` (DATETIME) a tabla products
3. Se modificó campo `phone` en tabla customers (VARCHAR(20))