# HU-003: Asignar Diferentes Niveles de Permisos

## Historia de Usuario

**Como** Administrador  
**Quiero** asignar diferentes niveles de permisos (Director, Admin, Encargado, Médico, Residente, etc.)  
**Para que** los usuarios solo vean información relevante a su rol

---

## Conversación
**P1: ¿Qué permisos específicos tiene cada rol?**  
**R:** Matriz de permisos:

| Módulo | Director | Admin | Encargado | Médico | Residente R4 | Residente R3-R1 | Recepción |
|--------|----------|-------|-----------|--------|--------------|-----------------|-----------|
| Ver TODO inventario | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ | ❌ |
| Ver inventario propio | N/A | N/A | ✅ | ✅ | ✅ | ✅ | ❌ |
| Solicitar insumos | ❌ | ❌ | ✅ | ✅ | ✅ | ✅ | ❌ |
| Aprobar solicitudes | ❌ | ✅ | ❌ | ❌ | ❌ | ❌ | ❌ |
| Registrar pacientes | ❌ | ❌ | ❌ | ❌ | ❌ | ❌ | ✅ |
| Ver expedientes | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ (con supervisión) | ❌ |
| Editar expedientes | ❌ | ❌ | ✅ | ✅ | ✅ | ✅ (limitado) | ❌ |
| Prescribir medicamentos | ❌ | ❌ | ✅ | ✅ | ✅ | Limitado por nivel | ❌ |
| Prescribir controlados | ❌ | ❌ | ✅ | ✅ | ✅ | ❌ | ❌ |
| Ver reportes financieros | ✅ | ✅ | Solo su servicio | ❌ | ❌ | ❌ | ❌ |
| Registrar pagos | ❌ | ✅ | ❌ | ❌ | ❌ | ❌ | ✅ |

**P2: ¿Los permisos se asignan automáticamente al crear usuario?**  
**R:** Sí, al seleccionar el rol en HU-001, los permisos se asignan automáticamente según esta matriz.

**P3: ¿Se pueden dar permisos "extra" a un usuario?**  
**R:** No en esta versión. Los permisos son fijos por rol. Si un usuario necesita más permisos, se le debe asignar un rol diferente que los contenga.

**P4: ¿Cómo implementamos multitenencia?**  
**R:** 
- Para Encargados, Médicos y Residentes, el sistema filtra automáticamente la información para mostrar solo la perteneciente a su servicio.
- El Administrador y Director no tienen este filtro y pueden ver la información de todos los servicios.
- El sistema valida los permisos antes de realizar cualquier operación.

---
## Criterios de Aceptación (Gherkin)

```gherkin
Feature: Control de Acceso Basado en Roles (RBAC)
  Como sistema
  Quiero aplicar permisos según el rol del usuario
  Para proteger información sensible y mantener multitenencia

  Background:
    Given los siguientes usuarios existen:
      | username  | role                  | service    |
      | director  | Director General      | N/A        |
      | admin     | Administrador General | N/A        |
      | encpedia  | Encargado de Servicio | Pediatría  |
      | drmedico  | Médico Adscrito       | Medicina   |
      | resR4     | Residente R4          | Pediatría  |
      | resR1     | Residente R1          | Cirugía    |
      | recep     | Personal de Recepción | N/A        |

  Scenario: Director General puede ver inventario de TODOS los servicios
    Given el usuario "director" ha iniciado sesión
    When accede a "Gestión de Inventario"
    Then puede ver inventario de "Pediatría"
    And puede ver inventario de "Medicina"
    And puede ver inventario de "Cirugía"
    And puede ver "Inventario General de la Clínica"

  Scenario: Encargado de Servicio solo ve inventario de SU servicio
    Given el usuario "encpedia" ha iniciado sesión (Pediatría)
    When accede a "Gestión de Inventario"
    Then puede ver SOLO inventario de "Pediatría"
    And NO puede ver inventario de "Medicina"
    And NO puede ver inventario de "Cirugía"
    And el sistema filtra automáticamente por serviceId = Pediatría

  Scenario: Residente R1 NO puede prescribir medicamentos controlados
    Given el usuario "resR1" ha iniciado sesión
    When accede a formulario de prescripción
    Then puede seleccionar medicamentos NO controlados
    And los medicamentos controlados aparecen DESHABILITADOS
    And al intentar prescribir controlado muestra: "Residente R1 no autorizado"

  Scenario: Residente R4 SÍ puede prescribir medicamentos controlados
    Given el usuario "resR4" ha iniciado sesión
    When accede a formulario de prescripción
    Then puede seleccionar medicamentos NO controlados
    And puede seleccionar medicamentos controlados
    And puede completar prescripción sin restricciones

  Scenario: Personal de Recepción NO puede ver expedientes médicos
    Given el usuario "recep" ha iniciado sesión
    When intenta acceder a "Expedientes Médicos"
    Then el sistema muestra error "Acceso denegado"
    And registra intento de acceso no autorizado en log de auditoría
    And NO muestra ningún dato del expediente

  Scenario: Médico Adscrito puede ver expedientes de CUALQUIER servicio
    Given el usuario "drmedico" (Medicina) ha iniciado sesión
    When busca paciente que fue atendido en "Pediatría"
    Then puede ver el expediente completo
    And puede ver consultas registradas por otros servicios
    And puede agregar nueva consulta desde "Medicina"

  Scenario: Encargado de Servicio NO puede aprobar solicitudes de insumos
    Given el usuario "encpedia" ha iniciado sesión
    When accede a "Solicitudes de Insumos"
    Then puede crear nueva solicitud
    And puede ver estado de sus solicitudes
    But NO puede ver botón "Aprobar" o "Rechazar"
    And NO tiene acceso a pantalla de aprobaciones

  Scenario: Administrador General puede aprobar solicitudes
    Given el usuario "admin" ha iniciado sesión
    When accede a "Solicitudes de Insumos Pendientes"
    Then puede ver solicitudes de TODOS los servicios
    And puede aprobar o rechazar cualquier solicitud
    And puede agregar comentarios de justificación

  Scenario: Menú del sistema se adapta al rol del usuario
    Given el usuario "resR1" ha iniciado sesión
    Then el menú muestra:
      | Opción Visible              |
      | 📋 Lista de Pacientes       |
      | 🩺 Consultas Médicas        |
      | 💊 Prescripciones (limitado)|
      | 🧪 Solicitar Laboratorio    |
      | 📦 Inventario Mi Servicio   |
    And el menú NO muestra:
      | Opción NO Visible           |
      | 👥 Gestión de Usuarios      |
      | 📊 Reportes Financieros     |
      | ✅ Aprobar Solicitudes      |
      | ⚙️ Configuración Sistema    |

  Scenario: Sistema valida permisos en backend (no solo frontend)
    # Nota: Este escenario incluye consideraciones técnicas para el equipo de desarrollo.
    Given el usuario "recep" ha iniciado sesión
    When intenta hacer request HTTP directo: GET /api/expedientes/123
    Then el backend valida el rol del token JWT
    And responde con código 403 Forbidden
    And mensaje: "Su rol no tiene permisos para esta operación"
    And registra intento en log de seguridad
```