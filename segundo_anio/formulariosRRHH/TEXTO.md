# PROYECTO: FORMULARIOS RRHH - ONBOARDING

Sistema de incorporación (onboarding) de candidatos desarrollado para Civica Software. Permite a los administradores gestionar el proceso completo de incorporación de nuevos empleados, desde el envío del formulario inicial hasta la validación final de documentos.

---

## 1. STACK TECNOLÓGICO

### Backend
- **Framework**: Spring Boot 3.4.5
- **Lenguaje**: Java 21
- **Build**: Maven 3.9+
- **Base de Datos**: MySQL 8+
- **ORM**: Spring Data JPA (Hibernate)
- **Seguridad**: Spring Security + JWT (JJWT 0.12.6)
- **Email**: Spring Mail (Gmail SMTP)
- **Utilidades**: Lombok, Jackson

### Frontend
- **Framework**: Angular 21.2.5
- **Lenguaje**: TypeScript 5.9.2
- **Build Tool**: Angular CLI 21.1.5
- **HTTP**: HttpClient + RxJS 7.8.0
- **Formularios**: Reactive Forms
- **Notificaciones**: ngx-toastr 20.0.5
- **Testing**: Vitest 4.0.8

---

## 2. ARQUITECTURA

### Patrón: Arquitectura Hexagonal (Ports & Adapters)

Tanto el backend como el frontend siguen el mismo patrón arquitectónico. Cada módulo está organizado en tres capas:

```
[Módulo]/
├── domain/          --> Modelos de negocio, puertos (interfaces), value objects
├── application/     --> Servicios (lógica de negocio), DTOs
└── infrastructure/  --> Controladores REST, entidades JPA, adaptadores, mappers
```

### Estructura General del Proyecto
```
formulariosRRHH/
├── onboarding-backend/      (Spring Boot)
├── onboarding-frontend/     (Angular)
├── modeloDatos/             (Diagramas ER y de clases)
├── uploads/                 (Almacenamiento de documentos)
└── CONFIGURATION.md
```

### Backend - Estructura por Módulo
```
src/main/java/com/onboarding/
├── adminUsers/
├── candidates/
├── onboardingProcesses/
├── dataFields/
├── dataAnswers/
├── documentRequirements/
├── uploadedDocuments/
├── fieldOptions/
├── accessTokens/
├── notifications/
└── config/                  (SecurityConfig, CORS)
```

### Frontend - Estructura por Módulo
```
src/app/
├── modules/                 (Un módulo por entidad)
│   ├── [módulo]/
│   │   ├── domain/          (interfaces, repositorios)
│   │   ├── application/     (servicios)
│   │   └── infrastructure/  (componentes, API clients, tokens DI)
├── pages/                   (Componentes de página/vista)
├── core/                    (guards, interceptores, servicios globales)
├── shared/                  (Componentes reutilizables)
└── environments/
```

---

## 3. MÓDULOS Y ENTIDADES DE DOMINIO

El sistema consta de **10 módulos** principales con sus respectivas entidades:

---

### 3.1 AdminUsers (Administradores)
Gestiona los usuarios administradores del sistema.

**Campos**: `id`, `email`, `passwordHash` (BCrypt), `fullName`, `createdAt`

**Particularidades**:
- Autenticación mediante JWT (duración: 8 horas)
- Un `AdminUserSeeder` crea el primer administrador automáticamente al arrancar si no existe
- El hash de contraseña usa BCrypt

---

### 3.2 Candidates (Candidatos)
Almacena los datos básicos de cada candidato.

**Campos**: `id` (UUID), `email`, `firstName`, `lastName`, `createdAt`

**Particularidades**:
- Al crear un candidato se genera automáticamente un `OnboardingProcess` y un `AccessToken` dentro de una **transacción única**
- Se envía email de bienvenida con el link de acceso al formulario

---

### 3.3 OnboardingProcesses (Procesos de Incorporación)
Representa el proceso completo de incorporación de un candidato. Es la entidad central del sistema.

**Campos**: `id` (UUID), `candidateId`, `status`, `createdAt`, `submittedAt`, `validatedAt`, `validatedById`

**Estados del proceso**:
```
PENDING_ACCESS  -->  IN_PROGRESS  -->  SUBMITTED  -->  VALIDATED
                                            |
                                        REJECTED  --> (nuevo token) --> PENDING_ACCESS
```

