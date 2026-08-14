package com.transactalert.common.event;

import java.io.Serializable;

public class FraudAlertEvent implements Serializable {

    private final String transactionId;
    private final String customerId;
    private final double amount;
    private final String merchant;
    private final String location;
    private final int riskScore;

    public FraudAlertEvent(
            String transactionId,
            String customerId,
            double amount,
            String merchant,
            String location,
            int riskScore
    ) {
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.amount = amount;
        this.merchant = merchant;
        this.location = location;
        this.riskScore = riskScore;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getAmount() {
        return amount;
    }

    public String getMerchant() {
        return merchant;
    }

    public String getLocation() {
        return location;
    }
    public int getRiskScore() {
        return riskScore;
    }

}
