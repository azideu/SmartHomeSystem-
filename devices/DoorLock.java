package devices;

import utils.DeviceType;

public class DoorLock extends Device {
    public DoorLock(String name) {
        super(name, DeviceType.DOORLOCK);
    }

    @Override
    public void performDeviceFunction() {
        System.out.println(getName() + " is locking or unlocking the door.");
    }
}