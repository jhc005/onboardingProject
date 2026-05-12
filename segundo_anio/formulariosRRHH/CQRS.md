# CQRS en el Proyecto Onboarding RRHH

## ¿Qué es CQRS?

**CQRS** (Command Query Responsibility Segregation) es un patrón arquitectónico que separa las operaciones de **escritura** (Commands) de las de **lectura** (Queries) en canales completamente distintos. La idea central es que una operación que modifica estado no debería al mismo tiempo devolver datos de lectura general, y viceversa.

En este proyecto, CQRS se implementa de forma completa: cada acción que cambia estado tiene su propio `Command` + `CommandHandler`, y cada consulta tiene su propio `Query` + `QueryHandler`. Los controladores REST se dividen en `*CommandController` y `*QueryController` según su responsabilidad.

---

## La carpeta `shared/cqrs`

Ubicación: `onboarding-backend/src/main/java/com/onboarding/shared/cqrs/`

Esta carpeta contiene el **framework CQRS propio del proyecto**: 6 interfaces y 2 implementaciones concretas de Spring. No se usa ninguna librería externa de CQRS; todo está construido a mano.

```
shared/cqrs/
├── Command.java              ← Marker interface para comandos
├── CommandBus.java           ← Contrato del bus de comandos
├── CommandHandler.java       ← Contrato de cada handler de comando
├── Query.java                ← Marker interface para queries (genérica en R)
├── QueryBus.java             ← Contrato del bus de queries
├── QueryHandler.java         ← Contrato de cada handler de query
├── SpringCommandBus.java     ← Implementación Spring del bus de comandos
└── SpringQueryBus.java       ← Implementación Spring del bus de queries
```

### Las 6 interfaces

#### `Command.java`
```java
public interface Command {}
```
Interfaz marcadora vacía. Cualquier clase que la implemente se convierte en un comando reconocido por el bus.

#### `CommandBus.java`
```java
public interface CommandBus {
    <R> R dispatch(Command command);
}
```
Define el único punto de entrada para despachar comandos. Devuelve un resultado genérico `R` (puede ser `void` o cualquier DTO). Los controladores solo conocen esta interfaz, nunca el handler concreto.

#### `CommandHandler.java`
```java
public interface CommandHandler<C extends Command, R> {
    R handle(C command);
}
```
Contrato que debe implementar cada handler. El parámetro `C` es el tipo del comando específico que maneja, y `R` es el tipo de respuesta.

#### `Query.java`
```java
public interface Query<R> {}
```
Interfaz marcadora genérica. El parámetro `R` indica el tipo de dato que devolverá la query. Esta genericidad en la interfaz misma permite que el bus sea completamente typesafe en compilación.

#### `QueryBus.java`
```java
public interface QueryBus {
    <R> R ask(Query<R> query);
}
```
Punto de entrada único para consultas. El método se llama `ask` (no `dispatch`) para reforzar semánticamente que es una operación de lectura.

#### `QueryHandler.java`
```java
public interface QueryHandler<Q extends Query<R>, R> {
    R handle(Q query);
}
```
Contrato de cada handler de consulta. Análogo a `CommandHandler` pero para el lado de lectura.

---

### Las 2 implementaciones Spring

#### `SpringCommandBus.java`

```java
@Component
public class SpringCommandBus implements CommandBus {

    private final Map<Class<?>, CommandHandler<?, ?>> handlers = new HashMap<>();

    @SuppressWarnings("rawtypes")
    public SpringCommandBus(List<CommandHandler> handlerList) {
        for (CommandHandler handler : handlerList) {
            Class<?> commandType = ResolvableType.forClass(handler.getClass())
                    .as(CommandHandler.class)
                    .getGeneric(0)
                    .resolve();
            if (commandType != null) {
                handlers.put(commandType, handler);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> R dispatch(Command command) {
        CommandHandler<Command, R> handler =
                (CommandHandler<Command, R>) handlers.get(command.getClass());

        if (handler == null) {
            throw new IllegalStateException(
                    "No se encontró handler para el comando: " + command.getClass().getName());
        }

        return handler.handle(command);
    }
}
```

**Cómo funciona el registro automático:**

