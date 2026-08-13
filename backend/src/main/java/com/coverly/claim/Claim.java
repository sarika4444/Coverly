package com.coverly.claim;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class Claim {

    private Long id;
    private String claimNumber;

    @NotBlank
    private String policyNumber;

    @NotBlank
    private String customerNumber;

    @NotBlank
    private String customerName;

    @NotBlank
    private String incidentType;

    @Positive
    private double claimAmount;

    private String status = "SUBMITTED";
    private int riskScore;
    private int fraudScore;
    private String decision;

    public Claim() {}

    public Claim(Long id, String claimNumber, String policyNumber, String customerNumber,
                 String customerName, String incidentType, double claimAmount,
                 String status, int riskScore, int fraudScore, String decision) {
        this.id = id;
        this.claimNumber = claimNumber;
        this.policyNumber = policyNumber;
        this.customerNumber = customerNumber;
        this.customerName = customerName;
        this.incidentType = incidentType;
        this.claimAmount = claimAmount;
        this.status = status;
        this.riskScore = riskScore;
        this.fraudScore = fraudScore;
        this.decision = decision;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClaimNumber() { return claimNumber; }
    public void setClaimNumber(String claimNumber) { this.claimNumber = claimNumber; }

    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }

    public String getCustomerNumber() { return customerNumber; }
    public void setCustomerNumber(String customerNumber) { this.customerNumber = customerNumber; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getIncidentType() { return incidentType; }
    public void setIncidentType(String incidentType) { this.incidentType = incidentType; }

    public double getClaimAmount() { return claimAmount; }
    public void setClaimAmount(double claimAmount) { this.claimAmount = claimAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getRiskScore() { return riskScore; }
    public void setRiskScore(int riskScore) { this.riskScore = riskScore; }

    public int getFraudScore() { return fraudScore; }
    public void setFraudScore(int fraudScore) { this.fraudScore = fraudScore; }

    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }
}
