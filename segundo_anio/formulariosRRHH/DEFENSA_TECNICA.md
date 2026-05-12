# Defensa Técnica: Sistema de Onboarding FormulariosRRHH

---

## 1. ¿Qué es esta aplicación y por qué existe?

**FormulariosRRHH** es un sistema web de onboarding para departamentos de Recursos Humanos. Resuelve un problema concreto: gestionar el proceso de incorporación de nuevos empleados o candidatos, desde la recogida de datos personales hasta la validación de documentos requeridos.

Sin este sistema, ese proceso se hace por correo electrónico, papel o formularios estáticos: difícil de rastrear, propenso a errores, sin historial de estado. La aplicación centraliza y digitaliza todo el flujo:

1. RRHH crea el proceso para un candidato y le envía un enlace único.
2. El candidato rellena un formulario dinámico y sube sus documentos.
3. RRHH revisa, aprueba o rechaza, y el sistema notifica automáticamente por email.

---

## 2. Stack Tecnológico: decisiones y justificación

### Backend — Spring Boot 3.4.5 + Java 21

**¿Por qué Spring Boot?**
Spring Boot es el estándar de la industria en Java para APIs REST empresariales. Ofrece:
- Autoconfiguración que elimina boilerplate sin perder control granular.
- Ecosistema maduro: Spring Security, Spring Data JPA, Spring Mail, todo integrado.
- Spring Boot 3.x requiere Java 17+ y soporta el modelo virtual threads de Java 21, lo que lo hace preparado para alta concurrencia sin cambio de código.

**¿Por qué Java 21?**
- LTS (Long-Term Support): soporte garantizado hasta 2031.
- Records, sealed classes, pattern matching: código más expresivo y seguro.
- Virtual Threads (Project Loom): permite escalar sin necesidad de WebFlux reactivo, que añade complejidad cognitiva importante.

**Alternativa descartada: Node.js/Express**
Node habría sido más rápido de prototipar, pero Spring Boot ofrece un sistema de tipos fuerte, validación integrada con Bean Validation y un ecosistema de seguridad más maduro. Para una aplicación de RRHH que maneja datos sensibles, eso importa.

---

### Frontend — Angular 21 + TypeScript 5.9

**¿Por qué Angular sobre React o Vue?**

| Criterio | Angular | React | Vue |
|---|---|---|---|
| Tipado | TypeScript nativo | Opcional | Opcional |
| Estructura | Opinionado | Libre | Semi-opinionado |
| DI integrada | Sí | No | No |
| Forms complejos | Reactive Forms potente | Librerías externas | Limitado |

Angular tiene una curva de aprendizaje mayor, pero aporta una arquitectura consistente que facilita el mantenimiento y el trabajo en equipo. Para una aplicación de gestión con formularios complejos y múltiples vistas de administración, Angular Reactive Forms es significativamente más robusto que las alternativas.

**Angular Standalone Components (Angular 14+)**
Se usa la API standalone en lugar del modelo basado en módulos NgModule. Esto reduce el boilerplate, mejora el tree-shaking y hace el código más legible. Es la dirección oficial del equipo de Angular desde la versión 17.

---

### Base de datos — MySQL 8

**¿Por qué MySQL y no PostgreSQL?**
Ambas son opciones igualmente válidas para este caso de uso. MySQL 8 se eligió por:
- Mayor familiaridad en el entorno de desarrollo.
- InnoDB con soporte completo de transacciones ACID.
- Buen soporte de JPA/Hibernate.

**¿Por qué relacional y no NoSQL?**
Los datos tienen relaciones claras y estructuradas: un proceso pertenece a un candidato, una respuesta pertenece a un campo y a un proceso, un documento pertenece a un requisito. Un modelo relacional garantiza integridad referencial mediante claves foráneas. NoSQL añadiría flexibilidad innecesaria a cambio de perder esa integridad.

**`ddl-auto: none`**
Hibernate NO gestiona el esquema automáticamente. Se usa el script `database/formsDB.sql` para control total sobre migraciones. En producción, `validate` o `none` + herramientas como Flyway es la práctica correcta; `create-drop` o `update` automáticos en producción son antipatrones peligrosos.

---

## 3. Arquitectura: Hexagonal (Ports & Adapters) + DDD + CQRS

Esta es probablemente la decisión más importante del proyecto.

> **Nota honesta**: la implementación no es hexagonal pura al 100%. Se toman las ideas centrales de Hexagonal y DDD y se adaptan pragmáticamente a Spring Boot. No hay eventos de dominio, los agregados no están formalmente definidos, y se usa JPA directamente en lugar de un ORM propio de dominio. Pero los principios clave están aplicados de forma consistente, y eso es lo que importa.

---

### 3.1 Arquitectura Hexagonal (Ports & Adapters)

Definida por Alistair Cockburn, la arquitectura hexagonal pone **la aplicación en el centro** y define que toda comunicación con el mundo exterior (base de datos, HTTP, email, sistema de ficheros) ocurre a través de **puertos** (interfaces), que son implementados por **adaptadores** concretos.

```
                  ┌──────────────────────────────┐
  [HTTP/REST]  →  │  Adaptador Entrada (input/)  │
                  │                              │
                  │        DOMINIO               │
                  │    (model/ + outport/)       │
                  │                              │
                  │  Adaptador Salida (output/)  │  →  [MySQL]
                  │  Adaptador Salida (output/)  │  →  [Filesystem]
                  │  Adaptador Salida (output/)  │  →  [Gmail SMTP]
                  └──────────────────────────────┘
```

**Puertos de salida (`outport/`)**: el dominio declara interfaces con lo que necesita, sin saber cómo se implementa:

```java
// El dominio define el puerto — no sabe que existe JPA ni MySQL
public interface AdminUsersRepositoryPort {
    Optional<AdminUsers> findByEmail(String email);
    AdminUsers save(AdminUsers adminUser);
}
```

**Adaptadores de salida (`output/adapter/`)**: la infraestructura implementa esos puertos:

```java
// El adaptador traduce entre el dominio y JPA
@Component
public class AdminUsersRepositoryAdapter implements AdminUsersRepositoryPort {
    private final AdminUsersJpaRepository jpaRepository;

    public Optional<AdminUsers> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(this::toDomain);
    }
}
```

**Adaptadores de entrada (`input/`)**: los controladores REST son adaptadores que traducen peticiones HTTP en comandos del dominio:

```java
// El controlador REST es un adaptador de entrada — adapta HTTP al dominio
@RestController
public class AdminUsersCommandController {
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        // Traduce HTTP → Command → respuesta HTTP
        return ResponseEntity.ok(commandBus.dispatch(new LoginCommand(req.email(), req.password())));
    }
}
```

Cada módulo funcional (adminUsers, candidates, onboardingProcesses, etc.) tiene esta estructura interna:

```
module/
├── domain/
│   ├── model/           ← Entidad de dominio pura, sin dependencias de framework
│   ├── outport/         ← Puerto de salida (interfaz del repositorio)
│   └── valueobject/     ← Enums que encapsulan reglas de negocio
├── application/
│   ├── commands/        ← Operaciones de escritura + sus handlers
│   ├── queries/         ← Operaciones de lectura + sus handlers
│   └── dto/             ← Objetos de transferencia (entrada/salida de la app)
└── infrastructure/
    ├── input/           ← Adaptador de entrada: Controlador REST
    └── output/
        ├── adapter/     ← Adaptador de salida: implementa el puerto
        └── jpa/         ← Entidad JPA + repositorio Spring Data
```

**¿Qué se gana con esto?**

