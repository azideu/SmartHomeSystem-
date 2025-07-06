package gui;

import javax.swing.*;
import java.awt.*;
import utils.FileUtils; // Import your utility class

public class LoginWindow extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private String username = null;

    public LoginWindow(JFrame parent) {
        super(parent, "Login", true);
        setSize(300, 220);
        setLocationRelativeTo(parent);

        Color bgColor = new Color(22, 29, 35);
        Color buttonNormal = new Color(44, 51, 57);
        Color textColor = Color.WHITE;

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username label and field
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(textColor);
        userLabel.setFont(new Font("Arial", Font.BOLD, 12));
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 12));
        usernameField.setBackground(new Color(44, 51, 57));
        usernameField.setForeground(textColor);
        usernameField.setCaretColor(textColor);

        // Password label and field
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(textColor);
        passLabel.setFont(new Font("Arial", Font.BOLD, 12));
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 12));
        passwordField.setBackground(new Color(44, 51, 57));
        passwordField.setForeground(textColor);
        passwordField.setCaretColor(textColor);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(buttonNormal);
        loginButton.setForeground(textColor);
        loginButton.setFont(new Font("Arial", Font.BOLD, 12));
        loginButton.addActionListener(e -> attemptLogin());

        // Layout
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(userLabel, gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(passLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        add(panel);

        // Enter key submits
        usernameField.addActionListener(e -> attemptLogin());
        passwordField.addActionListener(e -> attemptLogin());
    }

    private void attemptLogin() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());
        if (FileUtils.validateAccount(user, pass)) {
            username = user;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getUsername() {
        return username;
    }
}