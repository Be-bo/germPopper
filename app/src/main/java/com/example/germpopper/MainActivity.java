package com.example.germpopper;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

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

            if(shake > 20){
                //Toast.makeText(getApplicationContext(), "SHAKIN' BRAH!", Toast.LENGTH_LONG).show();

                // Play pop animation
                img.setBackgroundResource(R.drawable.pop_animation);

                // Get the background, which has been compiled to an AnimationDrawable object.
                frameAnimation = (AnimationDrawable) img.getBackground();
                frameAnimation.start();

                //a timer, to exit the app, once the animation's done playing
                Timer timer = new Timer();
                TimerTask t = new TimerTask() {
                    @Override
                    public void run() {
                        finish();
                    }
                };
                timer.schedule(t, 1500);
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
