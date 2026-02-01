9# HU-052: Registrar Uso de Insumos en Consulta

## Historia de Usuario

**Como** Médico  
**Quiero** registrar qué insumos usé durante una consulta  
**Para** que el inventario se actualice automáticamente y los costos se rastreen por paciente

---

## Conversación

**P1: ¿Debo registrar cada torunda de algodón?**  
**R:** No, sería impráctico. Hay dos tipos de insumos:
1.  **De cargo directo/trazables:** Medicamentos aplicados, jeringas especiales, suturas, vendas grandes. Estos SÍ se registran uno por uno.
2.  **Gasto general (Bulk):** Algodón, alcohol, abatelenguas simples. Estos se prorratean o se descuentan por "Kit de Consulta" (cada consulta descuenta 1 kit virtual).
    *Para esta historia, nos enfocamos en el registro explícito de los materiales trazables.*

**P2: ¿Cómo los agrego?**  
**R:** En la pantalla de Consulta, hay una pestaña "Materiales Utilizados". Busco y agrego (ej. "Jeringa 10ml - 1 pza").

**P3: ¿Se descuentan de mi inventario de servicio?**  
**R:** Sí. Se descuentan del stock del consultorio/servicio donde estoy atendiendo.

**P4: ¿Se le cobra al paciente?**  
**R:** Depende de la configuración. Algunos materiales están incluidos en el costo de consulta, otros se cargan extra. El sistema debe saber cuál es cuál.

**P5: ¿Qué pasa si no registro nada?**  
**R:** El sistema asume solo gasto general. Pero si hice una curación y no registré las gasas, habrá merma en el inventario físico vs sistema luego.

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Registro de materiales consumibles
```gherkin
Feature: Gasto de Materiales
  Como Médico
  Quiero justificar el uso de insumos
  Para mantener el inventario al día

  Background:
    Given el médico está terminando una consulta
    And utilizó 1 par de guantes estériles y 1 sutura

  Scenario: Captura de consumo
    When va a la sección "Materiales"
    And agrega "Guantes Estériles" (1)
    And agrega "Sutura Nylon 3-0" (1)
    And finaliza la consulta
    Then el sistema descuenta esos artículos del inventario del Servicio
```

### Escenario 2: Validación de stock al consumir
```gherkin
  Scenario: Consumir lo que no hay (Error lógico)
    When intenta registrar "Venda Yeso"
    But el sistema dice que hay 0 existencias
    Then permite registrarlo pero lanza una ADVERTENCIA: "Inventario negativo generado. Favor de verificar."
    (Porque físicamente sí se usó, el sistema debe reflejar la realidad aunque el stock lógico estuviera mal)
```

### Escenario 3: Cargo automático a cuenta del paciente
```gherkin
  Scenario: Material cobrable
    Given la "Sutura" está configurada como "Cobrable"
    When se registra en la consulta
    Then se agrega un cargo a la cuenta del paciente por el costo de la sutura
```

### Escenario 4: Material incluido en consulta
```gherkin
  Scenario: Material no cobrable
    Given los "Guantes" están configurados como "Incluidos"
    When se registra su uso
    Then se descuenta del inventario
    But NO se genera cargo extra al paciente
```

### Escenario 5: Kits predefinidos (Favoritos)
```gherkin
  Scenario: Cargar kit de curación
    When el médico selecciona "Kit Curación Básica"
    Then el sistema agrega automáticamente: 2 Gasas, 1 Venda, 1 Isodine
    And ahorra tiempo de captura manual
```

### Escenario 6: Trazabilidad por paciente
```gherkin
  Scenario: Auditoría de costos
    When el administrador revisa el reporte de costos
    Then puede ver exactamente qué materiales se gastaron en el paciente "Juan Pérez" en su visita de hoy
```
