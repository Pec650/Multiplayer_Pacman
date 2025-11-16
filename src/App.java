import javax.swing.*;
import java.awt.*;

public class App {
    static public void main(String[] args) {
        int tileSize = 32;
        int rowCount = 21;
        int columnCount = 19;
        int windowWidth = tileSize * columnCount;
        int windowHeight = tileSize * rowCount;

        JFrame window = new JFrame("Pacman");
        window.getContentPane().setBackground(Color.BLACK);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(windowWidth, windowHeight);
        window.setResizable(true);

        window.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;

        Game pacmanGame = new Game(tileSize, rowCount, columnCount, windowWidth, windowHeight);
        window.add(pacmanGame, gbc);
        window.pack();
        window.getContentPane().setMinimumSize(window.getContentPane().getSize());
        window.setMinimumSize(window.getSize());

        pacmanGame.requestFocus();

        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}
