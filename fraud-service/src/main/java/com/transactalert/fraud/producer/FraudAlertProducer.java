package com.transactalert.fraud.producer;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import com.transactalert.common.event.FraudAlertEvent;


@Component
public class FraudAlertProducer{
    private final JmsTemplate jmsTemplate;

    public FraudAlertProducer(JmsTemplate jmsTemplate){
        this.jmsTemplate = jmsTemplate; 
    } 

    public void sendAlert(FraudAlertEvent event){
        jmsTemplate.convertAndSend("fraud.alerts", event);
    }
}

