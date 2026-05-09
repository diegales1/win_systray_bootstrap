package com.myapp;

import java.nio.file.*;
import org.json.JSONObject;
import java.util.*;

public class Config {

    private static JSONObject json;
    private static final Path CONFIG_PATH = Paths.get("config.json");
    private static final List<ConfigListener> listeners = new ArrayList<>();

    public interface ConfigListener {
        void onConfigChanged(String key, Object value);
    }

    public static void addListener(ConfigListener listener) {
        listeners.add(listener);
    }

    public static void load() {

        try {

            if (!Files.exists(CONFIG_PATH)) {
                createDefault();
            }

            String content = Files.readString(CONFIG_PATH);

            json = new JSONObject(content);

        } catch (Exception e) {

            json = new JSONObject();

            Log.get().warning("Failed to load config");
        }
    }

    private static void createDefault() {

        try {

            JSONObject defaults = new JSONObject();

            defaults.put("app.name", "My Tray App");
            defaults.put("interval.seconds", 5);
            defaults.put("update.url", "");
            defaults.put("log.level", "INFO");

            Files.writeString(CONFIG_PATH, defaults.toString(4));

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static String get(String key, String def) {
        return json.optString(key, def);
    }

    public static int getInt(String key, int def) {
        return json.optInt(key, def);
    }

    public static void set(String key, Object value) {

        json.put(key, value);

        for (ConfigListener listener : listeners) {
            listener.onConfigChanged(key, value);
        }
    }

    public static void save() {

        try {

            Files.writeString(CONFIG_PATH, json.toString(4));

            Log.get().info("Config saved");

        } catch (Exception e) {

            Log.get().severe("Failed to save config");
        }
    }
}