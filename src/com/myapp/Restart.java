package com.myapp;

public class Restart {
    public static void relaunch() {
        try {
            String javaBin = System.getProperty("java.home") + "/bin/java";
            String jar = new java.io.File(
                Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()
            ).getPath();

            new ProcessBuilder(javaBin, "-jar", jar).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}