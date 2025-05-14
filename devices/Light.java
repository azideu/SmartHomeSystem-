package devices;

import utils.DeviceType;

public class Light extends Device {
    public static final String[] FORM_FIELDS = {"name", "brightness", "color"};

    private int brightness;
    private String color;

    public Light(String name) {
        super(name, DeviceType.LIGHT);
        this.brightness = 100;
        this.color = "White";
    }

    public Light(String name, int brightness, String color) {
        super(name, DeviceType.LIGHT);
        this.brightness = brightness;
        this.color = color;
    }

    public static String[] getFormFields() {
        return FORM_FIELDS;
    }

    public void setBrightness(int brightness) { this.brightness = brightness; }
    public void setColor(String color) { this.color = color; }
    public int getBrightness() { return brightness; }
    public String getColor() { return color; }

    @Override
    public void performDeviceFunction() {
        System.out.println(getName() + " is changing color to " + color + " at brightness " + brightness + "%.");
    }

    @Override
    public String[] getConfigFields() {
        return new String[] {"brightness", "color"};
    }
}