package com.example.rishabh.eggtimer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView timerTextView;
    SeekBar timerSeekBar;
    Button timerButton;
    boolean counterActive=false;
    CountDownTimer countDownTimer;

    public void reset()
    {
        timerSeekBar.setProgress(30);
        timerTextView.setText("0:30");
        countDownTimer.cancel();
        timerButton.setText("GO!!");
        timerSeekBar.setEnabled(true);
        counterActive=false;
    }
    public void update(int secondLeft)
    {
        int minutes=(int)secondLeft/60;
        int seconds=secondLeft-minutes*60;
        String secondString=Integer.toString(seconds);
        if (seconds<=9)
        {
            secondString="0"+secondString;
        }

        timerTextView.setText(Integer.toString(minutes)+":"+secondString);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         timerTextView=(TextView)findViewById(R.id.timerTextView);
        timerSeekBar=(SeekBar)findViewById(R.id.timeSeekBar);
        timerButton=(Button)findViewById(R.id.timerButton);
        timerSeekBar.setMax(600);
        timerSeekBar.setProgress(30);
        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                update(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counterActive==false)
                {
                    counterActive=true;
                    timerSeekBar.setEnabled(false);
                    timerButton.setText("Stop");
                   countDownTimer = new CountDownTimer(timerSeekBar.getProgress()*1000+100,1000){

                        @Override
                        public void onTick(long l) {
                            update((int)l/1000);
                        }

                        @Override
                        public void onFinish() {
                            timerTextView.setText("00:00");
                            MediaPlayer mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.airhorn);
                            mediaPlayer.start();
                            reset();
                        }
                    }.start();
                }
                else{
                    reset();

                }

            }
        });
    }
}
