package tetris;

/**
 * Interface for components that manage game lifecycle events.
 */
public interface GameLifecycle {
    public void newGame();
    public void startGame();
    public void stepGame();
    public void stopGame();
} 