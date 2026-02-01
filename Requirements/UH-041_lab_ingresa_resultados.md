# HU-041: Laboratorio Ingresa Resultados

## Historia de Usuario

**Como** Personal de Laboratorio  
**Quiero** ingresar resultados de estudios en formato texto/numérico  
**Para** que los médicos puedan revisarlos y completar el diagnóstico

---

## Conversación

**P1: ¿Es texto libre o campos estructurados?**  
**R:** Depende del estudio.
- Para **Biometría Hemática**: Queremos campos numéricos (Hemoglobina: [Campo], Leucocitos: [Campo]).
- Para **EGO**: Campos de selección (Color: amarillo/ámbar, Aspecto: ligero/turbio).
- Para **Cultivos**: Texto libre grande para describir hallazgos.

**P2: ¿El sistema marca valores anormales?**  
**R:** Sí. Si definimos valores de referencia (Rango: 12-16), y capturo 10, debe ponerse en negrita/rojo o marcar con un asterisco (*).

**P3: ¿Puedo guardar parcial y seguir luego?**  
**R:** Sí (Guardar Borrador). Pero el médico solo ve el resultado cuando le doy "Liberar/Publicar".

**P4: ¿Se genera PDF?**  
**R:** Sí. Al liberar, el sistema genera el reporte oficial en PDF con el encabezado de la clínica y la firma digital del Químico Responsable.

**P5: ¿Puedo adjuntar archivos?**  
**R:** Sí, para estudios que generan gráficos o imágenes (histopatología), poder subir un JPG o PDF anexo es necesario.

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Captura de resultados numéricos con referencias
```gherkin
Feature: Reporte de Resultados
  Como Químico Analista
  Quiero transcribir los datos de los equipos
  Para generar el informe

  Background:
    Given la orden "LAB-505" está en proceso

  Scenario: Captura de Glucosa
    When el químico abre la captura de resultados
    And en el campo "Glucosa" ingresa "105"
    Then el sistema muestra al lado el rango de referencia (70-100 mg/dL)
    And marca el valor "105" como "ALTO" (H) visualmente
```

### Escenario 2: Liberación de resultados
```gherkin
  Scenario: Publicar estudio completo
    When termina de capturar todos los campos
    And hace clic en "Validar y Publicar"
    Then el estado de la solicitud cambia a "Completado"
    And los resultados se vuelven visibles para el Médico en el Expediente (HU-042)
    And se genera el PDF final
```

### Escenario 3: Bloqueo de edición post-liberación
```gherkin
  Scenario: Garantizar inmutabilidad del reporte
    Given el resultado ya fue publicado
    When el químico intenta cambiar un valor
    Then los campos están bloqueados
    And requiere un proceso especial de "Rectificación" para corregir errores (con nota de corrección)
```

### Escenario 4: Valores de pánico (Críticos)
```gherkin
  Scenario: Alerta de resultado peligroso
    When ingresa Plaquetas "20,000" (Muy bajo)
    Then el sistema muestra una ALERTA CRÍTICA: "VALOR DE PÁNICO - Notificar al médico inmediatamente"
    And registra que se mostró la alerta
```

### Escenario 5: Captura de texto descriptivo
```gherkin
  Scenario: Resultado cualitativo
    When captura un Examen General de Orina
    And selecciona Color: "Rojizo"
    And Aspecto: "Turbio"
    And escribe en Observaciones: "Abundantes bacterias+++"
    Then el reporte guarda el texto tal cual
```

### Escenario 6: Adjuntar evidencia externa
```gherkin
  Scenario: Subir imagen de microscopía
    When el usuario selecciona "Adjuntar Archivo"
    And sube "foto_parasito.jpg"
    Then la imagen se anexa al final del reporte PDF
```
