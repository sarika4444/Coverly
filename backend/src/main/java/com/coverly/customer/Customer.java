package com.coverly.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class Customer {

    private Long id;

    private String customerNumber;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone;

    private String city;
    private String riskLevel = "UNKNOWN";

    public Customer() {}

    public Customer(Long id, String customerNumber, String name, String email,
                    String phone, String city, String riskLevel) {
        this.id = id;
        this.customerNumber = customerNumber;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.riskLevel = riskLevel;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerNumber() { return customerNumber; }
    public void setCustomerNumber(String customerNumber) { this.customerNumber = customerNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
}
