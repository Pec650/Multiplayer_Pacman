import javax.swing.*;
import java.awt.*;

public class PauseOverlay extends JPanel {
    public PauseOverlay() {
        setOpaque(false);

        setLayout(new GridBagLayout());
        JLabel paused = new JLabel("PAUSED");

        try {
            Font pixelFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("Fonts/ByteBounce.ttf")).deriveFont(60f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pixelFont);
            paused.setFont(pixelFont);
        } catch (Exception e) {
            e.printStackTrace();
            paused.setFont(new Font("Arial", Font.BOLD, 40));
        }

        paused.setForeground(Color.WHITE);
        add(paused);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw dark transparent overlay
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
