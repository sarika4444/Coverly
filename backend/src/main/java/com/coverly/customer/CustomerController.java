package com.coverly.customer;
import com.coverly.claim.ClaimService;
import com.coverly.billing.BillingService;
import com.coverly.policy.PolicyService;
import com.coverly.sentinel.SentinelService;
import com.coverly.audit.AuditService;
import com.coverly.audit.AuditEvent;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final PolicyService policyService;
    private final ClaimService claimService;
    private final BillingService billingService;
    private final SentinelService sentinelService;
    private final AuditService auditService;

    public CustomerController(CustomerService customerService,
                              PolicyService policyService,
                              ClaimService claimService,
                              BillingService billingService,
                              SentinelService sentinelService,
                              AuditService auditService) {
        this.customerService = customerService;
        this.policyService = policyService;
        this.claimService = claimService;
        this.billingService = billingService;
        this.sentinelService = sentinelService;
        this.auditService = auditService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Customer create(@Valid @RequestBody Customer customer) {
        Customer created = customerService.create(customer);

        auditService.record(
                "CUSTOMER_CREATED",
                created.getCustomerNumber(),
                "Customer " + created.getName() + " created"
        );

        return created;
    }

    @GetMapping
    public List<Customer> findAll() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public Customer findById(@PathVariable Long id) {
        return customerService.findById(id);
    }

    @PutMapping("/{id}")
    public Customer update(@PathVariable Long id,
                           @Valid @RequestBody Customer customer) {

        Customer updated = customerService.update(id, customer);

        auditService.record(
                "CUSTOMER_UPDATED",
                updated.getCustomerNumber(),
                "Customer profile updated"
        );

        return updated;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {

        Customer customer = customerService.findById(id);

        customerService.delete(id);

        auditService.record(
                "CUSTOMER_DELETED",
                customer.getCustomerNumber(),
                "Customer deleted"
        );
    }

    @GetMapping("/{customerNumber}/360")
    public Customer360Response customer360(
            @PathVariable String customerNumber) {

        Customer customer =
                customerService.findByCustomerNumber(customerNumber);

        var policies =
                policyService.findByCustomerNumber(customerNumber);

        var claims =
                claimService.findByCustomerNumber(customerNumber);

        var billings =
                billingService.findByCustomerNumber(customerNumber);

        var risk =
                sentinelService.assessCustomer(customerNumber);

        return new Customer360Response(
                customer,
                policies,
                claims,
                billings,
                risk
        );
    }

    @GetMapping("/{customerNumber}/timeline")
    public List<AuditEvent> customerTimeline(
            @PathVariable String customerNumber) {

        Customer customer =
                customerService.findByCustomerNumber(customerNumber);

        var policies =
                policyService.findByCustomerNumber(customerNumber);

        var claims =
                claimService.findByCustomerNumber(customerNumber);

        var billings =
                billingService.findByCustomerNumber(customerNumber);

        Set<String> references = new HashSet<>();

        // Customer-level events
        references.add(customer.getCustomerNumber());

        // Policy-level events
        policies.forEach(policy ->
                references.add(policy.getPolicyNumber())
        );

        // Claim-level events
        claims.forEach(claim ->
                references.add(claim.getClaimNumber())
        );

        // Billing-level events
        billings.forEach(billing ->
                references.add(billing.getBillingId())
        );

        List<AuditEvent> timeline = new ArrayList<>();

        for (AuditEvent event : auditService.findAll()) {
            if (references.contains(event.referenceId())) {
                timeline.add(event);
            }
        }

        return timeline;
    }
}