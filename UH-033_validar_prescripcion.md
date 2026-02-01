# HU-033: Validar Prescripción antes de Dispensar

## Historia de Usuario

**Como** Personal de Farmacia  
**Quiero** que el sistema valide que existe una prescripción antes de dispensar y verifique existencias  
**Para** no dispensar sin orden médica y asegurar el control de inventario

---

## Conversación

**P1: ¿Puedo vender medicina sin receta?**  
**R:** El sistema está diseñado para la clínica interna. Todo lo que sale de esta farmacia debe estar ligado a una Consulta/Receta (HU-031). Si es venta al público externo sin consulta, sería otro proceso, pero por ahora asumimos flujo interno: No Receta = No Medicamento.

**P2: ¿Qué valida el sistema al momento de surtir?**  
**R:** 
1. Que la receta exista y sea válida.
2. Que el medicamento seleccionado corresponda a lo recetado.
3. Que haya stock suficiente en el inventario.

**P3: ¿El sistema descuenta del inventario al momento?**  
**R:** Sí. Al confirmar la entrega, se resta del stock (HU-005/006 se actualizan).

**P4: ¿Se puede cambiar un medicamento por un equivalente (similiar)?**  
**R:** Sí, pero el sistema debe registrar el cambio ("Sustitución por bioequivalente") y el farmacéutico debe confirmarlo.

**P5: ¿Qué pasa si intento dispensar dos veces la misma receta?**  
**R:** El sistema bloquea. Una vez marcada como "Entregada Total", no permite volver a surtirla.

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Validación de existencia en receta
```gherkin
Feature: Control de Dispensación
  Como sistema de seguridad
  Quiero evitar salidas no autorizadas
  Para proteger el inventario y la salud

  Background:
    Given la receta "REC-100" incluye "Ibuprofeno 400mg"

  Scenario: Dispensar lo correcto
    When el farmacéutico escanea el código de barras de una caja de "Ibuprofeno 400mg"
    Then el sistema marca la línea como "Listo para entregar" (Check verde)
    And verifica que la cantidad sea la solicitada
```

### Escenario 2: Intento de dispensar medicamento no recetado
```gherkin
  Scenario: Error al surtir
    When el farmacéutico escanea "Paracetamol" (no está en la receta)
    Then el sistema emite un sonido de error
    And muestra alerta "ESTE ARTÍCULO NO CORRESPONDE A LA RECETA"
    And bloquea la salida del producto
```

### Escenario 3: Bloqueo por falta de stock
```gherkin
  Scenario: Inventario insuficiente
    Given la receta pide 5 cajas de "Insulina"
    And el inventario solo tiene 2 cajas
    When intenta procesar la salida de 5
    Then el sistema alerta "Stock insuficiente (Disponible: 2)"
    And ofrece la opción "Surtir Parcial (2)"
```

### Escenario 4: Prevención de doble surtido
```gherkin
  Scenario: Receta ya surtida
    Given la receta "REC-100" ya tiene estado "Entregada"
    When alguien intenta volver a procesarla
    Then el sistema muestra error "Esta receta ya fue surtida el [Fecha/Hora]"
    And no permite sacar más insumos
```

### Escenario 5: Validación de caducidad al dispensar (Cross-check)
```gherkin
  Scenario: Alerta FEFO (First Expired, First Out)
    Given hay un lote A que caduca en un mes y un lote B que caduca en un año
    When el farmacéutico escanea el lote B
    Then el sistema sugiere: "Alerta: Existe un lote (Lote A) con caducidad más próxima. Se recomienda usar ese primero."
    And pide confirmación para usar el Lote B de todos modos
```

### Escenario 6: Descuento automático de inventario
```gherkin
  Scenario: Actualización de kardex
    Given el artículo "Gasas" tiene 100 unidades
    When se confirma la dispensación de 10 unidades
    Then el inventario se actualiza a 90 unidades inmediatamente
    And se genera un movimiento de salida tipo "Dispensación a Paciente"
```
