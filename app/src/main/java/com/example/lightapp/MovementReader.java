package com.example.lightapp;

import android.util.Log;

public class MovementReader {
    public MovementReader() {
        while(true) {
            Log.v("TEST","Hello Reader");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

}
