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

    public LoginScreen(Panel panel) {
        this.gamePanel = panel;
        setLayout(null);
        setOpaque(false);
        setFocusable(true);

        // Configurar etiquetas
        usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBounds(50, 80, 100, 25);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setBounds(50, 130, 100, 25);

        // Configurar campos de texto
        usernameField = new JTextField();
        usernameField.setBounds(150, 80, 150, 25);
        usernameField.setFocusable(true);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 130, 150, 25);
        passwordField.setFocusable(true);

        // Configurar botón de login
        loginButton = new JButton("Login");
        loginButton.setBounds(150, 180, 100, 30);
        loginButton.setFocusable(true);

        // Agregar action listener al botón
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (validateLogin(username, password)) {
                    // Si el login es exitoso
                    setVisible(false);
                    gamePanel.loginCompleted();
                } else {
                    JOptionPane.showMessageDialog(LoginScreen.this,
                            "Invalid username or password",
                            "Login Error",
                            JOptionPane.ERROR_MESSAGE);
                }
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
        // Aquí puedes implementar tu lógica de validación
        // Por ahora, acepta cualquier combinación no vacía
        return !username.isEmpty() && !password.isEmpty();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Puedes agregar elementos visuales adicionales aquí si lo deseas
    }
}