1. **Inversión de dependencias**: el dominio no depende de Spring, JPA ni MySQL. Define interfaces y la infraestructura se adapta a él, no al revés.
2. **Testabilidad**: los handlers de la capa de aplicación se pueden testear inyectando mocks simples de los puertos, sin levantar Spring ni base de datos.
3. **Intercambiabilidad**: si mañana se cambia MySQL por PostgreSQL, o JPA por jOOQ, solo cambian los adaptadores. El dominio y la aplicación no se tocan.
4. **Legibilidad**: la estructura de carpetas refleja la arquitectura. Sin leer código, se sabe qué hace cada fichero por su ubicación.

**Alternativa descartada: MVC clásico (Controller → Service → Repository)**
El MVC en tres capas es más simple, pero tiende a degradarse en "servicios gordos" donde toda la lógica de negocio, validaciones, mapeos y llamadas externas conviven en el mismo método. A medida que el proyecto crece, se vuelve difícil de mantener y testear.

---

### 3.2 DDD (Domain-Driven Design)

DDD es una filosofía de diseño que propone organizar el código alrededor del **modelo de negocio**, no alrededor de la tecnología. Los conceptos de DDD presentes en el proyecto son:

**Bounded Contexts (Contextos Acotados)**
Cada paquete de módulo es un bounded context independiente con su propio modelo de dominio. `adminUsers` no conoce las clases internas de `candidates`, y viceversa. La comunicación entre módulos ocurre a través de los puertos o de los DTOs de la capa de aplicación, nunca entre entidades de dominio de distintos módulos.

```
com.onboarding/
├── adminUsers/      ← Bounded Context: gestión de administradores
├── candidates/      ← Bounded Context: candidatos
├── onboardingProcesses/  ← Bounded Context: flujo del proceso (agregado raíz)
├── dataFields/      ← Bounded Context: formulario dinámico
...
```

**Entidades de Dominio**
Las clases en `domain/model/` son entidades con identidad propia. Por ejemplo, `OnboardingProc` tiene un UUID como identidad que persiste a lo largo del tiempo independientemente de sus atributos. No son POJOs anémicos: encapsulan las reglas de negocio que les corresponden.

**Value Objects**
Los enums como `OnboardingStatus`, `FieldType` y `ValidationStatus` son value objects: representan conceptos del dominio con semántica de negocio, no simples enteros o strings.

```java
// No es un int, no es un String — es un concepto del dominio
public enum OnboardingStatus {
    PENDING_ACCESS, IN_PROGRESS, SUBMITTED, VALIDATED, REJECTED
}
```

Usar un ENUM en lugar de un `int` o `String` libre hace que las transiciones de estado inválidas sean imposibles de representar en el sistema de tipos.

**Ubiquitous Language (Lenguaje Ubicuo)**
Los nombres de clases, métodos y tablas usan el vocabulario del negocio de RRHH, no términos técnicos genéricos. No hay `UserEntity`, `RecordManager` o `DataProcessor`. Hay `Candidates`, `OnboardingProcess`, `DocumentRequirements`. Un miembro de RRHH que sepa algo de programación puede leer los nombres y entender qué hace cada clase.

**Repositorios como abstracciones de dominio**
Los `*RepositoryPort` son interfaces del dominio que hablan el lenguaje de dominio (`findByEmail`, `findByProcessId`), no términos técnicos de base de datos. El dominio no sabe que existe SQL; le pide al repositorio "dame el candidato con este email" y el adaptador lo resuelve como quiera.

---

### 3.2 Por qué CQRS (Command Query Responsibility Segregation)

CQRS separa explícitamente las operaciones que **modifican estado** (Commands) de las que **leen estado** (Queries).

**Implementación concreta:**

```java
// Un comando es un objeto que expresa una intención
public record LoginCommand(String email, String password) implements Command {}

// Un handler contiene toda la lógica para ejecutar ese comando
@Component
public class LoginCommandHandler implements CommandHandler<LoginCommand, LoginResponse> {
    public LoginResponse handle(LoginCommand cmd) {
        // 1. Buscar usuario
        // 2. Validar contraseña con BCrypt
        // 3. Generar JWT
        // 4. Devolver DTO de respuesta
    }
}

// El bus despacha el comando al handler correcto
commandBus.dispatch(new LoginCommand(email, password));
```

**¿Qué problema resuelve?**

Sin CQRS, los métodos de un servicio acaban siendo: `login()`, `createUser()`, `updateUser()`, `deleteUser()`, `getAllUsers()`, `getUserById()`... un "God Service" que mezcla responsabilidades. Con CQRS cada operación es una clase cohesiva con una sola responsabilidad.

**`SpringCommandBus` y `SpringQueryBus`**
Son implementaciones propias que usan el contexto de Spring para resolver qué handler corresponde a cada comando/query usando el sistema de inyección de dependencias. Cada handler es un `@Component` Spring, lo que permite que Spring gestione su ciclo de vida, transacciones y demás.

---

## 4. Seguridad

### 4.1 Autenticación de administradores: JWT

**Flujo:**
1. El administrador envía email + contraseña a `POST /api/admin/auth/login`.
2. El backend valida con BCrypt y genera un JWT firmado con HMAC-SHA.
3. El frontend almacena el token en `localStorage`.
4. El interceptor Angular (`authInterceptor`) añade `Authorization: Bearer <token>` en cada petición HTTP.
5. `JwtAuthFilter` en el backend intercepta cada request, valida el token y establece el `SecurityContext`.

**¿Por qué JWT y no sesiones?**

Las sesiones requieren estado en el servidor (almacenamiento de sesión). JWT es stateless: el servidor no necesita guardar nada, el token se verifica criptográficamente en cada request. Esto facilita el escalado horizontal y encaja perfectamente con una API REST.

**Expiración: 8 horas (`28800000ms`)**
Un balance entre seguridad (el token expira) y usabilidad (el usuario no tiene que reloguearse cada hora). En producción, el tiempo óptimo depende del nivel de sensibilidad de los datos.

**Contraseñas con BCrypt**
BCrypt incluye un salt aleatorio en el hash, lo que lo hace resistente a ataques de rainbow tables. Es el estándar recomendado para hashing de contraseñas en aplicaciones Java/Spring.

### 4.2 Acceso de candidatos: Token de un solo uso

Los candidatos no tienen contraseña. RRHH genera un link único con un token:

```
http://localhost:4200/onboarding?token=abc123xyz
```

El token se almacena hasheado en la base de datos junto con:
- `expires_at`: expira en 30 días.
- `is_used`: una vez usado, no puede reutilizarse.
- `attempts`: seguimiento de intentos.

**¿Por qué este enfoque?**
Pedir al candidato que cree una cuenta y contraseña añade fricción innecesaria. El candidato accede una sola vez para completar su proceso. Un token de acceso único es más seguro (no hay contraseña que comprometer) y más simple para el usuario final.

### 4.3 Recuperación de contraseña de administradores

1. `POST /api/admin/auth/forgot-password` genera un token aleatorio y lo guarda en `admin_users.reset_token` con un `reset_token_exp`.
2. Se envía un email con el link de reset.
3. `POST /api/admin/auth/reset-password` valida que el token no haya expirado antes de cambiar la contraseña.

**CSRF**
Está deshabilitado porque la API es stateless (no usa cookies de sesión). Sin estado de sesión en el servidor, CSRF no es un vector de ataque aplicable. Esto es correcto y no es un descuido de seguridad.

**CORS**
Configurado para aceptar solo peticiones desde `http://localhost:4200`. En producción se cambiaría al dominio real del frontend.

---

## 5. Modelo de datos

### Entidades y relaciones principales

```
candidates
    └── onboarding_processes (1:N)
            ├── data_answers (N:M via data_fields)
            ├── uploaded_documents (N:M via document_requirements)
            ├── access_tokens (1:N)
            └── notifications (1:N)

data_fields
    └── field_options (1:N, solo para tipo SELECT)

document_requirements
    └── uploaded_documents (1:N)
```

### Decisiones de diseño del modelo

