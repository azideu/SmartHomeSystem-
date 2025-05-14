package gui;

import javax.swing.*;
import java.util.ArrayList;

public class DeviceListModel extends AbstractListModel<String> {
    private final ArrayList<String> devices = new ArrayList<>();

    @Override
    public int getSize() {
        return devices.size();
    }

    @Override
    public String getElementAt(int index) {
        return devices.get(index);
    }

    public void addDevice(String device) {
        devices.add(device);
        fireIntervalAdded(this, devices.size() - 1, devices.size() - 1);
    }

    public void removeDevice(int index) {
        devices.remove(index);
        fireIntervalRemoved(this, index, index);
    }
}