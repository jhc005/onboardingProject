# Tienda Online (Mejoras)

Aplicación completa de tienda online con backend en Spring Boot y dos frontends independientes: uno en Angular y otro en React + Vite. Permite la gestión de productos, categorías, clientes y pedidos, con operaciones CRUD completas y una interfaz moderna.

---

## Tabla de Contenidos
- [Descripción General](#descripción-general)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Backend (Spring Boot)](#backend-spring-boot)
- [Frontend Angular](#frontend-angular)
- [Frontend React + Vite](#frontend-react--vite)
- [Instrucciones de Ejecución](#instrucciones-de-ejecución)
- [Autores](#autores)

---

## Descripción General

Este proyecto implementa una tienda online con arquitectura desacoplada. El backend expone una API RESTful y los frontends consumen estos servicios para ofrecer una experiencia de usuario moderna y responsiva. Incluye gestión de productos, categorías, clientes y pedidos, así como validaciones, manejo de errores y visualización de estadísticas.

---

## Estructura del Proyecto

```
MEJORAS.md
pom.xml
README.md (este archivo)
angular-frontend/        # Frontend Angular
front-end/online_store/  # Frontend React + Vite
src/                     # Backend Spring Boot
```

---

## Backend (Spring Boot)

- **Tecnologías:** Java 17, Spring Boot, Spring Data JPA, MySQL, Maven, MapStruct
- **Características:**
  - API RESTful para productos, categorías, clientes y pedidos
  - DTOs y mapeo con MapStruct
  - Validaciones y manejo de errores global
  - Optimistic locking para concurrencia
  - Migraciones de base de datos (db.migration)
  - Arquitectura en capas: controllers, services, repositories, mappers, dtos

### Estructura
```
src/main/java/com/tienda_online/
  ├── application/services/
  ├── config/
  ├── domain/entities/
  ├── dto/
  ├── mappers/
  ├── infrastructure/controllers/
  └── infrastructure/repositories/
src/main/resources/
  ├── application.yaml
  └── db.migration/online_store.sql
```

### Endpoints Principales
- **Categorías:**
  - `GET /api/categories` — Listar categorías
  - `POST /api/categories` — Crear categoría
  - `PUT /api/categories/{id}` — Editar categoría
  - `DELETE /api/categories/{id}` — Eliminar categoría
- **Productos:**
  - `GET /api/products` — Listar productos
  - `GET /api/products?categoryId={id}` — Filtrar por categoría
  - `POST /api/products` — Crear producto
  - `PUT /api/products/{id}` — Editar producto
  - `DELETE /api/products/{id}` — Eliminar producto
- **Clientes y Pedidos:**
  - Endpoints análogos para gestión completa

---

## Frontend Angular

- **Ubicación:** `angular-frontend/`
- **Tecnologías:** Angular 16+, Angular CLI, TypeScript
- **Características:**
  - Interfaz SPA moderna y responsiva
  - CRUD de productos, categorías, clientes y pedidos
  - Ruteo y navegación modular
  - Interceptores para manejo de errores y respuestas
  - Formularios reactivos y validaciones
  - Consumo de API REST del backend

### Estructura
```
angular-frontend/
  ├── src/app/
  │   ├── core/interceptors/
  │   ├── modules/
  │   │   ├── categories/
  │   │   ├── customers/
  │   │   ├── orders/
  │   │   └── products/
  │   └── pages/
  │       ├── categories/
  │       ├── customers/
  │       ├── main/
  │       ├── orders/
  │       └── products/
  ├── public/
  └── ...
```

### Ejecución
1. Instalar dependencias:
   ```bash
   cd angular-frontend
   npm install
   ```
2. Levantar servidor de desarrollo:
   ```bash
   ng serve
   ```
3. Acceder a `http://localhost:4200/`

---

## Frontend React + Vite

- **Ubicación:** `front-end/online_store/`
- **Tecnologías:** React 18, Vite, React Router DOM, Axios, React Bootstrap
- **Características:**
  - Interfaz SPA rápida y ligera
  - CRUD de productos, categorías, clientes y pedidos
  - Hooks personalizados para gestión de datos
  - Formularios modales y validaciones
  - Visualización de estadísticas
  - Consumo de API REST del backend

### Estructura
```
front-end/online_store/
  ├── src/components/
  │   ├── Category/
  │   ├── Product/
  │   ├── Customer/
  │   ├── Order/
  │   └── Statistics/
  ├── services/api.js
  ├── config/menuItems.js
  ├── styles/global.css
  ├── App.jsx
  └── main.jsx
```

### Ejecución
1. Instalar dependencias:
   ```bash
   cd front-end/online_store
   npm install
   ```
2. Levantar servidor de desarrollo:
   ```bash
   npm run dev
   ```
3. Acceder a `http://localhost:5173/` (o el puerto indicado)

---

## Instrucciones de Ejecución

### Backend
1. Configura la base de datos en `src/main/resources/application.yaml`.
2. Ejecuta las migraciones SQL si es necesario.
3. Compila y ejecuta el backend:
   ```bash
   ./mvnw spring-boot:run
   ```
4. El backend estará disponible en `http://localhost:8080/`.

### Frontends
- Sigue las instrucciones de cada frontend arriba descritas.

---

## Autores
- Javier Herrera

---

## Notas
- Puedes usar ambos frontends de forma independiente.
- El backend debe estar corriendo para que los frontends funcionen correctamente.
- Personaliza los archivos de configuración según tu entorno.
