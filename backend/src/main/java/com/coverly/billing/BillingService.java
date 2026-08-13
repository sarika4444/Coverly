package com.coverly.billing;

import com.coverly.audit.AuditService;
import com.coverly.common.BadRequestException;
import com.coverly.common.NotFoundException;
import com.coverly.policy.Policy;
import com.coverly.policy.PolicyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BillingService {

    private final List<Billing> billings = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong();

    private final PolicyService policyService;
    private final AuditService auditService;

    public BillingService(PolicyService policyService, AuditService auditService) {
        this.policyService = policyService;
        this.auditService = auditService;
    }

    public Billing createBilling(Billing billing) {
        Policy policy = policyService.getAllPolicies().stream()
                .filter(p -> p.getPolicyNumber().equalsIgnoreCase(billing.getPolicyNumber()))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(
                        "Policy not found: " + billing.getPolicyNumber()));

        billing.setId(sequence.incrementAndGet());
        billing.setBillingId("INV-" + String.format("%05d", billing.getId()));
        billing.setCustomerNumber(policy.getCustomerNumber());
        billing.setPremium(policy.getPremium());

        double paid = Math.max(0, billing.getAmountPaid());
        billing.setAmountPaid(Math.min(paid, policy.getPremium()));

        double due = Math.max(0, policy.getPremium() - billing.getAmountPaid());
        billing.setAmountDue(due);

        if (due == 0) {
            billing.setPaymentStatus("PAID");
        } else if (billing.getAmountPaid() > 0) {
            billing.setPaymentStatus("PARTIALLY_PAID");
        } else {
            billing.setPaymentStatus("OVERDUE");
        }

        billings.add(billing);

        auditService.record("BILLING_CREATED", billing.getBillingId(),
                "Invoice created for " + billing.getPolicyNumber());

        return billing;
    }

    public List<Billing> getAllBillings() {
        return billings;
    }

    public Billing getBillingById(Long id) {
        return billings.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Billing record not found: " + id));
    }

    public List<Billing> findByCustomerNumber(String customerNumber) {
        return billings.stream()
                .filter(b -> b.getCustomerNumber().equalsIgnoreCase(customerNumber))
                .toList();
    }

    public Billing makePayment(Long id, double amount) {
        if (amount <= 0) {
            throw new BadRequestException("Payment amount must be greater than zero");
        }

        Billing billing = getBillingById(id);
        double newPaid = Math.min(billing.getPremium(), billing.getAmountPaid() + amount);

        billing.setAmountPaid(newPaid);
        billing.setAmountDue(Math.max(0, billing.getPremium() - newPaid));

        if (billing.getAmountDue() == 0) {
            billing.setPaymentStatus("PAID");
        } else {
            billing.setPaymentStatus("PARTIALLY_PAID");
        }

        auditService.record("PAYMENT_RECEIVED", billing.getBillingId(),
                "Payment received: " + amount);

        return billing;
    }
}
