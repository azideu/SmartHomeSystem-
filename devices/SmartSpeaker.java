package devices;

import utils.DeviceType;
import java.time.LocalTime;

public class SmartSpeaker extends Device implements Schedulable {
    private LocalTime schedule;
    private boolean playing = false;

    public SmartSpeaker(String name) {
        super(name, DeviceType.SMART_SPEAKER);
    }

    @Override
    public void performDeviceFunction() {
        playing = true;
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

    @Override
    public String getStatus() {
        if (playing) {
            return getName() + " is playing your favorite playlist!";
        } else {
            return getName() + " is idle.";
        }
    }
}