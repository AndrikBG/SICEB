# HU-023: Validar Tutor Presente para Menores

## Historia de Usuario

**Como** Personal de Recepción  
**Quiero** que el sistema valide que esté presente un tutor para pacientes menores de 17 años  
**Para** cumplir con los requisitos legales y las políticas de protección al menor de la clínica

---

## Conversación

**P1: ¿Cuál es la edad límite para exigir tutor?**  
**R:** Menores de 17 años (según nuestra nueva política). Todo paciente de 0 a 16 años y 11 meses requiere tutor. A partir de los 17 (aunque legalmente son menores hasta los 18, para la clínica se consideran aptos para consulta simple), el sistema es más flexible, pero para <17 es estricto.

**P2: ¿Qué datos necesitamos del tutor?**  
**R:** Nombre completo, parentesco (padre, madre, abuelo, etc.) y una identificación oficial (INE/Pasaporte).

**P3: ¿El sistema bloquea el registro si no hay tutor?**  
**R:** Si la fecha de nacimiento indica que tiene menos de 17 años, el sistema DEBE bloquear el guardado hasta que se llenen los campos del Tutor. **EXCEPCIÓN:** En "Casos Especiales" autorizados, un supervisor puede desbloquear el registro sin tutor (ej. emergencias o menors emancipados/situación de calle identificada).

**P4: ¿Puede ser tutor otro menor de edad?**  
**R:** No. El tutor debe ser mayor de 18 años.

**P5: ¿Esta información se guarda en el expediente?**  
**R:** Sí, en la carátula del expediente debe decir claramente "Menor de edad - Tutor responsable: [Nombre]".

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Registro de menor requiere tutor obligatoriamente
```gherkin
Feature: Protección del Menor
  Como sistema
  Quiero asegurar que todo niño tenga un responsable
  Para cumplimiento legal

  Background:
    Given la fecha actual es 2026

  Scenario: Detectar menor de 17 años
    Given el usuario registra un paciente de 16 años
    When llena los datos del paciente
    Then el sistema despliega automáticamente la sección "Datos del Tutor"
    And marca estos campos como OBLIGATORIOS
```

### Escenario 2: Bloqueo de guardado sin tutor
```gherkin
  Scenario: Intentar guardar menor sin responsable
    Given es un paciente de 16 años
    When el usuario intenta guardar dejando vacía la sección del tutor
    Then el sistema impide guardar
    And muestra el error "Paciente menor de 17 años requiere registrar un tutor responsable"
```

### Escenario 3: Registro exitoso con tutor
```gherkin
  Scenario: Registrar menor con su madre
    Given es un paciente de 10 años
    When el usuario llena los datos del niño
    And ingresa en Tutor:
      | Nombre    | Laura Martínez |
      | Parentesco| Madre          |
      | Teléfono  | 5599887766     |
    And hace clic en Guardar
    Then el sistema registra exitosamente al paciente
    And vincula a "Laura Martínez" como contacto de emergencia principal
```

### Escenario 4: Excepción para casos especiales (Sin Tutor)
```gherkin
  Scenario: Atención a menor en situación especial sin tutor
    Given es un paciente de 15 años sin tutor presente
    When el usuario activa el checkbox "Caso Especial / Sin Tutor"
    Then el sistema solicita "Clave de Supervisor" para autorizar
    When ingresa la clave correcta
    Then permite guardar el registro sin datos de tutor
    And marca el expediente con alerta "MENOR SIN TUTOR - CASO ESPECIAL"
```

### Escenario 5: Validación de edad del tutor (manual)
```gherkin
  Scenario: Sistema pide confirmar mayoría de edad del tutor
    When se registra el tutor
    Then el sistema muestra un checkbox obligatorio: "[ ] Confirmo que el tutor presentó identificación oficial y es mayor de edad"
    If no se marca
    Then no permite guardar
```

### Escenario 6: Visualización en Expediente
```gherkin
  Scenario: Alerta visual en expediente
    Given se registró al paciente "Pepito" (14 años)
    When el médico abre el expediente
    Then aparece una etiqueta visible: "PACIENTE MENOR DE EDAD (<17)"
```

