import javax.swing.*;

public class App {
    static public void main(String[] args) {
        int tileSize = 32;
        int rowCount = 21;
        int columnCount = 19;
        int windowWidth = tileSize * columnCount;
        int windowHeight = tileSize * rowCount;

        JFrame window = new JFrame("Pacman");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(windowWidth, windowHeight);
        window.setResizable(true);
        window.setLocationRelativeTo(null);

        Game pacmanGame = new Game(tileSize, rowCount, columnCount, windowWidth, windowHeight);
        window.add(pacmanGame);
        window.pack();
        pacmanGame.requestFocus();

        window.setVisible(true);
    }
}
