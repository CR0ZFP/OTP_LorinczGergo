import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Payment {
    private String webshopID;
    private String customerID;
    private String paymentMethod;
    private double amount;
    private String toBeDecided; //A csv file-ban csak az egyik fog szerepelni a bankAccount és a Card number közül
    private String bankAccount;
    private String cardNumber;
    private LocalDate paymentDate;

    public String getWebshopID() {
        return webshopID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public double getAmount() {
        return amount;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public Payment(String webshopID, String customerID, String paymentMethod, double amount, String toBeDecided, String paymentDate) throws InvalidPaymentMethodException, InvalidPaymentDateFormatException{
        this.webshopID = webshopID;
        this.customerID = customerID;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy'T'HH:mm"); //csak ezeket a dátumokat fogadjuk el
        try {
            this.paymentDate = LocalDate.parse(paymentDate, formatter);
        }catch (Exception e)
        {
            throw new InvalidPaymentDateFormatException ("Payment date format is incorrect");
        }

        if (paymentMethod.equals("card")) {  //ha a method card akkor a kártyás fizetésről van szó
            this.cardNumber = toBeDecided;
        }
        else if (paymentMethod.equals("transfer")) { // ha a method transfer a akkor banki átutalás lesz
            this.bankAccount = toBeDecided;
        }
        else {
            throw new InvalidPaymentMethodException("Invalid payment method: "+paymentMethod);
        }

    }
    public String getID (){
        return webshopID+"#"+customerID;
    }
}


