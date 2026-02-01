# HU-009: Enviar Solicitud Digital de Insumos

## Historia de Usuario

**Como** Encargado de Servicio  
**Quiero** enviar una solicitud digital de insumos  
**Para** que el proceso de reabastecimiento sea más rápido y rastreable

---

## Conversación

**P1: ¿A quién le pido los insumos?**  
**R:** Al Almacén Central (Administrador General).

**P2: ¿Cómo sé qué pedir?**  
**R:** El sistema me muestra el catálogo de insumos autorizados para mi servicio. Puedo ver cuánto tengo (Stock actual) y cuánto necesito.

**P3: ¿Debo justificar el pedido?**  
**R:** Sí, especialmente si pido cantidades inusuales. El campo "Observaciones/Justificación" es útil para explicar ("Campaña de vacunación próxima", "Derrame accidental", etc.).

**P4: ¿Puedo guardar un borrador?**  
**R:** Sí, puedo ir armando mi pedido durante el turno y enviarlo al final.

**P5: ¿Cómo sé que lo recibieron?**  
**R:** El estado cambia de "Borrador" a "Enviado/Pendiente de Aprobación".

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Crear solicitud estándar
```gherkin
Feature: Requisición de Materiales
  Como Jefe de Servicio
  Quiero surtido de materiales
  Para operar mi área

  Background:
    Given el usuario "Jefe Urgencias" está en "Solicitar Insumos"

  Scenario: Pedido normal
    When selecciona "Guantes de Látex Medianos" -> Cantidad: 10 cajas
    And selecciona "Jeringas 5ml" -> Cantidad: 100 piezas
    And agrega comentario: "Reposición semanal"
    And hace clic en "Enviar Solicitud"
    Then el sistema genera el Folio "SOL-2026-001"
    And notifica que la solicitud fue enviada al Almacén
```

### Escenario 2: Validación de cantidades negativas
```gherkin
  Scenario: Error de dedo
    When intenta pedir "-5" cajas
    Then el sistema bloquea la entrada
    And muestra error "La cantidad debe ser mayor a 0"
```

### Escenario 3: Borrador
```gherkin
  Scenario: Guardar para después
    When agrega items al carrito
    And hace clic en "Guardar Borrador"
    Then la solicitud NO se envía al admin todavía
    And queda guardada en "Mis Solicitudes" con estado "Borrador"
```

### Escenario 4: Verificar stock disponible en almacén (Opcional)
```gherkin
  Scenario: Pedir artículo agotado
    Given el Almacén Central tiene 0 existencias de "Gasas"
    When el usuario intenta pedir "Gasas"
    Then el sistema muestra advertencia: "Artículo agotado en Almacén Central"
    But permite enviarla (como backorder/pendiente) o la bloquea según configuración
```

### Escenario 5: Cancelar solicitud enviada
```gherkin
  Scenario: Arrepentimiento
    Given envió la solicitud hace 5 minutos
    And el estado sigue siendo "Pendiente"
    When selecciona "Cancelar Solicitud"
    Then el estado cambia a "Cancelada por Usuario"
    And desaparece de la lista de pendientes del Admin
```

### Escenario 6: Visualización de historial
```gherkin
  Scenario: Ver pedidos pasados
    When consulta el historial
    Then ve una lista de todas sus solicitudes
    And puede ver el detalle de qué pidió y qué se le entregó realmente
```
