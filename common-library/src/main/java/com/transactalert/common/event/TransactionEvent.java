package com.transactalert.common.event;

public class TransactionEvent implements Serializable{
    private final String transactionId;
    private final String customerId;
    private final double amount;
    private final String merchant;
    private final String location;

    public TransactionEvent(
        String transactionId,
        String customerId,
        double amount,
        String merchant,
        String location
    ){
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.amount = amount;
        this.merchant = merchant;
        this.location = location; 
    }

    public TransactionEvent() {};

    public String getTransactionId(){
        return transactionId;
    }
    
    public String getCustomerId(){ 
        return customerId;
    }
    
    public double getAmount(){ 
        return amount;
    }
    
    public String getMerchant(){ 
        return merchant;
    }
    
    public String getLocation(){ 
        return location;
    }
}
