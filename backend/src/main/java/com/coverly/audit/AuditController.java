package com.coverly.audit;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    public List<AuditEvent> all() {
        return auditService.findAll();
    }

    @GetMapping("/{referenceId}")
    public List<AuditEvent> byReference(@PathVariable String referenceId) {
        return auditService.findByReference(referenceId);
    }
}
