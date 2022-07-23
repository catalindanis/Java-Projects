import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            //Scanner directory = new Scanner(new File("directory.txt"));
            //Scanner find = new Scanner(new File("find.txt"));
            Scanner directory = new Scanner(new File("small_directory.txt"));
            Scanner find = new Scanner(new File("small_find.txt"));
            ArrayList<String> findList = new ArrayList<>();
            ArrayList<String> fullList = new ArrayList<>();
            while(find.hasNext()){
                findList.add(find.nextLine());
            }
            while(directory.hasNext()){
                fullList.add(directory.nextLine().split("\\d ")[1]);
            }
            System.out.println("Start searching...");
            long start = System.currentTimeMillis();
            int counter = 0;
            for(int i=0;i<fullList.size();i++)
                for(int j=0;j<findList.size();j++)
                    if(fullList.get(i).equals(findList.get(j))) {
                        counter++;
                        break;
                    }
            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            long minutes = timeElapsed/60000;
            long seconds = (timeElapsed-(minutes*60000))/1000;
            long ms = (timeElapsed-(seconds*1000));
            System.out.printf("Found %d / %d entries. Time taken: %d min. %d sec. %d ms.",counter,findList.size(),minutes,seconds,ms);
        }
        catch (FileNotFoundException fnfe){
            fnfe.printStackTrace();
        }
    }
}
