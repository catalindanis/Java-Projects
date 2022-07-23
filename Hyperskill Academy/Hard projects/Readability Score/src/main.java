import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class main {
    static Scanner fileReader,scanner;
    public static void main(String[] args) {
        StringBuilder sentence = new StringBuilder();
        try {
            fileReader = new Scanner(new File(args[0]));
            readSentence(sentence);
            fileReader.close();
        }
        catch(FileNotFoundException fnfe){}
        displayInfo(sentence);
    }

    private static void displayInfo(StringBuilder sentence){
        System.out.printf("Words: %d\n",getWordsNumber(sentence));
        System.out.printf("Sentences: %d\n",getSentencesNumber(sentence));
        System.out.printf("Characters: %d\n",getCharacterNumber(sentence));
        System.out.printf("Syllables: %d\n",getSyllablesNumber(sentence));
        System.out.printf("Polysyllables: %d\n",getPolysyllablesNumber(sentence));

        System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all):");
        scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        System.out.println();
        switch (command){
            case "ARI":
                System.out.printf("Automated Readability Index: %.2f (about %d-year-olds).",getAutomatedReadabilityIndex(sentence),getTheAge(getAutomatedReadabilityIndex(sentence),true));
                break;
            case "FK":
                System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d-year-olds).",getFleschKincaid(sentence),getTheAge(getFleschKincaid(sentence),true));
                break;
            case "SMOG":
                System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d-year-olds).",getSimpleMeasureOfGobbledygook(sentence),getTheAge(getSimpleMeasureOfGobbledygook(sentence),true));
                break;
            case "CL":
                System.out.printf("Coleman–Liau index: %.2f (about %d-year-olds).",getColemanLiauIndex(sentence),getTheAge(getColemanLiauIndex(sentence),true));
                break;
            case "all":
                all(sentence);
                break;
        }
        System.out.println();
        double ageAverage = getTheAge(getAutomatedReadabilityIndex(sentence),true) + getTheAge(getFleschKincaid(sentence),true) +
                getTheAge(getSimpleMeasureOfGobbledygook(sentence),true) + getTheAge(getColemanLiauIndex(sentence),true);
        System.out.printf("This text should be understood in average by %.2f-year-olds.",ageAverage*1.0/4);
    }

    private static void readSentence(StringBuilder sentence)throws FileNotFoundException{
        sentence.append(fileReader.nextLine());
    }

    private static int getSentencesNumber(StringBuilder sentence){
        int sentenceNumber = sentence.toString().split("[\\.!\\?:]").length;
        return sentenceNumber;
    }

    private static int getWordsNumber(StringBuilder sentence){
        int wordsNumber = 0;
        for(int i=0;i<sentence.toString().split("[\\.!\\?] ").length;i++) {
            wordsNumber += sentence.toString().split("[\\.!\\?] ")[i].split("\\s").length;
        }
        return wordsNumber;
    }

    private static int getCharacterNumber(StringBuilder sentence){
        int charNumber = 0;
        for(int i=0;i<sentence.length();i++)
            if(sentence.charAt(i) != ' ')
                charNumber++;
        return charNumber;
    }

    private static int getPolysyllablesNumber(StringBuilder sentence){
        int polysyllablesNumber = 0;
        for(int i=0;i<sentence.toString().split("[\\.!\\?] ").length;i++) {
            for(int j=0;j<sentence.toString().split("[\\.!\\?] ")[i].split("\\s").length;j++)
                if(getVowelNumber(sentence.toString().split("[\\.!\\?] ")[i].split("\\s")[j]) >= 3) {
                    polysyllablesNumber++;
                }
        }
        return polysyllablesNumber;
    }

    private static int getSyllablesNumber(StringBuilder sentence){
        int syllablesNumber = 0;
        for(int i=0;i<getWordsNumber(sentence);i++) {
            String word = sentence.toString().split("[\\s]")[i].toLowerCase();
            syllablesNumber += getVowelNumber(word);
        }
        return syllablesNumber;
    }

    private static int getVowelNumber(String word){
        int counter = 0;

        if(word.charAt(0) == 'a' || word.charAt(0) == 'e' || word.charAt(0) == 'i' || word.charAt(0) == 'o' || word.charAt(0) == 'u' || word.charAt(0) == 'y')
            counter++;
        boolean lastCharVowel = false;
        for(int i=1;i<word.length();i++) {
            if (word.charAt(i) == 'a' || word.charAt(i) == 'e' || word.charAt(i) == 'i' || word.charAt(i) == 'o' || word.charAt(i) == 'u' || word.charAt(i) == 'y') {
                if(!(word.charAt(i-1) == 'a' || word.charAt(i-1) == 'e' || word.charAt(i-1) == 'i' || word.charAt(i-1) == 'o' || word.charAt(i-1) == 'u' || word.charAt(i-1) == 'y'))
                    counter++;
            }
        }
        if(Character.isAlphabetic(word.charAt(word.length()-1))){
            if(word.charAt(word.length()-1) == 'e')
                counter--;
        }
        else if(word.charAt(word.length()-2) == 'e')
            counter--;

        if(counter == 0)
            counter = 1;

        return counter;
    }

    private static void getTheAge(double score){
        switch ((int) score){
            case 1:
                System.out.printf("This text should be understood in average by %d-%d-year-olds.",5,6);
                break;
            case 2:
                System.out.printf("This text should be understood in average by %d-%d-year-olds.",6,7);
                break;
            case 3:
                System.out.printf("This text should be understood in average by %d-%d-year-olds.",7,9);
                break;
            case 4:
                System.out.printf("This text should be understood in average by %d-%d-year-olds.",9,10);
                break;
            case 5:
                System.out.printf("This text should be understood in average by %d-%d-year-olds.",10,11);
                break;
            case 6:
                System.out.printf("This text should be understood in average by %d-%d-year-olds.",11,12);
                break;
            case 7:
                System.out.printf("This text should be understood in average by %d-%d-year-olds.",12,13);
                break;
            case 8:
                System.out.printf("This text should be understood in average by %d-%d-year-olds.",13,14);
                break;
            case 9:
                System.out.printf("This text should be understood in average by %d-%d-year-olds.",14,15);
                break;
            case 10:
                System.out.printf("This text should be understood in average by %d-%d-year-olds.",15,16);
                break;
            case 11:
                System.out.printf("This text should be understood in average by %d-%d-year-olds.",17,18);
                break;
            case 12:
                System.out.printf("This text should be understood in average by %d-%d-year-olds.",17,18);
                break;
            case 13:
                System.out.printf("This text should be understood in average by %d-%d-year-olds.",18,24);
                break;
            case 14:
                System.out.printf("This text should be understood in average by %d+-year-olds.",24);
                break;
        }
    }

    private static int getTheAge(double score,boolean bool){
        switch ((int) Math.round(score)){
            case 1:
                return 6;
            case 2:
                return 7;
            case 3:
                return 9;
            case 4:
                return 10;
            case 5:
                return 11;
            case 6:
                return 12;
            case 7:
                return 13;
            case 8:
                return 14;
            case 9:
                return 15;
            case 10:
                return 16;
            case 11:
                return 17;
            case 12:
                return 18;
            case 13:
                return 24;
            case 14:
                return 25;
        }
        return 25;
    }

    private static double getFleschKincaid(StringBuilder sentence){
        double score = (0.39 * (getWordsNumber(sentence)*1.0/getSentencesNumber(sentence))) + (11.8 * (getSyllablesNumber(sentence)*1.0/getWordsNumber(sentence))) - 15.59;
        return score;
    }

    private static double getAutomatedReadabilityIndex(StringBuilder sentence){
        double score = (4.71 * (getCharacterNumber(sentence)*1.0/getWordsNumber(sentence))) +(0.5*(getWordsNumber(sentence))*1.0/getSentencesNumber(sentence)) - 21.43;
        return score;
    }

    private static double getSimpleMeasureOfGobbledygook(StringBuilder sentence){
        double score = 1.043 * Math.sqrt(getPolysyllablesNumber(sentence) * 1.0 * (30/getSentencesNumber(sentence))) + 3.1291;
        return score;
    }

    private static double getColemanLiauIndex(StringBuilder sentence){
        double L = (getCharacterNumber(sentence)*1.0/getWordsNumber(sentence)) * 100;
        double S = (getSentencesNumber(sentence)*1.0/getWordsNumber(sentence)) * 100;
        double score = (0.0588 * L) - (0.296 * S) - 15.8;
        return score;
    }

    private static void all(StringBuilder sentence){
        System.out.printf("Automated Readability Index: %.2f (about %d-year-olds).\n",getAutomatedReadabilityIndex(sentence),getTheAge(getAutomatedReadabilityIndex(sentence),true));
        System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d-year-olds).\n",getFleschKincaid(sentence),getTheAge(getFleschKincaid(sentence),true));
        System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d-year-olds).\n",getSimpleMeasureOfGobbledygook(sentence),getTheAge(getSimpleMeasureOfGobbledygook(sentence),true));
        System.out.printf("Coleman–Liau index: %.2f (about %d-year-olds).\n",getColemanLiauIndex(sentence),getTheAge(getColemanLiauIndex(sentence),true));
    }
}
