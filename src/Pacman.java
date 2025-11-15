import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;

public class Pacman extends Entity {
    /* FIXED ATTRIBUTES */
    private final int TOTAL_LIVES = 3;
    /* ---------------- */

    private int lives = TOTAL_LIVES;
    public Direction controlDirection = null;

    private BufferedImage up, down, left, right;
    public BufferedImage sprite;

    Pacman(int x, int y, int width, int height) {
        super(null, x, y, width, height);

        setSprites();
        updateDirection(Direction.R, null);
    }

    private void setSprites() {
        try {

            up = ImageIO.read(getClass().getResource("./Sprites/Pacman/Up.png"));
            down = ImageIO.read(getClass().getResource("./Sprites/Pacman/Down.png"));
            left = ImageIO.read(getClass().getResource("./Sprites/Pacman/Left.png"));
            right = ImageIO.read(getClass().getResource("./Sprites/Pacman/Right.png"));

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void loseLife() {
        if (lives > 0) {
            lives--;
        }
    }

    public void resetLife() {
        lives = TOTAL_LIVES;
    }

    public int getLives() {
        return lives;
    }

    public void updateDirection(Direction direction, HashSet<Tile> walls) {
        if (this.direction == direction) {
            return;
        }

        boolean allowUpdate = true;
        Direction prevDirection = this.direction;
        this.direction = direction;
        updateVelocity();
        x += velocityX;
        y += velocityY;

        if (walls != null) {
            for (Tile wall : walls) {
                if (collided(wall)) {
                    x -= velocityX;
                    y -= velocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                    allowUpdate = false;
                    return;
                }
            }
        }

        if (allowUpdate) {
            controlDirection = null;
        }

        switch(direction) {
            case U -> sprite = up;
            case D -> sprite = down;
            case L -> sprite = left;
            case R -> sprite = right;
        }
    }

    public void reset() {
        x = startX;
        y = startY;
        updateDirection(Direction.R, null);
    }
}