**UUIDs como PK para `candidates` y `onboarding_processes`**
Se usan `CHAR(36)` con UUID v4 en lugar de `BIGINT AUTO_INCREMENT`. Razones:
- Seguridad: los IDs numéricos secuenciales son predecibles (un atacante puede enumerar recursos). Los UUIDs no lo son.
- Distribución: si el sistema escala a múltiples nodos, no hay conflicto de IDs.
- Trazabilidad: el ID de un proceso puede compartirse de forma segura sin revelar cuántos procesos existen.

**`data_fields` y `field_options`: formulario dinámico**
En lugar de hardcodear los campos del formulario, se almacenan en base de datos. RRHH puede añadir, modificar o eliminar campos sin tocar código. Los tipos soportados son: `TEXT`, `NUMBER`, `SELECT`, `TEXTAREA`.

Cada campo puede tener:
- `validation_regex`: para validar el formato de la respuesta (ej. DNI, teléfono).
- `min_length` / `max_length`: restricciones de longitud.
- `is_required`: campo obligatorio o no.
- `display_order`: orden de aparición en el formulario.

Los campos de tipo `SELECT` tienen opciones en la tabla `field_options`, también gestionadas dinámicamente.

**Estado del proceso como ENUM**

```
PENDING_ACCESS → IN_PROGRESS → SUBMITTED → VALIDATED
                                         → REJECTED
```

Usar un ENUM en lugar de una columna booleana o un entero garantiza que el estado siempre sea uno de los valores válidos a nivel de base de datos.

**`uploaded_documents.validation_status` separado del proceso**
Un documento puede estar `PENDING_VALIDATION`, `APPROVED` o `REJECTED` de forma independiente al estado global del proceso. Esto permite que RRHH valide documentos individualmente antes de cerrar todo el proceso.

---

## 6. Módulos del sistema

### 6.1 Módulos del backend (180 archivos Java)

| Módulo | Propósito |
|---|---|
| `adminUsers` | CRUD de administradores, login, recuperación de contraseña |
| `candidates` | Registro y consulta de candidatos |
| `onboardingProcesses` | Ciclo de vida del proceso de onboarding |
| `dataFields` | Definición dinámica de campos del formulario |
| `fieldOptions` | Opciones de los campos tipo SELECT |
| `dataAnswers` | Respuestas del candidato a los campos |
| `documentRequirements` | Especificación de documentos requeridos |
| `uploadedDocuments` | Gestión de archivos subidos y su validación |
| `accessTokens` | Tokens de acceso para candidatos |
| `notifications` | Envío y registro de notificaciones por email |
| `config` | Configuración de seguridad, JWT y CORS |
| `shared` | Infraestructura del bus de comandos y queries |

### 6.2 Módulos del frontend (Angular)

| Módulo | Páginas / Componentes clave |
|---|---|
| `adminUsers` | Login, forgot password, reset password, tabla de usuarios |
| `candidates` | Listado de candidatos, formulario de creación |
| `onboardingProcesses` | Listado de procesos, detalle de proceso |
| `dataFields` | Gestión de campos del formulario |
| `documentRequirements` | Gestión de requisitos documentales |
| `accessTokens` | Vista del candidato (formulario de onboarding) |
| `uploadedDocuments` | Subida y visualización de documentos |
| `notifications` | (Integrado en el backend, trazabilidad en el detalle de proceso) |

---

## 7. Flujos principales

### Flujo 1: Onboarding de un candidato

```
[Admin] Crea candidato + proceso
        ↓
[Sistema] Genera token de acceso + envía email con link
        ↓
[Candidato] Accede a /onboarding?token=xxx
        ↓
[Angular] candidateGuard valida que el token existe en la URL
        ↓
[Backend] ValidateTokenCommand verifica token en DB (no expirado, no usado)
        ↓
[Candidato] Rellena formulario dinámico (campos desde data_fields)
[Candidato] Sube documentos requeridos (según document_requirements)
        ↓
[Candidato] Envía el formulario → proceso pasa a SUBMITTED
        ↓
[Sistema] Notifica por email al administrador
        ↓
[Admin] Accede a /admin/processes/:id
[Admin] Revisa respuestas y aprueba/rechaza documentos
[Admin] Marca el proceso como VALIDATED o REJECTED
        ↓
[Sistema] Notifica por email al candidato
```

### Flujo 2: Subida y validación de documentos

1. El candidato sube un fichero.
2. `UploadDocumentCommandHandler` valida:
   - **Extensión**: que sea una de las permitidas en `document_requirements.allowed_formats`.
   - **Nombre del fichero**: formato regex `[NombreDoc] Apellido1 Apellido2, Nombre`. Esto no es arbitrario: garantiza que los documentos sean identificables por nombre de forma consistente para RRHH.
   - **Tamaño**: máximo 20MB por fichero, 25MB por request.
3. El fichero se almacena en `./uploads/{processId}/` con un prefijo UUID para evitar colisiones.
4. Se crea un registro en `uploaded_documents` con estado `PENDING_VALIDATION`.
5. El administrador cambia el estado a `APPROVED` o `REJECTED` con notas opcionales.

### Flujo 3: Login de administrador

```
POST /api/admin/auth/login
        ↓
AdminUsersCommandController.login()
        ↓
SpringCommandBus.dispatch(new LoginCommand(email, password))
        ↓
LoginCommandHandler.handle()
  1. AdminUsersRepositoryPort.findByEmail(email)
  2. BCryptPasswordEncoder.matches(rawPassword, storedHash)
  3. JwtService.generateToken(email)
  4. return LoginResponse(token, userInfo)
        ↓
Frontend almacena token en localStorage
authInterceptor inyecta Bearer token en todas las peticiones
JwtAuthFilter valida token en cada request al backend
```

---

## 8. Gestión de notificaciones por email

Se usa **Spring Mail** con **Gmail SMTP** (puerto 587, STARTTLS).

Tipos de notificación implementados:

| Tipo | Receptor | Cuándo |
|---|---|---|
| `ACCESS_LINK` | Candidato | Al crear el proceso |
| `FORM_SUBMITTED` | Administrador | Al enviar el formulario |
| `VALIDATED` | Candidato | Al validar el proceso |
| `REJECTED` | Candidato | Al rechazar el proceso |
| `DOCUMENT_REJECTED` | Candidato | Al rechazar un documento individual |

Cada notificación queda registrada en la tabla `notifications` con `SENT` o `FAILED`, lo que proporciona trazabilidad completa. Si el envío falla, el sistema no colapsa; registra el error y continúa.

**¿Por qué Gmail SMTP y no un servicio como SendGrid o AWS SES?**
Para un entorno de desarrollo/prototipo, Gmail SMTP es suficiente. En producción, la recomendación sería migrar a un servicio de email transaccional (SendGrid, SES) por límites de envío, reputación de dominio y métricas de entregabilidad.

---

## 9. Configuración y secretos

**`application.yaml`** contiene la configuración general con variables de entorno:

```yaml
spring:
  datasource:
    url: jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3307}/forms_db
  servlet.multipart:
    max-file-size: 20MB
jwt:
  expiration: 28800000  # 8 horas
```

**`application-secret.properties`** (excluido de git) contiene:
- `DB_PASSWORD`
- `JWT_SECRET` (mínimo 32 caracteres para HMAC-SHA)
- `MAIL_USERNAME` / `MAIL_PASSWORD` (app password de Gmail)
- `ADMIN_EMAIL` / `ADMIN_PASSWORD` / `ADMIN_FULL_NAME` (administrador inicial)

**¿Por qué separar secretos en un fichero aparte?**
Para que nunca entren en el repositorio. El `.gitignore` excluye `application-secret.properties`. En producción, los secretos deberían gestionarse con variables de entorno del servidor o un vault (HashiCorp Vault, AWS Secrets Manager).

