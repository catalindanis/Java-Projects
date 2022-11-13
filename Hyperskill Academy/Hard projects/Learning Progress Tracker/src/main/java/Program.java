import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

class Program{
    List<Student> students = new LinkedList<>();
    Scanner scanner = new Scanner(System.in);
    public void run(){
        System.out.println("Learning Progress Tracker");
        while(true){
            String input = scanner.nextLine();
            switch (input){
                case "exit":
                    System.out.println("Bye!");
                    return;
                case "add students":
                    addStudents();
                    break;
                case "add points":
                    addPoints();
                    break;
                case "list":
                    listStudents();
                    break;
                case "find":
                    findStudent();
                    break;
                default :
                    if(input.isBlank())
                        System.out.println("no input");
                    else if(input.equals("back"))
                        System.out.println("Enter 'exit' to exit the program");
                    else System.out.println("Error: unknown command!");
            }
        }
    }

    private void findStudent() {
        System.out.println("Enter an id or 'back' to return:");
        while(true){
            String input = scanner.nextLine();
            if(input.equals("back"))
                return;

            try{
                boolean studentExists = false;
                for(Student student : students)
                    if(student.getId().equals(input)) {
                        studentExists = true;
                        System.out.println(String.format("%s points: Java=%d; DSA=%d; Databases=%d; Spring=%d",
                                input,student.getJavaPoints(),student.getDsaPoints(),student.getDbPoints(),student.getSpringPoints()));
                        break;
                    }

                if(!studentExists){
                    System.out.println(String.format("No student is found for id=%s",input));
                }
            }
            catch (Exception exception){}
        }
    }

    private void listStudents() {
        if(students.size() == 0) {
            System.out.println("No students found");
            return;
        }
        System.out.println("Students:");
        for(Student student : students)
            System.out.println(student.getId());
    }
    private void addPoints() {
        System.out.println("Enter an id and points or 'back' to return");
        while(true){
            String input = scanner.nextLine();
            if(input.equals("back"))
                return;

            try{
                if(!(input.split(" ").length == 5)){
                    System.out.println("Incorrect points format");
                    continue;
                }

                String id = input.split(" ")[0];
                int javaPoints = Integer.valueOf(input.split(" ")[1]);
                int dsaPoints = Integer.valueOf(input.split(" ")[2]);
                int dbPoints = Integer.valueOf(input.split(" ")[3]);
                int springPoints = Integer.valueOf(input.split(" ")[4]);

                if(javaPoints < 0 || dsaPoints < 0 || dbPoints < 0 || springPoints < 0) {
                    System.out.println("Incorrect points format");
                    continue;
                }

                boolean studentExists = false;
                for(Student student : students)
                    if(student.getId().equals(id)) {
                        student.updatePoints(javaPoints,dsaPoints,dbPoints,springPoints);
                        studentExists = true;
                        System.out.println("Points updated");
                        break;
                    }

                if(!studentExists){
                    System.out.println(String.format("No student is found for id=%s",id));
                }

            }catch (Exception exception){
                exception.printStackTrace();
                System.out.println("Incorrect points format");
            }
        }
    }

    private void addStudents(){
        System.out.println("Enter student credentials or 'back' to return");
        int counter = 0;
        while(true){
            String input = scanner.nextLine();
            if(input.equals("back")){
                System.out.println(String.format("Total %d students have been added.",counter));
                break;
            }
            try {
                String firstName = input.split(" ")[0];
                String lastName = input.substring(input.indexOf(' ') + 1, input.lastIndexOf(' '));
                String email = input.split(" ")[input.split(" ").length - 1];
                boolean fnValidation = firstName.matches("[A-Za-z](['\\-\\s]?[A-Za-z]+)+");
                boolean lnValidation = lastName.matches("[A-Za-z](['\\-\\s]?[A-Za-z]+)+");
                boolean emailValidation = email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]+$");

                if (fnValidation && lnValidation && emailValidation) {
                    if(emailNotUsed(email)) {
                        students.add(new Student(String.valueOf(10000 + students.size()), firstName, lastName, email));
                        counter++;
                        System.out.println("The student has been added.");
                    }
                    else System.out.println("This email is already taken.");
                } else {
                    if (!fnValidation) {
                        System.out.println("Incorrect first name");
                        continue;
                    }
                    if (!lnValidation) {
                        System.out.println("Incorrect last name");
                        continue;
                    }
                    if (!emailValidation) {
                        System.out.println("Incorrect email");
                        continue;
                    }
                }
            }catch (Exception exception){
                System.out.println("Incorrect credentials");
            }
        }
    }

    private boolean emailNotUsed(String email) {
        for(Student currentStudent : students)
            if(currentStudent.getEmail().equals(email))
                return false;

        return true;
    }

    public void addStudentsTest(String input){
    }
}