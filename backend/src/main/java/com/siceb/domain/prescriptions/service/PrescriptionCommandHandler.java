package com.siceb.domain.prescriptions.service;

import com.siceb.domain.clinicalcare.exception.ClinicalDomainException;
import com.siceb.domain.clinicalcare.model.ClinicalEvent;
import com.siceb.domain.clinicalcare.model.ClinicalEventType;
import com.siceb.domain.clinicalcare.repository.ClinicalEventRepository;
import com.siceb.domain.clinicalcare.service.ClinicalEventStore;
import com.siceb.domain.prescriptions.command.CreatePrescriptionCommand;
import com.siceb.platform.branch.TenantContext;
import com.siceb.platform.iam.StaffContext;
import com.siceb.shared.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Handles prescription creation within a consultation context (US-031).
 * Prescriber-level restrictions deferred to Phase 3 AuthorizationMiddleware.
 */
@Service
public class PrescriptionCommandHandler {

    private static final Logger log = LoggerFactory.getLogger(PrescriptionCommandHandler.class);

    private final ClinicalEventStore eventStore;
    private final ClinicalEventRepository eventRepository;

    public PrescriptionCommandHandler(ClinicalEventStore eventStore,
                                       ClinicalEventRepository eventRepository) {
        this.eventStore = eventStore;
        this.eventRepository = eventRepository;
    }

    @Transactional
    public UUID createPrescription(CreatePrescriptionCommand cmd) {
        UUID branchId = TenantContext.require();
        UUID staffId = StaffContext.require();

        boolean consultationExists = eventRepository.findById(cmd.consultationId())
                .filter(e -> e.getEventType() == ClinicalEventType.CONSULTATION)
                .isPresent();

        if (!consultationExists) {
            throw new ClinicalDomainException(ErrorCode.RESOURCE_NOT_FOUND,
                    "Consultation " + cmd.consultationId() + " not found");
        }

        if (cmd.items().isEmpty()) {
            throw new ClinicalDomainException(ErrorCode.VALIDATION_FAILED,
                    "Prescription must have at least one item");
        }

        List<Map<String, Object>> itemPayloads = cmd.items().stream()
                .map(item -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("medicationId", item.medicationId().toString());
                    m.put("medicationName", item.medicationName());
                    m.put("quantity", item.quantity());
                    m.put("dosage", item.dosage());
                    m.put("frequency", item.frequency());
                    if (item.duration() != null) m.put("duration", item.duration());
                    if (item.route() != null) m.put("route", item.route());
                    if (item.instructions() != null) m.put("instructions", item.instructions());
                    return m;
                })
                .toList();

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("prescriptionId", cmd.prescriptionId().toString());
        payload.put("consultationId", cmd.consultationId().toString());
        payload.put("status", "ACTIVE");
        payload.put("items", itemPayloads);

        ClinicalEvent event = ClinicalEvent.builder()
                .eventId(cmd.prescriptionId())
                .recordId(cmd.recordId())
                .eventType(ClinicalEventType.PRESCRIPTION)
                .branchId(branchId)
                .performedByStaffId(staffId)
                .idempotencyKey(cmd.idempotencyKey())
                .payload(payload)
                .build();

        ClinicalEvent saved = eventStore.append(event);
        log.info("Prescription created: id={}, consultation={}, items={}",
                cmd.prescriptionId(), cmd.consultationId(), cmd.items().size());
        return saved.getEventId();
    }
}
