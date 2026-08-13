package com.coverly.billing;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Billing create(@Valid @RequestBody Billing billing) {
        return billingService.createBilling(billing);
    }

    @GetMapping
    public List<Billing> getAll() {
        return billingService.getAllBillings();
    }

    @GetMapping("/{id}")
    public Billing getById(@PathVariable Long id) {
        return billingService.getBillingById(id);
    }

    @GetMapping("/customer/{customerNumber}")
    public List<Billing> byCustomer(@PathVariable String customerNumber) {
        return billingService.findByCustomerNumber(customerNumber);
    }

    @PostMapping("/{id}/payment")
    public Billing makePayment(@PathVariable Long id,
                                @RequestParam double amount) {
        return billingService.makePayment(id, amount);
    }
}
