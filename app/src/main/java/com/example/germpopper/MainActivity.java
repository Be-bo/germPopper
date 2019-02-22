package com.example.germpopper;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    private float acelVal;
    private float acelLast;
    private float shake;
    private SensorManager sm;

    private AnimationDrawable frameAnimation;
    private ImageView img;

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            acelLast=acelVal;
            acelVal = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = acelVal - acelLast;
            shake = shake * 0.9f + delta;

            if(shake > 12){
                //Toast.makeText(getApplicationContext(), "SHAKIN' BRAH!", Toast.LENGTH_LONG).show();

                // Play pop animation
                img.setBackgroundResource(R.drawable.pop_animation);

                // Get the background, which has been compiled to an AnimationDrawable object.
                frameAnimation = (AnimationDrawable) img.getBackground();
                frameAnimation.start();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(sensorListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.0f;

        // Load the ImageView that will host the animation and
        // set its background to our AnimationDrawable XML resource.
        img = (ImageView)findViewById(R.id.germ_background);
        img.setBackgroundResource(R.drawable.idle_animation);

        // Get the background, which has been compiled to an AnimationDrawable object.
        frameAnimation = (AnimationDrawable) img.getBackground();

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Start the animation. Must be started after link made after onCreate() finishes
        frameAnimation.start();
    }


}
