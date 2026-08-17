package com.transactalert.transaction.controller;

import com.transactalert.transaction.model.TransactionRequest;
import com.transactalert.transaction.producer.TransactionProducer;
import com.transactalert.common.event.TransactionEvent;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TransactionController {
    private final TransactionProducer transactionProducer;

    public TransactionController(TransactionProducer transactionProducer) {
        this.transactionProducer = transactionProducer;
    }

    @PostMapping("/transactions")
    public String submitTransaction(@Valid @RequestBody TransactionRequest request) {
        String id = UUID.randomUUID().toString();
        TransactionEvent event = new TransactionEvent(
                id,
                request.getCustomerId(),
                request.getAmount(),
                request.getMerchant(),
                request.getLocation()
        );
        transactionProducer.sendTransaction(event);
        return "Transaction accepted: " + id;
    }
}
