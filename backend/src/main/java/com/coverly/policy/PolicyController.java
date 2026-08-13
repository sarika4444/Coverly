package com.coverly.policy;

import com.coverly.audit.AuditService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    private final PolicyService policyService;
    private final AuditService auditService;

    public PolicyController(PolicyService policyService, AuditService auditService) {
        this.policyService = policyService;
        this.auditService = auditService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Policy create(@Valid @RequestBody Policy policy) {
        Policy created = policyService.createPolicy(policy);
        auditService.record("POLICY_CREATED", created.getPolicyNumber(),
                "Policy created for " + created.getCustomerNumber());
        return created;
    }

    @GetMapping
    public List<Policy> getAll() {
        return policyService.getAllPolicies();
    }

    @GetMapping("/{id}")
    public Policy getById(@PathVariable Long id) {
        return policyService.getPolicyById(id);
    }

    @GetMapping("/customer/{customerNumber}")
    public List<Policy> byCustomer(@PathVariable String customerNumber) {
        return policyService.findByCustomerNumber(customerNumber);
    }

    @PatchMapping("/{id}/status")
    public Policy changeStatus(@PathVariable Long id,
                                @RequestParam String status) {
        Policy updated = policyService.changeStatus(id, status);
        auditService.record("POLICY_STATUS_CHANGED", updated.getPolicyNumber(),
                "Policy status changed to " + updated.getStatus());
        return updated;
    }
}