**`AdminUserSeeder`**
Al arrancar la aplicación, si no existe ningún administrador en la base de datos, se crea uno automáticamente con las credenciales configuradas. Esto evita el problema del "huevo y la gallina": no puedes crear el primer admin a través de la propia aplicación si no hay ningún admin para autenticarte.

---

## 10. Frontend: decisiones de arquitectura

### AUTH-SERVICE
auth.service.ts — Gestión de sesión
Maneja todo lo relacionado con la autenticación del usuario en el navegador usando localStorage.

Almacena dos claves:

- civica_token → el JWT para autenticar peticiones.
- civica_user → datos del usuario (id, email, fullName).


Usa Signals de Angular para estado reactivo:
- isAuthenticated → true/false según si hay token guardado.
- currentUser → objeto con los datos del usuario logueado o null.

Métodos:
Método	                Qué hace
setSession(token, user)	Guarda el token y usuario en localStorage y actualiza los signals (al hacer login)
getToken()	        Devuelve el token JWT (lo usa el authInterceptor)
logout()	        Borra token y usuario del localStorage y resetea los signals

### Interceptores HTTP
auth.interceptor.ts — Inyección del token JWT
Se ejecuta antes de cada petición HTTP saliente.

Obtiene el token JWT del AuthService.
Si hay token, clona la petición y le agrega el header Authorization: Bearer <token>.
Si no hay token, deja pasar la petición sin modificar.
Gracias a este interceptor, no tienes que agregar el header manualmente en cada llamada a la API.

error.interceptor.ts — Manejo global de errores HTTP
Se ejecuta después de cada respuesta, capturando los errores. Maneja 4 casos:

Código	Acción
401 Unauthorized	Hace logout, redirige a /login y muestra toast "Sesión expirada"
403 Forbidden	Muestra toast "No tienes permisos"
0 Sin conexión	Muestra toast "No se puede conectar con el servidor"
Otro error	Muestra el mensaje de error que viene del backend
Centraliza el manejo de errores para no repetir lógica en cada servicio.

success.interceptor.ts — Interceptor vacío
Actualmente no hace nada, solo deja pasar las peticiones. Es un placeholder preparado para agregar lógica de éxito en el futuro (por ejemplo, mostrar un toast de confirmación en ciertos endpoints).

En resumen: los tres interceptores forman una capa transversal: authInterceptor adjunta credenciales a las salidas, errorInterceptor gestiona todos los errores de forma centralizada, y successInterceptor está reservado para lógica futura de respuestas exitosas.

### AuthGuard
auth.guard.ts — Guarda de autenticación general
Protege rutas que requieren que el usuario esté logueado.

- Llama a AuthService.isAuthenticated() para verificar si hay sesión activa.
- Si el usuario está autenticado → permite el acceso (return true).
- Si no está autenticado → redirige a /login.
Se usa típicamente en rutas del panel de administración o RRHH.

### CandidateGuard
candidate.guard.ts — Guarda para candidatos
Protege rutas que solo deben ser accesibles mediante un token en la URL (por ejemplo, cuando se le envía un link al candidato por email).

- Lee el parámetro ?token=... de la URL (queryParamMap.get('token')).
- Si el token existe en la URL → permite el acceso.
- Si no hay token → redirige a /login.
Sirve para que solo candidatos con un link válido puedan acceder al formulario de onboarding, sin necesidad de credenciales.

En resumen: authGuard es para usuarios internos (admins/RRHH) que inician sesión normal, mientras que candidateGuard es para candidatos externos que acceden via link con token único.

### Standalone Components
Se usa la API standalone de Angular (sin NgModules) porque:
- Reduce el boilerplate significativamente.
- El tree-shaking es más eficiente (solo se incluye lo que se importa).
- Es la dirección oficial de Angular desde la v17.

### Dependency Injection con tokens
Los servicios de API (repositorios) se inyectan mediante tokens DI en `app.config.ts`, no directamente por clase. Esto permite sustituir implementaciones en tests sin modificar los componentes que las usan.

### Signals para estado reactivo
`AuthService` usa Angular Signals (`signal()`, `computed()`) en lugar de BehaviorSubject de RxJS para el estado de autenticación. Signals tienen sincronización automática con el ciclo de detección de cambios de Angular, lo que los hace más eficientes para estado de UI.

### Guards de navegación
- `authGuard`: protege todas las rutas de `/admin`. Si el JWT no está presente o expiró, redirige a `/login`.
- `candidateGuard`: protege `/onboarding`. Verifica que el query param `?token` exista.

### Interceptores HTTP
- `authInterceptor`: añade el header `Authorization: Bearer <token>` automáticamente.
- `errorInterceptor`: maneja errores HTTP globalmente (401, 403, 500) con feedback al usuario via `ngx-toastr`.

### `ngx-toastr`
Se usa para notificaciones visuales no intrusivas (success, error, info, warning). Es una librería madura y bien mantenida para Angular con soporte de accesibilidad.

---

## 11. Preguntas frecuentes en una defensa técnica

**P: ¿Por qué Hexagonal + DDD y no MVC simple? ¿No es sobreingeniería para este proyecto?**

R: La pregunta es legítima. MVC habría sido más rápido de arrancar, pero el coste aparece cuando el proyecto crece. En MVC, los servicios acaban siendo "God Services" con 20 métodos mezclando lógica de negocio, validaciones, mapeos y llamadas externas. Con Hexagonal + DDD, cada bounded context es un paquete autocontenido, cada operación es una clase con una sola responsabilidad, y las reglas del dominio están en el dominio, no repartidas por controllers y repositorios. Con 180 clases Java repartidas en 11 módulos, esa estructura no es sobreingeniería — es lo que hace navegable el código. Dicho esto, la implementación es pragmática: no hay eventos de dominio ni agregados formales, solo los principios clave aplicados donde aportan valor real.

**P: ¿Qué diferencia hay entre tu arquitectura hexagonal y Clean Architecture de Robert C. Martin?**

R: Comparten el mismo principio fundamental: las dependencias apuntan siempre hacia el centro (el dominio). Las diferencias son principalmente de nomenclatura y énfasis. Clean Architecture habla de "capas concéntricas" (Entities → Use Cases → Interface Adapters → Frameworks). Hexagonal habla de "puertos y adaptadores" con un hexágono central. En la práctica, la estructura de este proyecto (domain/outport/, application/, infrastructure/input/, infrastructure/output/) es compatible con ambas descripciones. La elección de llamarlo hexagonal es porque el concepto de "puerto" — una interfaz que el dominio define y el adaptador implementa — es el mecanismo central y más visible del código.

**P: ¿Por qué no usar Spring Data REST o GraphQL en lugar de endpoints manuales?**

R: Spring Data REST expone automáticamente los repositorios JPA como endpoints REST. Es útil para CRUDs simples, pero limita el control sobre la lógica de negocio (validaciones, transformaciones, flujos de estado). Con CQRS necesitamos ese control explícito. GraphQL habría sido excesivo para la complejidad actual.

**P: ¿Cómo se garantiza que el esquema de base de datos está sincronizado con el código?**

R: El script `formsDB.sql` es la fuente de verdad del esquema. `ddl-auto: none` significa que Hibernate no modifica el esquema. La gestión manual del esquema es adecuada para este proyecto. En un equipo grande con múltiples releases, se recomendaría Flyway o Liquibase para migraciones versionadas.

**P: ¿Los ficheros subidos son seguros? ¿Puede un atacante subir un fichero malicioso?**

R: El sistema valida la extensión del fichero contra la lista blanca del requisito documental. Adicionalmente, valida el nombre del fichero con regex. Los ficheros se almacenan fuera del directorio público del servidor web con un prefijo UUID. Una mejora de seguridad adicional sería validar el MIME type real del fichero (magic bytes), no solo la extensión, usando una librería como Apache Tika.

**P: ¿El sistema escala?**

