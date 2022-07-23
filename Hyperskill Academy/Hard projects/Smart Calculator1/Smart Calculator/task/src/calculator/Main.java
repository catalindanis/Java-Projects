package calculator;

import java.util.List;
import java.util.Scanner;

abstract class MathematicalOperation{
    MathematicalOperation(List<String> numbers){
        getResult(numbers);
    }
    public abstract void getResult(List<String> numbers);
}
class Sum extends MathematicalOperation{
    Sum(List<String> numbers) {
        super(numbers);
    }
    public void getResult(List<String> numbers){
        int sum = 0;
        for(int i=0;i<numbers.size();i++)
            sum += Integer.valueOf(numbers.get(i));
        System.out.println(sum);
    }
}

public class Main {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        do{
            String input = scanner.nextLine();
            try{
                MathematicalOperation operation = new Sum(List.of(input.split(" ")));
            }
            catch (NumberFormatException exception){
                if(input.isEmpty())
                    continue;
                else{
                    if(input.equals("/help"))
                        System.out.println("The program calculates the sum of numbers");
                    else break;
                }

            }
        }while(true);
        System.out.println("Bye!");
    }
}
