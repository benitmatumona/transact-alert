package com.transactalert.fraud.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import com.transactalert.common.event.TransactionEvent;
import com.transactalert.fraud.rules.FraudRuleEngine;


@Component
public class FraudConsumer{
    private final FraudRuleEngine fraudRuleEngine;
    
    public FraudConsumer(FraudRuleEngine fraudRuleEngine){
        this.fraudRuleEngine = fraudRuleEngine;
    }

    @JmsListener(destination = "fraud.queue")
    public void receiveTransaction(TransactionEvent event){
        int score = fraudRuleEngine.calculateRiskScore(event);
        if (score >= 50) System.out.println(
            "ALERT: Transaction " + event.getTransactionId() + " flagged with risk score " + score
        );
        else System.out.println(
             "Transaction " + event.getTransactionId() + " passed fraud check"
        );
    }
}
