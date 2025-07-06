package devices;

import utils.DeviceType;
import utils.FileUtils;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class Aircon extends Device implements Schedulable {
    public static final String[] FORM_FIELDS = {"name", "temperature", "mode"};

    private int temperature;
    private String mode;
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
    }

    public static String[] getFormFields() { return FORM_FIELDS; }

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
        turnOn();
        FileUtils.logAction(getName() + " aircon turned ON (" + temperature + "°C, " + mode + " mode).");
        System.out.println(getName() + " is set to " + temperature + "°C in " + mode + " mode.");
    }

    @Override
    public void turnOff() {
        super.turnOff();
        FileUtils.logAction(getName() + " aircon turned OFF.");
        System.out.println(getName() + " aircon is now OFF.");
    }

    @Override
    public String[] getConfigFields() { return new String[] {"temperature", "mode"}; }

    @Override
    public void setSchedule(LocalTime schedule) { this.schedule = schedule; }

    @Override
    public LocalTime getSchedule() { return this.schedule; }

    @Override
    public void checkAndActivate(LocalTime currentTime) {
        if (schedule != null && !isOn() && (currentTime.equals(schedule) || currentTime.isAfter(schedule))) {
            performDeviceFunction();
        }
    }
}