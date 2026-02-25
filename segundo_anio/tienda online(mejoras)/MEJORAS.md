
# Mejoras y Errores Detectados en mi Backend (src/main/java)

En este documento resumo, desde mi experiencia desarrollando este backend, los principales problemas que he detectado y cómo pienso solucionarlos. He intentado ser honesto y directo, priorizando lo que realmente impacta la calidad y el mantenimiento del proyecto.

## 🔴 Problemas críticos que debo arreglar ya

### 1. Uso de ResponseEntity anidado en CategoryController

Me di cuenta que en el CategoryController estoy devolviendo un `ResponseEntity<ResponseEntity<CategoryDto>>`, lo cual es absurdo y rompe la estructura REST. Esto genera respuestas mal formadas y confunde a cualquier cliente que consuma la API. Lo correcto es devolver solo `ResponseEntity<CategoryDto>`.

### 2. La entidad "Orders" debería llamarse "Order"

Nombré la clase como `Orders` cuando en realidad representa una sola orden. Todas mis demás entidades están en singular, así que esto es inconsistente y puede confundir a cualquiera que lea el código. El nombre de la tabla puede seguir siendo `orders`, pero la clase debe ser `Order`.

### 3. Exposición directa de entidades en OrderController y CustomerController

En estos controladores estoy devolviendo directamente las entidades JPA (`Orders` y `Customer`) en vez de usar DTOs. Esto es peligroso porque si cambio la entidad, rompo la API y además puedo estar exponiendo datos sensibles. Tengo que crear DTOs para ambos y mapearlos correctamente.

### 5. ProductService hace demasiado (SRP violado)

El método `createProduct()` es un monstruo: valida, busca, mapea, guarda, construye URIs y hasta devuelve ResponseEntity. Esto es imposible de testear y mantener. Debo separar responsabilidades: validación, persistencia y respuesta HTTP deben estar en capas distintas.

---

## 🟠 Problemas altos (debería arreglar en este sprint)

### 6. Validaciones dispersas y sin Bean Validation

Tengo validaciones manuales en los controladores, como comprobar si el nombre está vacío. Esto es repetitivo y poco mantenible. Lo correcto es usar anotaciones como `@NotBlank` en los DTOs y `@Valid` en los controladores.

### 7. Contraseña hardcodeada en application.yaml

Dejé la contraseña de la base de datos en el archivo de configuración. Esto es un riesgo de seguridad enorme. Debo usar variables de entorno para las credenciales.

### 8. Mis servicios retornan ResponseEntity

Los servicios deberían ser agnósticos de HTTP. Ahora mismo devuelvo `ResponseEntity` desde los servicios, cuando eso debería hacerlo solo el controlador. Tengo que corregir esto y devolver solo DTOs o entidades desde los servicios.

### 9. Creación manual de la base de datos
Actualmente mi backend requiere que la base de datos exista antes de arrancar la aplicación. Esto es problemático porque:

- Cada vez que alguien nuevo clona el proyecto tiene que crear la DB manualmente.

- No es reproducible ni escalable (dev → staging → producción).

- Puede romper el arranque si cambias algo del esquema.

---

## 🟡 Problemas medios (para el siguiente sprint)

### 12. Falta de logging

No tengo ningún log en los servicios ni en los controladores. Esto dificulta mucho el debug y el monitoreo en producción. Debo agregar logs con SLF4J.

### 14. Imports innecesarios

En algunos servicios tengo imports de anotaciones web que no deberían estar ahí. Hay que limpiar los imports para evitar confusiones.

---

## 🟢 Problemas bajos (deuda técnica)

### 16. Constantes mágicas

Tengo strings hardcodeadas (como rutas) que deberían ser constantes. Esto facilita el mantenimiento y evita errores tontos.

### 17. Respuestas de error sin mensaje

En algunos casos devuelvo un BadRequest vacío. Es mejor devolver un objeto con un mensaje de error claro para el cliente.

---

## 📊 Mi plan de acción

**Fase 1 (Crítica):**
- Quitar ResponseEntity anidado en CategoryController
- Renombrar Orders a Order
- Crear OrderDto y CustomerDto
- Implementar Global Exception Handler

**Fase 2 (Alta):**
- Refactorizar ProductService
- Añadir @Valid y Bean Validation
- Unificar constructores
- Mover credenciales a variables de entorno
- Usar spring.jpa.hibernate.ddl-auto

**Fase 3 (Media):**
- Agregar logging
- Extraer métodos largos
- Añadir JavaDoc
- Escribir tests unitarios

---

## ✅ Cosas que sí están bien

