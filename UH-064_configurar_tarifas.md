# HU-064: Configurar Tarifas por Servicio Médico

## Historia de Usuario

**Como** Administrador  
**Quiero** configurar tarifas por servicio médico especificando precio base  
**Para** que el sistema calcule los cobros correctamente de forma automática

---

## Conversación

**P1: ¿Quién define los precios de los servicios?**  
**R:** Solo el Administrador General o el Director tienen permisos para crear o modificar las listas de precios.

**P2: ¿Se pueden manejar servicios gratuitos?**  
**R:** Sí, es posible asignar un precio de $0.00 para ciertos servicios públicos o campañas de salud, y el sistema debe procesarlos normalmente (generando un recibo por $0).

**P3: ¿Qué pasa si cambiamos un precio hoy? ¿Afecta a cobros pasados?**  
**R:** No. El cambio solo aplica para nuevos cobros generados a partir del momento del cambio. Los registros históricos deben mantener el precio que tenían cuando se realizó el cobro.

**P4: ¿El precio incluye impuestos?**  
**R:** Sí, los precios configurados son finales.

**P5: ¿Podemos tener diferentes tarifas para el mismo servicio (ej. tarifa empleado vs público)?**  
**R:** La tarifa base es única por servicio. Los ajustes para empleados o estudiantes se manejan mediante descuentos automáticos (ver HU-020), no creando múltiples tarifas para lo mismo.

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Registrar una nueva tarifa de servicio
```gherkin
Feature: Gestión de Tarifas
  Como Administrador
  Quiero definir los costos de los servicios
  Para mantener el catálogo de precios actualizado

  Background:
    Given el usuario "admin" ha iniciado sesión
    And navega a "Configuración de Tarifas"

  Scenario: Crear tarifa para nuevo servicio
    When hace clic en "Nueva Tarifa"
    And ingresa los datos:
      | Servicio   | Consulta Nutrición |
      | Código     | NUT-001            |
      | Precio     | 350.00             |
    And guarda el registro
    Then el servicio "Consulta Nutrición" aparece disponible para cobro con precio $350.00
```

### Escenario 2: Actualizar precio de servicio existente
```gherkin
  Scenario: Aumento de precio por inflación
    Given existe el servicio "Consulta General" con precio actual de $200.00
    When el usuario edita el servicio "Consulta General"
    And cambia el precio a $250.00
    And guarda los cambios
    Then el sistema actualiza el precio para futuros cobros
    And muestra un mensaje de confirmación "Tarifa actualizada correctamente"
```

### Escenario 3: Validación de precios no negativos
```gherkin
  Scenario: Intentar asignar precio negativo
    When el usuario intenta crear una tarifa con precio "-50.00"
    Then el sistema muestra un error "El precio no puede ser negativo"
    And no permite guardar el registro
```

### Escenario 4: Configuración de servicio gratuito
```gherkin
  Scenario: Configurar campaña de vacunación gratuita
    When el usuario crea el servicio "Aplicación Vacuna Influenza"
    And ingresa el precio "$0.00"
    And guarda el registro
    Then el sistema acepta el precio
    And al cobrar este servicio, el total a pagar es $0.00
```

### Escenario 5: Búsqueda de tarifa en catálogo
```gherkin
  Scenario: Buscar precio de un servicio específico
    Given existen 50 servicios registrados
    When el usuario busca "Dental" en la barra de búsqueda
    Then el sistema filtra la lista
    And muestra "Limpieza Dental - $400.00" y "Extracción Dental - $600.00"
```

### Escenario 6: Integración con módulo de cobro
```gherkin
  Scenario: Recepción visualiza precio actualizado
    Given el Admin acaba de cambiar el precio de "Curación" a $150.00
    When el usuario "recepcion" selecciona "Curación" para cobrar a un paciente
    Then el sistema carga automáticamente el precio de $150.00
    And el campo de precio aparece bloqueado (no editable por recepción)
```
