package com.jonghyeon.shakeup;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;

public class MySensor extends Activity implements SensorEventListener{

    private SensorManager manager;
    private Sensor sensor;
    private Vibrator vibrator;

    private float data=0;
    private float oldData=0;
    private int result=0;
    private Exit exit = new Exit();

    TextView tv;

    MediaPlayer mp;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wake_up);

        tv=(TextView)findViewById(R.id.parcent);


        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        manager=(SensorManager)getSystemService(SENSOR_SERVICE);
        sensor=manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        AudioManager amanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        amanager.setStreamVolume(AudioManager.STREAM_MUSIC, amanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        mp=MediaPlayer.create(getBaseContext(),R.raw.duki);
        mp.setLooping(true);
        mp.start();
    }

    @Override
    protected void onStop(){
        super.onStop();
        manager.unregisterListener(this);
        mp.stop();
        finish();
    }

    @Override
    protected void onResume(){
        super.onResume();
        manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        data=event.values[SensorManager.DATA_Y]-oldData;
        oldData=event.values[SensorManager.DATA_Y];
        vibrator.vibrate(500);
        if(Math.abs(data)>20){
            result++;
            tv.setText("남은 횟수 : " + String.valueOf(20 - result));
        }

        if(result>=20){ // 흔드는 횟수
            onStop();
            vibrator.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "횟수가 부족하여 끝낼 수 없습니다.", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){
    }

    class Exit
    {
        private boolean isExit = false;
        private final Runnable task = new Runnable()
        {
            @Override
            public void run()
            {
                isExit = false;
            }
        };

        public void doExitInOneSecond()
        {
            isExit = true;
            HandlerThread thread = new HandlerThread("doTask");
            thread.start();
            new Handler(thread.getLooper()).postDelayed(task, 2000);
        }

        public boolean isExit()
        {
            return isExit;
        }

        public void setExit(boolean isExit)
        {
            this.isExit = isExit;
        }
    }

}

