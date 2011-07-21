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

    public static final float[] X_THRESHOLD = new float[]{-1.0f, 1.0f};
    public static final float X_CEILING = 10f;
    public static final float[] Y_THRESHOLD = new float[]{-1.0f, 1.0f};
    public static final float Y_CEILING = 10f;

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

    private void setEngSpd(float left, float right)
    {
        leftEngine.setValue(left);
        rightEngine.setValue(right);
    }

    /**
     * Updates engine bars
     *
     * @param x gyro sensor x coord: must be [-X_CEILING,+X_CEILING]
     * @param y gyro sensor y coord: must be [-Y_CEILING,+Y_CEILING]
     */
    private void updateEngines(float x, float y)
    {
        if (x < X_THRESHOLD[0])
        {
            if (y > Y_THRESHOLD[1])
            {
                //FORWARD_LEFT

            } else if (y < Y_THRESHOLD[0])
            {
                //BACKWARD_LEFT

            } else
            {
                //ROTATE_LEFT
                float power = (Math.abs(x) - Math.abs(X_THRESHOLD[0])) / (X_CEILING - Math.abs(X_THRESHOLD[0]));
                setEngSpd(-leftEngine.getAbsoluteMax() * power, rightEngine.getAbsoluteMax() * power);
            }
        } else if (x > X_THRESHOLD[1])
        {
            if (y > Y_THRESHOLD[1])
            {
                //FORWARD_RIGHT
            } else if (y < Y_THRESHOLD[0])
            {
                //BACKWARD_RIGHT
            } else
            {
                //ROTATE_RIGHT
                float power = (Math.abs(x) - Math.abs(X_THRESHOLD[1])) / (X_CEILING - Math.abs(X_THRESHOLD[1]));
                setEngSpd(leftEngine.getAbsoluteMax() * power, -rightEngine.getAbsoluteMax() * power);
            }
        } else
        {
            if (y > Y_THRESHOLD[1])
            {
                //FORWARD
                float power = (Math.abs(y) - Math.abs(Y_THRESHOLD[1])) / (Y_CEILING - Math.abs(Y_THRESHOLD[1]));
                setEngSpd(leftEngine.getAbsoluteMax() * power, rightEngine.getAbsoluteMax() * power);
            } else if (y < Y_THRESHOLD[0])
            {
                //BACKWARD
                float power = (Math.abs(y) - Math.abs(Y_THRESHOLD[0])) / (Y_CEILING - Math.abs(Y_THRESHOLD[0]));
                setEngSpd(-leftEngine.getAbsoluteMax() * power, -rightEngine.getAbsoluteMax() * power);
            } else
            {
                //CENTER
                setEngSpd(0f, 0f);
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        float x = Math.abs(event.values[0]) > X_CEILING ? Math.signum(event.values[0]) * X_CEILING : event.values[0];
        // we should invert y to get standard cartesian coord system
        float y = Math.abs(event.values[1]) > Y_CEILING ? -Math.signum(event.values[1]) * Y_CEILING : -event.values[1];
        updateEngines(x, y);
        console.setText(new SensorEventDigest(event).toString());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {
    }
}
