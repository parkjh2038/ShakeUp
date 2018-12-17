package com.jonghyeon.shakeup;

import java.util.TimerTask;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class MyTimerTask extends TimerTask{

    Handler handler;
    Context context;
    Intent intent;

    public MyTimerTask(Context context)
    {
        handler =new Handler();
        this.context=context;
        intent=new Intent(context,MySensor.class);
    }

    @Override
    public void run(){
        handler.post(new Runnable(){
            @Override
            public void run(){
                ((MainActivity)context).ring();
            }
        });
    }
}