1. Spring detecta todos los beans que implementan `CommandHandler` (gracias a que cada handler está anotado con `@Component`).
2. Spring los inyecta como una `List<CommandHandler>` en el constructor del bus.
3. El bus itera esa lista y usa `ResolvableType` (utilidad de Spring Core) para inspeccionar los tipos genéricos en tiempo de ejecución. Específicamente extrae el primer parámetro genérico (`getGeneric(0)`), que es el tipo del `Command` que maneja ese handler.
4. Construye un `Map<Class<?>, CommandHandler>` donde la clave es la clase del comando y el valor es el handler correspondiente.
5. Al hacer `dispatch(command)`, busca en el mapa por `command.getClass()` y delega al handler encontrado.

Si no hay ningún handler registrado para un comando, lanza `IllegalStateException` en lugar de fallar silenciosamente.

#### `SpringQueryBus.java`

Idéntico en estructura a `SpringCommandBus`, pero para queries. Usa `getGeneric(0)` para extraer el tipo `Q` de cada `QueryHandler<Q, R>` y construye el mismo mapa en tiempo de startup.

```java
@Component
public class SpringQueryBus implements QueryBus {

    private final Map<Class<?>, QueryHandler<?, ?>> handlers = new HashMap<>();

    public SpringQueryBus(List<QueryHandler> handlerList) {
        for (QueryHandler handler : handlerList) {
            Class<?> queryType = ResolvableType.forClass(handler.getClass())
                    .as(QueryHandler.class)
                    .getGeneric(0)
                    .resolve();
            if (queryType != null) {
                handlers.put(queryType, handler);
            }
        }
    }

    @Override
    public <R> R ask(Query<R> query) {
        QueryHandler<Query<R>, R> handler =
                (QueryHandler<Query<R>, R>) handlers.get(query.getClass());

        if (handler == null) {
            throw new IllegalStateException(
                    "No se encontró handler para la query: " + query.getClass().getName());
        }

        return handler.handle(query);
    }
}
```

**Por qué no hay ningún archivo de configuración `@Configuration`:** los buses son `@Component` y se auto-registran. Spring descubre todos los handlers mediante component scanning. No se necesita declarar ningún `@Bean` manualmente.

---

## Cómo se implementa CQRS en el proyecto

### Estructura de módulos

Cada dominio del proyecto (adminUsers, candidates, dataFields, onboardingProcesses, etc.) repite la misma estructura interna:

```
<modulo>/
├── application/
│   ├── commands/
│   │   ├── XxxCommand.java          ← Datos del comando (inmutable, solo @Getter)
│   │   └── XxxCommandHandler.java   ← Lógica de escritura
│   ├── queries/
│   │   ├── XxxQuery.java            ← Parámetros de la consulta
│   │   └── XxxQueryHandler.java     ← Lógica de lectura
│   └── dto/
│       └── XxxDto.java              ← DTOs de entrada/salida compartidos
├── domain/
│   ├── model/                       ← Entidades de dominio
│   └── outport/                     ← Interfaces de repositorio (puertos)
└── infrastructure/
    └── input/
        ├── XxxCommandController.java ← REST endpoints de escritura
        └── XxxQueryController.java  ← REST endpoints de lectura
```

### Convenciones del proyecto

| Elemento | Convención | Ejemplo |
|---|---|---|
| Comando | `VerbNombreCommand` | `LoginCommand`, `CreateDataFieldCommand` |
| Handler de comando | `VerbNombreCommandHandler` | `LoginCommandHandler` |
| Query | `GetXxxQuery` o `CanXxxQuery` | `GetAllAdminUsersQuery`, `CanModifyDataFieldsQuery` |
| Handler de query | `GetXxxQueryHandler` | `GetAllAdminUsersQueryHandler` |
| Controlador de escritura | `NombreCommandController` | `AdminUsersCommandController` |
| Controlador de lectura | `NombreQueryController` | `AdminUsersQueryController` |

---

## Flujo completo: ejemplo de Command

### Caso: Login de administrador

**1. Controlador recibe la petición HTTP**

`AdminUsersCommandController` — `POST /api/admin/auth/login`

