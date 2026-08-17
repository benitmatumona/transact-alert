package com.transactalert.fraud.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import com.transactalert.common.event.TransactionEvent;
import com.transactalert.fraud.rules.FraudRuleEngine;
import com.transactalert.common.event.FraudAlertEvent;
import com.transactalert.fraud.producer.FraudAlertProducer;


@Component
public class FraudConsumer{
    private final FraudRuleEngine fraudRuleEngine;
    private final FraudAlertProducer fraudAlertProducer;    

    public FraudConsumer(
        FraudRuleEngine fraudRuleEngine, 
        FraudAlertProducer fraudAlertProducer
    ){
        this.fraudRuleEngine = fraudRuleEngine;
        this.fraudAlertProducer = fraudAlertProducer;
    }

    @JmsListener(destination = "fraud.queue")
    public void receiveTransaction(TransactionEvent event){
        int score = fraudRuleEngine.calculateRiskScore(event);
        if (score >= 50){
            System.out.println(
            "ALERT: Transaction " + event.getTransactionId() + " flagged with risk score " + score
            );
            FraudAlertEvent alertEvent = new FraudAlertEvent(
                event.getTransactionId(),
                event.getCustomerId(), 
                event.getAmount(), 
                event.getMerchant(), 
                event.getLocation(),
                score
            );
            fraudAlertProducer.sendAlert(alertEvent);
        }
        else System.out.println(
             "Transaction " + event.getTransactionId() + " passed fraud check"
        );
    }
}
