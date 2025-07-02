package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginWindow extends JDialog {
    private JTextField usernameField;
    private String username = null;

    public LoginWindow(JFrame parent) {
        super(parent, "Login", true);
        setSize(300, 200);
        setLocationRelativeTo(parent);

        // Set colors to match SmartHomeDashboard
        Color bgColor = new Color(22, 29, 35);  // #161D23 background
        Color buttonNormal = new Color(44, 51, 57);  // #2C3339
        Color buttonHover = new Color(60, 67, 73);   // #3C4349
        Color buttonPressed = new Color(28, 35, 41); // #1C2329
        Color textColor = Color.WHITE;

        // Main panel setup
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username label
        JLabel label = new JLabel("Username:");
        label.setForeground(textColor);
        label.setFont(new Font("Arial", Font.BOLD, 12));

        // Username field
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 12));
        usernameField.setBackground(new Color(44, 51, 57));
        usernameField.setForeground(textColor);
        usernameField.setCaretColor(textColor);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 70, 80)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Login button with immediate hover effects
        JButton loginButton = new JButton("Login") {
            {
                setContentAreaFilled(false);
                setOpaque(true);
                setBackground(buttonNormal);
                setForeground(textColor);
                setFont(new Font("Arial", Font.BOLD, 12));
                setFocusPainted(false);
                setBorderPainted(false);
                setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
                setPreferredSize(new Dimension(100, 30));

                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        setBackground(buttonHover);
                        setCursor(new Cursor(Cursor.HAND_CURSOR));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        setBackground(buttonNormal);
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        setBackground(buttonPressed);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        setBackground(buttonHover);
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g2);
                g2.dispose();
            }
        };

        // Layout
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(label, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(usernameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
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