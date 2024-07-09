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
        creditsTextArea = new JTextArea(languagePack.credits);
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
        private String credits;
        private String windowTitle;
        private String title;
        private String returnButtonText;

        //This method takes a String as its attribute and if it is "en" it chooses the English text, otherwise it chooses the Greek text.
        public void loadLanguage(String language) {
            if (language.equals("en")) {
                credits = "ARMADA PATROLS: THE GAME\n\n" +
                        "Login/Register\n" +
                        "When entering the game, the user can either log into an existing account or create a new one by pressing LOGIN/REGISTER. " +
                        "This leads to the Welcome Screen, where the user can choose either Login or Register. (The benefit of logging in is access to Statistics.)\n\n" +
                        "Game Setup\n" +
                        "The user can press Start Game and select the game difficulty (Easy – Medium – Hard), to proceed to the Pre-Game Screen. " +
                        "There, the user must place their ships by dragging and dropping them into the grid. " +
                        "The ships are initially horizontal in the left column but can be rotated 90 degrees by pressing the ROTATE Button to place them vertically. " +
                        "Once the user has placed all 5 ships, they can press START THE GAME to begin their Patrol!\n\n" +
                        "Game Start\n" +
                        "On the game screen, the user and the computer take turns. " +
                        "The user's grid is on the left, showing their ships. The computer's grid is on the right, showing any ships the user reveals. " +
                        "The user starts first by clicking on any cell in the opponent's grid. " +
                        "If there is a ship in that cell, an explosion appears; otherwise, a splash appears to indicate a shot was fired there. " +
                        "Next, the computer fires at the user's grid. If it hits a ship, the corresponding part of the ship appears burned; otherwise, a splash appears. " +
                        "The winner is the one who sinks all the opponent's ships first. A win/loss message appears to the user. " +
                        "At any time during the game, the user can press RETURN to go back to the main screen.\n\n" +
                        "Game Continuation\n" +
                        "If the game is interrupted, the user can press CONTINUE GAME from the main screen to resume from where they left off.\n\n" +
                        "Powerups\n" +
                        "During a game, the user can choose to use a powerup to fire with it.\n\n" +
                        "Settings\n" +
                        "From the main screen, the user can press OPTIONS to go to the settings screen, where they can change the language from English to Greek and vice versa. " +
                        "They can also view the credits.\n\n" +
                        "Statistics\n" +
                        "From the main screen, if logged in, the user can press STATISTICS to view their stats, such as wins/losses, times each Powerup was used, total shots against the opponent, etc.\n";
                windowTitle = "Help";
                title = "HELP";
                returnButtonText = "RETURN";
            } else {
                credits = "ARMADA PATROLS: THE GAME\n\n" +
                        "Login/Register\n" +
                        "Ο χρήστης όταν μπαίνει στο παιχνίδι μπορεί να συνδεθεί σε υπάρχοντα λογαριασμό του ή να δημιουργήσει καινούργιο λογαριασμό πατώντας το LOGIN/REGISTER. " +
                        "Έτσι μεταβαίνει στην οθόνη Welcome Screen, και έχει την επιλογή είτε για Login, είτε για Register. (Το προνόμιο της σύνδεσης είναι η πρόσβαση σε Statistics.)\n\n" +
                        "Προετοιμασία παρτίδας\n" +
                        "Ο χρήστης μπορεί να πατήσει Start Game και να διαλέξει δυσκολία παρτίδας (Εύκολο – Μεσαίο – Δύσκολο), προκειμένου να μεταβεί στο Pre-Game Screen. " +
                        "Εκεί, πρέπει να τοποθετήσει τα πλοία του κάνοντάς τα drag-and-drop στο πλαίσιο. " +
                        "Τα πλοία βρίσκονται αρχικά οριζόντια στην αριστερή στήλη, αλλά μπορούν να περιστραφούν 90ο πατώντας το ROTATE Button προκειμένου να τοποθετηθούν κάθετα. " +
                        "Όταν ο χρήστης τοποθετήσει και τα 5 πλοία, μπορεί να πατήσει START THE GAME για να ξεκινήσει το Patrol του!\n\n" +
                        "Έναρξη παρτίδας\n" +
                        "Στην οθόνη της παρτίδας, ο χρήστης και ο υπολογιστής παίζουν εναλλάξ. " +
                        "Το grid του χρήστη βρίσκεται στα αριστερά και εκεί φαίνονται τα πλοία του. Το grid του υπολογιστή είναι στα δεξιά και εκεί θα φαίνονται όσα πλοία αποκαλύψει ο χρήστης. " +
                        "Πρώτος ξεκινά ο χρήστης, πατώντας σε οποιοδήποτε από τα κελιά του grid του αντίπαλου. " +
                        "Αν στο κελί αυτό υπάρχει πλοίο, εμφανίζεται έκρηξη στον χρήστη, ενώ αν δεν υπάρχει εμφανίζεται απλά πιτσιλιά ώστε να γνωρίζει ότι έχει πυροβολήσει εκεί. " +
                        "Σειρά έχει ο υπολογιστής, ο οποίος πυροβολεί στο grid του χρήστη. Αν βρει στόχο εμφανίζεται το αντίστοιχο κομμάτι του πλοίου καμένο, ενώ αλλιώς εμφανίζεται πιτσιλιά. " +
                        "Ο νικητής της παρτίδας είναι αυτός που βυθίζει πρώτος όλα τα αντίπαλα πλοία. Εμφανίζεται αντίστοιχο μήνυμα νίκης/ήττας στον χρήστη. " +
                        "Ανά πάσα στιγμή της παρτίδας, ο χρήστης μπορεί να πατήσει RETURN για να γυρίσει στην αρχική οθόνη.\n\n" +
                        "Συνέχιση παρτίδας\n" +
                        "Σε περίπτωση λοιπόν όπου διακοπεί πρόωρα το παιχνίδι, ο χρήστης μπορεί να πατήσει CONTINUE GAME από την αρχική οθόνη και να συνεχίσει το παιχνίδι από εκεί που το άφησε.\n\n" +
                        "Powerups\n" +
                        "Κατά τη διάρκεια μιας παρτίδας, ο χρήστης μπορεί να επιλέξει τη χρήση ενός powerup και να πυροβολήσει με αυτό.\n\n" +
                        "Ρυθμίσεις\n" +
                        "Από την αρχική οθόνη ο χρήστης μπορεί να πατήσει OPTIONS προκειμένου να μεταβεί στην οθόνη των ρυθμίσεων, απ’ όπου μπορεί να αλλάξει γλώσσα από αγγλικά σε ελληνικά και αντιστρόφως. " +
                        "Επίσης μπορεί να να δει τα credits.\n\n" +
                        "Στατιστικά\n" +
                        "Από την αρχική οθόνη ο χρήστης, αν είναι συνδεδεμένος, μπορεί να πατήσει STATISTICS και να δει τα στατιστικά του στοιχεία, π.χ. νίκες/ήττες, φορές χρήσης του κάθε Powerup, συνολικές βολές ενάντια στον αντίπαλο, κλπ.\n";
                windowTitle = "Βοήθεια";
                title = "ΒΟΗΘΕΙΑ";
                returnButtonText = "ΕΠΙΣΤΡΟΦΗ";
            }
        }
    }
}