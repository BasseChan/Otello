import java.util.ArrayList;
import java.util.List;

class Elem {
    public int cost;
    public Position position;

    public Elem(int score, Position position) {
        this.cost = score;
        this.position = position;
    }
}

public class AI {
    private static final int maxIteration = 15;
    private static final int optionsToCut = 6;
    private final GameGrid gameGrid;
    private final int player;
    private final int otherPlayer;

    public AI(GameGrid gameGrid, int player) {
        this.gameGrid = gameGrid;
        this.player = player;
        this.otherPlayer = player == 1 ? 2 : 1;
    }

//    public Position moveAI() {
//        maxIteration = 20;
//        List<Position> validMoves = gameGrid.getValidMoves(player);
//        if(validMoves.size() > 6) maxIteration--;
//        List<Position> toChange = gameGrid.getChangedAfter(validMoves.get(0), player);
//        gameGrid.moveWithoutShowing(validMoves.get(0), toChange, player);
//        int best = move2(false, 1, true, 1000000);
//        Position position = validMoves.get(0);
//        gameGrid.undoMove(validMoves.get(0), toChange, otherPlayer);
//        for(int i = 1; i < validMoves.size(); i++) {
//            toChange = gameGrid.getChangedAfter(validMoves.get(i), player);
//            gameGrid.moveWithoutShowing(validMoves.get(i), toChange, player);
//            int current = move2(false, 1, true, best);
//            gameGrid.undoMove(validMoves.get(i), toChange, otherPlayer);
//            if(current > best) {
//                best = current;
//                position = validMoves.get(i);
//            }
//        }
//        System.out.println(best);
//        return position;
//    }

    public Position moveAI() {
        List<Position> validMoves = gameGrid.getValidMoves(player);

        List<Elem> moves = new ArrayList<>(validMoves.size());
        for (Position validMove : validMoves) {
            List<Position> toChange = gameGrid.getChangedAfter(validMove, player);
            gameGrid.moveWithoutShowing(validMove, toChange, player);
            moves.add(new Elem(countScore(), validMove));
            gameGrid.undoMove(validMove, toChange, otherPlayer);
        }
        moves.sort((e1, e2) -> Integer.compare(e2.cost, e1.cost));

        int nextLayer = validMoves.size() > optionsToCut ? 2 : 1;
        Position currentMove = moves.get(0).position;
        List<Position> toChange = gameGrid.getChangedAfter(currentMove, player);
        gameGrid.moveWithoutShowing(currentMove, toChange, player);
        int best = move2(false, nextLayer, true, 1000000);
        Position position = currentMove;
        gameGrid.undoMove(moves.get(0).position, toChange, otherPlayer);
        for(int i = 1; i < moves.size(); i++) {
            currentMove = moves.get(i).position;
            toChange = gameGrid.getChangedAfter(currentMove, player);
            gameGrid.moveWithoutShowing(currentMove, toChange, player);
            int current = move2(false, nextLayer, true, best);
            gameGrid.undoMove(currentMove, toChange, otherPlayer);
            if(current > best) {
                best = current;
                position = currentMove;
            }
        }
        return position;
    }

    private int countDifference() {
        int value = 0;
        int[][] board = gameGrid.getBoard();
        for(int y = 0; y < gameGrid.sizeY; y++) {
            for(int x = 0; x < gameGrid.sizeX; x++) {
                if (board[x][y] == player) {
                    value++;
                }
                else if(board[x][y] == otherPlayer) {
                    value--;
                }
            }
        }
        return value;
    }

    private int countInDanger() {
        int inDanger = 0;
        List<Position> validMoves = gameGrid.getValidMoves(otherPlayer);
        for(Position position : validMoves) {
            inDanger += gameGrid.getChangedNumber(position, player);
        }
        validMoves = gameGrid.getValidMoves(otherPlayer);
        for(Position position : validMoves) {
            inDanger -= gameGrid.getChangedNumber(position, otherPlayer);
        }
        return inDanger;
    }

    private int countOnCorners() {
        int number = 0;
        int[][] board = gameGrid.getBoard();
        int value = board[0][0];
        if(value != 0) {
            if(value == player) number++;
            else number--;
        }
        value = board[0][gameGrid.sizeY - 1];
        if(value != 0) {
            if(value == player) number++;
            else number--;
        }
        value = board[gameGrid.sizeX - 1][0];
        if(value != 0) {
            if(value == player) number++;
            else number--;
        }
        value = board[gameGrid.sizeX - 1][gameGrid.sizeY - 1];
        if(value != 0) {
            if(value == player) number++;
            else number--;
        }
        return number;
    }

