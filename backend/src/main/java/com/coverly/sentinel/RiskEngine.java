package com.coverly.sentinel;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RiskEngine {

    public RiskResult evaluate(
            double claimAmount,
            double insuredValue,
            String policyStatus) {

        int riskScore = 0;

        List<String> reasons = new ArrayList<>();

        // Rule 1: Policy must be active
        if (!"ACTIVE".equalsIgnoreCase(policyStatus)) {
            riskScore += 100;
            reasons.add("Policy is not active");
        }

        // Rule 2: Claim amount compared with insured value
        double claimRatio = claimAmount / insuredValue;

        if (claimRatio > 0.70) {

            riskScore += 50;

            reasons.add(
                    "Claim exceeds 70% of insured value"
            );

        } else if (claimRatio > 0.50) {

            riskScore += 25;

            reasons.add(
                    "Claim exceeds 50% of insured value"
            );
        }

        // Keep score within 0-100
        riskScore = Math.min(riskScore, 100);

        String decision;

        if (riskScore >= 80) {
            decision = "HIGH_RISK";

        } else if (riskScore >= 40) {
            decision = "MANUAL_REVIEW";

        } else {
            decision = "AUTO_APPROVE";
        }

        return new RiskResult(
                riskScore,
                decision,
                reasons
        );
    }
}