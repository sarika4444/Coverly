package com.coverly.billing;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class Billing {

    private Long id;
    private String billingId;

    @NotBlank
    private String policyNumber;

    @NotBlank
    private String customerNumber;

    private double premium;

    @PositiveOrZero
    private double amountPaid;

    private double amountDue;
    private String paymentStatus;

    public Billing() {}

    public Billing(Long id, String billingId, String policyNumber, String customerNumber,
                    double premium, double amountPaid, double amountDue,
                    String paymentStatus) {
        this.id = id;
        this.billingId = billingId;
        this.policyNumber = policyNumber;
        this.customerNumber = customerNumber;
        this.premium = premium;
        this.amountPaid = amountPaid;
        this.amountDue = amountDue;
        this.paymentStatus = paymentStatus;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBillingId() { return billingId; }
    public void setBillingId(String billingId) { this.billingId = billingId; }

    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }

    public String getCustomerNumber() { return customerNumber; }
    public void setCustomerNumber(String customerNumber) { this.customerNumber = customerNumber; }

    public double getPremium() { return premium; }
    public void setPremium(double premium) { this.premium = premium; }

    public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }

    public double getAmountDue() { return amountDue; }
    public void setAmountDue(double amountDue) { this.amountDue = amountDue; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}
