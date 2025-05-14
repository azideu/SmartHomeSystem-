package gui;

import javax.swing.*;
import java.awt.*;

public class DevicePanel extends JPanel {
    public DevicePanel() {
        setLayout(new BorderLayout());

        
        String[] devices = {"Light", "Aircon", "Security Camera", "Door Lock", "Smart Speaker"};
        JList<String> deviceList = new JList<>(devices);
        add(new JScrollPane(deviceList), BorderLayout.CENTER);
    }
}