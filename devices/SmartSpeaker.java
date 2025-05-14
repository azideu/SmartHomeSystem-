package devices;

import utils.DeviceType;
import java.time.LocalTime;

public class SmartSpeaker extends Device implements Schedulable {
    private LocalTime schedule;
    private boolean playing = false;
    private int volume;
    private String playlist;

    public SmartSpeaker(String name) {
        super(name, DeviceType.SMART_SPEAKER);
        this.volume = 50;
        this.playlist = "Top Hits";
    }

    // Getters
    public int getVolume() {
        return volume;
    }

    public String getPlaylist() {
        return playlist;
    }

    // Setters
    public void setVolume(int volume) {
        this.volume = volume;
    }
    
    public void setPlaylist(String playlist) {
        this.playlist = playlist;
    }

    @Override
    public void performDeviceFunction() {
        playing = true;
        System.out.println(getName() + " is playing your favorite playlist: " + playlist + " at volume " + volume + "!");
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
    public String[] getConfigFields() {
        return new String[] {"volume", "playlist"};
    }

    @Override
    public String getStatus() {
        if (playing) {
            return getName() + " is playing your favorite playlist: " + playlist + " at volume " + volume + "!";
        } else {
            return getName() + " is idle.";
        }
    }
}