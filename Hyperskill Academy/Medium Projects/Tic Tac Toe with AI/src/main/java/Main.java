import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static String gameTable[][] = new String[3][3];
    static Scanner scanner = new Scanner(System.in);
    static PrintWriter fileWriter;
    static boolean firstMove = true;

    static {
        try {
            fileWriter = new PrintWriter(new File("D:\\log.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static String move = "X";
    public static void main(String[] args) {
        runGame();
    }
    private static void runGame(){
        initEmptyTable();
        boolean correctInput = false;
        String firstMove = new String(),secondMove = new String();
        while(!correctInput){
            System.out.println("Input command:");
            String input = scanner.nextLine();
            if(input.split(" ")[0].equals("exit"))
                return;
            else{
                try{
                    firstMove = input.split(" ")[1];
                    secondMove = input.split(" ")[2];
                    break;
                }catch (ArrayIndexOutOfBoundsException sioobe){
                    System.out.println("Bad parameters!");
                }
            }
        }
        displayTable();
        while(gameStatus().equals("Game not finished")){
            makeMove(firstMove);
            displayTable();
            if (gameStatus().equals("Game not finished")) {
                makeMove(secondMove);
                displayTable();
            }
        }
        System.out.println(gameStatus());
    }
    private static void makeMove(String turn){
        if(turn.equals("user")){
            askForMove();
        }
        else{
            makeMoveByAI(turn);
        }
    }
    private static void makeMoveByAI(String difficulty){
        switch (difficulty){
            case "easy": {
                System.out.println("Making move level \"easy\"");
                Random random = new Random();
                while (true) {
                    int x = random.nextInt(3);
                    int y = random.nextInt(3);
                    if (x >= 0 && x <= 2 && y >= 0 && y <= 2) {
                        if (gameTable[x][y].equals("_")) {
                            gameTable[x][y] = move;
                            changeMove();
                            return;
                        }
                    }
                }
            }
            case "medium":
            {
                System.out.println("Making move level \"medium\"");
                if (twoInARow(move)){
                    changeMove();
                    return;
                }
                else{
                    if(move.equals("X")){
                        if(twoInARow("O","X")) {
                            changeMove();
                            return;
                        }
                    }
                    else {
                        if(twoInARow("X","O")) {
                            changeMove();
                            return;
                        }
                    }
                    Random random = new Random();
                    while (true) {
                        int x = random.nextInt(3);
                        int y = random.nextInt(3);
                        if (x >= 0 && x <= 2 && y >= 0 && y <= 2) {
                            if (gameTable[x][y].equals("_")) {
                                gameTable[x][y] = move;
                                changeMove();
                                return;
                            }
                        }
                    }
                }
            }
            case "hard":
            {
                System.out.println("Making move level \"hard\"");
                if (twoInARow(move)){
                    changeMove();
                    return;
                }
                else {
                    if (move.equals("X")) {
                        if (twoInARow("O", "X")) {
                            changeMove();
                            return;
                        }
                    } else {
                        if (twoInARow("X", "O")) {
                            changeMove();
                            return;
                        }
                    }
                }
                if(firstMove){
                    if(gameTable[1][1].equals("_")) {
                        gameTable[1][1] = move;
                        changeMove();
                        firstMove = false;
                        return;
                    }
                }
                int max = -10000, mindepth = 100000;
                int imax = -1,jmax = -1;
                for(int i=0;i<3;i++)
                    for(int j=0;j<3;j++)
                        if(gameTable[i][j].equals("_")){
                            gameTable[i][j] = move;
                            String copyMove = move;
                            changeMove();
                            int value[] = minimax(0,i,j,true);
                            if(value[0] > max){
                                max = value[0];
                                imax = i;
                                jmax = j;
                            }
                            else if(value[0] == max){
                                if(value[1] < mindepth){
                                    mindepth = value[1];
                                    imax = i;
                                    jmax = j;
                                }
                            }
                            move = copyMove;
                            gameTable[i][j] = "_";
                            break;
                        }
                gameTable[imax][jmax] = move;
                changeMove();
                fileWriter.close();
                return;
            }

        }
    }
    private static int[] minimax(int depth,int x,int y,boolean opponentTurn){
        //logToFile(String.format("MINIMAX: depth = %d; x = %d; y = %d; opponentTurn = %b\n",depth,x,y,opponentTurn));
        int value[] = new int[]{0,0};
        if(gameStatus().equals(move+" wins")){
            value[0] = -1; value[1] = depth;
        }
        else if (gameStatus().contains("wins")){
            value[0] = 1;value[1] = depth;
        }
        else if (gameStatus().equals("Draw")){
            value[0] = 0; value[1] = depth;
        }
        else{
            if(opponentTurn){
                int min = 10000, maxdepth = -10000;
                int imax = -1,jmax = -1;
                for(int i=0;i<3;i++)
                    for(int j=0;j<3;j++)
                        if(gameTable[i][j].equals("_")){
                            gameTable[i][j] = move;
                            String copyMove = move;
                            changeMove();
                            int result[] = minimax(depth+1,i,j,false);
                            if(result[0] < min){
                                min = result[0];
                                imax = i;
                                jmax = j;
                            }
                            else if(result[0] == min){
                                if(result[1] > maxdepth){
                                    maxdepth = result[1];
                                    imax = i;
                                    jmax = j;
                                }
                            }
                            move = copyMove;
                            gameTable[i][j] = "_";
                        }
            }
            else{
                int max = -10000, mindepth = 100000;
                int imax = -1,jmax = -1;
                for(int i=0;i<3;i++)
                    for(int j=0;j<3;j++)
                        if(gameTable[i][j].equals("_")){
                            gameTable[i][j] = move;
                            String copyMove = move;
                            changeMove();
                            int result[] = minimax(depth+1,i,j,true);
                            if(result[0] > max){
                                max = result[0];
                                imax = i;
                                jmax = j;
                            }
                            else if(result[0] == max){
                                if(result[1] < mindepth){
                                    mindepth = result[1];
                                    imax = i;
                                    jmax = j;
                                }
                            }
                            move = copyMove;
                            gameTable[i][j] = "_";
                        }
            }
        }
        return value;
    }
    private static void logToFile(String string){
        fileWriter.write(string);
        for(int i=0;i<3;i++)
            fileWriter.append(gameTable[i][0]+" "+gameTable[i][1]+" "+gameTable[i][2]+"\n");
    }
    private static boolean twoInARow(String turn){
        for(int i=0;i<3;i++) {
            if (gameTable[i][0].equals(turn) && gameTable[i][1].equals(turn) && gameTable[i][2].equals("_")) {
                gameTable[i][2] = turn;
                return true;
            }
            if (gameTable[i][0].equals(turn) && gameTable[i][2].equals(turn) && gameTable[i][1].equals("_")){
                gameTable[i][1] = turn;
                return true;
            }
            if (gameTable[i][1].equals(turn) && gameTable[i][2].equals(turn) && gameTable[i][0].equals("_")){
                gameTable[i][0] = turn;
                return true;
            }
            if (gameTable[0][i].equals(turn) && gameTable[1][i].equals(turn) && gameTable[2][i].equals("_")) {
                gameTable[2][i] = turn;
                return true;
            }
            if (gameTable[0][i].equals(turn) && gameTable[2][i].equals(turn) && gameTable[1][i].equals("_")){
                gameTable[1][i] = turn;
                return true;
            }
            if (gameTable[1][i].equals(turn) && gameTable[2][i].equals(turn) && gameTable[0][i].equals("_")){
                gameTable[0][i] = turn;
                return true;
            }
        }
        if (gameTable[0][0].equals(turn) && gameTable[1][1].equals(turn) && gameTable[2][2].equals("_")) {
            gameTable[2][2] = turn;
            return true;
        }
        if (gameTable[0][0].equals(turn) && gameTable[2][2].equals(turn) && gameTable[1][1].equals("_")){
            gameTable[1][1] = turn;
            return true;
        }
        if (gameTable[1][1].equals(turn) && gameTable[2][2].equals(turn) && gameTable[0][0].equals("_")){
            gameTable[0][0] = turn;
            return true;
        }
        if (gameTable[0][2].equals(turn) && gameTable[1][1].equals(turn) && gameTable[2][0].equals("_")) {
            gameTable[2][0] = turn;
            return true;
        }
        if (gameTable[0][2].equals(turn) && gameTable[2][0].equals(turn) && gameTable[1][1].equals("_")){
            gameTable[1][1] = turn;
            return true;
        }
        if (gameTable[1][1].equals(turn) && gameTable[2][0].equals(turn) && gameTable[0][2].equals("_")){
            gameTable[0][2] = turn;
            return true;
        }
        return false;
    }
    private static boolean twoInARow(String opponentTurn,String myTurn){
        for(int i=0;i<3;i++) {
            if (gameTable[i][0].equals(opponentTurn) && gameTable[i][1].equals(opponentTurn) && gameTable[i][2].equals("_")) {
                gameTable[i][2] = myTurn;
                return true;
            }
            if (gameTable[i][0].equals(opponentTurn) && gameTable[i][2].equals(opponentTurn) && gameTable[i][1].equals("_")){
                gameTable[i][1] = myTurn;
                return true;
            }
            if (gameTable[i][1].equals(opponentTurn) && gameTable[i][2].equals(opponentTurn) && gameTable[i][0].equals("_")){
                gameTable[i][0] = myTurn;
                return true;
            }
            if (gameTable[0][i].equals(opponentTurn) && gameTable[1][i].equals(opponentTurn) && gameTable[2][i].equals("_")) {
                gameTable[2][i] = myTurn;
                return true;
            }
            if (gameTable[0][i].equals(opponentTurn) && gameTable[2][i].equals(opponentTurn) && gameTable[1][i].equals("_")){
                gameTable[1][i] = myTurn;
                return true;
            }
            if (gameTable[1][i].equals(opponentTurn) && gameTable[2][i].equals(opponentTurn) && gameTable[0][i].equals("_")){
                gameTable[0][i] = myTurn;
                return true;
            }
        }
        if (gameTable[0][0].equals(opponentTurn) && gameTable[1][1].equals(opponentTurn) && gameTable[2][2].equals("_")) {
            gameTable[2][2] = myTurn;
            return true;
        }
        if (gameTable[0][0].equals(opponentTurn) && gameTable[2][2].equals(opponentTurn) && gameTable[1][1].equals("_")){
            gameTable[1][1] = myTurn;
            return true;
        }
        if (gameTable[1][1].equals(opponentTurn) && gameTable[2][2].equals(opponentTurn) && gameTable[0][0].equals("_")){
            gameTable[0][0] = myTurn;
            return true;
        }
        if (gameTable[0][2].equals(opponentTurn) && gameTable[1][1].equals(opponentTurn) && gameTable[2][0].equals("_")) {
            gameTable[2][0] = myTurn;
            return true;
        }
        if (gameTable[0][2].equals(opponentTurn) && gameTable[2][0].equals(opponentTurn) && gameTable[1][1].equals("_")){
            gameTable[1][1] = myTurn;
            return true;
        }
        if (gameTable[1][1].equals(opponentTurn) && gameTable[2][0].equals(opponentTurn) && gameTable[0][2].equals("_")){
            gameTable[0][2] = myTurn;
            return true;
        }
        return false;
    }
    private static void askForMove(){
        while(true) {
            System.out.println("Enter the coordinates:");
            String input = scanner.nextLine();
            try {
                int x = Integer.valueOf(input.split(" ")[0]) - 1;
                int y = Integer.valueOf(input.split(" ")[1]) - 1;
                if (x >= 0 && x <= 2 && y >= 0 && y <= 2) {
                    if (gameTable[x][y].equals("_")) {
                        gameTable[x][y] = move;
                        changeMove();
                        return;
                    } else System.out.println("This cell is occupied! Choose another one!");
                } else {
                    System.out.println("Coordinates should be from 1 to 3!");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("You should enter numbers!");
            }
        }
    }
    private static String gameStatus(){
        for(int i=0;i<3;i++){
            if(gameTable[i][0].equals(gameTable[i][1]) && gameTable[i][0].equals(gameTable[i][2]) && !gameTable[i][0].equals("_"))
                return gameTable[i][0] + " wins";
            if(gameTable[0][i].equals(gameTable[1][i]) && gameTable[0][i].equals(gameTable[2][i]) && !gameTable[0][i].equals("_"))
                return gameTable[0][i] + " wins";
        }
        if(gameTable[0][0].equals(gameTable[1][1]) && gameTable[0][0].equals(gameTable[2][2]) && !gameTable[0][0].equals("_"))
            return gameTable[0][0] + " wins";
        if(gameTable[0][2].equals(gameTable[1][1]) && gameTable[0][2].equals(gameTable[2][0]) && !gameTable[0][2].equals("_"))
            return gameTable[0][2] + " wins";

        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                if(gameTable[i][j].equals("_"))
                    return "Game not finished";

        return "Draw";
    }
    private static void changeMove(){
        if(move.equals("X"))
            move = "O";
        else move  = "X";
    }
    private static void patternToTable(){
        System.out.println("Enter the cells:");
        String pattern = scanner.nextLine();
        int x = 0,o = 0;
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++) {
                gameTable[i][j] = String.valueOf(pattern.charAt(2 * i + j + i));
                if(pattern.charAt(2 * i + j + i) == 'X')
                    x++;
                else if(pattern.charAt(2 * i + j + i) == 'O')
                    o++;
            }
        if(x == o)
            move = "X";
        else move = "O";
    }

    private static void displayTable(){
        System.out.println("---------");
        for(int i=0;i<3;i++){
            System.out.print("| ");
            for(int j=0;j<3;j++)
                System.out.print(gameTable[i][j]+" ");
            System.out.println("|");
        }
        System.out.println("---------");
    }
    private static void initEmptyTable(){
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                gameTable[i][j] = "_";
    }
}