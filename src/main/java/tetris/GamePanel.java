package tetris;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Handles rendering of the game board and pieces using JavaFX Canvas.
 */
public class GamePanel extends Canvas {
    
    private Board board;
    
    public static final int CANVAS_WIDTH = Board.COLS * Board.CELL_SIZE;
    public static final int CANVAS_HEIGHT = Board.ROWS * Board.CELL_SIZE;
    
    public GamePanel(Board board) {
        super(CANVAS_WIDTH, CANVAS_HEIGHT);
        this.board = board;
        setFocusTraversable(true);
        paintComponent();
    }
    
    public void paintComponent() {
        GraphicsContext gc = getGraphicsContext2D();
        
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, getWidth(), getHeight());
        
        if (board != null) {
            board.paint(gc);
        }
        
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(0, 0, getWidth(), getHeight());
    }
    
    public void setBoard(Board board) {
        this.board = board;
    }
    
    public Board getBoard() {
        return board;
    }
    
    public void repaint() {
        paintComponent();
    }
} 