# Online Store Application

A full-stack e-commerce application built with Spring Boot backend and React frontend, implementing complete CRUD operations for managing an online store.

## Project Structure

The project is divided into two main parts:
- Backend (Spring Boot)
- Frontend (React + Vite)

## Backend

### Technology Stack
- Java 17
- Spring Boot
- Spring Data JPA
- MySQL Database
- Maven
- MapStruct for DTO mapping

### Features
- RESTful API endpoints for all entities
- DTO pattern implementation
- Optimistic locking for concurrent operations
- Service layer business logic
- Repository pattern implementation
- CORS configuration
- Error handling and validation

### Project Structure
```
src/
├── main/
│   ├── java/com/tienda_online/
│   │   ├── Main.java
│   │   ├── application/
│   │   │   └── services/
│   │   ├── config/
│   │   ├── domain/
│   │   │   └── entities/
│   │   ├── dto/
│   │   ├── mappers/
│   │   └── infrastructure/
│   │       ├── controllers/
│   │       └── repositories/
│   └── resources/
│       ├── application.yaml
│       └── db.migration/
└── test/
    └── java/com/tienda_online/
```

### API Endpoints

#### Categories
- GET /api/categories - List all categories
- POST /api/categories - Create new category
- PUT /api/categories/{id} - Update category
- DELETE /api/categories/{id} - Delete category

#### Products
- GET /api/products - List all products
- GET /api/products?categoryId={id} - List products by category
- POST /api/products - Create new product
- PUT /api/products/{id} - Update product
- DELETE /api/products/{id} - Delete product

// ...existing endpoints...

### Data Transfer Objects (DTOs)
- ProductDTO: Simplified product representation
- CategoryDTO: Category data without circular references
- CustomerDTO: Customer information transfer
- OrderDTO: Order processing data

### Mappers
- ProductMapper: Product entity-DTO conversion
- CategoryMapper: Category entity-DTO conversion
- CustomerMapper: Customer entity-DTO conversion
- OrderMapper: Order entity-DTO conversion

## Frontend

### Technology Stack
- React 18
- Vite
- React Router DOM
- React Bootstrap
- Axios for API calls
- Custom hooks for data management

### Features
- Responsive user interface
- CRUD operations for all entities
- Custom hooks for API interactions
- Form validation and error handling
- Loading states and error messages
- Category-based product filtering
- Modal forms for creating/editing

### Project Structure
```
front-end/online_store/
├── src/
│   ├── components/
│   │   ├── Category/
│   │   │   ├── CategoryList.jsx
│   │   │   └── CategoryForm.jsx
│   │   ├── Product/
│   │   │   ├── ProductList.jsx
│   │   │   ├── ProductForm.jsx
│   │   │   └── useProducts.jsx
│   │   └── OnlineStoreMenu.jsx
│   ├── services/
│   │   └── api.js
│   ├── App.jsx
│   └── main.jsx
└── public/
```

### Custom Hooks
- useProducts: Product management logic
- useCategories: Category management logic
- useCustomers: Customer management logic
- useOrders: Order management logic

// ...rest of existing content...

## Recent Implementations

### Backend
- Added DTO pattern for data transfer
- Implemented MapStruct for object mapping
- Added optimistic locking for concurrent operations
- Improved error handling and validation
- Enhanced service layer logic

### Frontend
- Added custom hooks for state management
- Implemented modal forms for CRUD operations
- Enhanced error handling and validation
- Added loading states
- Improved UI/UX with React Bootstrap

### Errors that i have not been able to solve
- Edit button from products creates a new product instead of updating it
