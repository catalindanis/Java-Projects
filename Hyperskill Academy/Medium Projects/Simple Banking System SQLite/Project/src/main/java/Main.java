import org.sqlite.SQLiteDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;
public class Main {
    static ArrayList<Card> card = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();
    static boolean exitRequested = false;
    private static SQLiteDataSource sqLiteDataSource = new SQLiteDataSource();

    public static void main(String[] args) {
        initDataBase(args[1]);
        menu();
        saveValuesToDB();
        System.out.println("Bye!");
    }

    public static void menu(){
        exitRequested = false;
        while(!exitRequested){
            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");
            int command = scanner.nextInt();
            scanner.nextLine();
            switch (command){
                case 1:
                    createAnAccount();
                    break;
                case 2:
                    logInToAccount();
                    break;
                case 0:
                    exitRequested = true;
                    break;
            }
        }
    }
    private static void createAnAccount(){
        String cardNumber;
        do {
            cardNumber = "400000";
            for (int i = 0; i < 10; i++) {
                cardNumber += random.nextInt(10);
            }
        }while(!luhnAlgorithm(cardNumber));
        String pinNumber = String.valueOf(random.nextInt(9)+1);
        for(int i=0;i<3;i++)
            pinNumber += random.nextInt(10);
        card.add(new Card(cardNumber,pinNumber,card.size(),0));
        System.out.printf("Your card number:\n%s\n",cardNumber);
        System.out.printf("Your card PIN:\n%s\n",pinNumber);
    }

    private static void logInToAccount(){
        System.out.println("Enter your card number: ");
        String cardNumber = scanner.nextLine();
        int id = -1;
        boolean cardNumberExist = false;
        for(int i=0;i<card.size() && !cardNumberExist;i++){
            if(card.get(i).cardNumber.equals(cardNumber)) {
                cardNumberExist = true;
                id = i;
            }
        }
        if(cardNumberExist) {
            System.out.println("Enter your PIN:");
            String pinNumber = scanner.nextLine();
            if(card.get(id).pinNumber.equals(pinNumber)) {
                System.out.println("You have successfully logged in!");
                accountMenu(id);
            }
            else System.out.println("Wrong card number or PIN!");
        }
        else System.out.println("Wrong card number or PIN!");
    }

    private static void accountMenu(int id){
        while(!exitRequested){
            //saveValuesToDB();
            System.out.println("1. Balance");
            System.out.println("2. Add income");
            System.out.println("3. Do transfer");
            System.out.println("4. Close account");
            System.out.println("5. Log out");
            System.out.println("0. Exit");
            int command = scanner.nextInt();
            scanner.nextLine();
            switch (command){
                case 1:
                    showBalance(id);
                    break;
                case 2:
                    addIncome(id);
                    break;
                case 3:
                    doTransfer(id);
                    break;
                case 4:
                    closeAccount(id);
                    break;
                case 5:
                    System.out.println("You have successfully logged out!");
                    return;
                case 0:
                    exitRequested = true;
                    return;
            }
        }
    }
    private static void showBalance(int id){
        System.out.println("Balance: "+card.get(id).balance);
    }
    private static void addIncome(int id){
        System.out.println("Enter income:");
        int income = scanner.nextInt();
        scanner.nextLine();
        card.get(id).balance += income;
        System.out.println("Income was added!");
        saveValuesToDB();
    }
    private static void doTransfer(int id){
        System.out.println("Transfer");
        System.out.println("Enter card number:");
        String cardNumber = scanner.nextLine();
        boolean cardExists = false;
        if(!card.get(id).cardNumber.equals(cardNumber)) {
            if(luhnAlgorithm(cardNumber)) {
                int transferId = -1;
                for (int i = 0; i < card.size(); i++) {
                    if (card.get(i).cardNumber.equals(cardNumber)) {
                        cardExists = true;
                        transferId = i;
                        break;
                    }
                }
                if (cardExists) {
                    System.out.println("Enter how much money you want to transfer:");
                    int amount = scanner.nextInt();
                    scanner.nextLine();
                    if(amount <= card.get(id).balance){
                        card.get(id).balance -= amount;
                        card.get(transferId).balance += amount;
                        System.out.println("Success!");
                    }
                    else System.out.println("Not enough money!");
                }
                else System.out.println("Such a card does not exist.");
            }
            else System.out.println("Probably you made a mistake in the card number. Please try again!");
        }
        else
            System.out.println("You can't transfer money to the same account!");
    }
    private static void closeAccount(int id){
        card.remove(id);
    }
    private static boolean luhnAlgorithm(String cardNumber){
        int newCard[] = new int[16];
        for(int i=0;i<16;i++)
            if(i%2 == 0)
                newCard[i] = ((cardNumber.charAt(i)-'0') * 2);
            else newCard[i] = (cardNumber.charAt(i)-'0');

        for(int i=0;i<15;i++)
            if(newCard[i] > 9)
                newCard[i]-=9;

        int sum = 0;
        for(int i=0;i<16;i++)
            sum+=newCard[i];

        return (sum%10) == 0;
    }

    private static void initDataBase(String url){
        url = "jdbc:sqlite:"+url;
        sqLiteDataSource.setUrl(url);
        try(Connection connection = sqLiteDataSource.getConnection()) {
            getValuesFromDB();
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
    }
    private static void getValuesFromDB(){
        try (Connection connection = sqLiteDataSource.getConnection()){
            if(connection.isValid(5)){
                try(Statement statement = connection.createStatement()){
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM card");
                    while(resultSet.next()){
                        card.add(new Card(
                                resultSet.getString("number"),
                                resultSet.getString("pin"),
                                resultSet.getInt("id"),
                                resultSet.getInt("balance")
                        ));
                    }
                }catch (Exception exception1){
                    Statement statement = connection.createStatement();
                    statement.execute("CREATE TABLE card" +
                            "(id INTEGER,number VARCHAR(16),pin VARCHAR(4),balance INTEGER DEFAULT 0)");
                }
            }
        }catch (Exception exception){exception.printStackTrace();}
    }

    private static void saveValuesToDB(){
        try (Connection connection = sqLiteDataSource.getConnection()){
            if(connection.isValid(5)){
                try(Statement statement = connection.createStatement()){
                    statement.execute("DELETE FROM card;");
                    for(int i=0;i<card.size();i++) {
                        statement.execute("" +
                                "INSERT INTO card VALUES " +
                                "("+card.get(i).id+","+card.get(i).cardNumber+","+card.get(i).pinNumber+","+card.get(i).balance+");");
                    }
                }catch (Exception exception1){exception1.printStackTrace();}
            }
        }catch (Exception exception){exception.printStackTrace();}
    }
}

class Card{
    public String cardNumber = new String();
    public String pinNumber = new String();
    public int id;
    public int balance;

    Card(String cardNumber,String pinNumber,int id,int balance){
        this.cardNumber = cardNumber;
        this.pinNumber = pinNumber;
        this.id = id;
        this.balance = balance;
    }
}