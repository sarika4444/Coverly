package com.coverly.sentinel;

import com.coverly.billing.Billing;
import com.coverly.billing.BillingService;
import com.coverly.claim.Claim;
import com.coverly.claim.ClaimService;
import com.coverly.common.BadRequestException;
import com.coverly.customer.Customer;
import com.coverly.customer.CustomerService;
import com.coverly.policy.Policy;
import com.coverly.policy.PolicyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SentinelService {

    private final PolicyService policyService;
    private final ClaimService claimService;
    private final BillingService billingService;
    private final CustomerService customerService;

    public SentinelService(PolicyService policyService,
                           ClaimService claimService,
                           BillingService billingService,
                           CustomerService customerService) {
        this.policyService = policyService;
        this.claimService = claimService;
        this.billingService = billingService;
        this.customerService = customerService;
    }

    /**
     * Assesses an individual insurance claim.
     *
     * This evaluates the claim amount, insured value,
     * previous claims and billing behavior.
     */
    public RiskAssessment assessClaim(String customerNumber,
                                      String policyNumber,
                                      double claimAmount) {

        Policy policy = policyService.getAllPolicies().stream()
                .filter(p -> p.getPolicyNumber()
                        .equalsIgnoreCase(policyNumber))
                .findFirst()
                .orElseThrow(() ->
                        new BadRequestException(
                                "Policy not found: " + policyNumber
                        )
                );

        List<Claim> previousClaims =
                claimService.findByCustomerNumber(customerNumber);

        List<Billing> billings =
                billingService.findByCustomerNumber(customerNumber);

        int risk = 0;
        int fraud = 0;

        List<String> flags = new ArrayList<>();
        List<String> reasons = new ArrayList<>();

        // Rule 1: Policy must be active.
        if (!"ACTIVE".equalsIgnoreCase(policy.getStatus())) {
            risk += 40;

            flags.add("POLICY_NOT_ACTIVE");
            reasons.add("Policy is not active");
        }

        // Rule 2: Compare claim amount with insured value.
        double ratio =
                claimAmount / Math.max(policy.getInsuredValue(), 1);

        if (ratio > 0.70) {
            risk += 30;
            fraud += 25;

            flags.add("HIGH_CLAIM_RATIO");
            reasons.add(
                    "Claim exceeds 70% of insured value"
            );

        } else if (ratio > 0.50) {
            risk += 15;
            fraud += 10;

            flags.add("ELEVATED_CLAIM_RATIO");
            reasons.add(
                    "Claim exceeds 50% of insured value"
            );
        }

        // Rule 3: High-value claim.
        if (claimAmount > 500000) {
            risk += 20;
            fraud += 25;

            flags.add("HIGH_CLAIM_AMOUNT");
            reasons.add(
                    "Claim amount exceeds ₹5,00,000"
            );
        }

        // Rule 4: Frequent previous claims.
        if (previousClaims.size() >= 3) {
            risk += 20;
            fraud += 20;

            flags.add("FREQUENT_CLAIMS");
            reasons.add(
                    "Customer has three or more previous claims"
            );
        }

        // Rule 5: Overdue billing.
        boolean overdue = billings.stream()
                .anyMatch(b ->
                        "OVERDUE".equalsIgnoreCase(
                                b.getPaymentStatus()
                        )
                );

        if (overdue) {
            risk += 15;

            flags.add("PAYMENT_OVERDUE");
            reasons.add(
                    "Customer has an overdue invoice"
            );
        }

        // Rule 6: Previous high-value claim.
        if (previousClaims.stream().anyMatch(c ->
                c.getClaimAmount() > 500000)) {

            fraud += 15;

            flags.add("PREVIOUS_HIGH_VALUE_CLAIM");
            reasons.add(
                    "Customer has a previous high-value claim"
            );
        }

        risk = Math.min(risk, 100);
        fraud = Math.min(fraud, 100);

        String level =
                risk >= 80 ? "CRITICAL"
                        : risk >= 60 ? "HIGH"
                        : risk >= 30 ? "MEDIUM"
                        : "LOW";

        String decision =
                (risk >= 60 || fraud >= 70)
                        ? "MANUAL_REVIEW"
                        : "APPROVE";

        return new RiskAssessment(
                risk,
                fraud,
                level,
                decision,
                flags,
                reasons
        );
    }

    /**
     * Assesses the overall risk of a customer.
     *
     * This is different from assessClaim().
     * It evaluates the customer's historical behavior.
     *
     * The calculated customer risk level is also
     * synchronized back to Customer.riskLevel.
     */
    public RiskAssessment assessCustomer(
            String customerNumber) {

        List<Claim> claims =
                claimService.findByCustomerNumber(customerNumber);

        List<Billing> billings =
                billingService.findByCustomerNumber(customerNumber);

        int risk = 0;
        int fraud = 0;

        List<String> flags = new ArrayList<>();
        List<String> reasons = new ArrayList<>();

        // Rule 1: Multiple claims.
        if (claims.size() >= 3) {
            risk += 30;
            fraud += 20;

            flags.add("MULTIPLE_CLAIMS");
            reasons.add(
                    "Customer has three or more claims"
            );
        }

        // Rule 2: High-value claims.
        long highValueClaims = claims.stream()
                .filter(c ->
                        c.getClaimAmount() >= 500000
                )
                .count();

        if (highValueClaims > 0) {
            risk += 20;
            fraud += 25;

            flags.add("HIGH_VALUE_CLAIMS");
            reasons.add(
                    "Customer has high-value claims"
            );
        }

        // Rule 3: Overdue billing.
        if (billings.stream().anyMatch(b ->
                "OVERDUE".equalsIgnoreCase(
                        b.getPaymentStatus()
                ))) {

            risk += 15;

            flags.add("OVERDUE_BILLING");
            reasons.add(
                    "Customer has overdue billing"
            );
        }

        risk = Math.min(risk, 100);
        fraud = Math.min(fraud, 100);

        String level =
                risk >= 80 ? "CRITICAL"
                        : risk >= 60 ? "HIGH"
                        : risk >= 30 ? "MEDIUM"
                        : "LOW";

        String decision =
                (risk >= 60 || fraud >= 70)
                        ? "MANUAL_REVIEW"
                        : "APPROVE";

        RiskAssessment assessment =
                new RiskAssessment(
                        risk,
                        fraud,
                        level,
                        decision,
                        flags,
                        reasons
                );

        /*
         * Synchronize the customer's stored risk level
         * with the latest Sentinel customer assessment.
         */
        Customer customer =
                customerService.findByCustomerNumber(
                        customerNumber
                );

        customer.setRiskLevel(
                assessment.riskLevel()
        );

        return assessment;
    }
}