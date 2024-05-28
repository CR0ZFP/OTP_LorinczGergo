public class Customer {
    private String webshopID;
    private String clientID;
    private String clientName;
    private String clientAddress;

    public Customer(String webshopID, String clientID, String clientName, String clientAddress) {
        this.webshopID = webshopID;
        this.clientID = clientID;
        this.clientName = clientName;
        this.clientAddress = clientAddress;
    }

    public String getWebshopID() {
        return webshopID;
    }

    public String getClientID() {
        return clientID;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public String getID(){
        return webshopID+"#"+clientID;
    }

    @Override
    public String toString() {
        return webshopID+","+clientID+","+clientName+","+clientAddress;
    }
}
