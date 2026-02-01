# HU-007: Alertas de Artículos por Caducar

## Historia de Usuario

**Como** Encargado de Servicio  
**Quiero** ver alertas para artículos próximos a caducar  
**Para** poder usarlos antes de que expiren y minimizar desperdicios

---

## Conversación

**P1: ¿Con cuánta anticipación nos avisa el sistema sobre la caducidad?**  
**R:** El sistema tiene dos niveles de alerta:
- **Alerta Preventiva (Color Naranja):** Se activa 30 días antes de que el insumo venza, para darnos tiempo de usarlo.
- **Alerta Crítica (Color Rojo):** Se activa el mismo día del vencimiento y permanece así para indicar que ya no debe usarse.

**P2: ¿El sistema impide usar un artículo si ya caducó?**  
**R:** El sistema mostrará una advertencia muy clara y visible si intentas usar o dispensar algo caducado, pero no bloqueará la acción totalmente. Esto es para permitir flexibilidad en casos de emergencia extrema o si hubo un error al registrar la fecha, aunque quedará registro de que se ignoró la advertencia.

**P3: ¿Esto aplica para todo el inventario?**  
**R:** No, exclusivamente para los artículos que requieren control de caducidad, como medicamentos y reactivos. Materiales como papelería no tendrán estas alertas.

**P4: ¿Cómo vemos estas alertas en el día a día?**  
**R:** En la lista de inventario habrá una columna específica de "Caducidad" que mostrará la fecha y un ícono de color según el estado.

**P5: ¿Podemos ver rápidamente qué es lo que va a caducar primero?**  
**R:** Sí, existirá una opción para ordenar o filtrar la lista por "Próximos a caducar", lo que facilitará sacar primero lo más antiguo (PEPS).

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Alerta preventiva de artículo próximo a caducar
```gherkin
Feature: Alertas de Caducidad PROFECO
  Como Encargado de Servicio
  Quiero identificar insumos próximos a caducar
  Para priorizar su uso (sistema PEPS: Primeras Entradas, Primeras Salidas)

  Background:
    Given la fecha actual es "2026-06-01"
    And existen los siguientes lotes de "Paracetamol":
      | Lote   | Cantidad | Fecha Caducidad | Estado Esperado  |
      | L-100  | 50       | 2026-06-15      | Por Caducar (<30 días) |
      | L-200  | 100      | 2027-01-01      | OK               |

  Scenario: Visualizar artículos por caducar en la lista
    When el usuario consulta el inventario
    Then el lote "L-100" se muestra resaltado en NARANJA
    And muestra un mensaje explicativo "Caduca en 14 días"
    And el lote "L-200" se muestra con estado normal
```

### Escenario 2: Alerta crítica de artículo ya caducado
```gherkin
  Scenario: Identificar claramente artículos vencidos
    Given la fecha actual es "2026-06-01"
    And existe un lote "L-OLD" con fecha de caducidad "2026-05-30"
    When el usuario consulta el inventario
    Then el lote "L-OLD" se muestra resaltado en ROJO intenso
    And muestra el estado "CADUCADO" de forma prominente
    And el sistema sugiere la opción de "Dar de baja"
```

### Escenario 3: Filtro de prioridad para sacar lo antiguo primero
```gherkin
  Scenario: Filtrar insumos para aplicar PEPS
    When el usuario aplica el filtro "Próximos a Caducar"
    Then la lista muestra solo los artículos con alertas Naranja o Roja
    And los ordena automáticamente por fecha de caducidad ascendente (lo que vence primero aparece arriba)
```

### Escenario 4: Dispensación de artículo a punto de caducar (Alerta Naranja)
```gherkin
  Scenario: Usar artículo con alerta preventiva
    Given un usuario intenta dispensar del lote "L-100" (Alerta Naranja)
    When selecciona el artículo para dispensar
    Then el sistema muestra un aviso informativo: "Atención: Este insumo vence pronto (14 días)"
    And permite continuar con la operación sin bloqueos
```

### Escenario 5: Intento de dispensación de artículo caducado (Alerta Roja)
```gherkin
  Scenario: Advertencia al intentar usar artículo vencido
    Given un usuario intenta dispensar del lote "L-OLD" (Caducado)
    When selecciona el artículo para dispensar
    Then el sistema muestra una ADVERTENCIA CRÍTICA: "PELIGRO: El insumo seleccionado ESTÁ CADUCADO"
    And solicita confirmación explícita para proceder
    And si el usuario confirma, registra el evento en el historial de seguridad
```

### Escenario 6: Gestión de múltiples lotes con diferentes fechas
```gherkin
  Scenario: Visualizar múltiples lotes de un mismo producto
    Given el producto "Amoxicilina" tiene 3 lotes con diferentes fechas
    When el usuario expande los detalles de "Amoxicilina"
    Then puede ver cada lote individualmente
    And cada lote muestra su propia fecha de caducidad y color de alerta independiente
```
