import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LoginScreen extends JFrame {
    private JLabel title;
    private JLabel info;
    private JPanel panel;
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton confirmButton;
    private JButton returnButton;
    private String language;
    private boolean isLogged;
    private LanguagePack languagePack;

    public LoginScreen(String language) {
        ConnectingDatabase.establishConnection(); //Establishes connection to the database. 
        
        this.language = language; //Sets the language attribute.

        languagePack = new LanguagePack(); //Contains all the text in the screen in both languages.
        languagePack.loadLanguage(language); //Loads the text depending on the language.


        panel = new LoginPanel();

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
        usernameField = new JTextField(languagePack.usernameText);
        usernameField.setMaximumSize(new Dimension(200, 40));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = new JTextField(languagePack.passwordText);
        passwordField.setMaximumSize(new Dimension(200, 40));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Creates the Buttons.
        confirmButton = new LoginButton(languagePack.confButtonText);
        returnButton = new LoginButton(languagePack.returnButtonText);

        //Adds ActionListeners to the Buttons.
        confirmButton.addActionListener(new ConfirmButtonListener());
        returnButton.addActionListener(new ReturnButtonListener());

        //Adds the screen formatting.
        panel.add(Box.createVerticalStrut(30));
        panel.add(title);
        panel.add(Box.createVerticalStrut(25));
        panel.add(info);
        panel.add(Box.createVerticalStrut(160));
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(160));
        panel.add(confirmButton);
        panel.add(Box.createVerticalStrut(20));
        panel.add(returnButton);

        getContentPane().add(panel);

        setTitle(languagePack.windowTitle);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    //Extends the JPanel class to contain the background.
    public class LoginPanel extends JPanel {
        private Image backgroundImage;

        public LoginPanel() {
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

    //ActionListener for the Confirm Button.
    public class ConfirmButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String username = usernameField.getText();
			String password = passwordField.getText();

			//Checks if the username field is empty.
            if (username.isEmpty()){
                JOptionPane.showMessageDialog(null, languagePack.error1);
                return;
            }

            //Checks if the password field is empty.
            if (password.isEmpty()){
                JOptionPane.showMessageDialog(null, languagePack.error2);
                return;
            }
			
            //The AccessDatabase class returns a boolean value and if it is true the user continues to StartScreen, otherwise they get an error message that the password is incorrect.
			isLogged = AccessDatabase.getCredentials(username, password);
			if(!isLogged) {
				JOptionPane.showMessageDialog(null, languagePack.error3); 
			}else {
				SwingUtilities.invokeLater(() -> new StartScreen(username, language));
                dispose();
			}
		}
    }

    //ActionListener for the Return Button.
    public class ReturnButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new WelcomeScreen(language));
            dispose();
        }
    }

    //Extends the JButton class to contain the Button formatting.
    public class LoginButton extends JButton{
        public LoginButton(String text){
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
    public class LanguagePack{
        private String windowTitle;
        private String title;
        private String info;
        private String usernameText;
        private String passwordText;
        private String confButtonText;
        private String returnButtonText;
        private String error1;
        private String error2;
        private String error3;

        //This method takes a String as its attribute and if it is "en" it chooses the English text, otherwise it chooses the Greek text.
        public void loadLanguage(String language){
            if (language.equals("en")){
                windowTitle = "Login";
                title = "I'M A COMMANDER";
                info = "Fill in your credentials to login, Commander!";
                usernameText = "Username";
                passwordText = "Password";
                confButtonText = "CONFIRM";
                returnButtonText = "RETURN";
                error1 = "You must enter a username to login.";
                error2 = "You must enter a password to login.";
                error3 = "Wrong password! Try again.";
            }
            else{
                windowTitle = "Σύνδεση";
                title = "ΕΙΜΑΙ ΔΙΟΙΚΗΤΗΣ";
                info = "Βάλε τα στοιχεία σου για να συνδεθείς, Διοικητή!";
                usernameText = "Όνομα Χρήστη";
                passwordText = "Κωδικός";
                confButtonText = "ΕΠΙΒΕΒΑΙΩΣΗ";
                returnButtonText = "ΕΠΙΣΤΡΟΦΗ";
                error1 = "Πρέπει να εισάγεις όνομα χρήστη για να συνδεθείς.";
                error2 = "Πρέπει να εισάγεις κωδικό για να συνδεθείς.";
                error3 = "Λάθος κωδικός! Προσπάθησε ξανά.";
            }
        }
    }
}