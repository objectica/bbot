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
        builder.append("values: [ ");
        for(float value : event.values)
        {
            builder.append(value).append(" ");
        }
        builder.append("]");
        return builder.toString();
    }
}
