package com.coverly.billing;

import com.coverly.policy.Policy;
import com.coverly.policy.PolicyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BillingService {

    // Temporary in-memory storage
    // We will replace this with a database later.
    private final List<Billing> billings = new ArrayList<>();

    private final PolicyService policyService;

    // Constructor injection
    public BillingService(PolicyService policyService) {
        this.policyService = policyService;
    }

    // --------------------------------------------------
    // CREATE BILLING
    // --------------------------------------------------
    public Billing createBilling(Billing billing) {

        // Find the policy associated with this billing
        Policy policy = findPolicy(billing.getPolicyNumber());

        // If policy doesn't exist, don't create the billing
        if (policy == null) {
            throw new RuntimeException(
                    "Policy not found: " + billing.getPolicyNumber()
            );
        }

        // Generate billing ID
        billing.setId((long) (billings.size() + 1));

        // --------------------------------------------------
        // GET PREMIUM FROM POLICY
        // --------------------------------------------------

        billing.setPremium(policy.getPremium());

        // --------------------------------------------------
        // HANDLE AMOUNT PAID
        // --------------------------------------------------

        Double amountPaid = billing.getAmountPaid();

        // If amountPaid was not provided, treat it as 0
        if (amountPaid == null) {
            amountPaid = 0.0;
            billing.setAmountPaid(amountPaid);
        }

        // --------------------------------------------------
        // CALCULATE AMOUNT DUE
        // --------------------------------------------------

        double amountDue =
                policy.getPremium() - amountPaid;

        // Don't allow negative amount due
        if (amountDue < 0) {
            amountDue = 0;
        }

        billing.setAmountDue(amountDue);

        // --------------------------------------------------
        // DETERMINE PAYMENT STATUS
        // --------------------------------------------------

        if (amountDue == 0) {

            billing.setPaymentStatus("PAID");

        } else if (amountPaid > 0) {

            billing.setPaymentStatus("PARTIALLY_PAID");

        } else {

            billing.setPaymentStatus("UNPAID");
        }

        // --------------------------------------------------
        // SAVE BILLING
        // --------------------------------------------------

        billings.add(billing);

        return billing;
    }

    // --------------------------------------------------
    // GET ALL BILLINGS
    // --------------------------------------------------

    public List<Billing> getAllBillings() {
        return billings;
    }

    // --------------------------------------------------
    // GET BILLING BY ID
    // --------------------------------------------------

    public Billing getBillingById(Long id) {

        return billings.stream()
                .filter(billing ->
                        billing.getId().equals(id)
                )
                .findFirst()
                .orElse(null);
    }

    // --------------------------------------------------
    // FIND POLICY
    // --------------------------------------------------

    private Policy findPolicy(String policyNumber) {

        return policyService.getAllPolicies()
                .stream()
                .filter(policy ->
                        policy.getPolicyNumber()
                                .equalsIgnoreCase(policyNumber)
                )
                .findFirst()
                .orElse(null);
    }
}