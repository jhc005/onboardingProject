# My First Angular App

## Descripción
Este proyecto es una aplicación Angular que demuestra los conceptos fundamentales del framework, incluyendo componentes, servicios, enrutamiento y gestión de datos.

## Estructura del Proyecto
```
src/
├── app/
│   ├── components/
│   ├── services/
│   ├── models/
│   └── app.module.ts
├── assets/
└── environments/
```

## Características
- Sistema de gestión de tareas
- Operaciones CRUD
- Diseño responsive
- Validación de formularios
- Persistencia de datos
- Implementación de rutas

## Stack Tecnológico
- Angular 15
- TypeScript
- RxJS
- Angular Material
- JSON Server (backend simulado)

## Comenzando

### Prerrequisitos
- Node.js (v14+)
- npm (v6+)
- Angular CLI

### Instalación
```bash
# Instalar dependencias
npm install

# Iniciar servidor de desarrollo
ng serve

# Iniciar backend simulado
json-server --watch db.json
```

## Comandos de Desarrollo
```bash
# Generar nuevo componente
ng generate component nombre-componente

# Construir proyecto
ng build

# Ejecutar pruebas
ng test

# Ejecutar linter
ng lint
```

## Componentes del Proyecto

### HomeComponent
- Página principal
- Muestra resumen de funcionalidades
- Enlaces a diferentes secciones

### TaskComponent
- Gestión de tareas
- Implementa operaciones CRUD
- Manejo de estados

## Servicios

### TaskService
- Gestiona peticiones HTTP
- Maneja operaciones CRUD
- Implementa manejo de errores
- Gestiona estado con RxJS

## Modelos

### User
```typescript
interface User {
  id: number;
  name: string;
  address: {
    city: string;
    zipcode: string;
  };
}
```

## Rutas
```typescript
const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'tareas', component: TaskComponent },
  { path: 'tarea/:id', component: TaskDetailComponent }
];
```

## Pruebas
- Pruebas unitarias con Jasmine
- Pruebas E2E con Protractor
- Pruebas de servicios
- Pruebas de componentes

## Buenas Prácticas Implementadas
- Carga perezosa (Lazy loading)
- Composición de componentes
- Inyección de servicios
- Formularios reactivos
- Patrones Observable
- Manejo de errores

## Mejoras Futuras
- [ ] Autenticación de usuarios
- [ ] Categorías de tareas
- [ ] Funcionalidad de arrastrar y soltar
- [ ] Soporte para tema oscuro
- [ ] Exportar/Importar tareas

## Desarrollador
- [Tu Nombre]

## Licencia
MIT