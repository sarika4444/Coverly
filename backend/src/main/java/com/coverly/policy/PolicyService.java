package com.coverly.policy;

import com.coverly.common.BadRequestException;
import com.coverly.common.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PolicyService {

    private final List<Policy> policies = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong();

    public Policy createPolicy(Policy policy) {
        policy.setId(sequence.incrementAndGet());
        policy.setPolicyNumber("POL-" + String.format("%05d", policy.getId()));
        if (policy.getStatus() == null || policy.getStatus().isBlank()) {
            policy.setStatus("QUOTED");
        }
        policies.add(policy);
        return policy;
    }

    public List<Policy> getAllPolicies() {
        return policies;
    }

    public Policy getPolicyById(Long id) {
        return policies.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Policy not found: " + id));
    }

    public List<Policy> findByCustomerNumber(String customerNumber) {
        return policies.stream()
                .filter(p -> p.getCustomerNumber().equalsIgnoreCase(customerNumber))
                .toList();
    }

    public Policy changeStatus(Long id, String newStatus) {
        Policy policy = getPolicyById(id);
        String current = policy.getStatus();

        if (!isValidTransition(current, newStatus)) {
            throw new BadRequestException(
                    "Invalid policy transition: " + current + " -> " + newStatus);
        }

        policy.setStatus(newStatus.toUpperCase());
        return policy;
    }

    private boolean isValidTransition(String current, String target) {
        target = target.toUpperCase();

        return switch (current.toUpperCase()) {
            case "QUOTED" -> target.equals("ISSUED") || target.equals("CANCELLED");
            case "ISSUED" -> target.equals("ACTIVE") || target.equals("CANCELLED");
            case "ACTIVE" -> target.equals("SUSPENDED") ||
                    target.equals("CANCELLED") || target.equals("EXPIRED");
            case "SUSPENDED" -> target.equals("ACTIVE") || target.equals("CANCELLED");
            default -> false;
        };
    }
}
