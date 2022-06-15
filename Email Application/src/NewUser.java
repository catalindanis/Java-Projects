import java.util.ArrayList;
import java.util.Scanner;
public class NewUser implements Email,Password{
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String department;
    private String company;
    private int mailboxCapacity = 0;

    private Scanner scanner = new Scanner(System.in);

    NewUser(String firstName,String lastName,String department,String company){
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.company = company;
        generateEmail();
        generatePassword();
    }

    private void generateEmail(){
        this.email =  firstName.toLowerCase()+'.'+lastName.toLowerCase()+'@'
                +department.toLowerCase()+'.'+company.toLowerCase()+".com";
    }

    public void changeEmail(){
        System.out.print("Enter your new email: ");
        this.email = scanner.next();
        System.out.println("Operation successful!");
    }

    public void displayEmail(){
        System.out.println("email: "+email);
    }

    private void generatePassword(){
        StringBuilder password = new StringBuilder();
        for(int i=1;i<=3;i++){
            int index = (int)(lowerCase.length()*Math.random());
            password.append(lowerCase.charAt(index));
            index = (int)(digits.length()*Math.random());
            password.append(digits.charAt(index));
            index = (int)(upperCase.length()*Math.random());
            password.append(upperCase.charAt(index));
        }
        this.password = password.toString();
    }

    public void changePassword(){
        System.out.print("Enter your new password: ");
        this.password = scanner.next();
        System.out.println("Operation successful!");
    }

    public void displayPassowrd(){
        System.out.println("password: "+password);
    }

    public void setMailboxCapacity(){
        System.out.print("Enter your mailbox capacity: ");
        this.mailboxCapacity = scanner.nextInt();
        System.out.println("Operation successful!");
    }

    public void displayMailboxCapacity(){
        System.out.println("Mailbox capacity: "+mailboxCapacity);
    }

    public void displayDepartment(){
        System.out.println("Department: "+department);
    }

    public void changeName(){
        System.out.print("Enter your new name(First name + Last Name): ");
        scanner.nextLine();
        String name = scanner.nextLine();
        this.firstName = name.split(" ")[0];
        this.lastName = name.split(" ")[1];
        System.out.println("Operation successful!");

        System.out.print("Do you also want to generate new email for the name? yes/no(1/2): ");
        int command = scanner.nextInt();
        switch (command){
            case 1:
                generateEmail();
                break;
            case 2:
                break;
        }
    }
    public void displayName(){
        System.out.println(firstName+' '+lastName);
    }

    public void displayInfo(){
        displayName();
        displayDepartment();
        displayEmail();
        displayPassowrd();
        displayMailboxCapacity();
    }

    public void runUserInterface(){
        boolean pressedExit = false;
        while(!pressedExit) {
            System.out.print("Choose your command: Display info(0), Change name(1), Change email(2), Change password(3), Change mailbox capacity(4), Remove account(5), Exit(6): ");
            int command = scanner.nextInt();
            switch (command) {
                case 0:
                    displayInfo();
                    break;
                case 1:
                    changeName();
                    break;
                case 2:
                    changeEmail();
                    break;
                case 3:
                    changePassword();
                    break;
                case 4:
                    setMailboxCapacity();
                    break;
                case 5:
                    main.runApp();
                    break;
                case 6:
                    pressedExit = true;
                    break;
            }
        }
    }

}
