package com.jonghyeon.shakeup;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends Activity {

    Boolean set=false;
    Boolean run=false;
    Date date;
    TextView clock;
    public Button btRun, btInquire, btCall;
    Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clock=(TextView)findViewById(R.id.setClock);
        btRun=(Button)findViewById(R.id.btRun);
        btInquire=(Button)findViewById(R.id.btInquire);
        btCall=(Button)findViewById(R.id.btCall);

        if(run){
            btRun.setText("중단");
        }
        else
        {
            btRun.setText("시작");
        }

        if(!set){
            btRun.setEnabled(false);
        }

        btRun.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(run)
                {
                    stop();
                    run=false;
                    btRun.setText("시작");
                }
                else
                {
                    run();
                    run=true;
                    btRun.setText("중단");
                }
            }
        });

        btCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cIntent = new Intent(Intent.ACTION_DIAL);
                startActivity(cIntent);
            }
        });

        btInquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse("mailto:pjh950312@gmail.com");
                Intent i=new Intent(Intent.ACTION_SENDTO,uri);
                startActivity(i);
            }
        });
    }

    public void onClickClock(View view){

        Calendar cal=Calendar.getInstance();
        int nowHourOfDay=cal.get(Calendar.HOUR_OF_DAY);
        int nowMin=cal.get(Calendar.MINUTE);

        new TimePickerDialog(MainActivity.this,new TimePickerDialog.OnTimeSetListener(){

            @Override
            public void onTimeSet(TimePicker view,int hourOfDay,int minute)
            {
                date=new Date();
                Calendar cal=Calendar.getInstance();
                int nowHourOfDay=cal.get(Calendar.HOUR_OF_DAY);
                int nowMin=cal.get(Calendar.MINUTE);
                clock.setText(String.format("%02d:%02d", hourOfDay, minute));
                if(minute<=nowMin){
                    hourOfDay++;
                    if(hourOfDay>24){
                        hourOfDay=0;
                    }
                }
                if(hourOfDay<nowHourOfDay){
                    date.setDate(cal.get(Calendar.DATE+1));
                }
                date.setHours(hourOfDay);
                date.setMinutes(minute);
                date.setSeconds(0);
            }

        },nowHourOfDay,nowMin,true).show();
        btRun.setEnabled(true);
    }

    public void run()
    {
        timer=new Timer();
        TimerTask timerTask=new MyTimerTask(MainActivity.this);
        timer.schedule(timerTask, date);
        Toast.makeText(this, "알람을 설정했습니다.", Toast.LENGTH_SHORT).show();
	}

    public void stop()
    {
        if(timer!=null){
            timer.cancel();
            Toast.makeText(this,"알람을 해제했습니다.", Toast.LENGTH_SHORT).show();
        }
        btRun.setText("시작");
        btRun.setEnabled(false);
    }

    public void ring(){
        Intent intent=new Intent(this,MySensor.class);
        startActivity(intent);
        stop();
    }
}
