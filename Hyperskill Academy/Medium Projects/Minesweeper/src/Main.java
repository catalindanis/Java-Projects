import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
public class Main {
    static Random random = new Random();
    static String[][] viewTable = new String[9][9];
    static String[][] valueTable = new String[9][9];
    static int[][] globalViewedCells = new int[9][9];
    static int[][] viewedCell = new int[9][9];
    static ArrayList<Position> MinePosition = new ArrayList<>();
    static ArrayList<Position> MarkedCells = new ArrayList<>();
    static boolean gameOver = false;
    static boolean firstMove = false;
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        play();
    }

    private static void play(){
        System.out.println("How many mines do you want on the field?");
        int nrOfMines = scanner.nextInt();
        scanner.nextLine();
        generateTable(nrOfMines);
        while(!gameOver){
            displayTable();
            verifyWin();
            if(!gameOver)
                makeMove();
        }
    }

    private static void makeMove(){
        System.out.println("Set/unset mine marks or claim a cell as free:");
        String input = scanner.nextLine();
        Position position = new Position(Integer.valueOf(input.split(" ")[1])
                ,Integer.valueOf(input.split(" ")[0]));
        switch(input.split(" ")[2]){
            case "free":
                free(position);
                break;
            case "mine":
                mine(position);
                break;
        }
    }

    private static void free(Position initialPos){
        initialPos.x--;
        initialPos.y--;
        resetViewedCells();
        int cx[] = {-1,-1,-1,0,0,+1,+1,+1};
        int cy[] = {-1,0,+1,-1,+1,-1,0,+1};
        ArrayDeque<Position> stack = new ArrayDeque<>();
        stack.addLast(initialPos);
        while(stack.size() > 0){
            Position position = new Position(stack.peek().x,stack.peek().y);
            if(firstMove){
                if(valueTable[position.x][position.y].equals("X")){
                    removeMine(position);
                    Position newPosition = new Position(random.nextInt(),random.nextInt());
                    while(valueTable[newPosition.x][newPosition.y].equals("X")) {
                        newPosition.x = random.nextInt(9);
                        newPosition.y = random.nextInt(9);
                    }
                    addMine(newPosition);
                }
                firstMove = false;
            }
            else if (valueTable[position.x][position.y].equals("X")){
                displayTable(true);
                System.out.println("You stepped on a mine and failed!");
                gameOver = true;
            }
            else{
                if(valueTable[position.x][position.y].equals("/")){
                    for(int i=0;i<8;i++){
                        Position neighbourPosition =
                                new Position(position.x+cx[i], position.y+cy[i]);
                        if(inTable(neighbourPosition.x, neighbourPosition.y)
                                && viewedCell[neighbourPosition.x][neighbourPosition.y] == 0){
                            viewedCell[neighbourPosition.x][neighbourPosition.y] = 1;
                            stack.add(neighbourPosition);
                        }
                    }
                }
                viewTable[position.x][position.y] = valueTable[position.x][position.y];
                viewedCell[position.x][position.y] = 1;
                globalViewedCells[position.x][position.y] = 1;
            }
            stack.remove();
        }
    }

    private static void resetViewedCells(){
        for(int i=0;i<9;i++)
            for(int j=0;j<9;j++)
                viewedCell[i][j] =0;
    }

    private static void mine(Position position){
        position.x--;
        position.y--;
        if(viewTable[position.x][position.y].equals("*")){
            for(int i=0;i<MarkedCells.size();i++)
                if(MarkedCells.get(i).x == position.x && MarkedCells.get(i).y == position.y){
                    MarkedCells.remove(i);
                    break;
                }
            if(globalViewedCells[position.x][position.y] == 0)
                viewTable[position.x][position.y] = ".";
            else viewTable[position.x][position.y] = valueTable[position.x][position.y];
        }
        else{
            MarkedCells.add(position);
            viewTable[position.x][position.y] = "*";
        }
    }

    private static void removeMine(Position position){
        for(int i=0;i<MinePosition.size();i++){
            if(MinePosition.get(i).x == position.x && MinePosition.get(i).y == position.y) {
                resetNeigbhours(position);
                MinePosition.remove(i);
                break;
            }
        }
    }

    private static void resetNeigbhours(Position minePosition){
        int cx[] = {-1,-1,-1,0,0,+1,+1,+1};
        int cy[] = {-1,0,+1,-1,+1,-1,0,+1};
        valueTable[minePosition.x][minePosition.y] = "/";
        for(int i=0;i<8;i++){
            Position position = new Position(minePosition.x + cx[i], minePosition.y + cy[i]);
            if(inTable(position.x, position.y)){
                if(valueTable[position.x][position.y].equals("X")) {
                    if(valueTable[minePosition.x][minePosition.y].equals("/"))
                        valueTable[minePosition.x][minePosition.y] = "1";
                    else valueTable[minePosition.x][minePosition.y] = String.valueOf(
                            Integer.valueOf(valueTable[minePosition.x][minePosition.y])+1);
                }
                else {
                    if (valueTable[position.x][position.y].equals("1"))
                        valueTable[position.x][position.y] = "/";
                    else valueTable[position.x][position.y] = String.valueOf(
                            Integer.valueOf(valueTable[position.x][position.y]) - 1);
                }
            }
        }
    }

    private static void addMine(Position position){
        valueTable[position.x][position.y] = "X";
        markNeighbours(position.x, position.y);
    }

    private static void generateTable(int nrOfMines){
        for(int i=0;i<9;i++)
            for(int j=0;j<9;j++) {
                valueTable[i][j] = "/";
                viewTable[i][j] = ".";
                globalViewedCells[i][j] = 0;
            }
        while(nrOfMines > 0){
            int x = random.nextInt(9);
            int y = random.nextInt(9);
            while(!valueTable[x][y].equals("/")){
                x = random.nextInt(9);
                y = random.nextInt(9);
            }
            MinePosition.add(new Position(x,y));
            valueTable[x][y] = "X";
            nrOfMines--;
            //System.out.println(nrOfMines);
        }
        for(int i=0;i<9;i++)
            for(int j=0;j<9;j++)
                if(valueTable[i][j].equals("X"))
                    markNeighbours(i,j);
    }

    private static void markNeighbours(int x,int y){
        int coordsHeight[] = {-1,-1,-1,0,0,+1,+1,+1};
        int coordsWidth[] = {-1,0,+1,-1,+1,-1,0,+1};
        for(int i=0;i<8;i++) {
            int newx = x + coordsHeight[i];
            int newy = y + coordsWidth[i];
            if (inTable(newx,newy)) {
                if(!valueTable[newx][newy].equals("X")) {
                    if (valueTable[newx][newy].equals("/"))
                        valueTable[newx][newy] = "1";
                    else{
                        valueTable[newx][newy] = String.valueOf(Integer.valueOf(valueTable[newx][newy]) + 1);
                    }
                }
            }
        }
    }

    private static void verifyWin(){
        boolean case1 = true,case2 = true;
        for(int i=0;i<MinePosition.size() && case1;i++) {
            boolean found = false;
            for (int j = 0; j < MarkedCells.size(); j++)
                if (MarkedCells.get(j).x == MinePosition.get(i).x &&
                        MarkedCells.get(j).y == MinePosition.get(i).y)
                    found = true;
            if(!found)
                case1 = false;
        }


        for(int i=0;i<9 && case2 ;i++)
            for(int j=0;j<9 && case2;j++){
                if(viewTable[i][j].equals(".")){
                    boolean found = false;
                    for(int k=0;k<MinePosition.size() && !found;k++){
                        if(MinePosition.get(k).x == i && MinePosition.get(k).y == j)
                            found = true;
                    }
                    if(!found)
                        case2 = false;
                }
            }
        if(case1 || case2){
            System.out.println("Congratulations! You found all the mines!");
            gameOver = true;
        }
    }

    private static boolean inTable(int x,int y){
        return x >= 0 && x < 9 && y >= 0 && y < 9;
    }
    public static void displayTable(){
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        for(int i=0;i<9;i++) {
            System.out.print((i+1)+"|");
            for (int j = 0; j < 9; j++) {
                System.out.print(viewTable[i][j]);
            }
            System.out.println("|");
        }
        System.out.println("-|---------|");
    }

    public static void displayTable(boolean gameOver){
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        for(int i=0;i<9;i++) {
            System.out.print((i+1)+"|");
            for (int j = 0; j < 9; j++) {
                if(valueTable[i][j].equals("X"))
                    System.out.print("X");
                else System.out.print(viewTable[i][j]);
            }
            System.out.println("|");
        }
        System.out.println("-|---------|");
    }
}

class Position{
    public int x;
    public int y;
    Position(int x,int y){
        this.x = x;
        this.y = y;
    }
}
