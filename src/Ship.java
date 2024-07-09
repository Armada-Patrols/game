
public abstract class Ship {
	private int size;
	private int lifePoints;
	
	
	public Ship(int size, int lifePoints) {
		this.size = size;
		this.lifePoints = lifePoints;
	}
	
	public static int shoot(int xAxis, int yAxis, int[][] array) {
        if(array[xAxis][yAxis] == 0) {
            array[xAxis][yAxis] = -1;
            return 1;
        }

        if(array[xAxis][yAxis] == 1) {
            array[xAxis][yAxis] = -2;
            return 2;
        }

        return 0;
    }

	//Returning the ships type after taking its index
	public static String shipImageName(int index) {
		if (index == 0) {
            return "Carrier";
        } else if (index == 1) {
            return "Battleship";
        } else if (index == 2) {
            return "Submarine";
        } else if (index == 3) {
            return "Cruiser";
        } else if (index == 4) {
            return "Destroyer";
        } else {
            return "sea";
        }
	}
}