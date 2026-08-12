package com.coverly.claim;
import com.coverly.sentinel.RiskEngine;
import com.coverly.sentinel.RiskResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    // Create a new claim
    @PostMapping
    public Claim createClaim(@RequestBody Claim claim) {
        return claimService.createClaim(claim);
    }

    // Get all claims
    @GetMapping
    public List<Claim> getAllClaims() {
        return claimService.getAllClaims();
    }

    // Get a claim by ID
    @GetMapping("/{id}")
    public Claim getClaimById(@PathVariable Long id) {
        return claimService.getClaimById(id);
    }

    // Evaluate claim risk using Coverly Sentinel
    @GetMapping("/{id}/risk")
    public RiskResult evaluateClaimRisk(@PathVariable Long id) {
        return claimService.evaluateClaim(id);
    }
}