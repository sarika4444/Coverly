package com.coverly.sentinel;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sentinel")
public class SentinelController {

    private final SentinelService sentinelService;

    public SentinelController(SentinelService sentinelService) {
        this.sentinelService = sentinelService;
    }

    @GetMapping("/claim")
    public RiskAssessment assessClaim(
            @RequestParam String customerNumber,
            @RequestParam String policyNumber,
            @RequestParam double claimAmount) {

        return sentinelService.assessClaim(
                customerNumber,
                policyNumber,
                claimAmount
        );
    }

    @GetMapping("/customer/{customerNumber}")
    public RiskAssessment assessCustomer(
            @PathVariable String customerNumber) {

        return sentinelService.assessCustomer(customerNumber);
    }
}
