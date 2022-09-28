import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static PrintWriter printWriter;
    static String inputFile = "",outputFile = "";
    static boolean inputProvided = false,outputProvided = false;

    public static void main(String[] args) {
        String sortingType = "";
        boolean dataTypeProvided = false;
        boolean sortingTypeProvided = false;
        String type = "";
        for (int i = 0; i < args.length; i++)
            if(args[i].equals("-dataType"))
                dataTypeProvided = true;
            else if (args[i].equals("-sortingType"))
                sortingTypeProvided = true;
            else if (args[i].equals("byCount") || args[i].equals("natural"))
                sortingType = args[i];
            else if (args[i].equals("line") || args[i].equals("long") || args[i].equals("word"))
                type = args[i];
            else if(args[i].equals("-inputFile"))
                inputFile = (args[i+1]);
            else if(args[i].equals("-outputFile"))
                outputFile = (args[i+1]);
            else{
                System.out.printf("\"%s\" is not a valid parameter. It will be skipped.",args[i]);
            }
        if(type.isEmpty() && dataTypeProvided){
            System.out.println("No data type defined!");
            return;
        }
        if(sortingTypeProvided && sortingType.isEmpty()){
            System.out.println("No sorting type defined!");
            return;
        }
        if(type.isEmpty())
            type = "word";
        if(sortingType.isEmpty()) {
            sortingType = "natural";
            sortingTypeProvided = true;
        }
        try {
            if(!inputFile.isEmpty())
                scanner = new Scanner(new File(inputFile));
        }catch (Exception fnfe){}
        try{
            if(!outputFile.isEmpty())
                printWriter = new PrintWriter(new File(outputFile));
        }catch (Exception ioe){}
        switch (type) {
            case "long":
                if (sortingTypeProvided)
                    processLong(true, sortingType);
                else processLong();
                break;
            case "word":
                if (sortingTypeProvided)
                    processWord(true, sortingType);
                else processWord();
                break;
            case "line":
                if (sortingTypeProvided)
                    processLine(true, sortingType);
                else processLine();
                break;
        }
        try{
            scanner.close();
            printWriter.close();
        }catch (Exception e){}
    }

    private static void processWord() {
        ArrayList<String> arrayList = new ArrayList<>();
        while (scanner.hasNext()) {
            arrayList.add(scanner.next());
        }
        String max = new String();
        for (int i = 0; i < arrayList.size(); i++)
            if (arrayList.get(i).length() > max.length())
                max = arrayList.get(i);
            else if (arrayList.get(i).length() == max.length())
                if (arrayList.get(i).compareTo(max) > 0)
                    max = arrayList.get(i);
        int numberOfTimes = 0;
        for (int i = 0; i < arrayList.size(); i++)
            if (arrayList.get(i).equals(max))
                numberOfTimes++;
        if(outputFile.isEmpty()) {
            System.out.printf("Total words: %d.\n", arrayList.size());
            System.out.printf("The longest word: %s", max);
            System.out.printf(" (%d time(s), %d%s).", numberOfTimes, numberOfTimes * 100 / arrayList.size(), "%");
        }
        else{
            printWriter.write(String.format("Total words: %d.\n", arrayList.size()));
            printWriter.write(String.format("The longest word: %s", max));
            printWriter.write(String.format(" (%d time(s), %d%s).", numberOfTimes, numberOfTimes * 100 / arrayList.size(), "%"));
        }
    }

    private static void processWord(boolean sort, String sortingType) {
        ArrayList<String> arrayList = new ArrayList<>();
        while (scanner.hasNext()) {
            arrayList.add(scanner.next());
        }
        String max = new String();
        for (int i = 0; i < arrayList.size(); i++)
            if (arrayList.get(i).length() > max.length())
                max = arrayList.get(i);
            else if (arrayList.get(i).length() == max.length())
                if (arrayList.get(i).compareTo(max) > 0)
                    max = arrayList.get(i);
        int numberOfTimes = 0;
        for (int i = 0; i < arrayList.size(); i++)
            if (arrayList.get(i).equals(max))
                numberOfTimes++;
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        arrayList.forEach(elem -> {
            integerArrayList.add(Integer.valueOf(elem));
        });
        if(outputFile.isEmpty())
            System.out.printf("Total words: %d.\n", arrayList.size());
        else printWriter.write(String.format("Total words: %d.\n", arrayList.size()));
        if(sortingType.equals("natural")) {
            arrayList.sort((a1, a2) -> {
                return a1.compareTo(a2);
            });
            if(outputFile.isEmpty())
                System.out.println("Sorted data: ");
            else printWriter.write("Sorted data: ");
            arrayList.forEach(line -> {
                if(outputFile.isEmpty())
                    System.out.println(line);
                else printWriter.write(line);
            });
        }else {
            arrayList.sort((a1, a2) -> {
                int c1 = 0, c2 = 0;
                for (int i = 0; i < arrayList.size(); i++)
                    if (arrayList.get(i).equals(a1))
                        c1++;
                    else if (arrayList.get(i).equals(a2))
                        c2++;
                if (c1 == c2){
                    return a1.compareTo(a2);
                }
                if (c1 < c2)
                    return -1;
                return 1;
            });
            HashMap<String,Boolean> alreadyPrinted = new HashMap<>();
            arrayList.forEach(a1 -> {
                if(alreadyPrinted.containsKey(a1))
                    return;
                int c1 = 0;
                alreadyPrinted.put(a1,true);
                for(int i=0;i<arrayList.size();i++)
                    if(arrayList.get(i).equals(a1))
                        c1++;
                if(outputFile.isEmpty())
                    System.out.printf("%s: %d time(s), %d%s\n",a1,c1,c1*100/arrayList.size(),"%");
                else printWriter.write(String.format("%s: %d time(s), %d%s\n",a1,c1,c1*100/arrayList.size(),"%"));
            });
        }
    }

    private static void processLine() {
        ArrayList<String> arrayList = new ArrayList<>();
        while (scanner.hasNextInt()) {
            arrayList.add(scanner.nextLine());
        }
        String max = new String();
        for (int i = 0; i < arrayList.size(); i++)
            if (arrayList.get(i).length() > max.length())
                max = arrayList.get(i);
            else if (arrayList.get(i).length() == max.length())
                if (arrayList.get(i).compareTo(max) > 0)
                    max = arrayList.get(i);
        int nrOfTimes = 0;
        for (int i = 0; i < arrayList.size(); i++)
            if (arrayList.get(i).equals(max))
                nrOfTimes++;
        System.out.printf("Total lines: %d.\n", arrayList.size());
        System.out.printf("The longest line:\n%s\n", max);
        System.out.printf("(%d time(s), %d%s).", nrOfTimes, nrOfTimes * 100 / arrayList.size(), "%");
    }

    private static void processLine(boolean sort, String sortingType) {
        ArrayList<String> arrayList = new ArrayList<>();
        while (scanner.hasNextInt()) {
            arrayList.add(scanner.nextLine());
        }
        String max = new String();
        for (int i = 0; i < arrayList.size(); i++)
            if (arrayList.get(i).length() > max.length())
                max = arrayList.get(i);
            else if (arrayList.get(i).length() == max.length())
                if (arrayList.get(i).compareTo(max) > 0)
                    max = arrayList.get(i);
        int nrOfTimes = 0;
        for (int i = 0; i < arrayList.size(); i++)
            if (arrayList.get(i).equals(max))
                nrOfTimes++;
        if(outputFile.isEmpty())
            System.out.printf("Total lines: %d.\n", arrayList.size());
        else printWriter.write(String.format("Total lines: %d.\n", arrayList.size()));
        if(sortingType.equals("natural")) {
            arrayList.sort((a1, a2) -> {
                return a1.compareTo(a2);
            });
            if(outputFile.isEmpty())
                System.out.println("Sorted data: ");
            else printWriter.write("Sorted data: ");
            arrayList.forEach(line -> {
                if(outputFile.isEmpty())
                    System.out.println(line);
                else printWriter.write(line);
            });
        }else {
            arrayList.sort((a1, a2) -> {
                int c1 = 0, c2 = 0;
                for (int i = 0; i < arrayList.size(); i++)
                    if (arrayList.get(i).equals(a1))
                        c1++;
                    else if (arrayList.get(i).equals(a2))
                        c2++;
                if (c1 == c2){
                    return a1.compareTo(a2);
                }
                if (c1 < c2)
                    return -1;
                return 1;
            });
            HashMap<String,Boolean> alreadyPrinted = new HashMap<>();
            arrayList.forEach(a1 -> {
                if(alreadyPrinted.containsKey(a1))
                    return;
                int c1 = 0;
                alreadyPrinted.put(a1,true);
                for(int i=0;i<arrayList.size();i++)
                    if(arrayList.get(i).equals(a1))
                        c1++;
                if(outputFile.isEmpty())
                    System.out.printf("%s: %d time(s), %d%s\n",a1,c1,c1*100/arrayList.size(),"%");
                else printWriter.write(String.format("%s: %d time(s), %d%s\n",a1,c1,c1*100/arrayList.size(),"%"));
            });
        }
    }

    private static void processLong() {
        ArrayList<Long> arrayList = new ArrayList<>();
        while (scanner.hasNext()) {
            String input = scanner.next();
            try {
                arrayList.add(Long.valueOf(input));
            }catch (NumberFormatException nfe){
                System.out.printf("\"%s\" is not a long. It will be skipped.",input);
            }
        }
        System.out.printf("Total numbers: %d\n", arrayList.size());
        long max = arrayList.stream().max(Long::compare).get(), numberOfTimes = 0;
        for (int i = 0; i < arrayList.size(); i++)
            if (arrayList.get(i) == max)
                numberOfTimes++;
        System.out.printf("The greatest number: %d (%d time(s), %d%s).", max, numberOfTimes, numberOfTimes * 100 / arrayList.size(), "%");
    }

    private static void processLong(boolean sort, String sortingType) {
        ArrayList<Long> arrayList = new ArrayList<>();
        while (scanner.hasNext()) {
            String input = scanner.next();
            try {
                arrayList.add(Long.valueOf(input));
            }catch (NumberFormatException nfe){
                System.out.printf("\"%s\" is not a long. It will be skipped.",input);
            }
        }
        if(outputFile.isEmpty())
            System.out.printf("Total numbers: %d\n", arrayList.size());
        else printWriter.write(String.format("Total numbers: %d\n", arrayList.size()));
        long max = arrayList.stream().max(Long::compare).get(), numberOfTimes = 0;
        for (int i = 0; i < arrayList.size(); i++)
            if (arrayList.get(i) == max)
                numberOfTimes++;
        if (sortingType.equals("natural")) {
            arrayList.sort((a1, a2) -> {
                if (a1 < a2)
                    return -1;
                if (a1 == a2)
                    return 0;
                return 1;
            });
            if(outputFile.isEmpty())
                System.out.print("Sorted data: ");
            else printWriter.write("Sorted data: ");
            arrayList.forEach(line -> {
                if(outputFile.isEmpty())
                    System.out.print(line+" ");
                else printWriter.write(String.valueOf(line));
            });
        } else {
            arrayList.sort((a1, a2) -> {
                int c1 = 0, c2 = 0;
                for (int i = 0; i < arrayList.size(); i++)
                    if (arrayList.get(i).equals(a1))
                        c1++;
                    else if (arrayList.get(i).equals(a2))
                        c2++;
                if (c1 == c2){
                    if(a1 == a2)
                        return 0;
                    if(a1 < a2)
                        return -1;
                    return 1;
                }
                if (c1 < c2)
                    return -1;
                return 1;
            });
            HashMap<Long,Boolean> alreadyPrinted = new HashMap<>();
            arrayList.forEach(a1 -> {
                if(alreadyPrinted.containsKey(a1))
                    return;
                int c1 = 0;
                alreadyPrinted.put(a1,true);
                for(int i=0;i<arrayList.size();i++)
                    if(arrayList.get(i).equals(a1))
                        c1++;
                if(outputFile.isEmpty())
                    System.out.printf("%s: %d time(s), %d%s\n",a1,c1,c1*100/arrayList.size(),"%");
                else printWriter.write(String.format("%s: %d time(s), %d%s\n",a1,c1,c1*100/arrayList.size(),"%"));
            });
        }

    }
}
