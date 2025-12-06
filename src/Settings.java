import javax.swing.*;
import java.awt.*;

public class Settings {
    private GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

    public void toggleFullScreen(JFrame window) {
        if (device.getFullScreenWindow() != window) {
            if (device.isFullScreenSupported()) {
                window.dispose();
                window.setUndecorated(true);
                device.setFullScreenWindow(window);
                window.setVisible(true);
            }
        } else {
            device.setFullScreenWindow(null);
            window.dispose();
            window.setUndecorated(false);
            window.setVisible(true);
            window.repaint();
        }
    }

    public void forceMinScreen(JFrame window) {
        if (device.getFullScreenWindow() == window) {
            device.setFullScreenWindow(null);
            window.dispose();
            window.setUndecorated(false);
            window.setVisible(true);
            window.repaint();
        }
    }
}