| Estado | Descripción |
|--------|-------------|
| `PENDING_ACCESS` | Esperando que el candidato acceda al formulario |
| `IN_PROGRESS` | Candidato rellenando el formulario |
| `SUBMITTED` | Formulario enviado, pendiente de revisión del admin |
| `VALIDATED` | Validado correctamente por un administrador |
| `REJECTED` | Rechazado, el candidato debe corregir y reenviar |

---

### 3.4 DataFields (Campos de Formulario)
Define los campos dinámicos que los candidatos deben completar.

**Campos**: `id`, `label`, `fieldType`, `validationRegex`, `minLength`, `maxLength`, `placeholder`, `isRequired`, `displayOrder`

**Tipos de campo**:
| Tipo | Descripción |
|------|-------------|
| `TEXT` | Campo de texto simple |
| `NUMBER` | Campo numérico |
| `TEXTAREA` | Área de texto multilínea |
| `SELECT` | Lista desplegable (con opciones) |
| `RADIO` | Botones de radio (con opciones) |

**Particularidades**:
- Ordenados por `displayOrder`
- Al eliminar un campo, se eliminan en cascada sus opciones y respuestas asociadas

---

### 3.5 DataAnswers (Respuestas)
Almacena las respuestas que un candidato da a cada campo del formulario.

**Campos**: `id`, `onboardingProcessId`, `dataFieldId`, `answerValue` (TEXT), `fieldOptionId`, `createdAt`

**Particularidades**:
- Si ya existe una respuesta para ese campo y proceso, se actualiza (upsert)
- Permite guardar respuestas parciales antes de enviar

---

### 3.6 FieldOptions (Opciones de Campo)
Define las opciones disponibles para campos de tipo `SELECT` o `RADIO`.

**Campos**: `id`, `dataFieldId`, `optionLabel`, `optionValue`, `displayOrder`

---

### 3.7 DocumentRequirements (Requisitos de Documento)
Define qué documentos deben subir los candidatos.

**Campos**: `id`, `name`, `description`, `allowedFormats`, `fileNameRegex`, `isRequired`, `isConditional`, `conditionDescription`, `displayOrder`, `templateFilePath`, `templateOriginalFilename`

**Particularidades**:
- El administrador puede subir una **plantilla descargable** por cada requisito
- Los candidatos pueden descargar la plantilla, completarla y subirla
- Soporta requisitos condicionales (con descripción de la condición)
- Validación de formato de archivo (`allowedFormats`) y nombre (`fileNameRegex`)

---

### 3.8 UploadedDocuments (Documentos Cargados)
Almacena la información de los documentos subidos por los candidatos.

**Campos**: `id`, `onboardingProcessId`, `documentRequirementId`, `filePath`, `originalFilename`, `mimeType`, `fileSizeBytes`, `validationStatus`, `validationNotes`, `uploadedAt`

**Estados de validación**:
| Estado | Descripción |
|--------|-------------|
| `PENDING_VALIDATION` | Pendiente de revisión |
| `APPROVED` | Aprobado por el administrador |
| `REJECTED` | Rechazado, con notas de corrección |

**Particularidades**:
- Tamaño máximo: **20 MB** por archivo
- Almacenamiento en filesystem: `uploads/{processId}/{uuid}_{originalFilename}`
- Plantillas en: `uploads/templates/{uuid}_{originalFilename}`

---

### 3.9 AccessTokens (Tokens de Acceso)
Controla el acceso seguro de candidatos al formulario mediante tokens de un solo uso.

**Campos**: `id`, `onboardingProcessId`, `tokenHash` (SHA-256), `expiresAt`, `usedAt`, `isUsed`, `attempts`, `createdAt`

**Particularidades**:
- El token raw (UUID) se envía por email, **solo el hash SHA-256 se almacena en BD**
- Válido durante **30 días**
- Un token solo puede usarse **una vez**
- Al rechazar un proceso, se genera automáticamente un nuevo token

---

### 3.10 Notifications (Notificaciones)
Registra todos los emails enviados y su estado (para auditoría).

**Campos**: `id`, `onboardingProcessId`, `type`, `recipientEmail`, `sentAt`, `status`, `errorMessage`

