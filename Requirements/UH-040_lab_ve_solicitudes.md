# HU-040: Laboratorio Ve Solicitudes Pendientes

## Historia de Usuario

**Como** Personal de Laboratorio  
**Quiero** ver solicitudes de estudios pendientes  
**Para** saber qué pruebas procesar y organizar la toma de muestras

---

## Conversación

**P1: ¿Qué pacientes aparecen en mi lista?**  
**R:** Solo los que ya tienen la solicitud hecha Y (muy importante) ya pagaron o tienen autorización de crédito/urgencia. No queremos procesar muestras si no han pasado por caja.

**P2: ¿Cómo sé qué tubos preparar?**  
**R:** El sistema debe decirme. Si la orden es "Biometría + Química", debe indicarme: "1 Tubo Lila (EDTA) + 1 Tubo Rojo/Amarillo (Suero)".

**P3: ¿Puedo filtrar por área?**  
**R:** Sí. El laboratorio puede dividirse en Hematología, Microbiología, etc. Debo poder filtrar mi área de trabajo.

**P4: ¿Se genera etiqueta para la muestra?**  
**R:** Sí, el sistema debe permitir imprimir etiquetas con código de barras para pegar en los tubos y evitar confusiones.

**P5: ¿Qué hago si la muestra es insuficiente/hemolizada?**  
**R:** Debes poder marcar la solicitud como "Rechazada - Muestra Inadecuada" para que recepción avise al paciente o enfermería a repetir la toma.

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Worklist de toma de muestras
```gherkin
Feature: Gestión de Trabajo de Laboratorio
  Como flebotomista
  Quiero saber a quién picar
  Para tomar las muestras correctas

  Background:
    Given el paciente "Pedro" pagó su orden de "Biometría Hemática"

  Scenario: Paciente listo en sala de espera
    When el personal de laboratorio consulta "Pacientes por Atender"
    Then "Pedro" aparece en la lista
    And el estado es "Listo para Toma de Muestra"
    And muestra instrucciones: "Tubo Lila"
```

### Escenario 2: Filtrado de urgencias
```gherkin
  Scenario: Priorizar urgencias
    Given hay 10 pacientes de rutina y 1 de urgencias
    When el analista abre el tablero principal
    Then la solicitud urgente aparece AL INICIO de la lista
    And tiene un fondo de color diferente (Rojo)
```

### Escenario 3: Generación de etiquetas de códigos de barras
```gherkin
  Scenario: Identificación de muestras
    When el flebotomista confirma la llegada del paciente
    And hace clic en "Imprimir Etiquetas"
    Then el sistema genera etiquetas con: Nombre Paciente, Fecha, Tipo de Tubo y Código de Barras Único
```

### Escenario 4: Rechazo de muestra
```gherkin
  Scenario: Problema con la calidad de muestra
    When el analista recibe el tubo
    But nota que la sangre está coagulada
    Then marca el estudio como "Muestra Rechazada"
    And ingresa motivo "Muestra coagulada"
    And el sistema cambia el estado a "Requiere Nueva Toma"
    And notifica a Recepción/Enfermería
```

### Escenario 5: Bitácora de toma de muestra
```gherkin
  Scenario: Confirmar que ya se tomó
    When el flebotomista termina de sacar sangre
    And da clic en "Muestra Tomada"
    Then la fecha/hora se registra (Tiempo de Toma)
    And el paciente desaparece de la lista "Por Atender"
    And la solicitud pasa a la lista "En Proceso Analítico"
```

### Escenario 6: Validación de pago (Pre-requisito)
```gherkin
  Scenario: Paciente intenta pasar sin pagar
    Given el médico hizo la orden pero el paciente no ha pagado
    When el paciente llega a ventanilla de laboratorio
    Then el sistema NO muestra la orden en la lista "Por Atender"
    And si lo buscan manualmente, aparece estatus "Bloqueado - Falta Pago"
```
