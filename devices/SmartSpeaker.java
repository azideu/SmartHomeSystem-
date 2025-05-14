package devices;

import utils.DeviceType;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class SmartSpeaker extends Device implements Schedulable {
    public static final String[] FORM_FIELDS = {"name", "volume", "playlist"};

    private LocalTime schedule;
    private boolean playing = false;
    private int volume;
    private String playlist;

    public SmartSpeaker(String name) {
        super(name, DeviceType.SMART_SPEAKER);
        this.volume = 50;
        this.playlist = "Top Hits";
    }

    public SmartSpeaker(String name, int volume, String playlist) {
        super(name, DeviceType.SMART_SPEAKER);
        this.volume = volume;
        this.playlist = playlist;
    }

    public static String[] getFormFields() {
        return FORM_FIELDS;
    }

    public static Map<String, String> getFormFieldTypes() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("name", "string");
        map.put("volume", "int");
        map.put("playlist", "string");
        return map;
    }

    public void setVolume(int volume) { this.volume = volume; }
    public void setPlaylist(String playlist) { this.playlist = playlist; }
    public int getVolume() { return volume; }
    public String getPlaylist() { return playlist; }

    @Override
    public void performDeviceFunction() {
        playing = true;
        System.out.println(getName() + " is playing your favorite playlist: " + playlist + " at volume " + volume + "!");
    }

    @Override
    public String getStatus() {
        if (playing) {
            return getName() + " is playing your favorite playlist: " + playlist + " at volume " + volume + "!";
        } else {
            return getName() + " is idle.";
        }
    }

    @Override
    public void setSchedule(LocalTime time) { this.schedule = time; }

    @Override
    public LocalTime getSchedule() { return schedule; }

    @Override
    public String[] getConfigFields() {
        return new String[] {"volume", "playlist"};
    }
}