R: El backend es stateless (JWT, sin sesiones). Puede escalarse horizontalmente añadiendo instancias sin configuración adicional. El cuello de botella sería el almacenamiento de ficheros en el sistema de ficheros local; en producción se migraría a almacenamiento distribuido (S3, Azure Blob Storage). La base de datos MySQL puede replicarse con read replicas. Para mayor escala, CQRS facilita separar los stores de lectura y escritura.

**P: ¿Por qué no se usa Spring WebFlux para un backend reactivo?**

R: Spring WebFlux (reactivo) introduce complejidad cognitiva significativa: programación reactiva con Mono/Flux, backpressure, manejo de errores reactivo. Para este caso de uso, con Java 21 y Virtual Threads de Project Loom, Spring MVC bloqueante tiene rendimiento comparable al reactivo sin la complejidad adicional. La elección pragmática fue Spring MVC.

**P: ¿Cómo se testea esta arquitectura?**

R: La separación en capas facilita tres niveles de test:
- **Tests unitarios**: los handlers de comandos y queries se pueden testear inyectando mocks de los puertos (repositorios).
- **Tests de integración**: los adaptadores JPA contra una base de datos de test (H2 en memoria o MySQL Testcontainers).
- **Tests E2E**: el controlador REST con MockMvc o un cliente HTTP real.

---

## 12. Diagrama de capas (resumen visual)

```
┌──────────────────────────────────────────────────────────────┐
│                     FRONTEND (Angular 21)                     │
│   Pages → Components → Services (API) → HTTP Interceptors    │
│   Guards (authGuard, candidateGuard)                         │
│   AuthService (Signals + localStorage JWT)                   │
└──────────────────────────┬───────────────────────────────────┘
                           │  HTTP / REST (JSON)
           ┌───────────────▼───────────────────────────────┐
           │         ADAPTADORES DE ENTRADA (input/)         │
           │   REST Controllers + JwtAuthFilter + CORS       │
           └───────────────┬───────────────────────────────┘
                           │  Commands / Queries
           ┌───────────────▼───────────────────────────────┐
           │          APLICACIÓN (application/)              │
           │   CommandBus / QueryBus                        │
           │   Command Handlers + Query Handlers            │
           │   DTOs de entrada y salida                     │
           └───────────────┬───────────────────────────────┘
                           │  llama a Puertos (interfaces)
           ┌───────────────▼───────────────────────────────┐
           │            DOMINIO (domain/)                   │
           │   Entidades: AdminUsers, Candidates,           │
           │              OnboardingProc, DataFields...     │
           │   Value Objects: OnboardingStatus, FieldType,  │
           │                  ValidationStatus              │
           │   Puertos de salida: *RepositoryPort           │
           └───────────────┬───────────────────────────────┘
                           │  implementados por
           ┌───────────────▼───────────────────────────────┐
           │        ADAPTADORES DE SALIDA (output/)          │
           │   *RepositoryAdapter → Spring Data JPA → MySQL │
           │   NotificationsService         → Gmail SMTP    │
           │   FileStorageService           → ./uploads/    │
           └────────────────────────────────────────────────┘
```

---

## 13. Estructura de directorios clave

```
formulariosRRHH/
├── CONFIGURATION.md                      ← Guía de setup
├── DEFENSA_TECNICA.md                    ← Este documento
├── onboarding-backend/
│   ├── pom.xml                           ← Dependencias Maven
│   └── src/main/
│       ├── java/com/onboarding/
│       │   ├── OnboardingApplication.java
│       │   ├── shared/                   ← Bus de comandos y queries (CQRS)
│       │   ├── config/                   ← Security, JWT, CORS
│       │   ├── adminUsers/               ← Módulo admin (23 clases)
│       │   ├── candidates/               ← Módulo candidatos (13)
│       │   ├── onboardingProcesses/      ← Flujo principal (22)
│       │   ├── dataFields/               ← Formulario dinámico (20)
│       │   ├── fieldOptions/             ← Opciones SELECT (15)
│       │   ├── dataAnswers/              ← Respuestas (13)
│       │   ├── documentRequirements/     ← Docs requeridos (23)
│       │   ├── uploadedDocuments/        ← Ficheros subidos (15)
│       │   ├── accessTokens/             ← Tokens candidato (10)
│       │   └── notifications/            ← Emails (13)
│       └── resources/
│           ├── application.yaml
│           ├── application-secret.properties  ← No en git
│           └── database/formsDB.sql      ← Esquema de BD
├── onboarding-frontend/
│   ├── package.json
│   └── src/app/
│       ├── app.config.ts                 ← DI providers
│       ├── app.routes.ts                 ← Routing
│       ├── core/                         ← Guards, interceptors, auth
│       ├── modules/                      ← Lógica por dominio
│       ├── pages/                        ← Rutas/vistas
│       └── shared/                       ← Layout, componentes comunes
└── uploads/                              ← Ficheros subidos por candidatos
```
"Spring me inyecta todos los handlers en el constructor. Por cada uno, uso ResolvableType —utilidad reflectiva de Spring— para leer el primer parámetro genérico de la interfaz CommandHandler<C,R> y extraer el tipo de comando C. Con eso construyo un Map<Class<?>, Handler> en arranque, y en dispatch hago lookup por command.getClass(). Es O(1) en runtime y O(n) solo una vez al levantar la aplicación."
"Cuando la aplicación arranca, Spring me pasa automáticamente la lista de todos los especialistas que existen. Yo los recorro uno a uno y a cada uno le pregunto: '¿qué tipo de petición sabes manejar tú?'. Con esa información voy rellenando una libreta interna: 'sobre tipo A → especialista 1, sobre tipo B → especialista 2', y así con todos. Cuando luego llega un sobre, miro su tipo y consulto la libreta — encontrar al especialista correcto es instantáneo. Lo único que cuesta un poco es montar la libreta al principio, pero eso solo pasa una vez, al arrancar."
---


### Backend:
 Domain Model — la entidad pura del negocio
📁 AdminUsers.java


public class AdminUsers {
    private Long id;
    private String email;
    private String passwordHash;
    private String fullName;
    ...
}
Función: representar el concepto de "administrador" en el lenguaje del negocio, sin que sepa nada de bases de datos, JSON, Spring ni JPA. Fíjate que no tiene ninguna anotación de framework.

¿Por qué existe? Para que la lógica de negocio trabaje con un objeto limpio. Si mañana cambias MySQL por MongoDB, esta clase no se toca.

2️⃣ Repository Port — el contrato del dominio
📁 AdminUsersRepositoryPort.java


public interface AdminUsersRepositoryPort {
    AdminUsers save(AdminUsers adminUser);
    Optional<AdminUsers> findByEmail(String email);
    ...
}
Función: es una interfaz (un contrato). El dominio dice: "yo necesito poder guardar admins y buscarlos por email — quien quiera, que me lo implemente, no me importa cómo".

Metáfora: es como un enchufe en la pared. El dominio define el agujero (el puerto). Cualquier aparato que tenga la clavija correcta (cualquier adaptador) se puede enchufar.

¿Por qué se llama "Port"? Porque en arquitectura hexagonal, los puertos son las interfaces del dominio y los adaptadores son las implementaciones. Tú aquí defines el puerto.

3️⃣ Command — la intención de modificar algo
📁 LoginCommand.java


public class LoginCommand implements Command {
    private final String email;
    private final String password;
}
Función: es un objeto-mensaje que dice "quiero hacer login con estos datos". No tiene lógica, solo guarda los datos necesarios para ejecutar la acción.

Metáfora: es el sobre cerrado que llega a la oficina de correos del bus. Dentro lleva los datos. El sobre solo sabe qué quiere, no cómo se hace.

Otros commands de tu proyecto: CreateAdminUserCommand, DeleteAdminUserCommand, ForgotPasswordCommand, ResetPasswordCommand.

4️⃣ Command Handler — quien ejecuta el command
📁 LoginCommandHandler.java


