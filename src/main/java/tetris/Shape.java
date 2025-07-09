package tetris;

import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;
import java.util.Random;

/**
 * Represents Tetris pieces with rotation and rendering capabilities.
 */
public class Shape {
   public final static int CELL_SIZE = 32;

   private static Shape shape;

   private Shape() { }

   int x, y;
   boolean[][] map;
   int rows, cols;
   int shapeType;
   private boolean[][] savedMap = new boolean[5][5];

   private static final boolean[][][] SHAPES_MAP = {
         {{ false, true,  false },
          { true,  true,  false },
          { true,  false, false }},  // Z
         {{ false, true,  false},
          { false, true,  false},
          { false, true,  true }},  // L
         {{ true, true },
          { true, true }},  // O
         {{ false, true,  false },
          { false, true,  true  },
          { false, false, true  }},   // S
         {{ false, true,  false, false },
          { false, true,  false, false },
          { false, true,  false, false },
          { false, true,  false, false }},  // I
         {{ false, true,  false},
          { false, true,  false},
          { true,  true,  false}},  // J
         {{ false, true,  false },
          { true,  true,  true  },
          { false, false, false }}};  // T

   private static final Color[] SHAPES_COLOR = {
         Color.web("#F52D41"), // Z (Red)
         Color.ORANGE,         // L
         Color.YELLOW,         // O
         Color.GREEN,          // S
         Color.CYAN,           // I
         Color.web("#4CB5F5"), // J (Blue)
         Color.PINK            // T
   };

   private static final Random rand = new Random();

   public static Shape newShape() {
      if(shape == null) {
         shape = new Shape();
      }

      shape.shapeType = rand.nextInt(SHAPES_MAP.length);
      shape.map = SHAPES_MAP[shape.shapeType];
      shape.rows = shape.map.length;
      shape.cols = shape.map[0].length;

      shape.x = ((Board.COLS - shape.cols) / 2);

      shape.y = findInitialYPosition();

      return shape;
   }

   private static int findInitialYPosition() {
      for (int row = 0; row < shape.rows; row++) {
         for (int col = 0; col < shape.cols; col++) {
            if (shape.map[row][col]) {
               return -row;
            }
         }
      }
      return 0;
   }

   public void rotateRight() {
      saveCurrentMap();
      for (int row = 0; row < rows; row++) {
         for (int col = 0; col < cols; col++) {
            map[row][col] = savedMap[cols - 1 - col][row];
         }
      }
   }

   public void rotateLeft() {
      saveCurrentMap();
      for (int row = 0; row < rows; row++) {
         for (int col = 0; col < cols; col++) {
            map[row][col] = savedMap[col][rows - 1 - row];
         }
      }
   }

   public void undoRotate() {
      restoreSavedMap();
   }

   private void saveCurrentMap() {
      for (int row = 0; row < rows; row++) {
         for (int col = 0; col < cols; col++) {
            savedMap[row][col] = map[row][col];
         }
      }
   }

   private void restoreSavedMap() {
      for (int row = 0; row < rows; row++) {
         for (int col = 0; col < cols; col++) {
            map[row][col] = savedMap[row][col];
         }
      }
   }

   public void paint(GraphicsContext gc) {
      gc.setFill(SHAPES_COLOR[this.shapeType]);
      gc.setStroke(Color.BLACK);
      gc.setLineWidth(1);
      
      for (int row = 0; row < rows; row++) {
         for (int col = 0; col < cols; col++) {
            if (map[row][col]) {
               double x = (this.x + col) * CELL_SIZE;
               double y = (this.y + row) * CELL_SIZE;
               
               gc.fillRect(x, y, CELL_SIZE, CELL_SIZE);
               gc.strokeRect(x, y, CELL_SIZE, CELL_SIZE);
            }
         }
      }
   }
} 