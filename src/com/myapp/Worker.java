package com.myapp;

public class Worker extends Thread implements Config.ConfigListener {

    private volatile boolean running = true;
    private volatile int intervalSeconds;


    @Override
    public void run() {
        // int interval = Config.getInt("interval.seconds", 5);
        intervalSeconds = Config.getInt("interval.seconds", 5);

        Log.get().info("Worker started");

        while (running) {
            try {
                Log.get().info("Working...");

                // 👉 Your real logic goes here

                Thread.sleep(intervalSeconds * 1000);

            } catch (InterruptedException e) {
                running = false;
            }
        }

        Log.get().info("Worker stopped");
    }

    @Override
    public void onConfigChanged(String key, Object value) {

        if ("interval.seconds".equals(key)) {

            try {

                intervalSeconds = Integer.parseInt(value.toString());

                Log.get().info(
                    "Worker interval updated to " + intervalSeconds
                );

            } catch (Exception e) {

                Log.get().warning("Invalid interval update");
            }
        }
    }


    public void shutdown() {
        running = false;
        interrupt();
    }
}