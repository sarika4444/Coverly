package com.coverly.claim;

import com.coverly.audit.AuditService;
import com.coverly.common.NotFoundException;
import com.coverly.policy.Policy;
import com.coverly.policy.PolicyService;
import com.coverly.sentinel.RiskAssessment;
import com.coverly.sentinel.SentinelService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ClaimService {

    private final List<Claim> claims = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong();

    private final PolicyService policyService;
    private final AuditService auditService;

    public ClaimService(PolicyService policyService,
                        AuditService auditService) {
        this.policyService = policyService;
        this.auditService = auditService;
    }

    public Claim create(Claim claim, SentinelService sentinelService) {

        Policy policy = findPolicy(claim.getPolicyNumber());

        if (policy == null) {
            throw new NotFoundException(
                    "Policy not found: " + claim.getPolicyNumber()
            );
        }

        if (!policy.getCustomerNumber()
                .equalsIgnoreCase(claim.getCustomerNumber())) {

            throw new IllegalArgumentException(
                    "Claim customer does not match policy customer"
            );
        }

        if (!"ACTIVE".equalsIgnoreCase(policy.getStatus())) {
            throw new IllegalArgumentException(
                    "Claims can only be submitted against ACTIVE policies"
            );
        }

        claim.setId(sequence.incrementAndGet());
        claim.setClaimNumber(
                "CLM-" + String.format("%05d", claim.getId())
        );

        RiskAssessment risk = sentinelService.assessClaim(
                claim.getCustomerNumber(),
                claim.getPolicyNumber(),
                claim.getClaimAmount()
        );

        claim.setRiskScore(risk.riskScore());
        claim.setFraudScore(risk.fraudScore());
        claim.setDecision(risk.decision());

        if ("APPROVE".equalsIgnoreCase(risk.decision())) {
            claim.setStatus("APPROVED");
        } else {
            claim.setStatus("UNDER_REVIEW");
        }

        claims.add(claim);

// Recalculate overall customer risk
// after the new claim has been added.
        sentinelService.assessCustomer(
                claim.getCustomerNumber()
        );

        auditService.record(
                "CLAIM_SUBMITTED",
                claim.getClaimNumber(),
                "Claim submitted with decision "
                        + claim.getDecision()
                        + " and risk score "
                        + claim.getRiskScore()
        );

        return claim;
    }

    public List<Claim> findAll() {
        return claims;
    }
    public List<Claim> getAllClaims() {
        return claims;
    }

    public Claim findById(Long id) {
        return claims.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new NotFoundException("Claim not found: " + id)
                );
    }

    public List<Claim> findByCustomerNumber(String customerNumber) {
        return claims.stream()
                .filter(c -> c.getCustomerNumber()
                        .equalsIgnoreCase(customerNumber))
                .toList();
    }

    public Claim updateStatus(Long id, String status) {

        Claim claim = findById(id);

        String normalized = status.toUpperCase();

        if (!List.of(
                "SUBMITTED",
                "UNDER_REVIEW",
                "APPROVED",
                "REJECTED",
                "SETTLED"
        ).contains(normalized)) {

            throw new IllegalArgumentException(
                    "Invalid claim status: " + status
            );
        }

        claim.setStatus(normalized);

        auditService.record(
                "CLAIM_STATUS_CHANGED",
                claim.getClaimNumber(),
                "Claim status changed to " + normalized
        );

        return claim;
    }

    private Policy findPolicy(String policyNumber) {

        return policyService.getAllPolicies().stream()
                .filter(policy ->
                        policy.getPolicyNumber()
                                .equalsIgnoreCase(policyNumber))
                .findFirst()
                .orElse(null);
    }
}