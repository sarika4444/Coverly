package com.coverly.customer;

import com.coverly.billing.Billing;
import com.coverly.claim.Claim;
import com.coverly.policy.Policy;
import com.coverly.sentinel.RiskAssessment;

import java.util.List;

public record Customer360Response(
        Customer customer,
        List<Policy> policies,
        List<Claim> claims,
        List<Billing> billings,
        RiskAssessment risk) {
}
