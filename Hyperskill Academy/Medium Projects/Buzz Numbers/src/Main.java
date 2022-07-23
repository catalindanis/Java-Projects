import java.lang.reflect.Array;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    static ArrayList<String> properties = new ArrayList<>(Arrays.asList(new String[]{"EVEN","ODD", "BUZZ", "DUCK",
            "PALINDROMIC", "GAPFUL", "SPY", "SQUARE", "SUNNY", "JUMPING", "SAD", "HAPPY", "-EVEN","-ODD", "-BUZZ", "-DUCK",
            "-PALINDROMIC", "-GAPFUL", "-SPY", "-SQUARE", "-SUNNY", "-JUMPING", "-SAD", "-HAPPY"}));
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("Welcome to Amazing Numbers!");
        printInstructions();
        boolean exitRequested = false;
        while(!exitRequested) {
            System.out.println("Enter a request: ");
            String number = scanner.nextLine();
            if(number.isEmpty()){
                printInstructions();
            }
            else if(number.equals("0"))
                exitRequested = true;
            else {
                int length = number.split(" ").length;
                if (length == 1) {
                    try {
                        if (Long.valueOf(number.split(" ")[0]) > 0)
                            getPropertiesOneNumber(Long.valueOf(number.split(" ")[0]));
                        else System.out.println("The first parameter should be a natural number or zero.");
                    } catch (Exception e) {
                        System.out.println("The first parameter should be a natural number or zero.");
                    }
                } else if (length == 2) {
                    try {
                        if (Long.valueOf(number.split(" ")[0]) > 0) {
                            if (Long.valueOf(number.split(" ")[1]) > 0)
                                for (int i = 0; i < Integer.valueOf(number.split(" ")[1]); i++)
                                    getPropertiesOneNumber(Long.valueOf(number.split(" ")[0]) + i, true);
                            else System.out.println("The second parameter should be a natural number.");
                        } else System.out.println("The first parameter should be a natural number or zero.");
                    } catch (Exception e) {
                        System.out.println("The second parameter should be a natural number.");
                    }
                }
                else{
                    try {
                        long value = Long.valueOf(number.split(" ")[0]);
                        if (value > 0) {
                            try {
                                long times = Long.valueOf(number.split(" ")[1]);
                                if (times > 0) {
                                    ArrayList<String> new_properties = new ArrayList<>(Arrays.asList(number.split(" ")));
                                    new_properties.remove(0);
                                    new_properties.remove(0);
                                    for (int i = 0; i < new_properties.size(); i++)
                                        new_properties.set(i, new_properties.get(i).toUpperCase());
                                    boolean errors = searchForCommandsError(new_properties);
                                    if (!errors) {
                                        String mutually[] = new String[2];
                                        mutually = searchForMutuallyProperties(new_properties);
                                        if (mutually[0].isEmpty() && mutually[1].isEmpty()) {
                                            while (times > 0) {
                                                boolean isGood = true;
                                                for(String property:new_properties){
                                                    if(property.contains("-")){
                                                        if(checkForCommand(
                                                                property.replaceAll("-",""),value)) {
                                                            isGood = false;
                                                            break;
                                                        }
                                                    }
                                                    else if(!checkForCommand(property,value)){
                                                        isGood = false;
                                                        break;
                                                    }
                                                }
                                                if(isGood) {
                                                    getPropertiesOneNumber(value, true);
                                                    times--;
                                                }
                                                value++;
                                            }
                                        } else printMutuallyProperties(mutually[0], mutually[1]);
                                    }
                                } else System.out.println("The second parameter should be a natural number.");
                            }
                            catch (Exception exception){
                                System.out.println("The second parameter should be a natural number.");
                            }
                        } else {
                            System.out.println("The first parameter should be a natural number or zero.");
                        }
                    }
                    catch(Exception exception){
                        System.out.println("The first parameter should be a natural number or zero.");
                    }
                }
            }

        }
        System.out.println("Goodbye!");
    }
    private static boolean checkForCommand(String command,long value){
        boolean isGood = false;
        switch (command) {
            case "EVEN":
                isGood = even(value);
                break;
            case "ODD":
                isGood = !even(value);
                break;
            case "BUZZ":
                isGood = buzzNumber(value);
                break;
            case "DUCK":
                isGood = duck(value);
                break;
            case "PALINDROMIC":
                isGood = palindromic(value);
                break;
            case "GAPFUL":
                isGood = gapful(value);
                break;
            case "SPY":
                isGood = spy(value);
                break;
            case "SQUARE":
                isGood = square(value);
                break;
            case "SUNNY":
                isGood = sunny(value);
                break;
            case "JUMPING":
                isGood = jumping(value);
                break;
            case "SAD":
                isGood = !happy(value);
                break;
            case "HAPPY":
                isGood = happy(value);
                break;
        }
        return isGood;
    }

    private static void printCommandError(String command){
        System.out.printf("The property [%s] is wrong.\n", command);
        System.out.println("Available properties:");
        System.out.println("[EVEN, ODD, BUZZ, DUCK, PALINDROMIC, GAPFUL, SPY, SQUARE, SUNNY, JUMPING, HAPPY, SAD]");
    }
    private static void printMutuallyProperties(String command1,String command2){
        System.out.printf("The request contains mutually exclusive properties: [%s, %s]\n",command1,command2);
        System.out.println("There are no numbers with these properties.");
    }

    private static void printInstructions(){
        System.out.println(
                "Supported requests:\n"+
                "- enter a natural number to know its properties;\n"+
                "- enter two natural numbers to obtain the properties of the list:\n"+
                "* the first parameter represents a starting number;\n"+
                "* the second parameter shows how many consecutive numbers are to be printed;\n"+
                "- two natural numbers and properties to search for;\n"+
                "- a property preceded by minus must not be present in numbers;\n"+
                "- separate the parameters with one space;\n"+
                "- enter 0 to exit.");
    }

    private static void getPropertiesOneNumber(long number){
        System.out.printf("Properties of %d\n",number);
        System.out.printf("buzz: %b\n",buzzNumber(number));
        System.out.printf("duck: %b\n",duck(number));
        System.out.printf("palindromic: %b\n",palindromic(number));
        System.out.printf("gapful: %b\n",gapful(number));
        System.out.printf("spy: %b\n",spy(number));
        System.out.printf("square: %b\n",square(number));
        System.out.printf("sunny: %b\n",sunny(number));
        System.out.printf("jumping: %b\n",jumping(number));
        System.out.printf("happy: %b\n",happy(number));
        System.out.printf("sad: %b\n",!happy(number));
        System.out.printf("even: %b\n",even(number));
        System.out.printf("odd: %b\n",!even(number));
    }
    private static void getPropertiesOneNumber(long number,boolean bool){
        System.out.printf("%d is ",number);
        if(buzzNumber(number))
            System.out.print("buzz, ");
        if(duck(number))
            System.out.print("duck, ");
        if(palindromic(number))
            System.out.print("palindromic, ");
        if(gapful(number))
            System.out.print("gapful, ");
        if(spy(number))
            System.out.print("spy, ");
        if(square(number))
            System.out.print("square, ");
        if(sunny(number))
            System.out.print("sunny, ");
        if(jumping(number))
            System.out.print("jumping, ");
        if(happy(number))
            System.out.print("happy, ");
        if(!happy(number))
            System.out.print("sad, ");
        if(even(number))
            System.out.print("even, ");
        if(!even(number))
            System.out.print("odd, ");
        System.out.println();
    }
    private static String[] searchForMutuallyProperties(ArrayList<String> new_properties){
        for(String property : new_properties)
            if(new_properties.contains("-"+property))
                return new String[]{property,"-"+property};

        if(new_properties.contains("ODD") && new_properties.contains("EVEN"))
            return new String[]{"ODD","EVEN"};

        if(new_properties.contains("-ODD") && new_properties.contains("-EVEN"))
            return new String[]{"-ODD","-EVEN"};

        if(new_properties.contains("DUCK") && new_properties.contains("SPY"))
            return new String[]{"DUCK","SPY"};

        if (new_properties.contains("SQUARE") && new_properties.contains("SUNNY"))
            return new String[]{"SQUARE","SUNNY"};

        if (new_properties.contains("-SAD") && new_properties.contains("-HAPPY"))
            return new String[]{"-SAD","-HAPPY"};

        if (new_properties.contains("SAD") && new_properties.contains("HAPPY"))
            return new String[]{"SAD","HAPPY"};

        return new String[]{"",""};
    }
    private static boolean searchForCommandsError(ArrayList<String> new_properties){
        ArrayList<String> errors = new ArrayList<>();
        for(String property : new_properties)
            if(!properties.contains(property)){
                errors.add(property);
            }
        if(errors.size() == 0)
            return false;
        else{
            if(errors.size() == 1)
                printCommandError(errors.get(0));
            else {
                System.out.print("The properties [");
                for(int i=0;i<errors.size();i++) {
                    if (i + 1 == errors.size())
                        System.out.print(errors.get(i)+"] ");
                    else System.out.print(errors.get(i)+", ");
                }
                System.out.print("are wrong.\n"+"Available properties: \n[EVEN, ODD, BUZZ, DUCK, PALINDROMIC, GAPFUL, SPY, SQUARE, SUNNY, JUMPING, HAPPY, SAD]\n");
            }
        }
        return true;
    }

    private static ArrayList<String> getRestrictions(ArrayList<String> properties){
        ArrayList<String> restrictions = new ArrayList<>();
        for(String property : properties)
            if(property.contains("-"))
                restrictions.add(property.replace("-",""));
        return restrictions;
    }

    private static boolean happy(long number){
        long initial_number = number;
        while(number!=1){
            long sum = 0;
            while(number > 0){
                sum = sum + ((number%10) * (number%10));
                number/=10;
            }
            number = sum;
            if(number == initial_number || number == 4)
                return false;
        }
        return true;
    }
    private static boolean jumping(long number){
        boolean jumping = true;
        while(number > 9 && jumping){
            int lastDigit = (int)(number%10);
            int secondDigit = (int)((number/10)%10);
            if(Math.abs((lastDigit-secondDigit)) != 1)
                jumping = false;
            number/=10;
        }
        return jumping;
    }

    private static boolean square(long number){
        return (Math.sqrt(number) == (int) Math.sqrt(number));
    }
    private static boolean sunny(long number){
        return (Math.sqrt(number+1) == (int) (Math.sqrt(number+1)));
    }

    private static boolean spy(long number){
        long prod=1,sum = 0;
        while(number > 0){
            prod*=(number%10);
            sum+=(number%10);
            number/=10;
        }
        //if(number == 2)
            //System.out.println(prod+" "+sum);
        return prod == sum;
    }
    private static boolean gapful(long number){
        long copy = number;
        int c = 0;
        int firstDigit= 0,lastDigit = (int) (number%10);
        while(copy > 0){
            c++;
            firstDigit = (int)copy;
            copy/=10;
        }
        if(c < 3)
            return false;
        else{
            int nr = firstDigit*10 + lastDigit;
            return number%nr == 0;
        }
    }

    private static boolean palindromic(long number){
        long inv = 0;
        long number_copy = number;
        while(number_copy > 0){
            inv = inv*10 + number_copy%10;
            number_copy/=10;
        }
        return number == inv;
    }
    private static boolean even(long number){
        if (number % 2 == 0)
            return true;
        else return false;
    }

    private static boolean duck(long number){
        while(number > 9){
            if(number%10 == 0)
                return true;
            number/=10;
        }
        return false;
    }

    /*
    private static void buzzNumber(int number){
        if (number % 10 == 7) {
            System.out.println("It is a Buzz number.");
            System.out.println("Explanation:");
            if (number % 7 == 0) {
                System.out.printf("%d is divisible by 7 and ends with 7.", number);
            } else {
                System.out.printf("%d ends with 7.", number);
            }
        } else if (number % 7 == 0) {
            System.out.println("It is a Buzz number.");
            System.out.println("Explanation:");
            System.out.printf("%d is divisible by 7.", number);
        } else {
            System.out.println("It is not a Buzz number.");
            System.out.println("Explanation:");
            System.out.printf("%d is neither divisible by 7 nor does it end with 7.", number);
        }
    }
    */
    private static boolean buzzNumber(long number){
        return number%10 == 7 || number%7 == 0;
    }
}
