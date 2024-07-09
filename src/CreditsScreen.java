import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class CreditsScreen extends JFrame {
    private JLabel title;
    private JPanel panel;
    private JButton returnButton;
    private String username;
    private String language;
    private LanguagePack languagePack;
    private JTextArea creditsTextArea;

    public CreditsScreen(String username, String language) {
        panel = new CreditsPanel();
    
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
    
        //Adds the credits into a ScrollPane.
        creditsTextArea = new JTextArea(languagePack.credits);
        creditsTextArea.setFont(new Font("Arial", Font.PLAIN, 20));
        creditsTextArea.setForeground(Color.WHITE);
        creditsTextArea.setBackground(new Color(0, 0, 0, 0));
        creditsTextArea.setOpaque(false);
        creditsTextArea.setEditable(false);
        creditsTextArea.setLineWrap(true);
        creditsTextArea.setWrapStyleWord(true);
        creditsTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        JScrollPane scrollPane = new JScrollPane(creditsTextArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    
        //Creates the Return Button.
        returnButton = new CreditsButton(languagePack.returnButtonText);
        //Adds ActionListener to Return Button.
        returnButton.addActionListener(new ReturnButtonListener());

        //Adds the screen formatting.
        panel.add(title);
        panel.add(Box.createHorizontalStrut(50));
        panel.add(scrollPane);
        panel.add(Box.createVerticalStrut(50));
        panel.add(returnButton);
    
        getContentPane().add(panel);
    
        setTitle(languagePack.windowTitle);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }    

    //Extends the JPanel class to contain the background.
    public class CreditsPanel extends JPanel {
        private Image backgroundImage;

        public CreditsPanel() {
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
            SwingUtilities.invokeLater(() -> new OptionsScreen(username, language));
            dispose();
        }
    }

    //Extends the JButton class to contain the Button formatting.
    public class CreditsButton extends JButton {
        public CreditsButton(String text) {
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
                        "General Coordination\n" +
                        "Petros Baloglou (ics23024)\n\n" +
                        "Analysis Coordination\n" +
                        "Kiriaki-Maria Kadrefi (ics23153)\n\n" +
                        "Analysts\n" +
                        "Aikaterini Argyriou (ics23002)\n" +
                        "Andreas Demirtzoglou (ics23169)\n" +
                        "Nikolaos Stefanidis (ics23003)\n\n" +
                        "Development Coordination\n" +
                        "Vasileios-Rafail Avramidis (ics23033)\n\n" +
                        "Developers\n" +
                        "Konstantinos Moukas (ics23001)\n" +
                        "Apostolos Moridis (ics23094)\n" +
                        "Nikolaos Nikolaidis (ics23080)\n\n" +
                        "Interface Design Coordination\n" +
                        "Rafailia-Aglaia Kotsiopoulou (ics23059)\n\n" +
                        "Interface Designers\n" +
                        "Christos Bakadimas (ics24141)\n" +
                        "Panagiota Baimaki (ics23087)\n" +
                        "Rafail-Efstathios Mystakidis (ics23092)\n\n" +
                        "Testing Coordination\n" +
                        "Dimitrios Mauroudis (ics23109)\n\n" +
                        "Testers\n" +
                        "Ioanna Papatriantafyllou (ics23037)\n" +
                        "Michail Chatzileontiadis (ics22191)\n\n" +
                        "Music Composition\n" +
                        "Ashma Band ©\n" +
                        "Petros Baloglou (ics23024)";
                windowTitle = "Credits";
                title = "CREDITS";
                returnButtonText = "RETURN";
            } else {
                credits = "ARMADA PATROLS: THE GAME\n\n" +
                        "Γενική Καθοδήγηση\n" +
                        "Πέτρος Μπάλογλου (ics23024)\n\n" +
                        "Καθοδήγηση Αναλυτών\n" +
                        "Κυριακή-Μαρία Καδρέφη (ics23153)\n\n" +
                        "Αναλυτές\n" +
                        "Αικατερίνη Αργυρίου (ics23002)\n" +
                        "Ανδρέας Δεμιρτζόγλου (ics23169)\n" +
                        "Νικόλαος Στεφανίδης (ics23003)\n\n" +
                        "Καθοδήγηση Developer\n" +
                        "Βασίλειος-Ραφαήλ Αβραμίδης (ics23033)\n\n" +
                        "Developers\n" +
                        "Κωνσταντίνος Μούκας (ics23001)\n" +
                        "Απόστολος Μορίδης (ics23094)\n" +
                        "Νικόλαος Νικολαίδης (ics23080)\n\n" +
                        "Καθοδήγηση Σχεδιασμού Γραφικής Διασύνδεσης\n" +
                        "Ραφαηλία-Αγλαΐα Κωτσιοπούλου (ics23059)\n\n" +
                        "Σχεδιαστές Γραφικής Διασύνδεσης\n" +
                        "Χρήστος Μπακαδήμας (ics24141)\n" +
                        "Παναγιώτα Μπαιμάκη (ics23087)\n" +
                        "Ραφαήλ-Ευστάθιος Μυστακίδης (ics23092)\n\n" +
                        "Καθοδήγηση Testing\n" +
                        "Δημήτριος Μαυρούδης (ics23109)\n\n" +
                        "Testers\n" +
                        "Ιωάννα Παπατριανταφύλλου (ics23037)\n" +
                        "Μιχαήλ Χατζηλεοντιάδης (ics22191)\n\n" +
                        "Μουσική Σύνθεση\n" +
                        "Ashma Band ©\n" +
                        "Πέτρος Μπάλογου (ics23024)";
                windowTitle = "Credits";
                title = "CREDITS";
                returnButtonText = "ΕΠΙΣΤΡΟΦΗ";
            }
        }
    }
}