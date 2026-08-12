package com.coverly.sentinel;

import java.util.List;

public class RiskResult {

    private int riskScore;
    private String decision;
    private List<String> reasons;

    public RiskResult() {
    }

    public RiskResult(int riskScore, String decision, List<String> reasons) {
        this.riskScore = riskScore;
        this.decision = decision;
        this.reasons = reasons;
    }

    public int getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(int riskScore) {
        this.riskScore = riskScore;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }
}