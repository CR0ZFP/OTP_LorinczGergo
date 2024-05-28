
public class Main {
    public static void main(String[] args) {
        WebshopManager application = new WebshopManager();
        application.readCustomers("customers.csv");
        application.readPayments("payments.csv");

        application.generateReports();
    }
}