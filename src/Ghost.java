import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;

public class Ghost extends Entity {
    public enum GhostColor {
        BLUE,
        ORANGE,
        PINK,
        RED
    }

    public Direction controlDirection = null;

    private BufferedImage up, down, left, right;
    public BufferedImage sprite;

    Ghost(GhostColor color, int x, int y, int width, int height) {
        super(null, x, y, width, height);
        setSprites(color);
        sprite = right;
    }

    private void setSprites(GhostColor color) {
        try {

            String ghostDIR = "";

            switch (color) {
                case color.BLUE -> ghostDIR = "BlueGhost";
                case color.ORANGE -> ghostDIR = "OrangeGhost";
                case color.PINK -> ghostDIR = "PinkGhost";
                case color.RED -> ghostDIR = "RedGhost";
            }

            right = ImageIO.read(getClass().getResource("./Sprites/" + ghostDIR +"/Right.png"));

        } catch(IOException e) {
            e.printStackTrace();
        }
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
    }
}
