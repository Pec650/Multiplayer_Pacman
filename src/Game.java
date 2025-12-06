import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Objects;

public class Game extends JPanel implements ActionListener, KeyListener {

    private Settings appSettings;

    private final int FPS = 24;
    private int tileSize;
    private int rowCount;
    private int columnCount;
    private int windowWidth;
    private int windowHeight;
    private App app;

    private int delay = 0;
    private int curTime = 120; // 2 minutes
    private JPanel timeText;
    JLabel scoreTextLabel;
    private JPanel scoreText;
    JLabel timeTextLabel;

    private Image wallImage;

    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
            "XXXXXXXXXOXXXXXXXXX",
            "X       X X       X",
            "X*XX XX X X XX XX*X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXOXX X XXXX",
            "O      OOGOO      O",
            "XXXX X XXOXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X       X    X",
            "X*XXXXX X X XXXXX*X",
            "X       X X       X",
            "XXXXXXXXXOXXXXXXXXX"
    };

    private HashSet<Tile> walls;
    private HashSet<Tile> foods;
    private HashSet<PowerPellet> powerPelletes;
    private Ghost ghost;
    private Pacman pacman;

    private int score = 0;
    private boolean gameOver = false;

    PauseOverlay overlay;
    private boolean isPaused = false;

    private Timer gameLoop;

    Game(int tileSize, int rowCount, int columnCount, int windowWidth, int windowHeight, App app) {
        this.tileSize = tileSize;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.app = app;

        appSettings = new Settings();

        setPreferredSize(new Dimension(windowWidth, windowHeight));
        setMinimumSize(new Dimension(windowWidth, windowHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        wallImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Sprites/Walls/wall.png"))).getImage();

        loadMap();

        overlay = new PauseOverlay();
        app.window.setGlassPane(overlay);

        timeText = new JPanel();
        timeText.setBackground(Color.BLACK);
        timeText.setLayout(new BorderLayout());
        int panelWidth = 300;
        Dimension fixedSize = new Dimension(panelWidth, 0);
        timeText.setPreferredSize(fixedSize);
        timeText.setMinimumSize(fixedSize);
        timeText.setMaximumSize(fixedSize);

        scoreText = new JPanel();
        scoreText.setBackground(Color.BLACK);
        scoreText.setLayout(new BorderLayout());
        scoreText.setPreferredSize(fixedSize);
        scoreText.setMinimumSize(fixedSize);
        scoreText.setMaximumSize(fixedSize);

        timeTextLabel = new JLabel();
        updateTimeText();
        timeTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeTextLabel.setVerticalAlignment(SwingConstants.CENTER);
        try {
            Font pixelFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/Fonts/ByteBounce.ttf")).deriveFont(40f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pixelFont);
            timeTextLabel.setFont(pixelFont);
        } catch (Exception e) {
            e.printStackTrace();
            timeTextLabel.setFont(new Font("Arial", Font.BOLD, 25));
        }
        timeText.add(timeTextLabel, BorderLayout.CENTER);

        scoreTextLabel = new JLabel();
        updateScoreText();
        scoreTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreTextLabel.setVerticalAlignment(SwingConstants.CENTER);
        try {
            Font pixelFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/Fonts/ByteBounce.ttf")).deriveFont(40f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pixelFont);
            scoreTextLabel.setFont(pixelFont);
        } catch (Exception e) {
            e.printStackTrace();
            scoreTextLabel.setFont(new Font("Arial", Font.BOLD, 25));
        }
        scoreText.add(scoreTextLabel, BorderLayout.CENTER);

        GridBagConstraints gbcText = new GridBagConstraints();
        gbcText.gridx = 0;
        gbcText.gridy = 0;
        gbcText.weightx = 1;
        gbcText.anchor = GridBagConstraints.CENTER;
        gbcText.fill = GridBagConstraints.VERTICAL;
        app.window.add(timeText, gbcText);

        gbcText.gridx = 2;
        app.window.add(scoreText, gbcText);

        gameLoop = new Timer(1000 / FPS, this);
        gameLoop.start();
    }

    public void loadMap() {
        walls = new HashSet<>();
        foods = new HashSet<>();
        powerPelletes = new HashSet<>();

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c * tileSize;
                int y = r * tileSize;

                switch (tileMapChar) {
                    case 'X':
                        walls.add(new Tile(wallImage, x, y, tileSize, tileSize));
                        break;
                    case 'G':
                        ghost = new Ghost(Ghost.GhostColor.RED, x, y, tileSize, tileSize);
                        break;
                    case 'P':
                        pacman = new Pacman(x, y, tileSize, tileSize);
                        break;
                    case '*':
                        powerPelletes.add(new PowerPellet(x, y, tileSize, tileSize));
                    case ' ':
                        foods.add(new Tile(null, x + 14, y + 14, 4, 4));
                        break;
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        for (Tile wall : walls) {
            g.drawImage(wall.sprite, wall.x, wall.y, wall.width, wall.height, null);
        }

        g.setColor(Color.white);
        for (Tile food : foods) {
            g.fillRect(food.x, food.y, food.width, food.height);
        }

        for (PowerPellet pallete : powerPelletes) {
            g.drawImage(pallete.sprite, pallete.x, pallete.y, pallete.width, pallete.height, null);
            pallete.updateSprites();
        }

        g.drawImage(pacman.sprite, pacman.x, pacman.y, pacman.width, pacman.height, null);
        g.drawImage(ghost.sprite, ghost.x, ghost.y, ghost.width, ghost.height, null);
        if (!isPaused) {
            pacman.updateSprites();
            ghost.updateSprites();
        }
    }

    private void move() {
        if (foods.isEmpty()) {
            endGame(1);
            return;
        }

        boolean isMoving;

        if (ghost.controlDirection != null) {
            ghost.updateDirection(ghost.controlDirection, walls);
        }

        if (pacman.controlDirection != null && !borderReached(pacman)) {
            pacman.updateDirection(pacman.controlDirection, walls);
        }

        // <-- GHOST MOVEMENT --> //
        ghost.x += ghost.velocityX;
        ghost.y += ghost.velocityY;

        isMoving = true;

        if (borderReached(ghost)) {
            ghost.x -= ghost.velocityX;
            ghost.y -= ghost.velocityY;
            ghost.updateState(Ghost.States.IDLE);
            isMoving = false;
        } else {
            for (Tile wall : walls) {
                if (ghost.collided(wall)) {
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    ghost.updateState(Ghost.States.IDLE);
                    isMoving = false;
                    break;
                }
            }
        }

        if (isMoving && !(ghost.velocityX == 0 && ghost.velocityY == 0)) {
            ghost.updateState(Ghost.States.MOVE);
        }

        // <-- PACMAN MOVEMENT --> //
        if (pacman.collided(ghost)) {

            if (ghost.isScared()) {
                SoundPlayer.playOnce(App.class.getResource("/SoundEffects/tasty.wav"));
                ghost.die();
                score += 200;
            } else if (!ghost.isRespawning()) {
                SoundPlayer.playOnce(App.class.getResource("/SoundEffects/pacman_death.wav"));
                pacman.loseLife();
                if (pacman.getLives() == 0) {
                    endGame(2);
                    return;
                }
                resetPositions();
            }

        }

        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        /* PACMAN TELEPORT */
        if (pacman.x < -tileSize) {
            pacman.x = windowWidth + tileSize;
            SoundPlayer.playOnce(App.class.getResource("/SoundEffects/dbz.wav"));
        } else if (pacman.x > windowWidth + tileSize) {
            pacman.x = -tileSize;
            SoundPlayer.playOnce(App.class.getResource("/SoundEffects/dbz.wav"));

        }

        if (pacman.y < -tileSize) {
            pacman.y = windowHeight + tileSize;
            SoundPlayer.playOnce(App.class.getResource("/SoundEffects/dbz.wav"));

        } else if (pacman.y > windowHeight + tileSize) {
            pacman.y = -tileSize;
            SoundPlayer.playOnce(App.class.getResource("/SoundEffects/dbz.wav"));

        }

        isMoving = true;
        for (Tile wall : walls) {
            if (pacman.collided(wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                pacman.updateState(Pacman.States.IDLE);
                isMoving = false;
                break;
            }
        }

        if (isMoving && !(pacman.velocityX == 0 && pacman.velocityY == 0)) {
            pacman.updateState(Pacman.States.MOVE);
        }


        Tile foodEaten = null;
        for (Tile food : foods) {
            if (pacman.collided(food)) {
                foodEaten = food;
                score += 10;
            }
        }
        foods.remove(foodEaten);

        PowerPellet eatenPalette = null;
        for (PowerPellet pallete : powerPelletes) {
            if (pacman.collided(pallete)) {
                SoundPlayer.playOnce(App.class.getResource("/SoundEffects/pacman_eatfruit.wav"));
                eatenPalette = pallete;
                score += 20;
                ghost.setAsScared();
            }
        }
        powerPelletes.remove(eatenPalette);

    }

    public boolean borderReached(Entity a) {
        return a.x < tileSize || a.x > windowWidth - tileSize * 2 ||
                a.y < tileSize || a.y > windowHeight - tileSize * 2;
    }

    private void updateTimeText() {
        if (curTime < 0) {
            endGame(2);
        }
        int minutes = curTime / 60;
        int seconds = curTime % 60;
        curTime -= 1;
        timeTextLabel.setText("<html><span style='color:white;'>Time: " + minutes + ":" + String.format("%02d", seconds)  + "</span></html>");
        timeText.revalidate();
        timeText.repaint();
    }

    private void updateScoreText() {
        scoreTextLabel.setText("<html><span style='color:white;'>Lives: " + pacman.getLives() + "<br><br>Score: " + score + "</span></html>");
        scoreTextLabel.revalidate();
        scoreTextLabel.repaint();
    }

    public void resetPositions() {
        ghost.reset();
        pacman.reset();
    }

    private void triggerPause() {
        if (!isPaused) {
            app.window.getGlassPane().setVisible(true);
            gameLoop.stop();
            ghost.stop();
            pacman.stop();
        } else {
            app.window.getGlassPane().setVisible(false);
            gameLoop.start();
        }
        isPaused = !isPaused;
    }

    private void endGame(int winner) { // 1: Pacman Wins | 2: Ghost Wins
        gameOver = true;
        pacman.updateState(Pacman.States.IDLE);
        pacman.stop();
        ghost.updateState(Ghost.States.END);
        ghost.stop();
        for (PowerPellet pallete : powerPelletes) {
            pallete.endAnim();
        }
        gameLoop.stop();
        switch (winner) {
            case 1:
            case 2:
                app.endGame(winner);
                break;
            default:
                app.startMenu();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isPaused) {
            if ((++delay) >= FPS) {
                updateTimeText();
                delay = 0;
            }
            if (!gameOver) {
                updateScoreText();
                move();
            }
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_T) {
            appSettings.toggleFullScreen(app.window);
        }

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            triggerPause();
        }

        if (!gameOver) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    ghost.controlDirection = Entity.Direction.U;
                    break;
                case KeyEvent.VK_S:
                    ghost.controlDirection = Entity.Direction.D;
                    break;
                case KeyEvent.VK_A:
                    ghost.controlDirection = Entity.Direction.L;
                    break;
                case KeyEvent.VK_D:
                    ghost.controlDirection = Entity.Direction.R;
                    break;
            }

            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    pacman.controlDirection = Entity.Direction.U;
                    break;
                case KeyEvent.VK_DOWN:
                    pacman.controlDirection = Entity.Direction.D;
                    break;
                case KeyEvent.VK_LEFT:
                    pacman.controlDirection = Entity.Direction.L;
                    break;
                case KeyEvent.VK_RIGHT:
                    pacman.controlDirection = Entity.Direction.R;
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
