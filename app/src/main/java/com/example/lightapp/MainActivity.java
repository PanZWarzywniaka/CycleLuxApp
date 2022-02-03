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

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private CameraManager mCameraManager;
    private String mCameraId;
//    private RadioGroup light_toggle;
//    private RadioGroup sensing_toggle;
//    private RadioGroup blinking_toggle;

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            float dx = event.values[0];
            float dy = event.values[1];
            float dz = event.values[2];
            double score = Math.sqrt(dx*dx + dy*dy + dz*dz);
            String msg = "Score: "+ score;
            Log.d("EVENT", msg);

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

    private void reset() {
        switchSensing(false);
        switchFlashLight(false);
        RadioGroup sensing_toggle = (RadioGroup) findViewById(R.id.sensing_toggle);
        sensing_toggle.check(R.id.sensing_off);

        RadioGroup blinking_toggle = (RadioGroup) findViewById(R.id.blinking_toggle);
        blinking_toggle.check(R.id.blinking_off);

    }
    private void setupSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }
    private void setupCamera() {
        //getting the camera manager and camera id
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup rg, int i) {
        switch(rg.getId()) {
            case R.id.light_toggle:
                boolean on = isLightOn(i);
                switchFlashLight(isLightOn(i));
                if(!on)
                    reset();
                Toast.makeText(this, "Light changed.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sensing_toggle:
                switchSensing(isSensingOn(i));
                Toast.makeText(this, "Sensing mode changed.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.blinking_toggle:
                break;
            default:
                break;
        }
    }


    private void setupButtons() {
        RadioGroup light_toggle = findViewById(R.id.light_toggle);
        light_toggle.setOnCheckedChangeListener(this);

        RadioGroup sensing_toggle = findViewById(R.id.sensing_toggle);
        sensing_toggle.setOnCheckedChangeListener(this);

        RadioGroup blinking_toggle = findViewById(R.id.blinking_toggle);
        blinking_toggle.setOnCheckedChangeListener(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupSensor();
        setupCamera();
        setupButtons();
        reset();
    }

    private boolean isLightOn(int checkedId) {
        return checkedId==R.id.lgt_on;
    }
    private boolean isSensingOn(int checkedId) {
        return checkedId==R.id.sensing_on;
    }
    private boolean isBlinkingOn(int checkedId) {
        return checkedId==R.id.blinking_on;
    }

    @Override
    protected void onResume() {
        super.onResume();
        switchSensing(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        switchSensing(false);
        switchFlashLight(false);
    }
    private void switchSensing(boolean status) {

        if(status)
            sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        else
            sensorManager.unregisterListener(sensorEventListener);
    }
    private void switchFlashLight(boolean status) {
        try {
            mCameraManager.setTorchMode(mCameraId, status);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

}






