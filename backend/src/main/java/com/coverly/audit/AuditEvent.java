package com.coverly.audit;

import java.time.LocalDateTime;

public record AuditEvent(
        Long id,
        String eventType,
        String referenceId,
        String description,
        LocalDateTime timestamp) {
}