**Tipos de notificación**:
| Tipo | Destinatario | Cuándo |
|------|-------------|--------|
| `ACCESS_LINK` | Candidato | Al crear el candidato |
| `FORM_SUBMITTED` | Administrador | Al enviar el formulario |
| `VALIDATED` | Candidato | Al validar el proceso |
| `REJECTED` | Candidato | Al rechazar (incluye nuevo link) |

**Particularidades**:
- Los emails tienen un template HTML profesional con branding de Civica Software
- Los fallos de envío se registran con el mensaje de error

---

## 4. API REST - ENDPOINTS

### Autenticación
| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| `POST` | `/api/admin/auth/login` | No | Login administrador |
| `POST` | `/api/admin/users` | JWT | Crear administrador |
| `GET` | `/api/admin/users` | JWT | Listar administradores |

### Candidatos
| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| `POST` | `/api/admin/candidates` | JWT | Crear candidato |
| `GET` | `/api/admin/candidates` | JWT | Listar candidatos |
| `GET` | `/api/admin/candidates/{id}` | JWT | Obtener candidato |

### Procesos de Incorporación
| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| `GET` | `/api/admin/processes` | JWT | Listar procesos |
| `GET` | `/api/admin/processes/{id}` | JWT | Detalle del proceso |
| `PATCH` | `/api/admin/processes/{id}/validate` | JWT | Validar proceso |
| `PATCH` | `/api/admin/processes/{id}/reject` | JWT | Rechazar proceso |
| `POST` | `/api/admin/processes/{id}/resend-access-link` | JWT | Reenviar enlace |
| `POST` | `/api/candidate/form/{processId}/submit` | Token | Enviar formulario |
| `GET` | `/api/candidate/form/{processId}/status` | Token | Estado del proceso |

### Campos de Formulario
| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| `GET` | `/api/admin/fields` | JWT | Listar campos |
| `POST` | `/api/admin/fields` | JWT | Crear campo |
| `PUT` | `/api/admin/fields/{id}` | JWT | Actualizar campo |
| `DELETE` | `/api/admin/fields/{id}` | JWT | Eliminar campo |
| `GET` | `/api/candidate/form/fields` | Token | Campos para candidato |

### Opciones de Campo
| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| `POST` | `/api/admin/field-options` | JWT | Crear opción |
| `GET` | `/api/admin/field-options/{fieldId}` | JWT | Listar opciones |
| `DELETE` | `/api/admin/field-options/{id}` | JWT | Eliminar opción |
| `GET` | `/api/candidate/form/field-options/{fieldId}` | Token | Opciones candidato |

### Requisitos de Documento
| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| `GET` | `/api/admin/document-requirements` | JWT | Listar requisitos |
| `POST` | `/api/admin/document-requirements` | JWT | Crear requisito |
| `PUT` | `/api/admin/document-requirements/{id}` | JWT | Actualizar |
| `DELETE` | `/api/admin/document-requirements/{id}` | JWT | Eliminar |
| `POST` | `/api/admin/document-requirements/{id}/template` | JWT | Subir plantilla |
| `GET` | `/api/admin/document-requirements/{id}/template/download` | JWT | Descargar plantilla |
| `GET` | `/api/candidate/form/document-requirements` | Token | Requisitos candidato |
| `GET` | `/api/candidate/form/document-requirements/{id}/template/download` | Token | Descargar plantilla |

### Respuestas a Campos
| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| `POST` | `/api/candidate/form/{processId}/answers` | Token | Guardar respuestas |
| `GET` | `/api/candidate/form/{processId}/answers` | Token | Obtener respuestas |
| `GET` | `/api/admin/processes/{processId}/answers` | JWT | Obtener (admin) |

### Documentos Cargados
| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| `POST` | `/api/candidate/form/{processId}/documents/{reqId}/upload` | Token | Subir documento |
| `GET` | `/api/candidate/form/{processId}/documents` | Token | Listar documentos |
| `GET` | `/api/admin/processes/{processId}/documents` | JWT | Listar (admin) |
| `GET` | `/api/admin/documents/{docId}/download` | JWT | Descargar documento |
| `PATCH` | `/api/admin/documents/{docId}/approve` | JWT | Aprobar documento |
| `PATCH` | `/api/admin/documents/{docId}/reject` | JWT | Rechazar documento |

