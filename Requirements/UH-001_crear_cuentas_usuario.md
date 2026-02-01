# HU-001: Crear Cuentas de Usuario con Permisos Basados en Roles

## Historia de Usuario

**Como** Administrador  
**Quiero** crear cuentas de usuario con permisos basados en roles  
**Para que** el personal pueda acceder al sistema según sus responsabilidades

---

## Conversación

**P1: ¿Qué roles necesitamos soportar inicialmente?**  
**R:** Necesitamos 9 roles:
1. **Director General** - Acceso total, reportes estratégicos
2. **Administrador General** - Gestión completa del sistema
3. **Encargado de Servicio** - Gestión de su servicio específico
4. **Médico Adscrito** - Atención médica completa sin restricciones
5. **Residente R4** - Atención médica con supervisión mínima
6. **Residente R3** - Atención médica con algunas restricciones
7. **Residente R2** - Atención médica con restricciones moderadas
8. **Residente R1** - Atención médica con máximas restricciones
9. **Personal de Recepción** - Registro de pacientes y cobros

**P2: ¿Un usuario puede tener múltiples roles?**  
**R:** No en el MVP. Cada usuario tiene UN solo rol. Si alguien necesita capacidades de dos roles, se asigna el rol con más permisos.

**P3: ¿Qué información mínima necesitamos para crear un usuario?**  
**R:** 
- Nombre completo (obligatorio)
- Correo electrónico institucional (obligatorio, único)
- Rol (obligatorio, selección de lista)
- Nombre de usuario (obligatorio, único, alfanumérico sin espacios)
- Contraseña inicial (obligatoria, mínimo 8 caracteres)
- Servicio al que pertenece (obligatorio solo para Encargado de Servicio, Médicos y Residentes)
- Estado (Activo/Inactivo)

**P4: ¿Cómo se asigna la contraseña inicial?**  
**R:** El Administrador ingresa una contraseña temporal. El usuario estará obligado a cambiarla en su primer inicio de sesión (ver HU-002).

**P5: ¿Qué pasa si intento crear un usuario con correo o nombre de usuario duplicado?**  
**R:** El sistema debe mostrar un error claro indicando que el correo o el nombre de usuario ya existen en el sistema.

**P6: ¿Puedo editar el rol de un usuario después de crearlo?**  
**R:** Sí, pero con restricciones:
- Solo el Administrador General puede cambiar roles
- El cambio debe quedar registrado en la auditoría (quién, cuándo, rol anterior, rol nuevo)
- El cambio es efectivo inmediatamente (el usuario verá las nuevas opciones en su próximo acceso)

**P7: ¿Qué pasa con usuarios inactivos?**  
**R:** No pueden iniciar sesión. Se marca como "inactivo" pero NO se elimina del sistema, ya que necesitamos mantener el historial de sus actividades.

**P8: ¿Necesitamos validar el formato del correo electrónico?**  
**R:** Sí, debe ser un formato de correo válido (contener @) y preferiblemente del dominio de la clínica, aunque esto último no es restrictivo.
---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Crear usuario exitosamente con rol básico

```gherkin
Feature: Creación de Cuentas de Usuario
  Como Administrador
  Quiero crear cuentas de usuario con roles específicos
  Para que el personal pueda acceder al sistema

  Background:
    Given el Administrador ha iniciado sesión
    And está en la pantalla "Gestión de Usuarios"

  Scenario: Crear usuario de recepción exitosamente
    Given el Administrador hace clic en "Nuevo Usuario"
    When ingresa los siguientes datos:
      | Campo            | Valor                          |
      | Nombre Completo  | María García López             |
      | Email            | mgarcia@clinicabienestar.mx    |
      | Usuario          | mgarcia                        |
      | Contraseña       | Temporal123                    |
      | Rol              | Personal de Recepción          |
      | Estado           | Activo                         |
    And hace clic en "Guardar"
    Then el sistema muestra el mensaje "Usuario creado exitosamente"
    And el usuario "mgarcia" aparece en la lista de usuarios
    And el rol mostrado es "Personal de Recepción"
    And el estado mostrado es "Activo"
```

### Escenario 2: Crear usuario médico con asignación de servicio

```gherkin
  Scenario: Crear médico residente R2 con servicio asignado
    Given el Administrador hace clic en "Nuevo Usuario"
    When ingresa los siguientes datos:
      | Campo            | Valor                          |
      | Nombre Completo  | Dr. Carlos Ramírez Soto        |
      | Email            | cramirez@clinicabienestar.mx   |
      | Usuario          | cramirez                       |
      | Contraseña       | Residente2024                  |
      | Rol              | Residente R2                   |
    Then el sistema muestra el campo "Servicio" como obligatorio
    When selecciona "Pediatría" en el campo Servicio
    And hace clic en "Guardar"
    Then el sistema muestra el mensaje "Usuario creado exitosamente"
    And el usuario "cramirez" aparece con servicio "Pediatría"
```

### Escenario 3: Error por email duplicado

