# HU-038: Solicitar Estudios de Laboratorio

## Historia de Usuario

**Como** Médico  
**Quiero** solicitar estudios de laboratorio durante una consulta  
**Para** que se ordenen pruebas diagnósticas y el laboratorio las procese

---

## Conversación

**P1: ¿Hay un catálogo de estudios?**  
**R:** Sí. El médico no debe escribir "Química Sanguínea" a mano, debe seleccionarlo de un catálogo para que el sistema sepa qué reactivos descontar y qué precio cobrar.

**P2: ¿Se pueden pedir varios estudios a la vez?**  
**R:** Sí, es un "carrito" de estudios. Puedo pedir Biometría Hemática, Examen General de Orina y Química de 27 elementos en una sola orden.

**P3: ¿El sistema avisa si requiere ayuno?**  
**R:** Sería ideal. Al seleccionar el estudio, el sistema debería mostrar "Indicaciones para el paciente: Ayuno de 8 horas".

**P4: ¿Se pueden pedir estudios de urgencia?**  
**R:** Sí, debe haber un checkbox "Prioridad: URGENTE" para que el laboratorio lo procese primero.

**P5: ¿El cobro es automático?**  
**R:** Sí. Al generar la solicitud, se envía a Recepción/Caja como una "Orden de Pago Pendiente". El paciente debe pagar antes de pasar al laboratorio (HU-039), salvo urgencias críticas.

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Creación de orden de laboratorio
```gherkin
Feature: Solicitud de Auxiliares Diagnósticos
  Como Médico
  Quiero pedir análisis clínicos
  Para confirmar mi diagnóstico

  Background:
    Given el médico está en consulta con "Ana López"

  Scenario: Solicitar perfil diabético
    When selecciona "Glucosa en Ayunas"
    And selecciona "Hemoglobina Glicosilada (HbA1c)"
    And añade indicación "Urge para ajuste de insulina"
    And guarda la solicitud
    Then se genera la orden de laboratorio "LAB-505"
    And el estado es "Pendiente de Pago"
```

### Escenario 2: Visualización de instrucciones al paciente
```gherkin
  Scenario: Imprimir hoja de solicitud
    When el médico imprime la orden
    Then el documento incluye: "Estudios solicitados"
    And para "Perfil Lipídico" imprime: "Ayuno de 12 horas requerido"
    And para "EGO" imprime: "Primera orina de la mañana"
```

### Escenario 3: Validación de estudios duplicados
```gherkin
  Scenario: Evitar doble solicitud el mismo día
    Given ya existe una orden de "Biometría Hemática" de hoy
    When el médico intenta pedir otra "Biometría Hemática"
    Then el sistema muestra alerta: "Este paciente ya tiene una solicitud igual del día de hoy. ¿Desea duplicarla?"
```

### Escenario 4: Solicitud Prioritaria (Urgencias)
```gherkin
  Scenario: Marcar como Urgente
    When selecciona "Troponina I"
    And marca la casilla "URGENTE"
    And guarda
    Then la solicitud aparece en el Laboratorio con un distintivo ROJO parpadeante
```

### Escenario 5: Paquetes de estudios (Check-up)
```gherkin
  Scenario: Selección rápida de grupos
    When el médico busca "Perfil Hepático"
    Then el sistema añade automáticamente los 5 estudios individuales del perfil (TGO, TGP, FA, BT, BD)
    And permite quitar alguno si no es necesario
```

### Escenario 6: Cancelación de solicitud
```gherkin
  Scenario: Error al pedir estudio
    Given la orden fue creada hace 5 minutos
    And el paciente aún no ha pagado
    When el médico decide cancelar "EGO"
    Then el sistema permite la cancelación
    And desaparece la deuda en Caja
```
