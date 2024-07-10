import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class HelpScreen extends JFrame {
    private JLabel title;
    private JPanel panel;
    private JButton returnButton;
    private String username;
    private String language;
    private LanguagePack languagePack;
    private JTextArea creditsTextArea;

    public HelpScreen(String username, String language) {
        panel = new HelpPanel();
    
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
    
        //Adds the info.
        creditsTextArea = new JTextArea(languagePack.help);
        creditsTextArea.setFont(new Font("Arial", Font.PLAIN, 20));
        creditsTextArea.setForeground(Color.WHITE);
        creditsTextArea.setBackground(new Color(0, 0, 0, 0));
        creditsTextArea.setOpaque(false);
        creditsTextArea.setEditable(false);
        creditsTextArea.setLineWrap(true);
        creditsTextArea.setWrapStyleWord(true);
        creditsTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        //Adds the HELP text.
        JScrollPane scrollPane = new JScrollPane(creditsTextArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    
        panel.add(title);
        panel.add(Box.createHorizontalStrut(50));
        panel.add(scrollPane);
    
        //Creates the Button.
        returnButton = new HelpButton(languagePack.returnButtonText);

        //Adds ActionListeners to the Buttons.
        returnButton.addActionListener(new ReturnButtonListener());
        panel.add(Box.createVerticalStrut(50));
        panel.add(returnButton);
    
        getContentPane().add(panel);
    
        setTitle(languagePack.windowTitle);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }    

    //Extends the JPanel class to contain the background.
    public class HelpPanel extends JPanel {
        private Image backgroundImage;

        public HelpPanel() {
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
    public class HelpButton extends JButton {
        public HelpButton(String text) {
            setMaximumSize(new Dimension(300, 50));
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setVerticalTextPosition(SwingConstants.CENTER);
            setHorizontalTextPosition(SwingConstants.CENTER);
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.BOLD, 20));
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
    public class LanguagePack {
        private String help;
        private String windowTitle;
        private String title;
        private String returnButtonText;

        //This method takes a String as its attribute and if it is "en" it chooses the English text, otherwise it chooses the Greek text.
        public void loadLanguage(String language) {
            if (language.equals("en")) {
                help = "ARMADA PATROLS: THE GAME\n\n" +
                        "Login/Register\n" +
                        "When entering the game, the user is greeted by the Welcome Screen where they can either log into an existing account or create a new one by pressing I'M A COMMANDER/CREATE COMMANDER. " +
                        "This leads to the Login or the Register Screen respectively, where they can enter their credentials to continue. Αlternatively, they can press CONTINUE AS GUEST to start playing without logging on. (The benefit of logging in is access to Statistics.)\n\n" +
                        "Game Setup\n" +
                        "The user can press Start Game and select the game difficulty (EASY – MEDIUM – HARD), to proceed to the Pre-Game Screen. " +
                        "There, the user must place their ships by dragging and dropping them into the grid. " +
                        "The ships are initially horizontal in the left column but can be rotated 90 degrees by pressing the ROTATE Button before they procceed with the placement to place them vertically." +
                        "Once the user has placed all 5 ships, they can press START THE GAME to begin their Patrol!\n\n" +
                        "Game Start\n" +
                        "On the game screen, the user and the computer take turns. " +
                        "The user's grid is on the left, showing their ships. The computer's grid is on the right, showing any ships the user reveals. " +
                        "The user starts first by clicking on any cell in the opponent's grid. " +
                        "If there is a ship in that cell, an explosion appears; otherwise, a splash appears to indicate a shot was fired there. " +
                        "Next, the computer fires at the user's grid. If it hits a ship, the corresponding part of the ship appears burned; otherwise, a splash appears. " +
                        "The winner is the one who sinks all the opponent's ships first. A win/loss message appears to the user. " +
                        "At any time during the game, the user can press RETURN to go back to the main screen.\n\n" +
                        "Powerup\n" +
                        "During a game, the user can choose a 2x2 bomb powerup to fire with it.\n\n" +
                        "Settings\n" +
                        "From the main screen, the user can press OPTIONS to go to the settings screen, where they can change the language from English to Greek and vice versa. " +
                        "They can also view the credits.\n\n" +
                        "Statistics\n" +
                        "From the main screen, if logged in, the user can press STATISTICS to view their stats, such as wins/losses and successful shots percentage.\n";
                windowTitle = "Help";
                title = "HELP";
                returnButtonText = "RETURN";
            } else {
                help = "ΠΕΡΙΠΟΛΙΕΣ ΑΡΜΑΔΑΣ: ΤΟ ΠΑΙΧΝΙΔΙ\n\n" +
                        "Σύνδεση/Εγγραφή\n" +
                        "Όταν εισέρχεται στο παιχνίδι, ο χρήστης χαιρετίζεται από την Οθόνη Υποδοχής όπου μπορεί είτε να συνδεθεί σε υπάρχοντα λογαριασμό είτε να δημιουργήσει νέο πατώντας ΕΙΜΑΙ ΔΙΟΙΚΗΤΗΣ/ΔΗΜΙΟΥΡΓΙΑ ΔΙΟΙΚΗΤΗ. " +
                        "Αυτό οδηγεί στην Οθόνη Σύνδεσης ή στην Οθόνη Εγγραφής αντίστοιχα, όπου μπορούν να εισάγουν τα στοιχεία τους για να συνεχίσουν. Εναλλακτικά, μπορούν να πατήσουν ΣΥΝΕΧΕΙΑ ΩΣ ΕΠΙΣΚΕΠΤΗΣ για να αρχίσουν να παίζουν χωρίς να συνδεθούν. (Το πλεονέκτημα της σύνδεσης είναι η πρόσβαση στα Στατιστικά.)\n\n" +
                        "Ρύθμιση Παιχνιδιού\n" +
                        "Ο χρήστης μπορεί να πατήσει Έναρξη Παιχνιδιού και να επιλέξει τη δυσκολία του παιχνιδιού (ΕΥΚΟΛΟ – ΜΕΣΑΙΟ – ΔΥΣΚΟΛΟ), για να προχωρήσει στην Προ-Παιχνιδιού Οθόνη. " +
                        "Εκεί, ο χρήστης πρέπει να τοποθετήσει τα πλοία του σύροντας και αφήνοντάς τα στο πλέγμα. " +
                        "Τα πλοία είναι αρχικά οριζόντια στη αριστερή στήλη αλλά μπορούν να περιστραφούν κατά 90 μοίρες πατώντας το κουμπί ΠΕΡΙΣΤΡΟΦΗ πριν συνεχίσουν με την τοποθέτηση για να τα τοποθετήσουν κάθετα." +
                        "Μόλις ο χρήστης τοποθετήσει και τα 5 πλοία, μπορεί να πατήσει ΕΝΑΡΞΗ ΤΟΥ ΠΑΙΧΝΙΔΙΟΥ για να ξεκινήσει την Περιπολία του!\n\n" +
                        "Έναρξη Παιχνιδιού\n" +
                        "Στην οθόνη του παιχνιδιού, ο χρήστης και ο υπολογιστής παίζουν εναλλάξ. " +
                        "Το πλέγμα του χρήστη είναι στα αριστερά, δείχνοντας τα πλοία του. Το πλέγμα του υπολογιστή είναι στα δεξιά, δείχνοντας όποια πλοία αποκαλύπτει ο χρήστης. " +
                        "Ο χρήστης ξεκινά πρώτος πατώντας σε οποιοδήποτε κελί στο πλέγμα του αντιπάλου. " +
                        "Αν υπάρχει πλοίο σε αυτό το κελί, εμφανίζεται έκρηξη· αλλιώς, εμφανίζεται ένα κύμα για να υποδηλώσει ότι πυροβολήθηκε εκεί. " +
                        "Στη συνέχεια, ο υπολογιστής πυροβολεί στο πλέγμα του χρήστη. Αν χτυπήσει πλοίο, το αντίστοιχο μέρος του πλοίου εμφανίζεται καμένο· αλλιώς, εμφανίζεται ένα κύμα. " +
                        "Ο νικητής είναι αυτός που θα βυθίσει πρώτος όλα τα πλοία του αντιπάλου. Εμφανίζεται ένα μήνυμα νίκης/ήττας στον χρήστη. " +
                        "Σε οποιαδήποτε στιγμή κατά τη διάρκεια του παιχνιδιού, ο χρήστης μπορεί να πατήσει ΕΠΙΣΤΡΟΦΗ για να επιστρέψει στην κύρια οθόνη.\n\n" +
                        "Ενίσχυση\n" +
                        "Κατά τη διάρκεια του παιχνιδιού, ο χρήστης μπορεί να επιλέξει μια ενίσχυση βόμβας 2x2 για να πυροβολήσει με αυτήν.\n\n" +
                        "Ρυθμίσεις\n" +
                        "Από την κύρια οθόνη, ο χρήστης μπορεί να πατήσει ΕΠΙΛΟΓΕΣ για να πάει στην οθόνη ρυθμίσεων, όπου μπορεί να αλλάξει τη γλώσσα από Αγγλικά σε Ελληνικά και αντίστροφα. " +
                        "Μπορεί επίσης να δει τα credits.\n\n" +
                        "Στατιστικά\n" +
                        "Από την κύρια οθόνη, αν είναι συνδεδεμένος, ο χρήστης μπορεί να πατήσει ΣΤΑΤΙΣΤΙΚΑ για να δει τα στατιστικά του, όπως νίκες/ήττες και ποσοστό επιτυχών βολών.\n";
                windowTitle = "Βοήθεια";
                title = "ΒΟΗΘΕΙΑ";
                returnButtonText = "ΕΠΙΣΤΡΟΦΗ";
            }
        }
    }
}