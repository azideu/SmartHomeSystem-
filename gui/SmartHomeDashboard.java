package gui;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import devices.Device;
import devices.Schedulable;
import backend.User;
import utils.DeviceType;

public class SmartHomeDashboard extends JFrame {
    private DefaultListModel<String> deviceListModel;
    private JList<String> deviceList;
    private User user;

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
        panel.setLayout(new GridLayout(11, 1, 10, 10));
        panel.add(new JLabel("Select an action:", SwingConstants.CENTER));

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
        
            String name = JOptionPane.showInputDialog(this, "Enter device name:");
            if (name == null || name.trim().isEmpty()) return;
        
            // Check for duplicate name
            for (String deviceName : user.getAllDeviceNames()) {
                if (deviceName.equals(name)) {
                    JOptionPane.showMessageDialog(this, "A device with that name already exists.");
                    return;
                }
            }
        
            for (DeviceType dt : types) {
                if (dt.toString().equals(typeStr)) {
                    try {
                        // Show config dialog and get values
                        String[] configValues = showDeviceConfigDialog(dt);
                        if (configValues == null) return; // User cancelled
        
                        // Create device (currently only name is used in constructor)
                        Device device = dt.getDeviceClass().getConstructor(String.class).newInstance(name);
        
                        // Set config fields for each device type
                        if (device instanceof devices.Light && configValues.length == 2) {
                            ((devices.Light) device).setBrightness(Integer.parseInt(configValues[0]));
                            ((devices.Light) device).setColor(configValues[1]);
                        } else if (device instanceof devices.Aircon && configValues.length == 2) {
                            ((devices.Aircon) device).setTemperature(Integer.parseInt(configValues[0]));
                            ((devices.Aircon) device).setMode(configValues[1]);
                        } else if (device instanceof devices.SecurityCamera && configValues.length == 1) {
                            ((devices.SecurityCamera) device).setResolution(configValues[0]);
                        } else if (device instanceof devices.SmartSpeaker && configValues.length == 2) {
                            ((devices.SmartSpeaker) device).setVolume(Integer.parseInt(configValues[0]));
                            ((devices.SmartSpeaker) device).setPlaylist(configValues[1]);
                        } else if (device instanceof devices.DoorLock && configValues.length == 1) {
                            // For DoorLock, configValues[0] could be "true"/"false" or "locked"/"unlocked"
                            boolean locked = configValues[0].equalsIgnoreCase("true") ||
                                             configValues[0].equalsIgnoreCase("locked");
                            ((devices.DoorLock) device).setLocked(locked);
                        }
        
                        user.addDevice(device);
                        refreshDeviceList();
                        return;
                    } catch (Exception ex) {
                        ex.printStackTrace();
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

            String timeStr = JOptionPane.showInputDialog(this, "Schedule time (HH:mm):");
            if (timeStr == null) return;
            try {
                String[] parts = timeStr.split(":");
                int h = Integer.parseInt(parts[0]);
                int m = Integer.parseInt(parts[1]);
                LocalTime time = LocalTime.of(h, m);
                user.setDeviceSchedule(selected, time);
                JOptionPane.showMessageDialog(this, "Schedule set for " + selected + " at " + time);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid time format.");
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
            String timeStr = JOptionPane.showInputDialog(this, "Time to run scheduled actions (HH:mm):");
            if (timeStr == null) return;
            try {
                String[] parts = timeStr.split(":");
                int h = Integer.parseInt(parts[0]);
                int m = Integer.parseInt(parts[1]);
                LocalTime time = LocalTime.of(h, m);
                user.runScheduledActions(time);
                JOptionPane.showMessageDialog(this, "Scheduled actions executed for " + time);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid time format.");
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

    // Dynamic config dialog for device creation
    private String[] showDeviceConfigDialog(DeviceType type) {
        try {
            Device tempDevice = type.getDeviceClass().getConstructor(String.class).newInstance("temp");
            String[] fields = tempDevice.getConfigFields();
            if (fields.length == 0) return new String[0];

            String[] values = new String[fields.length];
            JPanel panel = new JPanel(new GridLayout(fields.length, 2));
            JTextField[] textFields = new JTextField[fields.length];

            for (int i = 0; i < fields.length; i++) {
                panel.add(new JLabel(fields[i] + ":"));
                textFields[i] = new JTextField();
                panel.add(textFields[i]);
            }

            int result = JOptionPane.showConfirmDialog(this, panel, "Configure " + type,
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                for (int i = 0; i < fields.length; i++) {
                    values[i] = textFields[i].getText();
                }
                return values;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
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