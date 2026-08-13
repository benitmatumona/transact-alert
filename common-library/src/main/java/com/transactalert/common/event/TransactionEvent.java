public class TransactionEvent implements Serializable{
    private String transactionId;
    private String customerId;
    private double amount;
    private String merchant;
    private String location;

    public TransactionEvent(
        String transactionId,
        String customerId.
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

    public getTransactionId(){
        return transactionId;
    }
    
    public getCustomerId(){ 
        return customerId;
    }
    
    public getAmount(){ 
        return amount;
    }
    
    public getMerchant(){ 
        return merchant;
    }
    
    public getLocation(){ 
        return location;
    }
}
