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
            System.out.println("8. List all devices");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

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
                    listAllDevices();
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
        scanner.close();
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
        System.out.print("Select device by number: ");
        int deviceIndex = Integer.parseInt(scanner.nextLine()) - 1;
        if (deviceIndex < 0 || deviceIndex >= schedulableNames.size()) {
            System.out.println("Invalid selection.");
            return;
        }
        String dName = schedulableNames.get(deviceIndex);
        System.out.print("Schedule time (HH:mm): ");
        String[] timeParts = scanner.nextLine().split(":");
        LocalTime time = LocalTime.of(Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]));
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
        System.out.print("\nTime to run scheduled actions (HH:mm): ");
        String[] tParts = scanner.nextLine().split(":");
        LocalTime runTime = LocalTime.of(Integer.parseInt(tParts[0]), Integer.parseInt(tParts[1]));
        user.runScheduledActions(runTime);
    }

    private static void listAllDevices(){
        Map<DeviceType, List<Device>> deviceBuckets = new HashMap<>();

        for (String name : user.getAllDeviceNames()) {
            Device device = user.getDevice(name);
            deviceBuckets.computeIfAbsent(device.getType(), k -> new ArrayList<>()).add(device);
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
