package com.coverly.policy;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PolicyService {

    private final List<Policy> policies = new ArrayList<>();

    public Policy createPolicy(Policy policy) {

        policy.setId((long) (policies.size() + 1));

        policies.add(policy);

        return policy;
    }

    public List<Policy> getAllPolicies() {
        return policies;
    }

    public Policy getPolicyById(Long id) {

        return policies.stream()
                .filter(policy -> policy.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}