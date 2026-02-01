# HU-066: Registrar Auditoría de Accesos a Expedientes

## Historia de Usuario

**Como** sistema  
**Quiero** registrar en un log de auditoría quién accedió a qué expediente y cuándo  
**Para** cumplir con la trazabilidad exigida por la LFPDPPP y normas de seguridad

---

## Conversación

**P1: ¿Qué información específica debemos registrar en cada acceso?**  
**R:** Debemos capturar: el usuario que realiza la acción, la fecha y hora exacta, el ID del paciente/expediente accedido, el tipo de acción (Lectura, Edición, Impresión) y la dirección IP desde donde se conectó.

**P2: ¿Se pueden borrar estos registros?**  
**R:** Nunca. Los logs de auditoría son inmutables. Ni siquiera el Administrador General debe poder eliminarlos, ya que son nuestra evidencia legal en caso de incidentes.

**P3: ¿Quién puede ver estos registros?**  
**R:** Únicamente el Director General y el Administrador General tienen permisos para consultar el historial de auditoría.

**P4: ¿Cuánto tiempo debemos guardar esta información?**  
**R:** Por requisitos legales, debemos garantizarlos por lo menos durante 5 años.

**P5: ¿Se registra también si alguien intenta acceder y falla?**  
**R:** Sí, los intentos denegados (403 Forbidden) son aún más importantes y deben quedar marcados con una alerta visual en el reporte.

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Registro automático de visualización de expediente
```gherkin
Feature: Auditoría de Accesos Clínicos
  Como oficial de seguridad
  Quiero que el sistema registre cada acceso a datos sensibles
  Para tener trazabilidad forense

  Background:
    Given el usuario "drmedico" (Médico Adscrito) ha iniciado sesión
    And existe el paciente "Juan Pérez" con ID 1001

  Scenario: Médico consulta expediente
    When el usuario abre el expediente del paciente 1001
    Then el sistema muestra la información del paciente
    And el sistema crea un registro de auditoría con:
      | Usuario   | drmedico            |
      | Recurso   | Expediente 1001     |
      | Acción    | LECTURA             |
      | Fecha     | (Fecha/Hora actual) |
      | Resultado | EXITOSO             |
```

### Escenario 2: Registro de modificación de datos
```gherkin
  Scenario: Médico agrega nota de evolución
    When el usuario guarda una nueva nota en el expediente 1001
    Then el sistema guarda la nota médica
    And el sistema crea un registro de auditoría con:
      | Usuario   | drmedico            |
      | Recurso   | Expediente 1001     |
      | Acción    | ESCRITURA           |
      | Detalle   | Nueva nota evolución|
```

### Escenario 3: Intento de acceso no autorizado
```gherkin
  Scenario: Usuario sin permisos intenta ver expediente
    Given el usuario "recep_ana" (Recepción) intenta acceder a "/api/expedientes/1001"
    When el sistema deniega el acceso
    Then el sistema crea un registro de auditoría CRÍTICO con:
      | Usuario   | recep_ana           |
      | Recurso   | Expediente 1001     |
      | Acción    | LECTURA             |
      | Resultado | DENEGADO (403)      |
      | Nivel     | ALERTA DE SEGURIDAD |
```

### Escenario 4: Administrador consulta logs por fecha
```gherkin
  Scenario: Filtrar logs de auditoría por rango de fechas
    Given el usuario "admin" ha ingresado al módulo "Auditoría"
    When selecciona el rango de fechas "01/01/2026" al "31/01/2026"
    Then el sistema muestra todos los accesos registrados en ese periodo
    And permite exportar el reporte a PDF
```

### Escenario 5: Búsqueda de actividad por usuario específico
```gherkin
  Scenario: Investigar actividad de un usuario sospechoso
    Given el usuario "admin" está en el módulo "Auditoría"
    When filtra por usuario "residente_nuevo"
    Then el sistema muestra cronológicamente todas las acciones de "residente_nuevo"
    And resalta en rojo cualquier acceso denegado
```

### Escenario 6: Validación de inmutabilidad
```gherkin
  Scenario: Intentar borrar registros de auditoría
    Given el usuario "admin" selecciona un registro de auditoría
    Then NO existe ningún botón o opción para "Eliminar"
    And si intenta enviar una petición de borrado por API
    Then el sistema rechaza la petición con error "Operación no permitida: Logs inmutables"
```
