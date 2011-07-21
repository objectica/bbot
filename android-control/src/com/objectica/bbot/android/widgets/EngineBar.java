package com.objectica.bbot.android.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Vertical bar
 *
 * @author ivan
 */
public class EngineBar extends View
{
    public static final int BG_COLOR = 0xff006600;
    public static final int BR_WIDTH = 20;
    public static final int CTRL_COLOR = 0xffffffff;
    public static final int CTRL_HEIGHT = 10;

    private float absoluteMax = 100f;

    private float value = 0f;


    public EngineBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    private int getControlBottom()
    {
        return (int) ((value + getAbsoluteMax()) / (2 * getAbsoluteMax()) * (getHeight() - CTRL_HEIGHT));
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawColor(BG_COLOR);
        Paint controlPaint = new Paint();
        controlPaint.setColor(CTRL_COLOR);
        controlPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(new Rect(0, getControlBottom(), getWidth(), getControlBottom() + CTRL_HEIGHT), controlPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        setMeasuredDimension(BR_WIDTH, getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    public float getValue()
    {
        return value;
    }

    public void setValue(float value)
    {
        if (value > getAbsoluteMax())
        {
            this.value = getAbsoluteMax();
        } else if (value < -getAbsoluteMax())
        {
            this.value = -getAbsoluteMax();
        } else
        {
            this.value = value;
        }

    }

    public float getAbsoluteMax()
    {
        return absoluteMax;
    }

    public void setAbsoluteMax(float absoluteMax)
    {
        this.absoluteMax = absoluteMax;
    }
}
