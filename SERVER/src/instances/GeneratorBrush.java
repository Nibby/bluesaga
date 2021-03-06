package instances;

import java.awt.Point;
import java.util.HashMap;

public class GeneratorBrush {

  public static HashMap<Point, Integer> getCaveBrush() {

    // 1 = deep water
    // 2 = shallow water
    // 3 = beach
    // 4 = grass
    /*
    int[][] brush = new int[][]{
    		{ 0, 0, 2, 2, 2, 2, 0, 0 },
    		{ 0, 2, 2, 1, 1, 2, 2, 0 },
    		{ 2, 2, 1, 1, 1, 1, 2, 2 },
    		{ 2, 1, 1, 1, 1, 1, 1, 2 },
    		{ 2, 1, 1, 1, 1, 1, 1, 2 },
    		{ 2, 2, 1, 1, 1, 1, 2, 2 },
    		{ 0, 2, 2, 1, 1, 2, 2, 0 },
    		{ 0, 0, 2, 2, 2, 2, 0, 0 }
    	};
    */

    int[][] brush =
        new int[][] {
          {0, 2, 2, 2, 0}, {2, 2, 1, 2, 2}, {2, 1, 1, 1, 2}, {2, 2, 1, 2, 2}, {0, 2, 2, 2, 0},
        };

    HashMap<Point, Integer> points = new HashMap<Point, Integer>();

    int brushWidth = brush.length;
    int brushHeight = brush[0].length;

    for (int i = 0; i < brushWidth; i++) {
      for (int j = 0; j < brushHeight; j++) {
        if (brush[i][j] != 0) {
          points.put(new Point(i, j), brush[i][j]);
        }
      }
    }

    return points;
  }

  public static HashMap<Point, Integer> getHeightBrush() {

    // 5 = grass cliff
    // 6 = grass on top, to be replaced with 4 grass

    int[][] brush =
        new int[][] {
          {0, 4, 4, 4, 4, 4, 0},
          {4, 4, 5, 5, 5, 4, 4},
          {4, 5, 5, 6, 5, 5, 4},
          {4, 5, 6, 6, 6, 5, 4},
          {4, 5, 5, 6, 5, 5, 4},
          {4, 4, 5, 5, 5, 4, 4},
          {0, 4, 4, 4, 4, 4, 0},
        };

    HashMap<Point, Integer> points = new HashMap<Point, Integer>();

    int brushWidth = brush.length;
    int brushHeight = brush[0].length;

    for (int i = 0; i < brushWidth; i++) {
      for (int j = 0; j < brushHeight; j++) {
        points.put(new Point(i, j), brush[i][j]);
      }
    }

    return points;
  }

