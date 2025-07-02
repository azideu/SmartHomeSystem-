package devices;

import utils.DeviceType;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class Aircon extends Device {
    public static final String[] FORM_FIELDS = {"name", "temperature", "mode"};

    private int temperature;
    private String mode;
    private boolean isOn; // Indicates if the aircon is currently active
    private LocalTime schedule;

    public Aircon(String name) {
        super(name, DeviceType.AIRCON);
        this.temperature = 22;
        this.mode = "Cool";
    }

    public Aircon(String name, int temperature, String mode) {
        super(name, DeviceType.AIRCON);
        this.temperature = temperature;
        this.mode = mode;
        this.isOn = false; // Initially, the aircon is off
    }

    public static String[] getFormFields() {
        return FORM_FIELDS;
    }

    public static Map<String, String> getFormFieldTypes() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("name", "string");
        map.put("temperature", "int");
        map.put("mode", "string");
        return map;
    }

    public void setTemperature(int temperature) { this.temperature = temperature; }
    public void setMode(String mode) { this.mode = mode; }
    public int getTemperature() { return temperature; }
    public String getMode() { return mode; }

    @Override
    public void performDeviceFunction() {
        System.out.println(getName() + " is set to " + temperature + "°C in " + mode + " mode.");
    }

    @Override
    public String[] getConfigFields() {
        return new String[] {"temperature", "mode"};
    }

    public void setSchedule(LocalTime schedule) {
        this.schedule = schedule;
    }

    public LocalTime getSchedule() {
        return this.schedule;
    }

    public void checkAndActivate(LocalTime currentTime) {
        if (schedule != null && !isOn() && (currentTime.equals(schedule) || currentTime.isAfter(schedule))) {
            performDeviceFunction();
        }
    }

    public void turnOn() {
        isOn = true;
        System.out.println(getName() + " light turned on at " + LocalTime.now());
    }
}