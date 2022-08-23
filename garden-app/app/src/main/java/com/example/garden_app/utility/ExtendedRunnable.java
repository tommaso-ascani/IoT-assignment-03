package com.example.garden_app.utility;

public interface ExtendedRunnable extends Runnable {
    void write(byte[] bytes);
    void cancel();
}
