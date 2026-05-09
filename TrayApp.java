import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TrayApp {

    private static JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createUI(); 

            if (SystemTray.isSupported()) {
                createTray();
            } else {
                System.out.println("No tray support — showing window");
                frame.setVisible(true);
            }
        });
    }

    private static void createUI() {
        frame = new JFrame("My Tray App");
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JLabel label = new JLabel("App is running in the background", SwingConstants.CENTER);
        frame.add(label, BorderLayout.CENTER);

        JButton button = new JButton("Do something");
        button.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Action executed"));
        frame.add(button, BorderLayout.SOUTH);

        // IMPORTANT: Don't exit when closing
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    private static void createTray() {
        try {
            SystemTray tray = SystemTray.getSystemTray();

            // Load icon (fallback to default if not found)
            Image image;
            try {
                image = Toolkit.getDefaultToolkit().getImage("icon.png");
            } catch (Exception e) {
                image = Toolkit.getDefaultToolkit().createImage(new byte[0]);
            }

            PopupMenu menu = new PopupMenu();

            MenuItem openItem = new MenuItem("Open");
            MenuItem exitItem = new MenuItem("Exit");

            menu.add(openItem);
            menu.addSeparator();
            menu.add(exitItem);

            TrayIcon trayIcon = new TrayIcon(image, "My Tray App", menu);
            trayIcon.setImageAutoSize(true);

            // Actions
            openItem.addActionListener(e -> showWindow());
            exitItem.addActionListener(e -> {
                tray.remove(trayIcon);
                System.exit(0);
            });

            trayIcon.addActionListener(e -> showWindow());

            tray.add(trayIcon);

            // Optional startup notification
            trayIcon.displayMessage(
                "My Tray App",
                "Application started in background",
                TrayIcon.MessageType.INFO
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showWindow() {
        if (!frame.isVisible()) {
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
        frame.toFront();
    }
}
