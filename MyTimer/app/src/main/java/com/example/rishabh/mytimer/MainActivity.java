package com.example.rishabh.mytimer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    Button button;
    Button b2;
    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=(TextView)findViewById(R.id.timer);
        button=(Button)findViewById(R.id.start);
        b2=(Button)findViewById(R.id.stop);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              countDownTimer=  new CountDownTimer(30000, 1000) {
                    @Override
                    public void onTick(long l) {
                        textView.setText("Seconds remaining"+l/1000);
                    }

                    @Override
                    public void onFinish() {
                        textView.setText("Done");
                    }
                }.start();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.cancel();
            }
        });


    }

}
