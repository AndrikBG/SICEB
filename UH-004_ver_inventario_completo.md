# HU-004: Administrador General Ve Inventario Completo

## Historia de Usuario

**Como** Administrador General  
**Quiero** ver el inventario completo de TODOS los servicios  
**Para** tener visibilidad total de los recursos de la clínica

---

## Conversación
**P1: ¿Qué información debe mostrar el inventario?**  
**R:** Por cada artículo:
- Código/SKU
- Nombre del artículo
- Categoría (medicamento, material médico, reactivo, etc.)
- Servicio al que pertenece
- Cantidad actual
- Unidad de medida
- Stock mínimo
- Fecha de caducidad (si aplica)
- Estado (OK, Stock Bajo, Por Caducar, Caducado)

**P2: ¿Cómo se organizan los artículos?**  
**R:** La vista por defecto es agrupada por servicio. Además, deben estar disponibles filtros por categoría, por estado y por servicio.

**P3: ¿Puede el Admin editar cantidades directamente?**  
**R:** No. Solo puede ver la información. Los cambios en el inventario provienen de:
- Solicitudes aprobadas (HU-013)
- Dispensación de farmacia (HU-034)
- Uso en consulta (HU-052)

**P4: ¿El Admin puede agregar nuevos artículos al catálogo?**  
**R:** Sí, pero esa funcionalidad corresponde a una historia de usuario separada y no está incluida en este alcance inicial.

---
## Criterios de Aceptación (Gherkin)

```gherkin
Feature: Vista Global de Inventario para Administrador
  Como Administrador General
  Quiero ver el inventario completo de todos los servicios
  Para tener control total de recursos

  Background:
    Given el usuario "admin" (Administrador General) ha iniciado sesión
    And existen los siguientes artículos:
      | SKU   | Nombre           | Servicio   | Cantidad | Stock Min | Estado      |
      | MED01 | Paracetamol 500mg| Farmacia   | 50       | 20        | OK          |
      | MED02 | Ibuprofeno 400mg | Farmacia   | 15       | 20        | Stock Bajo  |
      | REA01 | Reactivo Glucosa | Laboratorio| 5        | 10        | Stock Bajo  |
      | MAT01 | Jeringas 5ml     | Pediatría  | 100      | 30        | OK          |
      | MAT02 | Gasas estériles  | Cirugía    | 200      | 50        | OK          |

  Scenario: Ver inventario completo de todos los servicios
    When accede a "Gestión de Inventario"
    Then puede ver artículos de "Farmacia"
    And puede ver artículos de "Laboratorio"
    And puede ver artículos de "Pediatría"
    And puede ver artículos de "Cirugía"
    And el total de artículos mostrados es 5

  Scenario: Inventario agrupado por servicio
    When accede a "Gestión de Inventario"
    Then los artículos están agrupados por servicio:
      | Servicio    | Cantidad de Artículos |
      | Farmacia    | 2                     |
      | Laboratorio | 1                     |
      | Pediatría   | 1                     |
      | Cirugía     | 1                     |
    And cada grupo muestra el nombre del servicio como encabezado

  Scenario: Filtrar inventario por servicio específico
    When accede a "Gestión de Inventario"
    And selecciona filtro "Servicio: Farmacia"
    Then solo muestra artículos de "Farmacia"
    And muestra 2 artículos (MED01, MED02)
    And NO muestra artículos de otros servicios

  Scenario: Filtrar por estado "Stock Bajo"
    When accede a "Gestión de Inventario"
    And selecciona filtro "Estado: Stock Bajo"
    Then muestra solo artículos con cantidad < stock mínimo
    And muestra 2 artículos (MED02, REA01)
    And cada artículo muestra indicador visual de alerta (⚠️)

  Scenario: Ver detalles de un artículo
    When accede a "Gestión de Inventario"
    And hace clic en artículo "Paracetamol 500mg"
    Then se muestra panel de detalles con:
      | Campo               | Valor               |
      | SKU                 | MED01               |
      | Nombre              | Paracetamol 500mg   |
      | Categoría           | Medicamento         |
      | Servicio            | Farmacia            |
      | Cantidad Actual     | 50 unidades         |
      | Stock Mínimo        | 20 unidades         |
      | Fecha Caducidad     | 2025-12-31          |
      | Estado              | OK                  |
      | Última Actualización| 2026-01-30 14:23    |

  Scenario: Buscar artículo por nombre o SKU
    When accede a "Gestión de Inventario"
    And ingresa "Paracetamol" en el buscador
    Then muestra solo "Paracetamol 500mg"
    When limpia búsqueda
    And ingresa "MED02" en el buscador
    Then muestra solo "Ibuprofeno 400mg"

  Scenario: Exportar inventario completo a Excel
    When accede a "Gestión de Inventario"
    And hace clic en "Exportar a Excel"
    Then se descarga archivo "Inventario_Completo_2026-01-31.xlsx"
    And el archivo contiene todos los artículos de todos los servicios
    And incluye todas las columnas visibles

  Scenario: Ver alertas resaltadas visualmente
    When accede a "Gestión de Inventario"
    Then los artículos en "Stock Bajo" se muestran con fondo amarillo
    And los artículos "Caducados" se muestran con fondo rojo
    And los artículos "Por Caducar" (<30 días) se muestran con fondo naranja
    And los artículos "OK" se muestran con fondo blanco

  Scenario: Ordenar inventario por diferentes columnas
    When accede a "Gestión de Inventario"
    And hace clic en encabezado "Cantidad"
    Then los artículos se ordenan de menor a mayor cantidad
    When hace clic nuevamente en "Cantidad"
    Then los artículos se ordenan de mayor a menor cantidad
    When hace clic en "Fecha Caducidad"
    Then los artículos se ordenan por fecha de caducidad ascendente

  Scenario: Paginación de inventario con muchos artículos
    Given existen 150 artículos en el inventario
    When accede a "Gestión de Inventario"
    Then muestra los primeros 50 artículos
    And muestra controles de paginación: "Página 1 de 3"
    When hace clic en "Página 2"
    Then muestra artículos 51-100
```
