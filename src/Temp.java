//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.KeyEvent;
//import java.util.HashSet;
//import java.util.Objects;
//import java.util.Random;
//
//public class Temp {
//    class Block {
//        int x;
//        int y;
//        int width;
//        int height;
//        Image sprite;
//
//        int startX;
//        int startY;
//        char direction = 'U'; // U - UP, D - DOWN, L - LEFT, R - RIGHT
//        int velocityX = 0;
//        int velocityY = 0;
//
//        Block(Image sprite, int x, int y, int width, int height) {
//            this.sprite = sprite;
//            this.x = x;
//            this.y = y;
//            this.width = width;
//            this.height = height;
//
//            this.startX = x;
//            this.startY = y;
//        }
//
//        void updateDirection(char direction) {
//            char prevDirection = this.direction;
//            this.direction = direction;
//            updateVelocity();
//            this.x += this.velocityX;
//            this.y += this.velocityY;
//            for (Game.Block wall : walls) {
//                if (collision(this, wall)) {
//                    this.x -= this.velocityX;
//                    this.y -= this.velocityY;
//                    this.direction = prevDirection;
//                    updateVelocity();
//                }
//            }
//        }
//
//        void updateVelocity() {
//            int speed = tileSize / 4;
//            switch (this.direction) {
//                case 'U': // UP
//                    this.velocityX = 0;
//                    this.velocityY = -speed;
//                    break;
//                case 'D': // DOWN
//                    this.velocityX = 0;
//                    this.velocityY = speed;
//                    break;
//                case 'L': // LEFT
//                    this.velocityX = -speed;
//                    this.velocityY = 0;
//                    break;
//                case 'R': // RIGHT
//                    this.velocityX = speed;
//                    this.velocityY = 0;
//                    break;
//            }
//        }
//
//        void reset() {
//            this.x = this.startX;
//            this.y = this.startY;
//        }
//    }
//
//    private int tileSize;
//    private int rowCount;
//    private int columnCount;
//    private int windowWidth;
//    private int windowHeight;
//
//    private Image wallImage;
//    private Image powerFoodImage;
//    private Image blueGhostImage;
//    private Image orangeGhostImage;
//    private Image pinkGhostImage;
//    private Image redGhostImage;
//
//    private Image pacmanUpImage;
//    private Image pacmanDownImage;
//    private Image pacmanLeftImage;
//    private Image pacmanRightImage;
//
//    //X = wall, O = skip, P = pac man, ' ' = food
//    //Ghosts: b = blue, o = orange, p = pink, r = red
//    private String[] tileMap = {
//            "XXXXXXXXXXXXXXXXXXX",
//            "X        X        X",
//            "X XX XXX X XXX XX X",
//            "X                 X",
//            "X XX X XXXXX X XX X",
//            "X    X       X    X",
//            "XXXX XXXX XXXX XXXX",
//            "OOOX X       X XOOO",
//            "XXXX X XXOXX X XXXX",
//            "O      OOGOO      O",
//            "XXXX X XXXXX   XXXX",
//            "OOOX X         XOOO",
//            "XXXX X XXXXX X XXXX",
//            "X        X        X",
//            "X XX XXX X XXX XX X",
//            "X  X     P     X  X",
//            "XX X X XXXXX X X XX",
//            "X    X   X   X    X",
//            "X XXXXXX X XXXXXX X",
//            "X                 X",
//            "XXXXXXXXXXXXXXXXXXX"
//    };
//
//    HashSet<Game.Block> walls;
//    HashSet<Game.Block> foods;
//    Game.Block ghost;
//    Game.Block pacman;
//    int score = 0;
//    int lives = 3;
//    boolean gameOver = false;
//
//    Timer gameLoop;
//    char[] directions = {'U', 'D', 'L', 'R'};
//    Random random = new Random();
//
//    Game(int tileSize, int rowCount, int columnCount, int windowWidth, int windowHeight) {
//        this.tileSize = tileSize;
//        this.rowCount = rowCount;
//        this.columnCount = columnCount;
//        this.windowWidth = windowWidth;
//        this.windowHeight = windowHeight;
//
//        setPreferredSize(new Dimension(windowWidth, windowHeight));
//        setBackground(Color.black);
//        addKeyListener(this);
//        setFocusable(true);
//
//        wallImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/wall.png"))).getImage();
//        powerFoodImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/powerFood.png"))).getImage();
//        blueGhostImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/blueGhost.png"))).getImage();
//        orangeGhostImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/orangeGhost.png"))).getImage();
//        pinkGhostImage =  new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/pinkGhost.png"))).getImage();
//        redGhostImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/redGhost.png"))).getImage();
//
//        pacmanUpImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/pacmanUp.png"))).getImage();
//        pacmanDownImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/pacmanDown.png"))).getImage();
//        pacmanLeftImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/pacmanLeft.png"))).getImage();
//        pacmanRightImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./Sprites/pacmanRight.png"))).getImage();
//
//        loadMap();
////
////        char newDirection = directions[random.nextInt(4)];
////        pacman.updateDirection(newDirection);
//
//        gameLoop = new Timer(50, this);
//        gameLoop.start();
//    }
//
//    public void loadMap() {
//        walls = new HashSet<Game.Block>();
//        foods = new HashSet<Game.Block>();
//
//        for (int r = 0; r < rowCount; r++) {
//            for (int c = 0; c < columnCount; c++) {
//                String row = tileMap[r];
//                char tileMapChar = row.charAt(c);
//
//                int x = c * tileSize;
//                int y = r * tileSize;
//
//                Game.Block tile;
//
//                switch (tileMapChar) {
//                    case 'X':
//                        tile = new Game.Block(wallImage, x, y, tileSize, tileSize);
//                        walls.add(tile);
//                        break;
//                    case 'G':
//                        ghost = new Game.Block(redGhostImage, x, y, tileSize, tileSize);
//                        break;
//                    case 'P':
//                        pacman = new Game.Block(pacmanRightImage, x, y, tileSize, tileSize);
//                        break;
//                    case ' ':
//                        tile = new Game.Block(null, x + 14, y + 14, 4, 4);
//                        foods.add(tile);
//                        break;
//                }
//            }
//        }
//    }
//
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        draw(g);
//    }
//
//    public void draw(Graphics g) {
//
//        g.drawImage(ghost.sprite, ghost.x, ghost.y, ghost.width, ghost.height, null);
//        g.drawImage(pacman.sprite, pacman.x, pacman.y, pacman.width, pacman.height, null);
//
//        for (Game.Block wall : walls) {
//            g.drawImage(wall.sprite, wall.x, wall.y, wall.width, wall.height, null);
//        }
//
//        g.setColor(Color.white);
//        for (Game.Block food : foods) {
//            g.fillRect(food.x, food.y, food.width, food.height);
//        }
//
//        g.setFont(new Font("Arial", Font.PLAIN, 18));
//        if (gameOver) {
//            g.drawString("Game Over: " + score, tileSize/2, tileSize/2);
//        } else {
//            g.drawString("x" + lives + " | Score: " + score, tileSize/2, tileSize/2);
//        }
//    }
//
//    public void move() {
//        // <-- GHOST MOVEMENT --> //
//        ghost.x += ghost.velocityX;
//        ghost.y += ghost.velocityY;
//
//        if (ghost.x < tileSize || ghost.x > windowWidth - tileSize * 2) {
//            ghost.x -= ghost.velocityX;
//            ghost.y -= ghost.velocityY;
//        } else {
//            for (Game.Block wall : walls) {
//                if (collision(ghost, wall)) {
//                    ghost.x -= ghost.velocityX;
//                    ghost.y -= ghost.velocityY;
//                    break;
//                }
//            }
//        }
//
//        // <-- PACMAN MOVEMENT --> //
//        if (collision(pacman, ghost)) {
//            lives -= 1;
//            if (lives <= 0) {
//                gameOver = true;
//                return;
//            }
//            resetPositions();
//        }
//
////        if (pacman.y == tileSize * 9 && pacman.direction != 'U' && pacman.direction != 'D') {
////            pacman.updateDirection('U');
////        }
////        pacman.x += pacman.velocityX;
////        pacman.y += pacman.velocityY;
////
////        for (Block wall : walls) {
////            if (collision(pacman, wall) || borderReached(pacman)) {
////                pacman.x -= pacman.velocityX;
////                pacman.y -= pacman.velocityY;
////                char newDirection = directions[random.nextInt(4)];
////                pacman.updateDirection(newDirection);
////                break;
////            }
////        }
//
//        pacman.x += pacman.velocityX;
//        pacman.y += pacman.velocityY;
//
//        for (Game.Block wall : walls) {
//            if (collision(pacman, wall)) {
//                pacman.x -= pacman.velocityX;
//                pacman.y -= pacman.velocityY;
//                break;
//            }
//        }
//
//        Game.Block foodEaten = null;
//        for (Game.Block food : foods) {
//            if (collision(pacman, food)) {
//                foodEaten = food;
//                score += 10;
//            }
//        }
//        foods.remove(foodEaten);
//
//        switch (pacman.direction) {
//            case 'U':
//                pacman.sprite = pacmanUpImage;
//                break;
//            case 'D':
//                pacman.sprite = pacmanDownImage;
//                break;
//            case 'L':
//                pacman.sprite = pacmanLeftImage;
//                break;
//            case 'R':
//                pacman.sprite = pacmanRightImage;
//                break;
//        }
//    }
//
//    public boolean collision(Game.Block a, Game.Block b) {
//        // COLLISION FORMULA
//        return a.x < b.x + b.width &&
//                a.x + a.width > b.x &&
//                a.y < b.y + b.height &&
//                a.y + a.height > b.y;
//    }
//
//    public boolean borderReached(Game.Block a) {
//        return a.x <= 0 || a.x + a.width >= windowWidth;
//    }
//
//    public void resetPositions() {
//        ghost.reset();
//        ghost.velocityX = 0;
//        ghost.velocityY = 0;
//
//        pacman.reset();
//        pacman.velocityX = 0;
//        pacman.velocityY = 0;
//
////        pacman.reset();
////        char newDirection = directions[random.nextInt(4)];
////        pacman.updateDirection(newDirection);
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        // Updates every 20 frames per second
//        move();
//        repaint();
//        if (gameOver) {
//            gameLoop.stop();
//        }
//    }
//
//    @Override
//    public void keyTyped(KeyEvent e) {}
//
//    @Override
//    public void keyPressed(KeyEvent e) {
//        if (gameOver) {
//            loadMap();
//            resetPositions();
//            lives = 3;
//            score = 0;
//            gameOver = false;
//            gameLoop.start();
//        }
//
//        switch (e.getKeyCode()) {
//            case KeyEvent.VK_UP:
//                if (ghost.direction != 'U' || ghost.velocityX == 0 && ghost.velocityY == 0) {
//                    ghost.updateDirection('U');
//                }
//                break;
//            case KeyEvent.VK_DOWN:
//                if (ghost.direction != 'D') {
//                    ghost.updateDirection('D');
//                }
//                break;
//            case KeyEvent.VK_LEFT:
//                if (ghost.direction != 'L') {
//                    ghost.updateDirection('L');
//                }
//                break;
//            case KeyEvent.VK_RIGHT:
//                if (ghost.direction != 'R') {
//                    ghost.updateDirection('R');
//                }
//                break;
//        }
//
//        switch (e.getKeyCode()) {
//            case KeyEvent.VK_W:
//                if (pacman.direction != 'U' || pacman.velocityX == 0 && pacman.velocityY == 0) {
//                    pacman.updateDirection('U');
//                }
//                break;
//            case KeyEvent.VK_S:
//                if (pacman.direction != 'D') {
//                    pacman.updateDirection('D');
//                }
//                break;
//            case KeyEvent.VK_A:
//                if (pacman.direction != 'L') {
//                    pacman.updateDirection('L');
//                }
//                break;
//            case KeyEvent.VK_D:
//                if (pacman.direction != 'R') {
//                    pacman.updateDirection('R');
//                }
//                break;
//        }
//    }
//
//    @Override
//    public void keyReleased(KeyEvent e) {}
//}
