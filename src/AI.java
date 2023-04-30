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
    private int number = 0;
    private static final int maxIteration = 10;     //:D
    private static final int optionsToCut = 8;
    private final GameGrid gameGrid;
    private final int player;
    private final int otherPlayer;
    private Heuristic heuristic;
    private int maxRoundNumber;
    public int totalNodesVisited = 0;
//    private int alfa;
//    private int beta;

    public AI(GameGrid gameGrid, int player) {
        this.gameGrid = gameGrid;
        this.player = player;
        this.otherPlayer = player == 1 ? 2 : 1;
    }

    public AI(GameGrid gameGrid, int player, Heuristic heuristic) {
        this.gameGrid = gameGrid;
        this.player = player;
        this.otherPlayer = player == 1 ? 2 : 1;
        this.heuristic = heuristic;
    }

//    public Position moveAI() {
//        alfa = -10000000;
//        beta = 10000000;
//        maxRoundNumber = gameGrid.getMoveNumber() + maxIteration;
//        long start = System.currentTimeMillis();
//        totalNodesVisited += number;
//        number = 0;
//        List<Position> validMoves = gameGrid.getValidMoves(player);
//
//        List<Elem> moves = getNotSortedValidMoves(validMoves, player, otherPlayer, true);    //:D
//
////        int nextLayer = 1;
//        int nextLayer = validMoves.size() > optionsToCut ? 2 : 1;
//        Position currentMove = moves.get(0).position;
//        Position position = currentMove;
//        List<Position> toChange = gameGrid.getChangedAfter(currentMove, player);
//        gameGrid.moveWithoutShowing(currentMove, toChange, player);
//        int best = move2(false, nextLayer, true, 1000000);
//        gameGrid.undoMove(currentMove, toChange, otherPlayer);
//        for(int i = 1; i < moves.size(); i++) {
//            currentMove = moves.get(i).position;
//            toChange = gameGrid.getChangedAfter(currentMove, player);
//            gameGrid.moveWithoutShowing(currentMove, toChange, player);
//            int current = move2(false, nextLayer, true, best);
//            gameGrid.undoMove(currentMove, toChange, otherPlayer);
//            if(current > best) {
//                best = current;
//                position = currentMove;
//            }
//        }
//        System.err.println("Odwiedzono " + number + " nodów");
//        System.err.println("Czas kompilacji: " + (System.currentTimeMillis() - start) + "ms");
//        return position;
//    }

    public Position moveAI() {
//        alfa = -10000000;
//        beta = 10000000;
        maxRoundNumber = gameGrid.getMoveNumber() + maxIteration;
        long start = System.currentTimeMillis();
        totalNodesVisited += number;
        number = 0;
        List<Position> validMoves = gameGrid.getValidMoves(player);

        List<Elem> moves = getSortedValidMoves(validMoves, player, otherPlayer, true);    //:D

//        int nextLayer = 1;
        int nextLayer = validMoves.size() > optionsToCut ? 2 : 1;
        Position currentMove = moves.get(0).position;
        Position position = currentMove;
        List<Position> toChange = gameGrid.getChangedAfter(currentMove, player);
        gameGrid.moveWithoutShowing(currentMove, toChange, player);
        int best = move4(false, nextLayer, true, -1000000, 1000000);
        gameGrid.undoMove(currentMove, toChange, otherPlayer);
        for(int i = 1; i < moves.size(); i++) {
            currentMove = moves.get(i).position;
            toChange = gameGrid.getChangedAfter(currentMove, player);
            gameGrid.moveWithoutShowing(currentMove, toChange, player);
            int current = move4(false, nextLayer, true, -1000000, 1000000);
            gameGrid.undoMove(currentMove, toChange, otherPlayer);
            if(current > best) {
                best = current;
                position = currentMove;
            }
        }
        System.err.println("Odwiedzono " + number + " nodów");
        System.err.println("Czas kompilacji: " + (System.currentTimeMillis() - start) + "ms");
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

    private int heuristicInDanger() {
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

    private int heuristicOnCorner() {
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

    private int heuristicOnBorder() {
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

    private int heuristicFieldValue() {
        int score = 0;
        int[][] board = gameGrid.getBoard();
        for(int ix = 0; ix < gameGrid.sizeX; ix++) {
            for(int iy = 0; iy < gameGrid.sizeY; iy++) {
                if(board[ix][iy] != 0) {
                    if(board[ix][iy] == player)
                        score += heuristic.fieldsValues[ix][iy];
                    else
                        score -= heuristic.fieldsValues[ix][iy];
                }
            }
        }
        return score;
    }

    public int move2(boolean myMove, int layer, boolean hasPrev, int alfa, int beta) {
        number++;
        int thisPlayer = myMove ? player : otherPlayer;
        List<Position> validMoves = gameGrid.getValidMoves(thisPlayer);
        if(validMoves.isEmpty()) {
            if(hasPrev) {
//                System.out.println("Brak ruchu");
                return move2(!myMove, layer, false, alfa, beta);
            }
            else {
//                System.out.println("Potencjalny koniec na głębokości " + layer);
                int value = countDifference();
                if(value > 0) return value + 100000000;
                if(value < 0) return value - 100000000;
                return 0;
            }
        }
        if(layer > maxIteration) {
            return countScore();
        }

        int notThisPlayer = myMove ? otherPlayer : player;

        List<Elem> moves;
        if(layer <= -maxIteration) {   //:D
            moves = getSortedValidMoves(validMoves, thisPlayer, notThisPlayer, myMove);
        } else {
            moves = getNotSortedValidMoves(validMoves, thisPlayer, notThisPlayer, myMove);
        }

        int nextLayer = validMoves.size() > optionsToCut ? layer + 2 : layer + 1;
//        int nextLayer = layer + 1;

        if(myMove) {
            for(int i = 1; i < validMoves.size(); i++) {
                Position currentPosition = moves.get(i).position;
                List<Position> toChange = gameGrid.getChangedAfter(currentPosition, thisPlayer);
                gameGrid.moveWithoutShowing(currentPosition, toChange, thisPlayer);
                int current = move2(false, nextLayer, true, alfa, beta);
                gameGrid.undoMove(currentPosition, toChange, notThisPlayer);
                if(current > alfa) {
                    alfa = current;
                    if(alfa >= beta) return alfa;  //:D
                }
            }
            return alfa;
        }
        else {
            for(int i = 1; i < validMoves.size(); i++) {
                Position currentPosition = moves.get(i).position;
                List<Position> toChange = gameGrid.getChangedAfter(currentPosition, thisPlayer);
                gameGrid.moveWithoutShowing(currentPosition, toChange, thisPlayer);
                int current = move2(true, nextLayer, true, alfa, beta);
                gameGrid.undoMove(currentPosition, toChange, notThisPlayer);
                if(current < beta) {
                    beta = current;
                    if(alfa >= beta) return beta;  //:D
                }
            }
            return beta;
        }
    }

    public int move4(boolean myMove, int layer, boolean hasPrev, int alfa, int beta) {
        number++;
        int thisPlayer = myMove ? player : otherPlayer;
        List<Position> validMoves = gameGrid.getValidMoves(thisPlayer);
        if(validMoves.isEmpty()) {
            if(hasPrev) {
//                System.out.println("Brak ruchu");
                return move4(!myMove, layer, false, alfa, beta);
            }
            else {
//                System.out.println("Potencjalny koniec na głębokości " + layer);
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

        List<Elem> moves;
//        if(layer <= 2) {   //:D
//            moves = getSortedValidMoves(validMoves, thisPlayer, notThisPlayer, myMove);
//        } else {
//            moves = getNotSortedValidMoves(validMoves, thisPlayer, notThisPlayer, myMove);
//        }
        moves = getNotSortedValidMoves(validMoves, thisPlayer, notThisPlayer, myMove);

        int nextLayer = validMoves.size() > optionsToCut ? layer + 2 : layer + 1;
        Position currentPosition = moves.get(0).position;
        List<Position> toChange = gameGrid.getChangedAfter(currentPosition, thisPlayer);
        gameGrid.moveWithoutShowing(currentPosition, toChange, thisPlayer);
        int best = 0;

        if(myMove) {
            best = move4(false, nextLayer, true, alfa,beta);
            gameGrid.undoMove(currentPosition, toChange, notThisPlayer);

            for(int i = 1; i < validMoves.size(); i++) {
                if(best > alfa) {
                    alfa = best;
                    if(alfa >= beta) return alfa;
                }
                currentPosition = moves.get(i).position;
                toChange = gameGrid.getChangedAfter(currentPosition, thisPlayer);
                gameGrid.moveWithoutShowing(currentPosition, toChange, thisPlayer);
                int current = move4(false, nextLayer, true, alfa, beta);
                gameGrid.undoMove(currentPosition, toChange, notThisPlayer);
                if(current > best) {
                    best = current;
                }
            }
        }
        if(!myMove) {
            best = move4(true, nextLayer, true, alfa, beta);
            gameGrid.undoMove(currentPosition, toChange, notThisPlayer);

            for(int i = 1; i < validMoves.size(); i++) {
                if(best < beta) {
                    beta = best;
                    if(alfa >= beta) return beta;
                }
                currentPosition = moves.get(i).position;
                toChange = gameGrid.getChangedAfter(currentPosition, thisPlayer);
                gameGrid.moveWithoutShowing(currentPosition, toChange, thisPlayer);
                int current = move4(true, nextLayer, true, alfa, beta);
                gameGrid.undoMove(currentPosition, toChange, notThisPlayer);
                if(current < best) {
                    best = current;
                }
            }
        }
        return best;
    }

    public int move3(boolean myMove, int layer, boolean hasPrev, int alfa, int beta) {
        number++;
        int thisPlayer = myMove ? player : otherPlayer;
        List<Position> validMoves = gameGrid.getValidMoves(thisPlayer);
        if(validMoves.isEmpty()) {
            if(hasPrev) {
//                System.out.println("Brak ruchu");
                return move3(!myMove, layer, false, alfa, beta);
            }
            else {
//                System.out.println("Potencjalny koniec na głębokości " + layer);
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

        List<Elem> moves;
        moves = getNotSortedValidMoves(validMoves, thisPlayer, notThisPlayer, myMove);

        int nextLayer = validMoves.size() > optionsToCut ? layer + 2 : layer + 1;
        Position currentPosition = moves.get(0).position;
        List<Position> toChange = gameGrid.getChangedAfter(currentPosition, thisPlayer);
        gameGrid.moveWithoutShowing(currentPosition, toChange, thisPlayer);
        int best = 0;

        if(myMove) {
            best = move3(false, nextLayer, true, alfa,beta);
            gameGrid.undoMove(currentPosition, toChange, notThisPlayer);

            for(int i = 1; i < validMoves.size(); i++) {
                currentPosition = moves.get(i).position;
                toChange = gameGrid.getChangedAfter(currentPosition, thisPlayer);
                gameGrid.moveWithoutShowing(currentPosition, toChange, thisPlayer);
                int current = move3(false, nextLayer, true, alfa, beta);
                gameGrid.undoMove(currentPosition, toChange, notThisPlayer);
                if(current > best) {
                    best = current;
                }
            }
        }
        if(!myMove) {
            best = move3(true, nextLayer, true, alfa, beta);
            gameGrid.undoMove(currentPosition, toChange, notThisPlayer);

            for(int i = 1; i < validMoves.size(); i++) {
                currentPosition = moves.get(i).position;
                toChange = gameGrid.getChangedAfter(currentPosition, thisPlayer);
                gameGrid.moveWithoutShowing(currentPosition, toChange, thisPlayer);
                int current = move3(true, nextLayer, true, alfa, beta);
                gameGrid.undoMove(currentPosition, toChange, notThisPlayer);
                if(current < best) {
                    best = current;
                }
            }
        }
        return best;
    }

    private List<Elem> getSortedValidMoves(List<Position> validMoves, int thisPlayer, int notThisPlayer, boolean myMove) {
        List<Elem> moves = new ArrayList<>(validMoves.size());
        for (Position validMove : validMoves) {
            List<Position> toChange = gameGrid.getChangedAfter(validMove, thisPlayer);
            gameGrid.moveWithoutShowing(validMove, toChange, thisPlayer);
            moves.add(new Elem(heuristicStability(), validMove));
            gameGrid.undoMove(validMove, toChange, notThisPlayer);
        }
        if(myMove) moves.sort((e1, e2) -> Integer.compare(e2.cost, e1.cost));
        else moves.sort((e1, e2) -> Integer.compare(e1.cost, e2.cost));
        return moves;
    }

    private List<Elem> getNotSortedValidMoves(List<Position> validMoves, int thisPlayer, int notThisPlayer, boolean myMove) {
        List<Elem> moves = new ArrayList<>(validMoves.size());
        for (Position validMove : validMoves) {
            moves.add(new Elem(0, validMove));
        }
        return moves;
    }

    private int countScore() {
        int score = 0;
        int[] weight = heuristic.getWeights(maxRoundNumber);
        if(weight[0] != 0) {
            score += weight[0] * countDifference();
        }
        if(weight[1] != 0) {
            score += weight[1] * heuristicInDanger();
        }
        if(weight[2] != 0) {
            score += weight[2] * heuristicOnCorner();
        }
        if(weight[3] != 0) {
            score += weight[3] * heuristicOnBorder();
        }
        if(weight[4] != 0) {
            score += weight[4] * heuristicFieldValue();
        }
        if(weight[5] != 0) {
            score += weight[5] * heuristicStability();
        }
        return score;
    }

    private int getStability(int x, int y, int notThisPlayer) {
        int stability = 2;
        int[] directionsX = {-1, 0, 1};
        int[] directionsY = {-1, 0};
        int[][] board = gameGrid.getBoard();
        for(int dirX : directionsX) {
            for(int dirY : directionsY) {
                if(dirX == 0 && dirY == 0) continue;
                int curX = x + dirX;
                int curY = y + dirY;
                int result1 = 2;
                while(curX >= 0 && curY >= 0 && curX < gameGrid.sizeX && curY < gameGrid.sizeY) {
                    if(board[curX][curY] == notThisPlayer) {
                        result1 = 1;
                        curX = -2;
                    } else if(board[curX][curY] == 0) {
                        result1 = 3;
                        curX = -2;
                    }
                    curX += dirX;
                    curY += dirY;
                }
                curX = x - dirX;
                curY = y - dirY;
                int result2 = 2;
                while(curX >= 0 && curY >= 0 && curX < gameGrid.sizeX && curY < gameGrid.sizeY) {
                    if(board[curX][curY] == notThisPlayer) {
                        result2 = 1;
                        curX = -2;
                    } else if(board[curX][curY] == 0) {
                        result2 = 3;
                        curX = -2;
                    }
                    curX -= dirX;
                    curY -= dirY;
                }
                int result = 2;
                switch (result1 * result2) {
                    case 1 -> result = 1;   // przeciwnik na obu stronach - pole słobo stabilne
                    case 2 -> result = 2;   // przeciwnik i krawędź - pole silnie stabilne
                    case 3 -> result = 0;   // przeciwnik i puste - pole niestabilne
                    case 4 -> result = 2;   // krawędzie na obu stronach - pole silnie stabilne
                    case 6 -> result = 2;   // krawędź i puste - pole silnie stabilne
                    case 9 -> result = 1;   // puste na obu stronach - pole słabo stabilne
                }
                if(result < stability) stability = result;
            }
        }
        return stability;
    }

    private int heuristicStabilityWithoutValue() {
        int score = 0;
        int[][] board = gameGrid.getBoard();
        for(int ix = 0; ix < gameGrid.sizeX; ix++) {
            for(int iy = 0; iy < gameGrid.sizeY; iy++) {
                if(board[ix][iy] != 0) {
                    if(board[ix][iy] == player)
                        score += getStability(ix, iy, player);
                    else
                        score -= getStability(ix, iy, otherPlayer);
                }
            }
        }
        return score;
    }

    private int heuristicStability() {
        int score = 0;
        int[][] board = gameGrid.getBoard();
        int[][] fieldsValues = heuristic.fieldsValues;;
        for(int ix = 0; ix < gameGrid.sizeX; ix++) {
            for(int iy = 0; iy < gameGrid.sizeY; iy++) {
                if(board[ix][iy] != 0) {
                    if(board[ix][iy] == player)
                        score += (getStability(ix, iy, player) - 1) * (fieldsValues[ix][iy] >= 0 ? fieldsValues[ix][iy] : 1);
                    else
                        score -= (getStability(ix, iy, otherPlayer) - 1) * (fieldsValues[ix][iy] >= 0 ? fieldsValues[ix][iy] : 1);
                }
            }
        }
        return score;
    }
}

