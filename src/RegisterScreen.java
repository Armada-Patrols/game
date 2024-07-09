import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class RegisterScreen extends JFrame {
    private JLabel title;
    private JLabel info;
    private JPanel panel;
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField retypePasswordField;
    private JButton confirmButton;
    private JButton returnButton;
    private String language;
    private LanguagePack languagePack;

    public RegisterScreen(String language) {
        ConnectingDatabase.establishConnection(); //Establishes connection to the database.

        this.language = language; //Sets the language attribute.

        languagePack = new LanguagePack(); //Contains all the text in the screen in both languages.
        languagePack.loadLanguage(language); //Loads the text depending on the language.

        panel = new RegisterPanel();

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

        retypePasswordField = new JTextField(languagePack.retypePasswordText);
        retypePasswordField.setMaximumSize(new Dimension(200, 40));
        retypePasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Creates the Buttons.
        confirmButton = new ReturnButton(languagePack.confButtonText);
        returnButton = new ReturnButton(languagePack.returnButtonText);

        //Adds ActionListeners to the Buttons.
        confirmButton.addActionListener(new ConfButtonListener());
        returnButton.addActionListener(new ReturnButtonListener());

        //Adds the screen formatting.
        panel.add(Box.createVerticalStrut(30));
        panel.add(title);
        panel.add(Box.createVerticalStrut(25));
        panel.add(info);
        panel.add(Box.createVerticalStrut(150));
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(retypePasswordField);
        panel.add(Box.createVerticalStrut(130));
        panel.add(confirmButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(returnButton);

        getContentPane().add(panel);

        setTitle(languagePack.windowTitle);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

    }

    //Extends the JPanel class to contain the background.
    public class RegisterPanel extends JPanel {
        private Image backgroundImage;

        public RegisterPanel() {
            try {
                backgroundImage = ImageIO.read(new File("images/background.png")); //Reads the background file.
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
    public class ConfButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String username = usernameField.getText();
			String password = passwordField.getText();
			String passwordAuth = retypePasswordField.getText();

			//Checks if the username field is empty.
            if (username.isEmpty()){
                JOptionPane.showMessageDialog(null, languagePack.error1); //Prints the error message.
                return;
            }

            //Checks if the password field is empty.
            if (password.isEmpty()){
                JOptionPane.showMessageDialog(null, languagePack.error2); //Prints the error message.
                return;
            }

           //Checks if the retypepassword field is empty.
            if (passwordAuth.isEmpty()){
                JOptionPane.showMessageDialog(null, languagePack.error3); //Prints the error message.
                return;
            }
 
            //Checks if the username already exists in the database.
            if (!DatabaseWork.insertingOperation(username, passwordAuth)) {
                JOptionPane.showMessageDialog(null, languagePack.error4); //Prints the error message.
                return;
            }

            //Checks if the password mismatches between the password and the retypepassword field.
            if (!password.equals(passwordAuth)) {
                JOptionPane.showMessageDialog(null, languagePack.error5); //Prints the error message.
                return;
            }
            
            SwingUtilities.invokeLater(() -> new StartScreen(username, language));
            dispose();
            
		}
    }

    //ActionListener for the Return Button.
    public class ReturnButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new WelcomeScreen(language)); //When pressed the user goes back to WelcomeScreen.
            dispose();
        }
    }

    //Extends the JButton class to contain the Button formatting.
    public class ReturnButton extends JButton{
        public ReturnButton(String text){
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
        private String retypePasswordText;
        private String confButtonText;
        private String returnButtonText;
        private String error1;
        private String error2;
        private String error3;
        private String error4;
        private String error5;

        //This method takes a String as its attribute and if it is "en" it chooses the English text, otherwise it chooses the Greek text.
        public void loadLanguage(String language){
            if (language.equals("en")){
                windowTitle = "Register";
                title = "CREATE COMMANDER";
                info = "Create a new Commander by typing the following fields!";
                usernameText = "Username";
                passwordText = "Password";
                retypePasswordText = "Confirm Password";
                confButtonText = "CONFIRM";
                returnButtonText = "RETURN";
                error1 = "You must enter a username to register.";
                error2 = "You must enter a password to register.";
                error3 = "You must re-enter your password to register.";
                error4 = "Username already exists. Try again.";
                error5 = "Password is different in retype password field.";
            }
            else{
                windowTitle = "Δημιουργία Λογαριασμού";
                title = "ΔΗΜΙΟΥΡΓΙΑ ΔΙΟΙΚΗΤΗ";
                info = "Δημιούργησε νέο Διοικητή συμπληρώνοντας τα παρακάτω πεδία!";
                usernameText = "Όνομα Χρήστη";
                passwordText = "Κωδικός";
                retypePasswordText = "Επιβεβαίωση Κωδικού";
                confButtonText = "ΕΠΙΒΕΒΑΙΩΣΗ";
                returnButtonText = "ΕΠΙΣΤΡΟΦΗ";
                error1 = "Πρέπει να εισάγεις όνομα χρήστη για να συνδεθείς.";
                error2 = "Πρέπει να εισάγεις κωδικό για να συνδεθείς.";
                error3 = "Πρέπει να ξαναεισάγεις κωδικό για να συνδεθείς.";
                error4 = "Το όνομα χρήστη αυτό ήδη υπάρχει. Προσπάθησε ξανά.";
                error5 = "Ο κωδικός είναι διαφορετικός στο πεδίο επαναεισαγωγής κωδικού.";
            }
        }
    }
}