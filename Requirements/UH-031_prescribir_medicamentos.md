# HU-031: Prescribir Medicamentos en Consulta

## Historia de Usuario

**Como** Médico  
**Quiero** prescribir medicamentos durante una consulta  
**Para** que la prescripción quede registrada y disponible para farmacia de inmediato

---

## Conversación

**P1: ¿Cómo busca el médico los medicamentos?**  
**R:** Por nombre comercial o sustancia activa. El sistema debe autocompletar desde el catálogo interno.

**P2: ¿Qué datos debe indicar para cada línea de medicamento?**  
**R:** Medicamento, Dosis (ej. 500mg), Frecuencia (cada 8 horas), Duración (por 5 días) y Vía de administración (Oral, IM, IV).

**P3: ¿El sistema calcula la cantidad total a surtir?**  
**R:** No. 

**P4: ¿Puede recetar algo que no tenemos en inventario?**  
**R:** Sí. El médico receta lo que el paciente necesita. Si Farmacia no lo tiene, es otro problema (surtido externo), pero la necesidad clínica queda registrada.

**P5: ¿Se pueden repetir recetas anteriores?**  
**R:** Sí, para pacientes crónicos, debería haber un botón "Copiar última receta" para agilizar .

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Prescripción estándar
```gherkin
Feature: Receta Médica Electrónica
  Como Médico
  Quiero indicar el tratamiento
  Para curar al paciente

  Background:
    Given el médico está en una consulta activa con "Juan Pérez"

  Scenario: Agregar medicamento
    When busca "Paracetamol"
    And selecciona "Paracetamol 500mg Tabletas"
    And indica: "1 tableta cada 8 horas por 3 días" (Oral)
    And hace clic en "Agregar"
    Then el medicamento se añade a la lista de prescripción actual
    And el sistema calcula cantidad sugerida: 9 tabletas
```

### Escenario 2: Advertencia de Alergias (Interacción Medicamento-Paciente)
```gherkin
  Scenario: Paciente alérgico a Penicilina
    Given el paciente tiene registrada alergia a "Penicilina"
    When el médico intenta recetar "Amoxicilina"
    Then el sistema muestra una ALERTA ROJA: "CONTRAINDICACIÓN: Paciente alérgico a Penicilinas"
    And pide confirmación y justificación para proceder
```

### Escenario 3: Generación de receta impresa/digital
```gherkin
  Scenario: Finalizar receta
    Given se han agregado 3 medicamentos
    When el médico finaliza la consulta
    Then se genera un folio de receta único
    And la información se envía al módulo de Farmacia instantáneamente
    And se genera un PDF imprimible con la firma digital del médico
```

### Escenario 4: Receta de medicamento controlado (Antibióticos/Psicotrópicos)
```gherkin
  Scenario: Validación de antibióticos
    When receta "Ceftriaxona" (Antibiótico IV)
    Then el sistema marca el medicamento como "Requiere Control"
    And obliga al médico a capturar el diagnóstico asociado (según norma)
    And en la impresión, resalta los datos requeridos por COFEPRIS/Autoridad
```

### Escenario 5: Copiar tratamiento crónico
```gherkin
  Scenario: Paciente hipertenso recurrente
    Given el paciente tiene una receta de hace un mes (Losartán)
    When el médico selecciona "Repetir última receta"
    Then se cargan los medicamentos con la misma dosis y frecuencia
    And permite editarlos antes de guardar
```

### Escenario 6: Validación de campos de dosificación
```gherkin
  Scenario: Olvidar frecuencia
    When selecciona "Ibuprofeno"
    But no especifica "Cada cuándo" ni "Por cuánto tiempo"
    And intenta agregar
    Then el sistema muestra error "Debe especificar frecuencia y duración del tratamiento"
```
