# HU-025: Agregar Consulta al Expediente

## Historia de Usuario

**Como** Médico  
**Quiero** agregar una nueva entrada de consulta al expediente existente de un paciente  
**Para** que su atención médica quede documentada cronológicamente

---

## Conversación

**P1: ¿Cuáles son los campos mínimos de una nota de consulta?**  
**R:** Subjetivo (Síntomas), Objetivo (Exploración física), Análisis (Diagnóstico) y Plan (Tratamiento). Formato SOAP. Además de Signos Vitales (que se pueden jalar de HU-030).

**P2: ¿El diagnóstico usa CIE-10?**  
**R:** Sí, el sistema debe tener un buscador del catálogo CIE-10 para estandarizar diagnósticos, aunque también debe permitir diagnósticos en texto libre para casos preliminares.

**P3: ¿Puedo dejar una consulta "en borrador"?**  
**R:** Sí, mientras el paciente está en el consultorio. Pero no se considera "Legal" hasta que se da clic en "Finalizar Consulta".

**P4: ¿Se guarda la hora exacta?**  
**R:** Sí, fecha y hora de inicio y de fin (guardado).

**P5: ¿Quién firma la nota?**  
**R:** El usuario logueado. Su nombre y cédula aparecen al pie de la nota automáticamente.

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Captura básica de consulta (SOAP)
```gherkin
Feature: Notas de Consulta
  Como Médico
  Quiero documentar la visita del paciente
  Para seguimiento clínico

  Background:
    Given el médico ha iniciado una nueva consulta para "Juan Pérez"

  Scenario: Llenado de nota SOAP
    When ingresa "Dolor de cabeza" en Subjetivo
    And ingresa "Garganta inflamada" en Objetivo
    And selecciona diagnóstico "J00 - Resfriado común" (CIE-10)
    And ingresa "Reposo e hidratación" en Plan
    And hace clic en "Finalizar Consulta"
    Then el sistema guarda la nota en el historial
    And marca la consulta como "Terminada"
```

### Escenario 2: Búsqueda de diagnósticos CIE-10
```gherkin
  Scenario: Autocompletado de diagnóstico
    When el médico escribe "diab" en el campo Diagnóstico
    Then el sistema despliega opciones del catálogo:
      | E11 - Diabetes mellitus tipo 2 |
      | E10 - Diabetes mellitus tipo 1 |
    And permite seleccionar uno
```

### Escenario 3: Datos de quien elabora
```gherkin
  Scenario: Firma automática
    Given el médico es "Dr. Chapatín" con cédula "123456"
    When finaliza la consulta
    Then la nota guardada muestra al final: "Elaboró: Dr. Chapatín - Céd. 123456"
    And la fecha y hora actuales
```

### Escenario 4: Guardado de borrador (Autoguardado)
```gherkin
  Scenario: Protección contra cierre accidental
    Given el médico tiene datos escritos sin finalizar
    When intenta salir de la pantalla o cerrar el navegador (simulado)
    Then el sistema muestra alerta "¿Desea guardar un borrador?"
    And si confirma, guarda el estado "En Progreso"
```

### Escenario 5: Validación de campos obligatorios
```gherkin
  Scenario: Intentar finalizar nota vacía
    When hace clic en "Finalizar Consulta" sin escribir nada
    Then el sistema marca los campos "Motivo de Consulta" y "Diagnóstico" en rojo
    And muestra error "Debe completar los datos clínicos mínimos para cerrar la consulta"
```

### Escenario 6: Integración automática de signos vitales
```gherkin
  Scenario: Jalar signos capturados previamente
    Given la enfermera ya registró signos vitales hace 10 minutos
    When el médico abre la nueva consulta
    Then los campos de Tensión Arterial, Peso y Temperatura ya aparecen pre-llenados con esos valores
    And permite al médico confirmarlos o editarlos
```
