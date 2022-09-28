package converter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    static {
        try {
            scanner = new Scanner(new File("test.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String input = read();
        //System.out.println(input);
        switch (input.charAt(0)){
            case '<':
                Converter xmlToJSON = new XMLConverter(input);
                System.out.println(xmlToJSON.convert());
                break;
            case '{':
                Converter jsonToXML = new JSONConverter(input);
                //jsonToXML.convert();
                System.out.println(jsonToXML.convert());
                break;
        }
    }
    private static String read(){
        StringBuilder input = new StringBuilder();
        while(scanner.hasNext()){
            input.append(scanner.nextLine());
        }
        scanner.close();
        return input.toString().replaceAll(" {2,}","");
    }
}
class JSONConverter extends Converter{
    JSONConverter(String input){
        super(input);
    }
    @Override
    public String convert() {
        String conversion = new String();
        ArrayList<String> paths = new ArrayList<>();
        conversion = conversion(getInput()/*.replaceAll("\\s*\"[@#\\s]?\"\\s*:\\s*\"?[\\w@#\\s]*\"?\\s*,?", "")*/,paths);
        return conversion;
    }

    private String conversion(String sequence, ArrayList<String> paths) {
        System.out.println(sequence);
        //System.out.println(paths.toString());
        if(sequence.startsWith("{"))
            sequence = sequence.replaceFirst("\\{","");
        //sequence = sequence.replaceFirst("\\s*","");
        while(sequence.matches("\\},?.*")){
            //System.out.println(sequence);
            //System.out.println(paths.toString());
            sequence = sequence.replaceFirst("\\},?","");
            //if(paths.size() >= 1)
                paths.remove(paths.size()-1);
        }
        sequence = sequence.replaceAll("\\{\\s*\\}","\"\"");
        sequence = sequence.replaceAll("\"\"\\s*:\\s*\\{[,\\s\\w@#:\"\']*\\}{1,1},?","");
        sequence = sequence.replaceAll("\"[@#]?\":\\s*\"?[\\w]*\"?,?","");
        //sequence = sequence.replaceAll("\\s*\"[@\\s]?\"\\s*:\\s*\"?[\\w@#\\s]*\"?\\s*,?", "");
        try {
            StringBuilder conversion = new StringBuilder();
            conversion.append("Element:\n");
            conversion.append("path = ");
//            if(!paths.contains(sequence.split("\"")[1]))
                paths.add(sequence.split("\"")[1].replaceFirst("[@#]",""));
            for (int i = 0; i < paths.size(); i++) {
                conversion.append(paths.get(i));
                if (i < paths.size() - 1)
                    conversion.append(", ");
                else conversion.append('\n');
            //conversion.append("sequence = "+sequence);
            }
            boolean hasChild = hasChild(sequence);
            //  \{"\w+"\s*:\s*\{.*  - has child regex
            //   \{\s*"[@#\w]*"\s*:\s*"[\w\.-]*",?
            String data = new String();
            //System.out.println(hasChild);
            if(hasChild) {
                int open = 0;
                boolean firstOpen = false;
                StringBuilder currentSequence = new StringBuilder();
                for (char character : sequence.toCharArray()) {
                    currentSequence.append(character);
                    if (character == '{') {
                        open++;
                        firstOpen = true;
                    }
                    if (character == '}')
                        open--;
                    if (open == 0 && firstOpen)
                        break;
                }
                currentSequence = new StringBuilder(currentSequence
                        .toString().replaceFirst(String.format("\"%s\"", sequence.split("\"")[1]), "")
                        .replaceFirst("\\s*:\\s*", ""));
                String[] childs = currentSequence.toString().substring(1,currentSequence.length()-1).split(",");
                //System.out.println(Arrays.toString(childs));
//                System.out.println(currentSequence.toString());
                if (currentSequence.toString().matches("(\\{?\"[@#\\w\\.']*\":\\s*\"[@#\\w\\.']*\",)*\\{?\"[@#\\w\\.']*\"\\s*:\\s*\\{\\s*\"['\\w\\.@#]*\"\\}?.*")) {
                    data = conversion(sequence.replaceFirst(String.format("\"%s\"", sequence.split("\"")[1]), "")
                            .replaceFirst("\\s*:\\s*", ""), paths);
                } else {
                    for(int i=0;i<childs.length;i++){
                        if(childs[i].split(":")[0].matches("\"[\\w\\s]+\"")){
                            for(int j=0;j<childs.length;j++)
                                if(i==j)
                                    continue;
                                else{
                                    if(childs[j].split(":")[0]
                                            .matches("\"@"+childs[i].split(":")[0].replaceAll("\"","")+"\"") ||
                                            childs[j].split(":")[0]
                                                    .matches("\"#"+childs[i].split(":")[0].replaceAll("\"","")+"\"")
                                    ){
                                        //System.out.println(currentSequence);
                                        //System.out.println(childs[j]);

                                        sequence = sequence.replaceFirst(childs[j]+",?","");
                                        currentSequence = new StringBuilder(currentSequence.toString().replaceFirst(childs[j]+",?",""));

                                        //System.out.println(sequence);
                                    }
                                }
                        }
                    }
                    String fatherName = sequence.split(":")[0].replaceAll("\"", "");
                    //currentSequence = new StringBuilder(currentSequence
                            //.toString().replaceAll("\\s*\"[@\\s]?\"\\s*:\\s*\"?[\\w@#\\s]*\"?\\s*,?", ""));
                    //System.out.println(currentSequence.toString());
                    if (currentSequence.toString().matches(String.format(".*#%s.*", fatherName))) {
                        //System.out.println(fatherName);
                                if (currentSequence.toString().split("#").length > 2 ||
                                    currentSequence.toString().matches(".*\"[\\w\\s]*\":.*")) {
                                    data = conversion(sequence.replaceFirst(String.format("\"%s\"", sequence.split("\"")[1]), "")
                                            .replaceFirst("\\s*:\\s*", ""), paths);
                                } else {
                                    //System.out.println(sequence);
                                    try {
                                        //System.out.println(Arrays.toString(childs));
                                        //System.out.println(currentSequence);
                                        conversion.append("value = ");
                                        for(int i=0;i<childs.length;i++) {
                                            //System.out.println(childs[i]);
                                            if(childs[i].matches(String.format(".*#%s.*", fatherName))) {
                                                if (childs[i].split(":")[1].matches("\\s*null?\"?,?\\s*"))
                                                    conversion.append("null\n");
                                                else
                                                    conversion.append(String.format("\"%s\"\n", childs[i].split(":")[1]
                                                            .replaceAll("\"","").replaceAll("\\s{1,}","")));
                                                break;
                                            }
                                        }
                                        boolean hasAttr = false;
                                        for(int i=0;i<childs.length;i++) {
                                            if(childs[i].matches(".*@[\\w\\s]+.*")){
                                                if(!hasAttr) {
                                                    conversion.append("attributes:\n");
                                                    hasAttr = true;
                                                }
                                                conversion.append(childs[i].split(":")[0].replaceAll("\"","").replaceFirst("@","") + " = ");
                                                if (childs[i].split(":")[1].matches("\\s*null\\s*"))
                                                    conversion.append("\"\"\n");
                                                else
                                                    conversion.append(String.format("\"%s\"\n", childs[i].split(":")[1]
                                                            .replaceAll("\"","").replaceAll("\\s{1,}","")));
                                            }
                                        }
                                    }catch (Exception exception){}
//                                    System.out.println(sequence);
//                                    System.out.println(sequence.replaceFirst(String.format("\"%s\": ",fatherName),"")
//                                            .replaceFirst(String.format("\\{%s",currentSequence.substring(1,currentSequence.length()-1)),""));
                                    data = conversion(sequence.replaceFirst(String.format("\"%s\": ",fatherName),"")
                                            .replaceFirst(String.format("\\{%s",currentSequence.substring(1,currentSequence.length()-1)),""),paths);
                                }
                            } else {
                                data = conversion(sequence.replaceFirst(String.format("\"%s\"", sequence.split("\"")[1]), "")
                                        .replaceFirst("\\s*:\\s*", ""), paths);
                            }
                    }
            }
            else{
                String currentElement = sequence.split("[,}]+")[0];
                //System.out.println(currentElement);
                try {
                    conversion.append("value = ");
                    if (currentElement.split(":")[1].matches("\\s*null[,\\s]*?"))
                        conversion.append("null\n");
                    else if(currentElement.split(":")[1].matches("\\s*\\{[,\\s]*?"))
                        conversion.append("\"\"\n");
                    else conversion.append(String.format("\"%s\"\n", currentElement.split(":")[1].replaceAll("\"","")
                            .replaceFirst("\\s{1,}","")));
                }catch (Exception exception){}

//                if(sequence.split("<")[1].split(">")[0].matches(".*=.*")) {
//                    conversion.append("attributes:\n");
//                    String attributes[] = sequence.split("<")[1].split(" ",2)[1].split("[/>]")[0]
//                            .replaceAll("\\s+=\\s+","=").split(" ");
//                    for(int i=0;i<attributes.length;i++) {
//                        conversion.append(String.format("%s = %s\n",
//                                attributes[i].split("=")[0],attributes[i].split("=")[1]));
//                    }
//                }

                paths.remove(currentElement.split("\"")[1].replaceFirst("[@#]",""));
                sequence = sequence.replaceAll("[a-zA-z]}","\"}");
                data = conversion(sequence.replaceFirst("\\s*\"[@#\\w'\\.\\s]*\":\\s*\"?['\\.\\{\\s\\w@\\-#)]*\\}?\"?[\\s,]*",""),paths);
                paths.add(currentElement.split("\"")[1].replaceFirst("[@#]",""));

                //conversion.append("\n");
            }
            conversion.append("\n");
            return conversion.toString() + data;
        }
        catch (Exception exception) {
            //exception.printStackTrace();
            return "";
        }
    }
    private boolean hasChild(String sequence) {
        return sequence.matches("\\{?\"[@#\\w\\.']*\"\\s*:\\s*\\{\\s*\"['\\w\\.@#]*\"\\}?.*");
    }
}
class XMLConverter extends Converter{
    private Pattern openTag = Pattern.compile("<\\w+");
    private Pattern closeTag = Pattern.compile("</\\w+");
    XMLConverter(String input){
        super(input);
    }
    @Override
    public String convert() {
        String conversion = new String();
        ArrayList<String> paths = new ArrayList<>();
        conversion = conversion(getInput(),paths);
        return conversion;
    }

    private String conversion(String sequence,ArrayList<String> paths){
        //System.out.println(sequence);
        if(sequence.matches("</\\w+>"))
            return "";
        try {
            StringBuilder conversion = new StringBuilder();
            conversion.append("Element:\n");
            conversion.append("path = ");
            if(!paths.contains(sequence.split("<")[1].split("[>/\\s]")[0]))
                paths.add(sequence.split("<")[1].split("[>/\\s]")[0]);
            for (int i = 0; i < paths.size(); i++) {
                conversion.append(paths.get(i));
                if (i < paths.size() - 1)
                    conversion.append(", ");
                else conversion.append('\n');
            }
            paths.remove(sequence.split("<")[1].split("[>/\\s]")[0]);
            boolean hasChild = true;
            if(sequence.matches("(<[\\w=\\s\"]+?>[\\s\\w\\+\\._@-]*</\\w+>.*)|(<[\\w=\\s\"]+?/>.*)")){
                hasChild = false;
            }
            String data = new String();
            if(hasChild){
                paths.add(sequence.split("<")[1].split("[>/\\s]")[0]);
                data = conversion(sequence.replaceFirst("<"+sequence.split("<")[1].split("[>/]")[0]+">","")
                        .replaceFirst("</"+sequence.split("<")[1].split("[>/\\s]")[0]+">",""),paths);
                //System.out.println(fromChilds);
            }
            else{
                if(!sequence.replaceFirst("(<[\\w=\\s\"]+?>[\\s-_@\\.\\w]*</\\w+>)|(<[\\w=\\s\"]+?/>)","").isEmpty())
                    data = conversion(sequence.replaceFirst("(<[\\w=\\s\"]+?>[\\s-_@\\.\\w]*</\\w+>)|(<[\\w=\\s\"]+?/>)",""),paths);
                //else //System.out.println("gol");
                try {
                    conversion.append("value = ");
                    if (sequence.split("<")[1].split(">")[0].contains("/"))
                        conversion.append(String.format("\"%s\"\n", "null"));
                    else conversion.append(String.format("\"%s\"\n", sequence.split(">")[1].split("<")[0]));
                }catch (Exception exception){}
            }

            if(sequence.split("<")[1].split(">")[0].matches(".*=.*")) {
                conversion.append("attributes:\n");
                String attributes[] = sequence.split("<")[1].split(" ",2)[1].split("[/>]")[0]
                        .replaceAll("\\s+=\\s+","=").split(" ");
                for(int i=0;i<attributes.length;i++) {
                    conversion.append(String.format("%s = %s\n",
                            attributes[i].split("=")[0],attributes[i].split("=")[1]));
                }
            }
            conversion.append("\n");
            return conversion.toString() + data;
        }
        catch (Exception exception) {
            //exception.printStackTrace();
            return "";
        }
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
