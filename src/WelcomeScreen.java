import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class WelcomeScreen extends JFrame {
    private JLabel title;
    private JLabel info;
    private JPanel panel;
    private JButton logButton;
    private JButton regButton;
    private JButton guestButton;
    private String language;
    private LanguagePack languagePack;

    public WelcomeScreen(String language) {
        panel = new WelcomePanel();
        
        this.language = language; //Sets the language attribute.

        languagePack = new LanguagePack(); //Contains all the text in the screen in both languages.
        languagePack.loadLanguage(language); //Loads the text depending on the language.

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        //Adds the title to the screen.
        title = new JLabel(languagePack.title);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 50));
        title.setForeground(Color.WHITE);

        //Adds the info.
        info = new JLabel(languagePack.info);
        info.setAlignmentX(Component.CENTER_ALIGNMENT);
        info.setFont(new Font("Arial", Font.BOLD, 25));
        info.setForeground(Color.WHITE);

        //Creates the Buttons.
        logButton = new WelcomeButton(languagePack.logButtonText);
        regButton = new WelcomeButton(languagePack.regButtonText);
        guestButton = new WelcomeButton(languagePack.guestButtonText);

        //Adds ActionListeners to the Buttons.
        logButton.addActionListener(new LogButtonListener());
        regButton.addActionListener(new RegButtonListener());
        guestButton.addActionListener(new GuestButtonListener());

        //Adds the screen formatting.
        panel.add(Box.createVerticalStrut(30));
        panel.add(title);
        panel.add(Box.createVerticalStrut(25));
        panel.add(info);
        panel.add(Box.createVerticalStrut(150));
        panel.add(logButton);
        panel.add(Box.createVerticalStrut(40));
        panel.add(regButton);
        panel.add(Box.createVerticalStrut(40));
        panel.add(guestButton);

        getContentPane().add(panel);

        setTitle(languagePack.windowTitle);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    //Extends the JPanel class to contain the background.
    public class WelcomePanel extends JPanel {
        private Image backgroundImage;

        public WelcomePanel() {
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

    //ActionListener for the Button that moves the user to the Login screen.
    public class LogButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            new LoginScreen(language);
            dispose();
        }
    }

    //ActionListener for the Button that logs the user as a guest.
    public class GuestButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            new StartScreen("guest", language);
            dispose();
        }
    }

    //ActionListener for the Button that moves the user to the Register screen.
    public class RegButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            new RegisterScreen(language);
            dispose();
        }
    }

    //Extends the JButton class to contain the Button formatting.
    public class WelcomeButton extends JButton{
        public WelcomeButton(String text){
            setMaximumSize(new Dimension(300, 70));
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
        private String info;
        private String logButtonText;
        private String regButtonText;
        private String guestButtonText;

        //This method takes a String as its attribute and if it is "en" it chooses the English text, otherwise it chooses the Greek text.
        public void loadLanguage(String language){
            if (language.equals("en")){
                windowTitle = "Welcome Commander";
                title = "WELCOME";
                info = "Hello, Commander! Ready to battle?";
                logButtonText = "I'M A COMMANDER";
                regButtonText = "CREATE COMMANDER";
                guestButtonText = "CONTINUE AS GUEST";
            }
            else{
                windowTitle = "Καλώς Όρισες Διοικητή";
                title = "ΚΑΛΩΣ ΟΡΙΣΕΣ";
                info = "Γειά σου, Διοικητή! Έτοιμος για μάχη;";
                logButtonText = "ΕΙΜΑΙ ΔΙΟΙΚΗΤΗΣ";
                regButtonText = "ΔΗΜΙΟΥΡΓΗΣΕ ΔΙΟΙΚΗΤΗ";
                guestButtonText = "ΣΥΝΕΧΙΣΕ ΩΣ ΚΑΛΕΣΜΕΝΟΣ";
            }
        }
    }
}