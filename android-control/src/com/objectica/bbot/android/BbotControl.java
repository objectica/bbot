package com.objectica.bbot.android;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.objectica.bbot.android.util.SensorEventDigest;
import com.objectica.bbot.android.widgets.EngineBar;

/**
 * @author ivan
 */
public class BbotControl extends Activity implements SensorEventListener
{
    private TextView console;
    private SensorManager manager;
    private Sensor sensor;
    public static final float[] THRESHOLD = new float[]{1.0f, 1.0f};
    public static final float[] CEILING = new float[]{10f, 10f};
    private EngineBar leftEngine;
    private EngineBar rightEngine;
    private boolean engineRunning = false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        console = (TextView) findViewById(R.id.console);
        leftEngine = (EngineBar) findViewById(R.id.leftEngine);
        rightEngine = (EngineBar) findViewById(R.id.rightEngine);
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void toggleEngineRunning(View view)
    {
        if (engineRunning)
        {
            engineRunning = false;
            manager.unregisterListener(this);
        } else
        {
            engineRunning = true;
            manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    /**
     * Should have this in case we switch from activity to preserve battery
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        manager.unregisterListener(this);
    }

    private void updateEngines(float a, float b)
    {
        if(a > 0 && b < 0)
        {

        }
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        updateEngines(event.values[0],event.values[1]);
        console.setText(new SensorEventDigest(event).toString());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {
    }
}
