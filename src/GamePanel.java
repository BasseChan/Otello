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
    private SimpleAI aiBehaviour;

    public GamePanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.LIGHT_GRAY);

        gameGrid = new GameGrid(new Position(0,0), PANEL_WIDTH, PANEL_HEIGHT-140, 8, 8);
        setGameState(GameState.BTurn);
        chooseAIType();
        addMouseListener(this);
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


    // esc - close app
    // r - restart
    // a - choose AI
    public void handleInput(int keyCode) {
        if(keyCode == KeyEvent.VK_ESCAPE) {
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

            while(gameState == GameState.WTurn && aiBehaviour != null) {
                playTurn(aiBehaviour.moveAI());
                testForEndGame(true);
            }
        }

        repaint();
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

    private void chooseAIType() {
        String[] options = new String[] {"Player vs Player", "Player vs Random AI"};
        String message = "Select the game mode you would like to use.";
        int difficultyChoice = JOptionPane.showOptionDialog(null, message,
                "Choose how to play.",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);
        switch (difficultyChoice) {
            case 0 -> aiBehaviour = null;
            case 1 -> aiBehaviour = new SimpleAI(gameGrid, 2);
        }
    }

    private void moveAI() {
        aiBehaviour.moveAI();
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