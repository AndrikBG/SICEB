# HU-006: Alertas de Stock Bajo

## Historia de Usuario

**Como** Encargado de Servicio  
**Quiero** ver alertas automáticas cuando los artículos de inventario estén por agotarse  
**Para** poder solicitar reabastecimiento antes de quedarme sin existencias

---

## Conversación

**P1: ¿Cuándo se considera que un artículo está "por agotarse"?**  
**R:** Cuando la cantidad que tenemos físicamente es igual o menor al nivel mínimo que hemos definido como seguro para ese artículo.

**P2: ¿Dónde aparecen estas alertas?**  
**R:** 
1. En el Tablero principal del Encargado (en una sección dedicada a "Artículos Críticos").
2. En la lista general de inventario, donde estos artículos se resaltan claramente (con color amarillo o un ícono de advertencia).

**P3: ¿El sistema envía correos electrónicos?**  
**R:** No en esta etapa. Las alertas son únicamente visuales dentro de la aplicación para que el encargado las vea mientras trabaja.

**P4: ¿El stock mínimo es configurable?**  
**R:** Sí. Para esta versión, el Encargado puede ajustar el "Stock Mínimo" de sus artículos para adaptar las alertas a la realidad y necesidades específicas de su servicio.

**P5: ¿Qué pasa si la cantidad llega a 0?**  
**R:** La alerta cambia de prioridad "Stock Bajo" (Amarillo) a "Agotado" (Rojo) y el sistema debe darle mayor visibilidad para que se atienda de inmediato.

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Visualización de alerta de stock bajo
```gherkin
Feature: Alertas de Stock Bajo
  Como Encargado de Servicio
  Quiero identificar rápidamente qué artículos necesitan reabastecimiento
  Para evitar desabasto en mi servicio

  Background:
    Given el usuario "encargado_urgencias" ha iniciado sesión
    And el artículo "Gasas Estériles" tiene:
      | Cantidad Actual | 15  |
      | Stock Mínimo    | 20  |
    And el artículo "Vendas 10cm" tiene:
      | Cantidad Actual | 50  |
      | Stock Mínimo    | 20  |

  Scenario: Identificar artículos con stock bajo en la lista
    When navega a la sección de "Inventario"
    Then el artículo "Gasas Estériles" se muestra con un indicador de alerta "Stock Bajo" (Amarillo)
    And el artículo "Vendas 10cm" se muestra con estado "OK" (Verde/Normal)
```

### Escenario 2: Widget de dashboard en tablero principal
```gherkin
  Scenario: Ver resumen de artículos críticos en el Tablero
    Given existen 3 artículos con stock bajo en el servicio "Urgencias"
    When navega al "Tablero Principal"
    Then el widget "Alertas de Inventario" muestra el número "3" en color amarillo
    And al hacer clic en el widget, redirige a la lista de inventario filtrada para mostrar solo los artículos con problemas
```

### Escenario 3: Alerta de Agotado (Cero existencia)
```gherkin
  Scenario: Artículo con existencia cero debe ser prioritario
    Given el artículo "Adrenalina Ampolletas" tiene cantidad 0
    When navega a la sección de "Inventario"
    Then el artículo se muestra con indicador de alerta "AGOTADO" (Rojo)
    And este artículo aparece listado antes que los artículos con stock bajo
```

### Escenario 4: Filtrar inventario por tipo de alerta
```gherkin
  Scenario: Filtrar artículos críticos para gestión rápida
    Given existen artículos en estados "OK", "Stock Bajo" y "Agotado"
    When selecciona el filtro "Estado: Críticos"
    Then la lista muestra únicamente los artículos "Agotado" y "Stock Bajo"
    And no muestra los artículos cons estado "OK"
```

### Escenario 5: Desaparición de alerta tras reabastecimiento
```gherkin
  Scenario: Alerta desaparece automáticamente al recibir insumos
    Given el artículo "Gasas Estériles" tiene alerta de "Stock Bajo" (Cantidad: 15, Mínimo: 20)
    When se registra una entrada de inventario de 10 unidades para "Gasas Estériles"
    Then la cantidad actual se actualiza a 25
    And la alerta de "Stock Bajo" desaparece
    And el estado del artículo cambia a "OK"
```

### Escenario 6: Configurar Stock Mínimo personalizado
```gherkin
  Scenario: Encargado ajusta nivel de alerta
    Given el artículo "Guantes Látex" tiene stock mínimo de 100
    When el Encargado cambia el stock mínimo a 50
    And la cantidad actual es 80
    Then el artículo NO muestra alerta de "Stock Bajo" (antes alerta, ahora OK)
```
