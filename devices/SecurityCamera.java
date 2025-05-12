package devices;

import utils.DeviceType;

public class SecurityCamera extends Device {
    public SecurityCamera(String name) {
        super(name, DeviceType.SECURITY_CAMERA);
    }

    @Override
    public void performDeviceFunction() {
        System.out.println(name + " is recording and uploading footage.");
    }
}

