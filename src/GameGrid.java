import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class GameGrid extends Rectangle {
    private final GridCell[][] grid;
    private final int[][] board;
    private int moveNumber;
    private List<Position> validMoves;
    public final int sizeX;
    public final int sizeY;
    private int[] fieldsCount;

    public GameGrid(Position position, int width, int height, int gridWidth, int gridHeight) {
        super(position, width, height);
        sizeX = gridWidth;
        sizeY = gridHeight;
        fieldsCount = new int[]{sizeX * sizeY, 0, 0};
        board = new int[gridWidth][gridHeight];
        grid = new GridCell[gridWidth][gridHeight];
        int cellWidth = (width-position.x)/gridWidth;
        int cellHeight = (height-position.y)/gridHeight;
        for(int x = 0; x < gridWidth; x++) {
            for(int y = 0; y < gridHeight; y++) {
                grid[x][y] = new GridCell(new Position(position.x+cellWidth*x, position.y+cellHeight*y),
                        cellWidth, cellHeight);
                board[x][y] = 0;
            }
        }
        moveNumber = 0;
        validMoves = new ArrayList<>();
        updateValidMoves(1);
    }

    public void reset() {
        for(int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                grid[x][y].reset();
                board[x][y] = 0;
            }
        }
        fieldsCount = new int[]{sizeX * sizeY, 0, 0};
        moveNumber = 0;
        updateValidMoves(1);
    }

    public GridCell[][] getGrid() {
        return grid;
    }

    public int[][] getBoard() {
        return board;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public void setMoveNumber(int moveNumber) {
        this.moveNumber = moveNumber;
    }

    public int[] getFieldsCount() {
        return fieldsCount;
    }

    public void setFieldsCount(int[] fieldsCount) {
        this.fieldsCount = fieldsCount;
    }

    public List<Position> getAllValidMoves() {
        return validMoves;
    }

    // zmień
    public void playMove(Position position, int player) {
        moveNumber++;
        fieldsCount[0]--;
        fieldsCount[player]++;

        grid[position.x][position.y].setCellState(player);
        board[position.x][position.y] = player;
        List<Position> changeCellPositions = getChangedAfter(position, player);
        int otherPlayer = player == 1 ? 2 : 1;
        fieldsCount[player] += changeCellPositions.size();
        fieldsCount[otherPlayer] -= changeCellPositions.size();
        for(Position swapPosition : changeCellPositions) {
            grid[swapPosition.x][swapPosition.y].setCellState(player);
            board[swapPosition.x][swapPosition.y] = player;
        }
        updateValidMoves(otherPlayer);
    }

    public void moveWithoutShowing(Position position, List<Position> toChange, int player) {
        moveNumber++;
        board[position.x][position.y] = player;
        for(Position swapPosition : toChange) {
            board[swapPosition.x][swapPosition.y] = player;
        }
    }

    public void undoMove(Position position, List<Position> toChange, int otherPlayer) {
        moveNumber--;
        board[position.x][position.y] = 0;
        for(Position swapPosition : toChange) {
            board[swapPosition.x][swapPosition.y] = otherPlayer;
        }
    }

    public Position convertMouseToGridPosition(Position mousePosition) {
        int gridX = (mousePosition.x- position.x)/grid[0][0].width;
        int gridY = (mousePosition.y- position.y)/grid[0][0].height;
        if(gridX >= grid.length || gridX < 0 || gridY >= grid[0].length || gridY < 0) {
            return new Position(-1,-1);
        }
        return new Position(gridX,gridY);
    }

    public boolean isValidMove(Position position) {
        return getAllValidMoves().contains(position);
    }

    public int getWinner(boolean stillValidMoves) {
        int[] counts = {0,0,0};
        for(int y = 0; y < sizeY; y++) {
            for(int x = 0; x < sizeX; x++) {
                //counts[grid[x][y].getCellState()]++;
                counts[board[x][y]]++;
            }
        }

        if(stillValidMoves && counts[0] > 0) return 0;
        else if(counts[1] == counts[2]) return 3;
        else return counts[1] > counts[2] ? 1 : 2;
    }

    public void paint(Graphics g) {
        drawGridLines(g);
        for (GridCell[] gridCells : grid) {
            for (int y = 0; y < grid[0].length; y++) {
                gridCells[y].paint(g);
            }
        }
    }

    private void drawGridLines(Graphics g) {
        g.setColor(Color.BLACK);
        // Draw vertical lines
        int y2 = position.y+height;
        int y1 = position.y;
        for(int x = 0; x < grid.length+1; x++)
            g.drawLine(position.x+x * grid[0][0].width, y1, position.x+x * grid[0][0].width, y2);

        // Draw horizontal lines
        int x2 = position.x+width;
        int x1 = position.x;
        for(int y = 0; y < grid[0].length+1; y++)
            g.drawLine(x1, position.y+y * grid[0][0].height, x2, position.y+y * grid[0][0].height);
    }

    public void updateValidMoves(int playerID) {
        for(Position validMove : validMoves) {
            grid[validMove.x][validMove.y].setHighlight(false);
        }
//        validMoves.clear();
//        if(moveNumber < 4) {
//            int midX = grid.length/2-1;
//            int midY = grid[0].length/2-1;
//            for (int x = midX; x < midX+2; x++) {
//                for (int y = midY; y < midY+2; y++) {
//                    if (grid[x][y].getCellState() == 0) {
//                        validMoves.add(new Position(x, y));
//                    }
//                }
//            }
//        } else {
//            for (int x = 0; x < sizeX; x++) {
//                for (int y = 0; y < sizeY; y++) {
//                    Position pos = new Position(x, y);
//                    if (board[x][y] == 0 && checkIfValid(pos,playerID)) {
//                        validMoves.add(new Position(x, y));
//                    }
//                }
//            }
//        }
        validMoves = getValidMoves(playerID);
        for(Position validMove : validMoves) {
            grid[validMove.x][validMove.y].setHighlight(true);
        }
    }

    public List<Position> getValidMoves(int playerID) {
        List<Position> validMovesList = new ArrayList<>();
        if(moveNumber < 4) {
            int midX = grid.length/2-1;
            int midY = grid[0].length/2-1;
            for (int x = midX; x < midX+2; x++) {
                for (int y = midY; y < midY+2; y++) {
                    if (grid[x][y].getCellState() == 0) {
                        validMovesList.add(new Position(x, y));
                    }
                }
            }
        } else {
            for (int x = 0; x < sizeX; x++) {
                for (int y = 0; y < sizeY; y++) {
                    Position pos = new Position(x, y);
                    if (board[x][y] == 0 && checkIfValid(pos,playerID)) {
                        validMovesList.add(new Position(x, y));
                    }
                }
            }
        }
        return validMovesList;
    }

//    public List<Position> getChangedPositionsForMove(Position position, int playerID) {
//        List<Position> result = new ArrayList<>();
//        result.addAll(getChangedPositionsForMoveInDirection(position, playerID, Position.DOWN));
//        result.addAll(getChangedPositionsForMoveInDirection(position, playerID, Position.LEFT));
//        result.addAll(getChangedPositionsForMoveInDirection(position, playerID, Position.UP));
//        result.addAll(getChangedPositionsForMoveInDirection(position, playerID, Position.RIGHT));
//        return result;
//    }

    public List<Position> getChangedAfter(Position position, int playerID) {
        List<Position> result = new ArrayList<>();
        int x = position.x;
        int y = position.y;
        int otherPlayer = playerID == 1 ? 2 : 1;
        for(int ix = x-1; ix >= 0; ix--) {
            int current = board[ix][y];
            if(current != otherPlayer) {
                if(current == playerID) {
                    for(int jx = ix+1; jx < x; jx++) {
                        result.add(new Position(jx, y));
                    }
                }
                ix = -1;
            }
        }
        for(int iy = y-1; iy >= 0; iy--) {
            int current = board[x][iy];
            if(current != otherPlayer) {
                if(current == playerID) {
                    for(int jy = iy+1; jy < y; jy++) {
                        result.add(new Position(x, jy));
                    }
                }
                iy = -1;
            }
        }
        for(int ix = x+1; ix < sizeX; ix++) {
            int current = board[ix][y];
            if(current != otherPlayer) {
                if(current == playerID) {
                    for(int jx = ix-1; jx > x; jx--) {
                        result.add(new Position(jx, y));
                    }
                }
                ix = sizeX;
            }
        }
        for(int iy = y+1; iy < sizeY; iy++) {
            int current = board[x][iy];
            if(current != otherPlayer) {
                if(current == playerID) {
                    for(int jy = iy-1; jy > y; jy--) {
                        result.add(new Position(x, jy));
                    }
                }
                iy = sizeY;
            }
        }
        int border = Math.min(x, y);
        for(int i = 1; i <= border; i++) {
            int current = board[x - i][y - i];
            if(current != otherPlayer) {
                if(current == playerID) {
                    for(int j = i-1; j > 0; j--) {
                        result.add(new Position(x - j, y - j));
                    }
                }
                i = border;
            }
        }
        border = Math.min(sizeX - x, sizeY - y);
        for(int i = 1; i < border; i++) {
            int current = board[x + i][y + i];
            if(current != otherPlayer) {
                if(current == playerID) {
                    for(int j = i-1; j > 0; j--) {
                        result.add(new Position(x + j, y + j));
                    }
                }
                i = border;
            }
        }
        border = Math.min(sizeX - x - 1, y);
        for(int i = 1; i <= border; i++) {
            int current = board[x + i][y - i];
            if(current != otherPlayer) {
                if(current == playerID) {
                    for(int j = i-1; j > 0; j--) {
                        result.add(new Position(x + j, y - j));
                    }
                }
                i = border;
            }
        }
        border = Math.min(x, sizeY - y - 1);
        for(int i = 1; i <= border; i++) {
            int current = board[x - i][y + i];
            if(current != otherPlayer) {
                if(current == playerID) {
                    for(int j = i-1; j > 0; j--) {
                        result.add(new Position(x - j, y + j));
                    }
                }
                i = border;
            }
        }
        return result;
    }

    public int getChangedNumber(Position position, int playerID) {
        int number = 0;
        int x = position.x;
        int y = position.y;
        int otherPlayer = playerID == 1 ? 2 : 1;
        for(int ix = x-1; ix >= 0; ix--) {
            int current = board[ix][y];
            if(current != otherPlayer) {
                if(current == playerID) {
                    number += x - ix - 1;
                }
                ix = -1;
            }
        }
        for(int iy = y-1; iy >= 0; iy--) {
            int current = board[x][iy];
            if(current != otherPlayer) {
                if(current == playerID) {
                    number += y - iy - 1;
                }
                iy = -1;
            }
        }
        for(int ix = x+1; ix < sizeX; ix++) {
            int current = board[ix][y];
            if(current != otherPlayer) {
                if(current == playerID) {
                    number += ix - x - 1;
                }
                ix = sizeX;
            }
        }
        for(int iy = y+1; iy < sizeY; iy++) {
            int current = board[x][iy];
            if(current != otherPlayer) {
                if(current == playerID) {
                    number += iy - y - 1;
                }
                iy = sizeY;
            }
        }
        int border = Math.min(x, y);
        for(int i = 1; i <= border; i++) {
            int current = board[x - i][y - i];
            if(current != otherPlayer) {
                if(current == playerID) {
                    number += i - 1;
                }
                i = border;
            }
        }
        border = Math.min(sizeX - x, sizeY - y);
        for(int i = 1; i < border; i++) {
            int current = board[x + i][y + i];
            if(current != otherPlayer) {
                if(current == playerID) {
                    number += i - 1;
                }
                i = border;
            }
        }
        border = Math.min(sizeX - x - 1, y);
        for(int i = 1; i <= border; i++) {
            int current = board[x + i][y - i];
            if(current != otherPlayer) {
                if(current == playerID) {
                    number += i - 1;
                }
                i = border;
            }
        }
        border = Math.min(x, sizeY - y - 1);
        for(int i = 1; i <= border; i++) {
            int current = board[x - i][y + i];
            if(current != otherPlayer) {
                if(current == playerID) {
                    number += i - 1;
                }
                i = border;
            }
        }
        return number;
    }

    private boolean checkIfValid(Position position, int playerID) {
        int x = position.x;
        int y = position.y;
        int otherPlayer = playerID == 1 ? 2 : 1;
        for(int ix = x-1; ix >= 0; ix--) {
            int current = board[ix][y];
            if(current != otherPlayer) {
                if(current == playerID && ix < x-1) {
                    return true;
                }
                ix = -1;
            }
        }
        for(int iy = y-1; iy >= 0; iy--) {
            int current = board[x][iy];
            if(current != otherPlayer) {
                if(current == playerID && iy < y-1) {
                    return true;
                }
                iy = -1;
            }
        }
        for(int ix = x+1; ix < sizeX; ix++) {
            int current = board[ix][y];
            if(current != otherPlayer) {
                if(current == playerID && ix > x+1) {
                    return true;
                }
                ix = sizeX;
            }
        }
        for(int iy = y+1; iy < sizeY; iy++) {
            int current = board[x][iy];
            if(current != otherPlayer) {
                if(current == playerID && iy > y+1) {
                    return true;
                }
                iy = sizeY;
            }
        }
        int border = Math.min(x, y);
        for(int i = 1; i <= border; i++) {
            int current = board[x - i][y - i];
            if(current != otherPlayer) {
                if(current == playerID && i > 1) {
                    return true;
                }
                i = border;
            }
        }
        border = Math.min(sizeX - x, sizeY - y);
        for(int i = 1; i < border; i++) {
            int current = board[x + i][y + i];
            if(current != otherPlayer) {
                if(current == playerID && i > 1) {
                    return true;
                }
                i = border;
            }
        }
        border = Math.min(sizeX - x - 1, y);
        for(int i = 1; i <= border; i++) {
            int current = board[x + i][y - i];
            if(current != otherPlayer) {
                if(current == playerID && i > 1) {
                    return true;
                }
                i = border;
            }
        }
        border = Math.min(x, sizeY - y - 1);
        for(int i = 1; i <= border; i++) {
            int current = board[x - i][y + i];
            if(current != otherPlayer) {
                if(current == playerID && i > 1) {
                    return true;
                }
                i = border;
            }
        }
        return false;
    }

//    private List<Position> getChangedPositionsForMoveInDirection(Position position, int playerID, Position direction) {
//        List<Position> result = new ArrayList<>();
//        Position movingPos = new Position(position);
//        int otherPlayer = playerID == 1 ? 2 : 1;
//        movingPos.add(direction);
//        // Keep moving while there are positions that would be changed.
//        while(inBounds(movingPos) && grid[movingPos.x][movingPos.y].getCellState() == otherPlayer) {
//            result.add(new Position(movingPos));
//            movingPos.add(direction);
//        }
//        // If the end position is off the board, or the end playerID does not match the player, that
//        // means that the move would not give any valid swaps in this direction.
//        if(!inBounds(movingPos) || grid[movingPos.x][movingPos.y].getCellState() != playerID) {
//            result.clear();
//        }
//        return result;
//    }

    private boolean inBounds(Position position) {
        return !(position.x < 0 || position.y < 0 || position.x >= grid.length || position.y >= grid[0].length);
    }

    public void drawBoardToConsole() {
        System.out.println("\nMove " + moveNumber);
        for(int y = 0; y < sizeY; y++) {
            for(int x = 0; x < sizeX; x++) {
                System.out.print(board[x][y] + "  ");
            }
            System.out.println();
        }
    }

//    public boolean checkIfHasValid(int player) {
//
//    }
}