package user;

import devices.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private List<Device> devices;

    public User(String username) {
        this.username = username;
        this.devices = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public Device getDevice(String name) {
        for (Device d : devices) {
            if (d.getName().equalsIgnoreCase(name)) {
                return d;
            }
        }
        System.out.println("Device named '" + name + "' not found.");
        return null;
    }

    public void addDevice(Device device) {
        devices.add(device);
        System.out.println("Added device: " + device.getName());
    }

    public void removeDevice(Device device) {
        devices.remove(device);
        System.out.println("Removed device: " + device.getName());
    }

    public void setDeviceSchedule(String deviceName, LocalTime time) {
        for (Device d : devices) {
            if (d.getName().equalsIgnoreCase(deviceName) && d instanceof Schedulable) {
                ((Schedulable) d).setSchedule(time);
                System.out.println("Schedule set for " + deviceName + " at " + time);
                return;
            }
        }
        System.out.println("No schedulable device named " + deviceName + " found.");
    }

    public void showAllStatus() {
        for (Device d : devices) {
            System.out.printf("%s [%s] - %s, Used: %d seconds%n", d.getName(),
                    d.getType(), d.isOn() ? "ON" : "OFF", d.getUsageInSeconds());
        }
    }

    public void runScheduledActions(LocalTime currentTime) {
        for (Device d : devices) {
            if (d instanceof Schedulable) {
                LocalTime schedule = ((Schedulable) d).getSchedule();
                if (schedule != null && schedule.equals(currentTime)) {
                    d.turnOn();
                }
            }
        }
    }

    public void activateAllFunctions() {
        for (Device d : devices) {
            d.performDeviceFunction();
        }
    }

    public List<String> getAllDeviceNames() {
        List<String> names = new ArrayList<>();
        for (Device d : devices) {
            names.add(d.getName());
        }
        return names;
    }
}

