# HU-012: Encargado Recibe Notificación de Decisión

## Historia de Usuario

**Como** Encargado de Servicio  
**Quiero** recibir notificaciones automáticas cuando mi solicitud sea aprobada o rechazada  
**Para** conocer el estado de mi pedido sin tener que preguntar o revisar manualmente a cada rato

---

## Conversación

**P1: ¿Cómo me entero si me aprobaron?**  
**R:** Te llega una notificación en el sistema (campanita).

**P2: ¿La notificación dice qué pasó?**  
**R:** Sí. "Tu solicitud SOL-001 ha sido APROBADA". Si fue rechazada, dice "RECHAZADA" e incluye el motivo que escribió el admin.

**P3: ¿Si fue parcial, me avisa?**  
**R:** Sí. "Aprobada con modificaciones". Debes entrar a ver el detalle para saber qué te quitaron.

**P4: ¿Cuándo llega?**  
**R:** Inmediatamente después de que el Admin da clic en guardar decisión (HU-011).

**P5: ¿Puedo ir a recoger mis cosas entonces?**  
**R:** La notificación indica el siguiente paso. Ej: "Pasa a recoger al almacén" o "Espera la entrega".

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Aviso de aprobación
```gherkin
Feature: Feedback de Solicitudes
  Como solicitante
  Quiero saber la respuesta
  Para planear mi operación

  Background:
    Given mi solicitud "SOL-001" estaba pendiente

  Scenario: Respuesta positiva
    When el Admin aprueba la solicitud
    Then recibo una notificación interna: "¡Buenas noticias! Tu solicitud ha sido aprobada"
    And el ícono de estado cambia a Verde
```

### Escenario 2: Aviso de rechazo con motivo
```gherkin
  Scenario: Respuesta negativa
    When el Admin rechaza la solicitud con motivo "Presupuesto agotado"
    Then recibo una notificación alerta: "Solicitud Rechazada"
    And al abrirla, veo claramente el motivo: "Presupuesto agotado"
```

### Escenario 3: Notificación de ajuste (Parcial)
```gherkin
  Scenario: Aprobación modificada
    When el Admin cambia cantidad de 50 a 20
    Then la notificación dice: "Solicitud Aprobada Parcialmente"
    And me invita a revisar los detalles de los cambios
```

### Escenario 4: Mensaje interno
```gherkin
  Scenario: Respaldo en mensajería interna
    When se toma la decisión
    Then queda registrado en el historial de "Mis Mensajes" del sistema
    And contiene el resumen de la decisión
```

### Escenario 5: Actualización en tiempo real del dashboard
```gherkin
  Scenario: Cambio de estatus visual
    Given estoy viendo mi lista de solicitudes
    When el Admin toma la decisión en su computadora
    Then mi pantalla se actualiza (o al refrescar) mostrando el nuevo estado
```

### Escenario 6: Instrucciones siguientes
```gherkin
  Scenario: Flujo de recolección
    Given la solicitud fue aprobada
    Then la notificación incluye texto: "Tus insumos están listos para recolección en Almacén Central. Presenta tu folio SOL-001."
```