```java
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminUsersCommandController {

    private final CommandBus commandBus;  // inyecta SpringCommandBus

    @PostMapping("/auth/login")
    public ResponseEntity<AdminUsersDto.LoginResponse> login(@RequestBody AdminUsersDto.LoginRequest request) {
        return ResponseEntity.ok(
            commandBus.dispatch(new LoginCommand(request.getEmail(), request.getPassword()))
        );
    }
}
```
El controlador no conoce nada de la lógica de negocio. Solo construye el comando con los datos del request y lo despacha al bus.

**2. El comando transporta los datos**

`LoginCommand.java`

```java
@Getter
@AllArgsConstructor
public class LoginCommand implements Command {
    private final String email;
    private final String password;
}
```
Clase inmutable con `@Getter` (Lombok). No tiene lógica, solo datos.

**3. El bus enruta al handler correcto**

`SpringCommandBus` busca en su mapa interno: `LoginCommand.class → LoginCommandHandler`. Si lo encuentra, invoca `handler.handle(command)`.

**4. El handler ejecuta la lógica de negocio**

`LoginCommandHandler.java`

```java
@Component
@RequiredArgsConstructor
public class LoginCommandHandler implements CommandHandler<LoginCommand, AdminUsersDto.LoginResponse> {

    private final AdminUsersRepositoryPort adminUsersPort;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AdminUsersDto.LoginResponse handle(LoginCommand command) {
        AdminUsers admin = adminUsersPort.findByEmail(command.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales incorrectas."));

        if (!passwordEncoder.matches(command.getPassword(), admin.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciales incorrectas.");
        }

        String token = jwtService.generateToken(admin.getEmail());

        return AdminUsersDto.LoginResponse.builder()
                .id(admin.getId())
                .email(admin.getEmail())
                .fullName(admin.getFullName())
                .token(token)
                .build();
    }
}
```

**Diagrama del flujo:**

```
POST /api/admin/auth/login
        │
        ▼
AdminUsersCommandController
        │  new LoginCommand(email, password)
        ▼
CommandBus.dispatch(command)
        │
        ▼
SpringCommandBus
  handlers.get(LoginCommand.class) → LoginCommandHandler
        │
        ▼
LoginCommandHandler.handle(command)
  │  findByEmail() → AdminUsersRepositoryPort
  │  passwordEncoder.matches()
  │  jwtService.generateToken()
        │
        ▼
AdminUsersDto.LoginResponse  →  200 OK
```

---

## Flujo completo: ejemplo de Query

### Caso: Listar todos los administradores

**1. Controlador recibe la petición**

`AdminUsersQueryController` — `GET /api/admin/users`

```java
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminUsersQueryController {

    private final QueryBus queryBus;

    @GetMapping("/users")
    public ResponseEntity<List<AdminUsersDto.Response>> findAll() {
        return ResponseEntity.ok(queryBus.ask(new GetAllAdminUsersQuery()));
    }
}
```

**2. La query (sin parámetros en este caso)**

```java
public class GetAllAdminUsersQuery implements Query<List<AdminUsersDto.Response>> {}
```

La query declara su tipo de retorno `List<AdminUsersDto.Response>` a nivel de generics. No tiene campos porque no necesita parámetros de filtrado.

**3. El handler consulta y mapea**

```java
@Component
@RequiredArgsConstructor
public class GetAllAdminUsersQueryHandler
        implements QueryHandler<GetAllAdminUsersQuery, List<AdminUsersDto.Response>> {

    private final AdminUsersRepositoryPort adminUsersPort;
    private final AdminUsersMapper mapper;

    @Override
    public List<AdminUsersDto.Response> handle(GetAllAdminUsersQuery query) {
        return adminUsersPort.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }
}
```

**Diagrama del flujo:**

```
GET /api/admin/users
        │
        ▼
AdminUsersQueryController
        │  new GetAllAdminUsersQuery()
        ▼
QueryBus.ask(query)
        │
        ▼
SpringQueryBus
  handlers.get(GetAllAdminUsersQuery.class) → GetAllAdminUsersQueryHandler
        │
        ▼
GetAllAdminUsersQueryHandler.handle(query)
  │  findAll() → AdminUsersRepositoryPort
  │  mapper.toResponse()
        │
        ▼
List<AdminUsersDto.Response>  →  200 OK
```

---

## Flujo completo: ejemplo de Command transaccional con efectos secundarios

### Caso: Candidato envía el formulario de onboarding

