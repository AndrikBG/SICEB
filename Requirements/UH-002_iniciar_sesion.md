# HU-002: Iniciar Sesión con Credenciales

## Historia de Usuario
**Como** usuario del sistema  
**Quiero** iniciar sesión con mis credenciales  
**Para** poder acceder al sistema de forma segura

---

## Conversación

**P1: ¿Qué campos se usan para iniciar sesión?**  
**R:** Nombre de usuario y contraseña. No usamos correo electrónico para el inicio de sesión, solo el nombre de usuario.

**P2: ¿Cuántos intentos fallidos permitimos antes de bloquear?**  
**R:** 5 intentos fallidos consecutivos. Después de 5, la cuenta se bloquea por 15 minutos.

**P3: ¿Qué pasa si el usuario tiene contraseña temporal?**  
**R:** Al iniciar sesión exitosa con su contraseña temporal (indicado en el sistema como que debe cambiar contraseña), el sistema obliga al usuario a realizar el cambio de contraseña antes de permitirle acceder a cualquier otra función.

**P4: ¿Cuánto dura una sesión?**  
**R:** 
- Sesión activa: 8 horas (jornada laboral)
- Cierre por inactividad: 30 minutos
- Al cerrar el navegador: la sesión se cierra automáticamente

**P5: ¿Qué ve cada rol después de login exitoso?**  
**R:**
- **Director:** Tablero ejecutivo
- **Administrador:** Panel de administración
- **Encargado de Servicio:** Inventario de su servicio
- **Médicos/Residentes:** Lista de pacientes
- **Recepción:** Registro de pacientes

**P6: ¿Soportamos "Recordar sesión"?**  
**R:** No en esta versión inicial. Por seguridad médica se requiere iniciar sesión cada vez.

**P7: ¿Hay opción "Olvidé mi contraseña"?**  
**R:** No en esta versión. El usuario debe contactar al Administrador para un restablecimiento manual.

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Login exitoso con credenciales correctas

```gherkin
Feature: Inicio de Sesión
  Como usuario del sistema
  Quiero iniciar sesión con mis credenciales
  Para acceder al sistema de forma segura

  Background:
    Given existe un usuario con:
      | username | password    | role                  | status  |
      | mgarcia  | Actual2024  | Personal de Recepción | Activo  |
    And el usuario está en la página de login

  Scenario: Login exitoso de usuario de recepción
    When ingresa username "mgarcia"
    And ingresa password "Actual2024"
    And hace clic en "Iniciar Sesión"
    Then el sistema valida las credenciales
    And el sistema crea una sesión con duración de 8 horas
    And el sistema redirige a "Registro de Pacientes"
    And el menú muestra opciones de "Personal de Recepción"
    And la barra superior muestra "Bienvenido, María García"
```

### Escenario 2: Login con contraseña incorrecta

```gherkin
  Scenario: Intento de login con contraseña incorrecta
    Given existe un usuario "mgarcia" con password "Actual2024"
    When ingresa username "mgarcia"
    And ingresa password "PasswordIncorrecto"
    And hace clic en "Iniciar Sesión"
    Then el sistema muestra el error "Usuario o contraseña incorrectos"
    And el usuario permanece en la página de login
    And el contador de intentos fallidos incrementa a 1
    And NO se crea ninguna sesión
```

### Escenario 3: Login con usuario inexistente

```gherkin
  Scenario: Intento de login con usuario que no existe
    Given NO existe un usuario "usuariofalso"
    When ingresa username "usuariofalso"
    And ingresa password "cualquierPassword123"
    And hace clic en "Iniciar Sesión"
    Then el sistema muestra el error "Usuario o contraseña incorrectos"
    And el usuario permanece en la página de login
    And NO se revela que el usuario no existe (seguridad)
```

### Escenario 4: Bloqueo por múltiples intentos fallidos

