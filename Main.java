import gui.LoginWindow;
import gui.SmartHomeDashboard;
import backend.User;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Show login dialog
            LoginWindow login = new LoginWindow(null);
            login.setVisible(true);

            // Retrieve username from LoginWindow
            String username = login.getUsername();

            if (username != null && !username.trim().isEmpty()) {
                User user = new User(username.trim());
                new SmartHomeDashboard(user);
            } else {
                // Optionally show a message or just exit
                System.exit(0);
            }
        });
    }
}