package com.coverly.billing;

public class Billing {

    private Long id;
    private String billingId;
    private String policyNumber;

    private Double premium;
    private Double amountPaid;
    private Double amountDue;

    private String paymentStatus;

    public Billing() {
    }

    public Billing(
            Long id,
            String billingId,
            String policyNumber,
            Double premium,
            Double amountPaid,
            Double amountDue,
            String paymentStatus) {

        this.id = id;
        this.billingId = billingId;
        this.policyNumber = policyNumber;
        this.premium = premium;
        this.amountPaid = amountPaid;
        this.amountDue = amountDue;
        this.paymentStatus = paymentStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillingId() {
        return billingId;
    }

    public void setBillingId(String billingId) {
        this.billingId = billingId;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public Double getPremium() {
        return premium;
    }

    public void setPremium(Double premium) {
        this.premium = premium;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Double getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(Double amountDue) {
        this.amountDue = amountDue;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}