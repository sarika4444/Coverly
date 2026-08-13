package com.coverly.config;

import com.coverly.billing.Billing;
import com.coverly.billing.BillingService;
import com.coverly.claim.Claim;
import com.coverly.claim.ClaimService;
import com.coverly.customer.CustomerService;
import com.coverly.policy.Policy;
import com.coverly.policy.PolicyService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DemoDataLoader implements CommandLineRunner {

    private final CustomerService customerService;
    private final PolicyService policyService;
    private final BillingService billingService;
    private final ClaimService claimService;

    public DemoDataLoader(CustomerService customerService,
                          PolicyService policyService,
                          BillingService billingService,
                          ClaimService claimService) {
        this.customerService = customerService;
        this.policyService = policyService;
        this.billingService = billingService;
        this.claimService = claimService;
    }

    @Override
    public void run(String... args) {

        if (!customerService.findAll().isEmpty()) {
            return;
        }

        // --------------------------------------------------
        // 1. Customers
        // --------------------------------------------------

        var rahul = customerService.create(
                new com.coverly.customer.Customer(
                        null,
                        null,
                        "Rahul Sharma",
                        "rahul@coverly.com",
                        "9876543210",
                        "Bengaluru",
                        "MEDIUM"
                )
        );

        var ananya = customerService.create(
                new com.coverly.customer.Customer(
                        null,
                        null,
                        "Ananya Rao",
                        "ananya@coverly.com",
                        "9876501234",
                        "Hyderabad",
                        "LOW"
                )
        );

        // --------------------------------------------------
        // 2. Rahul's Motor Policy
        // --------------------------------------------------

        Policy p1 = policyService.createPolicy(
                new Policy(
                        null,
                        null,
                        rahul.getCustomerNumber(),
                        rahul.getName(),
                        "MOTOR",
                        1000000,
                        45000,
                        "QUOTED"
                )
        );

        policyService.changeStatus(p1.getId(), "ISSUED");
        policyService.changeStatus(p1.getId(), "ACTIVE");

        // --------------------------------------------------
        // 3. Ananya's Home Policy
        // --------------------------------------------------

        Policy p2 = policyService.createPolicy(
                new Policy(
                        null,
                        null,
                        ananya.getCustomerNumber(),
                        ananya.getName(),
                        "HOME",
                        2000000,
                        60000,
                        "QUOTED"
                )
        );

        policyService.changeStatus(p2.getId(), "ISSUED");
        policyService.changeStatus(p2.getId(), "ACTIVE");

        // --------------------------------------------------
        // 4. Billing
        // --------------------------------------------------

        // Rahul has only partially paid his billing.
        billingService.createBilling(
                new Billing(
                        null,
                        null,
                        p1.getPolicyNumber(),
                        null,
                        0,
                        10000,
                        0,
                        null
                )
        );

        // Ananya has fully paid her billing.
        billingService.createBilling(
                new Billing(
                        null,
                        null,
                        p2.getPolicyNumber(),
                        null,
                        0,
                        60000,
                        0,
                        null
                )
        );

        // --------------------------------------------------
        // 5. Demo Claim
        // --------------------------------------------------

        // High-value claim intentionally included so
        // Coverly Sentinel has something interesting to evaluate.
        //
        // The claim will be submitted through the API rather
        // than directly here because claim creation requires
        // Sentinel risk evaluation.

    }
}