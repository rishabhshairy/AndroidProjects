package com.example.rishabh.whatstheweather;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Rishabh on 12/29/2017.
 */

public class Weather extends AppCompatActivity {

    RelativeLayout weaRelativeLayout;
    TextView textView;
    Button check;

    public class MyTimer extends TimerTask{

        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    Random  rand=new Random();

                    weaRelativeLayout.setBackgroundColor(Color.argb(255,rand.nextInt(256),rand.nextInt(256),rand.nextInt(256)));

                }
            });
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        weaRelativeLayout=(RelativeLayout)findViewById(R.id.weatherLayout);
        weaRelativeLayout.setBackgroundColor(Color.CYAN);
        textView=(TextView)findViewById(R.id.weatherTextView);
        check=(Button)findViewById(R.id.checkButton);

        Intent intent=getIntent();

        String message=intent.getStringExtra(MainActivity.WEATHER_MESSAGE);
        textView.setText(message);

        Timer timer=new Timer();

        MyTimer mt=new MyTimer();
        timer.schedule(mt,1500,1500);

    }
}
