package fi.tamk.rentogames;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidFiles;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import fi.tamk.rentogames.Framework.GetSteps;

public class MyService extends Service implements SensorEventListener, StepListener, GetSteps {

    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private int numSteps;

    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        MyService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MyService.this;
        }
    }

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private static final String NOTIFICATION_CHANNEL_ID ="notification_channel_id";
    private static final String NOTIFICATION_Service_CHANNEL_ID = "service_channel";

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        loadSteps();

        sensorManager.registerListener(MyService.this, accel, SensorManager.SENSOR_DELAY_FASTEST);

        if(Build.VERSION.SDK_INT>=26) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_Service_CHANNEL_ID, "Sync Service", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Service Name");
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_Service_CHANNEL_ID)
                    .setContentTitle("Service")
                    .setContentText("Running...")
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build();
            startForeground(121, notification);
        }
    }

    public void loadSteps(){
        Gdx.files = new AndroidFiles(this.getAssets(), getFilesDir().getAbsolutePath());
        Json json = new Json();
        FileHandle file = Gdx.files.local("steps.json");

        if(file.exists()){
            numSteps = json.fromJson(int.class, file);
        }
    }

    public void saveSteps(){
        Gdx.files = new AndroidFiles(this.getAssets(), getFilesDir().getAbsolutePath());
        Json json = new Json();
        FileHandle file = Gdx.files.local("steps.json");

        file.writeString(json.prettyPrint(numSteps),false);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_UI, new Handler());
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveSteps();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        if(numSteps%100 == 0){
            saveSteps();
        }

    }

    @Override
    public int getNumSteps() {
        int steps = numSteps;
        numSteps = 0;
        return steps;
    }
}