import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JCheckBox;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class GameScreen extends JFrame {
    private JPanel mainPanel;
    private JPanel gridPanel;
    private JCheckBox bombCheckBox;

    public static JButton[][] buttons;
    private static int[][] index_array = new int[10][10];
    private static JButton[][] grid2Buttons;
    private static int[][] computerGrid = {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};

    private static int[][] statisticsArray = {{0, 0},
    {0, 0}}; 
    private Statistics statistics;    

    private String language;
    private String username;
    private int difficulty;
    private LanguagePack languagePack;

    private static int[][] playerGrid = new int[10][10];

    private static ComputerShooting compShoot;
    private boolean bombUsed = false;

    public GameScreen(JButton[][] buttons, String username, String language, int difficulty) {
        this.buttons = buttons;
        this.language = language;
        this.username = username;
        this.difficulty = difficulty;

        languagePack = new LanguagePack();
        languagePack.loadLanguage(language);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));

        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(1, 2, 20, 0)); //1 row, 2 columns, with 20px horizontal gap.

        JPanel grid1 = createButtonGrid1(10, 10);
        JPanel grid2 = createButtonGrid2(10, 10);

        bombCheckBox = new JCheckBox(languagePack.powerUpText);
        bombCheckBox.setEnabled(true);

        gridPanel.add(grid1);
        gridPanel.add(grid2);

        mainPanel.add(gridPanel, BorderLayout.CENTER);
        mainPanel.add(bombCheckBox, BorderLayout.SOUTH);

        //Adjust the border to add space above and below.
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10)); //30px top and bottom, 10px left and right.

        getContentPane().add(mainPanel);

        setTitle(languagePack.windowTitle);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        initialiseGame(buttons);

        updatePlayerGridIcons();
    }


    public void initialiseGame(JButton[][] buttons){
        //Reset computer grid.
        CheckConditions.totalComputerLifepoints = 17;
        CheckConditions.totalPlayerLifepoints = 17;
        for (int i = 0; i < computerGrid.length; i++) {
            for (int j = 0; j < computerGrid[i].length; j++) {
                computerGrid[i][j] = 0;
            }
        }
    
        //Reset player grid.
        for (int i = 0; i < playerGrid.length; i++) {
            for (int j = 0; j < playerGrid[i].length; j++) {
                playerGrid[i][j] = 0;
            }
        }
    
        //Reset buttons.
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].setIcon(null);
                buttons[i][j].setBackground(Color.WHITE);
            }
        }
    
        //Reset grid2 buttons.
        for (int i = 0; i < grid2Buttons.length; i++) {
            for (int j = 0; j < grid2Buttons[i].length; j++) {
                grid2Buttons[i][j].setIcon(null);
                grid2Buttons[i][j].setBackground(Color.WHITE);
                computerGrid[i][j] = 0;
            }
        }
    
        //Reinitialize game logic.
        RandomPlacement.setComputerGrid(computerGrid);
        RandomPlacement.placingShips();
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                if (buttons[i][j].getIcon() != null) {
                    playerGrid[i][j] = 1;
                } else {
                    playerGrid[i][j] = 0;
                }
            }
        }
    
        compShoot = new ComputerShooting(difficulty, playerGrid);
        index_array = PreGameScreen.getIndexArray();
    }


    public int posititionOfship(boolean isHorizontal, int i, int j){


        int pos = 0;
        int indexOfShip = index_array[i][j];

        if(isHorizontal) {
            while (true) {
                pos++;

                if(j - pos < 0){
                    break;
                }

                if (index_array[i][j - pos] != indexOfShip) {
                    break;
                }
            }
        }else{
            while (true) {
                pos++;

                if(i - pos < 0){
                    break;
                }

                if (index_array[i - pos][j] != indexOfShip) {
                    break;
                }
            }
        }

        return pos;



    }

    public void updatePlayerGridIcons() {
        for (int i = 0; i < index_array.length; i++) {
            for (int j = 0; j < index_array[i].length; j++) {

                String direction = "horizontal";

                int indexOfShip = index_array[i][j];
                if (index_array[i][j] != -1) {
                    boolean isHorizontal = false;

                    

                    if(j - 1 >= 0)
                        if(index_array[i][j-1] == indexOfShip)
                            isHorizontal = true;
                    if(j + 1 < index_array.length)
                        if(index_array[i][j+1] == indexOfShip)
                            isHorizontal = true;
                    if(!isHorizontal)
                            direction = "vertical";

                    int pos =  posititionOfship(isHorizontal, i, j);

                    String shipImageName = Ship.shipImageName(index_array[i][j]);
                    String filePath = "images/" + shipImageName + "_" + direction + "_part" + pos + ".png";
                    try {
                        Image image = ImageIO.read(new File(filePath));
                        buttons[i][j].setIcon(new ImageIcon(image));
                        buttons[i][j].setOpaque(true);
                        buttons[i][j].setContentAreaFilled(true);
                        buttons[i][j].repaint();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    

    public void gameLoop() {
		if(CheckConditions.totalComputerLifepoints > 0) {
    		
    		if(CheckConditions.totalComputerLifepoints == 0) {
    			JOptionPane.showMessageDialog(null, "You won!");
    		}else {
    			int[] temp = compShoot.chooseSpot();
    			while(temp[0] == 10) {

    				temp = compShoot.chooseSpot();
    			}
    			
    	    		int indexOfShip = index_array[temp[1]][temp[2]];

                    boolean isHorizontal = false;
                    if(temp[2] - 1 >= 0)
                        if(index_array[temp[1]][temp[2]-1] == indexOfShip)
                            isHorizontal = true;
                    if(temp[2] + 1 < index_array.length)
                        if(index_array[temp[1]][temp[2]+1] == indexOfShip)
                            isHorizontal = true;

                    if(isHorizontal){
	                    if(!Ship.shipImageName(indexOfShip).equals("sea")){
	                        CheckConditions.decreasePlayerLifepoints();
	                        statisticsArray[0][1]++;
	                        int pos = 0;
	                        while (true) {
	                            pos++;
	
	                            if(temp[2] - pos < 0){
	                                break;
	                            }
	
	                            if (index_array[temp[1]][temp[2] - pos] != indexOfShip) {
	                                break;
	                            }
	                        }

                            String imagePath = "images/hit" + Ship.shipImageName(indexOfShip) + "_horizontal_part_" + pos + ".png";
	
	                        try {
	                            Image image2 = ImageIO.read(new File(imagePath));
	                            buttons[temp[1]][temp[2]].setIcon(new ImageIcon(image2));
	                            buttons[temp[1]][temp[2]].setOpaque(true);
	                            buttons[temp[1]][temp[2]].setContentAreaFilled(true);
	                            buttons[temp[1]][temp[2]].repaint();
	                        } catch (IOException e) {
	                            e.printStackTrace();
	                        }
	
	                        
	                    }else{
	                        try {
	
	                            statisticsArray[1][1]++;
	
	                            Image image2 = ImageIO.read(new File("images/sea.jpg"));
	                            buttons[temp[1]][temp[2]].setIcon(new ImageIcon(image2));
	                            buttons[temp[1]][temp[2]].setOpaque(true);
	                            buttons[temp[1]][temp[2]].setContentAreaFilled(true);
	                            buttons[temp[1]][temp[2]].repaint();
	                        } catch (IOException e) {
	                            e.printStackTrace();
	                        }
	                    }
                    }else {
                    	if(!Ship.shipImageName(indexOfShip).equals("sea")){
	                        CheckConditions.decreasePlayerLifepoints();
	                        statisticsArray[0][1]++;
	                        int pos = 0;
	                        while (true) {
	                            pos++;
	
	                            if(temp[1] - pos < 0){
	                                break;
	                            }
	
	                            if (index_array[temp[1] - pos][temp[2]] != indexOfShip) {
	                                break;
	                            }
	                        }

                            String imagePath = "images/hit" + Ship.shipImageName(indexOfShip) + "_vertical_part_" + pos + ".png";
                            System.out.println(imagePath);
                            System.out.println(temp[0]);
	
	                        try {
	                            Image image2 = ImageIO.read(new File(imagePath));
	                            buttons[temp[1]][temp[2]].setIcon(new ImageIcon(image2));
	                            buttons[temp[1]][temp[2]].setOpaque(true);
	                            buttons[temp[1]][temp[2]].setContentAreaFilled(true);
	                            buttons[temp[1]][temp[2]].repaint();
	                        } catch (IOException e) {
	                            e.printStackTrace();
	                        }
	
	                        
	                    }else{
	                        try {
	
	                            statisticsArray[1][1]++;
	
	                            Image image2 = ImageIO.read(new File("images/sea.jpg"));
	                            buttons[temp[1]][temp[2]].setIcon(new ImageIcon(image2));
	                            buttons[temp[1]][temp[2]].setOpaque(true);
	                            buttons[temp[1]][temp[2]].setContentAreaFilled(true);
	                            buttons[temp[1]][temp[2]].repaint();
	                        } catch (IOException e) {
	                            e.printStackTrace();
	                        }
	                    }
                    }
    	    		if(CheckConditions.totalPlayerLifepoints == 0) {
    	    		    JOptionPane.showMessageDialog(null, "Game Over! You Lost!");
                        if (username != "guest"){
                            statistics = new Statistics(statisticsArray);
                            statistics.appendStatisticsToFile("statistics/statistics-" + username + ".obj");
                        }
                        SwingUtilities.invokeLater(() -> new StartScreen(username, language));
                        dispose();
    	    		}
    			
    		}
    	}
        else {
            JOptionPane.showMessageDialog(null, "YOU WON");
            if (username != "guest"){
                statistics = new Statistics(statisticsArray);
                statistics.appendStatisticsToFile("statistics/statistics-" + username + ".obj");
            }
            SwingUtilities.invokeLater(() -> new StartScreen(username, language)); 
            dispose();
        }
    }

    public JPanel createButtonGrid1(int rows, int cols) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(rows, cols));
        panel.setBackground(Color.WHITE);

        JButton[][] newButtons = new JButton[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                newButtons[i][j] = buttons[i][j];
                panel.add(buttons[i][j]);
            }
        }

        return panel;
    }

    public JPanel createButtonGrid2(int rows, int cols) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(rows, cols));
        panel.setBackground(Color.WHITE);

        grid2Buttons = new JButton[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JButton button = new JButton();
                button.setBackground(Color.WHITE);
                button.putClientProperty("row", i);
                button.putClientProperty("col", j);
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        new SwingWorker<Void, Void>() {
                            public Void doInBackground() {
                                JButton source = (JButton) e.getSource();
                                int row = (int) source.getClientProperty("row");
                                int col = (int) source.getClientProperty("col");
                                handleButtonClick(row, col);
                                return null;
                            }

                            public void done() {
                                //UI updates should be done on the Event Dispatch Thread (EDT).
                                SwingUtilities.invokeLater(new Runnable() {
                                    public void run() {
                                        //Refresh UI or update button icons as necessary.
                                        updateButtonIcons();
                                    }
                                });
                            }
                        }.execute();
                    }
                });
                grid2Buttons[i][j] = button;
                panel.add(button);
            }
        }
        return panel;
    }

    public boolean is2x2BombActive() {
        return bombCheckBox.isSelected() && !bombUsed;
    }

    public void activate2x2Bomb(int row, int col) {
        //Apply the 2x2 bomb effect on the grid.
        for (int i = row; i <= row + 1 && i < 10; i++) {
            for (int j = col; j <= col + 1 && j < 10; j++) {
                if (computerGrid[i][j] >= 0) {
                    int result = Ship.shoot(i, j, computerGrid);
                    updateButtonIcon(i, j, result);
                }
            }
        }
        bombUsed = true;
        bombCheckBox.setEnabled(false);
    }

    public void handleButtonClick(int row, int col) {
        if (computerGrid[row][col] >= 0) { //Making sure not to hit the same spot.
            if (is2x2BombActive()) {
                activate2x2Bomb(row, col);
                bombCheckBox.setSelected(false); //Deselect the checkbox after using the bomb.
            } else {
                int result = Ship.shoot(row, col, computerGrid);
                updateButtonIcon(row, col, result);
            }
            gameLoop();
        }
    }

    public void updateButtonIcon(int row, int col, int x) {
        if (x != 1) {
            CheckConditions.decreaseComputerLifepoints();
            statisticsArray[0][0]++;
        } else {
            statisticsArray[1][0]++;
        }
        String path = (x == 1) ? "images/sea.jpg" : "images/hit-sea.jpg";
        try {
            Image image2 = ImageIO.read(new File(path));
            grid2Buttons[row][col].setIcon(new ImageIcon(image2));
            grid2Buttons[row][col].setOpaque(true);
            grid2Buttons[row][col].setContentAreaFilled(true);
            grid2Buttons[row][col].repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateButtonIcons() {
        for (int i = 0; i < grid2Buttons.length; i++) {
            for (int j = 0; j < grid2Buttons[i].length; j++) {
                if (computerGrid[i][j] == -1) {
                    grid2Buttons[i][j].setIcon(new ImageIcon("images/sea.jpg"));
                } else if (computerGrid[i][j] == -2) {
                    grid2Buttons[i][j].setIcon(new ImageIcon("images/hit-sea.jpg"));
                }
                grid2Buttons[i][j].repaint();
            }
        }
    }

    public class LanguagePack{
        private String windowTitle;
        private String powerUpText;

        public void loadLanguage(String language){
            if (language.equals("en")){
                windowTitle = "Fight!";
                powerUpText = "Use 2x2 bomb";
            }
            else{
                windowTitle = "Πολέμα!";
                powerUpText = "Χρησιμοποιήσε την 2x2 βόμβα";
            }
        }
    }
}