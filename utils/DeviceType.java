package utils;

import devices.Aircon;
import devices.Device;
import devices.DoorLock;
import devices.Light;
import devices.SecurityCamera;
import devices.SmartSpeaker;

public enum DeviceType {
    LIGHT(Light.class), 
    THERMOSTAT(Aircon.class), 
    SECURITY_CAMERA(SecurityCamera.class), 
    SMARTSPEAKER(SmartSpeaker.class), 
    DOORLOCK(DoorLock.class);


    //functionality to map type to class
    private final Class<? extends Device> deviceClass;

    DeviceType(Class<? extends Device> devClass) {
        this.deviceClass = devClass;
    }

    public Class<? extends Device> getDeviceClass() {
        return deviceClass;
    }
}

