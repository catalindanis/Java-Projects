import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.EmptyStackException;
import java.util.Stack;

public class Calculator extends JFrame {
    static boolean isNegated;
    public Calculator() {
        super("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(330, 550);
        setLocationRelativeTo(null);

        initComponents();

        setLocationRelativeTo(null);
        setLayout(null);
        setVisible(true);
    }

    private void initComponents() {

        Font font = new Font("Arial",Font.PLAIN,21);
        Font font1 = new Font("Arial",Font.BOLD,50);

        JLabel resultLabel = new JLabel("0");
        setAttributtes(resultLabel,"ResultLabel",Color.BLACK,font1,10,15,300,50);

        JLabel equationLabel = new JLabel("");
        setAttributtes(equationLabel,"EquationLabel",Color.GREEN.darker(),font,10,80,300,25);

        JButton buttonParantheses = new JButton("( )");
        setAttributtes(buttonParantheses, "Parentheses", Color.LIGHT_GRAY, false, font,
                BorderFactory.createEmptyBorder(), 10, 150, 70, 50, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int leftParanthesis = 0;
                        int rightParanthesis = 0;
                        for(int i=0;i<equationLabel.getText().length();i++) {
                            if (equationLabel.getText().charAt(i) == '(')
                                leftParanthesis++;
                            if(equationLabel.getText().charAt(i) == ')')
                                rightParanthesis++;
                        }
                        try {
                            if (leftParanthesis == rightParanthesis) {
                                if(equationLabel.getText().isEmpty())
                                    equationLabel.setText(equationLabel.getText() + "(");
                                else if(!Character.isDigit(equationLabel.getText().charAt(equationLabel.getText().length()-1)))
                                    equationLabel.setText(equationLabel.getText() + "(");
                                else leftParanthesis--;
                                leftParanthesis++;
                            }
                            else if(equationLabel.getText().charAt(equationLabel.getText().length() - 1) == '(' ||
                                    isOperator(equationLabel.getText().charAt(equationLabel.getText().length() - 1))){
                                equationLabel.setText(equationLabel.getText() + "(");
                                leftParanthesis++;
                            }
                            else {
                                equationLabel.setText(equationLabel.getText() + ")");
                                rightParanthesis++;
                            }
                        }catch (StringIndexOutOfBoundsException exception){}
                    }
                });

