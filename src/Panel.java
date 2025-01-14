import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.sound.sampled.*;
import java.io.File;

public class Panel extends JPanel implements KeyListener, MouseListener {
    private static final long serialVersionUID = 1L;
    private Clip backgroundMusic;
    private boolean isMusicPlaying = false;
    private int timeInterval = 450;
    private int score = 0;
    private boolean collision = false;
    private boolean game_over = false;
    private boolean inMenu = true;
    private boolean isPlayButtonPressed = false;
    private boolean isQuitButtonPressed = false;
    private ArrayList<Image> backgrounds = new ArrayList<>();
    private int currentBackground = 0;
    private final int POINTS_TO_CHANGE_BG = 15;
    // Initializing Objects
    private Alien alien = new Alien();
    private Timer timer = new Timer(timeInterval, new TimerListener());
    private ArrayList<Asteroid> list = new ArrayList<Asteroid>();
    private Random random = new Random();

    private Image logo = new ImageIcon("assets/logo.png").getImage();        // logo
    private Image background = new ImageIcon("assets/bg.png").getImage(); // background image
    private Image gameOver = new ImageIcon("assets/gameOver.png").getImage(); // for game over prompt

    private boolean showingIntro = true;
    private boolean showingLogin = false;
    private boolean showingCharacterSelection = false;
    private Timer introTimer;
    private ImageIcon introGif;
    private LoginScreen loginScreen;
    private CharacterSelection characterSelection;

    // Panel constructor
    public Panel() {
        this.setLayout(null);
        this.setBorder(BorderFactory.createLineBorder(Color.white));
        this.setFocusable(true);

        // Inicializar el GIF de introducción
        introGif = new ImageIcon("assets/intro.gif");
        backgrounds.add(new ImageIcon("assets/level2.gif").getImage());
        backgrounds.add(new ImageIcon("assets/level1.gif").getImage());
        backgrounds.add(new ImageIcon("assets/level3.gif").getImage());
        backgrounds.add(new ImageIcon("assets/level4.gif").getImage());
        // Inicializar las pantallas
        loginScreen = new LoginScreen(this);
        add(loginScreen);
        loginScreen.setVisible(false);

        characterSelection = new CharacterSelection(this);
        add(characterSelection); // Agregar esta línea
        characterSelection.setVisible(false); // Inicialmente oculto
        // Variables de estado
        showingIntro = true;
        showingLogin = false;
        showingCharacterSelection = false;

        // Timer para la introducción
        introTimer = new Timer(5000, e -> {
            showingIntro = false;
            showingLogin = true;
            loginScreen.setVisible(true);
            loginScreen.setEnabled(true);
            loginScreen.requestFocusInWindow();
            repaint();
            introTimer.stop();
        });
        introTimer.setRepeats(false);
        introTimer.start();

        // Inicializar el timer del juego
        timer.start();
    }

    public void showCharacterSelection() {
        showingLogin = false;
        showingCharacterSelection = true;
        characterSelection.setVisible(true);
        characterSelection.setEnabled(true);
        repaint();
    }

    public void startGame(boolean isAlien) {
        showingCharacterSelection = false;
        characterSelection.setVisible(false); // Agregar esta línea
        inMenu = false;
        if (!isAlien) {
            alien.setImage(new ImageIcon("assets/astronaut.png").getImage());
        }
        if (!isMusicPlaying) {
            playBackgroundMusic();
        }
        resetGame();
        this.requestFocusInWindow(); // Agregar esta línea para asegurar que el panel principal reciba los eventos de teclado
    }

    // Checking collision between Asteroids and Alien
    public void checkCollision() {
        int alienX1 = alien.getxPos();
        int alienX2 = alienX1 + alien.getWidth();
        int alienY1 = alien.getyPos();
        int alienY2 = alienY1 + alien.getHeight();

        for (Asteroid asteroid : list) {
            int asteroidX1 = asteroid.getxPos();
            int asteroidX2 = asteroidX1 + asteroid.getLato();
            int asteroidY = asteroid.getyPos();

            if (((asteroidX1 > alienX1 && asteroidX1 < alienX2) ||
                    (asteroidX2 > alienX1 && asteroidX2 < alienX2)) &&
                    (asteroidY > alienY1 && asteroidY < alienY2)) {
                timer.stop();
                collision = true;
                game_over = true;
                inMenu = true;
                this.removeKeyListener(this);
                this.addMouseListener(this); // Asegurarse de que el MouseListener está activo
                this.requestFocusInWindow();
                stopBackgroundMusic();
            }
        }
    }

    // Paints components
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Siempre dibuja el fondo primero
        g.drawImage(background, 0, 0, null);
        // Actualizar el fondo según el puntaje
        updateBackground();