A pesar de todo, hay cosas que funcionan y están bien hechas:
- Estructura de capas clara
- Inyección de dependencias
- JPA/Hibernate funcionando
- DTOs y mappers en parte del código
- CORS configurado
- El frontend funciona y se comunica con el backend

## 🔴 PROBLEMAS CRÍTICOS (Arreglar ya)

### 1. ResponseEntity Anidado en CategoryController

**Archivo:** [infrastructure/controllers/CategoryController.java](src/main/java/com/tienda_online/infrastructure/controllers/CategoryController.java) línea 24

El controlador tiene un problema grave: está retornando `ResponseEntity<ResponseEntity<>>` cuando debería solo retornar `ResponseEntity<CategoryDto>`.

```java
// ❌ INCORRECTO
public ResponseEntity<ResponseEntity<CategoryDto>> createCategory(...)
```

Esto causa:
- Respuestas REST malformadas
- Serialización incorrecta
- Clientes confundidos sobre la estructura de datos
- Violación de contratos REST API

**Solución:** Quitar un nivel de anidamiento
```java
// ✅ CORRECTO
public ResponseEntity<CategoryDto> createCategory(...)
```

---

### 2. La Entidad "Orders" Debería Llamarse "Order"

**Archivo:** [domain/entities/Orders.java](src/main/java/com/tienda_online/domain/entities/Orders.java)

Es una inconsistencia fundamental. La clase está nombrada en plural pero representa una orden individual:

```java
// ❌ INCORRECTO
@Entity
@Table(name = "orders")
public class Orders { }
```

Todas las otras entidades son singulares (Category, Product, Customer), pero esta está en plural. Confunde a los desarrolladores sobre si es una colección o una entidad. El nombre de la tabla puede seguir siendo `orders` (plural es común), pero la clase debe ser `Order`.

```java
// ✅ CORRECTO
@Entity
@Table(name = "orders")
public class Order { }
```

---

### 3. OrderController y CustomerController Exponen Entidades Directas

**Archivos:** [infrastructure/controllers/OrderController.java](src/main/java/com/tienda_online/infrastructure/controllers/OrderController.java) y [CustomerController.java](src/main/java/com/tienda_online/infrastructure/controllers/CustomerController.java)

Hay inconsistencia arquitectónica: algunos controladores usan DTOs mientras que otros retornan entidades directas:

- ✅ **ProductController** - Usa `ProductDto` (correcto)
- ✅ **CategoryController** - Usa `CategoryDto` (correcto)
- ❌ **OrderController** - Retorna `Orders` directamente
- ❌ **CustomerController** - Retorna `Customer` directamente

La exposición directa de entidades JPA crea problemas:
- Si cambias una entidad, rompes la API
- No puedes controlar lo que se serializa
- Problemas de seguridad (datos sensibles expuestos)
- Acoplamiento fuerte entre cliente y base de datos

**Solución:** Crear DTOs para ambos
```java
public class OrderDto {
    private Long id;
    private LocalDateTime orderDate;
    // ... otros campos
}

public class CustomerDto {
    private Long id;
    private String name;
    // ... otros campos
}
```

---

### 4. Falta Global Exception Handler

**Ubicación:** Todo el proyecto

La aplicación no tiene un manejador centralizado de excepciones. Cada servicio lanza `RuntimeException` genérica:

```java
.orElseThrow(() -> new RuntimeException("Category not found with id: " + productDto.getCategoryId()));
```

Problemas:
- Cada endpoint retorna errores diferentes
- Stacktraces expuestos al cliente
- No hay logging centralizado de errores
- Imposible controlar el formato de respuesta de error

