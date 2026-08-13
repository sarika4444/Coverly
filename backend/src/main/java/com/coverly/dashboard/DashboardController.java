package com.coverly.dashboard;

import com.coverly.billing.BillingService;
import com.coverly.claim.ClaimService;
import com.coverly.customer.CustomerService;
import com.coverly.policy.PolicyService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final CustomerService customerService;
    private final PolicyService policyService;
    private final ClaimService claimService;
    private final BillingService billingService;

    public DashboardController(CustomerService customerService,
                               PolicyService policyService,
                               ClaimService claimService,
                               BillingService billingService) {
        this.customerService = customerService;
        this.policyService = policyService;
        this.claimService = claimService;
        this.billingService = billingService;
    }

    @GetMapping("/summary")
    public Map<String, Object> summary() {
        long activePolicies = policyService.getAllPolicies().stream()
                .filter(p -> "ACTIVE".equalsIgnoreCase(p.getStatus()))
                .count();

        long claimsUnderReview = claimService.getAllClaims().stream()
                .filter(c -> "UNDER_REVIEW".equalsIgnoreCase(c.getStatus()))
                .count();

        double outstanding = billingService.getAllBillings().stream()
                .mapToDouble(b -> b.getAmountDue())
                .sum();

        return Map.of(
                "customers", customerService.findAll().size(),
                "policies", policyService.getAllPolicies().size(),
                "activePolicies", activePolicies,
                "claims", claimService.getAllClaims().size(),
                "claimsUnderReview", claimsUnderReview,
                "billingRecords", billingService.getAllBillings().size(),
                "outstandingAmount", outstanding
        );
    }
}