        JButton buttonClearElement = new JButton("CE");
        setAttributtes(buttonClearElement, "Clear Element", Color.LIGHT_GRAY, false, font,
                BorderFactory.createEmptyBorder(), 85, 150, 70, 50, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            if (isOperator(equationLabel.getText().charAt(equationLabel.getText().length() - 1))) {
                                equationLabel.setText(equationLabel.getText().substring(0, equationLabel.getText().length() - 1));
                            } else {
                                int index = 0;
                                for (int i = equationLabel.getText().length() - 1; i >= 0 && !isOperator(equationLabel.getText().charAt(i)); i--)
                                    index = i;
                                equationLabel.setText(equationLabel.getText().substring(0, index));
                            }
                        }catch (StringIndexOutOfBoundsException exception){}
                    }
                });

        JButton buttonClear = new JButton("C");
        setAttributtes(buttonClear, "Clear", Color.LIGHT_GRAY, false, font,
                BorderFactory.createEmptyBorder(), 160, 150, 70, 50, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        isNegated = false;
                        equationLabel.setText("");
                        resultLabel.setText("0");
                    }
                });

        JButton buttonDelete = new JButton("Del");
        setAttributtes(buttonDelete, "Delete", Color.LIGHT_GRAY, false, font,
                BorderFactory.createEmptyBorder(), 235, 150, 70, 50, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            equationLabel.setText(equationLabel.getText().substring(0, equationLabel.getText().length() - 1));
                        } catch (Exception exception) {}
                    }
                });

        JButton buttonPowerTwo = new JButton("x²");
        setAttributtes(buttonPowerTwo, "PowerTwo", Color.LIGHT_GRAY, false, font,
                BorderFactory.createEmptyBorder(), 10, 210, 70, 50, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        equationLabel.setText(equationLabel.getText()+"^(2)");
                    }
                });

        JButton buttonPowerY = new JButton("xʸ");
        setAttributtes(buttonPowerY, "PowerY", Color.LIGHT_GRAY, false, font,
                BorderFactory.createEmptyBorder(), 85, 210, 70, 50, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        equationLabel.setText(equationLabel.getText()+"^(");
                    }
                });

        JButton buttonSquareRoot = new JButton("\u221A");
        setAttributtes(buttonSquareRoot, "SquareRoot", Color.LIGHT_GRAY, false, font,
                BorderFactory.createEmptyBorder(), 160, 210, 70, 50, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        equationLabel.setText(equationLabel.getText()+"\u221A(");
                    }
                });

        JButton buttonDivide = new JButton("\u00F7");
        setAttributtes(buttonDivide, "Divide", Color.LIGHT_GRAY, false, font,
                BorderFactory.createEmptyBorder(), 235, 210, 70, 50, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        equationLabel.setText(formatExpression(equationLabel.getText()));
                        if (equationLabel.getText().length() > 0) {
                            if (isOperator(equationLabel.getText().charAt(equationLabel.getText().length() - 1)))
                                equationLabel.setText(equationLabel.getText().substring(
                                        0, equationLabel.getText().length() - 1
                                ) + "\u00F7");
                            else equationLabel.setText(equationLabel.getText() + "\u00F7");
                        }
                    }
                });

        JButton buttonSeven = new JButton("7");
        setAttributtes(buttonSeven, "Seven", Color.WHITE, false, font,
                BorderFactory.createEmptyBorder(), 10, 270, 70, 50, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        equationLabel.setForeground(Color.GREEN.darker());
                        equationLabel.setText(equationLabel.getText() + "7");
                    }
                });

        JButton buttonEight = new JButton("8");
        setAttributtes(buttonEight, "Eight", Color.WHITE, false, font,
                BorderFactory.createEmptyBorder(), 85, 270, 70, 50, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        equationLabel.setForeground(Color.GREEN.darker());
                        equationLabel.setText(equationLabel.getText() + "8");
                    }
                });

        JButton buttonNine = new JButton("9");
        setAttributtes(buttonNine, "Nine", Color.WHITE, false, font,
                BorderFactory.createEmptyBorder(), 160, 270, 70, 50, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        equationLabel.setForeground(Color.GREEN.darker());
                        equationLabel.setText(equationLabel.getText() + "9");
                    }
                });

        JButton buttonMultiply = new JButton("\u00D7");
        setAttributtes(buttonMultiply, "Multiply", Color.LIGHT_GRAY, false, font,
                BorderFactory.createEmptyBorder(), 235, 270, 70, 50, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        equationLabel.setText(formatExpression(equationLabel.getText()));
                        if (equationLabel.getText().length() > 0) {
                            if (isOperator(equationLabel.getText().charAt(equationLabel.getText().length() - 1)))
                                equationLabel.setText(equationLabel.getText().substring(
                                        0, equationLabel.getText().length() - 1
                                ) + "\u00D7");
                            else
                                equationLabel.setText(equationLabel.getText() + "\u00D7");
                        }
                    }
                });

        JButton buttonFour = new JButton("4");
        setAttributtes(buttonFour, "Four", Color.WHITE, false, font,
                BorderFactory.createEmptyBorder(), 10, 330, 70, 50, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        equationLabel.setForeground(Color.GREEN.darker());
                        equationLabel.setText(equationLabel.getText() + "4");
                    }
                });

        JButton buttonFive = new JButton("5");
        setAttributtes(buttonFive, "Five", Color.WHITE, false, font,
                BorderFactory.createEmptyBorder(), 85, 330, 70, 50, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        equationLabel.setForeground(Color.GREEN.darker());
                        equationLabel.setText(equationLabel.getText() + "5");
                    }
                });

        JButton buttonSix = new JButton("6");
        setAttributtes(buttonSix, "Six", Color.WHITE, false, font,
                BorderFactory.createEmptyBorder(), 160, 330, 70, 50, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        equationLabel.setForeground(Color.GREEN.darker());
                        equationLabel.setText(equationLabel.getText() + "6");
                    }
                });

        JButton buttonSubtract = new JButton("-");
        setAttributtes(buttonSubtract, "Subtract", Color.LIGHT_GRAY, false, font,
                BorderFactory.createEmptyBorder(), 235, 330, 70, 50, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        equationLabel.setText(formatExpression(equationLabel.getText()));
                        if (equationLabel.getText().length() > 0) {
                            if (isOperator(equationLabel.getText().charAt(equationLabel.getText().length() - 1)))
                                equationLabel.setText(equationLabel.getText().substring(
                                        0, equationLabel.getText().length() - 1
                                ) + "-");
                            else
                                equationLabel.setText(equationLabel.getText() + "-");
                        }
                    }
                });

        JButton buttonOne = new JButton("1");
        setAttributtes(buttonOne, "One", Color.WHITE, false, font,
                BorderFactory.createEmptyBorder(), 10, 390, 70, 50, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        equationLabel.setForeground(Color.GREEN.darker());
                        equationLabel.setText(equationLabel.getText() + "1");
                    }
                });

        JButton buttonTwo = new JButton("2");
        setAttributtes(buttonTwo, "Two", Color.WHITE, false, font,
                BorderFactory.createEmptyBorder(), 85, 390, 70, 50, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        equationLabel.setForeground(Color.GREEN.darker());
                        equationLabel.setText(equationLabel.getText() + "2");
                    }
                });

        JButton buttonThree = new JButton("3");
        setAttributtes(buttonThree, "Three", Color.WHITE, false, font,
                BorderFactory.createEmptyBorder(), 160, 390, 70, 50, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        equationLabel.setForeground(Color.GREEN.darker());
                        equationLabel.setText(equationLabel.getText() + "3");
                    }
                });

        JButton buttonAdd = new JButton("\u002B");
        setAttributtes(buttonAdd, "Add", Color.LIGHT_GRAY, false, font,
                BorderFactory.createEmptyBorder(), 235, 390, 70, 50, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        equationLabel.setText(formatExpression(equationLabel.getText()));
                        if (equationLabel.getText().length() > 0) {
                            if (isOperator(equationLabel.getText().charAt(equationLabel.getText().length() - 1)))
                                equationLabel.setText(equationLabel.getText().substring(
                                        0, equationLabel.getText().length() - 1
                                ) + "+");
                            else
                                equationLabel.setText(equationLabel.getText() + "+");
                        }
                    }
                });

        JButton buttonPlusMinus = new JButton("±");
        setAttributtes(buttonPlusMinus, "PlusMinus", Color.WHITE, false, font,
                BorderFactory.createEmptyBorder(), 10, 450, 70, 50, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(equationLabel.getText().startsWith("(-")){
                            if(isEquation(equationLabel.getText(),1))
                                equationLabel.setText(equationLabel.getText().substring(3,equationLabel.getText().length()));
                            else equationLabel.setText(equationLabel.getText().substring(2,equationLabel.getText().length()));
                            isNegated = false;
                        }
                        else {
                            if(isEquation(equationLabel.getText(),0))
                                equationLabel.setText("(-("+equationLabel.getText()+"))");
                            else equationLabel.setText("(-"+equationLabel.getText());
                            isNegated = true;
                        }
                    }
                });

        JButton buttonZero = new JButton("0");
        setAttributtes(buttonZero, "Zero", Color.WHITE, false, font,
                BorderFactory.createEmptyBorder(), 85, 450, 70, 50, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        equationLabel.setForeground(Color.GREEN.darker());
                        equationLabel.setText(equationLabel.getText() + "0");
                    }
                });

        JButton buttonDot = new JButton(".");
        setAttributtes(buttonDot, "Dot", Color.WHITE, false, font,
                BorderFactory.createEmptyBorder(), 160, 450, 70, 50, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        equationLabel.setText(equationLabel.getText() + ".");
                    }
                });

        JButton buttonEquals = new JButton("=");
        setAttributtes(buttonEquals, "Equals", Color.LIGHT_GRAY, false, font,
                BorderFactory.createEmptyBorder(), 235, 450, 70, 50, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            resultLabel.setText(calculateExpression(toPostfix(equationLabel.getText())).replaceAll("\\.0+", ""));
                        } catch (ArithmeticException | EmptyStackException exception) {
                            equationLabel.setForeground(Color.red.darker());
                            //exception.printStackTrace();
                        }
                    }
                });

    }

    private void setAttributtes(JLabel label, String labelName, Color color, Font font,
                                int x, int y, int width, int height) {
        label.setName(labelName);
        label.setForeground(color);
        label.setFont(font);
        label.setBounds(x, y, width, height);
        add(label);
    }

    private void setAttributtes(JButton button, String buttonName , Color color, boolean textBorder, Font font,
                                Border emptyBorder , int x , int y , int width , int height, ActionListener actionListener) {
        button.setName(buttonName);
        button.setBackground(color);
        button.setFocusPainted(textBorder);
        button.setFont(font);
        button.setBorder(emptyBorder);
        button.setBounds(x, y, width, height);
        button.addActionListener(actionListener);
        add(button);
    }

    private static String formatExpression(String expression){
        StringBuilder finalExpression = new StringBuilder();
        for(int i=0;i<expression.length();i++) {
            if (expression.charAt(i) == '.') {
                try {
                    if (isOperator(expression.charAt(i - 1)))
                        finalExpression.append("0");
                } catch (Exception exception) {
                    finalExpression.append("0");
                }
            }
            finalExpression.append(expression.charAt(i));

            if (expression.charAt(i) == '.') {
                try {
                    if (isOperator(expression.charAt(i +1)))
                        finalExpression.append("0");
                } catch (Exception exception) {
                    finalExpression.append("0");
                }
            }
        }
        return finalExpression.toString();
    }

    private static boolean isOperator(char character){
        return character == '-' || character == '\u00D7' || character == '\u00F7'
                || character == '+' || character == '^' || character == '\u221A';
    }

    private static boolean isEquation(String expression, int isNegated){
        int operators = 0;
        for(int i=0;i<expression.length();i++)
            if (isOperator(expression.charAt(i)))
                operators ++;
        return operators > isNegated;
    }

    private static String calculateExpression(String expression) throws ArithmeticException,EmptyStackException{
        expression = expression.replaceAll("\\s{2,}"," ");
        //System.out.println(expression);
        Stack<String> stack = new Stack<>();
        String[] values = expression.split(" ");
        for(int i=0;i<values.length;i++){
            //System.out.println(stack.toString());
            if(values[i].matches("[\\d\\.]+")) {
                stack.add(values[i]);
            }
            else {
                if(values[i].isEmpty())
                    continue;
                BigDecimal v1,v2;
                v1 = v2 = BigDecimal.ZERO;
                v1 = new BigDecimal(stack.pop());
                v2 = new BigDecimal(stack.pop());

                if(!v1.toString().contains("."))
                    v1 = new BigDecimal(v1.toString() + ".0");
                if(!v2.toString().contains("."))
                    v2 = new BigDecimal(v2.toString() + ".0");

                switch (values[i]){
                    case "+":
                        stack.add(String.valueOf(v2.add(v1)));
                        break;
                    case "-":
                        stack.add(String.valueOf(v2.subtract(v1)));
                        break;
                    case "\u00D7":
                        stack.add(String.valueOf(v2.multiply(v1)));
                        break;
                    case "\u00F7":
                        stack.add(String.valueOf(v2.divide(v1)));
                        break;
                    case "^":
                        stack.add(String.valueOf(v2.pow(Integer.valueOf(String.valueOf(v1.toBigInteger())))));
                        break;
                }
            }
        }
        //System.out.println(stack.peek());
        if(isNegated)
            return "-"+stack.peek().toString();
        return stack.peek().toString();
    }

    private static String toPostfix(String expression){
        StringBuilder finalExpression = new StringBuilder();
        for(int i=0;i<expression.length();i++)
            if(expression.charAt(i) == '\u221A'){
                int end = 0;
                int open = 1;
                for(int j=i+2;j<expression.length() && open != 0;j++) {
                    if (expression.charAt(j) == ')')
                        open--;
                    else if (expression.charAt(j) == '(')
                        open++;
                    end = j;
                }
                //System.out.println(expression.substring(i+2,end));
                String result = String.valueOf(Math.sqrt(Double.valueOf(calculateExpression(toPostfix(expression.substring(i+2,end))))));
                i = end;
                finalExpression.append(result);
            }
            else finalExpression.append(expression.charAt(i));
        //System.out.println("final expression : "+finalExpression.toString());
        return Convert.infixToPostfix(finalExpression.toString());
    }

    public static void main(String[] args) throws Exception {
        Runnable initFrame = new Runnable() {
            @Override
            public void run() {
                new Calculator();
            }
        };

        SwingUtilities.invokeAndWait(initFrame);
    }
}