```gherkin
  Scenario: Cuenta bloqueada después de 5 intentos fallidos
    Given el usuario "mgarcia" tiene 4 intentos fallidos previos
    When ingresa username "mgarcia"
    And ingresa password "PasswordIncorrecto"
    And hace clic en "Iniciar Sesión"
    Then el sistema muestra el error "Cuenta bloqueada por 15 minutos debido a múltiples intentos fallidos"
    And el usuario NO puede intentar login nuevamente
    And el estado del usuario cambia a "Bloqueado Temporalmente"
    And se registra en log de seguridad el bloqueo
```

### Escenario 5: Login con cuenta inactiva

```gherkin
  Scenario: Intento de login con cuenta desactivada
    Given existe un usuario "jlopez" con status "Inactivo"
    When ingresa username "jlopez"
    And ingresa password correcta
    And hace clic en "Iniciar Sesión"
    Then el sistema muestra el error "Su cuenta está inactiva. Contacte al administrador"
    And NO se crea ninguna sesión
```

### Escenario 6: Forzar cambio de contraseña temporal

```gherkin
  Scenario: Primer login con contraseña temporal
    Given existe un usuario "nuevousuario" con mustChangePassword = true
    When ingresa username "nuevousuario"
    And ingresa password temporal "Temporal123"
    And hace clic en "Iniciar Sesión"
    Then el sistema valida las credenciales
    And el sistema muestra pantalla "Cambiar Contraseña Obligatorio"
    And el sistema NO permite acceder al menú principal hasta cambiar contraseña
```

### Escenario 7: Cambio de contraseña temporal exitoso

```gherkin
  Scenario: Cambiar contraseña temporal en primer login
    Given el usuario "nuevousuario" está en pantalla de cambio obligatorio
    When ingresa contraseña actual "Temporal123"
    And ingresa nueva contraseña "MiNuevaPass2024"
    And confirma nueva contraseña "MiNuevaPass2024"
    And hace clic en "Cambiar Contraseña"
    Then el sistema valida que la nueva contraseña cumple requisitos
    And el sistema actualiza la contraseña
    And establece mustChangePassword = false
    And crea la sesión del usuario
    And redirige a la pantalla inicial según su rol
```

### Escenario 8: Cambio de contraseña temporal con contraseña débil

```gherkin
  Scenario: Intentar cambiar a contraseña que no cumple requisitos
    Given el usuario "nuevousuario" está en pantalla de cambio obligatorio
    When ingresa contraseña actual "Temporal123"
    And ingresa nueva contraseña "12345678"
    And confirma nueva contraseña "12345678"
    And hace clic en "Cambiar Contraseña"
    Then el sistema muestra error "La contraseña debe contener letras y números"
    And la contraseña NO es cambiada
    And permanece en pantalla de cambio obligatorio
```

### Escenario 9: Sesión expira por inactividad

```gherkin
  Scenario: Sesión expira después de 30 minutos sin actividad
    Given el usuario "mgarcia" ha iniciado sesión exitosamente
    And han pasado 30 minutos sin ninguna acción del usuario
    When el usuario intenta realizar cualquier acción
    Then el sistema muestra el mensaje "Su sesión ha expirado por inactividad"
    And el sistema cierra la sesión automáticamente
    And redirige a la página de login
```

### Escenario 10: Login exitoso de médico residente

```gherkin
  Scenario: Login de residente muestra pantalla apropiada
    Given existe un usuario con:
      | username | password      | role        | service    |
      | cramirez | Residente2024 | Residente R2| Pediatría  |
    When ingresa username "cramirez"
    And ingresa password "Residente2024"
    And hace clic en "Iniciar Sesión"
    Then el sistema redirige a "Lista de Pacientes - Pediatría"
    And el menú muestra opciones de "Residente R2"
    And el menú NO muestra opciones de prescripción de controlados
    And la barra superior muestra "Dr. Carlos Ramírez - Residente R2 - Pediatría"
```