```gherkin
  Scenario: Intentar crear usuario con email existente
    Given existe un usuario con email "mgarcia@clinicabienestar.mx"
    And el Administrador hace clic en "Nuevo Usuario"
    When ingresa los siguientes datos:
      | Campo            | Valor                          |
      | Nombre Completo  | María Guadalupe García         |
      | Email            | mgarcia@clinicabienestar.mx    |
      | Usuario          | mggarcia                       |
      | Contraseña       | Temporal123                    |
      | Rol              | Personal de Recepción          |
    And hace clic en "Guardar"
    Then el sistema muestra el error "El email ya existe en el sistema"
    And el usuario NO es creado
    And permanece en el formulario de creación con datos ingresados
```

### Escenario 4: Error por username duplicado

```gherkin
  Scenario: Intentar crear usuario con username existente
    Given existe un usuario con username "mgarcia"
    And el Administrador hace clic en "Nuevo Usuario"
    When ingresa los siguientes datos:
      | Campo            | Valor                          |
      | Nombre Completo  | Manuel García Pérez            |
      | Email            | manuelgarcia@clinicabienestar.mx |
      | Usuario          | mgarcia                        |
      | Contraseña       | Temporal123                    |
      | Rol              | Personal de Recepción          |
    And hace clic en "Guardar"
    Then el sistema muestra el error "El nombre de usuario ya está en uso"
    And el usuario NO es creado
```

### Escenario 5: Validación de contraseña débil

```gherkin
  Scenario: Intentar crear usuario con contraseña menor a 8 caracteres
    Given el Administrador hace clic en "Nuevo Usuario"
    When ingresa los siguientes datos:
      | Campo            | Valor                          |
      | Nombre Completo  | Pedro López Ruiz               |
      | Email            | plopez@clinicabienestar.mx     |
      | Usuario          | plopez                         |
      | Contraseña       | Temp12                         |
      | Rol              | Personal de Recepción          |
    And hace clic en "Guardar"
    Then el sistema muestra el error "La contraseña debe tener mínimo 8 caracteres"
    And el usuario NO es creado
```

### Escenario 6: Validación de contraseña sin números

```gherkin
  Scenario: Intentar crear usuario con contraseña solo letras
    Given el Administrador hace clic en "Nuevo Usuario"
    When ingresa los siguientes datos:
      | Campo            | Valor                          |
      | Nombre Completo  | Pedro López Ruiz               |
      | Email            | plopez@clinicabienestar.mx     |
      | Usuario          | plopez                         |
      | Contraseña       | Temporal                       |
      | Rol              | Personal de Recepción          |
    And hace clic en "Guardar"
    Then el sistema muestra el error "La contraseña debe contener al menos un número"
    And el usuario NO es creado
```

### Escenario 7: Validación de email inválido

```gherkin
  Scenario: Intentar crear usuario con email sin formato válido
    Given el Administrador hace clic en "Nuevo Usuario"
    When ingresa los siguientes datos:
      | Campo            | Valor                          |
      | Nombre Completo  | Ana Martínez Flores            |
      | Email            | anamartinez.com                |
      | Usuario          | amartinez                      |
      | Contraseña       | Temporal123                    |
      | Rol              | Personal de Recepción          |
    And hace clic en "Guardar"
    Then el sistema muestra el error "Ingrese un email válido"
    And el campo "Email" se marca en rojo
    And el usuario NO es creado
```

### Escenario 8: Validación de username con caracteres especiales

```gherkin
  Scenario: Intentar crear usuario con caracteres especiales en username
    Given el Administrador hace clic en "Nuevo Usuario"
    When ingresa los siguientes datos:
      | Campo            | Valor                          |
      | Nombre Completo  | José Luis Hernández            |
      | Email            | jlhernandez@clinicabienestar.mx |
      | Usuario          | j.luis@hernandez               |
      | Contraseña       | Temporal123                    |
      | Rol              | Personal de Recepción          |
    And hace clic en "Guardar"
    Then el sistema muestra el error "El usuario solo puede contener letras y números sin espacios"
    And el usuario NO es creado
```

### Escenario 9: Crear usuario Director General

```gherkin
  Scenario: Crear usuario con máximos privilegios
    Given el Administrador hace clic en "Nuevo Usuario"
    When ingresa los siguientes datos:
      | Campo            | Valor                          |
      | Nombre Completo  | Dr. Roberto Sánchez Díaz       |
      | Email            | rsanchez@clinicabienestar.mx   |
      | Usuario          | rsanchez                       |
      | Contraseña       | Director2024                   |
      | Rol              | Director General               |
      | Estado           | Activo                         |
    And hace clic en "Guardar"
    Then el sistema muestra el mensaje "Usuario creado exitosamente"
    And el usuario "rsanchez" aparece con rol "Director General"
    And NO se solicita campo "Servicio" (Director ve todo)
```

### Escenario 10: Campo servicio obligatorio solo para roles médicos

```gherkin
  Scenario: Validar que servicio no es obligatorio para Administrador General
    Given el Administrador hace clic en "Nuevo Usuario"
    When selecciona rol "Administrador General"
    Then el campo "Servicio" NO está visible
    When selecciona rol "Residente R1"
    Then el campo "Servicio" se muestra como obligatorio
    When selecciona rol "Personal de Recepción"
    Then el campo "Servicio" NO está visible
```
