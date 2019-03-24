package fi.tamk.gameproject;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;




public class AndroidLauncher extends AndroidApplication implements SensorEventListener, StepListener{
	private StepDetector simpleStepDetector;
	private SensorManager sensorManager;
	private Sensor accel;
	private int numSteps;
	private DungeonEscape game;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("Create");

		PedometerStatus pedometerStatus = new AndroidPedometerStatus();
		// Get an instance of the SensorManager
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		simpleStepDetector = new StepDetector();
		simpleStepDetector.registerListener(this);

		numSteps = 0;

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(game = new DungeonEscape(pedometerStatus), config);


		if(pedometerStatus.getStatus() == true) {
			startPedometer();
		} else if(pedometerStatus.getStatus() == false) {
			stopPedometer();
		}

		startPedometer();


	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

		System.out.println("Accuracy");
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			simpleStepDetector.updateAccel(
					event.timestamp, event.values[0], event.values[1], event.values[2]);
			// System.out.println("sensor");


		}

//		if(game.getMeterStance()) {
//			startPedometer();
//		} else {
//			stopPedometer();
//		}

	}

	@Override
	public void step(long timeNs) {
		//numSteps++;
		game.addSteps();

	}

	public void startPedometer() {
		System.out.println("start pedometer");
		sensorManager.registerListener(AndroidLauncher.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
	}

	public void stopPedometer() {
		System.out.println("stop pedometer");
		sensorManager.unregisterListener(AndroidLauncher.this);

	}

}
