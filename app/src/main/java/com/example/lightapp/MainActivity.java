package com.example.lightapp;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;

import android.util.Log;

import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor sensor;
    //private LinearLayout layout;
    private CameraManager mCameraManager;
    private String mCameraId;

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float dx = event.values[0];
            float dy = event.values[1];
            float dz = event.values[2];
            double score = Math.sqrt(dx*dx + dy*dy + dz*dz);
            String msg = "Score: "+ score;
            Log.d("EVENT", msg);
            //changeBackgroundColor((int)score);

            //flash light
            boolean active = score > 5;
            switchFlashLight(active);



        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.d("MY_APP", sensor.toString() + " - " + accuracy);
        }
    };




//    private void changeBackgroundColor(int offset) {
//        String colorOffset = toHexColorString(offset*50);
//        String colorStr = baseColor.replace("00",colorOffset);
//        Log.d("Color", colorStr);
//        int color = Color.parseColor(colorStr);
//        layout.setBackgroundColor(color);
//    }
//    private String toHexColorString(int value) {
//        String ret = Integer.toHexString(value);
//        if(ret.length()==1)
//            return "0"+ret;
//
//        if(ret.length()>2)
//            return "FF";
//        return ret;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        //layout = findViewById(R.id.layout);
        //changeBackgroundColor(0);

        //getting the camera manager and camera id
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }



    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
        switchFlashLight(false);
    }

    private void switchFlashLight(boolean status) {
        try {
            mCameraManager.setTorchMode(mCameraId, status);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

}