---

## 5. FLUJOS PRINCIPALES

### Flujo 1: Crear Candidato e Iniciar Proceso
```
1. Admin crea candidato (nombre, apellido, email)
2. Backend (transacción atómica):
   a. Crea registro en Candidates
   b. Crea OnboardingProcess (estado: PENDING_ACCESS)
   c. Genera token UUID, calcula hash SHA-256
   d. Guarda AcessToken con el hash
   e. Envía email con link: /onboarding?token={rawToken}
3. Candidato recibe email
```

### Flujo 2: Candidato Completa el Formulario
```
1. Candidato accede al link del email
2. Backend valida el token (hash, expiración, no usado)
3. proceso pasa a IN_PROGRESS
4. Candidato ve:
   a. Campos de formulario (dinámicos por DataFields)
   b. Requisitos de documento (con plantillas descargables)
5. Candidato completa campos y sube documentos
6. Los datos se guardan progresivamente (sin enviar aún)
```

### Flujo 3: Enviar Formulario
```
1. Candidato hace clic en "Enviar"
2. Se guardan respuestas y documentos finales
3. OnboardingProcess.status = SUBMITTED
4. Backend envía email al admin: "Hay un formulario pendiente de revisión"
5. Candidato ve confirmación
```

### Flujo 4: Admin Valida el Proceso
```
1. Admin en /admin/processes, filtra por SUBMITTED
2. Revisa respuestas de campos
3. Revisa documentos subidos (puede aprobar/rechazar individualmente)
4. Hace clic en "Validar"
5. Backend:
   a. OnboardingProcess.status = VALIDATED
   b. Registra validatedAt y validatedById
   c. Envía email al candidato: "Tu proceso fue validado"
```

### Flujo 5: Admin Rechaza el Proceso
```
1. Admin identifica problemas en respuestas o documentos
2. Hace clic en "Rechazar", ingresa notas
3. Backend:
   a. OnboardingProcess.status = REJECTED
   b. Genera nuevo token (anterior invalidado)
   c. Envía email al candidato con:
      - El motivo del rechazo
      - Nuevo link para corregir
4. Candidato puede corregir y reenviar (los datos anteriores se mantienen)
```

### Flujo 6: Login de Administrador
```
1. Admin accede a /login
2. Ingresa email + contraseña
3. Backend valida credenciales (BCrypt)
4. Retorna JWT (válido 8 horas)
5. Frontend almacena en localStorage
6. Redirige a /admin
7. Todas las peticiones siguientes incluyen el JWT en el header Authorization
```

---

## 6. SEGURIDAD

### Autenticación JWT (Administradores)
- **Algoritmo**: HMAC-SHA256
- **Duración**: 8 horas (28800000 ms)
- **Header**: `Authorization: Bearer {token}`
- **Rutas protegidas**: Todas bajo `/api/admin/**` excepto el login

### Acceso de Candidatos (Access Tokens)
- Token enviado por email (UUID raw)
- Solo se almacena el **hash SHA-256** en base de datos
- El token se incluye como parámetro en la URL: `?token={rawToken}`
- Válido 30 días, un solo uso

### CORS
- Orígenes permitidos: `http://localhost:4200` (desarrollo)

### Contraseñas de Administradores
- Hash BCrypt con salt automático

### Interceptores Frontend
| Interceptor | Función |
|------------|---------|
| `AuthInterceptor` | Agrega JWT a todas las peticiones |
| `ErrorInterceptor` | Maneja 401 → redirige a login |
| `SuccessInterceptor` | Muestra toast de éxito |

### Guards de Rutas
| Guard | Protege | Criterio |
|-------|---------|---------|
| `authGuard` | Rutas admin | JWT válido en localStorage |
| `candidateGuard` | Ruta onboarding | Parámetro `token` en URL |

---

## 7. CONFIGURACIÓN

### Variables de Entorno (Backend)
Las variables sensibles se cargan desde `application-secret.properties` (no versionado):

