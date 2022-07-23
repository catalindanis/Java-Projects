import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input the file path:");
        String input = scanner.nextLine();
        try {
            Scanner fileReader = new Scanner(new File(input));
            ArrayList<String> lines = new ArrayList<>();
            int max = 0;
            while (fileReader.hasNext()) {
                lines.add(fileReader.nextLine());
                if(lines.get(lines.size()-1).length() > max)
                    max = lines.get(lines.size()-1).length();
            }
            for(int i=0;i<lines.size();i++){
                for(int j=0;j<max;j++){
                    if(j < lines.get(i).length())
                        System.out.print(lines.get(i).charAt(j));
                    else System.out.print(" ");
                }
                System.out.print(" | ");
                for(int j=max-1;j>=0;j--){
                    if(j < lines.get(i).length()) {
                        if(lines.get(i).charAt(j) == '<')
                            System.out.print(">");
                        else if(lines.get(i).charAt(j) == '>')
                            System.out.print("<");
                        else if(lines.get(i).charAt(j) == ')')
                            System.out.print("(");
                        else if(lines.get(i).charAt(j) == '(')
                            System.out.print(")");
                        else if(lines.get(i).charAt(j) == '{')
                            System.out.print("}");
                        else if(lines.get(i).charAt(j) == '}')
                            System.out.print("{");
                        else if(lines.get(i).charAt(j) == '\\')
                            System.out.print("/");
                        else if(lines.get(i).charAt(j) == '/')
                            System.out.print("\\");
                        else if(lines.get(i).charAt(j) == '[')
                            System.out.print("]");
                        else if(lines.get(i).charAt(j) == ']')
                            System.out.print("[");
                        else System.out.print(lines.get(i).charAt(j));
                    }
                    else System.out.print(" ");
                }
                System.out.println();
            }
        }catch (FileNotFoundException fnfe){
            System.out.println("File not found!");
        }
    }
}