Este es el ejemplo más complejo del proyecto. El handler cambia el estado de un proceso, persiste el cambio y dispara una notificación por email.

```java
@Component
@RequiredArgsConstructor
public class SubmitProcessCommandHandler
        implements CommandHandler<SubmitProcessCommand, OnboardingProcDto.Response> {

    private final OnboardingProcRepositoryPort processPort;
    private final CandidatesRepositoryPort candidatesPort;
    private final NotificationsService notificationsService;
    private final OnboardingProcMapper mapper;

    @Override
    @Transactional
    public OnboardingProcDto.Response handle(SubmitProcessCommand command) {
        OnboardingProc process = processPort.findById(command.getProcessId())
                .orElseThrow(() -> new IllegalArgumentException("Proceso no encontrado."));

        if (process.getStatus() != OnboardingStatus.IN_PROGRESS) {
            throw new IllegalStateException("El proceso no está en estado IN_PROGRESS.");
        }

        process.setStatus(OnboardingStatus.SUBMITTED);
        process.setSubmittedAt(LocalDateTime.now());
        processPort.save(process);

        Candidates candidate = candidatesPort.findById(process.getCandidateId()).orElseThrow();
        String fullName = candidate.getFirstName() + " " + candidate.getLastName();
        notificationsService.sendFormSubmitted(command.getProcessId(), fullName);

        return mapper.toResponse(process);
    }
}
```

`@Transactional` se pone directamente en el handler, no en el controlador. Si el envío de notificación falla antes de hacer `save`, la transacción se revierte.

---

## Inventario completo de Commands y Queries

### Commands (22)

| Módulo | Command | Qué hace |
|---|---|---|
| accessTokens | `ValidateTokenCommand` | Valida un token de acceso de candidato |
| adminUsers | `LoginCommand` | Autentica un administrador |
| adminUsers | `CreateAdminUserCommand` | Crea un nuevo administrador |
| adminUsers | `DeleteAdminUserCommand` | Elimina un administrador |
| adminUsers | `ForgotPasswordCommand` | Inicia flujo de recuperación de contraseña |
| adminUsers | `ResetPasswordCommand` | Establece nueva contraseña con token |
| candidates | `CreateCandidateCommand` | Registra un nuevo candidato |
| dataAnswers | `SaveAnswersCommand` | Guarda respuestas del formulario |
| dataFields | `CreateDataFieldCommand` | Crea un campo personalizado del formulario |
| dataFields | `DeleteDataFieldCommand` | Elimina un campo del formulario |
| dataFields | `UpdateDataFieldCommand` | Actualiza un campo del formulario |
| documentRequirements | `CreateDocumentRequirementCommand` | Crea un requisito de documento |
| documentRequirements | `DeleteDocumentRequirementCommand` | Elimina un requisito de documento |
| documentRequirements | `UpdateDocumentRequirementCommand` | Actualiza un requisito de documento |
| documentRequirements | `UploadTemplateCommand` | Sube un archivo de plantilla |
| fieldOptions | `CreateFieldOptionCommand` | Crea una opción para un campo select |
| fieldOptions | `DeleteFieldOptionCommand` | Elimina una opción de campo |
| onboardingProcesses | `SubmitProcessCommand` | Candidato envía el formulario |
| onboardingProcesses | `ValidateProcessCommand` | Admin valida/aprueba un proceso |
| onboardingProcesses | `RejectProcessCommand` | Admin rechaza un proceso |
| onboardingProcesses | `ResendAccessLinkCommand` | Reenvía enlace de acceso al candidato |
| uploadedDocuments | `UploadDocumentCommand` | Candidato sube un documento |

### Queries (14)

