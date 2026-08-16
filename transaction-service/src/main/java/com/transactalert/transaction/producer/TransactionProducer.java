package com.transactalert.transaction.producer;

import com.transactalert.common.event.TransactionEvent;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransactionProducer {

    private final JmsTemplate jmsTemplate;

    public TransactionProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendTransaction(TransactionEvent event) {
        jmsTemplate.convertAndSend("fraud.queue", event);
    }
}
