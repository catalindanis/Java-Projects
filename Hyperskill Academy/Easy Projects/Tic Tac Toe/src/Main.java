import java.util.Scanner;
public class Main {
    static Scanner scanner = new Scanner(System.in);
    static char point = 'X';
    static char values[][] = new char[3][3];
    static String pattern = new String();
    public static void main(String[] args) {
        drawPattern();
        boolean finished = false;
        while(!finished) {
            if (!getWinner().equals("X wins") && !getWinner().equals("O wins") && !getWinner().equals("Draw")) {
                move();
                drawPatternByValue();
            }
            else {
                System.out.println(getWinner());
                finished = true;
            }
        }
    }

    public static void move(){
        boolean moved = false;
        while(!moved) {
            System.out.println("Enter the coordinates: ");
            try {
                int x = Integer.parseInt(scanner.next());
                int y = Integer.parseInt(scanner.next());
                if (x <= 3 && x >= 1 && y <= 3 && y >= 1) {
                    if (values[x - 1][y - 1] == '_') {
                        values[x - 1][y - 1] = point;
                        moved = true;

                        if(point == 'X')
                            point = 'O';
                        else point = 'X';

                    } else System.out.println("This cell is occupied! Choose another one!");
                }
                else{
                    System.out.println("Coordinates should be from 1 to 3!");
                }
            }
            catch (Exception exception){
                System.out.println("You should enter numbers!");
            }
        }
    }
    public static void drawPattern(){
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                values[i][j] = '_';
        for(int i=0;i<9;i++){
            System.out.print("-");
        }
        System.out.println();
        for(int i=0;i<3;i++) {
            for (int j = 0; j < 5; j++) {
                if (j == 0 || j == 4)
                    System.out.print("| ");
                else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
        for(int i=0;i<9;i++){
            System.out.print("-");
        }
        System.out.println();
    }

    public static void drawPatternByValue(){
        for(int i=0;i<9;i++){
            System.out.print("-");
        }
        System.out.println();
        for(int i=0;i<3;i++) {
            for (int j = 0; j < 5; j++) {
                if (j == 0 || j == 4)
                    System.out.print("| ");
                else {
                    System.out.print(values[i][j-1]+" ");
                }
            }
            System.out.println();
        }
        for(int i=0;i<9;i++){
            System.out.print("-");
        }
        System.out.println();
    }

    private static void setValues(String pattern){
        for(int i=0;i<9;i++){
            if(i < 3){
                values[0][i] = pattern.charAt(i);
            }
            else if(i<6){
                values[1][i-3] = pattern.charAt(i);
            }
            else{
                values[2][i-6] = pattern.charAt(i);
            }
        }
    }

    public static String getWinner(){
        String winner = "null";
        for(int i=0;i<3;i++)
            if(values[i][0] == values[i][1] && values[i][1] == values[i][2])
                if(winner.equals("null"))
                    winner = String.valueOf(values[i][0])+" wins";
                else if(!winner.equals(String.valueOf(values[i][0])))
                    return "Impossible";
        for(int i=0;i<3;i++)
            if(values[0][i] == values[1][i] && values[1][i] == values[2][i])
                if(winner.equals("null"))
                    winner = String.valueOf(values[0][i])+" wins";
                else if(!winner.equals(String.valueOf(values[0][i])))
                    return "Impossible";
        if(values[0][0] == values[1][1] && values[1][1] == values[2][2])
            if(winner.equals("null"))
                winner = String.valueOf(values[0][0])+" wins";
            else if(!winner.equals(String.valueOf(values[0][0])))
                return "Impossible";
        if(values[0][2] == values[1][1] && values[1][1] == values[2][0])
            if(winner.equals("null"))
                winner = String.valueOf(values[0][2])+" wins";
            else if(!winner.equals(String.valueOf(values[0][2])))
                return "Impossible";
        if(!winner.equals("null"))
            return winner;
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                if(values[i][j] == '_')
                    return "Game not finished!";
        return "Draw";
    }
}
