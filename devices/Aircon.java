package devices;

import utils.DeviceType;

import java.time.LocalTime;

public class Aircon extends Device implements Schedulable {
    private int temperature;
    private LocalTime schedule;

    //base case
    public Aircon(String name){
        super(name, DeviceType.THERMOSTAT);
        this.temperature = 22;
    }

    public Aircon(String name, int initialTemp) {
        super(name, DeviceType.THERMOSTAT);
        this.temperature = initialTemp;
    }

    @Override
    public void performDeviceFunction() {
        System.out.println(name + " is regulating temperature to " + temperature + "°C.");
    }

    public void setTemperature(int temp) {
        this.temperature = temp;
        System.out.println(name + " temperature set to " + temp + "°C.");
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

