
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.Stack;

abstract class MathematicalOperation{
    MathematicalOperation(String expression){
        getResult(expression);
    }
    public abstract void getResult(String expression);
}
//class Addition extends MathematicalOperation{
//    Addition(List<String> numbers) {
//        super(numbers);
//    }
//    public void getResult(List<String> numbers){
//        int addition = 0;
//        for(int i=0;i<numbers.size();i++)
//            addition += Integer.valueOf(numbers.get(i));
//        System.out.println(addition);
//    }
//}
//class Subtraction extends MathematicalOperation{
//    Subtraction(List<String> numbers) {
//        super(numbers);
//    }
//    public void getResult(List<String> numbers){
//        int subtraction = 0;
//        for(int i=0;i<numbers.size();i++)
//            subtraction -= Integer.valueOf(numbers.get(i));
//        System.out.println(subtraction);
//    }
//}

class Operation extends MathematicalOperation{
    Operation(String expression) {
        super(expression);
    }
    public void getResult(String expression){
        Stack<BigInteger> stack = new Stack<>();
        String[] values = expression.split(" ");
        for(int i=0;i<values.length;i++){
            //System.out.println("("+values[i]+")");
            if(values[i].matches("\\d+")) {
                stack.add(new BigInteger(values[i]));
            }
            else if(values[i].matches("[a-zA-Z]+")) {
                stack.add(Main.values.get(values[i]));
            }
            else {
                if(values[i].isEmpty())
                    continue;
                BigInteger v1,v2;
                v1 = v2 = BigInteger.ZERO;
                try {
                    v1 = stack.pop();
                    v2 = stack.pop();
                }
                catch (NullPointerException npe){
                    System.out.println("Unknown variable");
                    return;
                }
                switch (values[i]){
                    case "+":
                        stack.add(v2.add(v1));
                        break;
                    case "-":
                        stack.add(v2.subtract(v1));
                        break;
                    case "*":
                        stack.add(v2.multiply(v1));
                        break;
                    case "/":
                        stack.add(v2.divide(v1));
                        break;
                    case "^":
                        break;
                }
            }
        }
        System.out.println(stack.peek());
    }
}

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static LinkedHashMap<String,BigInteger> values = new LinkedHashMap<>();
    public static void main(String[] args) {
        menu();
        System.out.println("Bye!");
    }

    public static void menu(){
        do{
            Expression input = new Expression(scanner.nextLine().replaceAll(" ",""));
            if(input.toString().matches("[+-]?\\d+"))
                System.out.println(input.toString());
            else {
                if (input.toString().matches("/.*")) {
                    switch (input.toString()) {
                        case "/help":
                            System.out.println("This is a calculator that makes operations for you");
                            break;
                        case "/exit":
                            return;
                        default:
                            System.out.println("Unknown command");
                    }
                } else {
                    String evaluation = input.evaluateExpression();
                    //System.out.println(evaluation);
                    switch (evaluation) {
                        case "Invalid identifier":
                            System.out.println("Invalid identifier");
                            break;
                        case "Invalid assignment":
                            System.out.println("Invalid assignment");
                            break;
                        case "Unknown variable":
                            System.out.println("Unknown variable");
                            break;
                        case "Invalid expression":
                            System.out.println("Invalid expression");
                            break;
                        case "continue":
                            break;
                        case "correctAssignment":
                            break;
                        default:
                            Operation operation = new Operation(evaluation);
                            break;
                    }
                }
            }
        }while(true);
    }
}
class Expression{
    private String Expression;
    public Expression(String expression){
        this.Expression = expression;
    }
    public String evaluateExpression(){
        if(Expression.isEmpty())
            return "continue";
        if(isArithmeticExpression(Expression)) {
            return formatExpression(Expression);
        }

        String leftTerm = getLeftTerm(Expression);
        String rightTerm = getRightTerm(Expression);

        if(invalidIdentifier(leftTerm,rightTerm))
            return "Invalid identifier";

        if (invalidAssignment(rightTerm))
            return "Invalid assignment";

        if(rightTerm == null){
            if(Main.values.get(leftTerm) != null) {
                System.out.println(String.valueOf(Main.values.get(leftTerm)));
                return "correctAssignment";
            }
            return "Unknown variable";
        }

        if(unknownVariable(rightTerm)) {
            if(rightTerm.matches("[\\+-]?[0-9]+$"))
                Main.values.put(leftTerm,new BigInteger(rightTerm));
            else
                return "Unknown variable";
        }
        else Main.values.put(leftTerm,Main.values.get(rightTerm));

        return "correctAssignment";
    }

