import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CharacterSelection extends JPanel {
    private Panel gamePanel;
    private boolean alienSelected = true;
    private Image alienImage;
    private Image astronautImage;
    private Rectangle alienBounds;
    private Rectangle astronautBounds;
    private boolean alienHover = false;
    private boolean astronautHover = false;

    public CharacterSelection(Panel gamePanel) {
        this.gamePanel = gamePanel;
        setLayout(null);
        setOpaque(true);

        // Cargar las imágenes y redimensionarlas
        alienImage = new ImageIcon("assets/alien48.png").getImage();
        astronautImage = new ImageIcon("assets/astronaut.png").getImage();

        // Definir las áreas clickeables
        alienBounds = new Rectangle(40, 300, 60, 60);
        astronautBounds = new Rectangle(180, 300, 60, 60);

        // Agregar listeners del mouse
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (alienBounds.contains(e.getPoint())) {
                    alienSelected = true;
                    gamePanel.startGame(true);
                } else if (astronautBounds.contains(e.getPoint())) {
                    alienSelected = false;
                    gamePanel.startGame(false);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                updateHoverState(e.getPoint());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                alienHover = false;
                astronautHover = false;
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                updateHoverState(e.getPoint());
            }
        });
    }

    private void updateHoverState(Point p) {
        boolean oldAlienHover = alienHover;
        boolean oldAstronautHover = astronautHover;

        alienHover = alienBounds.contains(p);
        astronautHover = astronautBounds.contains(p);

        if (oldAlienHover != alienHover || oldAstronautHover != astronautHover) {
            repaint();
            setCursor(alienHover || astronautHover ?
                    Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) :
                    Cursor.getDefaultCursor());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Dibujar fondo negro
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Configurar renderizado para mejor calidad
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar título
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        String title = "Selecciona tu personaje";
        FontMetrics fm = g2d.getFontMetrics();
        int titleX = (getWidth() - fm.stringWidth(title)) / 2;
        g2d.drawString(title, titleX, 150);

        // Dibujar imágenes con efectos de hover
        drawCharacterImage(g2d, alienImage, alienBounds, alienHover, "Alien");
        drawCharacterImage(g2d, astronautImage, astronautBounds, astronautHover, "Astronauta");
    }

    private void drawCharacterImage(Graphics2D g2d, Image img, Rectangle bounds, boolean hover, String label) {
        // Dibujar marco si el mouse está encima
        if (hover) {
            g2d.setColor(Color.YELLOW);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(bounds.x - 3, bounds.y - 3, bounds.width + 6, bounds.height + 6);
        }

        // Dibujar la imagen
        g2d.drawImage(img, bounds.x, bounds.y, bounds.width, bounds.height, null);

        // Dibujar el nombre del personaje
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g2d.getFontMetrics();
        int labelX = bounds.x + (bounds.width - fm.stringWidth(label)) / 2;
        g2d.drawString(label, labelX, bounds.y + bounds.height + 25);
    }
}