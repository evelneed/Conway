import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.*;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GUI extends JFrame implements ActionListener {
    public static final int WIDTH = 1490;
    public static final int HEIGHT = 860;
    private Board board;
    String name;
    boolean running;
    CellPanel[][] panelStorage;

    HashMap<String, Board> hash;

    HashMap<String, Board> newOnes;
    HashMap<String, Board> newStart;

    defaultBoardV2 start;

    JPanel biggerPanel;

    boolean clicked;

    double[] speed = {1.0, 0.5, 2};

    double speedM;
    JMenu boardMenu;

    private int mode = 0;
    private Color borderColor = Color.LIGHT_GRAY;
    private Color otherBorderColor = Color.WHITE;


    /**
     * Updates the CellPanels color and state depending on its corresponding cell in this.board
     */
    private void updateBoard() {
        this.board.drawBoard();
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 80; j++) {
                Color color = board.getCurrentColor(i, j);
                panelStorage[i][j].setBackground(board.getCurrentColor(i, j));
                if (color.equals(Color.GRAY) || color.equals(Color.BLACK))
                    panelStorage[i][j].getCell().setAlive(0);
                else
                    panelStorage[i][j].getCell().setAlive(1);
            }
        }
    }

    /**
     * Constructor for our GUI class
     * @param defBoard the defaultBoard object containing our starting hashMap of boards
     */
    public GUI(defaultBoardV2 defBoard) {
        clicked = false;
        speedM = speed[0];
        hash = defBoard.hash;
        start = new defaultBoardV2();
        this.board = hash.get("sonia");
        name = "sonia";
        running = false;
        newOnes = new HashMap<>();
        newStart = new HashMap<>();
        boardMenu = new JMenu("Pick Board");
        makeBoard();
    }


    /**
     * Creates the J components (buttons, menus, etc) for our GUI and makes a mouse listener for each CellPanel
     */
    public void makeBoard() {
        biggerPanel = new JPanel();
        panelStorage = new CellPanel[40][80];

        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        biggerPanel.setLayout(new GridLayout(40, 80));
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 80; j++) {
                CellPanel pane = new CellPanel(this.board.game[i][j]);
                pane.setBackground(board.getCurrentColor(i, j));
                LineBorder border = new LineBorder(borderColor);
                pane.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (clicked) {
                            if (pane.getCell().isAlive()) {
                                pane.getCell().setAlive(0);
                                pane.getCell().setColor(Cell.deadColor);
                                // pane.setBorder(border);
                            } else {
                                pane.getCell().setAlive(1);
                                pane.getCell().setColor(Cell.liveColor);
                                // pane.setBorder(border);
                            }
                            updateBoard();
                        }
                    }
                    @Override
                    public void mousePressed(MouseEvent e) {
                        clicked = true;
                        //System.out.println(pane.getCell());
                        if (pane.getCell().isAlive()) {
                            pane.getCell().setAlive(0);
                            pane.getCell().setColor(Cell.deadColor);
                            //     pane.setBorder(border);
                        } else {
                            pane.getCell().setAlive(1);
                            pane.getCell().setColor(Cell.liveColor);
                            //     pane.setBorder(border);
                        }
                        updateBoard();
                        for (int i = 0; i < 40; i++) {
                            for (int j = 0; j < 80; j++) {
                                if (panelStorage[i][j].getCell().isAlive())
                                    board.game[i][j].setAlive(1);
                                else
                                    board.game[i][j].setAlive(0);
                            }
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        clicked = false;
                    }
                });
                //  pane.setVisible(true);
                panelStorage[i][j] = pane;
