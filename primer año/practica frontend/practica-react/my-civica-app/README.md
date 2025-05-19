# Documentación del Proyecto - My Civica App

## Descripción General del Proyecto
Esta es una aplicación React construida con Vite que muestra un catálogo de servicios con tarjetas interactivas. La aplicación demuestra prácticas modernas de desarrollo en React, diseño responsivo y arquitectura basada en componentes.

## Estructura del Proyecto
```
src/
  ├── components/        # Componentes de React
  ├── config/           # Archivos de configuración
  ├── assets/           # Recursos estáticos
  └── App.jsx           # Componente principal de la aplicación
```

## Componentes Principales

### Componente ServiceCard
Ubicado en `src/components/ServiceCard.jsx`
- Renderiza tarjetas de servicio individuales
- Características:
  - Efectos de animación al pasar el cursor
  - Íconos de FontAwesome
  - Diseño responsivo
  - Estilizado personalizado con transiciones

### Componente ServiceList
Ubicado en `src/components/ServiceList.jsx`
- Renderiza la cuadrícula de tarjetas de servicio
- Mapea los datos de servicios para crear tarjetas
- Diseño de cuadrícula responsiva

## Estilos

### Estilos Globales (src/index.css)
- Define familia de fuentes base y colores
- Define esquema de colores para modo oscuro
- Configura tipografía básica

### Estilos de App (src/App.css)
- Implementa diseño de cuadrícula responsiva
- Media queries para diferentes tamaños de pantalla:
  - 1200px: 4 columnas
  - 900px: 3 columnas
  - 600px: 2 columnas
  - Móvil: 1 columna
- Decoración personalizada de puntos para encabezados

### Estilos de Tarjeta de Servicio (src/components/ServiceCard.css)
- Dimensiones de tarjeta: 300x300px
- Efectos de gradiente al pasar el cursor
- Transiciones animadas
- Estilo personalizado de botones

## Configuración

### Datos de Servicios
Ubicado en `src/config/services.ts`
- Define array de ofertas de servicios
- Cada servicio contiene:
  - título
  - descripción

### Configuración de Build
- Utiliza Vite como herramienta de construcción
- Configuración de ESLint para calidad de código
- Plugins de React para HMR (Hot Module Replacement)

## Scripts
```json
"scripts": {
  "dev": "vite",          // Servidor de desarrollo
  "build": "vite build",  // Build de producción
  "lint": "eslint .",     // Análisis de código
  "preview": "vite preview" // Vista previa del build de producción
}
```

## Dependencias
- React 19
- FontAwesome para íconos
- Vite para construcción y desarrollo
- ESLint para calidad de código

## Configuración de Desarrollo
1. Instalar dependencias: `npm install`
2. Iniciar servidor de desarrollo: `npm run dev`
3. Construir para producción: `npm run build`

## Soporte de Navegadores
La aplicación utiliza características modernas de CSS incluyendo:
- CSS Grid
- Flexbox
- Variables CSS
- Transiciones y Animaciones CSS