package com.myapp;

import java.awt.*;
import javax.swing.*;

public class TrayManager {

    private TrayIcon trayIcon;
    private JFrame frame;
    private SettingsWindow settingsWindow;
    private boolean positioned = false;

    public TrayManager() {
        createUI();
    }

    private void createUI() {


        frame = new JFrame(Config.get("app.name", "App"));
        frame.setVisible(false);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        frame.add(new JLabel("Running in background", SwingConstants.CENTER));
    }

    public void initTray(Worker worker) throws Exception {
        if (!SystemTray.isSupported()) {
            frame.setVisible(true);
            return;
        }

        SystemTray tray = SystemTray.getSystemTray();

        // Image image = Toolkit.getDefaultToolkit().getImage("icon.png");
        // Image image = new ImageIcon(getClass().getResource("/icon.png")).getImage();
        java.net.URL iconUrl = getClass().getResource("/icon.png");

        if (iconUrl == null) {
            throw new RuntimeException("icon.png not found in resources");
        }

        Image image = new ImageIcon(iconUrl).getImage();

        String appName = Config.get("app.name", "My App");

        PopupMenu menu = new PopupMenu();

        MenuItem openItem = new MenuItem("Open");
        MenuItem settingsItem = new MenuItem("Settings");
        MenuItem exitItem = new MenuItem("Exit");

        openItem.addActionListener(e -> SwingUtilities.invokeLater(this::show));

        exitItem.addActionListener(e -> {
            worker.shutdown();
            tray.remove(trayIcon);
            System.exit(0);
        });

        menu.add(openItem);
        menu.add(settingsItem);
        menu.addSeparator();
        menu.add(exitItem);

        trayIcon = new TrayIcon(image, appName, menu);
        trayIcon.setImageAutoSize(true);

        trayIcon.addActionListener(e -> SwingUtilities.invokeLater(this::show));
        // trayIcon.addActionListener(e -> show());

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            Log.get().severe("Failed to add tray icon");
            frame.setVisible(true);
        }

        trayIcon.displayMessage(
            appName,
            "Running normally",
            TrayIcon.MessageType.INFO
        );


        settingsItem.addActionListener(e -> SwingUtilities.invokeLater(() -> {

            if (settingsWindow == null) {
                settingsWindow = new SettingsWindow();
            }

            settingsWindow.setLocationRelativeTo(null);

            settingsWindow.setVisible(true);

            settingsWindow.toFront();
        }));


        trayIcon.setToolTip("Running - OK");

    }

    private void show() {
        if (!positioned) {
           frame.setLocationRelativeTo(null);
           positioned = true;
        }
        frame.setVisible(true);
        frame.toFront();
    }

    private void shutdown(Worker worker, SystemTray tray) {
        try {
            worker.shutdown();

            if (trayIcon != null) {
                tray.remove(trayIcon);
            }

            Log.get().info("Application shutdown complete");

        } finally {
            System.exit(0);
        }
    }

}