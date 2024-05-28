import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class WebshopManager {
         Map<String,Customer> customers = new HashMap<>();
         ArrayList<Payment> payments = new ArrayList<Payment>() {
    };

    public void writeLog (String fileName, String message){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true));
            writer.write(message+"\n");
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void readCustomers(String fileName) {
        try{
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            while (line != null){
                String[] data = line.split(";");
                String Key = data[0]+"#"+data[1]; //Ezzel biztosítjuk hogy teljesen egyedi customer id webshoponként egyedi legyen

                if (customers.containsKey(Key)){ //Ha már van egy ilyen kulcsunk akkor exception helyett csak simán nem végezzük el a HashMap-be putolást valamint logoljuk a hibát
                    writeLog("application.log","Customer with key "+Key+" already exists!");
                }
                else {
                    Customer customer = new Customer (data[0],data[1],data[2],data[3]); //Elvileg csak Stringet fogunk beolvasni szóval nem okozhat exceptiont
                    customers.put(Key,customer);
                }
                line = br.readLine();
            }
            br.close();
        }catch (IOException e){
            writeLog("application.log",e.getMessage());
        }
    }
    public void readPayments(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            while (line != null){
                String[] data = line.split(";");
                try{
                    payments.add(new Payment(
                            data[0], //webshopID
                            data[1], //customerID
                            data[2], //paymentMethod
                            Double.parseDouble(data[3]), //ammount
                            data[4], //paymentDate
                            data[5] //cardNumber or bankAccount
                    ));
                }catch (Exception e){
                    writeLog("application.log",e.getMessage());
                }
                line = br.readLine();

            }
            br.close();
        }catch (IOException e){
            writeLog("application.log",e.getMessage());
        }


    }
    public void  generateReports(){
        Map<String,Integer> Spenders = new HashMap<>();
        //Első report: Összes Customer teljes költése
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter("report01.csv"));

            for (Customer customer : customers.values()){
                int totalPayment =0;
                List<Payment> customerPayments= payments.stream().filter(x->x.getID().equals(customer.getID())).collect(Collectors.toList()); //Vásárlások szűrése azonosító szerint

                for (Payment payment : customerPayments){
                    totalPayment += payment.getAmount(); //Teljes költés meghatározása
                }

                Spenders.put(customer.getID(),totalPayment);
                bw.write(customer.toString()+","+totalPayment+"\n");
            }
            bw.close();
        }catch (IOException e)
        {
            writeLog("application.log",e.getMessage());
        }

        //Második report: Top 2 legtöbbet költő kiírása
        List<Map.Entry<String,Integer>> SpendersEntries = new ArrayList<>(Spenders.entrySet());
        SpendersEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder())); // az első reportban a hashmapünket konvertáljuk egy listává amit tudunk rendezni a totalPayment szerint
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter("top.csv"));
            for(int i=0;i<2;i++){
                Customer spender = customers.get(SpendersEntries.get(i).getKey()); //Customer adatai miatt lekérjük a Customert
                int totalPayment = SpendersEntries.get(i).getValue(); //totalPayment
                bw.write(spender.toString()+","+totalPayment+"\n");
            }
            bw.close();
        }catch (IOException e)
        {
            writeLog("application.log",e.getMessage());
        }

        //Harmadik report: Webshopok összesített adatai
        Set <String> uniqueWebshopIDs = payments.stream().map(Payment::getWebshopID).collect(Collectors.toSet()); //Egyedi webshopID-k kinyerése
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("report02.csv"));
                for(String webshopID : uniqueWebshopIDs) {
                    double webshopCard = payments.stream().filter(x -> x.getWebshopID().equals(webshopID) && x.getPaymentMethod().equals("card")).collect(Collectors.summingDouble(Payment::getAmount)); // kártyás vásárlások összege
                    double webshopBank = payments.stream().filter(x -> x.getWebshopID().equals(webshopID) && x.getPaymentMethod().equals("transfer")).collect(Collectors.summingDouble(Payment::getAmount)); // Banki átutalásos vásárlások összege
                    bw.write(webshopID + "," + webshopCard + "," + webshopBank + "\n"); //ID,Kártyás vásárlás, Banki átutalás
                }
                bw.close();
            }catch (IOException e){
                writeLog("application.log",e.getMessage());
            }

        }

    }