    private int countOnBorder() {
        int number = 0;
        int[][] board = gameGrid.getBoard();
        for(int ix = 1; ix < gameGrid.sizeX - 1; ix++) {
            int value = board[ix][0];
            if(value != 0) {
                if(value == player) number++;
                else number--;
            }
        }
        for(int iy = 1; iy < gameGrid.sizeX - 1; iy++) {
            int value = board[0][iy];
            if(value != 0) {
                if(value == player) number++;
                else number--;
            }
        }
        for(int ix = 1; ix < gameGrid.sizeX - 1; ix++) {
            int value = board[ix][gameGrid.sizeY - 1];
            if(value != 0) {
                if(value == player) number++;
                else number--;
            }
        }
        for(int iy = 1; iy < gameGrid.sizeX - 1; iy++) {
            int value = board[gameGrid.sizeX - 1][iy];
            if(value != 0) {
                if(value == player) number++;
                else number--;
            }
        }
        return number;
    }

//    public int move2(boolean myMove, int layer, boolean hasPrev, int previous) {
//        int thisPlayer = myMove ? player : otherPlayer;
//        List<Position> validMoves = gameGrid.getValidMoves(thisPlayer);
//        if(validMoves.isEmpty()) {
//            if(hasPrev) {
//                return move2(!myMove, layer - 1, false, previous);
//            }
//            else {
//                int value = countDifference();
//                if(value > 0) return value + 1000000;
//                if(value < 0) return value - 1000000;
//                return 0;
//            }
//        }
//        if(layer > maxIteration) {
//            return countScore();
//        }
//
//        int notThisPlayer = myMove ? otherPlayer : player;
//        if(validMoves.size() > 6) maxIteration--;
//        List<Position> toChange = gameGrid.getChangedAfter(validMoves.get(0), thisPlayer);
//        gameGrid.moveWithoutShowing(validMoves.get(0), toChange, thisPlayer);
//        int best = 0;
//
//        if(myMove) {
//            best = move2(false, layer + 1, true, 1000000);
//            gameGrid.undoMove(validMoves.get(0), toChange, notThisPlayer);
//
//            if(best > previous) return best;
//
//            for(int i = 1; i < validMoves.size(); i++) {
//                toChange = gameGrid.getChangedAfter(validMoves.get(i), thisPlayer);
//                gameGrid.moveWithoutShowing(validMoves.get(i), toChange, thisPlayer);
//                int current = move2(false, layer + 1, true, best);
//                gameGrid.undoMove(validMoves.get(i), toChange, notThisPlayer);
//                if(current > best) {
//                    if(hasPrev && current > previous) return current;
//                    best = current;
//                }
//            }
//        }
//        if(!myMove) {
//            best = move2(true, layer + 1, true, -100000);
//            gameGrid.undoMove(validMoves.get(0), toChange, notThisPlayer);
//
//            if(best < previous) return best;
//
//            for(int i = 1; i < validMoves.size(); i++) {
//                toChange = gameGrid.getChangedAfter(validMoves.get(i), thisPlayer);
//                gameGrid.moveWithoutShowing(validMoves.get(i), toChange, thisPlayer);
//                int current = move2(true, layer + 1, true, best);
//                gameGrid.undoMove(validMoves.get(i), toChange, notThisPlayer);
//                if(current < best) {
//                    if(hasPrev && current < previous) return current;
//                    best = current;
//                }
//            }
//        }
//        return best;
//    }

