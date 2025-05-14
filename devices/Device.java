package devices;

import utils.DeviceType;

import java.time.Duration;
import java.time.Instant;

public abstract class Device {
    protected String name;
    protected boolean isOn;
    protected DeviceType type;
    protected Instant lastOnTime;
    protected long totalOnDuration; // in seconds

    public Device(String name, DeviceType type) {
        this.name = name;
        this.type = type;
        this.isOn = false;
        this.totalOnDuration = 0;
    }

    public void turnOn() {
        if (!isOn) {
            isOn = true;
            lastOnTime = Instant.now();
            System.out.println(name + " is now ON.");
        }
    }

    public void turnOff() {
        if (isOn) {
            isOn = false;
            totalOnDuration += Duration.between(lastOnTime, Instant.now()).getSeconds();
            System.out.println(name + " is now OFF.");
        }
    }

    public abstract String[] getConfigFields();

    public abstract void performDeviceFunction();

    public String getName() {
        return name;
    }

    public DeviceType getType() {
        return type;
    }

    public boolean isOn() {
        return isOn;
    }

    public String getStatus() {
        return isOn ? "ON" : "OFF";
    }

    public long getUsageInSeconds() {
        return totalOnDuration;
    }
}

