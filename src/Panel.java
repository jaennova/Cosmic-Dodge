import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class Panel extends JPanel implements KeyListener, MouseListener {
    private static final long serialVersionUID = 1L;

    private int timeInterval = 450;
    private int score = 0;
    private boolean collision = false;
    private boolean game_over = false;
    private boolean inMenu = true;
    private boolean isPlayButtonPressed = false;
    private boolean isQuitButtonPressed = false;

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

        // Inicializar las pantallas
        loginScreen = new LoginScreen(this);
        add(loginScreen);
        loginScreen.setVisible(false);

        characterSelection = new CharacterSelection(this);

        // Variables de estado
        showingIntro = true;
        showingLogin = false;
        showingCharacterSelection = false;

        // Timer para la introducción
        introTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showingIntro = false;
                showingLogin = true;
                loginScreen.setVisible(true);
                loginScreen.setEnabled(true);
                loginScreen.requestFocusInWindow();
                repaint();
                introTimer.stop();
            }
        });
        introTimer.setRepeats(false);
        introTimer.start();

        // Inicializar el timer del juego
        timer.start();
    }

    public void showCharacterSelection() {
        showingLogin = false;
        showingCharacterSelection = true;
        repaint();
    }

    public void startGame(boolean isAlien) {
        showingCharacterSelection = false;
        inMenu = false;
        if (!isAlien) {
            alien.setImage(new ImageIcon("assets/astronaut.png").getImage());
        }
        resetGame();
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
            // Simply compare the bounds of each component
            if (((asteroidX1 > alienX1 && asteroidX1 < alienX2) || (asteroidX2 > alienX1 && asteroidX2 < alienX2))
                    && (asteroidY > alienY1 && asteroidY < alienY2)) {
                timer.stop();
                this.removeKeyListener(this); // stop
                collision = true; // collision detected
            }
        }
    }

    // Paints components
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Siempre dibuja el fondo primero
        g.drawImage(background, 0, 0, null);

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
            String playText = "Play";
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
            String quitText = "Quit";
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
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            String scoreText = "Score: " + score * timeInterval / 1000;
            g.drawString(scoreText, 10, 30);
        }
        repaint();
    }

    private void resetGame() {
        alien.setxPos(124);
        alien.setyPos(544);

        // Clear lists
        list.clear();

        // Initialize game state
        collision = false;
        game_over = false;
        score = 0;
        timer.start();

        this.addKeyListener(this);
        this.addMouseListener(this);
    }

    private void showLogin() {
        showingLogin = true;
        loginScreen.setVisible(true);
        loginScreen.setEnabled(true);
        loginScreen.requestFocusInWindow();

        // Deshabilitar temporalmente los listeners del panel principal
        this.setFocusable(false);
        this.removeKeyListener(this);
        this.removeMouseListener(this);
    }

    public void loginCompleted() {
        showingLogin = false;
        loginScreen.setVisible(false);

        // Reactivar los listeners del panel principal
        this.setFocusable(true);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.requestFocusInWindow();
    }

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
            // Handle menu options
            int playButtonX = getWidth() / 2 - 50;
            int playButtonY = getHeight() / 2 + 50;
            int quitButtonX = playButtonX;
            int quitButtonY = playButtonY + 50;

            int ButtonWidth = 100;
            int ButtonHeight = 40;

            if (e.getX() >= playButtonX && e.getX() <= playButtonX + ButtonWidth &&
                    e.getY() >= playButtonY && e.getY() <= playButtonY + ButtonHeight) {
                resetGame();
            } else if (e.getX() >= quitButtonX && e.getX() <= quitButtonX + ButtonWidth &&
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
            }
        }
    }
}
