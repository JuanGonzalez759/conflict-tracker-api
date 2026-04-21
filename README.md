## Descripción del Proyecto

Conflict Tracker es una aplicación backend que proporciona una API REST completa para gestionar información sobre conflictos bélicos. Implementa arquitectura por capas con Spring Boot, JPA/Hibernate y base de datos PostgreSQL (Supabase en producción).

## 🚀 Arquitectura de Despliegue

```
┌─────────────────────────────────────────────────────┐
│            Frontend Layer (Vue 3 + Vite)            │
│  Vercel/Netlify: https://<your-frontend>.vercel.app│
└──────────────────┬──────────────────────────────────┘
                   │ VITE_API_URL
                   │ (CORS allowed)
                   ↓
┌─────────────────────────────────────────────────────┐
│       Backend Layer (Spring Boot + Java 17)         │
│    Railway/Render/Fly.io: https://<your-api>       │
│         /api/v1 (REST API)                          │
└──────────────────┬──────────────────────────────────┘
                   │ JDBC PostgreSQL
                   ↓
┌─────────────────────────────────────────────────────┐
│   Persistence Layer (PostgreSQL via Supabase)       │
│  https://cnblimsqcyvurigapfpc.supabase.co          │
└─────────────────────────────────────────────────────┘
```

## Características Principales

- ✅ API REST completa con operaciones CRUD
- ✅ Modelo de datos relacional con JPA/Hibernate
- ✅ Arquitectura por capas (Controller - Service - Repository)
- ✅ DTOs para desacoplar el modelo de datos de la API
- ✅ Gestión de relaciones Many-to-Many y One-to-Many
- ✅ Validación de datos con Bean Validation
- ✅ Manejo global de excepciones
- ✅ Base de datos H2 en memoria (desarrollo)
- ✅ Soporte para PostgreSQL (producción)
- ✅ **Frontend web con Thymeleaf** (Interfaz clásica sin JS)
  - Listado de conflictos con tabla interactiva
  - Formulario de creación con validación
  - Vista detallada de conflictos
  - Diseño responsivo con Bootstrap 5
  - Navegación completa por la aplicación

## Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (desarrollo)
- **PostgreSQL** (producción)
- **Maven** (gestión de dependencias)
- **Lombok** (reducción de código boilerplate)
- **Thymeleaf** (motor de templates HTML)
- **Bootstrap 5** (framework CSS para frontend)

## Modelo de Datos

### Entidades

1. **Conflict** (Conflicto)
   - `id`: Long
   - `name`: String
   - `startDate`: LocalDate
   - `status`: Enum (ACTIVE, FROZEN, ENDED)
   - `description`: String
   - Relaciones: ManyToMany con Country, OneToMany con Faction y Event

2. **Faction** (Facción)
   - `id`: Long
   - `name`: String
   - Relaciones: ManyToOne con Conflict, ManyToMany con Country

3. **Country** (País)
   - `id`: Long
   - `name`: String
   - `code`: String

4. **Event** (Evento)
   - `id`: Long
   - `eventDate`: LocalDate
   - `location`: String
   - `description`: String
   - Relaciones: ManyToOne con Conflict

## Instalación y Ejecución

### Prerrequisitos

- Java 17 o superior
- Maven 3.6 o superior

### Pasos para Ejecutar

1. **Clonar el repositorio**
   ```bash
   git clone <url-del-repositorio>
   cd conflict-tracker
   ```

2. **Compilar el proyecto**
   ```bash
   mvn clean install
   ```

3. **Ejecutar la aplicación**
   ```bash
   mvn spring-boot:run
   ```

4. **Acceder a la aplicación**
   - API: http://localhost:8080/api/v1
   - Frontend: http://localhost:8080
   - Consola H2: http://localhost:8080/h2-console
     - JDBC URL: `jdbc:h2:mem:conflictdb`
     - Usuario: `sa`
     - Contraseña: (dejar en blanco)
## Desplegament en el núvol
Per desplegar el backend en un contenidor amb PostgreSQL al núvol, utilitza variables d'entorn externes.

### Variables d'entorn importants
- `DB_URL`: URL de connexió a PostgreSQL, per exemple `jdbc:postgresql://host:port/dbname`
- `DB_USERNAME`: usuari de la base de dades
- `DB_PASSWORD`: contrasenya de la base de dades
- `DB_DRIVER`: `org.postgresql.Driver` (opcional, per defecte H2)
- `HIBERNATE_DDL_AUTO`: `update` o `validate` en producció
- `FRONTEND_URL`: URL pública del frontend per a CORS, per exemple `https://<your-frontend-url>`
- `SERVER_PORT`: port on escolta Spring Boot (opcjonal)

### CORS segur
El backend utilitza un configurador global de CORS (`WebConfig`) que only permet orígens des de `FRONTEND_URL`.

### Construir i executar en producció
```bash
mvn clean package
java -jar target/conflict-tracker-api-1.0.0.jar
```
## Frontend Web - Thymeleaf

Se ha implementado una interfaz web clásica usando **Thymeleaf** que permite interactuar con la aplicación de forma visual.

### Rutas del Frontend

| Ruta | Descripción |
|------|-------------|
| `/web/conflicts` | Listado de conflictos (tabla interactiva) |
| `/web/conflicts/new` | Formulario para crear nuevo conflicto |
| `/web/conflicts/{id}` | Vista detallada de un conflicto |

### Características del Frontend

