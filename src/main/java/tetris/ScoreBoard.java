package tetris;

import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

/**
 * Manages game scoring and display.
 */
public class ScoreBoard implements GameLifecycle {
    
    private static final int POINTS_PER_LINE = 10;
    
    private int score;
    private int linesCleared;
    
    private Text scoreText;
    private Text linesText;
    
    public ScoreBoard() {
        initializeUI();
        newGame();
    }
    
    private void initializeUI() {
        Font font = Font.font("Arial", 16);
        
        scoreText = new Text("Score: 0");
        scoreText.setFont(font);
        scoreText.setFill(Color.BLACK);
        
        linesText = new Text("Lines: 0");
        linesText.setFont(font);
        linesText.setFill(Color.BLACK);
    }
    
    @Override
    public void newGame() {
        score = 0;
        linesCleared = 0;
        updateDisplay();
    }
    
    @Override
    public void startGame() {
    }
    
    @Override
    public void stepGame() {
    }
    
    @Override
    public void stopGame() {
    }
    
    public void updateScore(int rowsCleared) {
        if (rowsCleared > 0) {
            score += rowsCleared * POINTS_PER_LINE;
            linesCleared += rowsCleared;
            updateDisplay();
        }
    }
    
    private void updateDisplay() {
        scoreText.setText("Score: " + score);
        linesText.setText("Lines: " + linesCleared);
    }
    
    public int getScore() {
        return score;
    }
    
    public Text getScoreText() {
        return scoreText;
    }
    
    public Text getLinesText() {
        return linesText;
    }
    
    public long getFallSpeed() {
        return 500;
    }
} 