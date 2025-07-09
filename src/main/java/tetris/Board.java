package tetris;

import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;

/**
 * Manages the game board including collision detection and line clearing.
 */
public class Board implements GameLifecycle {
   public final static int ROWS = 20;
   public final static int COLS = 10;
   public final static int CELL_SIZE = Shape.CELL_SIZE;

   private static final Color COLOR_OCCUPIED = Color.LIGHTGRAY;
   private static final Color COLOR_EMPTY = Color.WHITE;

   boolean map[][] = new boolean[ROWS][COLS];
   Shape shape;

   public Board() { }

   @Override
   public void newGame() {
      clearBoard();
      shape = Shape.newShape();
   }

   private void clearBoard() {
      for (int row = 0; row < ROWS; row++) {
         for (int col = 0; col < COLS; col++) {
            map[row][col] = false;
         }
      }
   }

   @Override
   public void startGame() {
      // Implementation handled by GameMain
   }

   @Override
   public void stepGame() {
      stepGame(Action.DOWN);
   }

   @Override
   public void stopGame() {
      // Implementation handled by GameMain
   }

   public boolean stepGame(Action action) {
      switch (action) {
         case LEFT:
            shape.x--;
            if (!actionAllowed()) shape.x++;
            break;
         case RIGHT:
            shape.x++;
            if (!actionAllowed()) shape.x--;
            break;
         case ROTATE_LEFT:
            shape.rotateLeft();
            if (!actionAllowed()) shape.undoRotate();
            break;
         case ROTATE_RIGHT:
            shape.rotateRight();
            if (!actionAllowed()) shape.undoRotate();
            break;
         case DOWN:
            shape.y++;
            if (!actionAllowed()) {
               shape.y--;
               return true; // Lock down needed
            }
            break;
      }
      return false;
   }

   public boolean actionAllowed() {
      for (int shapeRow = 0; shapeRow < shape.rows; shapeRow++) {
         for (int shapeCol = 0; shapeCol < shape.cols; shapeCol++) {
            if (shape.map[shapeRow][shapeCol]) {
               int boardRow = shapeRow + shape.y;
               int boardCol = shapeCol + shape.x;
               
               if (isOutOfBounds(boardRow, boardCol) || isCellOccupied(boardRow, boardCol)) {
                  return false;
               }
            }
         }
      }
      return true;
   }

   private boolean isOutOfBounds(int row, int col) {
      return row < 0 || row >= ROWS || col < 0 || col >= COLS;
   }

   private boolean isCellOccupied(int row, int col) {
      return map[row][col];
   }

   public int lockDown() {
      for (int shapeRow = 0; shapeRow < shape.rows; shapeRow++) {
         for (int shapeCol = 0; shapeCol < shape.cols; shapeCol++) {
            if (shape.map[shapeRow][shapeCol]) {
               this.map[shapeRow + shape.y][shapeCol + shape.x] = true;
            }
         }
      }
      return clearLines();
   }

   public int clearLines() {
      int rowsCleared = 0;
      int currentRow = ROWS - 1;

      while (currentRow >= 0) {
         if (isRowFull(currentRow)) {
            removeRow(currentRow);
            rowsCleared++;
         } else {
            currentRow--;
         }
      }
      return rowsCleared;
   }

   private boolean isRowFull(int row) {
      for (int col = 0; col < COLS; col++) {
         if (!map[row][col]) {
            return false;
         }
      }
      return true;
   }

   private void removeRow(int rowToRemove) {
      for (int row = rowToRemove; row > 0; row--) {
         for (int col = 0; col < COLS; col++) {
            map[row][col] = map[row - 1][col];
         }
      }
      clearTopRow();
   }

   private void clearTopRow() {
      for (int col = 0; col < COLS; col++) {
         map[0][col] = false;
      }
   }

   public boolean isGameOver() {
      for (int col = 0; col < COLS; col++) {
         if (map[0][col]) {
            return true;
         }
      }
      return false;
   }

   public void paint(GraphicsContext gc) {
      for (int row = 0; row < ROWS; row++) {
         for (int col = 0; col < COLS; col++) {
            gc.setFill(map[row][col] ? COLOR_OCCUPIED : COLOR_EMPTY);
            gc.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            gc.strokeRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
         }
      }

      if (shape != null) {
         shape.paint(gc);
      }
   }
} 