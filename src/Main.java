import javax.swing.SwingUtilities;

public class Main{    
	public static void main(String[] args) {
		//addition of music
		//new Thread(() -> MusicPlayer.getInstance().playMusic("music/ArmadaInstrumental.wav")).start();
		//The first screen of the game sets English as default language (the user can set Greek as default language too)
	    SwingUtilities.invokeLater(() -> new WelcomeScreen("en")); 
	}
}