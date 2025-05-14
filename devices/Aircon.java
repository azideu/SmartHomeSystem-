package devices;

import utils.DeviceType;

public class Aircon extends Device {
    private int temperature;
    private String mode;

    public Aircon(String name) {
        super(name, DeviceType.AIRCON);
        this.temperature = 22;
        this.mode = "Cool";
    }

    // Getters
    public int getTemperature() {
        return temperature;
    }
    public String getMode() {
        return mode;
    }

    // Setters
    public void setTemperature(int temperature) {
    this.temperature = temperature;
    }
    
    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public void performDeviceFunction() {
        System.out.println(getName() + " is set to " + temperature + "°C in " + mode + " mode.");
    }

    @Override
    public String[] getConfigFields() {
        return new String[] {"temperature", "mode"};
    }
}