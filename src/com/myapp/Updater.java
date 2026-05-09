package com.myapp;

import java.io.*;
import java.net.URL;
import java.nio.file.*;

public class Updater {

    public static void check() {
        new Thread(() -> {
            try {
                BufferedReader br = new BufferedReader(
                    new InputStreamReader(new URL(Config.get("update.url", "")).openStream())
                );

                String latestVersion = br.readLine();
                String downloadUrl = br.readLine();

                String current = Config.get("app.version", "1.0.0");

                if (!current.equals(latestVersion)) {
                    Log.get().info("Updating to " + latestVersion);
                    download(downloadUrl);
                    Restart.relaunch();
                    System.exit(0);
                }

            } catch (Exception e) {
                Log.get().warning("Update check failed");
            }
        }).start();
    }

    private static void download(String url) throws Exception {
        InputStream in = new URL(url).openStream();
        Files.copy(in, Paths.get("app_new.jar"), StandardCopyOption.REPLACE_EXISTING);

        // Replace on next launch logic (simple version)
        new File("app.jar").delete();
        new File("app_new.jar").renameTo(new File("app.jar"));
    }
}