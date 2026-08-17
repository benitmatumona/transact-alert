package com.transactalert.fraud.rules;

import org.springframework.stereotype.Component;
import com.transactalert.common.event.TransactionEvent;


@Component
public class FraudRuleEngine {

    public int calculateRiskScore(TransactionEvent event) {
        int score = 0;
        if (event.getAmount() > 10000) score += 50;
        if (event.getMerchant().equalsIgnoreCase("Unknown Merchant")) score += 30;
        return score;
    }
}