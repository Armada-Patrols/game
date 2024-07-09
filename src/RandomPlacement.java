import java.util.Random;

public class RandomPlacement {
    private final static int VERTICAL = 0; 
    private final static int N = 10; 
    private final static int NUM_OF_SHIPS = 5; //Number of ships to place.

    private static int[][] computerGrid;

    public static void setComputerGrid(int[][] computerGrid) {
        RandomPlacement.computerGrid = computerGrid;
    }

    static int[] sizes = {5, 4, 3, 3, 2}; 

    //Method to randomly place ships on the grid.
    public static int[][] placingShips() {
        Random rand = new Random();
        int limit; 
        int direction; 
        int row, col; 

        for (int k = 0; k < NUM_OF_SHIPS; k++) {
            boolean isOverlap = true;
            while (isOverlap) {
                try {
                    limit = N - 1 - (sizes[k] - 1); 
                    direction = rand.nextInt(2); 
                    if (direction == VERTICAL) {
                        row = rand.nextInt(limit + 1);
                        col = rand.nextInt(N);
                        //Check for overlap in vertical placement.
                        for (int i = 0; i < sizes[k]; i++) {
                            if (computerGrid[row + i][col] != 0) {
                                throw new Exception();
                            }
                        }
                        //Place the ship vertically.
                        for (int i = 0; i < sizes[k]; i++) {
                            computerGrid[row + i][col] = 1;
                        }
                    } else {
                        row = rand.nextInt(N);
                        col = rand.nextInt(limit + 1);
                        //Check for overlap in horizontal placement.
                        for (int i = 0; i < sizes[k]; i++) {
                            if (computerGrid[row][col + i] != 0) {
                                throw new Exception();
                            }
                        }
                        //Place the ship horizontally.
                        for (int i = 0; i < sizes[k]; i++) {
                            computerGrid[row][col + i] = 1;
                        }
                    }
                    isOverlap = false; //Ship placed successfully.
                } catch (Exception e) {
                    //Start again.
                }
            }
        }
        return computerGrid;
    }
}
