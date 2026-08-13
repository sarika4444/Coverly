package com.coverly.customer;

import com.coverly.common.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CustomerService {

    private final List<Customer> customers = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong();

    public Customer create(Customer customer) {
        customer.setId(sequence.incrementAndGet());
        customer.setCustomerNumber(
                "CUS-" + String.format("%05d", customer.getId())
        );
        customers.add(customer);
        return customer;
    }

    public List<Customer> findAll() {
        return customers;
    }

    public Customer findById(Long id) {
        return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new NotFoundException(
                                "Customer not found: " + id
                        )
                );
    }

    public Customer findByCustomerNumber(String customerNumber) {
        return customers.stream()
                .filter(c -> c.getCustomerNumber()
                        .equalsIgnoreCase(customerNumber))
                .findFirst()
                .orElseThrow(() ->
                        new NotFoundException(
                                "Customer not found: " + customerNumber
                        )
                );
    }

    public Customer update(Long id, Customer input) {
        Customer existing = findById(id);

        existing.setName(input.getName());
        existing.setEmail(input.getEmail());
        existing.setPhone(input.getPhone());
        existing.setCity(input.getCity());

        return existing;
    }

    public void delete(Long id) {
        Customer customer = findById(id);
        customers.remove(customer);
    }

    public List<Customer> seedIfEmpty() {
        if (customers.isEmpty()) {
            create(new Customer(
                    null,
                    null,
                    "Rahul Sharma",
                    "rahul@coverly.com",
                    "9876543210",
                    "Bengaluru",
                    "MEDIUM"
            ));

            create(new Customer(
                    null,
                    null,
                    "Ananya Rao",
                    "ananya@coverly.com",
                    "9876501234",
                    "Hyderabad",
                    "LOW"
            ));
        }

        return customers;
    }
}