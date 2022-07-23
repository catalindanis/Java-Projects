import org.w3c.dom.ls.LSOutput;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;
public class Main {

    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();
    private static String cardNumber = new String();
    private static String pinNumber = new String();

    public static void main(String[] args) {
        menu();
    }

    public static void menu(){
        boolean exitRequested = false;
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
        do {
            cardNumber = "400000";
            for (int i = 0; i < 10; i++) {
                cardNumber += random.nextInt(0, 10);
            }
        }while(!luhnAlgorithm());
        pinNumber = "";
        for(int i=0;i<4;i++)
            pinNumber += random.nextInt(0,10);
        System.out.printf("Your card number:\n%s\n",cardNumber);
        System.out.printf("Your card PIN:\n%s\n",pinNumber);
    }

    private static void logInToAccount(){
        System.out.println("Enter your card number: ");
        String card = scanner.nextLine();
        if(card.equals(cardNumber)){
            System.out.println("Enter your PIN:");
            String pin = scanner.nextLine();
            if(pin.equals(pinNumber)){
                System.out.println("You have successfully logged in!");
                accountMenu();
            }
            else System.out.println("Wrong card number or PIN!");
        }
        else System.out.println("Wrong card number or PIN!");
        return;
    }

    private static void accountMenu(){
        boolean exitRequested = false;
        while(!exitRequested){
            System.out.println("1. Balance");
            System.out.println("2. Log out");
            System.out.println("0. Exit");
            int command = scanner.nextInt();
            scanner.nextLine();
            switch (command){
                case 1:
                    System.out.println("Balance: 0");
                    break;
                case 2:
                    System.out.println("You have successfully logged out!");
                    exitRequested = true;
                    break;
                case 0:
                    exitRequested = true;
                    break;
            }
        }
    }
    private static boolean luhnAlgorithm(){
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

}
