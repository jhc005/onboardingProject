# Análisis de Código: `CourseManager.java`

Este proyecto contiene una implementación básica de un sistema de gestión de cursos en Java. A continuación se identifican los **potenciales problemas** desde el punto de vista del **código limpio**, sin entrar en su refactorización.

---

## 📋 Problemas Identificados

### 1. **Nombres de Variables No Descriptivos**
- Variables como `a`, `g`, `p`, `s` no son intuitivas.
- Deberían tener nombres autoexplicativos como `students`, `grades`, `passedList`, `sum`, etc.

### 2. **Nombres de Métodos Abreviados**
- Métodos como `addS`, `removeS`, `getPr`, `updGr`, `getStat`, `check` son poco claros.
- Idealmente, los nombres de los métodos deben ser verbos claros como `addStudent`, `removeStudent`, `printResults`, `updateGrade`, etc.

### 3. **Duplicación de Lógica**
- Validación de calificaciones (`grade < 0 || grade > 10`) se repite en varios métodos. Esta lógica podría extraerse a un método separado.
- La lógica de actualización de promedio (`avg`) también se repite y puede ser centralizada.

### 4. **Uso de Múltiples Listas Paralelas**
- El uso de tres `ArrayList` paralelos (`a`, `g`, `p`) para manejar información relacionada de estudiantes puede causar errores de sincronización.
- Es preferible usar una clase `Student` que encapsule nombre, nota y estado de aprobado.

### 5. **Falta de Separación de Responsabilidades**
- La clase `CourseManager` maneja tanto la lógica de negocio como la presentación (impresión en consola).
- La responsabilidad de imprimir resultados debería estar separada de la lógica del curso.

### 6. **Uso de Tipos Primitivos Sin Validación Extra**
- `avg` y `s` están expuestos a posibles errores si se accede antes de haber agregado estudiantes.
- Sería útil manejar casos como división por cero con mayor robustez.

### 7. **Mensajes de Error en Consola**
- El uso de `System.out.println` para errores mezcla salida estándar con errores.
- Sería mejor usar `System.err.println` para mensajes de error o manejar excepciones adecuadamente.

### 8. **Método `main` Como Prueba**
- El método `main` se usa para pruebas manuales. No es reutilizable ni escalable.
- Sería mejor tener pruebas unitarias en una clase de prueba separada (usando JUnit, por ejemplo).

### 9. **Estructura de Código Verbosa y Poca Modularización**
- Algunos métodos son largos y hacen múltiples cosas (por ejemplo, `getPr`).
- Podrían dividirse en métodos más pequeños y legibles.

### 10. **Falta de Documentación y Comentarios Claros**
- Algunos comentarios son demasiado breves o redundantes (e.g., `// Function to add student`).
- Sería mejor usar comentarios que expliquen el "por qué" en lugar del "qué", especialmente si el código no es autoexplicativo.

---

## ✅ Buenas Prácticas Presentes

- Se verifica validez de calificaciones antes de agregarlas o actualizarlas.
- Se mantiene un promedio actualizado sin recorrer la lista completamente cada vez.
- Se ofrece retroalimentación visual tras las operaciones.

---

