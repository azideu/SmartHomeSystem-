package utils;

import java.util.Map;

import devices.Aircon;
import devices.Device;
import devices.DoorLock;
import devices.Light;
import devices.SecurityCamera;
import devices.SmartSpeaker;

public enum DeviceType {
    LIGHT(Light.class),
    AIRCON(Aircon.class),
    SECURITY_CAMERA(SecurityCamera.class),
    SMART_SPEAKER(SmartSpeaker.class),
    DOORLOCK(DoorLock.class);

    private final Class<? extends Device> deviceClass;

    DeviceType(Class<? extends Device> devClass) {
        this.deviceClass = devClass;
    }

    public Class<? extends Device> getDeviceClass() {
        return deviceClass;
    }

    public String[] getFormFields() {
        try {
            return (String[]) deviceClass.getMethod("getFormFields").invoke(null);
        } catch (Exception e) {
            return new String[] {"name"};
        }
    }   

    @SuppressWarnings("unchecked")
    public Map<String, String> getFormFieldTypes() {
        try {
            return (Map<String, String>) deviceClass.getMethod("getFormFieldTypes").invoke(null);
        } catch (Exception e) {
            Map<String, String> fallback = new java.util.LinkedHashMap<>();
            fallback.put("name", "string");
            return fallback;
        }
    }

    @Override
    public String toString() {
        // Replace underscores with spaces and capitalize each word
        String s = name().replace('_', ' ').toLowerCase();
        String[] words = s.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(Character.toUpperCase(word.charAt(0)))
              .append(word.substring(1))
              .append(" ");
        }
        return sb.toString().trim();
    }
}