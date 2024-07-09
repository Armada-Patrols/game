import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {
    private static MusicPlayer instance = null;
    private Clip clip;

    public MusicPlayer() {}

    public static MusicPlayer getInstance() {
        if (instance == null) {
            instance = new MusicPlayer();
        }
        return instance;
    }

    public void playMusic(String filePath) {
        try {
            if (clip != null && clip.isRunning()) {
                return; //Music is already playing
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }

    public void pauseMusic() {
        if (clip != null) {
            clip.stop();
        }
    }

    public void resumeMusic() {
        if (clip != null) {
            clip.start();
        }
    }
}