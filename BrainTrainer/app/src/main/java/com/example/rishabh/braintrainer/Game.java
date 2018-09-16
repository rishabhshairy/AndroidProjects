package com.example.rishabh.braintrainer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Rishabh on 12/23/2017.
 */

public class Game extends AppCompatActivity {
    TextView timerTextView;
    TextView sumTextView;
    TextView scoreTextView;
    TextView answerTextView;
    Button option1;
    Button option2;
    Button option3;
    Button option4;
    Button playButton;
    ArrayList<Integer> answer=new ArrayList<Integer>();
    int locationOfAnswer;
    int score=0;
    int questions=0;
    CountDownTimer countDownTimer;

    public void timerCount()
    {
        countDownTimer=new CountDownTimer(31000,1000) {
            @Override
            public void onTick(long l) {
                timerTextView.setText(String.valueOf(l/1000)+"s");

            }

            @Override
            public void onFinish() {
                timerTextView.setText("00s");
                answerTextView.setText("Game Over");
                playButton.setVisibility(View.VISIBLE);

            }
        }.start();
    }

    public void generateQuestion()
    {
        Random rand=new Random();
        int a=rand.nextInt(21);
        int b=rand.nextInt(21);
        sumTextView.setText(Integer.toString(a)+"+"+Integer.toString(b));

        locationOfAnswer=rand.nextInt(4);
        answer.clear();
        int incorrectAnswer;

        for (int i=0;i<4;i++)
        {
            if (i==locationOfAnswer)
            {
                answer.add(a+b);
            }
            else
            {
                incorrectAnswer=rand.nextInt(41);
                while (incorrectAnswer==a+b)
                {
                    incorrectAnswer=rand.nextInt(41);
                }
                answer.add(incorrectAnswer);
            }
        }

        option1.setText(Integer.toString(answer.get(0)));
        option2.setText(Integer.toString(answer.get(1)));
        option3.setText(Integer.toString(answer.get(2)));
        option4.setText(Integer.toString(answer.get(3)));

    }

    public void chooseAnswer(View view)
    {
        if(view.getTag().toString().equals(Integer.toString(locationOfAnswer)))
        {
            score++;
            answerTextView.setText("Correct!!");


        }
        else
        {
            answerTextView.setText("Oops Wrong!!");


        }
        questions++;
        scoreTextView.setText(Integer.toString(score)+"/"+Integer.toString(questions));
        generateQuestion();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        timerTextView=(TextView)findViewById(R.id.timerTextView);
        sumTextView=(TextView)findViewById(R.id.sumTextView);
        scoreTextView=(TextView)findViewById(R.id.scoreTextView);
        answerTextView=(TextView)findViewById(R.id.answerTextView);
        option1=(Button)findViewById(R.id.option1);
        option2=(Button)findViewById(R.id.option2);
        option3=(Button)findViewById(R.id.option3);
        option4=(Button)findViewById(R.id.option4);
        playButton=(Button)findViewById(R.id.playAgain);


        generateQuestion();
        timerCount();

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score=0;
                questions=0;
                countDownTimer.start();
                answerTextView.setText("");
                timerCount();
                generateQuestion();
                scoreTextView.setText("0/0");
                playButton.setVisibility(View.INVISIBLE);
            }
        });

    }
}
