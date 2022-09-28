package maze;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.management.GarbageCollectorMXBean;
import java.util.*;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static Maze maze;
    static boolean mazeExists = false;
    public static void main(String[] args) {
        menu();
        System.out.println("Bye!");
    }
    private static void menu() {
        while(true) {
            displayMenu(mazeExists);
            String input = scanner.nextLine();
            switch (input){
                case "1":
                    generateNewMaze();
                    break;
                case "2":
                    loadMaze();
                    break;
                case "3":
                    saveMaze();
                    break;
                case "4":
                    displayMaze();
                    break;
                case "5":
                    solveMaze();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Incorrect option. Please try again");
                    break;
            }
            System.out.println();
        }
    }

    private static void solveMaze() {
        maze.solveMaze();
    }

    private static void displayMaze() {
        maze.displayMaze();
    }

    private static void saveMaze() {
        File file = new File(scanner.nextLine());
        try {
            maze.saveMaze(file);
        } catch (FileNotFoundException e) {
            System.out.println("The file ... does not exist");
        }
    }

    private static void loadMaze() {
        File file = new File(scanner.nextLine());
        try {
            maze = new Maze(file);
        } catch (FileNotFoundException e) {
            System.out.println("The file ... does not exist");
        }
        mazeExists = true;
    }

    private static void generateNewMaze(){
        System.out.println("Enter the size of a new maze");
        int size = Integer.valueOf(scanner.nextLine());
        maze = new Maze(size);
        maze.displayMaze();
        mazeExists = true;
    }
    private static void displayMenu(boolean mazeExists){
        System.out.println("=== Menu ===");
        System.out.println("1. Generate a new maze");
        System.out.println("2. Load a maze");
        if(mazeExists){
            System.out.println("3. Save the maze");
            System.out.println("4. Display the maze");
            System.out.println("5. Find the escape");
        }
        System.out.println("0. Exit");
    }
}

class Maze {
    private String table[][];
    private int adjacencyMatrix[][];
    private int size = 0;
    private final int dirX[] = {-1, 0, +1, 0};
    private final int dirY[] = {0, +1, 0, -1};
    private final String block = "\u2588\u2588";
    private final String empty = "  ";
    int startX = -1,startY = -1;
    int stopX = -1,stopY = -1;
    public Maze(int size) {
        this.size = size;
        generateMaze();
    }
    public Maze(File file) throws FileNotFoundException {
        loadMaze(file);
    }

    private void generateMaze() {
        Random random = new Random();
        table = new String[size][size];
        for(int i=0;i<size;i++)
            for(int j=0;j<size;j++)
                table[i][j] = block;
        int row = random.nextInt(size-2)+1;
        table[row][0] = empty;
        table[row][1] = empty;
        int front = 2;
        for(int j=2;j<size;) {
            if(front == 2){
                front = 0;
                j--;
                if(row == 1){
                    row ++;
                    table[row][j] = empty;
                }else if(row == size-2){
                    row --;
                    table[row][j] = empty;
                }else{
                    if(random.nextInt(2) == 1){
                        row ++;
                        table[row][j] = empty;
                    }
                    else{
                        row --;
                        table[row][j] = empty;
                    }
                }
            }
            else{
                front ++;
                table[row][j] = empty;
                j++;
            }
        }
        for(int j=1;j<size-1;j+=2){
            for(int i=1;!table[i][j].equals("  ");i++)
                table[i][j] = "  ";
            for(int i = size-2;!table[i][j].equals("  ");i--)
                table[i][j] = "  ";
        }
    }
    private void loadMaze(File file) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(file);
        String pattern = new String();
        while(fileScanner.hasNextLine())
            pattern += fileScanner.nextLine()+"\n";
        size = pattern.split("\n").length;
        table = new String[size][size];
        int index = 0;
        for(int i=0;i<size;i++) {
            for (int j = 0; j < size; j++,index++)
                if(pattern.charAt(index) == '1')
                    table[i][j] = block;
                else table[i][j] = empty;
            index++;
        }
        //displayMaze();
    }

    public void displayMaze(){
        for(int i=0;i<size;i++,System.out.println())
            for(int j=0;j<size;j++)
                System.out.print(table[i][j]);
    }
    public void saveMaze(File file) throws FileNotFoundException{
        PrintWriter printWriter = new PrintWriter(file);
        for(int i=0;i<size;i++,printWriter.write("\n"))
            for(int j=0;j<size;j++)
                if(table[i][j].equals(block))
                    printWriter.write('1');
                else printWriter.write('0');
        printWriter.close();
    }
    public void solveMaze(){
        for(int i=0;i<size;i++) {
            if (table[i][0].equals(empty)) {
                startX = i;
                startY = 0;
            }
            if(table[i][size-1].equals(empty))
                stopX = i;
            stopY = size-1;
        }
        find(startX,startY);
        displayMaze();
        for(int i=0;i<size;i++)
            for(int j=0;j<size;j++)
                if(table[i][j].equals("//"))
                    table[i][j] = empty;
    }
    private boolean find(int x,int y){
        table[x][y] = "//";
        if(x == stopX && y == stopY)
            return true;
        for(int k=0;k<4;k++){
            Position newPos = new Position(x + dirX[k],y + dirY[k]);
            if(inGraph(newPos) && notWall(newPos) && !table[newPos.x][newPos.y].equals("//")){
                if(find(newPos.x,newPos.y))
                    return true;
            }
        }
        table[x][y] = empty;
        return false;
    }
    private boolean inGraph(Position position){
        return position.x >= 0 && position.x < size && position.y >= 0 && position.y < size;
    }
    private boolean notWall(Position position){
        return !table[position.x][position.y].equals(block);
    }
}

class Position{
    public int x,y;
    public Position(int x,int y){
        this.x = x;
        this.y = y;
    }
}