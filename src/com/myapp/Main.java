package com.myapp;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        try{
            if (!SingleInstance.acquire()) {
                Log.get().warning("App already running");
                System.exit(0);
            }

            Log.init();
            Config.load();

            Log.get().info("App starting");

            Worker worker = new Worker();
            Config.addListener(worker);
            
            worker.start();

            SwingUtilities.invokeLater(() -> {
                try {
                    TrayManager tray = new TrayManager();
                    tray.initTray(worker);
                } catch (Exception e) {
                    Log.get().severe("Tray init failed: " + e.getMessage());
                }
            });

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                Log.get().info("Shutdown hook triggered");
                worker.shutdown();
            }));
        }
         catch (Exception e) {
            Log.get().severe("Fatal error: " + e.getMessage());
            Restart.relaunch();
        }

    }
}