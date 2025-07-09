package tetris;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;

/**
 * Main application class that coordinates all game components and manages the game loop.
 */
public class GameMain extends Application implements GameLifecycle {
    
    private Board board;
    private ScoreBoard scoreBoard;
    private GamePanel gamePanel;
    private InputHandler inputHandler;
    
    private State currentState;
    
    private AnimationTimer gameTimer;
    private long lastFallTime;
    private long fallSpeed;
    
    private Stage primaryStage;
    private BorderPane root;
    private VBox sidePanel;
    private Button newGameButton;
    private Label gameStatusLabel;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        initializeGame();
        setupUI();
        setupEventHandlers();
        
        primaryStage.setTitle("Tetris - JavaFX");
        primaryStage.setResizable(false);
        primaryStage.show();
        
        startGameLoop();
    }
    
    private void initializeGame() {
        board = new Board();
        scoreBoard = new ScoreBoard();
        gamePanel = new GamePanel(board);
        inputHandler = new InputHandler(this::processAction);
        
        currentState = State.INITIALIZED;
        fallSpeed = scoreBoard.getFallSpeed();
        lastFallTime = System.nanoTime();
    }
    
    private void setupUI() {
        root = new BorderPane();
        setupSidePanel();
        
        root.setCenter(gamePanel);
        root.setRight(sidePanel);
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        gamePanel.requestFocus();
    }
    
    private void setupSidePanel() {
        sidePanel = new VBox(10);
        sidePanel.setPrefWidth(200);
        sidePanel.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0;");
        
        sidePanel.getChildren().addAll(
            createTitleLabel(),
            createGameStatusLabel(),
            createNewGameButton(),
            createSpacer(),
            scoreBoard.getScoreText(),
            scoreBoard.getLinesText(),
            createSpacer(),
            createInstructionsLabel()
        );
    }

    private Label createTitleLabel() {
        Label titleLabel = new Label("TETRIS");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        return titleLabel;
    }

    private Label createGameStatusLabel() {
        gameStatusLabel = new Label("Ready to Play!");
        gameStatusLabel.setFont(Font.font("Arial", 14));
        return gameStatusLabel;
    }

    private Button createNewGameButton() {
        newGameButton = new Button("New Game");
        newGameButton.setPrefWidth(120);
        newGameButton.setOnAction(e -> newGame());
        return newGameButton;
    }

    private Label createInstructionsLabel() {
        Label instructionsLabel = new Label(
            "Controls:\n" +
            "A/D - Move Left/Right\n" +
            "S - Move Down\n" +
            "Q/E - Rotate Left/Right"
        );
        instructionsLabel.setFont(Font.font("Arial", 12));
        return instructionsLabel;
    }

    private Label createSpacer() {
        return new Label("");
    }
    
    private void setupEventHandlers() {
        primaryStage.getScene().setOnKeyPressed(inputHandler::handleKeyPressed);
        primaryStage.getScene().getRoot().setFocusTraversable(true);
    }
    

    
    private void processAction(Action action) {
        if (currentState != State.PLAYING) {
            return;
        }
        
        boolean lockDown = board.stepGame(action);
        
        if (lockDown) {
            handleLockDown();
        }
        
        gamePanel.repaint();
    }
    
    private void handleLockDown() {
        int rowsCleared = board.lockDown();
        scoreBoard.updateScore(rowsCleared);
        fallSpeed = scoreBoard.getFallSpeed();
        
        if (board.isGameOver()) {
            gameOver();
            return;
        }
        
        board.shape = Shape.newShape();
    }
    
    private void startGameLoop() {
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (currentState == State.PLAYING && isTimeToFall(now)) {
                    processAction(Action.DOWN);
                    lastFallTime = now;
                }
            }
        };
        gameTimer.start();
    }

    private boolean isTimeToFall(long currentTime) {
        long fallSpeedInNanos = fallSpeed * 1_000_000; // Convert milliseconds to nanoseconds
        return currentTime - lastFallTime >= fallSpeedInNanos;
    }

    private void gameOver() {
        currentState = State.GAMEOVER;
        gameStatusLabel.setText("Game Over!");
        
        Platform.runLater(() -> {
            gameStatusLabel.setText("Game Over!\nFinal Score: " + scoreBoard.getScore());
        });
    }
    
    @Override
    public void newGame() {
        currentState = State.READY;
        
        board.newGame();
        scoreBoard.newGame();
        
        gameStatusLabel.setText("Ready to Play!");
        
        fallSpeed = scoreBoard.getFallSpeed();
        lastFallTime = System.nanoTime();
        
        gamePanel.repaint();
        
        Platform.runLater(() -> startGame());
    }
    
    @Override
    public void startGame() {
        if (currentState == State.READY) {
            currentState = State.PLAYING;
            gameStatusLabel.setText("Playing");
        }
    }
    
    @Override
    public void stepGame() {
        if (currentState == State.PLAYING) {
            processAction(Action.DOWN);
        }
    }
    
    @Override
    public void stopGame() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        currentState = State.GAMEOVER;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
} 