# HU-024: Crear Expediente Médico

## Historia de Usuario

**Como** Médico o Encargado de Servicio  
**Quiero** crear un nuevo expediente médico cuando registro un paciente  
**Para** que comience su historial clínico digital de forma organizada

---

## Conversación

**P1: ¿El expediente se crea manualmente?**  
**R:** No, se crea automáticamente al registrar al paciente (HU-019), pero esta historia define la *estructura* inicial que debe tener.

**P2: ¿Qué secciones debe tener un expediente nuevo?**  
**R:** Debe nacer con las secciones vacías listas:
1.  **Historia Clínica:** Heredofamiliares, Patológicos, No Patológicos.
2.  **Notas de Evolución:** Donde irán las consultas.
3.  **Estudios:** Laboratorio e Imagen.
4.  **Signos Vitales:** Gráficas.
5.  **Prescripciones:** Historial de recetas.

**P3: ¿Quién es el "propietario" de ese expediente?**  
**R:** La clínica. No pertenece a un médico específico. Cualquier médico autorizado puede consultarlo.

**P4: ¿Se le asigna un folio?**  
**R:** Sí, el ID de expediente debe ser único e irrepetible.

**P5: ¿Se debe capturar la Historia Clínica inmediatamente?**  
**R:** No es obligatorio en el segundo 1, pero el sistema debe insistir (mediante alertas o indicadores de "Perfil Incompleto") para que el médico la llene en la primera consulta.

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Generación automática de estructura
```gherkin
Feature: Estructura del Expediente
  Como sistema
  Quiero organizar la información de salud
  Para facilitar la práctica clínica

  Background:
    Given se acaba de registrar al paciente "Nuevo Usuario"

  Scenario: Creación de secciones vacías
    When el médico abre el expediente por primera vez
    Then ve las pestañas: "Historia Clínica", "Consultas", "Signos", "Recetas", "Estudios"
    And todas las secciones están habilitadas para escritura
    And muestra el estado "Expediente Nuevo - Historia Clínica Pendiente"
```

### Escenario 2: Asignación de Folio Único
```gherkin
  Scenario: Poner identificador clínico
    When el sistema confirma el registro del paciente
    Then genera un Folio de Expediente (ej. 2026-00501)
    And lo muestra visiblemente en la cabecera del perfil
    And este folio no puede ser modificado manualmente
```

### Escenario 3: Validación de acceso inicial
```gherkin
  Scenario: Verificar permisos de creación
    Given un usuario "Recepcionista"
    When intenta acceder a la sección "Historia Clínica"
    Then el sistema bloquea el acceso (solo lectura de datos demográficos)
    And muestra "Acceso Clínico Restringido a Personal Médico"
```

### Escenario 4: Alerta de Antecedentes Pendientes
```gherkin
  Scenario: Recordar llenado de historia clínica
    Given un expediente nuevo sin antecedentes capturados
    When el médico inicia una consulta
    Then el sistema muestra un aviso amarillo: "Falta capturar Antecedentes Heredofamiliares y Patológicos"
    And ofrece un acceso directo "Capturar ahora"
```

### Escenario 5: Unicidad del expediente
```gherkin
  Scenario: Un paciente, un expediente
    Given el paciente ya tiene el expediente "EXP-100"
    When el sistema intenta procesos internos
    Then jamás crea un "EXP-101" para el mismo ID de paciente
    And siempre reutiliza el expediente existente
```

### Escenario 6: Auditoría de creación
```gherkin
  Scenario: Log de apertura
    When se crea el expediente
    Then se registra en auditoría: "Expediente Creado - Fecha: [Hoy]"
    And inicia el ciclo de vida del documento
```
