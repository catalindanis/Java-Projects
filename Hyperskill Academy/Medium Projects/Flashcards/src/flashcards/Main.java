package flashcards;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
public class Main {
    static Map<String,String> cards = new LinkedHashMap<>();
    static Map<String,Integer> errors = new LinkedHashMap<>();
    static StringBuilder log = new StringBuilder();
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        if(args.length >= 2){

            for(int i=0;i<args.length;i+=2)
                if(args[i].equals("-import"))
                    importCards(new File(args[i+1]));
        }
        menu();
        if(args.length >= 2){
            for(int i=0;i<args.length;i+=2)
                if(args[i].equals("-export"))
                    exportCards(new File(args[i+1]));
        }
    }
    private static void menu(){
        log.append("\n");
        boolean exitRequested = false;
        while(!exitRequested) {
            System.out.println("Input the action (add, remove, import, " +
                    "export, ask, exit, log, hardest card, reset stats)");
            log.append("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats)\n");
            String action = scanner.nextLine();
            log.append(action+"\n");
            switch (action) {
                case "add":
                    addCard();
                    break;
                case "remove":
                    removeCard();
                    break;
                case "import":
                    importCards();
                    break;
                case "export":
                    exportCards();
                    break;
                case "ask":
                    ask();
                    break;
                case "log":
                    log();
                    break;
                case "hardest card":
                    hardest_card();
                    break;
                case "reset stats":
                    reset_stats();
                    break;
                case "exit":
                    exitRequested = true;
                    break;
            }
        }
        System.out.println("Bye bye!");
        log.append("Bye bye!");
    }

    private static void addCard(){
        System.out.println("The card:");
        String term = scanner.nextLine();
        log.append("The card:\n"+term+"\n");
        if(cards.containsKey(term)) {
            System.out.printf("The card \"%s\" already exists.\n",term);
            log.append("The card \""+term+"\" already exists.\n");
        }
        else {
            System.out.println("The definition of the card:");
            String definition = scanner.nextLine();
            log.append("The definition of the card:\n"+definition+"\n");
            if (cards.containsValue(definition)) {
                System.out.printf("The definition \"%s\" already exists.\n",definition);
                log.append("The definition \""+definition+"\" already exists.\n");
            }
            else {
                cards.put(term, definition);
                errors.put(term,0);
                System.out.printf("The pair (\"%s\":\"%s\") has been added.\n",term,definition);
                log.append("The pair (\""+term+"\":\""+definition+"\") has been added.\n");
            }
        }
    }

    private static void removeCard(){
        System.out.println("Which card?");
        String card = scanner.nextLine();
        log.append("Which card?\n"+card+"\n");
        if(cards.containsKey(card)) {
            cards.remove(card);
            errors.remove(card);
            System.out.println("The card has been removed.");
            log.append("The card has been removed.\n");
        }
        else{
            System.out.printf("Can't remove \"%s\": there is no such card.\n",card);
            log.append("Can't remove \""+card+"\": there is no such card.\n");
        }
    }

    private static void importCards(){
        System.out.println("File name:");
        log.append("File name:\n");
        File file = new File(scanner.nextLine());
        log.append(file.getName().toString()+"\n");
        try {
            Scanner fileReader = new Scanner(file);
            int counter = 0;
            while(fileReader.hasNext()){
                String line = fileReader.nextLine();
                cards.put(line.split(":")[0],line.split(":")[1]);
                errors.put(line.split(":")[0],Integer.valueOf(line.split(":")[2]));
                counter++;
            }
            fileReader.close();
            System.out.printf("%d cards have been loaded.\n",counter);
            log.append(counter+" cards have been loaded.\n");
        }
        catch (FileNotFoundException fnfe){
            System.out.println("File not found.");
            log.append("File not found.\n");
        }

    }

    private static void importCards(File file){
        try {
            Scanner fileReader = new Scanner(file);
            int counter = 0;
            while(fileReader.hasNext()){
                String line = fileReader.nextLine();
                cards.put(line.split(":")[0],line.split(":")[1]);
                errors.put(line.split(":")[0],Integer.valueOf(line.split(":")[2]));
                counter++;
            }
            fileReader.close();
            System.out.printf("%d cards have been loaded.\n",counter);
            log.append(counter+" cards have been loaded.\n");
        }
        catch (FileNotFoundException fnfe){
        }

    }

    private static void exportCards(){
        System.out.println("File name:");
        log.append("File name:\n");
        File file = new File(scanner.nextLine());
        log.append(file.getName().toString()+"\n");
        try{
            PrintWriter fileWriter = new PrintWriter(file);
            for(Map.Entry entry : cards.entrySet()){
                fileWriter.printf("%s:%s:%s\n",entry.getKey(),
                        entry.getValue(),errors.get(entry.getKey()));
            }
            fileWriter.close();
            System.out.printf("%d cards have been saved.\n",cards.size());
            log.append(cards.size()+" cards have been saved.\n");
        }
        catch(FileNotFoundException fnfe){}
    }

    private static void exportCards(File file){
        try{
            PrintWriter fileWriter = new PrintWriter(file);
            for(Map.Entry entry : cards.entrySet()){
                fileWriter.printf("%s:%s:%s\n",entry.getKey(),
                        entry.getValue(),errors.get(entry.getKey()));
            }
            fileWriter.close();
            System.out.printf("%d cards have been saved.\n",cards.size());
            log.append(cards.size()+" cards have been saved.\n");
        }
        catch(FileNotFoundException fnfe){}
    }

    private static void ask(){
        System.out.println("How many times to ask?");
        log.append("How many times to ask?\n");
        int numberOfTimes = Integer.valueOf(scanner.nextLine());
        log.append(numberOfTimes+"\n");
        for(Map.Entry entry : cards.entrySet()){
            if(numberOfTimes == 0)
                break;

            System.out.printf("Print the definition of \"%s\":\n",entry.getKey());
            String definition = scanner.nextLine();
            log.append("Print the definition of \""+entry.getKey()+"\":\n"+definition+"\n");
            if(definition.equals(entry.getValue())){
                System.out.println("Correct!");
                log.append("Correct!\n");
            }
            else {
                if (cards.containsValue(definition)) {
                    String term = new String();
                    for (String key : cards.keySet()) {
                        if (cards.get(key).equals(definition)) {
                            term = key;
                            break;
                        }
                    }
                    System.out.printf("Wrong. The right answer is \"%s\", " +
                                    "but your definition is correct for \"%s\".\n",
                            entry.getValue(), term);
                    log.append("Wrong. The right answer is \""+entry.getValue()+"\", but your definition is correct for \""+term+"\".\n");
                } else {
                    System.out.printf("Wrong. The right answer is \"%s\".\n", entry.getValue());
                    log.append("Wrong. The right answer is \""+entry.getValue()+"\".\n");
                }
                errors.put(entry.getKey().toString(),errors.get(entry.getKey())+1);
            }
            numberOfTimes--;
        }
    }

    private static void log(){
        System.out.println("File name:");
        log.append("File name:\n");
        File file = new File(scanner.nextLine());
        log.append(file.getName().toString()+"\n");
        try{
            PrintWriter fileWriter = new PrintWriter(file);
            log.append("The log has been saved.\n");
            fileWriter.write(log.toString());
            fileWriter.close();
            System.out.println("The log has been saved.");
        }
        catch (FileNotFoundException fnfe){}
    }

    private static void hardest_card(){
        int max = 0;
        for(Map.Entry entry : errors.entrySet()){
            if(Integer.valueOf(entry.getValue().toString())> max)
                max = Integer.valueOf(entry.getValue().toString());
        }
        if(max > 0) {
            ArrayList<String> error_terms = new ArrayList<>();
            for (Map.Entry entry : errors.entrySet()) {
                if (Integer.valueOf(entry.getValue().toString()) == max)
                    error_terms.add(entry.getKey().toString());
            }
            if(error_terms.size() > 1){
                System.out.print("The hardest cards are ");
                log.append("The hardest cards are ");
                for(int i=0;i<error_terms.size();i++){
                    System.out.printf("\"%s\"",error_terms.get(i));
                    log.append(error_terms.get(i));
                    if(i < error_terms.size()-1) {
                        System.out.print(", ");
                        log.append(", ");
                    }
                    else{
                        System.out.printf(". ");
                        log.append(". ");
                    }
                }
                System.out.printf("You have %d errors answering them\n",max);
                log.append("You have "+max+" errors answering them\n");
            }
            else{
                System.out.printf("The hardest card is \"%s\". You have %d errors answering it\n",error_terms.get(0),max);
                log.append("The hardest card is \""+error_terms.get(0)+"\". You have "+max+" errors answering it\n");
            }
        }
        else{
            System.out.println("There are no cards with errors.");
            log.append("There are no cards with errors.\n");
        }
    }

    private static void reset_stats(){
        for(Map.Entry entry : errors.entrySet()){
            errors.put(entry.getKey().toString(),0);
        }
        System.out.println("Card statistics have been reset.");
        log.append("Card statistics have been reset.\n");
    }
}