@Component
public class LoginCommandHandler implements CommandHandler<LoginCommand, LoginResponse> {
    public LoginResponse handle(LoginCommand command) {
        AdminUsers admin = adminUsersPort.findByEmail(command.getEmail())...
        if (!passwordEncoder.matches(...)) { throw ... }
        String token = jwtService.generateToken(admin.getEmail());
        return LoginResponse.builder()...build();
    }
}
Función: aquí está la lógica de negocio real. El handler:

Busca al admin por email (a través del puerto)
Compara la contraseña con BCrypt
Genera un JWT
Devuelve la respuesta
Es el "especialista" que recibe el sobre y ejecuta la acción.

5️⃣ Query y Query Handler — lo mismo pero para leer
📁 GetAllAdminUsersQuery.java + GetAllAdminUsersQueryHandler.java


public class GetAllAdminUsersQuery implements Query<List<AdminUsersDto.Response>> {}

@Component
public class GetAllAdminUsersQueryHandler implements QueryHandler<GetAllAdminUsersQuery, List<AdminUsersDto.Response>> {
    public List<AdminUsersDto.Response> handle(GetAllAdminUsersQuery query) {
        return adminUsersPort.findAll().stream().map(mapper::toResponse).toList();
    }
}
Función: una Query representa una petición de lectura ("dame los datos de X"). El QueryHandler la resuelve. No modifica nada del sistema, solo consulta.

Diferencia con Command: un Command modifica estado, una Query solo lee. Esa separación es CQRS.

6️⃣ DTO (Data Transfer Object) — el formato de entrada/salida
📁 AdminUsersDto.java


public class AdminUsersDto {
    public static class LoginRequest { String email; String password; }
    public static class LoginResponse { Long id; String email; String fullName; String token; }
    public static class CreateRequest { ... }
    public static class Response { ... }
}
Función: son las estructuras de datos que viajan por la API HTTP. Lo que el frontend envía y recibe.

¿Por qué no usar directamente la entidad de dominio? Por dos razones:

Seguridad: la entidad AdminUsers tiene passwordHash y resetToken. Si la devolvieras tal cual, expondrías secretos. El Response DTO solo contiene lo que es seguro mostrar.
Forma diferente: la entrada (CreateRequest) trae password en texto plano, pero el dominio guarda passwordHash. Son formas distintas para distintos propósitos.
Metáfora: son los formularios oficiales que se rellenan para hablar con la API. La entidad de dominio es lo que vive dentro de la empresa; el DTO es lo que sale al exterior.

7️⃣ Controller — la puerta de entrada HTTP
📁 AdminUsersCommandController.java


@RestController
@RequestMapping("/api/admin")
public class AdminUsersCommandController {
    private final CommandBus commandBus;

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(
            commandBus.dispatch(new LoginCommand(request.getEmail(), request.getPassword()))
        );
    }
}
Función: traduce HTTP → Command/Query. Es la única clase que sabe que existe HTTP. Su trabajo:

Recibir la petición HTTP
Sacar los datos del JSON (@RequestBody)
Crear un Command/Query
Pasarlo al bus
Devolver la respuesta como JSON
Fíjate que el controller no tiene lógica de negocio. Es una capa fina que adapta el mundo HTTP al mundo de comandos.

Hay dos controladores por módulo:

*CommandController → endpoints que modifican (POST, PUT, DELETE)
*QueryController → endpoints que solo leen (GET)
8️⃣ JPA Entity — la representación para la base de datos
📁 AdminUsersJpaEntity.java


@Entity
@Table(name = "admin_users")
public class AdminUsersJpaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;
    ...
}
Función: es la versión "para JPA" de la entidad. Tiene anotaciones (@Entity, @Table, @Column) que le dicen a Hibernate cómo guardarla en la tabla admin_users de MySQL.

¿Por qué duplicar la clase entonces? Porque AdminUsers (dominio) es libre — no tiene atadura a MySQL. AdminUsersJpaEntity (infraestructura) sí está atada. Si mañana cambias de tecnología, borras esta clase y haces otra; el dominio no se entera.

9️⃣ JPA Repository — el ejecutor SQL automático
📁 AdminUsersJpaRepository.java


public interface AdminUsersJpaRepository extends JpaRepository<AdminUsersJpaEntity, Long> {
    Optional<AdminUsersJpaEntity> findByEmail(String email);
    Optional<AdminUsersJpaEntity> findByResetToken(String token);
    boolean existsByEmail(String email);
}
Función: Spring Data JPA genera el SQL automáticamente a partir del nombre del método. findByEmail(String email) se traduce a SELECT * FROM admin_users WHERE email = ?. No escribes ni una línea de SQL.

Ya viene con save(), findAll(), findById(), deleteById() heredados de JpaRepository.

Es un "interruptor mágico": con declarar el método, ya funciona.

🔟 Adapter — el puente entre el dominio y JPA
📁 AdminUsersAdapter.java


@Component
public class AdminUsersAdapter implements AdminUsersRepositoryPort {
    private final AdminUsersJpaRepository repository;
    private final AdminUsersMapper mapper;

    public AdminUsers save(AdminUsers adminUser) {
        return mapper.toDomain(repository.save(mapper.toEntity(adminUser)));
    }

    public Optional<AdminUsers> findByEmail(String email) {
        return repository.findByEmail(email).map(mapper::toDomain);
    }
}
Función: es la clase clave que conecta los dos mundos. Implementa el puerto del dominio usando JPA.

Lo que hace cada método:

Recibe una AdminUsers (mundo del dominio)
Llama al mapper para convertirla en AdminUsersJpaEntity (mundo JPA)
Llama al JpaRepository para guardar/buscar
Convierte el resultado de vuelta a AdminUsers
Lo devuelve
Metáfora: es el traductor entre dos idiomas. El dominio habla "AdminUsers", la base de datos habla "AdminUsersJpaEntity", y el adapter traduce en ambas direcciones.

1️⃣1️⃣ Mapper — el traductor especializado
📁 AdminUsersMapper.java


@Component
public class AdminUsersMapper {
    public AdminUsers toDomain(AdminUsersJpaEntity entity) { ... }
    public AdminUsersJpaEntity toEntity(AdminUsers domain) { ... }
    public AdminUsersDto.Response toResponse(AdminUsers admin) { ... }
}
Función: centraliza la conversión entre los tres tipos que existen en cada módulo:

AdminUsers (dominio)
AdminUsersJpaEntity (JPA)
AdminUsersDto.Response (API)
¿Por qué tener un mapper aparte y no hacerlo dentro del adapter? Porque la conversión se reutiliza en varios sitios (en el adapter Y en los handlers que devuelven DTOs al frontend). Centralizando en una sola clase, si cambias un campo, lo cambias una vez.

### Frontend:

Las piezas, una a una
1️⃣ app.config.ts — la configuración raíz
📁 app.config.ts


export const appConfig: ApplicationConfig = {
  providers: [
    { provide: ADMIN_USERS_REPOSITORY, useExisting: AdminUsersApi },
    { provide: CANDIDATES_REPOSITORY, useExisting: CandidatesApi },
    ...
    provideRouter(routes, withComponentInputBinding()),
    provideHttpClient(withInterceptors([authInterceptor, errorInterceptor])),
    provideToastr({ ... }),
  ],
};
Función: es el panel de configuración global de la aplicación. Aquí se decide:

Qué clase concreta se inyecta cuando alguien pide un repositorio (useExisting)
Qué interceptores se aplican a todas las peticiones HTTP
Qué configuración tiene el router y los toasts
Es el equivalente al application.yaml + configuración de Spring del backend.

2️⃣ app.routes.ts — el mapa de rutas
📁 app.routes.ts


