package com.myapp;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class SingleInstance {
    private static FileLock lock;

    public static boolean acquire() {
        try {
            File file = new File("app.lock");
            FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
            lock = channel.tryLock();
            return lock != null;
        } catch (Exception e) {
            return false;
        }
    }
}