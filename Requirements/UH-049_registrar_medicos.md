# HU-049: Registrar Personal Médico

## Historia de Usuario

**Como** Administrador  
**Quiero** registrar personal médico (adscritos y residentes R1-R4)  
**Para** que el personal esté en el sistema y se le asignen sus responsabilidades clínicas correctas

---

## Conversación

**P1: ¿Qué diferencia hay entre registrar un Médico Adscrito y un Residente?**  
**R:** Ambos son médicos, pero el Residente requiere que especifiquemos su nivel (R1, R2, R3, R4) ya que esto determinará sus restricciones en el sistema (ej. bloqueo de recetas controladas para R1). El Médico Adscrito tiene privilegios completos.

**P2: ¿Es obligatorio registrar la Cédula Profesional?**  
**R:** Sí, es un requisito legal indispensable para que puedan firmar recetas y expedientes. El sistema no debe permitir guardar sin cédula.

**P3: ¿Se les asigna un servicio al crearlos?**  
**R:** Sí, todo personal médico debe estar vinculado a un Servicio (Pediatría, Ginecología, etc.). Esto define qué inventarios ven y qué pacientes gestionan principalmente.

**P4: ¿Un médico puede pertenecer a dos servicios?**  
**R:** No en esta versión. Se asignan a su servicio principal. Si rotan (caso de residentes), el Admin debe actualizar su servicio manualmente.

**P5: ¿Qué datos personales necesitamos?**  
**R:** Nombre completo, CURP, RFC, Email institucional, Teléfono de contacto y Universidad de procedencia (para expediente de RRHH).

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Registrar Médico Adscrito (Especialista)
```gherkin
Feature: Registro de Personal Médico
  Como Administrador
  Quiero dar de alta médicos y residentes
  Para habilitar su operación en la clínica

  Background:
    Given el usuario "admin" ha iniciado sesión
    And navega a "Gestión de Usuarios > Personal Médico"

  Scenario: Alta de Médico Adscrito
    When selecciona "Nuevo Médico"
    And ingresa los datos:
      | Nombre    | Dr. Juan Pérez        |
      | Tipo      | Médico Adscrito       |
      | Cédula    | 12345678              |
      | Servicio  | Cardiología           |
      | Email     | jperez@hospital.mx    |
    And guarda el registro
    Then el sistema crea la cuenta de usuario
    And asigna permisos totales de prescripción
```

### Escenario 2: Registrar Residente R1 (Nivel inicial)
```gherkin
  Scenario: Alta de Residente R1 con restricciones
    When selecciona "Nuevo Médico"
    And ingresa los datos:
      | Nombre    | Dra. Ana López        |
      | Tipo      | Residente             |
      | Grado     | R1                    |
      | Cédula    | PENDIENTE-TRAMITE     |
      | Servicio  | Urgencias             |
    And guarda el registro
    Then el sistema crea la cuenta
    And configura restricciones de seguridad (bloqueo de controlados)
    And marca el perfil con "Supervisión Requerida"
```

### Escenario 3: Validación de Cédula duplicada
```gherkin
  Scenario: Evitar duplicidad de personal
    Given ya existe un médico con cédula "888888"
    When intenta registrar otro médico con cédula "888888"
    Then el sistema muestra error "La cédula profesional ya está registrada"
    And no permite guardar el duplicado
```

### Escenario 4: Asignación de servicio obligatoria
```gherkin
  Scenario: Intentar guardar sin servicio
    When ingresa los datos del médico
    But deja el campo "Servicio" vacío
    And intenta guardar
    Then el sistema marca el campo "Servicio" en rojo
    And muestra mensaje "Debe asignar un servicio clínico"
```

### Escenario 5: Actualización de grado de Residente
```gherkin
  Scenario: Promoción de residente de R1 a R2
    Given el usuario "residente_ana" es R1 actualmente
    When el Admin edita su perfil
    And cambia el grado de "R1" a "R2"
    And guarda los cambios
    Then el sistema actualiza sus permisos automáticamente
    And registra el cambio de grado en el historial
```

### Escenario 6: Baja de personal médico
```gherkin
  Scenario: Desactivar cuenta de médico que renuncia
    Given el médico "Dr. House" deja de laborar
    When el Admin cambia su estado a "Inactivo"
    Then el usuario "drhouse" ya no puede iniciar sesión
    But sus recetas y notas previas permanecen inalteradas en el sistema (integridad histórica)
```
