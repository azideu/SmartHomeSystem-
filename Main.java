import java.time.LocalTime;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import devices.Schedulable;
import devices.Device;
import user.User;
import utils.DeviceType;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static User user;
    public static void main(String[] args) {
        System.out.print("Enter username: ");
        user = new User(scanner.nextLine());

        while (true) {
            System.out.println();
            System.out.println("1. Add Device");
            System.out.println("2. Set Device Schedule");
            System.out.println("3. Turn Device On");
            System.out.println("4. Turn Device Off");
            System.out.println("5. Show All Status");
            System.out.println("6. Activate All Functions");
            System.out.println("7. Run Scheduled Actions");
            System.out.println("8: Remove device");
            System.out.println("9. List all devices");
            System.out.println("0. Exit");
            int choice = getIntegerInput("Choose an option: ");

            if (choice == 0) break;

            switch (choice) {
                case 1:
                    addDevice();
                    break;
                case 2:
                    scheduleDevice();
                    break;
                case 3:
                    turnOnDevice();
                    break;
                case 4:
                    turnOffDevice();
                    break;
                case 5:
                    showAllStatus();
                    break;
                case 6:
                    activateAllFunctions();
                    break;
                case 7:
                    runScheduledActions();
                    break;
                case 8:
                    removeDevice();
                    break;
                case 9:
                    listAllDevices();
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
        scanner.close();
    }

    
    private static int getIntegerInput(String queryText){
        int integer = 0;
        while (true) {
            try {
                System.out.println(queryText);
                integer = Integer.parseInt(scanner.nextLine());
                if (integer < 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                System.out.println("Please input a valid integer\n");
                continue;
            }
            break;
        }
        return integer;
    }

    private static LocalTime getTimeInput(String queryText) {
        int hours;
        int minutes;
        while (true) {
            try {
                System.out.println(queryText);
                String[] timeParts = scanner.nextLine().split(":");
                if (timeParts.length != 2) throw new NumberFormatException();

                hours = Integer.parseInt(timeParts[0]);
                if (hours < 0 || hours > 23) throw new NumberFormatException();

                minutes = Integer.parseInt(timeParts[1]);
                if (minutes < 0 || minutes > 59) throw new NumberFormatException();

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid time");
                continue;
            }
            break;
        }
        return LocalTime.of(hours,minutes);
    }

    private static void addDevice() {
        DeviceType[] types = DeviceType.values();
        {   //list device types
            System.out.print("(Types: ");
            for (int i = 0; i<types.length; i++) {
                System.out.print(types[i]);
                if (i<types.length-1) System.out.print(" / ");
            }
            System.out.print(")\n");
        }

        System.out.print("\nDevice type: ");
        String type = scanner.nextLine().toLowerCase();
        System.out.print("Device name: ");
        String name = scanner.nextLine();

        for (String deviceName : user.getAllDeviceNames()){
            if (deviceName.equals(name)) {
                System.out.println("A device with that name is already in the system. \nPlease chose a different name.");
                return;
            }
            //If speed becomes a problem save names as keys in empty hashmap for constant lookup speed
        }

        for (int i = 0; i < types.length; i++) {
            if(types[i].toString().toLowerCase().equals(type)) {
                try{
                    user.addDevice(
                    types[i].getDeviceClass()
                    .getConstructor(String.class)
                    .newInstance(name)
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        System.out.println("Unknown device type.");
    }

    private static void removeDevice() {
        System.out.print("Device name: ");
        String name = scanner.nextLine();
        Device device = user.getDevice(name);
        user.removeDevice(device);
    }

    private static void scheduleDevice(){
        List<String> schedulableNames = new ArrayList<>();
        for (String deviceName : user.getAllDeviceNames()) {
            if (user.getDevice(deviceName) instanceof Schedulable) {
                schedulableNames.add(deviceName);
            }
        }
        if (schedulableNames.isEmpty()) {
            System.out.println("\nNo schedulable devices available to schedule.");
            return;
        }
        System.out.println("\nSchedulable devices:");
        for (int i = 0; i < schedulableNames.size(); i++) {
            System.out.println((i + 1) + ". " + schedulableNames.get(i));
        }

        int deviceIndex = getIntegerInput("Select device by number: ") - 1;
        if (deviceIndex < 0 || deviceIndex >= schedulableNames.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        String dName = schedulableNames.get(deviceIndex);
        LocalTime time = getTimeInput("Schedule time (HH:mm): ");        
        user.setDeviceSchedule(dName, time);
    }

    private static void turnOnDevice(){
        System.out.print("\nDevice name: ");
        user.getDevice(scanner.nextLine()).turnOn();
    }

    private static void turnOffDevice(){
        turnOffDevice();
    }

    private static void showAllStatus(){
        System.out.println();
        user.showAllStatus();
    }

    private static void activateAllFunctions(){
        System.out.println();
        user.activateAllFunctions();
    }

    private static void runScheduledActions(){
        LocalTime runTime = getTimeInput("\nTime to run scheduled actions (HH:mm): ");
        user.runScheduledActions(runTime);
    }

    private static void listAllDevices(){
        Map<DeviceType, List<Device>> deviceBuckets = new HashMap<>();

        for (String name : user.getAllDeviceNames()) {
            Device device = user.getDevice(name);
            deviceBuckets.computeIfAbsent(device.getType(), _ -> new ArrayList<>()).add(device);
        }

        if (deviceBuckets.entrySet().isEmpty()){
            System.out.println("No Devices");
            return;
        }
        
        for (Map.Entry<DeviceType, List<Device>> entry : deviceBuckets.entrySet()) {
            System.out.println(entry.getKey());
            List<Device> devices = entry.getValue();
            if (!devices.isEmpty()) {
                devices.sort(Comparator.comparing(Device::getName));
                for (Device device : devices) System.out.printf("    %s%s\n", device.getName(),
                ((device instanceof Schedulable) && (((Schedulable)device).getSchedule() != null)) ? "*" : ""); //indicate schedule existance
            }
            System.out.println();
        }
    }
}
