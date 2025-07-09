package tetris;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Handles keyboard input and converts to game actions.
 */
public class InputHandler {
    
    public interface ActionListener {
        void onAction(Action action);
    }
    
    private ActionListener actionListener;
    
    public InputHandler(ActionListener actionListener) {
        this.actionListener = actionListener;
    }
    
    public void handleKeyPressed(KeyEvent event) {
        KeyCode code = event.getCode();
        Action action = mapKeyToAction(code);
        
        if (action != null && actionListener != null) {
            actionListener.onAction(action);
        }
        
        event.consume();
    }
    
    private Action mapKeyToAction(KeyCode code) {
        switch (code) {
            case A:
            case LEFT:
                return Action.LEFT;
            case D:
            case RIGHT:
                return Action.RIGHT;
            case S:
            case DOWN:
                return Action.DOWN;
            case Q:
                return Action.ROTATE_LEFT;
            case E:
                return Action.ROTATE_RIGHT;
            default:
                return null;
        }
    }
} 