  public static HashMap<Point, Integer> getIslandBrush() {

    // 1 = deep water
    // 2 = shallow water
    // 3 = beach
    // 4 = grass
    /*
    int[][] brush = new int[][]{
    		{ 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0 },
    		{ 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0 },
    		{ 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0 },
    		{ 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 0, 0, 0 },
    		{ 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 0, 0 },
    		{ 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 0 },
    		{ 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1, 1, 0 },
    		{ 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1, 1 },
    		{ 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1 },
    		{ 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 4, 3, 3, 3, 3, 2, 2, 2, 1, 1, 1 },
    		{ 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4, 3, 3, 3, 2, 2, 2, 1, 1, 1 },
    		{ 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 4, 3, 3, 3, 3, 2, 2, 2, 1, 1, 1 },
    		{ 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1 },
    		{ 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1, 1 },
    		{ 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1, 1, 0 },
    		{ 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 0 },
    		{ 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 0, 0 },
    		{ 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 0, 0, 0 },
    		{ 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0 },
    		{ 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0 },
    		{ 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0 }
    	};
     */
    int[][] brush =
        new int[][] {
          {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0},
          {0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 0, 0, 0},
          {0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 0, 0},
          {0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 0},
          {0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1, 1, 0},
          {1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1, 1},
          {1, 1, 1, 2, 2, 2, 2, 3, 3, 4, 4, 4, 4, 3, 3, 2, 2, 2, 2, 1, 1, 1},
          {1, 1, 1, 2, 2, 2, 3, 3, 4, 4, 4, 4, 4, 4, 3, 3, 2, 2, 2, 1, 1, 1},
          {1, 1, 1, 2, 2, 2, 3, 3, 4, 4, 4, 4, 4, 4, 3, 3, 2, 2, 2, 1, 1, 1},
          {1, 1, 1, 2, 2, 2, 3, 3, 4, 4, 4, 4, 4, 4, 3, 3, 2, 2, 2, 1, 1, 1},
          {1, 1, 1, 2, 2, 2, 3, 3, 4, 4, 4, 4, 4, 4, 3, 3, 2, 2, 2, 1, 1, 1},
          {1, 1, 1, 2, 2, 2, 2, 3, 3, 4, 4, 4, 4, 3, 3, 2, 2, 2, 2, 1, 1, 1},
          {1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1, 1},
          {0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1, 1, 0},
          {0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 0},
          {0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 0, 0},
          {0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 0, 0, 0},
          {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0}
        };
    /*
    int[][] brush = new int[][]{
    		{ 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0 },
    		{ 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0 },
    		{ 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1, 1, 0, 0, 0, 0 },
    		{ 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1, 1, 0, 0, 0, 0 },
    		{ 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 0, 0 },
    		{ 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 0, 0 },
    		{ 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1, 1 },
    		{ 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1, 1 },
    		{ 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 1, 1 },
    		{ 1, 1, 2, 2, 2, 2, 3, 3, 3, 4, 4, 3, 3, 3, 2, 2, 2, 2, 1, 1 },
    		{ 1, 1, 2, 2, 2, 2, 3, 3, 3, 4, 4, 3, 3, 3, 2, 2, 2, 2, 1, 1 },
    		{ 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 1, 1 },
    		{ 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1, 1 },
    		{ 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1, 1 },
    		{ 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 2, 2, 2, 1, 1, 1, 1, 0, 0 },
    		{ 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 0, 0 },
    		{ 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1, 1, 0, 0, 0, 0 },
    		{ 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1, 1, 0, 0, 0, 0 },
    		{ 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0 },
    		{ 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0 }
    };
    */

    HashMap<Point, Integer> points = new HashMap<Point, Integer>();

    int brushWidth = brush.length;
    int brushHeight = brush[0].length;

    for (int i = 0; i < brushWidth; i++) {
      for (int j = 0; j < brushHeight; j++) {
        if (brush[i][j] != 0) {
          points.put(new Point(i, j), brush[i][j]);
        }
      }
    }

    return points;
  }

  public static HashMap<Point, Integer> getWaterBrush() {

    // 1 = deep water
    // 2 = shallow water
    // 3 = grass
    int[][] brush =
        new int[][] {
          {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0},
          {0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0},
          {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0},
          {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
          {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
          {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
          {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
          {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
          {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
          {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
          {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
          {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
          {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
          {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
          {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0},
          {0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0},
          {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0}
        };

    HashMap<Point, Integer> points = new HashMap<Point, Integer>();

    int brushWidth = brush.length;
    int brushHeight = brush[0].length;

    for (int i = 0; i < brushWidth; i++) {
      for (int j = 0; j < brushHeight; j++) {
        if (brush[i][j] != 0) {
          points.put(new Point(i, j), brush[i][j]);
        }
      }
    }

    return points;
  }

  public static HashMap<Point, Integer> getShallowBrush() {

    // 1 = deep water
    // 2 = shallow water
    // 3 = grass
    int[][] brush =
        new int[][] {
          {0, 0, 0, 0, 0, 0, 0},
          {0, 0, 2, 2, 2, 0, 0},
          {0, 2, 2, 2, 2, 2, 0},
          {0, 2, 2, 2, 2, 2, 0},
          {0, 2, 2, 2, 2, 2, 0},
          {0, 0, 2, 2, 2, 0, 0},
          {0, 0, 0, 0, 0, 0, 0},
        };

    HashMap<Point, Integer> points = new HashMap<Point, Integer>();

    int brushWidth = brush.length;
    int brushHeight = brush[0].length;

    for (int i = 0; i < brushWidth; i++) {
      for (int j = 0; j < brushHeight; j++) {
        if (brush[i][j] != 0) {
          points.put(new Point(i, j), brush[i][j]);
        }
      }
    }

    return points;
  }
}
