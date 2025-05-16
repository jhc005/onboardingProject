# 🧼 Refactorización de Código – Práctica 1 (Java)

Este documento describe la refactorización de cuatro ejercicios básicos de Java. Se han aplicado principios de **código limpio** para mejorar la calidad del software, su legibilidad, mantenibilidad y escalabilidad.

---

## 🎯 Objetivos de la Refactorización

- Mejorar la **claridad semántica** mediante nombres significativos.
- **Separar responsabilidades** para evitar funciones monolíticas.
- Eliminar **duplicación de código** (principio DRY).
- Facilitar **reutilización y extensión futura** del código.

---

## 🧩 Ejercicio 1 – Cálculo de precio total

### 🔍 Problemas detectados

- Nombres de variables crípticos (`d`, `p`, `t`).
- Lógica del cálculo incrustada directamente en el método `main`.
- Falta de separación entre lógica de negocio y presentación.

### ✅ Cambios realizados

- Se renombraron variables por nombres descriptivos como `numberOfDays`, `dailyPrice`, `totalPrice`.

---

## 🧩 Ejercicio 2 – Información del usuario y descuento

### 🔍 Problemas detectados

- El método `main` contenía demasiadas responsabilidades (mostrar datos, lógica de negocio y cálculo de precio).
- Código poco modular y difícil de mantener.
- Constantes mágicas (como el `0.8` del descuento).
- Nombres como `student` o `price` poco representativos del contexto.

### ✅ Cambios realizados

- Se dividieron las tareas en métodos separados: uno para imprimir información del usuario, otro para determinar si es mayor de edad y otro para calcular el precio final.
- Se utilizaron constantes para el descuento.
- Se mejoraron los nombres de variables y métodos.
- La clase fue renombrada a `UserInfo`.

---

## 🧩 Ejercicio 3 – Cálculo del salario neto

### 🔍 Problemas detectados

- Números mágicos para tasas de impuestos (`0.9`, `0.85`, `0.75`).
- Estructura de decisión repetitiva directamente en el `main`.
- Código poco flexible para escalabilidad futura (por ejemplo, nuevos tramos de impuestos).

### ✅ Cambios realizados

- Se definieron constantes descriptivas para las tasas impositivas.
- La lógica fue encapsulada en un método `calculateNetSalary`.
- El nombre de la clase se cambió a `SalaryCalculator` para mayor claridad.

---

## 🧩 Ejercicio 4 – Cálculo de áreas de rectángulos

### 🔍 Problemas detectados

- Código duplicado para cada rectángulo (mismo patrón repetido tres veces).
- Uso de variables con sufijos numéricos (`width1`, `height1`, etc.).
- Falta de métodos auxiliares para calcular y mostrar resultados.

### ✅ Cambios realizados

- Se creó un método `calculateArea` para encapsular el cálculo del área.
- Se añadió un método `printRectangleArea` que recibe número de rectángulo, ancho y alto, y evita duplicación.
- Mejora en la claridad del flujo de ejecución.
- La clase fue renombrada a `RectangleAreaCalculator`.

---

## 🛠️ Conclusiones

- La refactorización mejora significativamente la **legibilidad** del código, facilitando futuras modificaciones o ampliaciones.
- Se adoptaron estándares básicos de **Clean Code** como la reducción de responsabilidades por función, eliminación de duplicaciones y claridad semántica.
- Los cambios realizados también facilitan la **automatización de pruebas** y la implementación de nuevas funcionalidades sin comprometer la estructura existente.

---

