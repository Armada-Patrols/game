import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class StartScreen extends JFrame {
    private JPanel panel;
    private JButton startButton;
    private JButton helpButton;
    private JButton optButton;
    private JButton statsButton;
    private String username;
    private JLabel info;
    private JButton logOutButton;
    private String language;
    private LanguagePack languagePack;

    public StartScreen(String username, String language) {
        panel = new StartPanel();
        
        this.username = username; //Sets the username attribute.
        this.language = language; //Sets the language attribute.

        languagePack = new LanguagePack(); //Contains all the text in the screen in both languages.
        languagePack.loadLanguage(language); //Loads the text depending on the language.


        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        //Adds the info.
        info = new JLabel();
        info.setAlignmentX(Component.CENTER_ALIGNMENT);
        info.setFont(new Font("Arial", Font.BOLD, 15));
        info.setForeground(Color.WHITE);

        //Creates the Buttons.
        startButton = new MainButton(languagePack.startButtonText);
        helpButton = new MainButton(languagePack.helpButtonText);
        optButton = new MainButton(languagePack.optButtonText);
        statsButton = new MainButton(languagePack.statsButtonText);
        
        //Adds ActionListeners to the Buttons.
        startButton.addActionListener(new StartGameButtonListener());
        helpButton.addActionListener(new HelpGameButtonListener());
        optButton.addActionListener(new OptionsButtonListener());
        statsButton.addActionListener(new StatisticsButtonListener());

        //Adds the screen formatting.
        panel.add(Box.createVerticalStrut(350));
        panel.add(startButton);
        panel.add(Box.createVerticalStrut(30));
        panel.add(helpButton);
        panel.add(Box.createVerticalStrut(30));
        panel.add(optButton);
        panel.add(Box.createVerticalStrut(30));
        panel.add(statsButton);
        panel.add(Box.createVerticalStrut(30));
        panel.add(info);
        panel.add(Box.createVerticalStrut(35));

        getContentPane().add(panel);

        setTitle(languagePack.windowTitle);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        logOutButton = new MainButton(languagePack.logOutButtonText);
        logOutButton.addActionListener(new LogOutButtonListener());

        panel.add(logOutButton);
        panel.add(Box.createVerticalStrut(10));

        if (!username.equals("guest"))
            info.setText(languagePack.info);
        else
            info.setText(languagePack.infoGuest);
    }

    //Extends the JPanel class to contain the background.
    public class StartPanel extends JPanel {
        private Image backgroundImage;

        public StartPanel() {
            try {
                backgroundImage = ImageIO.read(new File("images/main-background.png"));
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

    //ActionListener for the Button that starts the Game.
    public class StartGameButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new DifficultyScreen(username, language));
            dispose();
        }
    }

    //ActionListener for the Button that shows the HelpScreen.
    public class HelpGameButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new HelpScreen(username, language));
            dispose();
        }
    }

    //ActionListener for the Button that shows the OptionsScreen.
    public class OptionsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new OptionsScreen(username, language));
            dispose();
        }
    }

    //ActionListener for the Button that shows the StatisticsScreen.
    public class StatisticsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new StatisticsScreen(username, language));
            dispose();
        }
    }

    //ActionListener for the Button that log outs the user.
    public class LogOutButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new WelcomeScreen(language);
            dispose();
        }
    }

    //Extends the JButton class to contain the Button formatting.
    public class MainButton extends JButton {
        public MainButton(String text) {
            setMaximumSize(new Dimension(300, 50));
            setPreferredSize(new Dimension(300, 50));
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setVerticalTextPosition(SwingConstants.CENTER);
            setHorizontalTextPosition(SwingConstants.CENTER);
            setText(text);
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.BOLD, 20));

            try {
                Image img = ImageIO.read(new File("images/button.png"));
                setIcon(new ImageIcon(img));
                setBorderPainted(false);
                setFocusPainted(false);
                setContentAreaFilled(false);
                setMargin(new Insets(10, 10, 10, 10));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //This class contains all the text of the screen in both languages and makes the switch between the two languages.
    public class LanguagePack{
        private String windowTitle;
        private String info;
        private String infoGuest;
        private String startButtonText;
        private String helpButtonText;
        private String optButtonText;
        private String statsButtonText;
        private String logOutButtonText;
    
        //This method takes a String as its attribute and if it is "en" it chooses the English text, otherwise it chooses the Greek text.
        public void loadLanguage(String language){
            if (language.equals("en")){
                windowTitle = "Main Menu";
                info = "Logged in as " + username + ".";
                infoGuest = "Welcome Guest!";
                startButtonText = "START GAME";
                helpButtonText = "HELP";
                optButtonText = "OPTIONS";
                statsButtonText = "STATISTICS";
                logOutButtonText = "ABANDON FLEET";
            }
            else{
                windowTitle = "Κεντρικό Μενού";
                info = "Συνδεδεμένος/η ως " + username + ".";
                infoGuest = "Καλώς Ορίσες Καλεσμένε!";
                startButtonText = "ΠΑΙΞΕ";
                helpButtonText = "ΒΟΗΘΕΙΑ";
                optButtonText = "ΡΥΘΜΙΣΕΙΣ";
                statsButtonText = "ΣΤΑΤΙΣΤΙΚΑ";
                logOutButtonText = "ΑΠΟΣΥΝΔΕΣΗ";
            }
        }
    }
}
