import javax.swing.*;
import javax.swing.text.html.Option;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OptionsScreen extends JFrame {
    private JLabel title;
    private JPanel panel;
    private JButton langButton;
    private JButton creditsButton;
    private JButton returnButton;
    private String username;
    private String language;
    private LanguagePack languagePack;

    public OptionsScreen(String username, String language) {
        panel = new OptionsPanel();

        this.username = username; //Sets the username attribute.
        this.language = language; //Sets the language attribute.

        languagePack = new LanguagePack(); //Contains all the text in the screen in both languages.
        languagePack.loadLanguage(language); //Loads the text depending on the language.


        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        //Adds the title to the screen.
        title = new JLabel(languagePack.title);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 55));
        title.setForeground(Color.WHITE);

        //Creates the Buttons.
        langButton = new OptionsButton(languagePack.langButtonText);
        creditsButton = new OptionsButton(languagePack.creditsButtonText);
        returnButton = new OptionsButton(languagePack.returnButtonText);

        //Adds ActionListeners to the Buttons.
        langButton.addActionListener(new LangButtonListener());
        creditsButton.addActionListener(new CreditsButtonListener());
        returnButton.addActionListener(new ReturnButtonListener());

        //Adds the screen formatting.
        panel.add(Box.createVerticalStrut(30));
        panel.add(title);
        panel.add(Box.createVerticalStrut(200));
        panel.add(langButton);
        panel.add(Box.createVerticalStrut(40));
        panel.add(creditsButton);
        panel.add(Box.createVerticalStrut(40));
        panel.add(returnButton);

        getContentPane().add(panel);

        setTitle(languagePack.windowTitle);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    //Extends the JPanel class to contain the background.
    public class OptionsPanel extends JPanel {
        private Image backgroundImage;

        public OptionsPanel() {
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

    //ActionListener for the Button that changes the language from English to Greek and vice versa.
    public class LangButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if (language.equals("en")){
                language = "gr";
            }
            else{
                language = "en";
            }

            languagePack.loadLanguage(language);
            title.setText(languagePack.title);
            setTitle(languagePack.windowTitle);
            creditsButton.setText(languagePack.creditsButtonText);
            returnButton.setText(languagePack.returnButtonText);
            langButton.setText(languagePack.langButtonText);
        }
    }

    //ActionListener for the Button that moves the user to CreditsScreen.
    public class CreditsButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new CreditsScreen(username, language));
            dispose();
        }
    }

    //ActionListener for the Return Button.
    public class ReturnButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new StartScreen(username, language));
            dispose();
        }
    }

    //Extends the JButton class to contain the Button formatting.
    public class OptionsButton extends JButton{
        public OptionsButton(String text){
            setMaximumSize(new Dimension(400, 80));
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setVerticalTextPosition(SwingConstants.CENTER);
            setHorizontalTextPosition(SwingConstants.CENTER);
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.BOLD, 15));
            setText(text);
    
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
    public class LanguagePack{
        private String windowTitle;
        private String title;
        private String langButtonText;
        private String creditsButtonText;
        private String returnButtonText;

        //This method takes a String as its attribute and if it is "en" it chooses the English text, otherwise it chooses the Greek text.
        public void loadLanguage(String language){
            if (language.equals("en")){
                windowTitle = "Options";
                title = "OPTIONS";
                creditsButtonText = "CREDITS";
                langButtonText = "CHANGE LANGUAGE";
                returnButtonText = "RETURN";
            }
            else{
                windowTitle = "Ρυθμίσεις";
                title = "ΡΥΘΜΙΣΕΙΣ";
                creditsButtonText = "CREDITS";
                langButtonText = "ΑΛΛΑΓΗ ΓΛΩΣΣΑΣ";
                returnButtonText = "ΕΠΙΣΤΡΟΦΗ"; 
            }
        }
    }
}