        // Dibujar el fondo actual
        if (!showingLogin) {
            g.drawImage(backgrounds.get(currentBackground), 0, 0, null);
        }
        if (showingIntro) {
            // Durante la intro solo muestra el GIF
            g.drawImage(introGif.getImage(), 0, 0, getWidth(), getHeight(), null);
        } else if (showingLogin) {
            // Asegúrate de que loginScreen esté visible y en la posición correcta
            loginScreen.setBounds(0, 0, getWidth(), getHeight());
            loginScreen.setVisible(true);
            loginScreen.setEnabled(true);
            loginScreen.paint(g);
        } else if (showingCharacterSelection) {
            characterSelection.setBounds(0, 0, getWidth(), getHeight());
            characterSelection.setVisible(true);
            characterSelection.paint(g);
        } else if (!inMenu && !game_over) {
            // Juego en curso
            alien.drawAlien(g);
            for (Asteroid asteroid : list) {
                asteroid.drawAsteroid(g);
            }
        }

        // Manejo del menú y game over
        if (collision) {
            g.drawImage(gameOver, 75, 150, 150, 150, null);
            game_over = true;
            inMenu = true;

            // Dibuja los botones de game over
            int playButtonX = getWidth() / 2 - 50;
            int playButtonY = getHeight() / 2 + 50;
            int quitButtonX = playButtonX;
            int quitButtonY = playButtonY + 50;
            int ButtonWidth = 100;
            int ButtonHeight = 40;

            // Botón Play Again
            g.setColor(Color.CYAN);
            g.fillRect(playButtonX, playButtonY, ButtonWidth, ButtonHeight);
            g.setColor(Color.BLACK);
            g.drawRect(playButtonX, playButtonY, ButtonWidth, ButtonHeight);

            // Texto Play Again
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.setColor(Color.BLACK);
            String playText = "Jugar de nuevo";
            int textWidth = g.getFontMetrics().stringWidth(playText);
            g.drawString(playText, playButtonX + (ButtonWidth - textWidth) / 2, playButtonY + 25);

            // Botón Quit
            g.setColor(Color.CYAN);
            g.fillRect(quitButtonX, quitButtonY, ButtonWidth, ButtonHeight);
            g.setColor(Color.BLACK);
            g.drawRect(quitButtonX, quitButtonY, ButtonWidth, ButtonHeight);

            // Texto Quit
            g.setColor(Color.BLACK);
            String quitText = "Salir";
            textWidth = g.getFontMetrics().stringWidth(quitText);
            g.drawString(quitText, quitButtonX + (ButtonWidth - textWidth) / 2, quitButtonY + 25);
        }

        if (inMenu && !collision && !showingIntro && !showingLogin && !showingCharacterSelection) {
            g.drawImage(logo, 4, 145, 293, 180, null);

            // Dibuja los botones
            int playButtonX = getWidth() / 2 - 50;
            int playButtonY = getHeight() / 2 + 50;
            int quitButtonX = playButtonX;
            int quitButtonY = playButtonY + 50;
            int ButtonWidth = 100;
            int ButtonHeight = 40;

            // Play Button
            if (isPlayButtonPressed) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.CYAN);
            }
            g.fillRect(playButtonX, playButtonY, ButtonWidth, ButtonHeight);
            g.drawRect(playButtonX, playButtonY, ButtonWidth, ButtonHeight);

            // Play Text
            g.setFont(new Font("Arial", Font.BOLD, 18));
            String playText = "Jugar";
            int playTextWidth = g.getFontMetrics().stringWidth(playText);
            int playTextX = playButtonX + ButtonWidth / 2 - playTextWidth / 2;
            int playTextY = playButtonY + ButtonHeight / 2 + 5;

            if (isPlayButtonPressed) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.BLACK);
            }
            g.drawString(playText, playTextX, playTextY);

            // Quit Button
            if (isQuitButtonPressed) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.CYAN);
            }
            g.fillRect(quitButtonX, quitButtonY, ButtonWidth, ButtonHeight);
            g.drawRect(quitButtonX, quitButtonY, ButtonWidth, ButtonHeight);

            // Quit Text
            String quitText = "Salir";
            int quitTextWidth = g.getFontMetrics().stringWidth(quitText);
            int quitTextX = quitButtonX + ButtonWidth / 2 - quitTextWidth / 2;
            int quitTextY = quitButtonY + ButtonHeight / 2 + 5;

            if (isQuitButtonPressed) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.BLACK);
            }
            g.drawString(quitText, quitTextX, quitTextY);
        }

        // Score siempre visible excepto en intro, login y selección de personaje
        if (!showingIntro && !showingLogin && !showingCharacterSelection) {
            // Mostrar Score (izquierda)
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            String scoreText = "Puntos: " + score * timeInterval / 1000;
            g.drawString(scoreText, 10, 30);

            // Mostrar Nivel (derecha)
            int currentLevel = (score * timeInterval / 1000) / 15 + 1; // Cada 15 puntos sube un nivel
            String levelText = "Nivel: " + currentLevel;
            int levelWidth = g.getFontMetrics().stringWidth(levelText);
            g.drawString(levelText, getWidth() - levelWidth - 10, 30);
        }
        repaint();
    }

    private void updateBackground() {
        int level = (score * timeInterval / 1000) / POINTS_TO_CHANGE_BG;
        if (level < backgrounds.size()) {
            currentBackground = level;
        }
    }

    private void playBackgroundMusic() {
        try {
            if (backgroundMusic != null && backgroundMusic.isRunning()) {
                backgroundMusic.stop();
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                    new File("assets/background_music.wav")
            );
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            isMusicPlaying = true;
        } catch (Exception e) {
            System.out.println("Error al cargar la música: " + e.getMessage());
        }
    }

    private void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic.close();
            isMusicPlaying = false;
        }
    }

    private void resetGame() {
        alien.setxPos(124);
        alien.setyPos(544);
        list.clear();
        collision = false;
        game_over = false;
        inMenu = false;
        score = 0;
        // Reiniciar listeners
        this.removeMouseListener(this);
        this.addKeyListener(this);
        this.requestFocusInWindow();

        // Reiniciar timer
        timer.start();
        playBackgroundMusic();
    }

    public void loginCompleted() {
        showingLogin = false;
        loginScreen.setVisible(false);
        showCharacterSelection();
    }
