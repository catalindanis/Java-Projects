import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String input = scanner.nextLine();
        switch (input.charAt(0)){
            case '<':
                Converter xmlToJSON = new XMLConverter(input);
                System.out.println(xmlToJSON.convert());
                break;
            case '{':
                Converter jsonToXML = new JSONConverter(input);
                System.out.println(jsonToXML.convert());
                break;
        }
    }

}
class JSONConverter extends Converter{
    JSONConverter(String input){
        super(input);
    }
    @Override
    public String convert() {
        StringBuilder conversion = new StringBuilder();
        conversion.append("<");
        conversion.append(getInput().split("\"")[1].split("\"")[0]);
        if(getInput().split(":")[1].matches("\\s?null\\s?[,}]?")){
            conversion.append("/");
        }
        else {
            conversion.append(">");
            conversion.append(getInput().split("\"")[3].split("\"")[0]);
            conversion.append("<");
            conversion.append("/"+getInput().split("\"")[1].split("\"")[0]);
        }
        conversion.append(">");
        return conversion.toString();
    }
}
class XMLConverter extends Converter{
    XMLConverter(String input){
        super(input);
    }
    @Override
    public String convert() {
        StringBuilder conversion = new StringBuilder();
        conversion.append("{");
        conversion.append(String.format("\"%s\"",getInput().split("<")[1].split("[>/]")[0]));
        conversion.append(':');
        try {
            conversion.append(String.format("\"%s\"", getInput().split("</?[a-zA-Z]+>")[1]));
        }catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException){
            conversion.append("null");
        }
        conversion.append("}");
        return conversion.toString();
    }
}
abstract class Converter{
    private String input;
    Converter(String input){
        this.input = input;
    }
    public abstract String convert();

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
