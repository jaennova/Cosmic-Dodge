import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Alien {
    private Image alien = new ImageIcon("assets/alien48.png").getImage();
    private int xPos = 124;
    private int yPos = 544;
    private int width = 48;
    private int height = 48;
    private int speed = 8;
    // Getters and Setters
    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    // Alien movement controls
    public void moveUp() {
        yPos -= speed;
        if (yPos < 0) {
            yPos = 0;
        }
    }

    public void moveDown() {
        yPos += speed;
        if (yPos > 554) {
            yPos = 554;
        }
    }

    public void setImage(Image newImage) {
        this.alien = newImage;
    }

    public void moveRight() {
        xPos += speed;
        if (xPos > 254) {
            xPos = 254;
        }
    }

    public void moveLeft() {
        xPos -= speed;
        if (xPos < 0) {
            xPos = 0;
        }
    }

    // Draw alien
    public void drawAlien(Graphics g) {
        g.drawImage(alien, xPos, yPos, width, height, null);
    }
}