export const routes: Routes = [
  { path: 'login', loadComponent: () => import('./pages/...').then(m => m.LoginPageComponent) },
  {
    path: 'admin',
    loadComponent: () => import('./shared/...'),
    canActivate: [authGuard],
    children: [
      { path: 'candidates', loadComponent: () => import('./pages/candidates/...') },
      ...
    ],
  },
  {
    path: 'onboarding',
    loadComponent: () => import('./pages/accessTokens/...'),
    canActivate: [candidateGuard],
  },
];
Función: define qué se muestra en cada URL. Hay tres cosas importantes:

a) loadComponent (lazy loading):
El componente no se carga hasta que el usuario visita esa ruta. Esto reduce el tamaño inicial del bundle: el navegador no descarga el código de "/admin/users" si el usuario solo va a "/login".

b) canActivate: [authGuard]:
Antes de mostrar la ruta, se ejecuta el guard. Si devuelve false, no entra. Es la "puerta con cerradura".

c) Rutas anidadas (children):
/admin tiene un layout común (sidebar, header) y las rutas hijas se renderizan dentro de él.

3️⃣ Standalone Components — el patrón moderno de Angular
📁 adminUsers-forms.component.ts


@Component({
  selector: 'app-admin-users-form',
  imports: [ReactiveFormsModule, RouterLink],   // ← imports DENTRO del componente
  templateUrl: './adminUsers-forms.component.html',
  styleUrl: './adminUsers-forms.component.css',
})
export class AdminUsersFormsComponent { ... }
Función: todos los componentes son standalone. Antes (hasta Angular 14) había que declararlos en un fichero NgModule con declarations: [...] e imports: [...]. Ahora cada componente declara lo que necesita él mismo dentro del propio decorador @Component.

Ventajas:

Menos boilerplate → no hay ficheros *.module.ts por cada feature
Tree-shaking más eficiente → Angular sabe exactamente qué se usa y qué no
Lazy loading directo → puedes hacer loadComponent (no loadModule)
Es la dirección oficial de Angular desde la v17. Lo verán como decisión técnica moderna.

4️⃣ AuthService — gestión de sesión con Signals
📁 auth.service.ts


@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly TOKEN_KEY = 'civica_token';

  isAuthenticated = signal(!!localStorage.getItem(this.TOKEN_KEY));
  currentUser = signal<AuthUser | null>(...);

  setSession(token: string, user: AuthUser): void {
    localStorage.setItem(this.TOKEN_KEY, token);
    this.isAuthenticated.set(true);
    this.currentUser.set(user);
  }

  getToken(): string | null { return localStorage.getItem(this.TOKEN_KEY); }
  logout(): void { ... }
}
Función: centraliza todo lo que tiene que ver con la sesión del usuario:

Guarda el JWT en localStorage (clave civica_token)
Guarda los datos del usuario (civica_user)
Expone dos Signals reactivos: isAuthenticated y currentUser
Lo importante: usa Signals, no Observables.

Un signal es la nueva forma reactiva de Angular (v17+). Cuando el valor cambia, cualquier componente que lo lea se actualiza solo, sin subscribe() ni async pipe. Más simple y más rápido que BehaviorSubject de RxJS.

Metáfora: un Signal es como una variable mágica que avisa cuando cambia. Tú la lees normal, pero si cambia, todo lo que dependa de ella se entera al instante.

5️⃣ Guards — los porteros de las rutas
📁 auth.guard.ts


export const authGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (auth.isAuthenticated()) return true;
  return router.createUrlTree(['/login']);
};
Función: es una función que se ejecuta antes de entrar en una ruta. Si devuelve true, se entra. Si devuelve un UrlTree, se redirige.

📁 candidate.guard.ts


export const candidateGuard: CanActivateFn = (route) => {
  const router = inject(Router);
  const token = route.queryParamMap.get('token');

  if (token) return true;
  return router.createUrlTree(['/login']);
};
Hay dos guards diferentes para dos tipos de usuario:

