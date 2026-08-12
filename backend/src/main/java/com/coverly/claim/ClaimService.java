package com.coverly.claim;

import com.coverly.policy.Policy;
import com.coverly.policy.PolicyService;
import com.coverly.sentinel.RiskEngine;
import com.coverly.sentinel.RiskResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClaimService {

    private final List<Claim> claims = new ArrayList<>();

    private final PolicyService policyService;

    private final RiskEngine riskEngine;

    public ClaimService(
            PolicyService policyService,
            RiskEngine riskEngine) {

        this.policyService = policyService;
        this.riskEngine = riskEngine;
    }

    public Claim createClaim(Claim claim) {

        Policy policy = findPolicy(claim.getPolicyNumber());

        if (policy == null) {
            throw new RuntimeException(
                    "Policy not found: " + claim.getPolicyNumber()
            );
        }

        claim.setId((long) (claims.size() + 1));

        claims.add(claim);

        return claim;
    }

    public List<Claim> getAllClaims() {
        return claims;
    }

    public Claim getClaimById(Long id) {

        return claims.stream()
                .filter(claim -> claim.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private Policy findPolicy(String policyNumber) {

        return policyService.getAllPolicies()
                .stream()
                .filter(policy ->
                        policy.getPolicyNumber()
                                .equalsIgnoreCase(policyNumber))
                .findFirst()
                .orElse(null);
    }

    public RiskResult evaluateClaim(Long claimId) {

        Claim claim = getClaimById(claimId);

        if (claim == null) {
            throw new RuntimeException(
                    "Claim not found: " + claimId
            );
        }

        Policy policy = findPolicy(claim.getPolicyNumber());

        return riskEngine.evaluate(
                claim.getClaimAmount(),
                policy.getInsuredValue(),
                policy.getStatus()
        );
    }
}
