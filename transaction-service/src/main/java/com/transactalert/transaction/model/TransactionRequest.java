package com.transactalert.transaction.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class TransactionRequest {

    @NotBlank
    private String customerId;

    @Positive
    private double amount;

    @NotBlank
    private String merchant;

    @NotBlank
    private String location;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
