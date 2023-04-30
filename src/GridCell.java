import java.awt.*;


public class GridCell extends Rectangle {
    private int cellState;
    private boolean highlight;
    private int defaultState;

    public GridCell(Position position, int width, int height, int defaultState) {
        super(position, width, height);
        this.defaultState = defaultState;
        reset();
    }

    public void reset() {
        cellState = defaultState;
        highlight = false;
    }

    public void setCellState(int newState) {
        this.cellState = newState;
    }

    public int getCellState() {
        return cellState;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public void paint(Graphics g) {
        if(highlight) {
            g.setColor(new Color(0,255,10, 120));
            g.fillRect(position.x, position.y, width, height);
        }

        if(cellState == 0) return;
        g.setColor(cellState == 1 ? Color.BLACK : Color.WHITE);
        g.fillOval(position.x, position.y, width, height);
    }
}