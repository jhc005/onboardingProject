# Sistema de Gestión de Productos y Categorías

## Descripción General
Este proyecto implementa un sistema básico de gestión de productos y sus categorías en Java, siguiendo principios de código limpio y buenas prácticas de programación orientada a objetos.

## Estructura del Proyecto

### 1. Clase Product
#### Atributos
- `name` (String): Nombre del producto
- `description` (String): Descripción detallada del producto
- `price` (double): Precio del producto
- `stock` (int): Cantidad disponible
- `category` (Category): Categoría asociada al producto

#### Métodos Principales
- Constructor: Inicializa un producto con sus atributos básicos
- Getters/Setters: Para acceso controlado a los atributos
- `setCategory()`: Asigna una categoría al producto
- `removeCategory()`: Elimina la asociación con una categoría
- `getSummary()`: Genera un resumen completo del producto

### 2. Clase Category
#### Atributos
- `name` (String): Nombre de la categoría

#### Métodos
- Constructor: Inicializa la categoría con su nombre
- Getter/Setter: Para el manejo del nombre

## Principios de Diseño Aplicados

### Encapsulamiento
- Atributos privados con acceso controlado mediante métodos públicos
- Protección de la integridad de los datos

### Responsabilidad Única
- Cada clase tiene un propósito específico y bien definido
- Product maneja la información del producto
- Category gestiona la categorización

### Acoplamiento Débil
- Las clases mantienen una relación flexible
- Un producto puede existir sin categoría
- Las categorías son independientes de los productos

## Ejemplos de Uso

```java
// Crear una categoría
Category electronicos = new Category("Electrónicos");

// Crear un producto
Product smartphone = new Product("iPhone 13", "Smartphone Apple", 999.99, 50);

// Asociar producto con categoría
smartphone.setCategory(electronicos);

// Obtener información del producto
System.out.println(smartphone.getSummary());
```

## Posibles Mejoras Futuras
1. Validación de datos
   - Precios no negativos
   - Stock mínimo
   - Nombres no vacíos

2. Funcionalidades adicionales
   - Sistema de descuentos
   - Gestión de inventario
   - Múltiples categorías por producto
   - Subcategorías

3. Persistencia de datos
   - Conexión con base de datos
   - Exportación/importación de datos

## Requisitos del Sistema
- Java 8 o superior
- IDE compatible con Java (Eclipse, IntelliJ, etc.)

## Autor
[Javier]


