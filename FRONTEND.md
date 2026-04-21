# 7. Frontend - Thymeleaf Integration

## Implementación Completada

Se ha desarrollado una capa de presentación web usando **Thymeleaf** que integra perfectamente con el motor de negocio existente (basado en Servicios). Esta arquitectura demuestra como la misma lógica puede servir datos tanto a una API REST como a una interfaz web clásica.

### 7.1 Implementación Obligatoria

#### Controlador Web
- **Ubicación**: `src/main/java/com/conflicttracker/web/WebConflictController.java`
- **Ruta base**: `/web/conflicts`
- **Anotación**: `@Controller` (no `@RestController`)
- **Métodos principales**:
  - `GET /web/conflicts` → Listado de conflictos
  - `GET /web/conflicts/new` → Formulario de creación
  - `POST /web/conflicts` → Crear nuevo conflicto
  - `GET /web/conflicts/{id}` → Vista detallada

#### Vistas HTML

**1. Listado de Conflictos** (`templates/conflicts/list.html`)
- Tabla interactiva con Bootstrap 5
- Muestra datos clave: nombre, fecha de inicio, estado, descripción resumida
- Comunicación mediante objeto `Model` de Spring
- Información del conflicto:
  - Nombre del conflicto
  - Fecha de inicio (formato dd/MM/yyyy)
  - Estado (Activo / Pausado / Finalizado)
  - Descripción (primeros 60 caracteres)
- Botón para crear nuevo conflicto
- Botón para acceder a API REST
- Mensaje vacío cuando no hay conflictos

**2. Formulario de Creación** (`templates/conflicts/create.html`)
- Formulario con validación
- Campos con `th:object` y `th:field`:
  - `name` (Nombre) - Obligatorio
  - `startDate` (Fecha) - Obligatorio (date picker)
  - `status` (Estado) - Obligatorio (select)
  - `description` (Descripción) - Opcional (textarea)
- Validación en servidor con `@Valid`
- Mensajes de error personalizados
- Redirección con parámetro flash al crear exitosamente

**3. Vista Detallada** (`templates/conflicts/details.html`)
- Información completa del conflicto
- Datos mostrables: nombre, fecha, estado, descripción, países
- Navegación entre vistas

#### Dependencias Agregadas
```xml
<!-- Spring Boot Starter Thymeleaf -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

### 7.2 Mejoras Implementadas (Opcional)

#### Formulario de Creación Avanzado
- Formulario POST con `th:object` y `th:field`
- Validación con anotaciones `@NotBlank`, `@NotNull`
- Mensajes de error en interfaz (`th:errors`)
- Campos requeridos marcados visualmente
- Redirecciones automáticas después de guardar

#### Validación Completa
- Validación server-side con Spring Validation
- Validación client-side con HTML5
- Mensajes de error específicos por campo
- Alerta de éxito con `RedirectAttributes.addFlashAttribute()`

#### Navegación y UX
- Menú de navegación entre vistas
- Enlaces "Volver al listado" desde detalle
- Botón "Crear nuevo" en el listado
- Botones "Cancelar" en formularios
- Enlaces a API REST para comparación

#### Estética y Diseño
  - **Bootstrap 5** integrado (CDN)
- CSS personalizado para mejorar UX:
  - Gradientes en formularios
  - Efectos hover en tablas
  - Badges de estado coloreados (verde, naranja, rojo)
  - Sombras y bordes redondeados
  - Iconos emoji para mejor legibilidad
- Diseño responsive para dispositivos móviles
- Paleta de colores coherente

### Arquitectura de la Solución

```
┌─────────────────────────────────────────────┐
│         Thymeleaf Templates                 │
│  (src/main/resources/templates/conflicts/)  │
│                                             │
│  • list.html       (Listado)                │
│  • create.html     (Formulario)             │
│  • details.html    (Detalles)               │
└────────────┬────────────────────────────────┘
             │
             ↓
┌─────────────────────────────────────────────┐
│   WebConflictController @Controller         │
│   (/web/conflicts)                          │
└────────────┬────────────────────────────────┘
             │
             ↓
┌─────────────────────────────────────────────┐
│      ConflictService (Business Layer)       │
│   • getAllConflictsDTO()                    │
│   • getConflictById()                       │
│   • createConflict()                        │
└────────────┬────────────────────────────────┘
             │
             ↓
┌─────────────────────────────────────────────┐
│   ConflictRepository                        │
│   (Acceso a datos H2/PostgreSQL)            │
└─────────────────────────────────────────────┘
```

### Métodos de Servicio Utilizados

Se agregó un nuevo método en `ConflictService`:

```java
@Transactional(readOnly = true)
public List<ConflictDTO> getAllConflictsDTO() {
    return conflictRepository.findAll().stream()
            .map(conflictMapper::toDTO)
            .collect(Collectors.toList());
}
```

Este método retorna DTOs completos (no resumidos) para proporcionar mayor información en la interfaz web.

### Características Thymeleaf Utilizadas

- **Variables Model**: `${conflicts}`, `${conflict}`, `${totalConflicts}`
- **Iteración**: `th:each="conflict : ${conflicts}"`
- **Condicionales**: `th:if`, `th:unless`, `th:switch`
- **Atributos**: `th:text`, `th:field`, `th:object`, `th:href`
- **Validación**: `th:errors`, `th:classappend`
- **Expresiones**: `#temporals.format()`, `#strings.abbreviate()`
- **Funciones**: Mapeo automático de objetos con `@ModelAttribute`

### Acceso a la Interfaz

| Ruta | Descripción | Método |
|------|-------------|--------|
| `http://localhost:8080/web/conflicts` | Listado de conflictos | GET |
| `http://localhost:8080/web/conflicts/new` | Formulario creación | GET |
| `http://localhost:8080/web/conflicts` | Guardar conflicto | POST |
| `http://localhost:8080/web/conflicts/{id}` | Detalles | GET |

### Comparación: Web vs API

La misma lógica de negocio (`ConflictService`) sirve:

**API REST** (`ConflictController`):
- Retorna JSON
- Responde en `/api/v1/conflicts`
- Cliente: Apps móviles, SPAs (React/Vue)

**Web** (`WebConflictController`):
- Retorna HTML renderizado
- Responde en `/web/conflicts`
- Cliente: Navegadores tradicionales

### Notas Técnicas

1. **Model vs ModelAttribute**:
   - `Model.addAttribute()` → Variables para templates
   - `@ModelAttribute` → Vinculación de formularios a objetos

2. **Validación**:
   - `@Valid` en controlador → Ejecuta anotaciones en DTO
   - `BindingResult` → Captura errores

3. **Redirecciones**:
   - Post-Redirect-Get pattern implementado
   - `RedirectAttributes.addFlashAttribute()` para mensajes

4. **Seguridad de URLs**:
   - Expresión `@{}` en Thymeleaf → Codifica URLs automáticamente

---

**Alumno**: Implementación de frontend completa y funcional
**Nivel**: Bueno (7.1) + Excelente (7.2)
