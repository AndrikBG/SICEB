# HU-020: Clasificar Pacientes y Aplicar Descuentos Automáticos

## Historia de Usuario

**Como** Personal de Recepción  
**Quiero** clasificar pacientes por tipo (Estudiante/Trabajador/Externo) y que el sistema aplique automáticamente el descuento correspondiente  
**Para** que la facturación sea consistente y automática (30% Estudiantes, 20% Trabajadores, 0% Externos)

---

## Conversación

**P1: ¿Cuáles son exactamente los descuentos?**  
**R:** 
- **Estudiantes de la institución:** 30% de descuento.
- **Trabajadores (Docentes/Admin):** 20% de descuento.
- **Externos (Público general):** 0% de descuento (pagan tarifa completa).

**P2: ¿El descuento aplica a todo?**  
**R:** Aplica a Consultas y Servicios Médicos. *No* aplica necesariamente a medicamentos de farmacia (eso se configura aparte), pero para esta historia asumamos que aplica a los servicios cobrados en recepción.

**P3: ¿Cómo validamos que sea estudiante o trabajador?**  
**R:** Debemos capturar su matrícula o número de empleado. El sistema debería (idealmente) validar contra una base de datos escolar, pero para este alcance, basta con registrar el número de credencial vigente.

**P4: ¿Se puede cambiar el tipo de paciente después?**  
**R:** Sí. Un alumno puede volverse egresado (Externo) o trabajador. Al cambiar su clasificación en el perfil, los *futuros* cobros se ajustarán. Los cobros pasados no cambian.

**P5: ¿Puede la cajera aplicar un descuento manual extra?**  
**R:** No en el flujo normal. Los descuentos deben ser automáticos por política para evitar abusos. Solo un Supervisor podría autorizar excepciones (otra historia).

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Registro de paciente Estudiante
```gherkin
Feature: Clasificación y Descuentos
  Como sistema de cobro
  Quiero aplicar descuentos según el reglamento
  Para asegurar cobros justos y correctos

  Background:
    Given el servicio "Consulta General" tiene precio base de $200.00

  Scenario: Cobro a Estudiante
    Given el paciente "Juan" está clasificado como "Estudiante"
    When Recepción genera un cobro por "Consulta General"
    Then el sistema aplica automáticamente el descuento del 30%
    And el subtotal es $200.00
    And el descuento es $60.00
    And el total a pagar es $140.00
```

### Escenario 2: Registro de paciente Trabajador
```gherkin
  Scenario: Cobro a Trabajador Docente
    Given el paciente "Profe Jirafales" está clasificado como "Trabajador"
    When Recepción genera un cobro por "Consulta General"
    Then el sistema aplica automáticamente el descuento del 20%
    And el subtotal es $200.00
    And el descuento es $40.00
    And el total a pagar es $160.00
```

### Escenario 3: Registro de paciente Externo
```gherkin
  Scenario: Cobro a Externo
    Given el paciente "Sr. Barriga" está clasificado como "Externo"
    When Recepción genera un cobro por "Consulta General"
    Then el sistema aplica 0% de descuento
    And el total a pagar es $200.00
```

### Escenario 4: Cambio de clasificación afecta nuevos cobros
```gherkin
  Scenario: Estudiante se gradúa (pasa a Externo)
    Given el paciente "María" era "Estudiante"
    When el usuario actualiza su perfil a "Externo"
    And genera un NUEVO cobro por "Consulta General"
    Then el sistema cobra el precio completo ($200.00) sin descuento
```

### Escenario 5: Validación de credencial para descuentos
```gherkin
  Scenario: Exigir matrícula para descuento
    When el usuario selecciona tipo de paciente "Estudiante"
    Then el campo "Matrícula/Credencial" se vuelve OBLIGATORIO
    If intenta guardar vacío
    Then el sistema muestra error "Debe capturar la matrícula para aplicar descuento de estudiante"
```

### Escenario 6: Desglose claro en el recibo
```gherkin
  Scenario: Recibo muestra ahorro
    Given se cobró una consulta a un estudiante
    When se imprime el recibo de pago
    Then el recibo muestra:
      | Concepto | Precio  |
      | Consulta | $200.00 |
      | Desc. Est| -$60.00 |
      | Total    | $140.00 |
```
