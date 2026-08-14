public class TransactionEvent implements Serializable{
    private String transactionId;
    private String customerId;
    private double amount;
    private String merchant;
    private String location;

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
