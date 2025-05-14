package devices;

import utils.DeviceType;

public class SecurityCamera extends Device {
    private boolean recording;
    private String resolution;

    public SecurityCamera(String name) {
        super(name, DeviceType.SECURITY_CAMERA);
        this.recording = false;
        this.resolution = "1080p";
    }

    // Getters
    public String getResolution() {
    return resolution;
    }

    // Setters
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    @Override
    public void performDeviceFunction() {
        recording = true;
        System.out.println(getName() + " is now recording at " + resolution + ".");
    }

    @Override
    public String[] getConfigFields() {
        return new String[] {"resolution"};
    }

    @Override
    public String getStatus() {
        if (recording) {
            return getName() + " is recording at " + resolution + ".";
        } else {
            return getName() + " is idle at " + resolution + ".";
        }
    }
}