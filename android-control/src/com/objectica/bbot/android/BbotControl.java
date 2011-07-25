package com.objectica.bbot.android;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

	// all floors/ceiling must be > 0
	private float xFloor = 1.0f;
	private float yFloor = 1.0f;
	private float xCeiling = 10f;
	private float yCeiling = 10f;

	private EngineBar leftEngine;
	private EngineBar rightEngine;
	private MovementType moving = MovementType.IDLE;
	private boolean engineRunning = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control);
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
			manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
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
	 * Updates engine bars. Whole bunch of crap code to revise later.
	 * 
	 * @param x
	 *            gyro sensor x coord: must be [-X_CEILING,+X_CEILING]
	 * @param y
	 *            gyro sensor y coord: must be [-Y_CEILING,+Y_CEILING]
	 */
	private void updateEngines(float x, float y) {
		if (x < -xFloor) {
			if (y > yFloor) {
				if (Math.abs(x) < Math.abs(y)) {
					moving = MovementType.FORWARD_LEFT;
					float rPower = getNormY(y) / getMaxY();
					float lPower = (getNormY(y) - getNormX(x)) * rPower / getNormY(y);
					setEngSpd(lPower * leftEngine.getAbsoluteMax(), rPower * rightEngine.getAbsoluteMax());
				} else {
					moving = MovementType.IDLE;
					setEngSpd(0f, 0f);
				}
			} else if (y < -yFloor) {
				if (Math.abs(x) < Math.abs(y)) {
					moving = MovementType.BACKWARD_LEFT;
					float rPower = getNormY(y) / getMaxY();
					float lPower = (getNormY(y) - getNormX(x)) * rPower / getNormY(y);
					setEngSpd(-lPower * leftEngine.getAbsoluteMax(), -rPower * rightEngine.getAbsoluteMax());
				} else {
					moving = MovementType.IDLE;
					setEngSpd(0f, 0f);
				}
			} else {
				moving = MovementType.ROTATE_LEFT;
				float power = getNormX(x) / getMaxX();
				setEngSpd(-leftEngine.getAbsoluteMax() * power, rightEngine.getAbsoluteMax() * power);
			}
		} else if (x > xFloor) {
			if (y > yFloor) {
				if (Math.abs(x) < Math.abs(y)) {
					moving = MovementType.FORWARD_RIGHT;
					float lPower = getNormY(y) / getMaxY();
					float rPower = (getNormY(y) - getNormX(x)) * lPower / getNormY(y);
					setEngSpd(lPower * leftEngine.getAbsoluteMax(), rPower * rightEngine.getAbsoluteMax());

				} else {
					moving = MovementType.IDLE;
					setEngSpd(0f, 0f);
				}
			} else if (y < -yFloor) {
				if (Math.abs(x) < Math.abs(y)) {
					moving = MovementType.BACKWARD_RIGHT;
					float lPower = getNormY(y) / getMaxY();
					float rPower = (getNormY(y) - getNormX(x)) * lPower / getNormY(y);
					setEngSpd(-lPower * leftEngine.getAbsoluteMax(), -rPower * rightEngine.getAbsoluteMax());

				} else {
					moving = MovementType.IDLE;
					setEngSpd(0f, 0f);
				}
			} else {
				moving = MovementType.ROTATE_RIGHT;
				float power = getNormX(x) / getMaxX();
				setEngSpd(leftEngine.getAbsoluteMax() * power, -rightEngine.getAbsoluteMax() * power);
			}
		} else {
			if (y > yFloor) {
				moving = MovementType.FORWARD;
				float power = getNormY(y) / getMaxY();
				setEngSpd(leftEngine.getAbsoluteMax() * power, rightEngine.getAbsoluteMax() * power);
			} else if (y < -yFloor) {
				moving = MovementType.BACKWARD;
				float power = getNormY(y) / getMaxY();
				setEngSpd(-leftEngine.getAbsoluteMax() * power, -rightEngine.getAbsoluteMax() * power);
			} else {
				moving = MovementType.IDLE;
				setEngSpd(0f, 0f);
			}
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float x = Math.abs(event.values[1]) > xCeiling ? Math.signum(event.values[1]) * xCeiling : event.values[1];
		// invert y to get standard cartesian coord system
		float y = Math.abs(event.values[0]) > yCeiling ? -Math.signum(event.values[0]) * yCeiling : -event.values[0];
		updateEngines(x, y);
		console.setText(new SensorEventDigest(event).toString());
		console.append("L: " + leftEngine.getValue() + "\n");
		console.append("R: " + rightEngine.getValue() + "\n");
		console.append("mode: " + moving);
		leftEngine.invalidate();
		rightEngine.invalidate();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int i) {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			startActivityForResult(new Intent(this, BbotSettings.class), 0);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private float getMaxX() {
		return xCeiling - xFloor;
	}

	private float getMaxY() {
		return yCeiling - yFloor;
	}

	private float getNormX(float x) {
		return Math.abs(x) - xFloor;
	}

	private float getNormY(float y) {
		return Math.abs(y) - yFloor;
	}

	enum MovementType {
		FORWARD, FORWARD_LEFT, FORWARD_RIGHT, IDLE, ROTATE_LEFT, ROTATE_RIGHT, BACKWARD, BACKWARD_LEFT, BACKWARD_RIGHT
	}
}
