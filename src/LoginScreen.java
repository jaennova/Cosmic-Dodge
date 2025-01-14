import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


// LoginScreen.java - Clase completa
public class LoginScreen extends JPanel {
    private Panel gamePanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private Image loginBackground;

    public LoginScreen(Panel panel) {
        this.gamePanel = panel;
        setLayout(null);
        setOpaque(false);
        setFocusable(true);
        loginBackground = new ImageIcon("assets/bg.png").getImage();
        // Configurar etiquetas

        usernameLabel = new JLabel("Usuario:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBounds(10, 140, 100, 25);

        passwordLabel = new JLabel("contraseña:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setBounds(10, 190, 100, 25);

        // Configurar campos de texto
        usernameField = new JTextField();
        usernameField.setBounds(100, 140, 150, 25);
        usernameField.setFocusable(true);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 190, 150, 25);
        passwordField.setFocusable(true);

        // Configurar botón de login
        loginButton = new JButton("Iniciar sesión");
        loginButton.setBounds(100, 250, 140, 30);
        loginButton.setFocusable(true);

        // Agregar action listener al botón
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (validateLogin(username, password)) {
                setVisible(false);
                gamePanel.loginCompleted();
            } else {
                JOptionPane.showMessageDialog(LoginScreen.this,
                        "Usuario o contraseña incorrectos",
                        "Error de inicio de sesión",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Agregar componentes al panel
        add(usernameLabel);
        add(passwordLabel);
        add(usernameField);
        add(passwordField);
        add(loginButton);
    }

    private boolean validateLogin(String username, String password) {
        // Validar que el usuario sea "MITZI" y la contraseña sea "12345"
        return username.equals("MITZI") && password.equals("12345");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dibuja el fondo del login
        g.drawImage(loginBackground, 0, 0, getWidth(), getHeight(), null);

    }
}