    public int move2(boolean myMove, int layer, boolean hasPrev, int previous) {
        int thisPlayer = myMove ? player : otherPlayer;
        List<Position> validMoves = gameGrid.getValidMoves(thisPlayer);
        if(validMoves.isEmpty()) {
            if(hasPrev) {
                return move2(!myMove, layer - 1, false, previous);
            }
            else {
                int value = countDifference();
                if(value > 0) return value + 1000000;
                if(value < 0) return value - 1000000;
                return 0;
            }
        }
        if(layer > maxIteration) {
            return countScore();
        }

        int notThisPlayer = myMove ? otherPlayer : player;

        List<Elem> moves = new ArrayList<>(validMoves.size());
        for (Position validMove : validMoves) {
            List<Position> toChange = gameGrid.getChangedAfter(validMove, thisPlayer);
            gameGrid.moveWithoutShowing(validMove, toChange, thisPlayer);
            moves.add(new Elem(countScore(), validMove));
            gameGrid.undoMove(validMove, toChange, notThisPlayer);
//            moves.add(new Elem(0, validMove));
        }
        moves.sort((e1, e2) -> Integer.compare(e2.cost, e1.cost));

        int nextLayer = validMoves.size() > optionsToCut ? layer + 2 : layer + 1;
        Position currentPosition = moves.get(0).position;
        List<Position> toChange = gameGrid.getChangedAfter(currentPosition, thisPlayer);
        gameGrid.moveWithoutShowing(currentPosition, toChange, thisPlayer);
        int best = 0;

        if(myMove) {
            best = move2(false, nextLayer, true, 1000000);
            gameGrid.undoMove(currentPosition, toChange, notThisPlayer);

            if(best > previous) return best;

            for(int i = 1; i < validMoves.size(); i++) {
                currentPosition = moves.get(i).position;
                toChange = gameGrid.getChangedAfter(currentPosition, thisPlayer);
                gameGrid.moveWithoutShowing(currentPosition, toChange, thisPlayer);
                int current = move2(false, nextLayer, true, best);
                gameGrid.undoMove(currentPosition, toChange, notThisPlayer);
                if(current > best) {
                    if(hasPrev && current > previous) return current;
                    best = current;
                }
            }
        }
        if(!myMove) {
            best = move2(true, layer + 1, true, -100000);
            gameGrid.undoMove(currentPosition, toChange, notThisPlayer);

            if(best < previous) return best;

            for(int i = 1; i < validMoves.size(); i++) {
                currentPosition = moves.get(i).position;
                toChange = gameGrid.getChangedAfter(currentPosition, thisPlayer);
                gameGrid.moveWithoutShowing(currentPosition, toChange, thisPlayer);
                int current = move2(true, nextLayer, true, best);
                gameGrid.undoMove(currentPosition, toChange, notThisPlayer);
                if(current < best) {
                    if(hasPrev && current < previous) return current;
                    best = current;
                }
            }
        }
        return best;
    }

    private int countScore() {
        if(gameGrid.getMoveNumber() > 50) {
            return countDifference() + 10 * countInDanger() + 20 * countOnCorners() + 4 * countOnBorder();
        }
        if(gameGrid.getMoveNumber() > 30) {
            return countDifference() + 4 * countInDanger() + 500 * countOnCorners() + 10 * countOnBorder();
        }
        return countInDanger() + 200 * countOnCorners() + 10 * countOnBorder();
    }

    //    public Position firstMove() {
//        List<Position> validMoves = gameGrid.getAllValidMoves();
////        int thisPlayer = myMove ? player : otherPlayer;
////        int notThisPlayer = myMove ? otherPlayer : player;
//        List<Position> toChange = gameGrid.getChangedAfter(validMoves.get(0), player);
//        gameGrid.doMove(validMoves.get(0), toChange, player);
//        int best = move(false, 1, true);
//        Position position = validMoves.get(0);
//        gameGrid.undoMove(validMoves.get(0), toChange, otherPlayer);
//        for(int i = 1; i < validMoves.size(); i++) {
//            toChange = gameGrid.getChangedAfter(validMoves.get(i), player);
//            gameGrid.doMove(validMoves.get(i), toChange, player);
//            int current = move(false, 1, true);
//            gameGrid.undoMove(validMoves.get(i), toChange, otherPlayer);
//            if(current > best) {
//                best = current;
//                position = validMoves.get(i);
//            }
//        }
//        return position;
//    }

//    public int move(boolean myMove, int layer, boolean hasPrev) {
//        int thisPlayer = myMove ? player : otherPlayer;
//        List<Position> validMoves = gameGrid.getValidMoves(thisPlayer);
//        if(validMoves.isEmpty()) {
//            if(hasPrev) {
//                return move(!myMove, layer - 1, false);
//            }
//            else {
//                int value = countDifference();
//                if(value > 0) return value + 1000;
//                if(value < 0) return value - 1000;
//                return 0;
//            }
//        }
//        if(layer > 5) {
//            return countScore();
//        }
//        int notThisPlayer = myMove ? otherPlayer : player;
//        List<Position> toChange = gameGrid.getChangedAfter(validMoves.get(0), thisPlayer);
//        gameGrid.moveWithoutShowing(validMoves.get(0), toChange, thisPlayer);
//        int best = move(!myMove, layer + 1, true);
//        gameGrid.undoMove(validMoves.get(0), toChange, notThisPlayer);
//        for(int i = 1; i < validMoves.size(); i++) {
//            toChange = gameGrid.getChangedAfter(validMoves.get(i), thisPlayer);
//            gameGrid.moveWithoutShowing(validMoves.get(i), toChange, thisPlayer);
//            int current = move(!myMove, layer + 1, true);
//            gameGrid.undoMove(validMoves.get(i), toChange, notThisPlayer);
//            if((myMove && current > best) || (!myMove && current < best)) {
//                best = current;
//            }
//        }
//        return best;
//    }

//    public Position chooseMove() {
//        Collections.shuffle(gameGrid.getAllValidMoves());
//        return gameGrid.getAllValidMoves().get(0);
//    }

}