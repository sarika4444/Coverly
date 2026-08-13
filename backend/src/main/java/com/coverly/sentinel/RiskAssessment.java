package com.coverly.sentinel;

import java.util.List;

public record RiskAssessment(
        int riskScore,
        int fraudScore,
        String riskLevel,
        String decision,
        List<String> flags,
        List<String> reasons) {
}
