package com.coverly.policy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class Policy {

    private Long id;
    private String policyNumber;

    @NotBlank
    private String customerNumber;

    @NotBlank
    private String customerName;

    @NotBlank
    private String policyType;

    @Positive
    private double insuredValue;

    @Positive
    private double premium;

    private String status = "QUOTED";

    public Policy() {}

    public Policy(Long id, String policyNumber, String customerNumber, String customerName,
                   String policyType, double insuredValue, double premium, String status) {
        this.id = id;
        this.policyNumber = policyNumber;
        this.customerNumber = customerNumber;
        this.customerName = customerName;
        this.policyType = policyType;
        this.insuredValue = insuredValue;
        this.premium = premium;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }

    public String getCustomerNumber() { return customerNumber; }
    public void setCustomerNumber(String customerNumber) { this.customerNumber = customerNumber; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getPolicyType() { return policyType; }
    public void setPolicyType(String policyType) { this.policyType = policyType; }

    public double getInsuredValue() { return insuredValue; }
    public void setInsuredValue(double insuredValue) { this.insuredValue = insuredValue; }

    public double getPremium() { return premium; }
    public void setPremium(double premium) { this.premium = premium; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