/*    public void loginCompleted() {
        showingLogin = false;
        loginScreen.setVisible(false);

        // Reactivar los listeners del panel principal
        this.setFocusable(true);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.requestFocusInWindow();
    }*/

    // Key Listener
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            alien.moveUp();
            checkCollision();
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            alien.moveDown();
            checkCollision();
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            alien.moveLeft();
            checkCollision();
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            alien.moveRight();
            checkCollision();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    // Mouse Listener
    @Override
    public void mouseClicked(MouseEvent e) {
        if (game_over) {
            int playButtonX = getWidth() / 2 - 50;
            int playButtonY = getHeight() / 2 + 50;
            int quitButtonX = playButtonX;
            int quitButtonY = playButtonY + 50;
            int ButtonWidth = 100;
            int ButtonHeight = 40;

            // Verificar click en Play Again
            if (e.getX() >= playButtonX && e.getX() <= playButtonX + ButtonWidth &&
                    e.getY() >= playButtonY && e.getY() <= playButtonY + ButtonHeight) {
                resetGame();
                repaint();
            }
            // Verificar click en Quit
            else if (e.getX() >= quitButtonX && e.getX() <= quitButtonX + ButtonWidth &&
                    e.getY() >= quitButtonY && e.getY() <= quitButtonY + ButtonHeight) {
                System.exit(0);
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int playButtonX = getWidth() / 2 - 50;
        int playButtonY = getHeight() / 2 + 50;
        int quitButtonX = playButtonX;
        int quitButtonY = playButtonY + 50;

        int ButtonWidth = 100;
        int ButtonHeight = 40;

        if (e.getX() >= playButtonX && e.getX() <= playButtonX + ButtonWidth &&
                e.getY() >= playButtonY && e.getY() <= playButtonY + ButtonHeight) {
            isPlayButtonPressed = true;
        } else if (e.getX() >= quitButtonX && e.getX() <= quitButtonX + ButtonWidth &&
                e.getY() >= quitButtonY && e.getY() <= quitButtonY + ButtonHeight) {
            isQuitButtonPressed = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int playButtonX = getWidth() / 2 - 50;
        int playButtonY = getHeight() / 2 + 50;
        int quitButtonX = playButtonX;
        int quitButtonY = playButtonY + 50;

        int ButtonWidth = 100;
        int ButtonHeight = 40;

        if (e.getX() >= playButtonX && e.getX() <= playButtonX + ButtonWidth &&
                e.getY() >= playButtonY && e.getY() <= playButtonY + ButtonHeight) {
            isPlayButtonPressed = false;
        } else if (e.getX() >= quitButtonX && e.getX() <= quitButtonX + ButtonWidth &&
                e.getY() >= quitButtonY && e.getY() <= quitButtonY + ButtonHeight) {
            isQuitButtonPressed = false;
        }
    }

    // Timer Listener to generate asteroids randomly
    public class TimerListener implements ActionListener {
        private int asteroidSpacing = 3; // Adjust the spacing between asteroids
        private int counter = 0; // Counter for asteroidSpacing

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!inMenu) {
                counter++;
                // Check if the counter has reached the desired spacing value
                if (counter >= asteroidSpacing) {
                    Integer x = random.nextInt(227);
                    list.add(new Asteroid(x));
                    counter = 0; // Reset the counter
                }
                for (Asteroid asteroid : list) {
                    checkCollision(); // check collisions everytime
                    asteroid.moveDown();
                }
                score++;
                updateBackground(); // Actualizar el fondo
            }
        }
    }
}
