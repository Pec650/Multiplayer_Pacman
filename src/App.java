import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class App implements KeyListener {
     public JFrame window = new JFrame("Pacman");
     private MusicPlayer music = new MusicPlayer();  // <--- add this
     enum AppStates {
         START, GAME, WIN
     }
     AppStates currentState = AppStates.START;
     private int tileSize = 32;
     private int rowCount = 21;
     private int columnCount = 19;
     private int windowWidth = tileSize * columnCount;
     private int windowHeight = tileSize * rowCount;

     final int actualWindowWidth = windowWidth + 600;
     final int actualWindowHeight = windowHeight + 50;

     private Settings appSettings = new Settings();

     private static boolean startMusic = true;

     public static void main(String[] args) {
         App app = new App();   // <-- create App instance
         app.init();            // <-- prepare window + key listener
     }

     public void init() {
         window.setSize(actualWindowWidth, actualWindowHeight);
         window.setResizable(false);
         window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         window.addKeyListener(this);
         startMenu();
     }

     public void startMenu() {
         currentState = AppStates.START;
         resetScreen();

         music.playLoop(getClass().getResource("SoundEffects/begin.wav"));

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
                 try {
                     Font pixelFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("Fonts/ByteBounce.ttf")).deriveFont(40f);
                     GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pixelFont);
                     g.setFont(pixelFont);
                 } catch (Exception e) {
                     e.printStackTrace();
                     g.setFont(new Font("Arial", Font.BOLD, 20));
                 }

                 FontMetrics fm = g.getFontMetrics();
                 int textWidth = fm.stringWidth("PRESS SPACE TO START");
                 int textHeight = fm.getAscent();  // height above the baseline

                 int x = (getWidth() - textWidth) / 2;
                 int y = (getHeight() + textHeight+100) / 2;

                 g.drawString("PRESS SPACE TO START", x, y);

             }
         };

         startScreen.setBackground(Color.BLACK);
         window.add(startScreen);

         window.setLocationRelativeTo(null);
         window.setVisible(true);
     }

     public void startGame() {

         currentState = AppStates.GAME;
         resetScreen();

         window.setLayout(new GridBagLayout());
         GridBagConstraints gbc = new GridBagConstraints();
         gbc.gridx = 1;
         gbc.gridy = 0;
         gbc.weightx = 1.0;
         gbc.weighty = 1.0;
         gbc.anchor = GridBagConstraints.CENTER;

         Game pacmanGame = new Game(tileSize, rowCount, columnCount, windowWidth, windowHeight, this);
         pacmanGame.setBorder(BorderFactory.createLineBorder(Color.BLUE));
         window.add(pacmanGame, gbc);
         pacmanGame.requestFocus();

         pacmanGame.requestFocusInWindow();
         window.setVisible(true);

     }

     private void resetScreen() {
         window.getContentPane().setBackground(Color.BLACK);
         Point windowPos = window.getLocation();
         window.setLayout(new BorderLayout());
         window.getContentPane().removeAll();
         window.revalidate();
         window.repaint();
         window.setLocation(windowPos);
     }

     public void endGame(int winner) { // 1: Pacman Wins | 2: Ghost Wins
         currentState = AppStates.WIN;
         window.requestFocus();
         resetScreen();

         JPanel winScreen = new JPanel() {

             BufferedImage img;

             // Load the image once
             {
                 try {
                     String dir = (winner == 1) ? "./Sprites/Pacman/Right.png" : "./Sprites/RedGhost/Idle.png";
                     img = ImageIO.read(getClass().getResource(dir));
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             }


             @Override
             protected void paintComponent(Graphics g) {
                 super.paintComponent(g);

                 if (img != null) {
                     int pwidth = img.getWidth() * 4;   // original width
                     int pheight = img.getHeight() * 4; // original height

                     int imgX = (getWidth() - pwidth) / 2;   // center image horizontally
                     int imgY = (getHeight() - pheight) / 2 - 50; // slightly above center
                     g.drawImage(img, imgX, imgY, pwidth, pheight, null); // width=64, height=64
                 }

                 g.setColor(Color.WHITE);
                 try {
                     Font pixelFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("Fonts/ByteBounce.ttf")).deriveFont(40f);
                     GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pixelFont);
                     g.setFont(pixelFont);
                 } catch (Exception e) {
                     e.printStackTrace();
                     g.setFont(new Font("Arial", Font.BOLD, 20));
                 }

                 FontMetrics fm = g.getFontMetrics();
                 String winText = (winner == 1) ? "PACMAN WINNER" : "GHOST WINNER";
                 int textWidth = fm.stringWidth(winText);
                 int textHeight = fm.getAscent();  // height above the baseline

                 int x = (getWidth() - textWidth) / 2;
                 int y = (getHeight() + textHeight+100) / 2;

                 g.drawString(winText, x, y);

                 fm = g.getFontMetrics();
                 textWidth = fm.stringWidth("PRESS SPACE TO CONTINUE");
                 textHeight = fm.getAscent();  // height above the baseline

                 x = (getWidth() - textWidth) / 2;
                 y = (getHeight() + textHeight+200) / 2;

                 g.drawString("PRESS SPACE TO CONTINUE", x, y);

             }
         };

         winScreen.setBackground(Color.BLACK);
         window.add(winScreen);

         window.setLocationRelativeTo(null);
         window.setVisible(true);
     }

     @Override
     public void keyPressed(KeyEvent e) {
         if (e.getKeyCode() == KeyEvent.VK_T) {
             appSettings.toggleFullScreen(window);
         }

         switch (currentState) {
             case START:
                 if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                     music.stop();
                     startGame();
                 }
                 break;
             case WIN:
                 if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                     music.stop();
                     startMenu();
                 }
                 break;
         }
     }

     @Override
     public void keyReleased(KeyEvent e) {}

     @Override
     public void keyTyped(KeyEvent e) {}
}