    private String formatExpression(String expression){
        if(expression.matches(".*[\\*]{2,}.*") || expression.matches(".*[/]{2,}.*")
                || expression.matches(".*[\\^]{2,}.*"))
            return "Invalid expression";
        String initialExp = expression;
        do{
            initialExp = expression;
            expression = expression.replaceAll("--","+");
            expression = expression.replaceAll("\\+-","-");
            expression = expression.replaceAll("\\+\\+","+");
            expression = expression.replaceAll("\\s+","");
        }while(!expression.equals(initialExp));
        expression = expression.replaceAll("\\*"," * ");
        expression = expression.replaceAll("\\+"," + ");
        expression = expression.replaceAll("-"," - ");
        expression = expression.replaceAll("/"," / ");
        expression = expression.replaceAll("\\^"," ^ ");
        expression = expression.replaceAll("\\(","( ");
        expression = expression.replaceAll("\\)"," )");
        //System.out.println(expression);
        String result = Test.infixToPostfix(expression);
        return result;
    }

    private boolean unknownVariable(String rightTerm){
        if(!Main.values.containsKey(rightTerm))
            return true;
        return false;
    }
    private boolean invalidIdentifier(String leftTerm,String rightTerm){
        if(!leftTerm.matches("^[A-Za-z]+$"))
            return true;
        if(rightTerm == null && Expression.matches(".*=+.*"))
            return true;
        return false;
    }
    private boolean invalidAssignment(String rightTerm){
        if(!(rightTerm == null)) {
            if (rightTerm.matches(".*[A-Za-z]+.*") &&
                    rightTerm.matches(".*[0-9]+.*"))
                return true;
            if (rightTerm.matches(".*=+.*"))
                return true;
        }
        return false;
    }
    private boolean isArithmeticExpression(String expression){
        if(expression.matches(".*[\\+-/\\*\\^]+.*") && !expression.contains("=")) {
            return true;
        }
        return false;
    }
    private String getLeftTerm(String expression){
        if(expression.matches(".*=.*"))
            return expression.split("=")[0].replaceAll(" ","");
        else return expression;
    }
    private String getRightTerm(String expression){
        try {
            return expression.split("=",2)[1].replaceAll(" ","");
        }catch (Exception exception){return null;}
    }
    public String toString(){
        return Expression;
    }
}

class Test{
    static int Prec(String ch)
    {
        switch (ch)
        {
            case "+":
            case "-":
                return 1;

            case "*":
            case "/":
                return 2;

            case "^":
                return 3;
        }
        return -1;
    }
    static String infixToPostfix(String exp)
    {
        String result = new String("");
        int opened = 0;
        int closed = 0;

        Stack<String> stack = new Stack<>();

        for (int i = 0; i<exp.split(" ").length; ++i)
        {
            String c = exp.split(" ")[i];

            if (c.matches("^[A-Za-z]+$") || c.matches("^[0-9]+$")) {
                result += " ";
                result += c;
            }

            else if (c.matches(".*\\(.*")) {
                opened ++;
                stack.push(c);
            }

            else if (c.equals(")"))
            {
                closed ++;
                while (!stack.isEmpty() &&
                        !stack.peek().equals("(")) {
                    result += " ";
                    result += stack.pop();
                }
                if(!stack.isEmpty())
                    stack.pop();
            }
            else
            {
                while (!stack.isEmpty() && Prec(c)
                        <= Prec(stack.peek())){
                    result += " ";
                    result += stack.pop();
                }
                stack.push(c);
            }

        }
        if(opened != closed)
            return "Invalid expression";
        while (!stack.isEmpty()){
            if(stack.peek().equals("("))
                return "Invalid expression";
            result += " ";
            result += stack.pop();
        }
        //System.out.println(result);
        return result;
    }
}