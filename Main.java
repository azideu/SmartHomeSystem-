import gui.LoginWindow;
import gui.SmartHomeDashboard;
import backend.User;
import utils.FileUtils;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {        
        SwingUtilities.invokeLater(() -> {
            // Test log entry at startup
            FileUtils.logAction("Test log entry at startup.");

            // Show login dialog
            LoginWindow login = new LoginWindow(null);
            login.setVisible(true);

            // Retrieve username from LoginWindow
            String username = login.getUsername();

            if (username != null && !username.trim().isEmpty()) {
                FileUtils.logAction("User " + username + " logged in.");
                User user = new User(username.trim());
                new SmartHomeDashboard(user);
            } else {
                FileUtils.logAction("Login cancelled or failed.");
                System.exit(0);
            }
        });
    }
}