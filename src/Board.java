import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Board {

    public Cell[][] game;
    int width;
    int height;

    public Board(int height, int width) {
        game = new Cell[height][width];
        this.height = height;
        this.width = width;
    }

    public Board() {
        game = new Cell[10][10];
        this.width = 10;
        this.height = 10;
    }

    public Color getCurrentColor(int row, int col) {
        return game[row][col].getColor();
    }


    /**
     * counts all the neighbors of every cell and updates their cell count
     */
    public void countNeighbors() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Cell current = game[i][j];
                current.setNeighborCount(countOneNeighbor(i, j));
            }
        }
        try {
            updateBoard();
        } catch (IOException e) {
            System.out.println("not there");
        }
    }

    public void drawBoard() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (game[i][j].isAlive()) {
                    game[i][j].setColor(Color.BLUE);
                }
                else {
                    game[i][j].setColor(Color.GRAY);
                }
            }

        }
    }


    private void updateBoard() throws IOException {
        FileRead myFile = new FileRead();
        myFile.readFile("Rules.txt");
        HashMap<Integer, String> aliveRules = myFile.getLIVERULES();
        HashMap<Integer, String> deadRules = myFile.getDEADRULES();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Cell current = game[i][j];
                int neighborNum = current.getNeighborCount();
                if (current.isAlive()) {
                    //System.out.println("Is alive!");
                    Set<Integer> keys = aliveRules.keySet();
                    for (Integer num : keys) {
                        String rules = aliveRules.get(num);
                        int number = Integer.parseInt(rules.substring(1, 2));
                        char state = rules.charAt(2);
                        if (rules.charAt(0) == '<') {
                            if (state == 'd') {
                                if (neighborNum < number)
                                    current.setAlive(0);
                            }

                        } else if (rules.charAt(0) == '>') {
                            if (state == 'd') {
                                if (neighborNum > number)
                                    current.setAlive(0);
                            }
                        } else if (rules.charAt(0) == '=') {
                            if (state == 'd') {
                                if (neighborNum == number)
                                    current.setAlive(0);
                            }
                        }
                    }
                }
                else{
                    Set<Integer> keys = deadRules.keySet();
                    for (Integer num : keys) {
                        String rules = deadRules.get(num);
                        int number = Integer.parseInt(rules.substring(1, 2));
                        char state = rules.charAt(2);
                        //System.out.println("rules: " + rules);
                        if (rules.charAt(0) == '<') {
                            if (state == 'l') {
                                if (neighborNum < number)
                                    current.setAlive(0);
                            }

                        } else if (rules.charAt(0) == '<') {
                            if (state == 'l') {
                                if (neighborNum > number)
                                    current.setAlive(1);
                            }
                        } else if (rules.charAt(0) == '=') {
                            if (state == 'l') {
                                if (neighborNum == number)
                                    current.setAlive(1);
                            }
                        }
                    }
                }
                current.setNeighborCount(0);
            }
        }

    }

    /**
     * helper method to count the neighbors of one cell
     *
     * @param i
     * @param j
     * @return
     */
    private int countOneNeighbor ( int i, int j){
        int[] rowOffsets = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] colOffsets = {-1, 0, 1, -1, 1, -1, 0, 1};
        int liveNeighbors = 0;
        for (int m = 0; m < 8; m++) {
            int newRow = i + rowOffsets[m];
            int newCol = j + colOffsets[m];
            // Check if the neighbor is within bounds and alive
            if (newRow >= 0 && newRow < game.length && newCol >= 0 && newCol < game[0].length && game[newRow][newCol].isAlive()) {
                liveNeighbors++;
            }
        }
        return liveNeighbors;
    }

//    public static void main(String[] args) {
//        Board board = new Board();
//    }
}