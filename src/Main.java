public class Main {
//    public static void main(String[] args) {
//        System.out.println("Hello world!");
//    }


        public static int knapsack(int[] wagi, int[] wartosci, int pojemnosc) {
            int n = wagi.length;
            int[][] dp = new int[n + 1][pojemnosc + 1];
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= pojemnosc; j++) {
                    if (wagi[i - 1] <= j) {
                        dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - wagi[i - 1]] + wartosci[i - 1]);
                    } else {
                        dp[i][j] = dp[i - 1][j];
                    }
                }
            }
            return dp[n][pojemnosc];
        }

        public static void main(String[] args) {
//            int[] wagi = {2, 4, 6, 1, 5, 3, 7, 9, 2, 4, 6, 8, 3, 5, 7, 1, 2, 4, 6, 8};
//            int[] wartosci = {3, 5, 7, 2, 8, 4, 6, 9, 1, 5, 7, 8, 3, 4, 6, 2, 1, 5, 8, 9};
//            int pojemnosc = 50;
//            System.out.println(knapsack(wagi, wartosci, pojemnosc)); // output: 11
//            int[] b = {1,1,1};
//            a(b);
//            System.out.println(b[0]);
//            String a = "a";
//            a(a);
//            System.out.println(a);
//            int i = 3;
//            for(int i = 0; i<4; i++) {
//                System.out.println(i);
//            }

        }
        static void a(String b) {
            b+="a";
        }
        static void a(int[] b) {
            b[0] = 4;
        }

    public int stability(char[][] board, char player) {
        int stabilityScore = 0;
        int boardSize = board.length;
        int[][] stableDiscs = new int[boardSize][boardSize];
        int[] directions = {-1, 0, 1};

        // Sprawdzenie stabilności pionków dla każdego pola na planszy
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == player) {
                    for (int xDirection : directions) {
                        for (int yDirection : directions) {
                            if (xDirection == 0 && yDirection == 0) {
                                continue;
                            }
                            int x = i + xDirection;
                            int y = j + yDirection;
                            boolean isStable = true;

                            // Sprawdzenie stabilności pionka w kierunku danym przez xDirection i yDirection
                            while (x >= 0 && x < boardSize && y >= 0 && y < boardSize && isStable) {
                                if (board[x][y] == '.') {
                                    isStable = false;
                                } else if (board[x][y] != player) {
                                    // przeciwnik ma pionek w tej pozycji - pionek nie jest stabilny
                                    isStable = false;
                                }
                                x += xDirection;
                                y += yDirection;
                            }

                            if (isStable) {
                                // Zaznaczamy każdy stabilny pionek na planszy i zwiększamy wynik stabilności gracza
                                x = i + xDirection;
                                y = j + yDirection;
                                while (x >= 0 && x < boardSize && y >= 0 && y < boardSize) {
                                    stableDiscs[x][y]++;
                                    x += xDirection;
                                    y += yDirection;
                                }
                                stabilityScore++;
                            }
                        }
                    }
                }
            }
        }

        // Sumujemy wynik stabilności dla wszystkich stabilnych pionków gracza
        int playerStability = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == player && stableDiscs[i][j] > 0) {
                    playerStability += stableDiscs[i][j];
                }
            }
        }

        return playerStability - stabilityScore;
    }

//public class Main {
//    public static void main(String[] args) {
//
//    }
//}
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


//    private int countScore1() {
//        if(gameGrid.getMoveNumber() > 50) {
//            return countDifference() + 10 * countInDanger() + 20 * countOnCorners() + 4 * countOnBorder();
//        }
//        if(gameGrid.getMoveNumber() > 30) {
//            return countDifference() + 4 * countInDanger() + 500 * countOnCorners() + 10 * countOnBorder();
//        }
//        return countInDanger() + 200 * countOnCorners() + 10 * countOnBorder();
//    }

//    private int countScore2() {
//        int score = 0;
//        int group = 0;
//        while(group < groupsBorders.length && groupsBorders[group] < gameGrid.getMoveNumber()) {
//            group++;
//        }
//        if(heuristicWeights[group][0] != 0) {
//            score += heuristicWeights[group][0] * countDifference();
//        }
//        if(heuristicWeights[group][1] != 0) {
//            score += heuristicWeights[group][1] * countInDanger();
//        }
//        if(heuristicWeights[group][2] != 0) {
//            score += heuristicWeights[group][2] * countOnCorners();
//        }
//        if(heuristicWeights[group][3] != 0) {
//            score += heuristicWeights[group][3] * countOnBorder();
//        }
//        if(heuristicWeights[group][4] != 0) {
//            score += heuristicWeights[group][4] * heuristicFieldValue(group);
//        }
//        return score;
//    }

//    private int heuristicStablePieces() {
//        int score = 0;
//        int[][] board = gameGrid.getBoard();
//        for(int ix = 0; ix < gameGrid.sizeX; ix++) {
//            for(int iy = 0; iy < gameGrid.sizeY; iy++) {
//                if(board[ix][iy] != 0) {
//                    boolean stabile = true;
//
//                    if(board[ix][iy] == player)
//                        score += fieldsValue[gameState][ix][iy];
//                    else
//                        score -= fieldsValue[gameState][ix][iy];
//                }
//            }
//        }
//        return score;
//    }


//    private int heuristicFieldValue(int gameState) {
//        int score = 0;
//        int[][] board = gameGrid.getBoard();
//        for(int ix = 0; ix < gameGrid.sizeX; ix++) {
//            for(int iy = 0; iy < gameGrid.sizeY; iy++) {
//                if(board[ix][iy] != 0) {
//                    if(board[ix][iy] == player)
//                        score += fieldsValue[gameState][ix][iy];
//                    else
//                        score -= fieldsValue[gameState][ix][iy];
//                }
//            }
//        }
//        return score;
//    }