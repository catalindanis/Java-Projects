import java.util.Scanner;

public class Cinema {

    static Scanner scanner = new Scanner(System.in);
    static String seats[][] = new String[101][101];
    static int rows,seats_row,income,numberOfSeats,currentIncome=0;
    static boolean exitRequest=false;
    static int buyedTickets = 0;

    public static void main(String[] args) {
        numberOfSeats = readNumberOfSeats();
        setValues();
        while(!exitRequest){
            menu();
        }
    }

    public static void menu(){
        System.out.println("1. Show the seats");
        System.out.println("2. Buy a ticket");
        System.out.println("3. Statistics");
        System.out.println("0. Exit");
        int command = scanner.nextInt();
        switch(command){
            case 1:
                displaySeats();
                break;
            case 2:
                buyTicket();
                break;
            case 3:
                statistics();
                break;
            case 0:
                exitRequest = true;
                break;
        }
    }

    public static void statistics(){
        System.out.println();
        income = income(numberOfSeats);
        double percentage = 100.0/numberOfSeats;
        System.out.printf("Number of purchased tickets: %d",buyedTickets);
        System.out.println();
        System.out.printf("Percentage: %.2f%%",buyedTickets*percentage);
        System.out.println();
        System.out.printf("Current income: $%d",currentIncome);
        System.out.println();
        System.out.printf("Total income: $%d",income);
        System.out.println();
        System.out.println();
    }

    public static void buyTicket(){
        System.out.println();
        boolean buyed = false;
        while(!buyed) {
            System.out.println("Enter a row number:");
            int row = scanner.nextInt();
            System.out.println("Enter a seat number in that row:");
            int seat_number = scanner.nextInt();
            System.out.println();
            if(row > 0 && row <= rows && seat_number > 0 && seat_number <= seats_row) {
                if (seats[row][seat_number].equals("S")) {
                    if (numberOfSeats > 60) {
                        if (row <= rows / 2) {
                            System.out.println("Ticket price: $10");
                            currentIncome+=10;
                        }
                        else {
                            System.out.println("Ticket price: $8");
                            currentIncome+=8;
                        }
                    } else {
                        System.out.println("Ticket price: $10");
                        currentIncome+=10;
                    }
                    seats[row][seat_number] = "B";
                    buyed = true;
                }
                else System.out.println("That ticket has already been purchased!");
            }
            else System.out.println("Wrong input!");

            System.out.println();
        }
        buyedTickets++;
    }

    public static void displayIncome(){
        System.out.println("Total income:");
        System.out.println("$"+income);
        System.out.println();
    }

    public static int readNumberOfSeats(){
        System.out.println("Enter the number of rows:");
        rows = scanner.nextInt();
        System.out.println("Enter the number of seats in each row:");
        seats_row = scanner.nextInt();
        System.out.println();
        return rows*seats_row;
    }

    public static int income(int numberOfSeats){
        if(numberOfSeats <= 60){
            return 10*numberOfSeats;
        }
        else{
            int mid = rows/2;
            if(rows %2 == 1) {
                return mid * seats_row * 10 + (mid + 1) * seats_row * 8;
            }
            else return mid * seats_row * 10 + mid * seats_row * 8;
        }
    }

    public static void displaySeats(){
        System.out.println("Cinema:");
        for(int i=0;i<=rows;i++) {
            for (int j = 0; j <= seats_row; j++) {
                if (i == 0 && j == 0)
                    System.out.print("  ");
                else System.out.print(" " + seats[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void setValues(){
        for(int i=0;i<=rows;i++)
            for(int j=0;j<=seats_row;j++)
                seats[i][j] = "S";
        for(Integer i=1;i<=seats_row;i++)
            seats[0][i] = i.toString();
        for(Integer i=1;i<=rows;i++)
            seats[i][0] = i.toString();
    }
}