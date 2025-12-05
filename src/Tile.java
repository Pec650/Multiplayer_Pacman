import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Tile extends Block {

    Tile(Image sprite, int x, int y, int width, int height) {
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        startX = x;
        startY = y;
    }

    public boolean collided(Tile tile) {
        // COLLISION FORMULA
        return x < tile.x + tile.width &&
                x + width > tile.x &&
                y < tile.y + tile.height &&
                y + height > tile.y;
    }
}