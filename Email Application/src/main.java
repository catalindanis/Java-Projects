import java.util.Scanner;
public class main {

    public static void main(String[] args) {
        runApp();
    }

    public static void runApp(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Create your account");
        String firstName,lastName,department,company;
        System.out.print("First name: ");
        firstName = scanner.nextLine();
        System.out.print("Last name: ");
        lastName = scanner.nextLine();
        System.out.print("Department: ");
        department = scanner.nextLine();
        System.out.print("Company: ");
        company = scanner.nextLine();

        NewUser user = new NewUser(firstName,lastName,department,company);

        System.out.println();
        System.out.println("User Generated! ");

        user.runUserInterface();
    }
}
