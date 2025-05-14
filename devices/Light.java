package devices;

import utils.DeviceType;

public class Light extends Device {
    private int brightness;
    private String color;

    public Light(String name) {
        super(name, DeviceType.LIGHT);
        this.brightness = 100;
        this.color = "White";
    }

    // Getters
    public int getBrightness() {
        return brightness;
    }
    public String getColor() {
        return color;
    }

    // Setters
    public void setBrightness(int brightness) {
    this.brightness = brightness;
    }
    
    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public void performDeviceFunction() {
        // Example: toggle color or brightness
        System.out.println(getName() + " is changing color to " + color + " at brightness " + brightness + "%.");
    }

    @Override
    public String[] getConfigFields() {
        return new String[] {"brightness", "color"};
    }

    
}