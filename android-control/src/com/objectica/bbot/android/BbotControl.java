package com.objectica.bbot.android;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import com.objectica.bbot.android.util.SensorEventDigest;

/**
 * @author ivan
 */
public class BbotControl extends Activity implements SensorEventListener
{
    private TextView console;
    private SensorManager manager;
    private Sensor gyro;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        console = (TextView)findViewById(R.id.console);
        manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        gyro = manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    @Override
    protected void onResume() {
         super.onResume();
         manager.registerListener(this, gyro, SensorManager.SENSOR_DELAY_NORMAL);
     }

    @Override
    protected void onPause() {
         super.onPause();
         manager.unregisterListener(this);
     }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        console.setText(new SensorEventDigest(sensorEvent).toString());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {
    }
}
