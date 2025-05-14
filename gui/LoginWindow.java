package gui;

import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JDialog {
    private JTextField usernameField;
    private String username = null;

    public LoginWindow(JFrame parent) {
        super(parent, "Login", true);
        setSize(300, 150);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel label = new JLabel("Username:");
        usernameField = new JTextField(15);
        JButton loginButton = new JButton("Login");

        // Layout
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(label, gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(loginButton, gbc);

        add(panel);

        // Action for login button
        loginButton.addActionListener(e -> {
            username = usernameField.getText();
            dispose();
        });

        // Enter key submits
        usernameField.addActionListener(e -> {
            username = usernameField.getText();
            dispose();
        });
    }

    public String getUsername() {
        return username;
    }
}