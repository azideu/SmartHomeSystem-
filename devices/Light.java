package devices;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

import utils.DeviceType;

public class Light extends Device implements Schedulable {
    public static final String[] FORM_FIELDS = {"name", "brightness", "color"};

    private int brightness;
    private String color;
    private LocalTime schedule;
    private boolean isOn;

    public Light(String name) {
        super(name, DeviceType.LIGHT);
        this.brightness = 100;
        this.color = "White";
        this.isOn = false;
    }

    public Light(String name, int brightness, String color) {
        super(name, DeviceType.LIGHT);
        this.brightness = brightness;
        this.color = color;
    }

    public static String[] getFormFields() {
        return FORM_FIELDS;
    }

    public static Map<String, String> getFormFieldTypes() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("name", "string");
        map.put("brightness", "int");
        map.put("color", "string");
        return map;
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


    @Override
    public void setSchedule(LocalTime schedule) {
        this.schedule = schedule;
    }

    @Override
    public LocalTime getSchedule() {
        return this.schedule;
    }

    @Override
    public void checkAndActivate(LocalTime currentTime) {
        if (schedule != null && schedule.equals(currentTime) && !isOn) {
            turnOn();
        }
    }

    public void turnOn() {
        isOn = true;
        System.out.println(getName() + " light turned on at " + LocalTime.now());
    }
}
