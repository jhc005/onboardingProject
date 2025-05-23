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

### Features
- RESTful API endpoints for:
  - Categories
  - Products
  - Customers
  - Orders
- CORS configuration for frontend integration
- Database persistence with JPA
- Service layer architecture
- Repository pattern implementation

### Project Structure
```
src/
├── main/
│   ├── java/com/tienda_online/
│   │   ├── Main.java
│   │   ├── application/services/
│   │   ├── config/
│   │   ├── domain/entities/
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
- POST /api/products - Create new product
- PUT /api/products/{id} - Update product
- DELETE /api/products/{id} - Delete product

#### Customers
- GET /api/customers - List all customers
- POST /api/customers - Create new customer
- PUT /api/customers/{id} - Update customer
- DELETE /api/customers/{id} - Delete customer

#### Orders
- GET /api/orders - List all orders
- POST /api/orders - Create new order
- PUT /api/orders/{id} - Update order
- DELETE /api/orders/{id} - Delete order

## Frontend

### Technology Stack
- React 18
- Vite
- React Router DOM
- Bootstrap for styling
- Axios for API calls

### Features
- Responsive user interface
- CRUD operations for all entities
- Navigation menu
- Form validation
- Error handling
- Loading states

### Project Structure
```
front-end/online_store/
├── src/
│   ├── components/
│   │   ├── Category/
│   │   ├── Customer/
│   │   ├── Order/
│   │   ├── Product/
│   │   └── OnlineStoreMenu.jsx
│   ├── services/
│   │   └── api.js
│   ├── App.jsx
│   └── main.jsx
└── public/
```

### Components
- **OnlineStoreMenu**: Main navigation component
- **Category Management**:
  - CategoryList: Displays and manages categories
  - CategoryForm: Form for creating categories
- **Product Management**:
  - ProductList: Displays and manages products
  - ProductForm: Form for creating/editing/delete products
- **Customer Management**:
  - CustomerList: Displays and manages customers
  - CustomerForm: Form for creating/editing customers
- **Order Management**:
  - OrderList: Displays and manages orders
  - OrderForm: Form for creating orders

## Setup and Installation

### Backend Setup
1. Clone the repository
2. Configure MySQL database settings in `application.yaml`
3. Run `mvn clean install` to build the project
4. Run `mvn spring-boot:run` to start the server

### Frontend Setup
1. Navigate to `front-end/online_store`
2. Run `npm install` to install dependencies
3. Run `npm run dev` to start the development server

## Running the Application
1. Start the backend server (runs on port 8080)
2. Start the frontend development server (runs on port 5173)
3. Access the application at `http://localhost:5173`

## Testing
- Backend: JUnit tests available in the `test` directory
- Frontend: Component testing implemented for major features

## Contributing
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request
