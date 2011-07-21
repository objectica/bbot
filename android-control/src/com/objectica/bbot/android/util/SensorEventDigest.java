package com.objectica.bbot.android.util;

import android.hardware.SensorEvent;

/**
 * @author ivan
 */
public class SensorEventDigest
{
    private SensorEvent event;

    public SensorEventDigest(SensorEvent event)
    {
        if(event == null)
        {
            throw new IllegalArgumentException("Event must not be null!");
        }
        this.event = event;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Sensor: ").append(event.sensor.getName()).append("\n");
        builder.append("accuracy: ").append(event.accuracy).append("\n");
        builder.append("timestamp: ").append(event.timestamp).append("\n");
        for(int i = 0; i < event.values.length; i ++)
        {
            builder.append("[").append(i).append("]: ").append(event.values[i]).append("\n");
        }
        return builder.toString();
    }
}
