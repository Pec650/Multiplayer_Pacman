import java.awt.*;

public class Entity extends Tile {
    public enum Direction {
        STOP, U, D, L, R
    }
    protected Direction direction = Direction.STOP;
    protected int velocityX = 0;
    protected int velocityY = 0;
    private int speed;

    Entity(Image sprite, int x, int y, int width, int height) {
        super(sprite, x, y, width, height);
        speed = width / 4;
    }

    protected void updateVelocity() {
        switch(direction) {
            case STOP:
                velocityX = 0;
                velocityY = 0;
                break;
            case U:
                velocityX = 0;
                velocityY = -speed;
                break;
            case D:
                velocityX = 0;
                velocityY = speed;
                break;
            case L:
                velocityX = -speed;
                velocityY = 0;
                break;
            case R:
                velocityX = speed;
                velocityY = 0;
                break;
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public void reset() {
        x = startX;
        y = startY;
        velocityX = 0;
        velocityY = 0;
    }
}