- 📋 **Listado**: Tabla con todos los conflictos, nombre, fecha, estado y descripción
- ➕ **Crear**: Formulario con validación para agregar nuevos conflictos
- 📝 **Detalles**: Vista completa con información del conflicto y países involucrados
- ✔️ **Validación**: Errores mostrados en interfaz, redirecciones tras éxito
- 🎨 **Diseño**: Bootstrap 5 con CSS personalizado, responsive y accesible
- 🔗 **Navegación**: Enlaces entre vistas y acceso a API REST

Para más detalles sobre la implementación del frontend, consulta [FRONTEND.md](FRONTEND.md).

## Endpoints de la API

### Conflicts

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/conflicts` | Obtener todos los conflictos |
| GET | `/api/v1/conflicts?status=ACTIVE` | Filtrar conflictos por estado |
| GET | `/api/v1/conflicts/{id}` | Obtener un conflicto por ID |
| POST | `/api/v1/conflicts` | Crear un nuevo conflicto |
| PUT | `/api/v1/conflicts/{id}` | Actualizar un conflicto |
| DELETE | `/api/v1/conflicts/{id}` | Eliminar un conflicto |

### Factions

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/factions` | Obtener todas las facciones |
| GET | `/api/v1/factions/{id}` | Obtener una facción por ID |
| POST | `/api/v1/factions` | Crear una nueva facción |
| PUT | `/api/v1/factions/{id}` | Actualizar una facción |
| DELETE | `/api/v1/factions/{id}` | Eliminar una facción |

### Events

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/events` | Obtener todos los eventos |
| GET | `/api/v1/events/{id}` | Obtener un evento por ID |
| POST | `/api/v1/events` | Crear un nuevo evento |
| PUT | `/api/v1/events/{id}` | Actualizar un evento |
| DELETE | `/api/v1/events/{id}` | Eliminar un evento |

### Countries

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/countries/{code}/conflicts` | Obtener conflictos por código de país |

## Ejemplos de Uso

### Obtener todos los conflictos

```bash
curl -X GET http://localhost:8080/api/v1/conflicts
```

### Filtrar conflictos activos

```bash
curl -X GET "http://localhost:8080/api/v1/conflicts?status=ACTIVE"
```

### Obtener un conflicto específico

```bash
curl -X GET http://localhost:8080/api/v1/conflicts/1
```

### Crear un nuevo conflicto

```bash
curl -X POST http://localhost:8080/api/v1/conflicts \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Nuevo Conflicto",
    "startDate": "2024-01-01",
    "status": "ACTIVE",
    "description": "Descripción del conflicto",
    "countries": []
  }'
```

### Actualizar un conflicto

```bash
curl -X PUT http://localhost:8080/api/v1/conflicts/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Conflicto Actualizado",
    "startDate": "2024-01-01",
    "status": "FROZEN",
    "description": "Nueva descripción",
    "countries": []
  }'
```

### Eliminar un conflicto

```bash
curl -X DELETE http://localhost:8080/api/v1/conflicts/1
```

### Obtener conflictos de un país

```bash
curl -X GET http://localhost:8080/api/v1/countries/UKR/conflicts
```

### Crear una facción

```bash
curl -X POST http://localhost:8080/api/v1/factions \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Nueva Facción",
    "conflictId": 1,
    "supportingCountries": []
  }'
```

### Crear un evento

```bash
curl -X POST http://localhost:8080/api/v1/events \
  -H "Content-Type: application/json" \
  -d '{
    "eventDate": "2024-01-15",
    "location": "Ciudad Ejemplo",
    "description": "Descripción del evento",
    "conflictId": 1
  }'
```

## Configuración de PostgreSQL

Para usar PostgreSQL en lugar de H2:

1. **Crear la base de datos**
   ```sql
   CREATE DATABASE conflictdb;
   ```

2. **Modificar application.properties**
   
   Comentar la configuración de H2 y descomentar la de PostgreSQL:
   
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/conflictdb
   spring.datasource.username=postgres
   spring.datasource.password=yourpassword
   spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
   spring.jpa.hibernate.ddl-auto=update
   spring.h2.console.enabled=false
   ```

## Arquitectura del Proyecto

```
src/main/java/com/conflicttracker/
├── controller/          # Controladores REST
├── service/            # Lógica de negocio
├── repository/         # Repositorios JPA
├── model/              # Entidades JPA
├── dto/                # Data Transfer Objects
├── mapper/             # Conversores Entity <-> DTO
├── exception/          # Manejo de excepciones
└── ConflictTrackerApiApplication.java
```

## Datos de Prueba

La aplicación incluye datos de prueba que se cargan automáticamente al iniciar:

- 10 países
- 4 conflictos principales (Rusia-Ucrania, Israel-Palestina, Siria, Yemen)
- 8 facciones
- 9 eventos históricos

## Estructura de DTOs

Los DTOs implementados garantizan el desacoplamiento entre el modelo de datos y la API:

- `ConflictDTO`: Para operaciones completas con conflictos
- `ConflictSummaryDTO`: Para listados resumidos
- `FactionDTO`: Para gestión de facciones
- `EventDTO`: Para eventos
- `CountryDTO`: Para países

## Validaciones

Se aplican validaciones mediante anotaciones de Bean Validation:

- `@NotBlank`: Campos de texto requeridos
- `@NotNull`: Campos obligatorios
- Mensajes de error personalizados

## Manejo de Errores

La aplicación incluye un manejador global de excepciones que devuelve respuestas JSON estructuradas:

- `ResourceNotFoundException`: Recurso no encontrado (404)
- `MethodArgumentNotValidException`: Error de validación (400)
- Excepciones generales (500)