import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class DifficultyScreen extends JFrame {
    private JLabel title;
    private JLabel info;
    private JPanel panel;
    private JButton easyButton;
    private JButton mediumButton;
    private JButton hardButton;
    private String username;
    private String language;
    private LanguagePack languagePack;

    private static int EASY = 10; //Sets the EASY value.
    private static int MEDIUM = 10000000; //Sets the MEDIUM value.
    private static int HARD = Integer.MAX_VALUE - 1000*1000; //Sets the HARD value.

    public DifficultyScreen(String username, String language) {
        this.language = language; //Sets the language attribute.
        this.username = username; //Sets the username attribute.

        languagePack = new LanguagePack(); //Contains all the text in the screen in both languages.
        languagePack.loadLanguage(language); //Loads the text depending on the language.


        panel = new DifficultyPanel();

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

        //Creates the Buttons.
        easyButton = new DifficultyButton(languagePack.easyButtonText);
        mediumButton = new DifficultyButton(languagePack.mediumButtonText);
        hardButton = new DifficultyButton(languagePack.hardButtonText);

        //Adds ActionListeners to the Buttons and the int values respectively.
        easyButton.addActionListener(new DifficultyButtonListener(DifficultyScreen.EASY));
        mediumButton.addActionListener(new DifficultyButtonListener(DifficultyScreen.MEDIUM));
        hardButton.addActionListener(new DifficultyButtonListener(DifficultyScreen.HARD));

        
        //Adds the screen formatting.
        panel.add(Box.createVerticalStrut(30));
        panel.add(title);
        panel.add(Box.createVerticalStrut(25));
        panel.add(info);
        panel.add(Box.createVerticalStrut(200));
        panel.add(easyButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(mediumButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(hardButton);

        getContentPane().add(panel);

        setTitle(languagePack.windowTitle);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    //Extends the JPanel class to contain the background.
    public class DifficultyPanel extends JPanel {
        private Image backgroundImage;

        public DifficultyPanel() {
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

    //Extends the JButton class to contain the Button formatting.
    public class DifficultyButton extends JButton{
        public DifficultyButton(String text){
            setText(text);
            setMaximumSize(new Dimension(250, 80));
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

    //ActionListener for the difficulty Buttons, it has an int attribute to distinguish between easy/medium/hard.
    public class DifficultyButtonListener implements ActionListener{
        private int difficulty;
        
        public DifficultyButtonListener(int difficulty){
            this.difficulty = difficulty;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new PreGameScreen(username, language, difficulty));
            dispose();
        }
    }

    //This class contains all the text of the screen in both languages and makes the switch between the two languages.
    public class LanguagePack{
        private String windowTitle;
        private String title;
        private String info;
        private String easyButtonText;
        private String mediumButtonText;
        private String hardButtonText;

       //This method takes a String as its attribute and if it is "en" it chooses the English text, otherwise it chooses the Greek text.
        public void loadLanguage(String language){
            if (language.equals("en")){
                windowTitle = "Choose Difficulty";
                title = "CHOOSE DIFFICULTY";
                info = "Choose the difficulty you want, Commander!";
                easyButtonText = "EASY";
                mediumButtonText = "MEDIUM";
                hardButtonText = "HARD";
            }
            else{
                windowTitle = "Διάλεξε Δυσκολία";
                title = "ΔΙΑΛΕΞΕ ΔΥΣΚΟΛΙΑ";
                info = "Διάλεξε την δυσκολία που θες, Διοικητή!";
                easyButtonText = "ΕΥΚΟΛΟ";
                mediumButtonText = "ΜΕΤΡΙΟ";
                hardButtonText = "ΔΥΣΚΟΛΟ";
            }
        }
    }
}