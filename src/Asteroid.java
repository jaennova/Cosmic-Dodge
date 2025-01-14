import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.util.Random;

public class Asteroid {
    private int xPos;
    private int yPos = -20;
    private int lato = 35; // for size of asteroid
    private Image asteroidImage;
    private static final Random random = new Random();

    // Array de imágenes de asteroides
    private static final String[] ASTEROID_IMAGES = {
            "assets/asteroid.png",
            "assets/roca.png",
            "assets/planet.png"
    };

    // Constructor
    public Asteroid(int xPos) {
        super();
        this.xPos = xPos;
        // Seleccionar una imagen aleatoria
        selectRandomAsteroid();
    }

    // Método para seleccionar aleatoriamente una imagen de asteroide
    private void selectRandomAsteroid() {
        int randomIndex = random.nextInt(ASTEROID_IMAGES.length);
        String selectedImagePath = ASTEROID_IMAGES[randomIndex];
        asteroidImage = new ImageIcon(selectedImagePath).getImage();
    }

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

    public int getLato() {
        return lato;
    }

    public void setLato(int lato) {
        this.lato = lato;
    }

    public void moveDown() {
        yPos += 40;
    }

    // Draw asteroid
    public void drawAsteroid(Graphics g) {
        g.drawImage(asteroidImage, xPos, yPos, lato, lato, null);
    }
}