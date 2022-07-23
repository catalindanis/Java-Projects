package machine;
import java.util.Scanner;

public class CoffeeMachine {

    static int water = 400;
    static int milk = 540;
    static int coffe_beans = 120;
    static int cups = 9;
    static int money = 550;

    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        boolean exit = false;
        while(!exit) {

            System.out.println("Write action (buy, fill, take, remaining, exit): ");
            String action = scanner.next();

            switch (action) {
                case "buy":
                    buy();
                    break;
                case "take":
                    take();
                    break;
                case "fill":
                    fill();
                    break;
                case "remaining":
                    remaining();
                    break;
                case "exit":
                    exit = true;
                    break;
            }
        }
    }

    private static void buy() {
        System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ");
        int n = 0;
        try {
            n = scanner.nextInt();
        } catch (Exception e) {
            scanner.next();
        }
        if (n == 1) {
            if (water >= 250 && coffe_beans >= 16) {
                water -= 250;
                coffe_beans -= 16;
                money += 4;
                cups--;
                System.out.println("I have enough resources, making you a coffee!");
            } else if (water < 250) {
                System.out.println("Sorry, not enough water!");
            } else {
                System.out.println("Sorry, not enough coffe beans!");
            }
        } else if (n == 2) {
            if (water >= 350 && coffe_beans >= 20 && milk >= 75) {
                water -= 350;
                coffe_beans -= 20;
                milk -= 75;
                money += 7;
                cups--;
                System.out.println("I have enough resources, making you a coffee!");
            } else if (water < 350) {
                System.out.println("Sorry, not enough water!");
            } else if (coffe_beans < 20) {
                System.out.println("Sorry, not enough coffe beans!");
            } else {
                System.out.println("Sorry, not enough milk!");
            }
        } else if (n == 3) {
            if (water >= 200 && coffe_beans >= 12 && milk >= 100) {
                water -= 200;
                coffe_beans -= 12;
                milk -= 100;
                money += 6;
                cups--;
                System.out.println("I have enough resources, making you a coffee!");
            } else if (water < 200) {
                System.out.println("Sorry, not enough water!");
            } else if (coffe_beans < 12) {
                System.out.println("Sorry, not enough coffe beans!");
            } else {
                System.out.println("Sorry, not enough milk!");
            }
        }
    }

    private static void fill () {
        System.out.println("Write how many ml of water you want to add: ");
        int w = scanner.nextInt();
        System.out.println("Write how many ml of milk you want to add: ");
        int m = scanner.nextInt();
        System.out.println("Write how many grams of coffee beans you want to add: ");
        int c = scanner.nextInt();
        System.out.println("Write how many disposable cups of coffee you want to add: ");
        int cu = scanner.nextInt();

        water+=w;
        milk+=m;
        coffe_beans+=c;
        cups+=cu;
    }

    private static void take () {
        System.out.println("I gave you "+money);
        money = 0;
    }

    private static void remaining () {
        System.out.println("The coffee machine has:\n" +
                water+" ml of water\n" +
                milk+" ml of milk\n" +
                coffe_beans+" g of coffee beans\n" +
                cups+" disposable cups\n" +
                "$"+money+" of money");
    }

}

