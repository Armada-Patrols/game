import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class StatisticsScreen extends JFrame {
    private JLabel title;
    private JLabel info;
    private JPanel panel;
    private JLabel playerHitField;
    private JLabel playerMissField;
    private JLabel computerHitField;
    private JLabel computerMissField;
    private JLabel playerPercentageField;
    private JLabel computerPercentageField;
    private JButton returnButton;
    private String username;
    private String language;
    private LanguagePack languagePack;

    public StatisticsScreen(String username, String language) {
        this.username = username; //Sets the username attribute.
        this.language = language; //Sets the language attribute.

        languagePack = new LanguagePack(); //Contains all the text in the screen in both languages.
        languagePack.loadLanguage(language); //Loads the text depending on the language.

        panel = new StatisticsPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        //Adds the title to the screen.
        title = new JLabel(languagePack.title);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.WHITE);

        //Adds the info.
        info = new JLabel(languagePack.info);
        info.setAlignmentX(Component.CENTER_ALIGNMENT);
        info.setFont(new Font("Arial", Font.BOLD, 18));
        info.setForeground(Color.WHITE);

        //Creates the TextFields.
        playerHitField = new JLabel();
        playerHitField.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerHitField.setFont(new Font("Arial", Font.BOLD, 25));
        playerHitField.setForeground(Color.WHITE);

        playerMissField = new JLabel();
        playerMissField.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerMissField.setFont(new Font("Arial", Font.BOLD, 25));
        playerMissField.setForeground(Color.WHITE);

        computerHitField = new JLabel();
        computerHitField.setAlignmentX(Component.CENTER_ALIGNMENT);
        computerHitField.setFont(new Font("Arial", Font.BOLD, 25));
        computerHitField.setForeground(Color.WHITE);

        computerMissField = new JLabel();
        computerMissField.setAlignmentX(Component.CENTER_ALIGNMENT);
        computerMissField.setFont(new Font("Arial", Font.BOLD, 25));
        computerMissField.setForeground(Color.WHITE);

        playerPercentageField = new JLabel();
        playerPercentageField.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerPercentageField.setFont(new Font("Arial", Font.BOLD, 25));
        playerPercentageField.setForeground(Color.WHITE);

        computerPercentageField = new JLabel();
        computerPercentageField.setAlignmentX(Component.CENTER_ALIGNMENT);
        computerPercentageField.setFont(new Font("Arial", Font.BOLD, 25));
        computerPercentageField.setForeground(Color.WHITE);

        //Creates the Buttons.
        returnButton = new LoginButton(languagePack.returnButtonText);

        //Adds ActionListeners to the Buttons.
        returnButton.addActionListener(new ReturnButtonListener());

        //Adds the screen formatting.
        panel.add(Box.createVerticalStrut(30));
        panel.add(title);
        panel.add(Box.createVerticalStrut(25));
        panel.add(info);
        panel.add(Box.createVerticalStrut(160));
        panel.add(playerHitField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(playerMissField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(computerHitField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(computerMissField);
        panel.add(Box.createVerticalStrut(50));
        panel.add(playerPercentageField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(computerPercentageField);
        panel.add(Box.createVerticalStrut(100));
        panel.add(returnButton);

        getContentPane().add(panel);

        setTitle(languagePack.windowTitle);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        System.out.println(username);

        //If the username is "guest" the UI changes accordingly.
        if (username.equals("guest"))
            showGuestMessage();
        else
            readStatisticsFromFile();
    }

    //This method shows a special message if the username equals "guest".
    public void showGuestMessage() {
        info.setText("");
        computerMissField.setText("You need to log on to view statistics!");
    }

    //This method reads the statistics of a player from their .obj file. 
    public void readStatisticsFromFile() {
        File file = new File("statistics/statistics-" + username + ".obj");
        if (!file.exists()) {
            createStatisticsFile(file);
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int playerHits = 0;
            int playerMisses = 0;
            int computerHits = 0;
            int computerMisses = 0;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String label = parts[0].trim();
                    int value1 = Integer.parseInt(parts[1].trim()); //Miss value.
                    int value2 = Integer.parseInt(parts[2].trim()); //Hit value.

                    switch (label) {
                        case "Hits":
                            playerHits = value1;
                            computerHits = value2;
                            playerHitField.setText(languagePack.playerHitText + value1);
                            computerHitField.setText(languagePack.computerHitText + value2);
                            break;
                        case "Misses":
                            playerMisses = value1;
                            computerMisses = value2;
                            playerMissField.setText(languagePack.playerMissText + value1);
                            computerMissField.setText(languagePack.computerMissText + value2);
                            break;
                        default:
                            break;
                    }
                }
            }

            if (playerHits + playerMisses != 0){
                double playerHitsPercentage = (double) playerHits / (playerHits + playerMisses) * 100;
                playerPercentageField.setText(languagePack.playerPercentageText + String.format("%.2f%%", playerHitsPercentage));
            }
            else
                playerPercentageField.setText(languagePack.computerPercentageText + "0%");

            if (computerHits + computerMisses != 0){
                double computerHitsPercentage = (double) computerHits / (computerHits + computerMisses) * 100;
                computerPercentageField.setText(languagePack.computerPercentageText + String.format("%.2f%%", computerHitsPercentage));
            }
            else
                computerPercentageField.setText(languagePack.computerPercentageText + "0%");

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    //This methods creates a .obj that contains the statistics for a player.
    public void createStatisticsFile(File file) {
        try {
            file.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("Hits,0,0\n");
                writer.write("Misses,0,0\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Extends the JPanel class to contain the background.
    public class StatisticsPanel extends JPanel {
        private Image backgroundImage;

        public StatisticsPanel() {
            try {
                backgroundImage = ImageIO.read(new File("images/background.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Paints the background on the panel.
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    //ActionListener for the Return Button.
    public class ReturnButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new StartScreen(username, language));
            dispose();
        }
    }

    //Extends the JButton class to contain the Button formatting.
    public class LoginButton extends JButton {
        public LoginButton(String text) {
            setText(text);
            setMaximumSize(new Dimension(200, 40));
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setVerticalTextPosition(SwingConstants.CENTER);
            setHorizontalTextPosition(SwingConstants.CENTER);
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.BOLD, 15));

            try {
                Image img = ImageIO.read(new File("images/button.png"));
                setIcon(new ImageIcon(img));
                setBorderPainted(false);
                setFocusPainted(false);
                setContentAreaFilled(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //This class contains all the text of the screen in both languages and makes the switch between the two languages.
    public class LanguagePack {
        private String windowTitle;
        private String title;
        private String info;
        private String playerHitText;
        private String playerMissText;
        private String computerHitText;
        private String computerMissText;
        private String computerPercentageText;
        private String playerPercentageText;
        private String returnButtonText;

        // This method takes a String as its attribute and if it is "en" it chooses the English text, otherwise it chooses the Greek text.
        public void loadLanguage(String language) {
            if (language.equals("en")) {
                windowTitle = "Statistics";
                title = "STATISTICS";
                info = "This is your statistics, Commander!";
                playerHitText = "Player hits: ";
                playerMissText = "Player misses: ";
                computerHitText = "Computer hits: ";
                computerMissText = "Computer misses: ";
                playerPercentageText = "Player percentage: ";
                computerPercentageText = "Computer percentage: ";
                returnButtonText = "RETURN";
            } else {
                windowTitle = "Στατιστικά";
                title = "ΣΤΑΤΙΣΤΙΚΑ";
                info = "Αυτά είναι τα στατιστικά σου, Διοικητή!";
                playerHitText = "Ευστοχίες παίχτη: ";
                playerMissText = "Αστοχίες παίχτη: ";
                computerHitText = "Ευστοχίες υπολογιστή: ";
                computerMissText = "Αστοχίες υπολογιστή: ";
                playerPercentageText = "Ποσοστό παίχτη: ";
                computerPercentageText = "Ποσοστό υπολογιστή: ";
                returnButtonText = "ΕΠΙΣΤΡΟΦΗ";
            }
        }
    }
}