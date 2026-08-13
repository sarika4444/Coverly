package com.coverly.audit;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AuditService {

    private final List<AuditEvent> events = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong();

    public void record(String eventType, String referenceId, String description) {
        events.add(new AuditEvent(
                sequence.incrementAndGet(),
                eventType,
                referenceId,
                description,
                LocalDateTime.now()
        ));
    }

    public List<AuditEvent> findAll() {
        return events;
    }

    public List<AuditEvent> findByReference(String referenceId) {
        return events.stream()
                .filter(e -> e.referenceId().equalsIgnoreCase(referenceId))
                .toList();
    }
}