**Solución:** Crear un handler global con `@RestControllerAdvice`

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("Recurso no encontrado", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex) {
        ErrorResponse error = new ErrorResponse("Validación fallida", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
```

---

### 5. ProductService Viola Single Responsibility Principle

**Archivo:** [application/services/ProductService.java](src/main/java/com/tienda_online/application/services/ProductService.java)

Este servicio está haciendo demasiadas cosas. Tiene casi 28 líneas en `createProduct()` solo:

- Validar que la categoría existe
- Buscar la categoría en base de datos
- Mapear DTO a entidad
- Asignar fecha actual
- Guardar en base de datos
- Construir URI de respuesta
- Retornar ResponseEntity (cosa que no debería hacer un servicio)

Esto lo hace:
- Difícil de testear
- Difícil de mantener
- Propenso a bugs
- Imposible de reutilizar la lógica

**Problema adicional:** Los servicios están retornando `ResponseEntity<ProductDto>` cuando deberían solo retornar `ProductDto`. El manejo de respuestas HTTP es responsabilidad del controlador.

**Solución:** Dividir responsabilidades

```java
@Service
public class ProductService {
    // Solo retorna ProductDto, no ResponseEntity
    public ProductDto createProduct(ProductDto dto) {
        // Validar
        validateProduct(dto);
        
        // Crear y guardar
        Product product = productMapper.toEntity(dto);
        Product saved = productRepository.save(product);
        
        // Retornar DTO
        return productMapper.toDto(saved);
    }
    
    private void validateProduct(ProductDto dto) {
        // Extraer validaciones aquí
    }
}
```

---

## 🟠 PROBLEMAS ALTOS (Arreglar este sprint)

### 6. Validaciones Dispersas sin Bean Validation

**Archivos:** Controllers ([CategoryController.java](src/main/java/com/tienda_online/infrastructure/controllers/CategoryController.java), etc.)

Las validaciones están hardcodeadas en los controladores:

```java
// ❌ INCORRECTO
if (categoryDto.getName() == null || categoryDto.getName().isEmpty()) {
    return ResponseEntity.badRequest().build();
}
```

Problemas:
- Código repetido en múltiples controllers
- Difícil de mantener las reglas
- Sin feedback claro sobre qué falló
- Responsabilidad del controlador confusa

**Solución:** Usar `@Valid` con anotaciones de validación

```java
// En el DTO
public class CategoryDto {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;
}

// En el controlador
@PostMapping
public ResponseEntity<CategoryDto> createCategory(
    @Valid @RequestBody CategoryDto categoryDto) {
    return ResponseEntity.ok(categoryService.createCategory(categoryDto));
}
```

---

### 7. Credenciales Hardcodeadas en application.yaml

**Archivo:** [src/main/resources/application.yaml](src/main/resources/application.yaml)

La contraseña de la base de datos está directamente en el repositorio:

```yaml
password: MiPassword123
```

Esto es un riesgo de seguridad enorme. Cualquiera que clone el repositorio tiene acceso a las credenciales.

**Solución:** Usar variables de entorno

```yaml
spring:
  datasource:
    password: ${DB_PASSWORD:defaultPassword}  # Leer de variable de entorno
```

Luego configurar la variable `DB_PASSWORD` en el servidor de producción.

---

### 8. Servicios Retornan ResponseEntity en lugar de DTOs

**Archivos:** [application/services/CategoryService.java](src/main/java/com/tienda_online/application/services/CategoryService.java), [ProductService.java](src/main/java/com/tienda_online/application/services/ProductService.java)

```java
// ❌ INCORRECTO
public ResponseEntity<CategoryDto> createCategory(CategoryDto categoryDto)
```

Los servicios no deberían conocer sobre HTTP. Esa es responsabilidad del controlador.

**Solución:**
```java
// ✅ CORRECTO
public CategoryDto createCategory(CategoryDto categoryDto)
```

El controlador maneja la respuesta HTTP:
```java
@PostMapping
public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto dto) {
    CategoryDto created = categoryService.createCategory(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
}
```

---

### 9. Inconsistencia en Constructores de Servicios

Algunos servicios usan `@AllArgsConstructor` de Lombok, otros tienen constructores manuales. Esto es inconsistente.

**Solución:** Usar `@AllArgsConstructor` en todos para consistencia

```java
@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository repository;
    private final CustomerMapper mapper;
}
```

---

### 10. Tests Vacíos e Inútiles

**Archivo:** [ProyectoTiendaOnlineApplicationTests.java](src/test/java/com/tienda_online/ProyectoTiendaOnlineApplicationTests.java)

```java
@Test
void contextLoads() { }  // No valida nada
```

Este test solo verifica que la aplicación arranca, pero no testea ninguna funcionalidad.

**Solución:** Escribir tests con sentido

```java
@Test
void shouldCreateCategorySuccessfully() {
    CategoryDto dto = new CategoryDto(null, "Electronics");
    CategoryDto created = categoryService.createCategory(dto);
    
    assertThat(created.getId()).isNotNull();
    assertThat(created.getName()).isEqualTo("Electronics");
}
```

---

## 🟡 PROBLEMAS MEDIOS (Siguiente sprint)

### 11. Método Muy Largo en ProductService

El método `createProduct()` tiene 28 líneas y hace muchas cosas:

```java
// ❌ INCORRECTO - Demasiado largo y confuso
public ResponseEntity<ProductDto> createProduct(ProductDto productDto, UriComponentsBuilder uriBuilder) {
    // 28 líneas de código...
}
```

**Solución:** Extraer en métodos privados

```java
public ProductDto createProduct(ProductDto productDto) {
    validateProduct(productDto);
    Category category = findAndValidateCategory(productDto.getCategoryId());
    Product product = createAndSaveProduct(productDto, category);
    return productMapper.toDto(product);
}

private void validateProduct(ProductDto productDto) {
    // Validaciones aquí
}

private Category findAndValidateCategory(Long categoryId) {
    return categoryRepository.findById(categoryId)
        .orElseThrow(() -> new CategoryNotFoundException(categoryId));
}

private Product createAndSaveProduct(ProductDto dto, Category category) {
    Product product = productMapper.toEntity(dto);
    product.setCategory(category);
    product.setCreatedAt(LocalDateTime.now());
    return productRepository.save(product);
}
```

---

### 12. Falta Logging en Toda la Aplicación

No hay ningún `log` en los servicios, controladores o mappers. Esto hace imposible debuggear problemas en producción.

**Solución:** Agregar logging con SLF4J

```java
@Slf4j  // De Lombok
@Service
public class ProductService {
    
    public ProductDto createProduct(ProductDto productDto) {
        log.info("Creating new product: {} in category: {}", 
                 productDto.getName(), productDto.getCategoryId());
        
        try {
            // ... lógica ...
            log.info("Product created successfully with id: {}", saved.getId());
        } catch (Exception e) {
            log.error("Failed to create product: {}", productDto.getName(), e);
            throw new ProductCreationException(e);
        }
    }
}
```

---

### 13. Falta Documentación JavaDoc

**Ubicación:** Toda la codebase

Los métodos complejos no tienen documentación JavaDoc, haciendo difícil entender qué hacen.

**Solución:** Agregar comentarios JavaDoc

```java
/**
 * Creates a new product and persists it in the database.
 * 
 * @param productDto the product data transfer object containing product details
 * @return the created product as a DTO with the generated ID
 * @throws CategoryNotFoundException if the specified category doesn't exist
 * @throws ValidationException if the product data is invalid
 */
public ProductDto createProduct(ProductDto productDto) {
    // ...
}
```

---

### 14. Imports No Utilizados

**Archivo:** [application/services/ProductService.java](src/main/java/com/tienda_online/application/services/ProductService.java)

Hay imports de anotaciones web que no deberían estar en un servicio:

```java
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
```

Los servicios no deben importar anotaciones web, eso es responsabilidad del controlador.

**Solución:** Limpiar imports

---

### 15. ProductRepository Tiene Formato Incorrecto

**Archivo:** [infrastructure/repositories/ProductRepository.java](src/main/java/com/tienda_online/infrastructure/repositories/ProductRepository.java) línea 12

```java
// ❌ INCORRECTO
List<Product>findByCategoryId(Long categoryId);

// ✅ CORRECTO
List<Product> findByCategoryId(Long categoryId);
```

Falta espacio entre el tipo de retorno y el nombre del método.

---

## 🟢 PROBLEMAS BAJOS (Técnica de deuda)

### 16. Constantes Mágicas

Hay valores hardcodeados que deberían ser constantes:

```java
// ❌ INCORRECTO
var uri = uriBuilder.path("/products/{id}")

// ✅ CORRECTO
private static final String PRODUCT_URI_PATH = "/products/{id}";
var uri = uriBuilder.path(PRODUCT_URI_PATH)
```

---

### 17. Respuestas de Error sin Mensaje

```java
// ❌ INCORRECTO
return ResponseEntity.badRequest().build();

// ✅ CORRECTO
return ResponseEntity.badRequest().body(
    new ErrorResponse("Invalid input", "The provided data is invalid")
);
```

---

## 📊 Plan de Implementación

**Fase 1 (Crítica):** 1-2 horas
- Quitar `ResponseEntity<ResponseEntity>` en CategoryController
- Renombrar `Orders` a `Order`
- Crear `OrderDto` y `CustomerDto`
- Crear Global Exception Handler

**Fase 2 (Alta):** 3-4 horas
- Refactorizar ProductService (dividir responsabilidades)
- Agregar `@Valid` y Bean Validation
- Hacer consistentes todos los servicios
- Mover credenciales a variables de entorno

**Fase 3 (Media):** 3-5 horas
- Agregar logging con SLF4J
- Extraer métodos largos
- Agregar Javadoc
- Crear tests unitarios básicos

---

## ✅ Lo que Está Bien

A pesar de los problemas listados, tienes:
- ✅ Estructura de capas correcta
- ✅ Inyección de dependencias configurada
- ✅ JPA y Hibernate funcionando
- ✅ DTOs para algunos controladores (Product, Category)
- ✅ Mappers con MapStruct
- ✅ CORS configurado
- ✅ Frontend funcionando

