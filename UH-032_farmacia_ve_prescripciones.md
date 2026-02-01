# HU-032: Farmacia Ve Prescripciones del Paciente

## Historia de Usuario

**Como** Personal de Farmacia  
**Quiero** ver las prescripciones de un paciente  
**Para** poder dispensar los medicamentos correctos y marcarlos como entregados

---

## Conversación

**P1: ¿Farmacia ve todo el expediente médico?**  
**R:** No. Por privacidad, solo ven la receta (medicamentos, dosis, doctor que prescribe). No ven notas de diagnósticos sensibles ni historia clínica detallada, salvo lo necesario para la dispensación.

**P2: ¿Cómo saben que hay una receta pendiente?**  
**R:** Tienen un "Tablero de Pedidos" que se actualiza en tiempo real. Cuando el médico cierra la consulta, aparece el paciente en la lista "Por Surtir".

**P3: ¿Pueden ver recetas viejas?**  
**R:** Sí, pueden consultar el historial de dispensación para ver qué se le entregó antes, pero la vista por defecto es "Lo de hoy/Pendiente".

**P4: ¿Se puede surtir parcialmente?**  
**R:** Sí. Si recetaron 3 medicinas y solo tenemos 2, surtimos 2 y la receta queda "Parcialmente Surtida".

**P5: ¿Cómo buscan una receta específica?**  
**R:** Por Folio de Receta o Nombre del Paciente.

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Recepción de nueva receta en tiempo real
```gherkin
Feature: Cola de Farmacia
  Como dispensador
  Quiero saber qué pacientes esperan medicina
  Para atenderlos en orden

  Background:
    Given el médico acaba de finalizar la receta "REC-100" para "Lucía Méndez"

  Scenario: Aparición en tablero
    When el personal de farmacia actualiza su pantalla
    Then aparece "Lucía Méndez" en la lista "Pendientes de Entrega"
    And muestra el estado "Nueva"
    And muestra la hora de emisión (hace 1 min)
```

### Escenario 2: Visualización de detalle de receta
```gherkin
  Scenario: Ver qué surtir
    When selecciona la orden de "Lucía Méndez"
    Then ve la lista de medicamentos:
      | Med                    | Cantidad | Dosis |
      | Paracetamol 500mg      | 1 caja   | ...   |
      | Ambroxol Jarabe        | 1 frasco | ...   |
    And ve instrucciones breves de toma (para explicarlas al paciente si es necesario)
```

### Escenario 3: Filtro de surtidos vs pendientes
```gherkin
  Scenario: Limpiar pantalla de trabajo
    Given hay 5 recetas pendientes y 20 ya entregadas hoy
    When selecciona el filtro "Estado: Pendientes"
    Then solo ve las 5 órdenes que requieren atención inmediata
```

### Escenario 4: Privacidad de datos diagnósticos
```gherkin
  Scenario: Farmacia no ve notas clínicas
    When abre el detalle de la receta
    Then ve el nombre del doctor y los medicamentos
    But NO ve el campo "Subjetivo/Objetivo" de la nota médica
    And el diagnóstico solo aparece si es requisito legal (ej. antibióticos)
```

### Escenario 5: Identificación del paciente al mostrador
```gherkin
  Scenario: Búsqueda rápida
    Given llega el paciente con su INE
    When el farmacéutico busca por Nombre "Lucía Méndez"
    Then el sistema encuentra su receta electrónica vigente
    And permite iniciar el proceso de surtido
```

### Escenario 6: Recetas caducadas
```gherkin
  Scenario: Receta muy antigua no surtida
    Given una receta fue emitida hace 30 días y nunca se recogió
    When el farmacéutico la consulta
    Then aparece marcada como "VENCIDA" (dependiendo política de vigencia)
    And el sistema advierte que se requiere re-valoración médica
```
