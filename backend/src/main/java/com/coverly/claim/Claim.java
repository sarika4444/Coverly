package com.coverly.claim;

public class Claim {

    private Long id;
    private String claimNumber;
    private String policyNumber;
    private String customerName;
    private String incidentType;
    private double claimAmount;
    private String status;

    public Claim() {
    }

    public Claim(Long id,
                 String claimNumber,
                 String policyNumber,
                 String customerName,
                 String incidentType,
                 double claimAmount,
                 String status) {

        this.id = id;
        this.claimNumber = claimNumber;
        this.policyNumber = policyNumber;
        this.customerName = customerName;
        this.incidentType = incidentType;
        this.claimAmount = claimAmount;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(String incidentType) {
        this.incidentType = incidentType;
    }

    public double getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(double claimAmount) {
        this.claimAmount = claimAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}