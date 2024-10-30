import javax.swing.*;

public class CellPanel extends JPanel {
    private Cell myCell;

    public CellPanel(Cell newCell) {
        myCell = newCell;
    }

    public void setCell(Cell newCell) {
        myCell = newCell;
    }

    public Cell getCell() {
        return myCell;
    }
}