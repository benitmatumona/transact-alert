package com.transactalert.notification.consumer;

import com.transactalert.common.event.FraudAlertEvent;
import org.springframework.stereotype.Component;
import org.springframework.jms.annotation.JmsListener;



@Component
public class NotificationConsumer{

    @JmsListener(destination = "fraud.alerts")
    public void receiveAlert(FraudAlertEvent event){
        System.out.println(
            "Notifying fraud analyst: Transaction " + event.getTransactionId() + 
            " flagged with risk score " + event.getRiskScore()
        );
    }
}
