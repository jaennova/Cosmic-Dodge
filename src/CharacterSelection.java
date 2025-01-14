import javax.swing.*;
import java.awt.*;

public class CharacterSelection extends JPanel {
    private Panel gamePanel;
    private boolean alienSelected = true;

    public CharacterSelection(Panel gamePanel) {
        this.gamePanel = gamePanel;
        setLayout(null);

        JButton alienButton = new JButton("Alien");
        alienButton.setBounds(50, 250, 100, 30);
        alienButton.addActionListener(e -> {
            alienSelected = true;
            gamePanel.startGame(true);
        });
        add(alienButton);

        JButton astronautButton = new JButton("Astronauta");
        astronautButton.setBounds(160, 250, 100, 30);
        astronautButton.addActionListener(e -> {
            alienSelected = false;
            gamePanel.startGame(false);
        });
        add(astronautButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Dibuja previsualizaciones de los personajes
        g.setColor(Color.WHITE);
        g.drawString("Selecciona tu personaje", 80, 200);
    }
}