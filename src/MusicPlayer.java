import javax.sound.sampled.*;
import java.net.URL;

public class MusicPlayer {

    private Clip clip;

    public void playLoop(URL soundURL) {
        try {
            if (clip != null && clip.isOpen()) {
                clip.stop();
                clip.close();
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            clip.loop(Clip.LOOP_CONTINUOUSLY); // <--- loops forever
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }

    public boolean isPlaying() {
        return clip != null && clip.isActive();
    }

}