Guard	Protege	Comprueba	Si falla
authGuard	/admin/*	Que haya JWT en localStorage	Redirige a /login
candidateGuard	/onboarding	Que la URL traiga ?token=...	Redirige a /login
¿Por qué dos? Porque admin y candidato tienen sistemas de autenticación diferentes:

El admin inicia sesión con email/password → JWT
El candidato no tiene cuenta → entra con un link único (?token=xxx)
6️⃣ Interceptores HTTP — la capa transversal
authInterceptor — pone el token en cada petición
📁 auth.interceptor.ts


export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = inject(AuthService).getToken();
  if (token) {
    return next(req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }));
  }
  return next(req);
};
Función: se ejecuta antes de cada petición HTTP saliente. Coge el token del AuthService y lo añade al header Authorization.

¿Por qué es útil? Para no tener que hacer esto en cada llamada API:


// Sin interceptor, harías esto en CADA petición ❌
this.http.get(url, { headers: { Authorization: `Bearer ${token}` } })

// Con interceptor, simplemente ✅
this.http.get(url)  // el header se añade solo
Metáfora: es como una secretaría que sella todos los sobres antes de mandarlos. Tú escribes la carta sin preocuparte del sello — la secretaría siempre lo pone.

errorInterceptor — atrapa todos los errores de la API
📁 error.interceptor.ts


export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    catchError((error) => {
      if (error.status === 401) {
        auth.logout();
        router.navigate(['/login']);
        toast.error('Sesión expirada. Inicia sesión de nuevo.');
      } else if (error.status === 403) { toast.error('No tienes permisos...'); }
      else if (error.status === 0) { toast.error('No se puede conectar...'); }
      else { toast.error(message); }
      return throwError(() => error);
    })
  );
};
Función: intercepta todas las respuestas con error y aplica una política global:

Código HTTP	Acción
401 (no autorizado)	Logout + redirigir a /login + toast "Sesión expirada"
403 (sin permisos)	Toast "No tienes permisos"
0 (sin conexión)	Toast "No se puede conectar con el servidor"
Otros	Toast con el mensaje del backend
¿Por qué es útil? Sin este interceptor, cada componente tendría que manejar a mano qué hacer si llega un 401. Aquí está centralizado: una sola regla para toda la app.

7️⃣ Domain (módulo) — los contratos del módulo
adminUsers.ts — los tipos del dominio
📁 adminUsers.ts


export interface AdminUser { id: number; email: string; fullName: string; createdAt: string; }
export interface LoginRequest { email: string; password: string; }
export interface LoginResponse { id: number; email: string; ...; token: string; }
Función: definir las estructuras de datos que el módulo maneja. Son interface de TypeScript, no clases, porque en frontend solo necesitas tipado, no comportamiento.

Equivale a: los DTOs y entidades de dominio del backend, todo junto.

adminUsers.repository.ts — el puerto del dominio
📁 adminUsers.repository.ts


export interface IAdminUsersRepository {
  login(request: LoginRequest): Observable<LoginResponse>;
  create(request: CreateAdminRequest): Observable<AdminUser>;
  findAll(): Observable<AdminUser[]>;
  ...
}
Función: es el puerto del módulo, igual que en el backend. Define qué necesita poder hacer el módulo, sin decir cómo. La implementación concreta (AdminUsersApi) está en infraestructura.

Esto es lo que hace que el módulo sea hexagonal en frontend también. El servicio depende de la interfaz, no de HTTP. Si mañana cambias HTTP por GraphQL, el servicio no se entera.

8️⃣ InjectionToken — la pieza clave del DI
📁 adminUsers.token.ts


export const ADMIN_USERS_REPOSITORY = new InjectionToken<IAdminUsersRepository>('IAdminUsersRepository');
Función: un InjectionToken es un identificador único que Angular usa para inyectar implementaciones de interfaces.

¿Por qué hace falta esto?
TypeScript borra las interfaces al compilar. Si intentas hacer inject(IAdminUsersRepository), Angular no sabe qué clase darte porque la interfaz ya no existe en runtime. Solución: crear un token (un objeto real) que sí existe en runtime y que sirve de "etiqueta".

Y luego en app.config.ts:


{ provide: ADMIN_USERS_REPOSITORY, useExisting: AdminUsersApi }
Esto le dice a Angular: "cuando alguien pida ADMIN_USERS_REPOSITORY, dale la instancia de AdminUsersApi".

Esto es lo que permite testear: en un test, cambias useExisting: AdminUsersApi por useValue: mockRepository y todo el código que usa el token recibe el mock sin tocar nada más.

9️⃣ Service (application) — el coordinador
📁 adminUsers.service.ts


@Injectable({ providedIn: 'root' })
export class AdminUsersService {
  private readonly repository = inject(ADMIN_USERS_REPOSITORY);
  private readonly auth = inject(AuthService);

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.repository.login(request).pipe(
      tap((res) =>
        this.auth.setSession(res.token, { id: res.id, email: res.email, fullName: res.fullName })
      )
    );
  }
  ...
}
Función: es la capa de aplicación del módulo. Coordina llamadas al repositorio + acciones colaterales.

Fíjate en login: no solo llama al backend; además guarda la sesión con auth.setSession(...). Esa orquestación está aquí, no en el componente.

Es el equivalente frontend al CommandHandler/QueryHandler del backend.

🔟 API (infrastructure) — el ejecutor HTTP
📁 adminUsers.api.ts


@Injectable({ providedIn: 'root' })
export class AdminUsersApi {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/api/admin`;

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.url}/auth/login`, request);
  }
  ...
}
Función: implementa el puerto del dominio (IAdminUsersRepository) usando HttpClient de Angular.

Es el adaptador del frontend, equivalente al *Adapter del backend que implementa el *RepositoryPort.

Si mañana quieres mockear esto en tests: creas otra clase que implemente IAdminUsersRepository y la registras con el token. El servicio no se entera.

1️⃣1️⃣ Form Component — la UI con Reactive Forms
📁 adminUsers-forms.component.ts


export class AdminUsersFormsComponent {
  private readonly fb = inject(FormBuilder);
  private readonly adminService = inject(AdminUsersService);

  loading = signal(false);

  form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required],
  });

  submit(): void {
    if (this.form.invalid) return;
    this.loading.set(true);
    const { email, password } = this.form.value;
    this.adminService.login({ email: email!, password: password! }).subscribe({
      next: () => { this.toast.success(...); this.router.navigate(['/admin/candidates']); },
      error: () => this.loading.set(false),
    });
  }
}
Función: maneja la pantalla del login. Usa Angular Reactive Forms, que es el sistema de formularios reactivo de Angular:

FormBuilder.group({...}) crea un grupo de campos con validaciones
Validators.required, Validators.email son reglas declarativas
form.invalid, form.value exponen estado y datos
El template (HTML) usa [formGroup] y formControlName para conectarse al modelo
¿Por qué Reactive Forms y no Template-driven Forms? Más predecibles, más fáciles de testear, mejor tipado. Y permiten validaciones complejas con código (no solo con atributos HTML).

Fíjate también en loading = signal(false) — un signal local del componente para deshabilitar el botón mientras llega la respuesta.

1️⃣2️⃣ Page Component — el contenedor de página
📁 pages/adminUsers/page.adminUsers-component.ts


@Component({
  selector: 'app-login-page',
  imports: [AdminUsersFormsComponent],
  template: '<app-admin-users-form />',
})
export class LoginPageComponent {}
Función: es el componente que se monta en una ruta. Aquí simplemente envuelve el formulario, pero en otras páginas (lista de candidatos, detalle de proceso...) coordina varios componentes.

Distinción importante:

Pages (/pages) → componentes que corresponden a URLs
Components (/modules/.../components) → componentes reutilizables que viven dentro de una página
Esto evita que las URLs y los componentes reutilizables se mezclen.

1️⃣3️⃣ ToastService — wrapper de notificaciones
📁 toast.service.ts


@Injectable({ providedIn: 'root' })
export class ToastService {
  constructor(private toastr: ToastrService) {}
  success(message: string, title = 'Éxito'): void { ... }
  error(message: string, title = 'Error'): void { ... }
  ...
}
Función: envuelve la librería ngx-toastr con una API más sencilla y con títulos en español por defecto.

¿Por qué un wrapper en lugar de usar ToastrService directamente? Si mañana quieres cambiar de librería de notificaciones, solo tocas esta clase. El resto del código no se entera.

Es el mismo principio que en el resto: abstraer las dependencias externas para poder cambiarlas sin afectar a la app

### JWT Token — la clave de la autenticación
1. Encriptación / Firma
Tu JWT no se cifra, se firma (HMAC simétrico). Esto es estándar JWT: el payload va en Base64URL legible, pero con una firma que impide modificarlo sin la clave.

En JwtService.java:22-24:


private SecretKey getKey() {
    return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
}
Algoritmo: HMAC-SHA. Keys.hmacShaKeyFor elige automáticamente HS256 / HS384 / HS512 según el tamaño en bytes de tu JWT_SECRET (mínimo 32 bytes para HS256).
Clave: variable de entorno JWT_SECRET (ver application.yaml:53-55). Es simétrica: la misma clave firma y verifica.
Librería: io.jsonwebtoken (jjwt).
2. Generación (login)
En LoginCommandHandler.java:21-37:

El admin envía email + password a POST /api/admin/auth/login (ruta pública en SecurityConfig.java:36).
Se busca el AdminUsers por email.
BCryptPasswordEncoder.matches() compara la contraseña contra el hash BCrypt almacenado.
Si coincide, JwtService.generateToken() emite un token con:
sub = email
iat = ahora
exp = ahora + JWT_EXPIRATION
firma HMAC-SHA con JWT_SECRET
El token se devuelve dentro de LoginResponse junto con id, email y fullName.
3. Uso (cada request autenticada)
JwtAuthFilter.java corre antes de UsernamePasswordAuthenticationFilter (SecurityConfig.java:42) y para cada request:

Lee el header Authorization. Si no empieza con Bearer , deja pasar sin autenticar (JwtAuthFilter.java:32).
Quita el prefijo Bearer y obtiene el token (JwtAuthFilter.java:37).
isTokenValid() parsea y verifica firma + fecha de expiración (JwtService.java:39-45). Cualquier excepción (firma inválida, expirado, malformado) → false.
Si es válido, extrae el email del sub, carga el UserDetails desde UserDetailsService y monta un UsernamePasswordAuthenticationToken en el SecurityContextHolder (JwtAuthFilter.java:46-52).
La sesión es STATELESS (SecurityConfig.java:33): el contexto se construye en cada request y no se persiste en HttpSession.
Rutas públicas que no requieren token: OPTIONS /**, login, forgot-password, reset-password, y todo /api/candidate/**. El resto exige autenticación (SecurityConfig.java:34-41).

4. Vida del token
En application.yaml:55:


expiration: ${JWT_EXPIRATION:28800000}
Duración por defecto: 28 800 000 ms = 8 horas desde el issuedAt.
Configurable por env var JWT_EXPIRATION (en milisegundos).
No hay refresh token, ni revocación, ni blacklist. Una vez firmado, el token es válido hasta exp aunque cambies la contraseña del admin o lo "desloguees" (el logout es solo client-side: borrar el token).
Si cambias JWT_SECRET, todos los tokens vivos quedan inválidos al instante (la verificación de firma falla).
Observaciones
El sub solo lleva el email; los roles/permisos no viajan en el token, se vuelven a consultar vía userDetailsService.loadUserByUsername() en cada request. Eso es bueno (roles cambiados se reflejan ya), pero implica un hit a BD por petición autenticada.
En JwtAuthFilter.java:39-42, si el token es inválido el filtro deja pasar la request sin autenticar en lugar de devolver 401; el 401 lo emitirá Spring Security después al ver que no hay autenticación en una ruta protegida.