class Convert{
    static int Prec(String ch)
    {
        switch (ch)
        {
            case "+":
            case "-":
                return 1;

            case "\u00D7":
            case "\u00F7":
                return 2;

            case "^":
                return 3;
        }
        return -1;
    }
    static String infixToPostfix(String exp)
    {
        if(exp.startsWith("(-"))
            exp = exp.replaceFirst("\\(-","(");

        if(Calculator.isNegated){
            for(int i=0;i<exp.length();i++)
                if(exp.charAt(i) == '+')
                    exp = exp.substring(0,i)+"-"+exp.substring(i+1,exp.length());
                else if(exp.charAt(i) == '-')
                    exp = exp.substring(0,i)+"+"+exp.substring(i+1,exp.length());
        }

        //System.out.println(exp);
        StringBuilder expression = new StringBuilder("");
        for(int i=0;i<exp.length();i++){
            if(exp.charAt(i) == '+' || exp.charAt(i) == '×' ||
                    exp.charAt(i) == '÷' || exp.charAt(i) == '-' || exp.charAt(i) == '^'
                    || exp.charAt(i) == '(' || exp.charAt(i) == ')') {

                expression.append(" ");
                expression.append(exp.charAt(i));
                expression.append(" ");

            }
            else{
                expression.append(exp.charAt(i));
            }
        }
        exp = expression.toString();
        //System.out.println(exp);

        String result = new String("");

        Stack<String> stack = new Stack<>();

        for (int i = 0; i<exp.split(" ").length; ++i)
        {
            String c = exp.split(" ")[i];
            if(c.isEmpty())
                continue;

            if (c.matches("-?[\\d\\.]+")) {
                result += " ";
                result += c;
            }
            else if (c.equals("("))
                stack.push(c);
            else if (c.equals(")"))
            {
                while (!stack.isEmpty() &&
                        !stack.peek().equals("(")) {
                    result += " ";
                    result += stack.pop();
                }
                stack.pop();
            }
            else // an operator is encountered
            {
                while (!stack.isEmpty() && Prec(c)
                        <= Prec(stack.peek())){
                    result += " ";
                    result += stack.pop();
                }
                stack.push(c);
            }
        }
        while (!stack.isEmpty()){
            if(stack.peek().equals("("))
                return "Invalid Expression";
            result += " ";
            result += stack.pop();
        }
        //System.out.println(result);
        return result;
    }
}