package devices;

import java.time.LocalTime;

public interface Schedulable {
    void setSchedule(LocalTime time);
    LocalTime getSchedule();

    // Checks if the current time matches the schedule and activates the device if so
    void checkAndActivate(LocalTime currentTime);
}