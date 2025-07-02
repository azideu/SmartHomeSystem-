package devices;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

import utils.DeviceType;

public class SecurityCamera extends Device implements Schedulable {
    public static final String[] FORM_FIELDS = {"name", "resolution"};

    private boolean recording;
    private String resolution;
    private LocalTime schedule;

    public SecurityCamera(String name) {
        super(name, DeviceType.SECURITY_CAMERA);
        this.recording = false;
        this.resolution = "1080p";
    }

    public SecurityCamera(String name, String resolution) {
        super(name, DeviceType.SECURITY_CAMERA);
        this.recording = false;
        this.resolution = resolution;
    }

    public static String[] getFormFields() {
        return FORM_FIELDS;
    }

    public static Map<String, String> getFormFieldTypes() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("name", "string");
        map.put("resolution", "string");
        return map;
    }

    public void setResolution(String resolution) { this.resolution = resolution; }
    public String getResolution() { return resolution; }

    @Override
    public void performDeviceFunction() {
        recording = true;
        System.out.println(getName() + " is now recording at " + resolution + ".");
    }

    @Override
    public String getStatus() {
        if (recording) {
            return getName() + " is now recording at " + resolution + "!";
        } else {
            return getName() + " is idle.";
        }
    }

    @Override
    public String[] getConfigFields() {
        return new String[] {"resolution"};
    }

    @Override
    public void checkAndActivate(LocalTime currentTime) {
        if (schedule != null && schedule.equals(currentTime)) {
            performDeviceFunction();
        }
    }

    @Override
    public void setSchedule(LocalTime schedule) {
        this.schedule = schedule;
    }

    @Override
    public LocalTime getSchedule() {
        return this.schedule;
    }
}