| Módulo | Query | Qué devuelve |
|---|---|---|
| adminUsers | `GetAllAdminUsersQuery` | Lista de todos los administradores |
| candidates | `GetAllCandidatesQuery` | Lista de todos los candidatos |
| dataAnswers | `GetAnswersByProcessQuery` | Respuestas de un proceso específico |
| dataFields | `GetAllDataFieldsQuery` | Todos los campos del formulario |
| dataFields | `CanModifyDataFieldsQuery` | Boolean: ¿se pueden modificar los campos? |
| documentRequirements | `GetAllDocumentRequirementsQuery` | Lista de requisitos de documentos |
| documentRequirements | `CanModifyDocumentRequirementsQuery` | Boolean: ¿se pueden modificar requisitos? |
| documentRequirements | `DownloadTemplateQuery` | Bytes del archivo de plantilla |
| fieldOptions | `GetFieldOptionsByDataFieldQuery` | Opciones de un campo específico |
| notifications | `GetNotificationsByProcessQuery` | Notificaciones de un proceso |
| onboardingProcesses | `GetAllProcessesQuery` | Todos los procesos de onboarding |
| onboardingProcesses | `GetProcessByIdQuery` | Un proceso específico por ID |
| uploadedDocuments | `GetDocumentsByProcessQuery` | Documentos subidos por proceso |
| uploadedDocuments | `DownloadDocumentQuery` | Bytes de un documento subido |

---

## Cómo añadir un nuevo Command

Pasos concretos para agregar, por ejemplo, `ArchiveProcessCommand`:

**1. Crear el comando** en `onboardingProcesses/application/commands/`

```java
@Getter
@AllArgsConstructor
public class ArchiveProcessCommand implements Command {
    private final String processId;
}
```

**2. Crear el handler** en el mismo paquete

```java
@Component
@RequiredArgsConstructor
public class ArchiveProcessCommandHandler
        implements CommandHandler<ArchiveProcessCommand, Void> {

    private final OnboardingProcRepositoryPort processPort;

    @Override
    @Transactional
    public Void handle(ArchiveProcessCommand command) {
        OnboardingProc process = processPort.findById(command.getProcessId())
                .orElseThrow(() -> new IllegalArgumentException("Proceso no encontrado."));
        process.setStatus(OnboardingStatus.ARCHIVED);
        processPort.save(process);
        return null;
    }
}
```

**3. Exponer el endpoint** en `OnboardingProcCommandController`

```java
@PostMapping("/api/admin/processes/{processId}/archive")
public ResponseEntity<Void> archive(@PathVariable String processId) {
    commandBus.dispatch(new ArchiveProcessCommand(processId));
    return ResponseEntity.noContent().build();
}
```

**No hay paso 4.** Spring detecta `ArchiveProcessCommandHandler` automáticamente al iniciar y lo registra en `SpringCommandBus`. No se modifica ningún archivo de configuración.

---

## Cómo añadir una nueva Query

Pasos para agregar, por ejemplo, `GetProcessesByStatusQuery`:

**1. Crear la query**

```java
@Getter
@AllArgsConstructor
public class GetProcessesByStatusQuery implements Query<List<OnboardingProcDto.Response>> {
    private final OnboardingStatus status;
}
```

**2. Crear el handler**

```java
@Component
@RequiredArgsConstructor
public class GetProcessesByStatusQueryHandler
        implements QueryHandler<GetProcessesByStatusQuery, List<OnboardingProcDto.Response>> {

    private final OnboardingProcRepositoryPort processPort;
    private final OnboardingProcMapper mapper;

    @Override
    public List<OnboardingProcDto.Response> handle(GetProcessesByStatusQuery query) {
        return processPort.findByStatus(query.getStatus()).stream()
                .map(mapper::toResponse)
                .toList();
    }
}
```

**3. Exponer el endpoint** en `OnboardingProcQueryController`

```java
@GetMapping("/api/admin/processes")
public ResponseEntity<List<OnboardingProcDto.Response>> findByStatus(
        @RequestParam OnboardingStatus status) {
    return ResponseEntity.ok(queryBus.ask(new GetProcessesByStatusQuery(status)));
}
```

---

## Ventajas de esta implementación

- **Cero configuración manual:** añadir un handler es suficiente. Spring y `ResolvableType` hacen el registro automáticamente.
- **Bajo acoplamiento:** los controladores solo conocen `CommandBus` y `QueryBus`. Nunca referencian un handler directamente.
- **Testabilidad:** cada handler se puede testear de forma unitaria pasándole el comando directamente, sin necesidad de levantar el bus ni el contexto HTTP.
- **Separación de responsabilidades clara:** los handlers de comando pueden tener `@Transactional` y efectos secundarios; los handlers de query son puros y sin estado.
- **Escalabilidad:** añadir nuevas operaciones no requiere tocar código existente (principio Open/Closed).
