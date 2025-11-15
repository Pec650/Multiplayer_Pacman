import java.awt.*;

public class Tile {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Image sprite;

    protected int startX;
    protected int startY;

    Tile(Image sprite, int x, int y, int width, int height) {
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        startX = x;
        startY = y;
    }

    public void reset() {
        x = startX;
        y = startY;
    }

    public boolean collided(Tile tile) {
        // COLLISION FORMULA
        return x < tile.x + tile.width &&
                x + width > tile.x &&
                y < tile.y + tile.height &&
                y + height > tile.y;
    }
}