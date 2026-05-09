package com.myapp;

import java.io.IOException;
import java.util.logging.*;

public class Log {
    private static Logger logger = Logger.getLogger("MyTrayApp");

    public static Logger get() {
        return logger;
    }

    public static void init() {
        try {
            LogManager.getLogManager().reset();

            logger.setLevel(Level.INFO);

            FileHandler fh = new FileHandler("app.log", 1024 * 1024, 3, true);
            fh.setFormatter(new SimpleFormatter());

            logger.addHandler(fh);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}