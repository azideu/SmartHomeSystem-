package devices;

import utils.DeviceType;

public class DoorLock extends Device {
    private boolean locked;

    public DoorLock(String name) {
        super(name, DeviceType.DOORLOCK);
        this.locked = true;
    }
    
    // Getters
    public boolean isLocked() {
        return locked;
    }

    // Setters
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

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