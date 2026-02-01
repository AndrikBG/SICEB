# HU-010: Admin Recibe Notificaciones de Solicitudes

## Historia de Usuario

**Como** Administrador General  
**Quiero** recibir notificaciones automáticas cuando se envíen solicitudes de insumos  
**Para** poder revisarlas prontamente y no retrasar el abasto

---

## Conversación

**P1: ¿Dónde veo las notificaciones?**  
**R:** En la barra superior, ícono de "Campana" con un contador rojo. Y en el Dashboard principal, un widget de "Tareas Pendientes".

**P2: ¿Me llega mensaje externo?**  
**R:** No. Por ahora, todas las notificaciones son internas dentro de la plataforma (In-App).

**P3: ¿Qué info debe tener la notificación rápida?**  
**R:** "Servicio", "Usuario Solicitante", "Fecha/Hora" y "Prioridad".

**P4: ¿Si tengo 50 solicitudes, cómo me organizo?**  
**R:** Deben ordenarse por fecha (FIFO: First In, First Out) para atender a los que pidieron primero. O filtrar por "Urgentes".

**P5: ¿Desaparecen solas?**  
**R:** No, desaparecen (o se marcan leídas) hasta que entro a gestionar la solicitud (Aprobar/Rechazar).

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Recepción de notificación inmediata
```gherkin
Feature: Centro de Notificaciones
  Como Administrador
  Quiero enterarme de lo que pasa
  Para actuar rápido

  Background:
    Given el Admin está logueado en el sistema
    And no tiene notificaciones pendientes

  Scenario: Nueva solicitud entrante
    When el usuario de Urgencias envía la solicitud "SOL-001"
    Then el contador de notificaciones del Admin cambia a "1"
    And aparece un aviso emergente (Toast): "Nueva solicitud de Urgencias"
```

### Escenario 2: Widget de Dashboard
```gherkin
  Scenario: Tareas pendientes
    Given existen 3 solicitudes esperando aprobación
    When el Admin entra al Dashboard
    Then el widget "Solicitudes Pendientes" muestra el número "3"
    And lista las 3 solicitudes con su antigüedad (ej. "hace 2 horas")
```

### Escenario 3: Acceso directo desde notificación
```gherkin
  Scenario: Navegación rápida
    When hace clic en la notificación de la campanita
    Then el sistema lo redirige directamente al detalle de la solicitud "SOL-001"
    Para proceder a su revisión (HU-011)
```

### Escenario 4: Notificación visual
```gherkin
  Scenario: Aviso en pantalla
    When se genera la solicitud
    Then se muestra un banner temporal en la esquina de la pantalla del Admin
```

### Escenario 5: Marcar como leída
```gherkin
  Scenario: Limpieza de notificaciones
    Given tiene la notificación activa
    When entra a revisar la solicitud
    Then la notificación se marca automáticamente como LEÍDA
    And el contador rojo disminuye
```

### Escenario 6: Notificación de urgencia
```gherkin
  Scenario: Pedido prioritario
    Given Urgencias marcó su pedido como "Prioridad Alta"
    When llega la notificación
    Then se muestra con un ícono de "Fuego" o color Rojo
    And se posiciona al tope de la lista
```
