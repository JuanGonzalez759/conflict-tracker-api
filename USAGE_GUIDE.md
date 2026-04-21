# Guía de Uso - Conflict Tracker API con Frontend

## Inicio Rápido

### Requisitos Previos
- Java 17+ (actualmente configurado para Java 17 Temurin)
- Maven 3.8+

### Ejecutar la Aplicación

**En PowerShell:**

```powershell
# Configurar variables de entorno
$env:JAVA_HOME = "C:\jdk17\jdk-17.0.8+7"
$env:PATH = "C:\jdk17\jdk-17.0.8+7\bin;C:\maven\bin;$env:PATH"

# Compilar y ejecutar
cd c:\fullstack\conflict-tracker-api-main
mvn clean spring-boot:run
```

La aplicación se iniciará en **http://localhost:8080**

### URLs Disponibles

#### Frontend (Thymeleaf)
- **Listado**: http://localhost:8080/web/conflicts
- **Nuevo Conflicto**: http://localhost:8080/web/conflicts/new
- **Detalles**: http://localhost:8080/web/conflicts/{id}

#### 📡 API REST
- **Todos los conflictos**: http://localhost:8080/api/v1/conflicts
- **Un conflicto**: http://localhost:8080/api/v1/conflicts/{id}

## 📋 Funcionalidades Implementadas

### 7.1 Obligatorio: Listado Web ✅

**Vista de Listado** (`/web/conflicts`)
```
┌─────────────────────────────────────────┐
│ Conflictos Bélicos Mundiales         │
│ Total: X conflictos                     │
├─────────────────────────────────────────┤
│ Nombre  │ Fecha  │ Estado │ Descripción │
├─────────────────────────────────────────┤
│ ...     │ ...    │ ...    │ ...         │
└─────────────────────────────────────────┘
```

**Datos mostrados:**
- Nombre: Identificación del conflicto
- Fecha de Inicio: En formato dd/MM/yyyy
- Estado: Con indicadores visuales (🟢 🟡 🔴)
- Descripción: Extracto de 60 caracteres

### 7.2 Opcional: Gestión Completa ✅

#### 🎯 Crear Conflicto
- Formulario en `/web/conflicts/new`
- Campos validados:
  - Nombre (Obligatorio)
  - Fecha (Obligatorio, date picker)
  - Estado (Obligatorio, select con 3 opciones)
  - Descripción (Opcional, textarea)

#### ✔️ Validación
- **Server-side**: Anotaciones `@NotBlank`, `@NotNull`
- **Client-side**: HTML5 validation
- **Mensajes**: Errores específicos por campo
- **Éxito**: Redirección con mensaje flash

#### 🔗 Navegación
- Botones "Volver al listado" en toda la aplicación
- Botón "+ Nuevo Conflicto" en el listado
- Botones "Cancelar" en formularios
- Enlaces a API REST para exploración

#### 🎨 Diseño y UX
- Bootstrap 5 integrado
- CSS personalizado con:
  - Degradados en formularios
  - Efectos hover en tablas
  - Badges para estados
  - Iconos emoji descriptivos
  - Responsive para móviles

## 🔄 Flujo de Uso

### Listado
1. Accede a http://localhost:8080/web/conflicts
2. Ves tabla con todos los conflictos
3. Puedes:
   - Ver detalles: Click en botón "Ver"
   - Crear nuevo: Click en "+ Nuevo Conflicto"
   - Consultar API: Click en "API REST"

### Crear Conflicto
1. Desde listado: Click "+ Nuevo Conflicto"
2. O directo: http://localhost:8080/web/conflicts/new
3. Completa el formulario:
   - Nombre: (ej) "Guerra en Oriente Próximo"
   - Fecha: Selecciona date picker
   - Estado: 🟢 Activo / 🟡 Pausado / 🔴 Finalizado
   - Descripción: Información adicional (opcional)
4. Click "Crear Conflicto" o "Cancelar"
5. Si es exitoso: Redirecciona al listado con mensaje ✓

### Ver Detalles
1. En listado: Click botón "Ver" de una fila
2. Ve toda la información del conflicto
3. Puedes:
   - Volver al listado
   - Ver en API REST

## 🛠️ Estructura del Proyecto

```
src/main/
├── java/com/conflicttracker/
│   ├── web/
│   │   └── WebConflictController.java    ← Controlador web
│   ├── controller/
│   │   ├── ConflictController.java       ← API REST
│   │   ├── CountryController.java
│   │   ├── EventController.java
│   │   └── FactionController.java
│   ├── service/
│   │   ├── ConflictService.java
│   │   ├── EventService.java
│   │   └── FactionService.java
│   ├── dto/
│   ├── model/
│   ├── repository/
│   └── mapper/
└── resources/
    ├── templates/
    │   └── conflicts/
    │       ├── list.html                 ← Listado
    │       ├── create.html               ← Formulario
    │       └── details.html              ← Detalles
    ├── static/
    │   └── index.html
    └── application.properties
```

## 🗄️ Base de Datos

**Ambiente Desarrollo**: H2 en memoria
- URL: http://localhost:8080/h2-console
- URL JDBC: `jdbc:h2:mem:testdb`
- Usuario: `sa`
- Contraseña: (vacía)

**Datos iniciales** (`data.sql`):
- Conflictos de ejemplo (cargados automáticamente)
- Países participantes
- Eventos históricos

## 🔧 Configuración

### application.properties
```properties
# Servidor
server.port=8080

# Base de datos H2
spring.h2.console.enabled=true
spring.datasource.url=jdbc:h2:mem:testdb

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect

# Thymeleaf
spring.thymeleaf.cache=false
```

## 🧪 Ejemplo de Uso

### 1. Crear conflicto vía Web
```
GET  /web/conflicts/new
→ Form: name="Guerra en Siria"
→        startDate="2011-03-15"
→        status="ACTIVE"
→        description="Conflicto civil en Siria..."
POST /web/conflicts
→ Redirect: /web/conflicts?message=Creado exitosamente
```

### 2. Consultar vía API
```
curl http://localhost:8080/api/v1/conflicts
→ JSON: [
    {
      "id": 1,
      "name": "Guerra en Siria",
      "startDate": "2011-03-15",
      "status": "ACTIVE",
      "description": "...",
      "countries": [...]
    }
  ]
```

## ⚠️ Mensajes de Error Comunes

| Error | Causa | Solución |
|-------|-------|----------|
| Puerto 8080 ocupado | Otra app usa ese puerto | Cambiar en `application.properties` |
| Thymeleaf template not found | Archivo en ubicación errónea | Verificar en `templates/` |
| Validación falla | Campos obligatorios vacíos | Completar todos campos requeridos |
| Base de datos vacía | Scripts no ejecutados | Ejecutar `data.sql` manualmente |

## 📚 Referencias

- [Spring Boot Thymeleaf](https://spring.io/guides/gs/serving-web-content/)
- [Bootstrap 5 Docs](https://getbootstrap.com/docs/)
- [Thymeleaf Manual](https://www.thymeleaf.org/documentation.html)
- [Jakarta Validation](https://jakarta.ee/specifications/bean-validation/)

---

**Desarrollado**: Febrero 2026
**Versión**: 1.0.0
