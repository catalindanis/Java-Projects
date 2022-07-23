package search;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

abstract class Search{
    public Search(){}
    public Search(ArrayList<String> people,String pattern){
        getResult(people,pattern);
    }
    public void getResult(ArrayList<String> people,String pattern){
        search(people,pattern);
    }
    public abstract void search(ArrayList<String> people,String pattern);
}

class allSearch extends Search{
    public allSearch(ArrayList<String> people,String pattern){
        super(people,pattern);
    }
    public void search(ArrayList<String> people,String pattern){
        ArrayList<String> foundPeoples = new ArrayList<>();
        for(int i=0;i<people.size();i++){
            boolean found = true;
            String currentPeople = people.get(i);
            for(int k=0;k<pattern.split(" ").length;k++)
                if(!currentPeople.toLowerCase().contains(pattern.split(" ")[k].toLowerCase()))
                    found = false;
            if(found)
                foundPeoples.add(currentPeople);
        }
        if(foundPeoples.size() == 0)
            System.out.println("No matching people found");
        for(int i=0;i<foundPeoples.size();i++)
            System.out.println(foundPeoples.get(i));
    }
}
class anySearch extends Search{
    public anySearch(ArrayList<String> people,String pattern){
        super(people,pattern);
    }
    public void search(ArrayList<String> people,String pattern){
        ArrayList<String> foundPeoples = new ArrayList<>();
        for(int i=0;i<people.size();i++){
            boolean found = false;
            String currentPeople = people.get(i);
            for(int k=0;k<pattern.split(" ").length;k++)
                if(currentPeople.toLowerCase().contains(pattern.split(" ")[k].toLowerCase()))
                    found = true;
            if(found)
                foundPeoples.add(currentPeople);
        }
        if(foundPeoples.size() == 0)
            System.out.println("No matching people found");
        for(int i=0;i<foundPeoples.size();i++)
            System.out.println(foundPeoples.get(i));
    }
}
class noneSearch extends Search{
    public noneSearch(ArrayList<String> people,String pattern){
        super(people,pattern);
    }
    public void search(ArrayList<String> people,String pattern){
        ArrayList<String> foundPeoples = new ArrayList<>();
        for(int i=0;i<people.size();i++){
            boolean found = false;
            String currentPeople = people.get(i);
            for(int k=0;k<pattern.split(" ").length;k++)
                if(currentPeople.toLowerCase().contains(pattern.split(" ")[k].toLowerCase()))
                    found = true;
            if(!found)
                foundPeoples.add(currentPeople);
        }
        if(foundPeoples.size() == 0)
            System.out.println("No matching people found");
        for(int i=0;i<foundPeoples.size();i++)
            System.out.println(foundPeoples.get(i));
    }
}

public class Main {
    static Scanner scanner;
    static int nrOfPeople;
    static ArrayList<String> people = new ArrayList<>();
    static HashMap<String,ArrayList<Integer>> map = new HashMap<>();
    static boolean exitRequested = false;

    public static void main(String[] args) {
        init(args[1]);
        scanner.close();
        scanner = new Scanner(System.in);
        while(!exitRequested){
            printMenu();
            int command = Integer.valueOf(scanner.nextLine());
            switch (command){
                case 1:
                    search();
                    break;
                case 2:
                    printExistingPeople();
                    break;
                case 0:
                    exitRequested = true;
                    break;
                default:
                    System.out.println("Incorrect option! Try again.");
                    break;
            }
        }
        System.out.println("Bye!");
    }
    private static void search(){
        System.out.println("Select a matching strategy: ALL, ANY, NONE");
        String strategy = scanner.nextLine();
        System.out.println("Enter a name or email to search all suitable people.");
        String resource = scanner.nextLine();
        switch (strategy){
            case "ANY":
                Search search1 = new anySearch(people,resource);
                break;
            case "ALL":
                Search search2 = new allSearch(people,resource);
                break;
            case "NONE":
                Search search3 = new noneSearch(people,resource);
                break;
        }
    }
    private static void printExistingPeople(){
        System.out.println("=== List of people ===");
        for(int i=0;i<people.size();i++)
            System.out.println(people.get(i));
    }
    private static void printMenu(){
        System.out.println("=== Menu ===\n" +
                "1. Find a person\n" +
                "2. Print all people\n" +
                "0. Exit");
    }
    private static void init(String file){
        try {
            scanner = new Scanner(new File(file));
            while(scanner.hasNext()) {
                String data = scanner.nextLine();
                people.add(data);
                for(String word : data.split(" ")){
                    try{
                        map.get(word.toLowerCase()).add(people.size()-1);
                    }catch (Exception exception){
                        map.put(word.toLowerCase(),new ArrayList<>(Arrays.asList(people.size()-1)));
                    }
                }
            }
        }
        catch (FileNotFoundException fnfe){
            fnfe.printStackTrace();
        }
    }
}
