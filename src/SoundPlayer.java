import javax.sound.sampled.*;
import java.net.URL;

public class SoundPlayer {

    public static void playOnce(URL soundURL) {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(soundURL);
            Clip clip = AudioSystem.getClip();
            clip.open(stream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