```
DB_PASSWORD          - Contraseña MySQL
JWT_SECRET           - Clave secreta JWT (mín. 32 caracteres)
MAIL_USERNAME        - Email de Gmail para envío
MAIL_PASSWORD        - App Password de Gmail
ADMIN_EMAIL          - Email del admin inicial (seeder)
ADMIN_PASSWORD       - Contraseña del admin inicial
ADMIN_FULLNAME       - Nombre del admin inicial
```

### Configuraciones Relevantes
| Parámetro | Valor | Descripción |
|-----------|-------|-------------|
| Puerto BD | 3307 | MySQL en puerto no estándar |
| `ddl-auto` | `none` | No se auto-crea/modifica el esquema |
| Tamaño máx. archivo | 20 MB | Límite de documentos subidos |
| JWT duración | 8 horas | Expiración de sesión admin |
| Token candidato | 30 días | Expiración del link de acceso |
| Almacenamiento | `./uploads` | Carpeta local (configurable) |
| Puerto backend | 8080 | API REST |
| Puerto frontend | 4200 | App Angular |

---

## 8. BASE DE DATOS

### Tablas Principales
| Tabla | Entidad |
|-------|---------|
| `admin_users` | Administradores |
| `candidates` | Candidatos |
| `onboarding_processes` | Procesos de incorporación |
| `data_fields` | Campos del formulario |
| `data_answers` | Respuestas de candidatos |
| `field_options` | Opciones de campos SELECT/RADIO |
| `document_requirements` | Requisitos de documentos |
| `uploaded_documents` | Documentos subidos |
| `access_tokens` | Tokens de acceso |
| `notifications` | Log de emails enviados |

### Notas sobre el Esquema
- El esquema **no se genera automáticamente** (`ddl-auto: none`), debe existir en MySQL previamente
- Los IDs de candidatos y procesos son **UUID** (String), no autoincrementales
- Los IDs de campos, opciones, documentos y tokens son **Long** autoincrementales
- Las relaciones usan Foreign Keys explícitas

---

## 9. DECISIONES DE DISEÑO RELEVANTES

| Decisión | Justificación |
|----------|---------------|
| **Arquitectura Hexagonal** | Separación clara entre lógica de negocio e infraestructura. Permite cambiar la BD, el framework HTTP o el sistema de emails sin tocar el dominio. |
| **UUID para candidatos y procesos** | Evita la exposición de secuencias predecibles en URLs públicas. Más seguro para procesos accesibles por candidatos sin autenticación completa. |
| **Token SHA-256 en BD** | Nunca se almacena el token en claro. Si la BD fuese comprometida, los tokens no serían utilizables directamente. |
| **Formulario dinámico** | Los campos del formulario son configurables desde la administración sin cambiar el código. Permite adaptar el proceso a diferentes perfiles de contratación. |
| **Documentos en filesystem** | Simplicidad y rendimiento para archivos. Solo los metadatos se almacenan en BD. |
| **Notificaciones como auditoría** | Cada email enviado queda registrado con su estado. Permite saber exactamente qué comunicaciones se han producido y detectar fallos. |
| **ddl-auto: none** | Control explícito sobre el esquema de BD. Evita migraciones no intencionadas en producción. |
| **Transacción al crear candidato** | Garantiza consistencia: si falla el email o el token, no se crea el candidato. |
| **Estados del proceso como enum** | Tipado fuerte, imposible insertar estados inválidos. Ciclo de vida del proceso claro y documentado en código. |
| **Campos condicionales en documentos** | Permite documentos que solo aplican a algunos candidatos según su situación (ej: contrato de trabajo actual). |

---

## 10. RESUMEN EJECUTIVO

**Sistema de Onboarding** desarrollado para Civica Software que digitaliza y gestiona el proceso de incorporación de nuevos empleados.

**Qué hace el sistema**:
1. El administrador crea un candidato → el sistema genera automáticamente el proceso y envía el link de acceso por email
2. El candidato accede mediante un link seguro y rellena un formulario dinámico + sube documentos requeridos
3. El administrador revisa, valida o rechaza el proceso (con notificaciones automáticas por email en cada paso)
4. Si es rechazado, el candidato recibe un nuevo link para corregir sus datos

**Tecnologías clave**: Spring Boot 3 + Angular 21 + MySQL + JWT + Gmail SMTP

**Patrón arquitectónico**: Arquitectura Hexagonal aplicada tanto en backend como en frontend, con 10 módulos bien delimitados.
