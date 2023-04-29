import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class GamePanel extends JPanel implements MouseListener {
    public enum GameState {WTurn,BTurn,Draw,WWins,BWins}
    private static final int PANEL_HEIGHT = 640;
    private static final int PANEL_WIDTH = 500;
    private final GameGrid gameGrid;
    private GameState gameState;
    private String gameStateStr;
    private AI aiBehaviour;
    private AI playerBlackAI;
//    private static final int[][][] fields1 = {
//            { // Faza początkowa
//                    {20, -10, 10, 5, 5, 10, -10, 20},
//                    {-10, -20, 1, 1, 1, 1, -20, -10},
//                    {10, 1, 3, 2, 2, 3, 1, 10},
//                    {5, 1, 2, 1, 1, 2, 1, 5},
//                    {5, 1, 2, 1, 1, 2, 1, 5},
//                    {10, 1, 3, 2, 2, 3, 1, 10},
//                    {-10, -20, 1, 1, 1, 1, -20, -10},
//                    {20, -10, 10, 5, 5, 10, -10, 20}
//            },
//            { // Faza środkowa
//                    {100, -50, 10, 5, 5, 10, -50, 100},
//                    {-50, -75, -5, -5, -5, -5, -75, -50},
//                    {10, -5, 2, 1, 1, 2, -5, 10},
//                    {5, -5, 1, 0, 0, 1, -5, 5},
//                    {5, -5, 1, 0, 0, 1, -5, 5},
//                    {10, -5, 2, 1, 1, 2, -5, 10},
//                    {-50, -75, -5, -5, -5, -5, -75, -50},
//                    {100, -50, 10, 5, 5, 10, -50, 100}
//            },
//            { // Faza końcowa
//                    {500, -250, 50, 20, 20, 50, -250, 500},
//                    {-250, -350, -50, -50, -50, -50, -350, -250},
//                    {50, -50, 10, 5, 5, 10, -50, 50},
//                    {20, -50, 5, 1, 1, 5, -50, 20},
//                    {20, -50, 5, 1, 1, 5, -50, 20},
//                    {50, -50, 10, 5, 5, 10, -50, 50},
//                    {-250, -350, -50, -50, -50, -50, -350, -250},
//                    {500, -250, 50, 20, 20, 50, -250, 500}
//            }
//    };
    private static final int[] groupsBorders1 = {30,50};
    private static final int[] groupsBorders2 = {30,50};
    private static final int[] groupsBorders3 = {30,50};
    private static final int[] groupsBorders4 = {30,50};
    private static final int[] groupsBorders5 = {30,50};
    private static final int[] groupsBorders6 = {30,55};

    // różnica (max 64), zagrożone (max 56), rogi (max 4), krawędzie (max 24), wartość pól (dużo), stabilne (max 128)
    private static final int[][] heuristicWeights1 = {{0,1,0,0,1,0},{0,3,0,0,1,0},{1,2,0,0,1,0}};
    private static final int[][] heuristicWeights2 = {{0,3,0,0,1,5},{0,5,0,0,1,5},{0,10,0,0,1,10}};
    private static final int[][] heuristicWeights3 = {{0,0,0,0,1,0},{0,0,0,0,1,0},{0,0,0,0,1,0}};
    private static final int[][] heuristicWeights4 = {{0,1,200,10,50,1000},{0,3,200,20,100,1000},{0,3,200,20,100,1000}};
    private static final int[][] heuristicWeights5 = {{0,1,0,0,1,10},{0,3,0,0,1,20},{1,2,0,0,1,50}};
//    private static final int[][] heuristicWeights6 = {{0,0,0,0,0,3},{0,3,100,0,2,5},{1,3,0,0,1,10}};
private static final int[][] heuristicWeights6 = {{0,0,0,0,0,3},{0,0,0,0,0,5},{0,0,0,0,0,5}};
    private static final int[][] fieldsValues1 = {
            {100, -50, 10, 5, 5, 10, -50, 100},
            {-50, -75, -5, -5, -5, -5, -75, -50},
            {10, -5, 2, 1, 1, 2, -5, 10},
            {5, -5, 1, 0, 0, 1, -5, 5},
            {5, -5, 1, 0, 0, 1, -5, 5},
            {10, -5, 2, 1, 1, 2, -5, 10},
            {-50, -75, -5, -5, -5, -5, -75, -50},
            {100, -50, 10, 5, 5, 10, -50, 100}
    };
    private static final int[][] fieldsValues2 = {
            {500, -250, 50, 20, 20, 50, -250, 500},
            {-250, -350, -50, -50, -50, -50, -350, -250},
            {50, -50, 10, 5, 5, 10, -50, 50},
            {20, -50, 5, 1, 1, 5, -50, 20},
            {20, -50, 5, 1, 1, 5, -50, 20},
            {50, -50, 10, 5, 5, 10, -50, 50},
            {-250, -350, -50, -50, -50, -50, -350, -250},
            {500, -250, 50, 20, 20, 50, -250, 500}
    };
    private static final int[][] fieldsValues3 = {
            {20, -10, 10, 5, 5, 10, -10, 20},
            {-10, -20, 1, 1, 1, 1, -20, -10},
            {10, 1, 3, 2, 2, 3, 1, 10},
            {5, 1, 2, 1, 1, 2, 1, 5},
            {5, 1, 2, 1, 1, 2, 1, 5},
            {10, 1, 3, 2, 2, 3, 1, 10},
            {-10, -20, 1, 1, 1, 1, -20, -10},
            {20, -10, 10, 5, 5, 10, -10, 20}
    };
    private static final int[][] fieldsValues4 = {
            {120, -20, 20, 5, 5, 20, -20, 120},
            {-20, -40, -5, -5, -5, -5, -40, -20},
            {20, -5, 15, 3, 3, 15, -5, 20},
            {5, -5, 3, 3, 3, 3, -5, 5},
            {5, -5, 3, 3, 3, 3, -5, 5},
            {20, -5, 15, 3, 3, 15, -5, 20},
            {-20, -40, -5, -5, -5, -5, -40, -20},
            {120, -20, 20, 5, 5, 20, -20, 120}
    };
    private static final int[][] fieldsValues5 = {
            {100, -50, 10, 5, 5, 10, -50, 100},
            {-50, -75, 3, -5, -5, 3, -75, -50},
            {10, 3, 5, 1, 1, 5, 3, 10},
            {5, -5, 1, 0, 0, 1, -5, 5},
            {5, -5, 1, 0, 0, 1, -5, 5},
            {10, 3, 5, 1, 1, 5, 3, 10},
            {-50, -75, 3, -5, -5, 3, -75, -50},
            {100, -50, 10, 5, 5, 10, -50, 100}
    };
    private static final int[][] fieldsValues6 = {
            {500, -10, 50, 10, 10, 50, -10, 500},
            {-10, -20, 5, 1, 1, 5, -20, -10},
            {50, 5, 15, 2, 2, 15, 5, 50},
            {10, 1, 2, 1, 1, 2, 1, 10},
            {10, 1, 2, 1, 1, 2, 1, 10},
            {50, 5, 15, 2, 2, 15, 5, 50},
            {-10, -20, 5, 1, 1, 5, -20, -10},
            {500, -10, 50, 10, 10, 50, -10, 500}
    };

    private static final int[][] startBoard = {
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 2, 1, 0, 0, 0},
            {0, 0, 0, 1, 2, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0}
    };

    public static Heuristic heuristic1 = new Heuristic(groupsBorders1,heuristicWeights1,fieldsValues1);
    public static Heuristic heuristic2 = new Heuristic(groupsBorders2,heuristicWeights2,fieldsValues2);
    public static Heuristic heuristic3 = new Heuristic(groupsBorders3,heuristicWeights3,fieldsValues3);
    public static Heuristic heuristic4 = new Heuristic(groupsBorders4,heuristicWeights4,fieldsValues4);
    public static Heuristic heuristic5 = new Heuristic(groupsBorders5,heuristicWeights5,fieldsValues5);
    public static Heuristic heuristic6 = new Heuristic(groupsBorders6,heuristicWeights6,fieldsValues6);

    public GamePanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.LIGHT_GRAY);

        gameGrid = new GameGrid(new Position(0,0), PANEL_WIDTH, PANEL_HEIGHT-140, 8, 8);
        setGameState(GameState.BTurn);
        chooseAIType();
        addMouseListener(this);
    }

    private void chooseAIType() {
        String[] options = new String[] {"PvP", "PvAI", "AIvAI"};
        String message = "Select the game mode you would like to use.";
        int difficultyChoice = JOptionPane.showOptionDialog(null, message,
                "Choose how to play.",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);
        switch (difficultyChoice) {
            case 0 -> {
                aiBehaviour = null;
                playerBlackAI = null;
            }
            case 1 -> {
                aiBehaviour = new AI(gameGrid, 2, heuristic1);
                playerBlackAI = null;
            }
            case 2 -> {
                aiBehaviour = new AI(gameGrid, 2, heuristic1);
                playerBlackAI = new AI(gameGrid, 1, heuristic6);
            }
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        gameGrid.paint(g);
        drawGameState(g);
    }

    public void restart() {
        gameGrid.reset();
        setGameState(GameState.BTurn);
    }

    // enter - AI move
    // esc - close app
    // R - restart
    // A - choose AI
    public void handleInput(int keyCode) {
        if(keyCode == KeyEvent.VK_ENTER) {
            moveAI();
        }
        else if(keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        } else if(keyCode == KeyEvent.VK_R) {
            restart();
            repaint();
        } else if(keyCode == KeyEvent.VK_A) {
            chooseAIType();
        }
    }

    private void playTurn(Position gridPosition) {
        if(!gameGrid.isValidMove(gridPosition)) {
            return;
        } else if(gameState == GameState.BTurn) {
            gameGrid.playMove(gridPosition, 1);
            setGameState(GameState.WTurn);
        } else if(gameState == GameState.WTurn) {
            gameGrid.playMove(gridPosition, 2);
            setGameState(GameState.BTurn);
        }
    }

    private void setGameState(GameState newState) {
        gameState = newState;
        switch (gameState) {
            case WTurn:
                if(gameGrid.getAllValidMoves().size() > 0) {
                    gameStateStr = "White Player Turn";
                } else {
                    gameGrid.updateValidMoves(1);
                    if(gameGrid.getAllValidMoves().size() > 0) {
                        setGameState(GameState.BTurn);
                    } else {
                        testForEndGame(false);
                    }
                }
                break;
            case BTurn:
                if(gameGrid.getAllValidMoves().size() > 0) {
                    gameStateStr = "Black Player Turn";
                } else {
                    gameGrid.updateValidMoves(2);
                    if(gameGrid.getAllValidMoves().size() > 0) {
                        setGameState(GameState.WTurn);
                    } else {
                        testForEndGame(false);
                    }
                }
                break;
            case WWins: gameStateStr = "White Player Wins! Press R."; break;
            case BWins: gameStateStr = "Black Player Wins! Press R."; break;
            case Draw: gameStateStr = "Draw! Press R."; break;
        }
        drawBoardToConsole();
    }

    private void testForEndGame(boolean stillValidMoves) {
        int gameResult = gameGrid.getWinner(stillValidMoves);
        if(gameResult == 1) {
            setGameState(GameState.BWins);
        } else if(gameResult == 2) {
            setGameState(GameState.WWins);
        } else if(gameResult == 3) {
            setGameState(GameState.Draw);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(gameState == GameState.WTurn || gameState == GameState.BTurn) {
            Position gridPosition = gameGrid.convertMouseToGridPosition(new Position(e.getX(), e.getY()));
            playTurn(gridPosition);
            testForEndGame(true);

//            while(gameState == GameState.WTurn && aiBehaviour != null) {
//                playTurn(aiBehaviour.moveAI());
//                testForEndGame(true);
//            }
            repaint();
        }
    }

    private void drawGameState(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 35));
        int strWidth = g.getFontMetrics().stringWidth(gameStateStr);
        g.drawString(gameStateStr, PANEL_WIDTH/2-strWidth/2, PANEL_HEIGHT-90);

        int[] fieldsCount = gameGrid.getFieldsCount();
        String s = String.format("\nblack: %02d         white: %02d", fieldsCount[1], fieldsCount[2]);
        strWidth = g.getFontMetrics().stringWidth(s);
        g.drawString(s, PANEL_WIDTH/2-strWidth/2, PANEL_HEIGHT-30);
    }


    private void drawBoardToConsole() {
        gameGrid.drawBoardToConsole();
    }

    private void moveAI() {
        if(gameState == GameState.BTurn && playerBlackAI != null) {
            playTurn(playerBlackAI.moveAI());
            testForEndGame(true);
            repaint();
        }
        else if (gameState == GameState.WTurn && aiBehaviour != null) {
            playTurn(aiBehaviour.moveAI());
            testForEndGame(true);
            repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}