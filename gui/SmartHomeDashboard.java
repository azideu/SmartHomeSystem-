package gui;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalTime;
import javax.swing.text.MaskFormatter;
import devices.Device;
import devices.Schedulable;
import backend.User;
import utils.DeviceType;

public class SmartHomeDashboard extends JFrame {
    private DefaultListModel<String> deviceListModel;
    private JList<String> deviceList;
    private User user;
    private JLabel clockLabel;

    public SmartHomeDashboard(User user) {
        this.user = user;
        setTitle("Smart Home Dashboard - " + user.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);

        deviceListModel = new DefaultListModel<>();
        deviceList = new JList<>(deviceListModel);
        JScrollPane deviceScrollPane = new JScrollPane(deviceList);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(12, 1, 10, 10)); // 12 rows: 1 for clock, 1 for label, 9 for buttons

        panel.add(new JLabel("Select an action:", SwingConstants.CENTER));

        // --- Clock label ---
        clockLabel = new JLabel();
        clockLabel.setHorizontalAlignment(SwingConstants.CENTER);
        clockLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        panel.add(clockLabel);

        JButton addDeviceBtn = new JButton("Add Device");
        JButton setScheduleBtn = new JButton("Set Device Schedule");
        JButton turnOnBtn = new JButton("Turn Device On");
        JButton turnOffBtn = new JButton("Turn Device Off");
        JButton showStatusBtn = new JButton("Show All Status");
        JButton activateAllBtn = new JButton("Activate All Functions");
        JButton runScheduledBtn = new JButton("Run Scheduled Actions");
        JButton removeDeviceBtn = new JButton("Remove Device");
        JButton exitBtn = new JButton("Exit");

        panel.add(addDeviceBtn);
        panel.add(setScheduleBtn);
        panel.add(turnOnBtn);
        panel.add(turnOffBtn);
        panel.add(showStatusBtn);
        panel.add(activateAllBtn);
        panel.add(runScheduledBtn);
        panel.add(removeDeviceBtn);
        panel.add(exitBtn);

        add(panel, BorderLayout.WEST);
        add(deviceScrollPane, BorderLayout.CENTER);

        // --- Start clock timer ---
        javax.swing.Timer timer = new javax.swing.Timer(1000, evt -> {
            clockLabel.setText("Current Time: " + java.time.LocalTime.now().withNano(0));
        });
        timer.start();

        refreshDeviceList();

