package com.example.rishabh.weathersunshine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView weatherText;
    String[] fakeData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherText=(TextView)findViewById(R.id.tv_weather_text);
        fakeData=WeatherData.getData();
        for (String dummy:fakeData)
        {
            weatherText.append(dummy+"\n\n\n");
        }

    }
}
