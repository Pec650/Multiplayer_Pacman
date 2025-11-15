import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Objects;

public class Game extends JPanel implements ActionListener, KeyListener {
    private final int FPS = 30;
    private int tileSize;
    private int rowCount;
    private int columnCount;
    private int windowWidth;
    private int windowHeight;

    private Image wallImage;
    private Image powerFoodImage;

    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
            "XXXXXXXXXOXXXXXXXXX",
            "X       X X       X",
            "X XX XX X X XX XX X",
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
            "X XXXXX X X XXXXX X",
            "X       X X       X",
            "XXXXXXXXX)XXXXXXXXX"
    };

    HashSet<Tile> walls;
    HashSet<Tile> foods;
    Ghost ghost;
    Pacman pacman;

    int score = 0;
    boolean gameOver = false;

    Timer gameLoop;

    Game(int tileSize, int rowCount, int columnCount, int windowWidth, int windowHeight) {
        this.tileSize = tileSize;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;

        setPreferredSize(new Dimension(windowWidth, windowHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        wallImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/wall.png"))).getImage();
        powerFoodImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/powerFood.png"))).getImage();

        loadMap();

        gameLoop = new Timer(1000 / FPS, this);
        gameLoop.start();
    }

    public void loadMap() {
        walls = new HashSet<>();
        foods = new HashSet<>();

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
                        ghost = new Ghost(Ghost.GhostColor.BLUE, x, y, tileSize, tileSize);
                        break;
                    case 'P':
                        pacman = new Pacman(x, y, tileSize, tileSize);
                        break;
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

        g.drawImage(ghost.sprite, ghost.x, ghost.y, ghost.width, ghost.height, null);
        g.drawImage(pacman.sprite, pacman.x, pacman.y, pacman.width, pacman.height, null);

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if (gameOver) {
            g.drawString("Game Over: " + score, tileSize/2, tileSize/2);
        } else {
            g.drawString("x" + pacman.getLives() + " | Score: " + score, tileSize/2, tileSize/2);
        }
    }

    public void move() {
        if (ghost.controlDirection != null) {
            ghost.updateDirection(ghost.controlDirection, walls);
        }

        if (pacman.controlDirection != null && !borderReached(pacman)) {
            pacman.updateDirection(pacman.controlDirection, walls);
        }

        // <-- GHOST MOVEMENT --> //
        ghost.x += ghost.velocityX;
        ghost.y += ghost.velocityY;

        if (borderReached(ghost)) {
            ghost.x -= ghost.velocityX;
            ghost.y -= ghost.velocityY;
        } else {
            for (Tile wall : walls) {
                if (ghost.collided(wall)) {
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    break;
                }
            }
        }

        // <-- PACMAN MOVEMENT --> //
        if (pacman.collided(ghost)) {
            pacman.loseLife();
            if (pacman.getLives() == 0) {
                gameOver = true;
                return;
            }
            resetPositions();
        }

        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        /* PACMAN TELEPORT */
        if (pacman.x < -tileSize) {
            pacman.x = windowWidth + tileSize;
        } else if (pacman.x > windowWidth + tileSize) {
            pacman.x = -tileSize;
        }

        if (pacman.y < -tileSize) {
            pacman.y = windowHeight + tileSize;
        } else if (pacman.y > windowHeight + tileSize) {
            pacman.y = -tileSize;
        }

        for (Tile wall : walls) {
            if (pacman.collided(wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }

        Tile foodEaten = null;
        for (Tile food : foods) {
            if (pacman.collided(food)) {
                foodEaten = food;
                score += 10;
            }
        }
        foods.remove(foodEaten);
    }

    public boolean borderReached(Entity a) {
        return a.x < tileSize || a.x > windowWidth - tileSize * 2 ||
                a.y < tileSize || a.y > windowHeight - tileSize * 2;
    }

    public void resetPositions() {
        ghost.reset();
        pacman.reset();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Updates every 20 frames per second
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            loadMap();
            resetPositions();
            pacman.resetLife();
            score = 0;
            gameOver = false;
            gameLoop.start();
        }

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

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
