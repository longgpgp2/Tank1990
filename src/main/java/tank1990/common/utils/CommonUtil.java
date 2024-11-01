package tank1990.common.utils;

import java.util.ArrayList;
import java.util.Random;

import tank1990.common.classes.Vector2D;

public class CommonUtil {
  /**
   * Convert list into array
   * 
   * @param arrayList - a 2d ArrayList
   * @return 2d array
   */
  public static int[][] arrayListToArray(ArrayList<ArrayList<Integer>> arrayList) {
    int[][] intArray = arrayList.stream().map(u -> u.stream().mapToInt(
        i -> i).toArray()).toArray(int[][]::new);
    return intArray;
  }

  /**
   * Get position based on tile index
   * 
   * @param index      - index of tile
   * @param tileWidth  - tile width
   * @param tileHeight - tile height
   * @return Vector2D which is the position of GameComponent
   */
  public static Vector2D getPositionByIndex(int index, int tileWidth, int tileHeight) {
    int column = (int) index % tileWidth;
    int row = (int) index / tileHeight;

    return new Vector2D(column * tileWidth, row * tileHeight);
  }

  /**
   * Generate randome integer
   * 
   * @param minValue - min value (included)
   * @param maxValue - max value (included)
   * @return
   */
  public static int randomInteger(int minValue, int maxValue) {
    Random random = new Random();
    return minValue + random.nextInt(maxValue - minValue + 1);
  }

  public static double randomDouble(double minValue, double maxValue) {
    Random random = new Random();
    return minValue + random.nextDouble(maxValue - minValue + 1);
  }

  public static Vector2D randomPosition(double x, double y, double width, double height) {
    double randomX = randomDouble(x, x + width);
    double randomY = randomDouble(y, y + height);

    return new Vector2D(randomX, randomY);
  }

  public static <T> boolean isArrayNotFull(T[] array) {
    for (T item : array) {
      if (item == null) {
        return true;
      }
    }

    return false;
  }
}
