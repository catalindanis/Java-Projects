import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Launcher{
    public static void main(String[] args) {
        ConnectFour connectFour = new ConnectFour();
    }
}

public class ConnectFour extends JFrame {
    private static String currentTurn = "X";
    private static boolean turnDisabled = false;
    private static Cell[][] cell = new Cell[6][7];
    public ConnectFour() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());
        init();
        setVisible(true);
        setTitle("Connect 4");
    }

    private void init() {
        JPanel cellPanel = new JPanel();
        cellPanel.setLayout(new GridLayout(6, 7));

        for(int i=5;i>=0;i--)
            for(char c = 'A';c<='G';c++) {
                cell[i][c - 'A'] = new Cell("Button" + c + (i+1));
                cellPanel.add(cell[i][c-'A']);
            }

        add(cellPanel,BorderLayout.CENTER);

        JPanel resetPanel = new JPanel();
        resetPanel.setLayout(new BorderLayout());

        JButton resetButton = new JButton("Reset");
        resetButton.setName("ButtonReset");
        resetButton.setBounds(570,0,30,10);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i=0;i<6;i++)
                    for(int j=0;j<7;j++) {
                        cell[i][j].setText(" ");
                        cell[i][j].setBackground(Color.LIGHT_GRAY);
                        turnDisabled = false;
                    }
                currentTurn = "X";
            }
        });

        resetPanel.add(resetButton,BorderLayout.EAST);

        add(resetPanel,BorderLayout.SOUTH);
    }

    private static void changeTurn(){
        currentTurn = currentTurn == "X" ? "O" : "X";
    }

    public static void makeTurn(String name){
        if(turnDisabled)
            return;
        int row = name.charAt(7) - '1';
        int column = name.charAt(6) - 'A';
        for(int i = 0;i < 6; i++)
            if(cell[i][column].getText().equals(" ")){
                cell[i][column].setText(currentTurn);
                changeTurn();
                if(winnerFound()){
                    turnDisabled = true;
                }
                break;
            }
    }

    private static boolean winnerFound(){

        for(int i=0;i<6;i++)
            for(int j=1;j<5;j++){
                if(!cell[i][j-1].getText().equals(" ") && lineEqual(i,j)){
                    markWinnerCells(i,j-1,i,j,i,j+1,i,j+2);
                    return true;
                }
            }

        for(int j=0;j<7;j++)
            for(int i=1;i<4;i++)
                if(!cell[i-1][j].getText().equals(" ") && columnEqual(i,j)){
                    markWinnerCells(i-1,j,i,j,i+1,j,i+2,j);
                    return true;
                }

        for (int i=3; i<6; i++)
            for (int j=0; j<7-3; j++)
                if (!cell[i][j].getText().equals(" ") && diagonalEqualAscending(i,j)) {
                    markWinnerCells(i,j,i-1,j+1,i-2,j+2,i-3,j+3);
                    //System.out.println("ascending");
                    return true;
                }


        for (int i=3; i<6; i++)
            for (int j=3; j<7; j++)
                if (!cell[i][j].getText().equals(" ") && diagonalEqualDescending(i,j)) {
                    //System.out.println("descending");
                    markWinnerCells(i, j, i - 1, j - 1, i - 2, j - 2, i - 3, j - 3);
                    return true;
                }

        return false;
    }

    private static boolean diagonalEqualDescending(int row,int column){
        return cell[row][column].getText().equals(cell[row-1][column-1].getText()) &&
                cell[row-1][column-1].getText().equals(cell[row-2][column-2].getText()) &&
                cell[row-2][column-2].getText().equals(cell[row-3][column-3].getText());
    }

    private static boolean diagonalEqualAscending(int row,int column){
        return cell[row][column].getText().equals(cell[row-1][column+1].getText()) &&
                cell[row-1][column+1].getText().equals(cell[row-2][column+2].getText()) &&
                cell[row-2][column+2].getText().equals(cell[row-3][column+3].getText());
    }

    private static boolean columnEqual(int row, int column) {
        return cell[row-1][column].getText().equals(cell[row][column].getText()) &&
                cell[row][column].getText().equals(cell[row+1][column].getText()) &&
                cell[row+1][column].getText().equals(cell[row+2][column].getText());
    }

    private static boolean lineEqual(int row, int column) {
        return cell[row][column-1].getText().equals(cell[row][column].getText()) &&
                cell[row][column].getText().equals(cell[row][column+1].getText()) &&
                cell[row][column+1].getText().equals(cell[row][column+2].getText());
    }

    private static void markWinnerCells(int x1,int y1,int x2,int y2,int x3,int y3,int x4,int y4){
        cell[x1][y1].setBackground(Color.cyan);
        cell[x2][y2].setBackground(Color.cyan);
        cell[x3][y3].setBackground(Color.cyan);
        cell[x4][y4].setBackground(Color.cyan);
    }

}

class Cell extends JButton{
    Cell(String name){
        this.setName(name);
        this.setText(" ");
        this.setBackground(Color.LIGHT_GRAY);
        this.setFont(new Font("Arial",Font.BOLD,20));
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConnectFour.makeTurn(name);
            }
        });
    }
}


