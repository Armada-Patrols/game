import java.util.Random;

public class ComputerShooting {
    private final static int MISS = 0;
    private final static int COMPUTER_WON = -2;

    private static double[][] weightsMatrix = new double[10][10];
    private static int difficulty; 
    private static int[][] playerArr;
    private static boolean[][] same = new boolean[10][10];

    static Random rand = new Random();

    public ComputerShooting(int difficulty, int[][] playerArray) {
        ComputerShooting.difficulty = difficulty;
        ComputerShooting.playerArr = playerArray;
        this.arrangeWeights(playerArray, difficulty);
    }

    //Making a weights matrix to choose the spots to shoot.
    public void arrangeWeights(int[][] array, int diffLevel) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (array[i][j] == 0) {
                    weightsMatrix[i][j] = (0.9 / diffLevel);
                } else {
                    weightsMatrix[i][j] = (0.06 * diffLevel);
                }
            }
        }
    }

    public static int[] chooseSpot() {
        double max = 0;
        int row = 0, col = 0;

        int randRow, randCol;

        randRow = rand.nextInt(10);
        randCol = rand.nextInt(10); 

        weightsMatrix[randRow][randCol] += rand.nextDouble(0.1 * (0.25 * difficulty), 1.0 * (0.25 * difficulty)); //Adds more randomness.

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (weightsMatrix[i][j] > max) {
                    max = weightsMatrix[i][j];
                    row = i;
                    col = j;
                }
            }
        }

        int result = Ship.shoot(row, col, playerArr);

        if (result == 2) {
            int[] temp = {2, row, col};
            weightsMatrix[row][col] = -Integer.MAX_VALUE;
            same[row][col] = true;
            return temp; // Miss
        } else if (result == 1 && !same[row][col]) {
            same[row][col] = true;

            if (row > 0) {
                row--;
                weightsMatrix[row][col] += (weightsMatrix[row + 1][col] / 4) * 3;
                row++;
            }
            if (row < 9) {
                row++;
                weightsMatrix[row][col] += (weightsMatrix[row - 1][col] / 4) * 3;
                row--;
            }
            if (col > 0) {
                col--;
                weightsMatrix[row][col] += (weightsMatrix[row][col + 1] / 4) * 3;
                col++;
            }
            if (col < 9) {
                col++;
                weightsMatrix[row][col] += (weightsMatrix[row][col - 1] / 4) * 3;
                col--;
            }
            weightsMatrix[row][col] = -Integer.MAX_VALUE;
            int[] temp = {1, row, col};
            return temp; //Hit.
        }

        int[] temp = {10, -1, -1};
        weightsMatrix[row][col] = -Integer.MAX_VALUE;
        return temp; //Invalid hit.
    }
}