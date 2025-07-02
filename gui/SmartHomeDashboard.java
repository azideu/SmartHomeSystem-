package gui;

import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalTime;
import javax.swing.text.MaskFormatter;
import devices.Device;
import devices.Schedulable;
import backend.User;
import utils.DeviceType;
import java.awt.image.BufferedImage;
import javax.swing.Timer;

public class SmartHomeDashboard extends JFrame {
    private User user;
    private JLabel clockLabel;
    private JPanel statusPanel;
    
    // Enhanced color scheme with accent colors
    private final Color bgColor = new Color(22, 29, 35);      // Dark background
    private final Color panelColor = new Color(34, 41, 47);   // Slightly lighter panels
    private final Color buttonNormal = new Color(44, 51, 57); 
    private final Color buttonHover = new Color(60, 67, 73);  
    private final Color buttonPressed = new Color(28, 35, 41);
    private final Color textColor = Color.WHITE;
    private final Color accentColor = new Color(0, 150, 255); // Blue accent
    private final Color successColor = new Color(76, 175, 80); // Green for success
    private final Color warningColor = new Color(255, 152, 0); // Orange for warnings
    private final Color inputBg = new Color(44, 51, 57);      
    private final Color borderColor = new Color(60, 70, 80);  
    private final Color errorBg = new Color(255, 180, 180);   

    public SmartHomeDashboard(User user) {
        this.user = user;
        setDarkThemeDefaults();
        initializeUI();
    }

