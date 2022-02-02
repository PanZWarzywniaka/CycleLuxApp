package com.example.lightapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor sensor;
    private ConstraintLayout layout;
    private final String baseColor = "#00CF01";

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float dx = event.values[0];
            float dy = event.values[1];
            float dz = event.values[2];
            double score = Math.sqrt(dx*dx + dy*dy + dz*dz);
            String msg = "Score: "+ score;
//            String msg = "Values:" +
//                    "    dx: " + dx +
//                    "    dy: " + dy +
//                    "    dz: " + dz;
            Log.d("EVENT", msg);
            changeBackgroundColor((int)score);

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.d("MY_APP", sensor.toString() + " - " + accuracy);
        }
    };

    public void add(android.view.View view){
        setContentView(R.layout.add_device);
    }

    public void high(android.view.View view){

        view.findViewById(R.id.low).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.medium).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.high).setVisibility(View.VISIBLE);
    }

    public void low(android.view.View view){
        view.findViewById(R.id.low).setVisibility(View.VISIBLE);
        view.findViewById(R.id.medium).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.high).setVisibility(View.INVISIBLE);
    }

    public void medium(android.view.View view){
        view.findViewById(R.id.low).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.medium).setVisibility(View.VISIBLE);
        view.findViewById(R.id.high).setVisibility(View.INVISIBLE);
    }

    private void changeBackgroundColor(int offset) {
        String colorOffset = toHexColorString(offset*50);
        String colorStr = baseColor.replace("00",colorOffset);
        Log.d("Color", colorStr);
        int color = Color.parseColor(colorStr);
        layout.setBackgroundColor(color);
    }
    private String toHexColorString(int value) {
        String ret = Integer.toHexString(value);
        if(ret.length()==1)
            return "0"+ret;

        if(ret.length()>2)
            return "FF";
        return ret;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        layout = findViewById(R.id.layout);
        changeBackgroundColor(0);
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
    }

}






