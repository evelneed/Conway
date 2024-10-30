import java.awt.*;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//40 by 80
public class defaultBoardV2 {

    HashMap<String, Board> hash;

    /**
     * Constructor method for the DefaultBoardV2 class. Initializes the hash map and calls saveDefaults to fill it.
     */
    public defaultBoardV2() {
        hash = new HashMap<String, Board>();
        try {
            saveDefaults();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HashMap<String, Board> getHash() {
        //saveDefaults();
        return hash;
    }

    /**
     * Fills each empty index in a Board game with a new Cell.
     * @param cur the empty board that is to be filled with Cells
     * @param countHeight the number if rows in the 2D array
     * @param countWidth the number of columns in the 2D array
     * @return the Board now filled with Cell objects
     */
    private Board initBoard(Board cur, int countHeight, int countWidth){
        for (int row = 0; row < countHeight; row++) {
            for (int col = 0; col < countWidth; col++) {
                Cell temp = new Cell(Cell.liveColor, Cell.deadColor);
                cur.game[row][col] = temp;
            }
        }
        return cur;
    }

    /**
     * Reads the file passed in and adds cells to a new Board, making them alive if a 1 is read and dead if a 0 is read.
     * @param file the name/path of the file being accessed
     * @return a Board that represents the file passed in, with alive and dead cells based on the file's contents
     * @throws IOException if any IOException occurs
     */
    public Board createBoard(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        BufferedReader reader2 = new BufferedReader(new FileReader(file));
        String line = "";
        int countWidth = 0;
        int countHeight = 0;
        while(true) {
            line = reader.readLine();
            if (line == null) {
                break;
            }
            countWidth = line.length();
            countHeight++;
        }
        Board cur = new Board(countHeight, countWidth);
        Board cur1 = initBoard(cur, countHeight, countWidth);
        line = "";
        int lineOn = 0;
        while(true) {
            line = reader2.readLine();
            if (line == null) {
                break;
            }
            if (lineOn < countHeight) {
                for (int x = 0; x < line.length(); x++) {
                    if (line.charAt(x) == '1') {
                        cur1.game[lineOn][x].setAlive(1);
                    }
                }
            }
            lineOn++;

        }
        return cur1;
    }

    /**
     * Adds new Boards to the hashmap which represent our five default files.
     * @throws IOException if any IOException occurs
     */
    public void saveDefaults() throws IOException {
        hash.put("sonia", createBoard("./src/Sonia.txt"));
        hash.put("default", createBoard("./src/bigDefault.txt"));
        hash.put("evelyn", createBoard("./src/Evelyn.txt"));
        hash.put("dax", createBoard("./src/Dax.txt"));
        hash.put("gosper", createBoard("./src/Gosper.txt"));
    }
}