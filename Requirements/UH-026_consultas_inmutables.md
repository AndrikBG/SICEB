# HU-026: Consultas Inmutables

## Historia de Usuario

**Como** Médico  
**Quiero** que las entradas de consulta sean inmutables (no editables) una vez finalizadas  
**Para** que los expedientes médicos mantengan integridad legal y fiabilidad para auditorías

---

## Conversación

**P1: ¿Qué significa exactamente "inmutable"?**  
**R:** Que una vez presionado el botó "Finalizar Consulta", el texto NO se puede borrar ni modificar. Es documento legal cerrado.

**P2: ¿Y si me equivoqué de dedo o dato?**  
**R:** Debes crear una "Nota Aclaratoria" o "Fe de Erratas" posterior que haga referencia a la nota original, pero la nota original se queda tal cual. El sistema debe permitir agregar estas notas anexas.

**P3: ¿El Administrador puede editarlo?**  
**R:** No. Nadie. Ni el programador (por base de datos sí, pero la aplicación no debe permitirlo). Esto da confianza de que nadie alteró el historial.

**P4: ¿Cuánto tiempo tengo para editar antes de que se bloquee?**  
**R:** Mientras está en estado "Borrador" o "En Progreso" es totalmente editable. El bloqueo ocurre al cambiar el estado a "Finalizada".

**P5: ¿Las notas aclaratorias también se bloquean?**  
**R:** Sí, siguen la misma lógica. Una vez guardada la aclaración, tampoco se puede editar.

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Bloqueo de edición tras finalizar
```gherkin
Feature: Integridad Clínica
  Como sistema legal 
  Quiero proteger la información histórica
  Para evitar alteraciones

  Background:
    Given existe una consulta con estado "Finalizada" del día de ayer

  Scenario: Intentar editar nota cerrada
    When el médico abre la consulta para ver detalles
    Then todos los campos (Subjetivo, Objetivo, Plan) aparecen en modo SOLO LECTURA
    And no existe botón "Editar" ni "Guardar"
    And los campos están bloqueados (disabled)
```

### Escenario 2: Corrección mediante nota posterior
```gherkin
  Scenario: Agregar fe de erratas
    Given una consulta finalizada con un error en el peso (dice 80kg, era 70kg)
    When el médico selecciona opción "Agregar Nota Aclaratoria"
    And escribe: "Fe de erratas: El peso correcto es 70kg, no 80kg."
    And guarda la nota
    Then la consulta original MANTIENE "80kg"
    And aparece un anexo visible: "Nota Aclaratoria [Fecha]: Fe de erratas..."
```

### Escenario 3: Borrador sí es editable
```gherkin
  Scenario: Editar consulta en progreso
    Given una consulta con estado "En Progreso" (no finalizada)
    When el médico cambia el diagnóstico
    And guarda cambios
    Then el sistema actualiza la información correctamente (aún no es inmutable)
```

### Escenario 4: Prohibición de borrado
```gherkin
  Scenario: Intentar eliminar consulta del historial
    Given el historial del paciente tiene 5 consultas pasadas
    When el usuario busca opción para "Eliminar Consulta"
    Then el botón de eliminar NO existe en la interfaz
    And ninguna consulta puede ser borrada bajo ninguna circunstancia
```

### Escenario 5: Auditoría de intentos de violación (Seguridad)
```gherkin
  Scenario: Intento de edición forzada por API
    Given un usuario "hacker" intenta enviar POST /api/consultas/{id_finalizada}/update
    When el backend recibe la petición
    Then verifica que el estado es "Finalizada"
    And rechaza la petición con Error 409 Conflict ("El recurso es inmutable")
    And registra el incidente en seguridad
```

### Escenario 6: Visualización clara de estado
```gherkin
  Scenario: Distinguir notas abiertas de cerradas
    When el médico ve el listado de consultas
    Then las notas finalizadas tienen un ícono de "Candado Cerrado"
    And las notas en borrador tienen un ícono de "Lápiz" o "Borrador"
```
