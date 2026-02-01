# HU-030: Registrar Signos Vitales

## Historia de Usuario

**Como** Médico o Enfermera (Personal de Salud)  
**Quiero** registrar signos vitales durante una consulta o pre-consulta  
**Para** que las tendencias de salud del paciente puedan rastrearse y detectar anomalías

---

## Conversación

**P1: ¿Qué signos vitales capturamos?**  
**R:** Presión Arterial (Sistólica/Diastólica), Frecuencia Cardíaca, Frecuencia Respiratoria, Temperatura (€C), Peso (kg), Talla (cm) y Saturación de Oxígeno (SpO2).

**P2: ¿El IMC se calcula solo?**  
**R:** Sí. Al ingresar Peso y Talla, el sistema debe calcular el Índice de Masa Corporal automáticamente.

**P3: ¿Quién los captura?**  
**R:** Generalmente la enfermera en el área de triaje/somatometría antes de que el paciente entre con el médico. O el médico mismo durante la consulta.

**P4: ¿Se grafican?**  
**R:** Sí, queremos ver una gráfica de evolución, especialmente de Peso y Presión Arterial, para ver tendencias en el tiempo.

**P5: ¿Hay alertas de valores anormales?**  
**R:** Sería muy útil. Si la temperatura es > 38°C, que se ponga en rojo. Si la presión es > 140/90, alerta de hipertensión.

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Registro de somatometría básica
```gherkin
Feature: Signos Vitales
  Como personal de enfermería
  Quiero registrar datos físicos del paciente
  Para monitorear su estado

  Background:
    Given el usuario está en la pantalla de "Signos Vitales" del paciente "Luis"

  Scenario: Registrar signos
    When ingresa:
      | Talla (cm) | 170 |
      | Peso (kg)  | 70  |
      | Temp (°C)  | 36.5|
      | FC (lpm)   | 75  |
      | FR (rpm)   | 18  |
      | TA (mmHg)  | 120/80 |
      | SpO2 (%)   | 98  |
    And guarda el registro
    Then se almacena con la fecha y hora actual
```

### Escenario 2: Cálculo automático de IMC
```gherkin
  Scenario: Obtener IMC
    When ingresa Peso "80" kg
    And ingresa Talla "180" cm (1.80 m)
    Then el sistema muestra automáticamente:
      | IMC | 24.69 |
      | Categoría | Peso Normal |
```

### Escenario 3: Alerta de fiebre (Visual)
```gherkin
  Scenario: Temperatura alta
    When ingresa Temperatura "39.5"
    Then el campo se resalta en ROJO
    And muestra ícono de alerta "Fiebre"
```

### Escenario 4: Alerta de presión arterial (Hipertensión)
```gherkin
  Scenario: Crisis hipertensiva
    When ingresa Tensión Arterial "160/100"
    Then el sistema detecta valores fuera de rango normal
    And muestra alerta visible "Posible Hipertensión Grado 2"
```

### Escenario 5: Gráfica de tendencias
```gherkin
  Scenario: Ver evolución de peso
    Given el paciente tiene 5 registros de peso en el último año
    When el médico selecciona la vista "Gráficas"
    Then se muestra una gráfica de línea del Peso vs Tiempo
    And permite ver claramente si el paciente ha subido o bajado
```

### Escenario 6: Registro incompleto permitido
```gherkin
  Scenario: Solo tomar presión
    When la enfermera solo mide la Presión Arterial (ej. revisión rápida)
    And deja vacío Peso y Talla
    And guarda
    Then el sistema permite guardar el registro parcial
    And el IMC aparece como "No calculado" (no marca error)
```
