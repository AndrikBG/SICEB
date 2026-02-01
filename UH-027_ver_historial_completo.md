# HU-027: Ver Historial Médico Completo

## Historia de Usuario

**Como** Médico  
**Quiero** ver el historial médico completo de un paciente de todos los servicios  
**Para** tener contexto completo para tomar mejores decisiones de tratamiento

---

## Conversación

**P1: ¿Puedo ver las consultas de otros médicos?**  
**R:** Sí. En el expediente multidisciplinario, un médico debe poder ver lo que otros especialistas le recetaron o diagnosticaron. Esto evita contraindicaciones medicamentosas.

**P2: ¿Cómo se ordena el historial?**  
**R:** Cronológicamente inverso (lo más reciente arriba).

**P3: ¿Puedo filtrar por servicio?**  
**R:** Sí, debería poder ver "Solo notas de Cardiología" o "Solo notas de Pediatría" si quiero enfocarme, pero por defecto muestra "Todo".

**P4: ¿Se ven los estudios de laboratorio en la línea de tiempo?**  
**R:** Sí, idealmente la línea de tiempo integra consultas y resultados de estudios para ver la evolución completa.

**P5: ¿Hay resumen rápido?**  
**R:** La vista principal debe ser un resumen tipo "Timeline" (Fecha - Especialidad - Diagnóstico). Al hacer clic en una entrada, se expande el detalle completo.

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Vista cronológica unificada
```gherkin
Feature: Historial Clínico Integral
  Como Médico tratante
  Quiero conocer los antecedentes del paciente
  Para evitar errores médicos

  Background:
    Given el paciente ha sido atendido en "Urgencias", "Medicina Interna" y "Nutrición"

  Scenario: Ver timeline completo
    When el médico accede a la pestaña "Historial"
    Then ve una lista ordenada por fecha descendente
    And la lista contiene las entradas de los 3 servicios
    And cada entrada muestra: Fecha, Servicio, Médico y Diagnóstico Principal
```

### Escenario 2: Ver detalle de consulta ajena
```gherkin
  Scenario: Consultar nota de otro especialista
    Given el médico es "Cardiólogo"
    And existe una nota previa de "Neumología"
    When hace clic en la nota de Neumología
    Then se despliega el contenido completo (SOAP)
    But en modo Solo Lectura (no puede editarla, ver HU-026)
```

### Escenario 3: Filtrado por servicio
```gherkin
  Scenario: Enfocar en especialidad
    When selecciona el filtro "Servicio: Nutrición"
    Then la lista oculta las notas de Urgencias y Medicina Interna
    And solo muestra el historial nutricional
```

### Escenario 4: Privacidad en notas sensibles (Ginecología/Psiquiatría) - Excepción
```gherkin
  Scenario: Notas confidenciales (Opcional por configuración)
    Given existe una nota marcada como "Confidencial" (ej. Psiquiatría)
    When un médico de otra área (ej. Dermatología) intenta verla
    Then el sistema muestra "Nota Reservada - Requiere permiso explícito"
    # Nota: Este escenario depende de la política de privacidad de la clínica.
    # Para MVP asumimos visibilidad total, pero dejamos este escenario preparado.
```

### Escenario 5: Identificación visual de servicio
```gherkin
  Scenario: Código de colores por área
    When visualiza el timeline
    Then las notas de Urgencias tienen un distintivo ROJO
    And las notas de Consulta Externa tienen distintivo AZUL
    And las de Hospitalización tienen distintivo VERDE
    Para facilitar el escaneo visual rápido
```

### Escenario 6: Carga progresiva (Performance)
```gherkin
  Scenario: Historial muy largo
    Given un paciente con 10 años de historial (500 notas)
    When abre el historial
    Then carga inicialmente las últimas 20 notas
    And al hacer scroll al final, carga las siguientes 20 (Infinite Scroll)
    Para no congelar el navegador
```
