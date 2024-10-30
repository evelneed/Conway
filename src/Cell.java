import java.awt.*;

/**
 * Class that represents one cell
 */
public class Cell {
    private Color color;
    public static Color liveColor = Color.BLUE;
    public static Color deadColor = Color.GRAY;
    public int alive; // 0 is dead, 1 is alive

    private int neighborCount;

    /**
     * Constructor that creates a cell with a live or dead state represented by color
     * @param l alive color
     * @param d dead color
     */
    public Cell(Color l, Color d) {
        this.deadColor = d;
        this.liveColor = l;
        this.color = d;
        alive = 0;
        neighborCount = 0;
    }
    public void setNeighborCount(int count) {
        neighborCount = count;
    }
    public int getNeighborCount() {
        return neighborCount;
    }

    /**
     * Returns whether or not a cell is alive
     * @return true if alive, false otherwise
     */
    public boolean isAlive() {
        return (alive == 1);
    }

    public void setAlive(int state) {
        if (state == 1)
            this.color = this.liveColor;
        else
            this.color = this.deadColor;
        this.alive = state;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    /**
     * Represents a cell as a string, depending on its state (1 if alive, 0 if dead)
     */
    public String toString() {
        if (alive == 1) {
            return "1";
        }
        else
            return "0";
    }
}