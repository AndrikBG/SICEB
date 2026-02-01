# HU-005: Encargado de Servicio Ve SOLO Su Inventario

## Historia de Usuario

**Como** Encargado de Servicio  
**Quiero** ver SOLO el inventario de mi servicio  
**Para** gestionar los recursos de mi área efectivamente

## Conversación 

**P1: ¿Cómo sabe el sistema qué inventario mostrar al Encargado?**  
**R:** El sistema identifica el servicio al que pertenece el Encargado al iniciar sesión y filtra automáticamente el inventario para mostrar únicamente los artículos correspondientes a su área.

**P2: ¿Puede el Encargado ver inventario de otros servicios aunque sea de solo lectura?**  
**R:** No. No debe tener ninguna visibilidad de otros servicios. Esto es un requisito estricto para mantener la separación de datos entre áreas.

**P3: ¿Qué pasa si un Encargado intenta acceder directamente a la información de otro servicio?**  
**R:** El sistema detecta que el usuario no tiene permisos para ver esa información, deniega el acceso y registra el intento en el historial de seguridad.

**P4: ¿El Encargado ve las mismas columnas que el Admin?**  
**R:** Sí, ve la misma información pero filtrada: Código, Nombre, Cantidad, Stock Mínimo, Fecha de Caducidad y Estado.

**P5: ¿Puede el Encargado editar cantidades de inventario?**  
**R:** No directamente. Solo puede:
- Ver su inventario
- Solicitar insumos (HU-009)
- Confirmar recepciones (HU-013)
Las cantidades se actualizan automáticamente por el sistema.

**P6: ¿El Encargado puede exportar su inventario?**  
**R:** Sí, pero solo de su servicio. El archivo exportado contiene únicamente los artículos de su área.

**P7: ¿Si un médico pertenece a un servicio, ve el inventario de ese servicio?**  
**R:** Sí. Los Médicos y Residentes asignados a un servicio ven el mismo inventario que su Encargado, en modo de solo lectura.

**P8: ¿Qué ve el Encargado si su servicio no tiene artículos aún?**  
**R:** El sistema muestra un mensaje indicando: "Su servicio aún no tiene artículos en inventario. Contacte al Administrador para configuración inicial."

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Encargado ve solo su inventario
```gherkin
Feature: Visibilidad de Inventario por Servicio
  Como Encargado de Servicio
  Quiero ver solo el inventario de mi servicio
  Para gestionar mis recursos sin acceder a información de otras áreas

  Background:
    Given existen los siguientes artículos en el sistema:
      | SKU   | Nombre           | Servicio   | Cantidad |
      | PED01 | Abatelenguas     | Pediatría  | 500      |
      | PED02 | Termómetro       | Pediatría  | 10       |
      | ADM01 | Papel Bond       | Admin      | 100      |
      | CIR01 | Bisturí          | Cirugía    | 50       |
    And el usuario "encargado_pediatria" ha iniciado sesión con rol "Encargado de Servicio" y servicio "Pediatría"

  Scenario: Visualización predeterminada del inventario
    When el usuario navega a la sección de "Inventario"
    Then el sistema muestra una lista de artículos
    And la lista contiene el artículo "Abatelenguas"
    And la lista contiene el artículo "Termómetro"
    But la lista NO contiene el artículo "Papel Bond"
    And la lista NO contiene el artículo "Bisturí"
```

### Escenario 2: Intento de acceso a inventario de otro servicio
```gherkin
  Scenario: Bloqueo de acceso directo a recursos de otro servicio
    When el usuario intenta acceder directamente a la URL "/api/inventario/Cirugía"
    Then el sistema responde con un error 403 Forbidden
    And se muestra un mensaje "No tiene permisos para acceder a este recurso"
    And se registra el intento de acceso no autorizado en el log de auditoría
```

### Escenario 3: Exportación de inventario
```gherkin
  Scenario: Exportar inventario a Excel
    When el usuario hace clic en el botón "Exportar Inventario"
    Then se descarga un archivo Excel
    And el archivo contiene las filas correspondientes a "Abatelenguas" y "Termómetro"
    But el archivo NO contiene filas para "Papel Bond" o "Bisturí"
```

### Escenario 4: Visualización de detalles de artículo
```gherkin
  Scenario: Ver detalles completos de un artículo propio
    When el usuario selecciona el artículo "PED01" (Abatelenguas)
    Then el sistema muestra el detalle completo del artículo:
      | Campo            | Valor        |
      | SKU              | PED01        |
      | Nombre           | Abatelenguas |
      | Cantidad Actual  | 500          |
      | Stock Mínimo     | 100          |
      | Fecha Caducidad  | N/A          |
```
