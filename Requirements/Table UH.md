##   HISTORIAS DE USUARIO

| ID | Historia de Usuario (Como... quiero... para que...) | Prioridad |
|----|-----------------------------------------------------|-----------|
| **HU-001** | Como **Administrador**, quiero crear cuentas de usuario con permisos basados en roles para que el personal pueda acceder al sistema según sus responsabilidades | **CRÍTICA** |
| **HU-002** | Como **usuario del sistema**, quiero iniciar sesión con mis credenciales para poder acceder al sistema de forma segura | **CRÍTICA** |
| **HU-003** | Como **Administrador**, quiero asignar diferentes niveles de permisos (Director, Admin, Encargado, Médico, Residente, etc.) para que los usuarios solo vean información relevante a su rol | **CRÍTICA** |
| **HU-004** | Como **Administrador General**, quiero ver el inventario completo de TODOS los servicios para tener visibilidad total de los recursos de la clínica | **CRÍTICA** |
| **HU-005** | Como **Encargado de Servicio**, quiero ver SOLO el inventario de mi servicio para gestionar los recursos de mi área efectivamente | **CRÍTICA** |
| **HU-006** | Como **Encargado de Servicio**, quiero ver alertas automáticas cuando los artículos de inventario estén por agotarse para poder solicitar reabastecimiento antes de quedarme sin existencias | **ALTA** |
| **HU-007** | Como **Encargado de Servicio**, quiero ver alertas para artículos próximos a caducar para poder usarlos antes de que expiren y minimizar desperdicios | **MEDIA** |
| **HU-008** | Como **Personal de Laboratorio**, quiero recibir alertas cuando los reactivos estén fuera del rango seguro de temperatura para poder tomar acción correctiva inmediatamente | **ALTA** |
| **HU-009** | Como **Encargado de Servicio**, quiero enviar una solicitud digital de insumos para que el proceso sea más rápido y rastreable | **ALTA** |
| **HU-010** | Como **Administrador General**, quiero recibir notificaciones automáticas cuando se envíen solicitudes de insumos para poder revisarlas prontamente | **ALTA** |
| **HU-011** | Como **Administrador General**, quiero aprobar o rechazar solicitudes de insumos con justificación para que las decisiones queden documentadas | **ALTA** |
| **HU-012** | Como **Encargado de Servicio**, quiero recibir notificaciones automáticas cuando mi solicitud sea aprobada o rechazada para conocer el estado sin seguimiento manual | **MEDIA** |
| **HU-013** | Como **Administrador General** y **Encargado de Servicio**, queremos completar el ciclo de entrega de materiales (Administrador registra entrega → Encargado confirma recepción → Inventario se actualiza) para cerrar el flujo de solicitud con trazabilidad completa | **MEDIA** |
| **HU-015** | Como **Encargado de Servicio** o **Residente**, quiero enviar una solicitud digital de talleres/capacitación para que el proceso de aprobación esté formalizado | **MEDIA** |
| **HU-016** | Como **Administrador General**, quiero aprobar o rechazar solicitudes de talleres para que las actividades de capacitación estén controladas | **MEDIA** |
| **HU-017** | Como **Administrador General**, quiero ver el historial de todas las solicitudes de talleres para poder rastrear la actividad de capacitación | **BAJA** |
| **HU-018** | Como **Encargado de Servicio**, quiero registrar la asistencia de talleres completados para que la participación de residentes quede documentada | **MEDIA** |
| **HU-019** | Como **Personal de Recepción**, quiero registrar pacientes nuevos con su información demográfica para que puedan recibir atención médica | **CRÍTICA** |
| **HU-020** | Como **Personal de Recepción**, quiero clasificar pacientes por tipo (Estudiante/Trabajador/Externo) Y que el sistema aplique automáticamente el descuento correspondiente (30% Estudiantes, 20% Trabajadores, 0% Externos) para que la facturación sea consistente y automática | **CRÍTICA** |
| **HU-022** | Como **Personal de Recepción**, quiero registrar un paciente menor con la información de su tutor para que se cumplan requisitos legales | **ALTA** |
| **HU-023** | Como **Personal de Recepción**, quiero que el sistema valide que esté presente un tutor para pacientes menores de 17 años para que se apliquen las políticas de la clínica | **CRÍTICA** |
| **HU-024** | Como **Médico** o **Encargado de Servicio**, quiero crear un nuevo expediente médico cuando registro un paciente para que comience su historial médico | **CRÍTICA** |
| **HU-025** | Como **Médico**, quiero agregar una nueva entrada de consulta al expediente existente de un paciente para que su atención quede documentada | **CRÍTICA** |
| **HU-026** | Como **Médico**, quiero que las entradas de consulta sean inmutables (no editables) para que los expedientes médicos mantengan integridad para auditorías | **CRÍTICA** |
| **HU-027** | Como **Médico**, quiero ver el historial médico completo de un paciente de todos los servicios para tener contexto completo para decisiones de tratamiento | **CRÍTICA** |
| **HU-028** | Como **Médico**, quiero buscar pacientes por nombre, ID, o número de estudiante/empleado para poder acceder rápidamente a sus expedientes | **ALTA** |
| **HU-029** | Como **Médico**, quiero adjuntar archivos (PDFs, imágenes) al expediente de un paciente para que documentos externos estén consolidados | **ALTA** |
| **HU-030** | Como **Médico**, quiero registrar signos vitales durante una consulta para que las tendencias de salud del paciente puedan rastrearse | **ALTA** |
| **HU-031** | Como **Médico**, quiero prescribir medicamentos durante una consulta para que la prescripción quede registrada y disponible para farmacia | **CRÍTICA** |
| **HU-032** | Como **Personal de Farmacia**, quiero ver las prescripciones de un paciente para poder dispensar los medicamentos correctos | **CRÍTICA** |
| **HU-033** | Como **Personal de Farmacia**, quiero que el sistema valide que existe una prescripción antes de dispensar para no dispensar sin orden médica | **CRÍTICA** |
| **HU-034** | Como **Personal de Farmacia**, quiero que el sistema verifique el inventario antes de dispensar para saber si los medicamentos están disponibles | **CRÍTICA** |
| **HU-035** | Como **Personal de Farmacia**, quiero registrar cuando dispenso medicamentos controlados para que haya trazabilidad completa | **CRÍTICA** |
| **HU-036** | Como **Personal de Farmacia**, quiero registrar la dispensación con quién prescribió, quién dispensó y a quién para que haya rastro de auditoría completo | **ALTA** |
| **HU-037** | Como **Personal de Farmacia**, quiero cobrar por medicamentos separadamente de la consulta para que la contabilidad sea exacta | **ALTA** |
| **HU-038** | Como **Médico**, quiero solicitar estudios de laboratorio durante una consulta para que se ordenen pruebas diagnósticas | **CRÍTICA** |
| **HU-039** | Como **Personal de Recepción**, quiero cobrar por estudios de laboratorio ANTES de realizarlos para que el pago esté asegurado por adelantado | **ALTA** |
| **HU-040** | Como **Personal de Laboratorio**, quiero ver solicitudes de estudios pendientes para saber qué pruebas procesar | **CRÍTICA** |
| **HU-041** | Como **Personal de Laboratorio**, quiero ingresar resultados de estudios en formato texto para que los médicos puedan revisarlos | **CRÍTICA** |
| **HU-042** | Como **Médico**, quiero ver resultados de laboratorio en el expediente médico del paciente para poder interpretarlos en contexto | **CRÍTICA** |
| **HU-043** | Como **Personal de Laboratorio**, quiero gestionar inventario de reactivos con fechas de caducidad y monitoreo de temperatura para que la calidad de las pruebas se mantenga | **ALTA** |
| **HU-044** | Como **Personal de Recepción**, quiero registrar pagos por consultas, medicamentos y estudios de laboratorio para que todos los ingresos se rastreen | **CRÍTICA** |
| **HU-045** | Como **Personal de Recepción**, quiero que el sistema genere recibos simples (no facturas CFDI) para que los pacientes tengan comprobante de pago | **ALTA** |
| **HU-046** | Como **Director** o **Administrador**, quiero generar reportes financieros mostrando ingresos por servicio, concepto y tipo de paciente para poder analizar rentabilidad | **ALTA** |
| **HU-047** | Como **Director** o **Administrador**, quiero generar reportes mostrando gastos por servicio para poder controlar costos | **ALTA** |
| **HU-048** | Como **Director**, quiero ver reportes de rentabilidad (ingresos - gastos) por servicio para poder tomar decisiones estratégicas | **ALTA** |
| **HU-049** | Como **Administrador**, quiero registrar personal médico (adscritos y residentes R1-R4) para que el personal esté en el sistema | **ALTA** |
| **HU-050** | Como **sistema**, quiero validar automáticamente que los residentes solo puedan realizar acciones permitidas para su nivel (R1-R4) para que se apliquen las políticas de la clínica | **CRÍTICA** |
| **HU-051** | Como **sistema**, quiero bloquear a Residentes R1, R2 y R3 de prescribir medicamentos controlados para que se apliquen políticas de seguridad | **CRÍTICA** |
| **HU-052** | Como **Médico**, quiero registrar qué insumos usé durante una consulta para que el inventario se actualice y los costos se rastreen | **ALTA** |
| **HU-053** | Como **Personal de Recepción**, quiero agendar citas médicas para pacientes especificando fecha, hora, médico y tipo de consulta para que el flujo de consultas esté organizado | **ALTA** |
| **HU-054** | Como **Médico**, quiero ver mi agenda de citas del día con información del paciente para saber qué pacientes atenderé y prepararme adecuadamente | **ALTA** |
| **HU-055** | Como **Personal de Recepción**, quiero cancelar o reagendar citas médicas documentando razón para gestionar cambios de pacientes | **ALTA** |
| **HU-056** | Como **Personal Administrativo**, quiero registrar consentimientos informados firmados adjuntando documento escaneado para cumplir NOM-024-SSA3-2012 | **ALTA** |
| **HU-057** | Como **Médico**, quiero marcar en el expediente que un procedimiento requiere consentimiento específico para documentar cumplimiento legal | **ALTA** |
| **HU-058** | Como **Encargado de Servicio**, quiero registrar materiales desechados con razón (caducados, dañados) para documentar pérdidas | **MEDIA** |
| **HU-059** | Como **Médico Pediatra**, quiero registrar datos de crecimiento y esquema de vacunación en expedientes pediátricos para seguimiento adecuado | **ALTA** |
| **HU-060** | Como **Médico Ginecólogo**, quiero registrar datos de embarazo (FUM, semanas de gestación, FPP) para seguimiento prenatal | **ALTA** |
| **HU-061** | Como **Personal de Urgencias**, quiero asignar nivel de triage (Rojo/Amarillo/Verde) para priorizar atención según gravedad | **ALTA** |
| **HU-062** | Como **Paciente**, quiero solicitar acceso a mi expediente médico completo (derecho ARCO) para ejercer mis derechos | **ALTA** |
| **HU-063** | Como **Personal Administrativo**, quiero procesar solicitudes ARCO dentro de plazo legal (20 días) para cumplir LFPDPPP | **ALTA** |
| **HU-064** | Como **Administrador**, quiero configurar tarifas por servicio médico especificando precio base para que el sistema calcule cobros correctamente | **CRÍTICA** |
| **HU-065** | Como **Administrador**, quiero asignar médico supervisor a residentes R1/R2 para cumplir políticas de supervisión obligatoria | **ALTA** |
| **HU-066** | Como **sistema**, quiero registrar en log de auditoría quién accedió a qué expediente y cuándo para cumplir trazabilidad LFPDPPP | **CRÍTICA** |
| **HU-067** | Como **Director**, quiero ver dashboard ejecutivo con métricas clave para monitorear desempeño de la clínica | **MEDIA** |
| **HU-068** | Como **Administrador**, quiero generar reporte de solicitudes de insumos por servicio y periodo para analizar patrones de consumo | **MEDIA** |
| **HU-070** | Como **sistema**, quiero enviar recordatorios de citas 24h antes para reducir ausentismo | **BAJA** |