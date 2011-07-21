package com.objectica.bbot.android;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.objectica.bbot.android.util.SensorEventDigest;
import com.objectica.bbot.android.widgets.EngineBar;

/**
 * @author ivan
 */
public class BbotControl extends Activity implements SensorEventListener {
	private TextView console;
	private SensorManager manager;
	private Sensor sensor;

	public static final float[] X_THRESHOLD = new float[] { -1.0f, 1.0f };
	public static final float X_CEILING = 10f;
	public static final float[] Y_THRESHOLD = new float[] { -1.0f, 1.0f };
	public static final float Y_CEILING = 10f;

	private EngineBar leftEngine;
	private EngineBar rightEngine;
	private MovementType moving = MovementType.IDLE;
	private boolean engineRunning = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		console = (TextView) findViewById(R.id.console);
		leftEngine = (EngineBar) findViewById(R.id.leftEngine);
		rightEngine = (EngineBar) findViewById(R.id.rightEngine);
		manager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}

	public void toggleEngineRunning(View view) {
		if (engineRunning) {
			engineRunning = false;
			manager.unregisterListener(this);
		} else {
			engineRunning = true;
			manager.registerListener(this, sensor,
					SensorManager.SENSOR_DELAY_GAME);
		}
	}

	/**
	 * Should have this in case we switch from activity to preserve battery
	 */
	@Override
	protected void onPause() {
		super.onPause();
		manager.unregisterListener(this);
		engineRunning = false;
		setEngSpd(0f, 0f);
	}

	private void setEngSpd(float left, float right) {
		leftEngine.setValue(left);
		rightEngine.setValue(right);
	}

	/**
	 * Updates engine bars
	 * 
	 * @param x
	 *            gyro sensor x coord: must be [-X_CEILING,+X_CEILING]
	 * @param y
	 *            gyro sensor y coord: must be [-Y_CEILING,+Y_CEILING]
	 */
	private void updateEngines(float x, float y) {
		if (x < X_THRESHOLD[0]) {
			if (y > Y_THRESHOLD[1]) {
				moving = MovementType.FORWARD_LEFT;				

			} else if (y < Y_THRESHOLD[0]) {
				moving = MovementType.BACKWARD_LEFT;

			} else {
				moving = MovementType.ROTATE_LEFT;
				float power = (Math.abs(x) - Math.abs(X_THRESHOLD[0]))
						/ (X_CEILING - Math.abs(X_THRESHOLD[0]));
				setEngSpd(-leftEngine.getAbsoluteMax() * power, rightEngine
						.getAbsoluteMax()
						* power);
			}
		} else if (x > X_THRESHOLD[1]) {
			if (y > Y_THRESHOLD[1]) {
				moving = MovementType.FORWARD_RIGHT;
			} else if (y < Y_THRESHOLD[0]) {
				moving = MovementType.BACKWARD_RIGHT;
			} else {
				moving = MovementType.ROTATE_RIGHT;
				float power = (Math.abs(x) - Math.abs(X_THRESHOLD[1]))
						/ (X_CEILING - Math.abs(X_THRESHOLD[1]));
				setEngSpd(leftEngine.getAbsoluteMax() * power, -rightEngine
						.getAbsoluteMax()
						* power);
			}
		} else {
			if (y > Y_THRESHOLD[1]) {
				moving = MovementType.FORWARD;
				float power = (Math.abs(y) - Math.abs(Y_THRESHOLD[1]))
						/ (Y_CEILING - Math.abs(Y_THRESHOLD[1]));
				setEngSpd(leftEngine.getAbsoluteMax() * power, rightEngine
						.getAbsoluteMax()
						* power);
			} else if (y < Y_THRESHOLD[0]) {
				moving = MovementType.BACKWARD;
				float power = (Math.abs(y) - Math.abs(Y_THRESHOLD[0]))
						/ (Y_CEILING - Math.abs(Y_THRESHOLD[0]));
				setEngSpd(-leftEngine.getAbsoluteMax() * power, -rightEngine
						.getAbsoluteMax()
						* power);
			} else {
				moving = MovementType.IDLE;
				setEngSpd(0f, 0f);
			}
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float x = Math.abs(event.values[1]) > X_CEILING ? Math
				.signum(event.values[1])
				* X_CEILING : event.values[1];
		// we should invert y to get standard cartesian coord system
		float y = Math.abs(event.values[0]) > Y_CEILING ? -Math
				.signum(event.values[0])
				* Y_CEILING : -event.values[0];
		updateEngines(x, y);
		console.setText(new SensorEventDigest(event).toString());
		console.append("L: " + leftEngine.getValue()+"\n");
		console.append("R: " + rightEngine.getValue()+"\n");
		console.append("moving: " + moving);
		leftEngine.invalidate();
		rightEngine.invalidate();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int i) {
	}
		
	enum MovementType
	{
		FORWARD,
		FORWARD_LEFT,
		FORWARD_RIGHT,		
		IDLE,
		ROTATE_LEFT,
		ROTATE_RIGHT,
		BACKWARD,
		BACKWARD_LEFT,
		BACKWARD_RIGHT
	}
}
