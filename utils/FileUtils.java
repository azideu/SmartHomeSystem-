package utils;

import java.io.*;
import java.nio.file.*;
import java.util.List;

public class FileUtils {
    // Validate username:password from accounts.txt
    public static boolean validateAccount(String username, String password) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("utils/accounts.txt"));
            for (String line : lines) {
                System.out.println("Checking line: " + line); // Debug
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Log actions with timestamp to log.txt
    public static void logAction(String message) {
        try (FileWriter fw = new FileWriter("log.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            String timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            out.println(timestamp + " - " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}