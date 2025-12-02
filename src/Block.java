import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

abstract class Block {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Image sprite;

    protected int startX;
    protected int startY;

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

    protected BufferedImage[] setSpriteFrames(String dir, int frames, int row, int col, int spriteWidth, int spriteHeight) {
        BufferedImage[] retVal = new BufferedImage[frames];
        if (row * col == frames) {
            try {
                BufferedImage temp = ImageIO.read(getClass().getResource(dir));
                retVal = getSpriteFrames(temp, row, col, spriteWidth, spriteHeight);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return retVal;
    };

    private BufferedImage[] getSpriteFrames(BufferedImage spritesheet, int row, int col, int spriteWidth, int spriteHeight) {
        BufferedImage[] frames = new BufferedImage[row * col];

        int index = 0;

        for (int x = 0; x < row; x++) {
            for (int y = 0; y < col; y++) {
                frames[index++] = spritesheet.getSubimage(y * spriteHeight, x * spriteWidth, spriteWidth, spriteHeight);
            }
        }

        return frames;
    }

    protected BufferedImage rotateSprite(BufferedImage src, double degrees) {
        double radians = Math.toRadians(degrees);
        double cos = Math.abs(Math.cos(radians));
        double sin = Math.abs(Math.sin(radians));

        int newWidth = (int) Math.round(src.getWidth() * cos + src.getHeight() * sin);
        int newHeight = (int) Math.round(src.getWidth() * sin + src.getHeight() * cos);

        BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, src.getType());

        AffineTransform transform = new AffineTransform();
        transform.translate((newWidth - src.getWidth()) / 2.0, (newHeight - src.getHeight()) / 2.0);
        transform.rotate(radians, src.getWidth() / 2.0, src.getHeight() / 2.0);

        Graphics2D graphics = rotatedImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.drawImage(src, transform, null);
        graphics.dispose();

        return rotatedImage;
    }
}