        // 1. Add Device
        addDeviceBtn.addActionListener(e -> {
            DeviceType[] types = DeviceType.values();
            String typeStr = (String) JOptionPane.showInputDialog(
                    this,
                    "Select device type:",
                    "Device Type",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    Arrays.stream(types).map(Enum::toString).toArray(),
                    types[0].toString()
            );
            if (typeStr == null) return;

            DeviceType selectedType = null;
            for (DeviceType dt : types) {
                if (dt.toString().equals(typeStr)) {
                    selectedType = dt;
                    break;
                }
            }
            if (selectedType == null) return;

            Map<String, String> fieldTypes = selectedType.getFormFieldTypes();
            String[] fields = fieldTypes.keySet().toArray(new String[0]);
            if (fields.length == 0) return;

            JPanel formPanel = new JPanel(new GridLayout(fields.length, 2));
            JComponent[] inputFields = new JComponent[fields.length];

            for (int i = 0; i < fields.length; i++) {
                String field = fields[i];
                String type = fieldTypes.get(field);
                formPanel.add(new JLabel(field + ":"));
                if ("int".equals(type)) {
                    inputFields[i] = new JFormattedTextField(NumberFormat.getIntegerInstance());
                } else if ("boolean".equals(type)) {
                    JComboBox<String> combo = new JComboBox<>(new String[]{"true", "false"});
                    inputFields[i] = combo;
                } else if (type.startsWith("enum:")) {
                    String[] options = type.substring(5).split(",");
                    JComboBox<String> combo = new JComboBox<>(options);
                    inputFields[i] = combo;
                } else {
                    inputFields[i] = new JTextField();
                }
                formPanel.add(inputFields[i]);
            }

            boolean valid = false;
            while (!valid) {
                // Reset all backgrounds
                for (JComponent comp : inputFields) comp.setBackground(Color.white);

                int result = JOptionPane.showConfirmDialog(this, formPanel, "Configure " + selectedType,
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result != JOptionPane.OK_OPTION) return;

                valid = true;
                Object[] args = new Object[fields.length];
                Class<?>[] argTypes = new Class<?>[fields.length];

                for (int i = 0; i < fields.length; i++) {
                    String type = fieldTypes.get(fields[i]);
                    try {
                        if ("int".equals(type)) {
                            String val = ((JFormattedTextField) inputFields[i]).getText();
                            int intVal = val.isEmpty() ? 0 : Integer.parseInt(val);

                            // Special validation for Aircon temperature
                            if (selectedType.toString().equalsIgnoreCase("AIRCON") && fields[i].equalsIgnoreCase("temperature")) {
                                if (intVal < 16 || intVal > 32) {
                                    inputFields[i].setBackground(new Color(255, 180, 180));
                                    JOptionPane.showMessageDialog(this, "Temperature for Aircon must be between 16 and 32°C.");
                                    valid = false;
                                }
                            }
                            args[i] = intVal;
                            argTypes[i] = int.class;
                        } else if ("boolean".equals(type)) {
                            String val = (String) ((JComboBox<?>) inputFields[i]).getSelectedItem();
                            args[i] = Boolean.parseBoolean(val);
                            argTypes[i] = boolean.class;
                        } else if (type.startsWith("enum:")) {
                            args[i] = ((JComboBox<?>) inputFields[i]).getSelectedItem();
                            argTypes[i] = String.class;
                        } else {
                            args[i] = ((JTextField) inputFields[i]).getText();
                            argTypes[i] = String.class;
                        }
                    } catch (Exception ex) {
                        inputFields[i].setBackground(new Color(255, 180, 180));
                        valid = false;
                    }
                }

                // Name validation (must not be empty or whitespace)
                String name = args[0].toString();
                if (name.trim().isEmpty()) {
                    inputFields[0].setBackground(new Color(255, 180, 180));
                    JOptionPane.showMessageDialog(this, "Device name cannot be empty.");
                    valid = false;
                }
                // Check for duplicate name
                for (String deviceName : user.getAllDeviceNames()) {
                    if (deviceName.equals(name)) {
                        inputFields[0].setBackground(new Color(255, 180, 180));
                        JOptionPane.showMessageDialog(this, "A device with that name already exists.");
                        valid = false;
                    }
                }

                if (valid) {
                    try {
                        Device device = selectedType.getDeviceClass().getConstructor(argTypes).newInstance(args);
                        user.addDevice(device);
                        refreshDeviceList();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Failed to add device.");
                    }
                }
            }
        });

        // 2. Set Device Schedule
        setScheduleBtn.addActionListener(e -> {
            List<String> schedulableNames = new ArrayList<>();
            for (String deviceName : user.getAllDeviceNames()) {
                if (user.getDevice(deviceName) instanceof Schedulable) {
                    schedulableNames.add(deviceName);
                }
            }
            if (schedulableNames.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No schedulable devices available.");
                return;
            }
            String selected = (String) JOptionPane.showInputDialog(
                    this,
                    "Select device:",
                    "Schedulable Devices",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    schedulableNames.toArray(),
                    schedulableNames.get(0)
            );
            if (selected == null) return;

            boolean valid = false;
            MaskFormatter timeFormatter = null;
            try {
                timeFormatter = new MaskFormatter("##:##");
                timeFormatter.setPlaceholderCharacter('0');
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }
            JFormattedTextField timeField = new JFormattedTextField(timeFormatter);

            while (!valid) {
                timeField.setBackground(Color.white);
                int result = JOptionPane.showConfirmDialog(this, timeField, "Schedule time (HH:mm)", JOptionPane.OK_CANCEL_OPTION);
                if (result != JOptionPane.OK_OPTION) return;
                String timeStr = timeField.getText();

                try {
                    String[] parts = timeStr.split(":");
                    int h = Integer.parseInt(parts[0]);
                    int m = Integer.parseInt(parts[1]);
                    if (h < 0 || h > 23 || m < 0 || m > 59) throw new NumberFormatException();
                    LocalTime time = LocalTime.of(h, m);
                    user.setDeviceSchedule(selected, time);
                    JOptionPane.showMessageDialog(this, "Schedule set for " + selected + " at " + time);
                    valid = true;
                } catch (Exception ex) {
                    timeField.setBackground(new Color(255, 180, 180));
                    JOptionPane.showMessageDialog(this, "Invalid time format or out of range.");
                }
            }
        });

