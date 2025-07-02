package devices;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

import utils.DeviceType;

public class DoorLock extends Device implements Schedulable {
    public static final String[] FORM_FIELDS = {"name", "locked"};

    private boolean locked;
    private LocalTime schedule;

    public DoorLock(String name) {
        super(name, DeviceType.DOORLOCK);
        this.locked = true;
    }

    public DoorLock(String name, boolean locked) {
        super(name, DeviceType.DOORLOCK);
        this.locked = locked;
    }

    public static String[] getFormFields() {
        return FORM_FIELDS;
    }

    public static Map<String, String> getFormFieldTypes() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("name", "string");
        map.put("locked", "enum:locked,unlocked");
        return map;
    }

    public void setLocked(boolean locked) { this.locked = locked; }
    public boolean isLocked() { return locked; }

    @Override
    public void performDeviceFunction() {
        locked = !locked;
        System.out.println(getName() + (locked ? " is now locked." : " is now unlocked."));
    }

    @Override
    public String[] getConfigFields() {
        return new String[] {"locked"};
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
        if (schedule != null && !isOn() && (currentTime.equals(schedule) || currentTime.isAfter(schedule))) {
            performDeviceFunction();
        }
    }
}