//                if (pane.getBackground().equals(Color.BLUE) ||pane.getBackground().equals(Color.PINK) )
//                    this.board.game[i][j].setAlive(1);
//                else
//                    this.board.game[i][j].setAlive(0);
                pane.setBorder(border);
                biggerPanel.add(pane);
            }
        }
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(otherBorderColor);
        buttonPanel.setLayout(new FlowLayout());
        JButton modeButton = new JButton("Mode");
        JButton startButton = new JButton("Start");
        JButton pauseButton = new JButton("Pause");
        JButton nextButton = new JButton("Next");
        JButton resetButton = new JButton("Reset");
        JButton saveButton = new JButton("Save");

        modeButton.setBackground(borderColor);
        modeButton.addActionListener(this);
        buttonPanel.add(modeButton);

        saveButton.setBackground(borderColor);
        saveButton.addActionListener(this);

        resetButton.setBackground(borderColor);
        resetButton.addActionListener(this);

        startButton.setBackground(borderColor);
        startButton.addActionListener(this);
        buttonPanel.add(startButton);

        pauseButton.setBackground(borderColor);
        pauseButton.addActionListener(this);
        buttonPanel.add(pauseButton);

        nextButton.setBackground(borderColor);
        nextButton.addActionListener(this);
        buttonPanel.add(nextButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(saveButton);
        biggerPanel.setBackground(otherBorderColor);
        add(buttonPanel, BorderLayout.SOUTH);
        add(biggerPanel, BorderLayout.CENTER);
        JMenuBar bar = new JMenuBar();
        MenuMan george = new MenuMan();
        george.execute();
        try {
            george.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        george.cancel(true);
        JMenu speedMenu = new JMenu("Pick Speed");
        for (double d : speed) {
            JMenuItem item = new JMenuItem("" + d +"x");
            item.addActionListener( this);
            speedMenu.add(item);
        }
        bar.add(speedMenu);
        bar.add(boardMenu);
        bar.setBackground(borderColor);
        add(bar, BorderLayout.NORTH);
    }


    @Override
    /**
     * The action listener for all of our listening components (buttons and menus)
     */
    public void actionPerformed(ActionEvent e) {
        String buttonString = e.getActionCommand();
        CoWorker fred = new CoWorker();
        if (buttonString.equals("Start")) {
            if (running){
            }
            else {
                running = true;
                fred = new CoWorker();
                fred.execute();
            }
        } else if (buttonString.equals("Pause")) {
            running = false;
            fred.cancel(true);
        } else if (buttonString.equals("Next")) {
            running=false;
            if(fred.cancel(false)){
                fred.cancel(true);
            }
            this.board.drawBoard();
            this.board.countNeighbors();
            updateBoard();
        }
        else if(buttonString.equals("Reset")){
            running = false;
            if(fred.cancel(false)){
                fred.cancel(true);
            }
            reset();
            //updateBoard();
        }
        else if (buttonString.equals("1.0x")) {
            speedM = 1;
        }
        else if (buttonString.equals("0.5x")) {
            speedM = 2;
        }
        else if (buttonString.equals("2.0x")) {
            speedM = 0.5;
        }
        else if(buttonString.equals("Save")){
            JFrame inputWindow = new JFrame("Save As");
            inputWindow.pack();
            inputWindow.setVisible(true);
            inputWindow.setSize(300,100);
            inputWindow.setLayout(new FlowLayout());
            JTextArea textbox = new JTextArea();
            inputWindow.add(textbox);
            JButton submit = new JButton("Submit");
            inputWindow.add(submit);
            submit.addActionListener(ex -> saveUserBoard(textbox.getText()));
            submit.addActionListener(ex -> inputWindow.dispose());
        }
        else if(buttonString.equals("Mode")){
            mode();
            partialReset();
        }
        else {
            running = false;
            if(fred.cancel(false)){
                fred.cancel(true);
            }
            this.board = start.getHash().get(buttonString);
            this.name = buttonString;
            reset();
        }
    }

    /**
     * Called when user presses save button. Saves two new boards reflecting the current board position. Adds
     * these to new hashmaps, one that is a starting position and one to be edited further.
     * @param input name of new board
     */
    private void saveUserBoard(String input){
        Board toSave = new Board(40,80);
        Board copy = new Board(40,80);
        for(int r=0; r<40; r++){
            for(int c=0; c<80; c++){
                int alive = this.board.game[r][c].alive;
                toSave.game[r][c] = new Cell(Cell.liveColor, Cell.deadColor);
                copy.game[r][c] = new Cell(Cell.liveColor, Cell.deadColor);
                if (alive == 1) {
                    toSave.game[r][c].setColor(Cell.liveColor);
                    copy.game[r][c].setColor(Cell.liveColor);
                    toSave.game[r][c].setAlive(1);
                    copy.game[r][c].setAlive(1);
                    panelStorage[r][c].getCell().setAlive(1);
                }
                else {
                    toSave.game[r][c].setColor(Cell.deadColor);
                    copy.game[r][c].setColor(Cell.deadColor);
                    toSave.game[r][c].setAlive(0);
                    copy.game[r][c].setAlive(0);
                    panelStorage[r][c].getCell().setAlive(0);
                }
            }
        }
        newOnes.put(input, toSave);
        newStart.put(input, copy);
        JMenuItem item = new JMenuItem(input);
        item.addActionListener( this);
        this.boardMenu.add(item);
        boardMenu.setVisible(true);
    }

    /**
     * Changes from light mode to dark mode and vice versa
     */
    private void mode(){
        if (mode==0) {
            for (int i = 0; i < 40; i++){
                for(int j=0; j < 80; j++){
                    if(this.board.game[i][j].getColor()==Color.GRAY) {
                        this.board.game[i][j].setColor(Color.BLACK);
                        this.board.game[i][j].setAlive(0);
                    }
                    if(this.board.game[i][j].getColor()==Color.BLUE) {
                        this.board.game[i][j].setColor(Color.PINK);
                        this.board.game[i][j].setAlive(1);
                    }
                }
            }
            borderColor = Color.DARK_GRAY;
            otherBorderColor = Color.DARK_GRAY;
            Cell.deadColor = Color.BLACK;
            Cell.liveColor = Color.PINK;
            mode=1;
        }
        else{
            for (int i = 0; i < 40; i++){
                for(int j=0; j< 80; j++){
                    if(this.board.game[i][j].getColor()==Color.BLACK) {
                        this.board.game[i][j].setColor(Color.GRAY);
                        this.board.game[i][j].setAlive(0);
                    }
                    if(this.board.game[i][j].getColor()==Color.PINK) {
                        this.board.game[i][j].setColor(Color.BLUE);
                        this.board.game[i][j].setAlive(1);
                    }
                }
            }
            borderColor = Color.LIGHT_GRAY;
            otherBorderColor = Color.WHITE;
            Cell.liveColor = Color.BLUE;
            Cell.deadColor = Color.GRAY;
            mode=0;
        }
    }

    /**
     * Makes a deep copy of Board passed in
     * @param copied board we aee copying
     * @return deep copy of copied
     */
    private Board deepCopy(Board copied) {
        Board copy = new Board(40,80);
        for(int r=0; r<copied.height; r++){
            for(int c=0; c<copied.width; c++){
                //int alive = copied.game[r][c].alive;
                Color color = copied.game[r][c].getColor();
                Cell cell = new Cell(Cell.liveColor, Cell.deadColor);
                if (color.equals(Color.BLUE) || color.equals(Color.PINK)) {
                    cell.setAlive(1);
                }
                else {
                    cell.setAlive(0);
                }
                copy.game[r][c] = cell;
                System.out.println(boardMenu.getAccessibleContext());
            }
        }
        return copy;
    }

    /**
     * Called when mode is changed, "resets" board to its current state, just different colors
     */
    private void partialReset() {
        start = new defaultBoardV2();
        for(String s: newStart.keySet()){
            start.getHash().put(s, deepCopy(newOnes.get(s)));
        }
        this.board.drawBoard();
        biggerPanel.removeAll();
        makeBoard();
        setVisible(true);

    }

    /**
     * Resets the current board back to its starting positions
     */
    private void reset(){
        start = new defaultBoardV2();
        for(String s: newStart.keySet()){
            start.getHash().put(s, deepCopy(newOnes.get(s)));
        }
        this.board = start.getHash().get(name);
        this.board.drawBoard();
        biggerPanel.removeAll();
        makeBoard();
        setVisible(true);
    }

    /**
     * SwingWorker to handle running the program in the background
     */
    class CoWorker extends SwingWorker<Void, Object> {
        @Override
        protected Void doInBackground() {
            return loopy();
        }
    }

    /**
     * Updates the board repeatedly until pause is pressed (running boolean set to false)
     */
    private Void loopy() {
        while (running) {
            this.board.drawBoard();
            this.board.countNeighbors();
            updateBoard();
            try {
                TimeUnit.MILLISECONDS.sleep((long) (100 * speedM));
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
        return null;
    }

    /**
     * SwingWorker that builds the menu with everything in the HashMap
     */
    class MenuMan extends SwingWorker<Void, Object> {
        @Override
        protected Void doInBackground(){
            return makeMenu();
        }
    }

    /**
     * Removes everything from the menu and adds it back, including new ones made by user
     */
    private synchronized Void makeMenu() {
        boardMenu.removeAll();
        for (String s : start.getHash().keySet()) {
            JMenuItem item = new JMenuItem(s);
            item.addActionListener( this);
            boardMenu.add(item);
        }
        return null;
    }
}