        // 3. Turn Device On
        turnOnBtn.addActionListener(e -> {
            String selected = deviceList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a device to turn on.");
            } else {
                String deviceName = selected.split("  \\[")[0];
                Device device = user.getDevice(deviceName);
                if (device != null) {
                    device.turnOn();
                    JOptionPane.showMessageDialog(this, deviceName + " turned ON.");
                }
            }
        });

        // 4. Turn Device Off
        turnOffBtn.addActionListener(e -> {
            String selected = deviceList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a device to turn off.");
            } else {
                String deviceName = selected.split("  \\[")[0];
                Device device = user.getDevice(deviceName);
                if (device != null) {
                    device.turnOff();
                    JOptionPane.showMessageDialog(this, deviceName + " turned OFF.");
                }
            }
        });

        // 5. Show All Status
        showStatusBtn.addActionListener(e -> {
            String status = user.showAllStatus();
            JOptionPane.showMessageDialog(this, status, "All Device Status", JOptionPane.INFORMATION_MESSAGE);
        });

        // 6. Activate All Functions
        activateAllBtn.addActionListener(e -> {
            user.activateAllFunctions();
            refreshDeviceList();
            String status = user.showAllStatus();
            JOptionPane.showMessageDialog(this, status, "All Device Status", JOptionPane.INFORMATION_MESSAGE);
        });

        // 7. Run Scheduled Actions
        runScheduledBtn.addActionListener(e -> {
            boolean valid = false;
            MaskFormatter timeFormatter = null;
            try {
                timeFormatter = new MaskFormatter("##:##");
                timeFormatter.setPlaceholderCharacter('0');
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }
            JFormattedTextField timeField = new JFormattedTextField(timeFormatter);

            while (!valid) {
                timeField.setBackground(Color.white);
                int result = JOptionPane.showConfirmDialog(this, timeField, "Time to run scheduled actions (HH:mm)", JOptionPane.OK_CANCEL_OPTION);
                if (result != JOptionPane.OK_OPTION) return;
                String timeStr = timeField.getText();

                try {
                    String[] parts = timeStr.split(":");
                    int h = Integer.parseInt(parts[0]);
                    int m = Integer.parseInt(parts[1]);
                    if (h < 0 || h > 23 || m < 0 || m > 59) throw new NumberFormatException();
                    LocalTime time = LocalTime.of(h, m);
                    user.runScheduledActions(time);
                    JOptionPane.showMessageDialog(this, "Scheduled actions executed for " + time);
                    valid = true;
                } catch (Exception ex) {
                    timeField.setBackground(new Color(255, 180, 180));
                    JOptionPane.showMessageDialog(this, "Invalid time format or out of range.");
                }
            }
        });

        // 8. Remove Device
        removeDeviceBtn.addActionListener(e -> {
            String selected = deviceList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a device to remove.");
            } else {
                String deviceName = selected.split("  \\[")[0];
                Device device = user.getDevice(deviceName);
                if (device != null) {
                    user.removeDevice(device);
                    refreshDeviceList();
                }
            }
        });

        // 0. Exit
        exitBtn.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private void refreshDeviceList() {
        deviceListModel.clear();
        for (String name : user.getAllDeviceNames()) {
            Device device = user.getDevice(name);
            String display = name + "  [" + device.getType() + "]";
            deviceListModel.addElement(display);
        }
    }
}