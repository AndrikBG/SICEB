# HU-008: Alertas de Temperatura de Reactivos

## Historia de Usuario

**Como** Personal de Laboratorio  
**Quiero** registrar la temperatura de los refrigeradores en tres turnos (mañana, tarde y noche) y recibir alertas si está fuera de rango  
**Para** asegurar la red de frío y cumplir con la normativa de bitácoras

---

## Conversación

**P1: ¿Qué rango de temperatura es el "seguro"?**  
**R:** Generalmente "Red de Frío" es de 2°C a 8°C. El sistema debe permitir configurar el rango por refrigerador.

**P2: ¿Es automático con sensores?**  
**R:** NO. Por ahora, el registro es MANUAL. El personal debe ir al termómetro físico, leer el valor y escribirlo en el sistema.

**P3: ¿Cuándo deben registrarlo?**  
**R:** Al menos 3 veces al día: Turno Matutino, Turno Vespertino y Turno Nocturno. El sistema debe marcar si ya se hizo el registro del turno o si está pendiente.

**P4: ¿Qué pasa si ingreso un valor malo?**  
**R:** Si escribo un valor fuera de rango (ej. 9°C), el sistema debe mostrar una ALERTA INMEDIATA en pantalla y pedir que registre la acción correctiva obligatoriamente.

**P5: ¿Se genera la gráfica?**  
**R:** Sí, con los puntos capturados manualmente se dibuja la tendencia.

---

## Criterios de Aceptación (Gherkin)

### Escenario 1: Captura de temperatura rutina (Turno Matutino)
```gherkin
Feature: Bitácora de Temperatura
  Como Químico
  Quiero registrar la temperatura
  Para cumplir con la norma

  Background:
    Given es el Turno Matutino (07:00 - 14:00)
    And el "Refrigerador A" requiere temperatura de 2 a 8°C

  Scenario: Registro exitoso
    When el usuario ingresa a la bitácora
    And captura 5.0°C para el "Refrigerador A"
    Then el sistema guarda el registro
    And marca el semáforo del turno como VERDE (Completado)
```

### Escenario 2: Alerta por temperatura fuera de rango
```gherkin
  Scenario: Excursión de temperatura
    When el usuario captura 9.5°C
    Then el sistema muestra una ALERTA ROJA en pantalla: "¡Temperatura fuera de rango!"
    And abre automáticamente la ventana de "Registro de Incidencia"
    And no permite guardar solo el valor sin justificar la acción correctiva
```

### Escenario 3: Registro de acción correctiva
```gherkin
  Scenario: Justificar incidencia
    Given saltó la alerta de 9.5°C
    When el usuario escribe: "Puerta mal cerrada, se ajustó y verificó descenso a 7°C"
    Then el sistema guarda la temperatura (9.5°C) Y la nota correctiva
    And el registro queda marcado en ROJO en el historial para auditoría
```

### Escenario 4: Auditoría de cumplimiento de turnos
```gherkin
  Scenario: Turno sin registro (Olvido)
    Given son las 23:00 y nadie capturó la temperatura del Turno Vespertino
    When el supervisor revisa el dashboard
    Then el "Refrigerador A" muestra una alerta de "FALTA REGISTRO VESPERTINO"
    And queda evidencia del incumplimiento
```

### Escenario 5: Gráfica de puntos manuales
```gherkin
  Scenario: Visualizar tendencia
    When el usuario consulta la gráfica mensual
    Then ve los puntos conectados (Mañana-Tarde-Noche) de cada día
    And las líneas visualizan la estabilidad térmica
```

### Escenario 6: Configuración de equipo
```gherkin
  Scenario: Alta de nuevo equipo
    When el Admin registra "Ultra-Congelador"
    And define rango seguro de -80°C a -60°C
    And define horarios de monitoreo
    Then el sistema habilita la bitácora para este nuevo equipo
```
