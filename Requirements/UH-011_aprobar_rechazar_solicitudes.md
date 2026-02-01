# HU-011: Aprobar o Rechazar Solicitudes de Insumos

## Historia de Usuario

**Como** Administrador General  
**Quiero** aprobar o rechazar solicitudes de insumos con justificación  
**Para** que las decisiones queden documentadas y se controle el gasto

---

## Conversación

**P1: ¿Puedo aprobar parcialmente?**  
**R:** Sí. Si piden 50 cajas y solo quiero darles 20 (porque hay poco stock o piden demasiado), puedo editar la "Cantidad Aprobada" antes de confirmar.

**P2: ¿Es obligatoria la justificación al rechazar?**  
**R:** Sí. Si rechazo o modifico la cantidad, debo escribir por qué (ej. "Presupuesto excedido", "No hay stock"). Al aprobar tal cual, la justificación es opcional.

**P3: ¿Qué pasa con el inventario cuando apruebo?**  
**R:** Se aparta (reserva) del Almacén Central, pero no se descuenta definitivamente hasta que se marca como "Entregado" (HU-013, fuera de esta fase pero parte del flujo). O bien, se descuenta de Almacén y pasa a "En Tránsito". Asumamos que al aprobar se genera la orden de salida.

**P4: ¿Puedo rechazar línea por línea?**  
**R:** Sí. Puedo aprobar los Guantes pero rechazar las Jeringas en la misma solicitud.

**P5: ¿Queda registro de quién aprobó?**  
**R:** Siempre. Usuario, Fecha y Hora de la decisión.

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Aprobación total
```gherkin
Feature: Gestión de Suministros
  Como Administrador
  Quiero autorizar pedidos
  Para abastecer a las áreas

  Background:
    Given revisando la solicitud "SOL-001" que pide 10 Cajas de Guantes

  Scenario: Autorizar todo
    When el Admin revisa que hay stock suficiente
    And hace clic en "Aprobar Solicitud Completa"
    Then el estado cambia a "Aprobada"
    And se genera la orden de salida de almacén por 10 Cajas
```

### Escenario 2: Modificación de cantidad (Aprobación Parcial)
```gherkin
  Scenario: Recorte de presupuesto
    Given piden 50 "Batas Desechables"
    But solo quedan 30 en almacén
    When el Admin cambia la "Cantidad Aprobada" a 30
    And agrega nota: "Surtido parcial por falta de stock"
    And confirma la aprobación
    Then la solicitud se marca como "Aprobada Parcialmente"
    And se notifica al solicitante el cambio
```

### Escenario 3: Rechazo total con justificación
```gherkin
  Scenario: Negar pedido
    When el Admin decide que el pedido es innecesario
    And hace clic en "Rechazar"
    Then el sistema exige un motivo
    When escribe "Solicitud duplicada, ya se surtió ayer"
    And confirma
    Then el estado cambia a "Rechazada"
    And el proceso termina ahí
```

### Escenario 4: Auditoría de decisión
```gherkin
  Scenario: Trazabilidad
    When se aprueba la solicitud
    Then el sistema guarda: "Aprobado por: Admin Juan, Fecha: [Hoy]"
    Para que no haya dudas de quién autorizó la salida
```

### Escenario 5: Validación de stock al aprobar
```gherkin
  Scenario: Intentar dar lo que no se tiene
    Given almacén tiene 5 piezas
    When el Admin intenta aprobar manualmente 10 piezas
    Then el sistema muestra error "No puedes aprobar más de lo que tienes en existencia (Disp: 5)"
```

### Escenario 6: Rechazo irreversible
```gherkin
  Scenario: Cambiar de opinión
    Given una solicitud fue "Rechazada"
    When el Admin intenta "Reactivarla"
    Then el sistema NO lo permite (deben crear una nueva solicitud)
```