    private void setDarkThemeDefaults() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Create the border first
            Border inputBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            );
            
            // Set UI defaults for all components
            UIManager.put("OptionPane.background", bgColor);
            UIManager.put("Panel.background", bgColor);
            UIManager.put("Button.background", buttonNormal);
            UIManager.put("Button.foreground", textColor);
            UIManager.put("Label.foreground", textColor);
            UIManager.put("TextField.background", inputBg);
            UIManager.put("TextField.foreground", textColor);
            UIManager.put("TextField.caretForeground", textColor);
            UIManager.put("TextField.border", inputBorder);
            UIManager.put("ComboBox.background", inputBg);
            UIManager.put("ComboBox.foreground", textColor);
            UIManager.put("ComboBox.selectionBackground", buttonHover);
            UIManager.put("ComboBox.selectionForeground", textColor);
            UIManager.put("TextArea.background", inputBg);
            UIManager.put("TextArea.foreground", textColor);
            UIManager.put("TextArea.caretForeground", textColor);
            UIManager.put("ComboBox.buttonBackground", buttonNormal);
            UIManager.put("ComboBox.buttonHighlight", textColor);
            UIManager.put("ComboBox.buttonShadow", textColor);
            UIManager.put("ComboBox.buttonDarkShadow", textColor);
            UIManager.put("OptionPane.messageForeground", textColor);
            
            // Custom settings for dialog buttons
            UIManager.put("OptionPane.buttonBackground", buttonNormal);
            UIManager.put("OptionPane.buttonForeground", textColor);
            UIManager.put("OptionPane.buttonFocus", buttonHover);
            UIManager.put("Button.select", buttonHover);
            UIManager.put("Button.background", buttonNormal);
            UIManager.put("Button.foreground", textColor);
            UIManager.put("Button.focus", buttonHover);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeUI() {
        setTitle("Smart Home Dashboard - " + user.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(bgColor);
        setIconImage(createAppIcon());

        // Main layout with header, content, and footer
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Header panel with welcome and clock
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Status summary panel
        statusPanel = createStatusPanel();
        mainPanel.add(statusPanel, BorderLayout.CENTER);

        // Control buttons panel
        JPanel controlsPanel = createControlsPanel();
        mainPanel.add(controlsPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setupClock();
        updateStatusPanel();
        setVisible(true);
    }

    private Image createAppIcon() {
        int size = 32;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(accentColor);
        g2.fillPolygon(new int[] {size/2, size-4, 4}, new int[] {4, size-4, size-4}, 3);
        g2.setColor(panelColor);
        g2.fillRect(size/4, size/2, size/2, size/2);
        
        g2.dispose();
        return image;
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel welcomeLabel = new JLabel("Welcome, " + user.getName() + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        welcomeLabel.setForeground(accentColor);
        welcomeLabel.setBorder(new EmptyBorder(0, 0, 5, 0));
        panel.add(welcomeLabel, BorderLayout.NORTH);

        clockLabel = new JLabel("", SwingConstants.RIGHT);
        clockLabel.setFont(new Font("Consolas", Font.BOLD, 16));
        clockLabel.setForeground(textColor);
        panel.add(clockLabel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(panelColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(borderColor, 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        panel.setOpaque(true);

        JLabel titleLabel = new JLabel("Home Status ");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(accentColor);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(titleLabel);

        JLabel statusContent = new JLabel("<html>Loading status...</html>");
        statusContent.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusContent.setForeground(textColor);
        statusContent.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(statusContent);

        return panel;
    }

    private void updateStatusPanel() {
        StringBuilder statusText = new StringBuilder("<html><div style='width:300px'>");
        
        int totalDevices = user.getAllDeviceNames().size();
        int activeDevices = (int) user.getAllDeviceNames().stream()
            .filter(name -> user.getDevice(name).isOn())
            .count();
        
        statusText.append("<b>Devices:</b> ").append(activeDevices)
                 .append(" active / ").append(totalDevices).append(" total<br>");
        
        user.getAllDeviceNames().stream()
            .filter(name -> user.getDevice(name) instanceof Schedulable)
            .findFirst()
            .ifPresent(name -> {
                LocalTime schedule = ((Schedulable)user.getDevice(name)).getSchedule();
                if (schedule != null) {
                    statusText.append("<b>Next scheduled action:</b> ").append(name)
                             .append(" at ").append(schedule).append("<br>");
                }
            });
        
        statusText.append("</div></html>");
        
        Component[] components = statusPanel.getComponents();
        if (components.length > 1) {
            ((JLabel)components[1]).setText(statusText.toString());
        }
    }

    private JPanel createControlsPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(new EmptyBorder(15, 0, 10, 0));

        JPanel buttonsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        buttonsPanel.setBackground(bgColor);

        String[] buttonLabels = {
            "Add Device", "Set Schedule", 
            "Turn On", "Turn Off", 
            "Show Status", "Test All",
            "Run Schedule", "Remove Device"
        };

        for (String label : buttonLabels) {
            JButton button = createModernButton(label);
            buttonsPanel.add(button);
            
            switch (label) {
                case "Add Device": button.addActionListener(e -> addDeviceAction()); break;
                case "Set Schedule": button.addActionListener(e -> setScheduleAction()); break;
                case "Turn On": button.addActionListener(e -> turnOnAction()); break;
                case "Turn Off": button.addActionListener(e -> turnOffAction()); break;
                case "Show Status": button.addActionListener(e -> showStatusAction()); break;
                case "Test All": button.addActionListener(e -> activateAllAction()); break;
                case "Run Schedule": button.addActionListener(e -> runScheduledAction()); break;
                case "Remove Device": button.addActionListener(e -> removeDeviceAction()); break;
            }
        }

        mainPanel.add(buttonsPanel, BorderLayout.CENTER);

        JButton exitButton = new JButton("EXIT") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(new Color(0x60, 0x03, 0x10));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(0xA0, 0x07, 0x1F));
                } else {
                    g2.setColor(new Color(0x80, 0x05, 0x17));
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                g2.setColor(new Color(0x40, 0x02, 0x0B));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        
        exitButton.setContentAreaFilled(false);
        exitButton.setOpaque(false);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        exitButton.setFocusPainted(false);
        exitButton.setBorderPainted(false);
        exitButton.setBorder(new EmptyBorder(12, 15, 12, 15));
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitButton.addActionListener(e -> System.exit(0));
        
        JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        exitPanel.setBackground(bgColor);
        exitButton.setPreferredSize(new Dimension(250, 45));
        exitPanel.add(exitButton);
        
        mainPanel.add(exitPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JButton createModernButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(buttonPressed);
                } else if (getModel().isRollover()) {
                    g2.setColor(buttonHover);
                } else {
                    g2.setColor(buttonNormal);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                g2.setColor(borderColor);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setForeground(textColor);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(new EmptyBorder(10, 15, 10, 15));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        return button;
    }

    private void setupClock() {
        new Timer(1000, e -> {
            String time = LocalTime.now().withNano(0).toString();
            clockLabel.setText("<html><b>Time:</b> " + time + "</html>");
            updateStatusPanel();
        }).start();
    }

    private int showStyledOptionDialog(Object message, String title, int optionType) {
        JButton[] buttons = new JButton[2];
        buttons[0] = createModernButton("OK");
        buttons[1] = optionType == JOptionPane.OK_CANCEL_OPTION ? createModernButton("Cancel") : null;

        JOptionPane pane = new JOptionPane(
            message, 
            JOptionPane.PLAIN_MESSAGE, 
            optionType, 
            null, 
            optionType == JOptionPane.OK_CANCEL_OPTION ? 
                new Object[] {buttons[0], buttons[1]} : 
                new Object[] {buttons[0]}
        );
        
        JDialog dialog = pane.createDialog(this, title);
        dialog.setIconImage(createAppIcon());
        
        buttons[0].addActionListener(e -> {
            pane.setValue(JOptionPane.OK_OPTION);
            dialog.dispose();
        });
        
        if (buttons[1] != null) {
            buttons[1].addActionListener(e -> {
                pane.setValue(JOptionPane.CANCEL_OPTION);
                dialog.dispose();
            });
        }
        
        dialog.setVisible(true);
        Object selectedValue = pane.getValue();
        
        if (selectedValue == null || (buttons[1] != null && selectedValue == buttons[1])) {
            return JOptionPane.CANCEL_OPTION;
        }
        return JOptionPane.OK_OPTION;
    }

    private String showOptionDialog(String message, String title, String[] options) {
        JPanel panel = new JPanel();
        panel.setBackground(bgColor);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel label = new JLabel("<html><font color='white'>" + message + "</font></html>");
        panel.add(label);
        
        JComboBox<String> comboBox = createStyledComboBox(options);
        panel.add(comboBox);
        
        int result = showStyledOptionDialog(panel, title, JOptionPane.OK_CANCEL_OPTION);
        
        return result == JOptionPane.OK_OPTION ? (String)comboBox.getSelectedItem() : null;
    }

    private void showMessageDialog(String message, Color highlightColor) {
        JLabel label = new JLabel("<html><div style='width:250px'>" + message + "</div></html>");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(textColor);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(highlightColor, 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        panel.add(label, BorderLayout.CENTER);
        
        showStyledOptionDialog(panel, "Message", JOptionPane.DEFAULT_OPTION);
    }

    private void addDeviceAction() {
        DeviceType[] types = DeviceType.values();
        String selectedType = showOptionDialog(
            "Select device type:",
            "Device Type",
            Arrays.stream(types).map(Enum::toString).toArray(String[]::new)
        );
        if (selectedType == null) return;
        configureDevice(selectedType);
    }

    private void configureDevice(String typeStr) {
        DeviceType selectedType = Arrays.stream(DeviceType.values())
            .filter(dt -> dt.toString().equals(typeStr))
            .findFirst().orElse(null);
        if (selectedType == null) return;

        Map<String, String> fieldTypes = selectedType.getFormFieldTypes();
        String[] fields = fieldTypes.keySet().toArray(new String[0]);
        if (fields.length == 0) return;

        JPanel formPanel = new JPanel(new GridLayout(fields.length, 2));
        formPanel.setBackground(bgColor);
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JComponent[] inputs = new JComponent[fields.length];
        for (int i = 0; i < fields.length; i++) {
            JLabel label = new JLabel("<html><font color='white'>" + fields[i] + ":</font></html>");
            formPanel.add(label);
            
            inputs[i] = createInputComponent(fieldTypes.get(fields[i]));
            formPanel.add(inputs[i]);
        }

        while (true) {
            for (JComponent comp : inputs) 
                comp.setBackground(inputBg);
            
            int result = showStyledOptionDialog(
                formPanel, "Configure " + selectedType, JOptionPane.OK_CANCEL_OPTION
            );
            
            if (result != JOptionPane.OK_OPTION) return;
            if (validateInputs(selectedType, fields, fieldTypes, inputs)) break;
        }
        
        try {
            Object[] args = new Object[fields.length];
            Class<?>[] argTypes = new Class<?>[fields.length];
            for (int i = 0; i < fields.length; i++) {
                String type = fieldTypes.get(fields[i]);
                if ("int".equals(type)) {
                    args[i] = Integer.parseInt(((JFormattedTextField)inputs[i]).getText());
                    argTypes[i] = int.class;
                } else if ("boolean".equals(type)) {
                    args[i] = Boolean.parseBoolean((String)((JComboBox<?>)inputs[i]).getSelectedItem());
                    argTypes[i] = boolean.class;
                } else if (type.startsWith("enum:")) {
                    args[i] = ((JComboBox<?>)inputs[i]).getSelectedItem();
                    argTypes[i] = String.class;
                } else {
                    args[i] = ((JTextComponent)inputs[i]).getText();
                    argTypes[i] = String.class;
                }
            }
            
            Device device = selectedType.getDeviceClass()
                .getConstructor(argTypes).newInstance(args);
            user.addDevice(device);
            showMessageDialog("Device added successfully!", successColor);
            updateStatusPanel();
        } catch (Exception ex) {
            showMessageDialog("Failed to add device: " + ex.getMessage(), errorBg);
        }
    }

    private boolean validateInputs(DeviceType selectedType, String[] fields, 
            Map<String, String> fieldTypes, JComponent[] inputs) {
        boolean valid = true;
        String name = "";
        
        try {
            for (int i = 0; i < fields.length; i++) {
                String type = fieldTypes.get(fields[i]);
                if ("int".equals(type)) {
                    String val = ((JFormattedTextField)inputs[i]).getText();
                    int intVal = val.isEmpty() ? 0 : Integer.parseInt(val);

                    if (selectedType.toString().equalsIgnoreCase("AIRCON") && 
                        fields[i].equalsIgnoreCase("temperature") && 
                        (intVal < 16 || intVal > 32)) {
                        inputs[i].setBackground(errorBg);
                        showMessageDialog("Temperature must be between 16 and 32°C.", errorBg);
                        valid = false;
                    }
                }
                
                if (i == 0) {
                    name = ((JTextComponent)inputs[i]).getText().trim();
                    if (name.isEmpty()) {
                        inputs[i].setBackground(errorBg);
                        showMessageDialog("Device name cannot be empty.", errorBg);
                        valid = false;
                    }
                }
            }
            
            if (valid && user.getAllDeviceNames().contains(name)) {
                inputs[0].setBackground(errorBg);
                showMessageDialog("A device with that name already exists.", errorBg);
                valid = false;
            }
        } catch (Exception ex) {
            showMessageDialog("Invalid input format", errorBg);
            valid = false;
        }
        
        return valid;
    }

    private JComponent createInputComponent(String type) {
        if ("int".equals(type)) {
            JFormattedTextField field = new JFormattedTextField(NumberFormat.getIntegerInstance());
            styleInputField(field);
            return field;
        } else if ("boolean".equals(type)) {
            return createStyledComboBox(new String[]{"true", "false"});
        } else if (type.startsWith("enum:")) {
            return createStyledComboBox(type.substring(5).split(","));
        } else {
            JTextField field = new JTextField();
            styleInputField(field);
            return field;
        }
    }

private void styleInputField(JTextComponent field) {
    field.setBackground(inputBg);
    field.setForeground(textColor);
    field.setCaretColor(textColor);
    field.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(borderColor),  // Outer border (line)
        BorderFactory.createEmptyBorder(5, 5, 5, 5)   // Inner border (padding)
    ));  // Properly closes both the compound border and setBorder call
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setBackground(inputBg);
        combo.setForeground(textColor);
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? buttonHover : inputBg);
                setForeground(textColor);
                return this;
            }
        });
        return combo;
    }

    private void setScheduleAction() {
        List<String> schedulableNames = new ArrayList<>();
        for (String name : user.getAllDeviceNames()) {
            if (user.getDevice(name) instanceof Schedulable) {
                schedulableNames.add(name);
            }
        }
        
        if (schedulableNames.isEmpty()) {
            showMessageDialog("No schedulable devices available.", warningColor);
            return;
        }
        
        String selected = showOptionDialog(
            "Select device:", 
            "Schedulable Devices", 
            schedulableNames.toArray(new String[0])
        );
        if (selected == null) return;

        try {
            JFormattedTextField timeField = new JFormattedTextField(createTimeFormatter());
            timeField.setColumns(5);
            styleInputField(timeField);
            
            JPanel panel = new JPanel();
            panel.setBackground(bgColor);
            panel.add(new JLabel("<html><font color='white'>Schedule time (HH:mm):</font></html>"));
            panel.add(timeField);
            
            int result = showStyledOptionDialog(
                panel, "Set Schedule", JOptionPane.OK_CANCEL_OPTION
            );
            
            if (result == JOptionPane.OK_OPTION) {
                String timeStr = timeField.getText();
                String[] parts = timeStr.split(":");
                int h = Integer.parseInt(parts[0]);
                int m = Integer.parseInt(parts[1]);
                
                if (h < 0 || h > 23 || m < 0 || m > 59) {
                    throw new IllegalArgumentException();
                }
                
                LocalTime time = LocalTime.of(h, m);
                user.setDeviceSchedule(selected, time);
                showMessageDialog("Schedule set for " + selected + " at " + time, successColor);
                updateStatusPanel();
            }
        } catch (Exception ex) {
            showMessageDialog("Invalid time format. Please use HH:mm format (24-hour).", errorBg);
        }
    }

    private MaskFormatter createTimeFormatter() {
        try {
            MaskFormatter formatter = new MaskFormatter("##:##");
            formatter.setPlaceholderCharacter('0');
            return formatter;
        } catch (Exception e) {
            return null;
        }
    }

    private void turnOnAction() {
        String selected = showDeviceSelectionDialog("Select device to turn ON:", "Turn On Device");
        if (selected != null) {
            user.getDevice(selected).turnOn();
            showMessageDialog(selected + " turned ON successfully!", successColor);
            updateStatusPanel();
        }
    }

    private void turnOffAction() {
        String selected = showDeviceSelectionDialog("Select device to turn OFF:", "Turn Off Device");
        if (selected != null) {
            user.getDevice(selected).turnOff();
            showMessageDialog(selected + " turned OFF successfully!", successColor);
            updateStatusPanel();
        }
    }

    private String showDeviceSelectionDialog(String message, String title) {
        String[] deviceNames = user.getAllDeviceNames().toArray(new String[0]);
        return showOptionDialog(message, title, deviceNames);
    }

    private void showStatusAction() {
        String status = user.showAllStatus();
        JTextArea textArea = new JTextArea(status);
        textArea.setEditable(false);
        styleInputField(textArea);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        // Create custom panel with styled OK button
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Use our styled dialog method instead of JOptionPane.showMessageDialog
        showStyledOptionDialog(panel, "Device Status", JOptionPane.DEFAULT_OPTION);
    }

    private void activateAllAction() {
        user.activateAllFunctions();
        showMessageDialog("All devices turned on for 5 seconds!", successColor);
        updateStatusPanel();
    }

    private void runScheduledAction() {
        try {
            JFormattedTextField timeField = new JFormattedTextField(createTimeFormatter());
            timeField.setColumns(5);
            styleInputField(timeField);
            
            JPanel panel = new JPanel();
            panel.setBackground(bgColor);
            panel.add(new JLabel("<html><font color='white'>Enter time to run schedules (HH:mm):</font></html>"));
            panel.add(timeField);
            
            int result = showStyledOptionDialog(
                panel, "Run Scheduled Actions", JOptionPane.OK_CANCEL_OPTION
            );
            
            if (result == JOptionPane.OK_OPTION) {
                String timeStr = timeField.getText();
                String[] parts = timeStr.split(":");
                int h = Integer.parseInt(parts[0]);
                int m = Integer.parseInt(parts[1]);
                
                if (h < 0 || h > 23 || m < 0 || m > 59) {
                    throw new IllegalArgumentException();
                }
                
                LocalTime time = LocalTime.of(h, m);
                user.runScheduledActions(time);
                showMessageDialog("Scheduled actions executed for " + time, successColor);
                updateStatusPanel();
            }
        } catch (Exception ex) {
            showMessageDialog("Invalid time format. Please use HH:mm format (24-hour).", errorBg);
        }
    }

    private void removeDeviceAction() {
        String selected = showDeviceSelectionDialog("Select device to remove:", "Remove Device");
        if (selected != null) {
            user.removeDevice(user.getDevice(selected));
            showMessageDialog(selected + " removed successfully!", successColor);
            updateStatusPanel();
        }
    }
}