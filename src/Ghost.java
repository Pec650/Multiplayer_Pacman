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

    private BufferedImage start;
    private BufferedImage[] up, down, left, right;
    public BufferedImage sprite;

    enum States {
        START, IDLE, MOVE;
    }
    private States currentState = States.START;
    private int curFrame = 0;

    Ghost(GhostColor color, int x, int y, int width, int height) {
        super(null, x, y, width, height);
        setSprites(color);
    }

    private void setSprites(GhostColor color) {

        String ghostDIR = "";

        switch (color) {
            case color.BLUE -> ghostDIR = "BlueGhost";
            case color.ORANGE -> ghostDIR = "OrangeGhost";
            case color.PINK -> ghostDIR = "PinkGhost";
            case color.RED -> ghostDIR = "RedGhost";
        }

        try {
            start = ImageIO.read(getClass().getResource("./Sprites/" + ghostDIR +"/Start.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        up = setSpriteFrames("./Sprites/" + ghostDIR +"/Up.png", 9, 1, 9, 16, 16);
        down = setSpriteFrames("./Sprites/" + ghostDIR +"/Down.png", 9, 1, 9, 16, 16);
        left = setSpriteFrames("./Sprites/" + ghostDIR +"/Left.png", 9, 1, 9, 16, 16);
        right = setSpriteFrames("./Sprites/" + ghostDIR +"/Right.png", 9, 1, 9, 16, 16);
    }

    public void updateSprites() {
        switch (currentState) {
            case START:
                sprite = start;
            case IDLE:
                break;
            case MOVE:
                switch(direction) {
                    case U -> sprite = up[curFrame];
                    case D -> sprite = down[curFrame];
                    case L -> sprite = left[curFrame];
                    case R -> sprite = right[curFrame];
                }
                curFrame = (curFrame + 1) % 9;
                break;
        }
    }

    public void updateState(States newState) {
        currentState = newState;
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

    public void reset() {
        x = startX;
        y = startY;
        direction = Direction.STOP;
        updateVelocity();
        currentState = Ghost.States.START;
    }
}
