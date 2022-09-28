import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static Client myClient = new Client();
    public static void main(String[] args) {
        while(true){
            displayMenu();
            int input = Integer.valueOf(scanner.nextLine());
            System.out.println();
            switch (input){
                case 0:
                    System.out.println("Bye!");
                    return;
                case 1:
                    addIncome();
                    break;
                case 2:
                    addPurchase();
                    break;
                case 3:
                    displayProducts();
                    break;
                case 4:
                    displayBalance();
                    break;
                case 5:
                    save();
                    break;
                case 6:
                    load();
                    break;
                case 7:
                    analyze();
                    break;
            }
            System.out.println();
        }
    }

    private static void displayBalance() {
        System.out.printf("Balance: $%.2f\n",myClient.getBalance());
    }

    private static void addPurchase() {
        while(true) {
            displayCategories();
            String category = scanner.nextLine();
            switch (category){
                case "5":
                    return;
                case "1":
                    category = "Food";
                    break;
                case "2":
                    category = "Clothes";
                    break;
                case "3":
                    category = "Entertainment";
                    break;
                case "4":
                    category = "Other";
                    break;
            }
            System.out.println();
            System.out.println("Enter purchase name:");
            String name = scanner.nextLine();
            System.out.println("Enter its price:");
            double price = Double.valueOf(scanner.nextLine());
            myClient.addPurchase(name, price, category);
            if(myClient.getBalance() <= price)
                myClient.setBalance(0.0);
            else myClient.setBalance(myClient.getBalance()-price);
            System.out.println("Purchase was added!");
            System.out.println();
        }
    }
    private static void analyze(){
        while(true) {
            displaySortMenu();
            switch (scanner.nextLine()) {
                case "1":
                    System.out.println();
                    if (myClient.getPurchases().isEmpty())
                        System.out.println("The purchase list is empty!");
                    else{
                        System.out.println("All:");
                        myClient.analyze((product1,product2) -> {
                            if(product1.getPrice() < product2.getPrice())
                                return 1;
                            else if(product1.getPrice() > product2.getPrice())
                                return -1;
                            return 0;
                        });
                        ArrayList<Product> sortedProducts = myClient.getPurchases();
                        for(Product product : sortedProducts){
                            System.out.printf("%s $%.2f\n", product.getName(), product.getPrice());
                        }
                        System.out.printf("%s: $%.2f\n","Total",getTotal(sortedProducts));
                    }
                    break;
                case "2":
                    System.out.println();
                    System.out.println("Types:");
                    ArrayList<Product> allProducts = myClient.getPurchases();
                    String[] types = new String[]{"Food","Entertainment","Clothes","Other"};
                    for(String type : types){
                        System.out.printf("%s - $%.2f\n",type,getTotal(allProducts,type));
                    }
                    System.out.printf("%s: $%.2f\n","Total sum",getTotal(allProducts));
                    break;
                case "3":
                    System.out.println();
                    displayCategories(1);
                    String category = scanner.nextLine();
                    switch (category) {
                        case "1":
                            category = "Food";
                            break;
                        case "2":
                            category = "Clothes";
                            break;
                        case "3":
                            category = "Entertainment";
                            break;
                        case "4":
                            category = "Other";
                            break;
                    }
                    String finalCategory = category;
                    myClient.analyze((product1,product2) -> {
                        if(product1.getCategory().equals(finalCategory) &&
                                product2.getCategory().equals(finalCategory)){
                            if(product1.getPrice() < product2.getPrice())
                                return 1;
                            else if(product1.getPrice() > product2.getPrice())
                                return -1;
                            return product1.getName().compareToIgnoreCase(product2.getName());
                        }
                        return 0;
                    });
                    System.out.println();
                    ArrayList<Product> sortedProducts = myClient.getPurchases(finalCategory);
                    if(sortedProducts.isEmpty())
                        System.out.println("The purchase list is empty!");
                    else{
                        System.out.println(finalCategory+":");
                        for(Product product : sortedProducts)
                            System.out.printf("%s $%.2f\n",product.getName(),product.getPrice());
                        System.out.printf("%s: %.2f\n","Total sum",getTotal(sortedProducts));
                    }
                    break;
                case "4":
                    return;
            }
            System.out.println();
        }
    }
    private static void displaySortMenu(){
        System.out.println("How do you want to sort?");
        System.out.println("1) Sort all purchases");
        System.out.println("2) Sort by type");
        System.out.println("3) Sort certain type");
        System.out.println("4) Back");
    }

    private static void displayCategories(){
        System.out.println("Choose the type of purchase");
        System.out.println("1) Food");
        System.out.println("2) Clothes");
        System.out.println("3) Entertainment");
        System.out.println("4) Other");
        System.out.println("5) Back");
    }
    private static void displayCategories(boolean display){
        System.out.println("Choose the type of purchase");
        System.out.println("1) Food");
        System.out.println("2) Clothes");
        System.out.println("3) Entertainment");
        System.out.println("4) Other");
        System.out.println("5) All");
        System.out.println("6) Back");
    }
    private static void displayCategories(int sort){
        System.out.println("Choose the type of purchase");
        System.out.println("1) Food");
        System.out.println("2) Clothes");
        System.out.println("3) Entertainment");
        System.out.println("4) Other");
    }
    private static void addIncome() {
        System.out.println("Enter income:");
        myClient.setBalance(Double.valueOf(scanner.nextLine()));
        System.out.println("Income was added!");
    }
    public static void displayMenu(){
        System.out.println("Choose your action:");
        System.out.println("1) Add income");
        System.out.println("2) Add purchase");
        System.out.println("3) Show list of purchases");
        System.out.println("4) Balance");
        System.out.println("5) Save");
        System.out.println("6) Load");
        System.out.println("7) Analyze (Sort)");
        System.out.println("0) Exit");
    }
    public static void save(){
        try {
            PrintWriter printWriter = new PrintWriter(new File("purchases.txt"));
            printWriter.write(myClient.getBalance()+"\n");
            ArrayList<Product> purchases = myClient.getPurchases();
            for(Product purchase : purchases){
                printWriter.write(purchase.getName()+"?"+purchase.getPrice()+"?"+purchase.getCategory()+"\n");
            }
            printWriter.close();
        }catch (FileNotFoundException fnfe){}
        System.out.println("Purchases were saved!");
    }
    public static void load(){
        try {
            Scanner fileScanner = new Scanner(new File("purchases.txt"));
            myClient.setBalance(Double.valueOf(fileScanner.nextLine()));
            while(fileScanner.hasNextLine()){
                String line = fileScanner.nextLine();
                myClient.addPurchase(line.split("\\?")[0],
                        Double.valueOf(line.split("\\?")[1]),line.split("\\?")[2]);
            }
        } catch (FileNotFoundException e) {}
        System.out.println("Purchases were loaded!");
    }
    public static void displayProducts(){
        if(myClient.getPurchases().isEmpty()){
            System.out.println("The purchase list is empty!");
            return;
        }
        while(true) {
            displayCategories(true);
            String category = scanner.nextLine();
            switch (category) {
                case "6":
                    return;
                case "1":
                    category = "Food";
                    break;
                case "2":
                    category = "Clothes";
                    break;
                case "3":
                    category = "Entertainment";
                    break;
                case "4":
                    category = "Other";
                    break;
                case "5":
                    category = "All";
                    break;
            }
            System.out.println();
            System.out.println(category + ":");
            ArrayList<Product> products = myClient.getPurchases(category);
            if (products.isEmpty())
                System.out.println("The purchase list is empty");
            else {
                double total = 0.0;
                for (int i = 0; i < products.size(); i++)
                    System.out.printf("%s $%.2f\n", products.get(i).getName(), products.get(i).getPrice());
                System.out.printf("Total sum: $%.2f\n", getTotal(products));
            }
            System.out.println();
        }
    }
    public static double getTotal(ArrayList<Product> products){
        double total = 0.0;
        for(int i=0;i<products.size();i++)
            total += Double.valueOf(products.get(i).getPrice());
        return total;
    }
    public static double getTotal(ArrayList<Product> products,String category){
        double total = 0.0;
        for(int i=0;i<products.size();i++)
            if(products.get(i).getCategory().equals(category))
                total += Double.valueOf(products.get(i).getPrice());
        return total;
    }
}

class Product{
    private String name = new String();
    private double price = 0.0;
    private String category = new String();

    public Product(String name,double price,String category){
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
class Client{
    private double balance = 0.0;
    private ArrayList<Product> purchases = new ArrayList<>();

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    public ArrayList<Product> getPurchases() {
        return purchases;
    }
    public ArrayList<Product> getPurchases(String category) {
        if(category.equals("All"))
            return purchases;
        ArrayList<Product> desiredPurchases = new ArrayList<>();
        for(Product product : purchases){
            if(product.getCategory().equals(category))
                desiredPurchases.add(product);
        }
        return desiredPurchases;
    }

    public Product getPurchaseByIndex(int index){
        return purchases.get(index);
    }

    public void addPurchase(String name,double price,String category){
        purchases.add(new Product(name,price,category));
    }

    public void setPurchases(ArrayList<Product> purchases) {
        this.purchases = purchases;
    }

    public void analyze(Comparator<Product> comparator){
        purchases.sort(comparator);
    }
}