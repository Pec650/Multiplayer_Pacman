import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import javax.sound.sampled.*;

public class App implements KeyListener {
    JFrame window = new JFrame("Pacman");
    MusicPlayer music = new MusicPlayer();  // <--- add this
    int tileSize = 32;
    int rowCount = 21;
    int columnCount = 19;
    int windowWidth = tileSize * columnCount;
    int windowHeight = tileSize * rowCount;
    public static boolean startMusic = true;

    public static void main(String[] args) {
        App app = new App();   // <-- create App instance
        app.init();            // <-- prepare window + key listener
    }

    public void init() {
        window.setSize(windowWidth, windowHeight);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.addKeyListener(this);
        if (startMusic) {
            music.playLoop(getClass().getResource("./Sprites/begin.wav"));
        }


        // Create start screen
        JPanel startScreen = new JPanel() {



            BufferedImage img;

            // Load the image once
            {
                try {
                    img = ImageIO.read(getClass().getResource("./Sprites/pac.png")); // path to your image
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);


                if (img != null) {
                    int pwidth = img.getWidth()/2;   // original width
                    int pheight = img.getHeight()/2; // original height

                    int imgX = (getWidth() - pwidth) / 2;   // center image horizontally
                    int imgY = (getHeight() - pheight) / 2 - 50; // slightly above center
                    g.drawImage(img, imgX, imgY, pwidth, pheight, null); // width=64, height=64
                }

                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 20));


                FontMetrics fm = g.getFontMetrics();
                int textWidth = fm.stringWidth("PRESS SPACE TO START");
                int textHeight = fm.getAscent();  // height above the baseline

                int x = (getWidth() - textWidth) / 2;
                int y = (getHeight() + textHeight+100) / 2;

                g.drawString("PRESS SPACE TO START", x, y);
                g.drawString("bab likes khit", 100, 100);


            }
        };

        startScreen.setBackground(Color.BLACK);  // optional
        window.add(startScreen);


        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }


    public void startGame() {
//         int tileSize = 32;
//         int rowCount = 21;
//         int columnCount = 19;
//         int windowWidth = tileSize * columnCount;
//         int windowHeight = tileSize * rowCount;

        window.getContentPane().removeAll();
        window.revalidate();
        window.repaint();

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
     @Override
     public void keyPressed(KeyEvent e) {
          if(e.getKeyCode() == KeyEvent.VK_SPACE)
          {
              startMusic = false;
              music.stop();
              startGame();

          }
     }
     @Override
     public void keyReleased(KeyEvent e) {

     }
     @Override
     public void keyTyped(KeyEvent e) {

     }
}
