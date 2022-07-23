
import java.util.Random;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        menu();
    }
    private static void menu(){
        String input = new String();
        try {
            System.out.println("Please, enter the secret code's length:");
            input = scanner.nextLine();
            int nrOfDigits = Integer.valueOf(input);
            System.out.println("Input the number of possible symbols in the code:");
            input = scanner.nextLine();
            int nrOfChars = Integer.valueOf(input);

            if(nrOfChars < nrOfDigits || nrOfDigits == 0) {
                System.out.printf("Error: it's not possible to generate a code with a length of %d with %d unique symbols.",
                        nrOfDigits,
                        nrOfChars);
                return;
            }
            else if(nrOfChars > 36) {
                System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
                return;
            }

            StringBuilder stars = new StringBuilder();
            for (int i = 0; i < nrOfDigits; i++)
                stars.append("*");
            System.out.printf("The secret is prepared: %s (0-9, a-%c).\n", stars.toString(), ('a' + (nrOfChars - 11)));
            System.out.println("Okay, let's start a game!");
            String secretCode = generateCodeWithCharacters(nrOfDigits, nrOfChars);
            String correctStatus = String.format("%d bull(s)", nrOfDigits);
            int turn = 1;
            while (true) {
                System.out.printf("Turn %d:\n", turn);
                String enteredCode = scanner.nextLine();
                String status = codeStatus(enteredCode, secretCode);
                System.out.printf("Gradle: %s\n", status);
                if (status.equals(correctStatus)) {
                    System.out.println("Congratulations! You guessed the secret code.");
                    break;
                }
                turn++;
            }
        }catch (NumberFormatException nfe){
            System.out.printf("Error: \"%s\" isn't a valid number.",input);
        }
    }
    private static String codeStatus(String enteredCode,String correctCode){
        int bulls = 0,cows = 0;
        for(int i=0;i<correctCode.length();i++)
            if(correctCode.charAt(i) == enteredCode.charAt(i)) {
                bulls++;
                cows++;
            }
            else if(enteredCode.contains(String.valueOf(correctCode.charAt(i))))
                cows++;
        if(bulls >= correctCode.length()-1)
            return bulls+" bull(s)";
        else{
            if(bulls != 0 && cows != 0)
                return bulls + " bull(s) and " + cows + " cow(s)";
            else if(cows != 0)
                return cows + " cow(s)";
            return "none";
        }
    }
    private static String generateCodeWithCharacters(int nrOfDigits,int nrOfChars){
        Random random = new Random();
        String code = new String();
        for(int i=0;i<nrOfDigits;i++){
            int digit = random.nextInt(10);
            if(code.contains(String.valueOf(digit))){
                int asciiCode = digit + 'a';
                if(!code.contains(String.valueOf((char)asciiCode)) && nrOfChars > 0) {
                    code += (char) asciiCode;
                    nrOfChars --;
                }
                else nrOfDigits++;
            }
            else code += digit;
        }
        //System.out.println(code);
        return code;
    }
    private static String generateCodeByRandom(int n){
        Random random = new Random();
        String code = new String();
        for(int i=0;i<n;i++) {
            int digit = random.nextInt(10);
            if(!code.contains(String.valueOf(digit))) {
                code += digit;
            }
            else n++;
        }
        return code;
    }
    @Deprecated
    private static String generateCodeByTime(int n){
        String number = String.valueOf(System.nanoTime());
        String code = new String();
        for(int i=0;i<n;i++) {
            if(!code.contains(String.valueOf(number.charAt(i)))) {
                code += number.charAt(i);
            }
            else n++;
        }
        return code;
    }
}
