import java.time.LocalTime;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import devices.Light;
import devices.Schedulable;
import devices.SecurityCamera;
import devices.SmartSpeaker;
import devices.Aircon;
import devices.DoorLock;
import user.User;

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
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 0) break;

            switch (choice) {
                case 1:
                    System.out.print("\nDevice type (light/aircon/camera/speaker/doorlock): ");
                    String type = scanner.nextLine().toLowerCase();
                    System.out.print("Device name: ");
                    String name = scanner.nextLine();
                    switch (type) {
                        case "light":
                            user.addDevice(new Light(name));
                            break;
                        case "aircon":
                            System.out.print("Default temperature: ");
                            int temp = Integer.parseInt(scanner.nextLine());
                            user.addDevice(new Aircon(name, temp));
                            break;
                        case "camera":
                            user.addDevice(new SecurityCamera(name));
                            break;
                        case "speaker":
                            user.addDevice(new SmartSpeaker(name));
                            break;
                        case "doorlock":
                            user.addDevice(new DoorLock(name));
                            break;
                        default:
                            System.out.println("Unknown device type.");
                    }
                    break;
                case 2:
                    List<String> schedulableNames = new ArrayList<>();
                    for (String deviceName : user.getAllDeviceNames()) {
                        if (user.getDevice(deviceName) instanceof Schedulable) {
                            schedulableNames.add(deviceName);
                        }
                    }
                    if (schedulableNames.isEmpty()) {
                        System.out.println("\nNo schedulable devices available to schedule.");
                        break;
                    }
                    System.out.println("\nSchedulable devices:");
                    for (int i = 0; i < schedulableNames.size(); i++) {
                        System.out.println((i + 1) + ". " + schedulableNames.get(i));
                    }
                    System.out.print("Select device by number: ");
                    int deviceIndex = Integer.parseInt(scanner.nextLine()) - 1;
                    if (deviceIndex < 0 || deviceIndex >= schedulableNames.size()) {
                        System.out.println("Invalid selection.");
                        break;
                    }
                    String dName = schedulableNames.get(deviceIndex);
                    System.out.print("Schedule time (HH:mm): ");
                    String[] timeParts = scanner.nextLine().split(":");
                    LocalTime time = LocalTime.of(Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]));
                    user.setDeviceSchedule(dName, time);
                    break;
                case 3:
                    System.out.print("\nDevice name: ");
                    user.getDevice(scanner.nextLine()).turnOn();
                    break;
                case 4:
                    System.out.print("\nDevice name: ");
                    user.getDevice(scanner.nextLine()).turnOff();
                    break;
                case 5:
                    System.out.println();
                    user.showAllStatus();
                    break;
                case 6:
                    System.out.println();
                    user.activateAllFunctions();
                    break;
                case 7:
                    System.out.print("\nTime to run scheduled actions (HH:mm): ");
                    String[] tParts = scanner.nextLine().split(":");
                    LocalTime runTime = LocalTime.of(Integer.parseInt(tParts[0]), Integer.parseInt(tParts[1]));
                    user.runScheduledActions(runTime);
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
        scanner.close();
    }
}
