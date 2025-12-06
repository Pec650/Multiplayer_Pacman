import java.awt.image.BufferedImage;

public class PowerPellet extends Tile {
    private BufferedImage[] animation;
    private int curFrame = 0;
    private boolean hasEnded = false;

    PowerPellet(int x, int y, int width, int height) {
        super(null, x, y, width, height);
        animation = setSpriteFrames("./Sprites/Foods/PowerPellet.png", 5, 1, 5, 16, 16);
        sprite = animation[curFrame];
    }

    public void updateSprites() {
        if (!hasEnded) {
            curFrame = (curFrame + 1) % (5 * 2);
            sprite = animation[curFrame / 2];
        }
    }

    public void endAnim() {
        hasEnded = true;
    }
}
