package devices;

import utils.DeviceType;

import java.time.LocalTime;

public class SmartSpeaker extends Device implements Schedulable {
    private LocalTime schedule;

    public SmartSpeaker(String name) {
        super(name, DeviceType.SMARTSPEAKER);
    }

    @Override
    public void performDeviceFunction() {
        System.out.println(getName() + " is playing your favorite playlist!");
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