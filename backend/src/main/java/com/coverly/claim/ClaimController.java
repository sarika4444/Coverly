package com.coverly.claim;

import com.coverly.sentinel.SentinelService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService claimService;
    private final SentinelService sentinelService;

    public ClaimController(ClaimService claimService,
                           SentinelService sentinelService) {
        this.claimService = claimService;
        this.sentinelService = sentinelService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Claim createClaim(@RequestBody Claim claim) {
        return claimService.create(claim, sentinelService);
    }

    @GetMapping
    public List<Claim> getAllClaims() {
        return claimService.findAll();
    }

    @GetMapping("/{id}")
    public Claim getClaim(@PathVariable Long id) {
        return claimService.findById(id);
    }

    @GetMapping("/customer/{customerNumber}")
    public List<Claim> getClaimsByCustomer(
            @PathVariable String customerNumber) {
        return claimService.findByCustomerNumber(customerNumber);
    }

    @PatchMapping("/{id}/status")
    public Claim updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return claimService.updateStatus(id, status);
    }
}