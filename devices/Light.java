package devices;

import utils.DeviceType;

import java.time.LocalTime;

public class Light extends Device implements Schedulable {
    private LocalTime schedule;

    public Light(String name) {
        super(name, DeviceType.LIGHT);
    }

    @Override
    public void performDeviceFunction() {
        System.out.println(name + " is adjusting brightness and color temperature.");
    }

    @Override
    public void setSchedule(LocalTime time) {
        this.schedule = time;
    }

    @Override
    public LocalTime getSchedule() {
        return schedule;
    }
}

