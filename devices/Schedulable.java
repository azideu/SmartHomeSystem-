package devices;

import java.time.LocalTime;

public interface Schedulable {
    void setSchedule(LocalTime time);
    LocalTime getSchedule();
}

