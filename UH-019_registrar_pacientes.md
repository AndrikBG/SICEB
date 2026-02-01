# HU-019: Registrar Pacientes Nuevos

## Historia de Usuario

**Como** Personal de Recepción  
**Quiero** registrar pacientes nuevos con su información demográfica  
**Para** que puedan recibir atención médica y tener un historial clínico

---

## Conversación

**P1: ¿Qué datos son obligatorios para registrar un paciente?**  
**R:** Necesitamos obligatoriamente: Nombre(s), Apellido Paterno, Apellido Materno, Fecha de Nacimiento (para calcular edad), Género y un Teléfono de contacto.

**P2: ¿El CURP es obligatorio?**  
**R:** Idealmente sí, pero si el paciente no lo conoce en ese momento (ej. urgencia o extranjero), podemos dejarlo pendiente. El sistema debería marcar el registro como "Incompleto" hasta que se llenen esos datos, pero permitir la atención médica.

**P3: ¿Qué pasa si el paciente ya existe?**  
**R:** Antes de guardar, el sistema debe verificar duplicados por Nombre Completo + Fecha de Nacimiento. Si encuentra una coincidencia, debe mostrar una alerta: "Posible paciente duplicado: [Datos del existente]" y preguntar si deseamos usar ese o crear uno nuevo (homónimos).

**P4: ¿Se genera un número de expediente automático?**  
**R:** Sí. El usuario de recepción NO inventa el número. El sistema asigna un ID único secuencial o con formato específico (ej. AAAA-MM-XXXX) al guardar con éxito.

**P5: ¿Podemos tomar foto del paciente?**  
**R:** Sí, sería excelente para identificación visual en seguridad, pero es opcional.

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Registro exitoso de paciente con datos completos
```gherkin
Feature: Registro de Pacientes
  Como recepcionista
  Quiero dar de alta a una persona en el sistema
  Para iniciar su proceso de atención

  Background:
    Given el usuario "recep_ana" está en la pantalla "Nuevo Paciente"

  Scenario: Registro estándar
    When ingresa los datos:
      | Nombre           | Roberto                  |
      | Ap. Paterno      | Gómez                    |
      | Ap. Materno      | Bolaños                  |
      | Fecha Nacimiento | 21/02/1929               |
      | Género           | Masculino                |
      | Teléfono         | 5512345678               |
    And hace clic en "Guardar"
    Then el sistema registra al paciente
    And asigna un Número de Expediente único
    And muestra mensaje "Paciente registrado exitosamente"
```

### Escenario 2: Detección de posibles duplicados
```gherkin
  Scenario: Alerta de homónimo
    Given ya existe un paciente "Roberto Gómez Bolaños" nacido el "21/02/1929"
    When el usuario intenta registrar otro paciente con los MISMOS datos
    Then el sistema muestra la alerta "Posible paciente duplicado encontrado"
    And muestra los datos del paciente existente para comparar
    And permite elegir "Es la misma persona" (abrir expediente) o "Es otra persona" (crear nuevo)
```

### Escenario 3: Validación de campos obligatorios
```gherkin
  Scenario: Intentar guardar sin nombre
    When deja el campo "Nombre" vacío
    And hace clic en "Guardar"
    Then el sistema muestra el error "El nombre es obligatorio"
    And no crea el registro
```

### Escenario 4: Cálculo automático de edad
```gherkin
  Scenario: Visualizar edad al ingresar fecha de nacimiento
    When el usuario ingresa Fecha de Nacimiento "01/01/2000"
    Then el sistema calcula y muestra automáticamente "Edad: 26 años" (dependiendo fecha actual)
    And si ingresa "01/01/2023", muestra "Edad: 3 años"
```

### Escenario 5: Registro de paciente extranjero (sin CURP)
```gherkin
  Scenario: Paciente sin documentos nacionales
    When ingresa los datos demográficos básicos
    But deja el campo "CURP" vacío
    And marca la casilla "Extranjero / Sin CURP"
    And guarda el registro
    Then el sistema permite guardar
    But marca el perfil con la etiqueta "Datos Fiscales Incompletos"
```

### Escenario 6: Validación de formato de teléfono
```gherkin
  Scenario: Ingresar teléfono inválido
    When ingresa "123" en el campo Teléfono
    And intenta guardar
    Then el sistema muestra error "El teléfono debe tener 10 dígitos"
```
