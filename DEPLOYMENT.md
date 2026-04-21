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
- ✅ CORS dinámico basado en variable `FRONTEND_URL`
- ✅ Validación de datos con Bean Validation
- ✅ Manejo global de excepciones
- ✅ Base de datos H2 en memoria (desarrollo)
- ✅ Soporte para PostgreSQL vía variables de entorno

## Tecnologías

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **PostgreSQL** (producción)
- **H2 Database** (desarrollo local)
- **Maven**
- **Supabase** (hosting PostgreSQL)

## Instalación Local

### Prerrequisitos

- Java 17+
- Maven 3.6+

### Ejecución en Desarrollo (H2)

```bash
git clone <url-del-repositorio>
cd conflict-tracker-api-main
mvn clean install
mvn spring-boot:run
```

Accede a:
- API: http://localhost:8080/api/v1
- Consola H2: http://localhost:8080/h2-console

### Ejecución en Desarrollo con Frontend Vue

Asegúrate que el frontend Vue está en `http://localhost:5173`:

```bash
FRONTEND_URL=http://localhost:5173 mvn spring-boot:run
```

## 📋 Cambios Realizados para Despliegue

### 1. Configuración de Variables de Entorno (`application.yml`)

**Error original:**
- La aplicación estaba hardcodeada con H2 en memoria
- No había soporte para PostgreSQL externo
- CORS abierto con `origins = "*"`

**Solución:**
- Migración a `application.yml` con soporte para múltiples bases de datos
- Variables de entorno para:
  - `DB_URL` (JDBC URL de la base de datos)
  - `DB_USERNAME` (usuario)
  - `DB_PASSWORD` (contraseña)
  - `DB_DRIVER` (driver: H2 por defecto, PostgreSQL en producción)
  - `HIBERNATE_DDL_AUTO` (create-drop en dev, update en prod)
  - `FRONTEND_URL` (para CORS seguro)

```yaml
spring:
  datasource:
    url: ${DB_URL:${DATABASE_URL:jdbc:h2:mem:conflictdb}}
    driver-class-name: ${DB_DRIVER:org.h2.Driver}
    username: ${DB_USERNAME:sa}
    password: ${DB_PASSWORD:}
  jpa:
    database-platform: ${HIBERNATE_DIALECT:org.hibernate.dialect.H2Dialect}
    hibernate:
      ddl-auto: ${HIBERNATE_DDL_AUTO:create-drop}

frontend:
  url: ${FRONTEND_URL:http://localhost:5173}
```

### 2. CORS Seguro Global (`WebConfig.java`)

**Error original:**
```java
@CrossOrigin(origins = "*")
```
- Aceptaba peticiones desde cualquier origen (inseguro en producción)

**Solución:**
- Crear clase `WebConfig.java` que implementa `WebMvcConfigurer`
- Leer `frontend.url` desde variables de entorno
- Aplicar CORS dinámicamente a todas las rutas `/api/**`

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(frontendUrl)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

### 3. Eliminación de `@CrossOrigin` en Controladores

**Cambio:**
- Antes: `@CrossOrigin(origins = "*")` en cada controlador
- Ahora: Removido, usando configuración global

Archivos modificados:
- `ConflictController.java`
- `FactionController.java`
- `EventController.java`
- `CountryController.java`

## 🌐 Despliegue en Producción

### Paso 1: Crear PostgreSQL en Supabase

1. Ve a [Supabase](https://app.supabase.com)
2. Crea un proyecto o usa uno existente
3. Ve a **Settings > Database**
4. Copia las credenciales:
   - Host
   - Port (5432)
   - Database
   - User
   - Password

### Paso 2: Migrar Tablas a PostgreSQL

1. Accede a **SQL Editor** en Supabase
2. Ejecuta este SQL para crear las tablas:

```sql
CREATE TABLE public.countries (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(10) NOT NULL
);

CREATE TABLE public.conflicts (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    description TEXT
);

CREATE TABLE public.conflict_country (
    conflict_id SERIAL REFERENCES conflicts(id),
    country_id SERIAL REFERENCES countries(id),
    PRIMARY KEY (conflict_id, country_id)
);

CREATE TABLE public.factions (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    conflict_id SERIAL REFERENCES conflicts(id)
);

CREATE TABLE public.faction_country (
    faction_id SERIAL REFERENCES factions(id),
    country_id SERIAL REFERENCES countries(id),
    PRIMARY KEY (faction_id, country_id)
);

CREATE TABLE public.events (
    id SERIAL PRIMARY KEY,
    event_date DATE NOT NULL,
    location VARCHAR(255),
    description TEXT,
    conflict_id SERIAL REFERENCES conflicts(id)
);
```

### Paso 3: Desplegar Backend (Railway)

1. Ve a [Railway](https://railway.app)
2. Conecta tu GitHub
3. Crea nuevo proyecto
4. Configura variables de entorno:
   ```
   DB_URL=jdbc:postgresql://<SUPABASE_HOST>:5432/<DB>?ssl=require
   DB_USERNAME=<SUPABASE_USER>
   DB_PASSWORD=<SUPABASE_PASSWORD>
   DB_DRIVER=org.postgresql.Driver
   HIBERNATE_DDL_AUTO=update
   HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect
   FRONTEND_URL=https://<your-frontend>.vercel.app
   ```
5. Deploy

### Paso 4: Desplegar Frontend (Vercel)

1. Ve a [Vercel](https://vercel.com)
2. Importa tu repositorio Vue
3. Configura variable de entorno:
   ```
   VITE_API_URL=https://<your-backend-railway>.railway.app/api/v1
   ```
4. Deploy

### Paso 5: Verificar Despliegue

```bash
# Test backend
curl https://<your-backend>/api/v1/conflicts

# Test frontend (abre en navegador)
https://<your-frontend>.vercel.app
```

## 📊 Endpoints de la API

### Conflicts
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/conflicts` | Obtener todos |
| GET | `/api/v1/conflicts/{id}` | Obtener por ID |
| POST | `/api/v1/conflicts` | Crear nuevo |
| PUT | `/api/v1/conflicts/{id}` | Actualizar |
| DELETE | `/api/v1/conflicts/{id}` | Eliminar |

### Factions
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/factions` | Obtener todas |
| POST | `/api/v1/factions` | Crear nueva |
| PUT | `/api/v1/factions/{id}` | Actualizar |
| DELETE | `/api/v1/factions/{id}` | Eliminar |

## 🔧 Troubleshooting

### Error: "CORS policy: No 'Access-Control-Allow-Origin'"
- Asegúrate que `FRONTEND_URL` es correcto
- Reinicia el backend después de cambiar variables

### Error: "Could not find the table"
- Ejecuta el SQL de creación de tablas en Supabase
- Comprueba `HIBERNATE_DDL_AUTO=update`

### Error: "Connection refused"
- Verifica que `DB_URL` es accesible
- Comprueba credenciales PostgreSQL

## 📚 Documentación Adicional

Ver [Frontend Vue README](../Conflict-Tracker.JuanGonzalez-main/README.md) para instrucciones del cliente.
