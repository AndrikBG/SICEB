# HU-028: Buscar Pacientes

## Historia de Usuario

**Como** Médico o Personal de Recepción  
**Quiero** buscar pacientes por nombre, ID, o número de estudiante/empleado  
**Para** poder acceder rápidamente a sus expedientes y realizar trámites

---

## Conversación

**P1: ¿Por qué criterios exactos se puede buscar?**  
**R:** Por Nombre completo (o parte del nombre), Número de Expediente (ID interno), CURP y Matrícula/Número de Empleado (para universitarios).

**P2: ¿El sistema muestra resultados parciales?**  
**R:** Sí. Si escribo "Juan", debe mostrar todos los "Juan". La búsqueda debe ser flexible (coincidencia parcial).

**P3: ¿Qué información muestra la lista de resultados?**  
**R:** Lo mínimo necesario para identificarlo sin abrir el expediente: Nombre, Fecha de Nacimiento/Edad, Sexo y el dato clave que se usó (ej. Matrícula).

**P4: ¿Puede buscar en expedientes inactivos/fallecidos?**  
**R:** Sí, pero deben aparecer marcados claramente (ej. en gris o con ícono de "Inactivo").

**P5: ¿Qué pasa si hay muchos resultados?**  
**R:** La lista se paginada (10-20 resultados por página) para no saturar la pantalla.

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Búsqueda exitosa por nombre
```gherkin
Feature: Búsqueda de Pacientes
  Como usuario clínico
  Quiero encontrar el registro de un paciente
  Para atenderlo

  Background:
    Given existen los pacientes "Juan Pérez", "Juan López" y "Ana García"

  Scenario: Buscar por nombre parcial
    When el usuario ingresa "Juan" en el buscador
    And presiona Enter
    Then el sistema muestra 2 resultados
    And la lista incluye a "Juan Pérez" y "Juan López"
    And NO incluye a "Ana García"
```

### Escenario 2: Búsqueda exacta por Expediente
```gherkin
  Scenario: Buscar por ID único
    Given el paciente "Ana García" tiene ID de expediente "EXP-1005"
    When el usuario busca "EXP-1005"
    Then el sistema muestra un único resultado: "Ana García"
    And permite abrir su expediente directamente
```

### Escenario 3: Búsqueda por Matrícula Escolar
```gherkin
  Scenario: Buscar estudiante por matrícula
    Given el paciente "Pedro" tiene matrícula "A00123456"
    When el usuario busca "A00123456"
    Then el sistema encuentra a "Pedro"
    And muestra su matrícula en los detalles del resultado
```

### Escenario 4: Búsqueda sin resultados
```gherkin
  Scenario: Paciente no encontrado
    When el usuario busca "NombreInexistente"
    Then el sistema muestra el mensaje "No se encontraron pacientes con ese criterio"
    And sugiere el botón "Registrar Nuevo Paciente"
```

### Escenario 5: Visualización de paciente inactivo
```gherkin
  Scenario: Encontrar paciente dado de baja
    Given el paciente "Luis" está marcado como "Inactivo"
    When el usuario busca "Luis"
    Then el sistema lo muestra en la lista
    But lo resalta en color GRIS
    And muestra una etiqueta "INACTIVO" junto a su nombre
```

### Escenario 6: Filtrado rápido por coincidencia múltiple
```gherkin
  Scenario: Búsqueda inteligente
    Given el paciente se llama "Maria Jose" con CURP "MAJO90..."
    When el usuario escribe "MAJO90"
    Then el sistema lo encuentra por coincidencia de CURP
    When el usuario borra y escribe "Jose"
    Then el sistema lo encuentra por coincidencia de Nombre
```
