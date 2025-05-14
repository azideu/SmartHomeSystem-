package devices;

import utils.DeviceType;

public class DoorLock extends Device {
    public static final String[] FORM_FIELDS = {"name", "locked"};

    private boolean locked;

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
}