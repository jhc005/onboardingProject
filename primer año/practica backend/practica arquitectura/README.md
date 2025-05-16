# Sistema de Gestión de Tareas

## Estructura de Carpetas
```
practica-arquitectura/
├── src/
│   └── main/
│       └── java/
│           ├── presentation/
│           │   └── ConsoleUI.java
│           ├── service/
│           │   └── ProductService.java
│           ├── dao/
│           │   └── ProductDAO.java
│           ├── model/
│           │   └── Product.java
│           ├── Database.java
│           └── Main.java
└── pom.xml
```

## Descripción
Sistema de gestión de tareas desarrollado en Java que implementa una arquitectura en capas (DAO, Service, UI) con persistencia en MySQL.

## Estructura del Proyecto

### Capas de la Aplicación
1. **Capa de Presentación** ([`presentation/ConsoleUI.java`](src/main/java/presentation/ConsoleUI.java))
   - Interfaz de consola para interacción con el usuario
   - Implementa el menú principal y operaciones CRUD
   - Maneja la entrada/salida de datos

2. **Capa de Servicio** ([`service/ProductService.java`](src/main/java/service/ProductService.java))
   - Implementa la lógica de negocio
   - Validación de datos
   - Intermediario entre la UI y el DAO

3. **Capa de Acceso a Datos** ([`dao/ProductDAO.java`](src/main/java/dao/ProductDAO.java))
   - Gestiona las operaciones CRUD en la base de datos
   - Implementa consultas SQL
   - Manejo de conexiones y resultados

4. **Modelo de Datos** ([`model/Product.java`](src/main/java/model/Product.java))
   - Clase que representa la entidad Producto/Tarea
   - Atributos: id, nombre, descripción
   - Getters y setters

### Componentes Adicionales

- **Conexión a Base de Datos** ([`Database.java`](src/main/java/Database.java))
  - Gestiona la conexión MySQL
  - Crea la tabla si no existe
  - Configuración de credenciales

- **Punto de Entrada** ([`Main.java`](src/main/java/Main.java))
  - Inicialización de componentes
  - Inyección de dependencias
  - Lanzamiento de la aplicación

## Funcionalidades

1. **Gestión de Tareas**
   - Listar todas las tareas
   - Agregar nueva tarea
   - Editar tarea existente
   - Eliminar tarea

## Configuración Técnica

### Requisitos
- Java 8
- MySQL 8.0
- Maven

### Dependencias
```xml
<dependencies>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>
</dependencies>
```