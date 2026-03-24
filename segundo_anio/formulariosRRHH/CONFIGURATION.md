# Guía de configuración del proyecto

## Requisitos previos

- Java 17+
- Node.js 18+
- MySQL 8+
- Maven 3.9+

---

## Backend (`onboarding-backend`)

### 1. Crear el archivo de secretos

Crea el archivo `onboarding-backend/src/main/resources/application-secret.properties`.
Este archivo **no se sube al repositorio** (está en `.gitignore`).

```properties
# Base de datos
DB_PASSWORD=tu_password_de_mysql

# JWT
JWT_SECRET=una_clave_secreta_larga_y_aleatoria_minimo_32_caracteres

# Correo (Gmail)
MAIL_USERNAME=tu_correo@gmail.com
MAIL_PASSWORD=tu_app_password_de_gmail

# Usuario administrador inicial
ADMIN_EMAIL=tu@email.com
ADMIN_PASSWORD=tu_password_segura
ADMIN_FULLNAME=Tu Nombre Completo
```

> **Nota sobre `MAIL_PASSWORD`:** Gmail requiere una *contraseña de aplicación*, no tu contraseña normal.
> Puedes generarla en: Cuenta de Google → Seguridad → Verificación en dos pasos → Contraseñas de aplicación.

### 2. Variables opcionales (con valores por defecto)

Estas variables ya tienen valores por defecto en `application.yaml` y solo necesitas definirlas si quieres cambiarlas:

| Variable | Por defecto | Descripción |
|---|---|---|
| `DB_HOST` | `localhost` | Host de MySQL |
| `DB_PORT` | `3307` | Puerto de MySQL |
| `DB_NAME` | `forms_db` | Nombre de la base de datos |
| `DB_USERNAME` | `root` | Usuario de MySQL |
| `JWT_EXPIRATION` | `28800000` | Expiración del JWT en ms (8 horas) |
| `FRONTEND_URL` | `http://localhost:4200` | URL del frontend |
| `STORAGE_PATH` | `./uploads` | Carpeta donde se guardan los archivos subidos |
| `ADMIN_FULLNAME` | `Administrador` | Nombre del usuario admin inicial |

### 3. Crear la base de datos

```sql
CREATE DATABASE forms_db;
```

### 4. Ejecutar el backend

```bash
cd onboarding-backend
./mvnw spring-boot:run
```

Al arrancar por primera vez, el sistema crea automáticamente el usuario administrador con las credenciales definidas en `application-secret.properties`. Si el usuario ya existe, se omite la creación.

---

## Frontend (`onboarding-frontend`)

### 1. Instalar dependencias

```bash
cd onboarding-frontend
npm install
```

### 2. Configurar la URL del backend

El archivo `src/environments/environment.ts` apunta por defecto a `http://localhost:8080`.
Si tu backend corre en otro puerto, edita ese archivo.

### 3. Ejecutar el frontend

```bash
npm start
```

La aplicación estará disponible en `http://localhost:4200`.

---

## Resumen de arranque rápido

```
1. Crear onboarding-backend/src/main/resources/application-secret.properties
2. Crear la base de datos MySQL: CREATE DATABASE forms_db;
3. cd onboarding-backend && ./mvnw spring-boot:run
4. cd onboarding-frontend && npm install && npm start
5. Abrir http://localhost:4200 e iniciar sesión con las credenciales de ADMIN_EMAIL / ADMIN_PASSWORD
```
