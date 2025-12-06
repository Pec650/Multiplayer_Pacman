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

    private int lives = 3;
    public Direction controlDirection = null;

    private BufferedImage[] move;
    public BufferedImage sprite;

    enum States {
        START, IDLE, MOVE, DEATH;
    }
    private States currentState = States.START;
    private int curFrame = 0;

    private MusicPlayer pacmanSoundEffect = new MusicPlayer();  // <--- Pacman Sound Effects

    Pacman(int x, int y, int width, int height) {
        super(null, x, y, width, height);
        setSprites();
    }

    private void setSprites() {
        move = setSpriteFrames("/Sprites/Pacman/Move.png", 7, 1, 7, 16, 16);
    }

    public void updateSprites() {
        double degrees = 0.0;
        switch(direction) {
            case U -> degrees = -90;
            case D -> degrees = 90;
            case L -> degrees = 180;
            case R -> degrees = 0;
        }

        switch (currentState) {
            case START:
                sprite = move[0];
                pacmanSoundEffect.stop();
                break;
            case IDLE:
                sprite = rotateSprite(move[3], degrees);
                pacmanSoundEffect.stop();
                break;
            case MOVE:
                sprite = rotateSprite(move[(curFrame++) % 7], degrees);
                if (!pacmanSoundEffect.isPlaying()) {
                    pacmanSoundEffect.playLoop(getClass().getResource("/SoundEffects/pacman_chomp.wav"));
                }
                break;
        }
    }

    public void updateState(States newState) {
        currentState = newState;
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
    }

    public void reset() {
        x = startX;
        y = startY;
        direction = Direction.STOP;
        updateVelocity();
        currentState = States.START;
    }

    public void stop() {
        pacmanSoundEffect.stop();
    }
}
