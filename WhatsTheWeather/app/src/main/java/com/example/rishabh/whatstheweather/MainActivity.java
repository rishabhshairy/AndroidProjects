package com.example.rishabh.whatstheweather;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String WEATHER_MESSAGE="com.example.rishabh.whatstheweather.MESSAGE";
    Button button;
    EditText locationText;
    ArrayList<String> weatherComponents=new ArrayList();
    String message="";


    public double convert(double temperatue)
    {

        temperatue = temperatue-273.15;
        return temperatue;
    }

    public class DownloadTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... urls) {

            URL  url;
            HttpURLConnection urlConnection=null;
            String result="";

            try {

                url=new URL(urls[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while (data!=-1)
                {
                    char current=(char)data;
                    result+=current;
                    data=reader.read();
                }
                return result;

            }catch (Exception e)
            {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            try {

                JSONObject jsonObject=new JSONObject(result);
                String weatherInfo=jsonObject.getString("weather");
                JSONArray weatherArr=new JSONArray(weatherInfo);

                JSONObject mainInfo=jsonObject.getJSONObject("main");



                for (int i=0;i<weatherArr.length();i++) {
                    JSONObject weatherPart = weatherArr.getJSONObject(i);
                    weatherComponents.add(weatherPart.getString("main").toString());
                    weatherComponents.add(weatherPart.getString("description").toString());


                }

                double temp=Math.round(convert(mainInfo.getDouble("temp")));
                weatherComponents.add(Double.toString(temp)+"*Degrees");

                double tempmax=Math.round(convert(mainInfo.getDouble("temp_max")));
                weatherComponents.add(Double.toString(tempmax)+"*Degrees");

                double tempmin=Math.round(convert(mainInfo.getDouble("temp_min")));
                weatherComponents.add(Double.toString(tempmin)+"*Degrees");



                for (int i=0;i<weatherComponents.size();i++)
                {
                    switch (i)
                    {
                        case 0:message+="Main:"+weatherComponents.get(i)+"\r\n";
                            break;
                        case 1:message+="Description:"+weatherComponents.get(i)+"\r\n";
                            break;
                        case 2:message+="Temprature:"+weatherComponents.get(i)+"\r\n";
                            break;

                        case 3:message+="Max:"+weatherComponents.get(i)+"\r\n";
                            break;

                        case 4:message+="Min:"+weatherComponents.get(i)+"\r\n";
                            break;
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Could not find weather", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=(Button)findViewById(R.id.button);
        locationText=(EditText)findViewById(R.id.locationText);

        final Intent i=new Intent(this,Weather.class);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    InputMethodManager mgr=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(locationText.getWindowToken(),0);

                    String encodedLocation= URLEncoder.encode(locationText.getText().toString(),"UTF-8");
                    DownloadTask task=new DownloadTask();
                    task.execute("http://api.openweathermap.org/data/2.5/weather?q="+encodedLocation+"&&appid=bedcf6ac538c2a0a584093c4fe6fa2a2");
                } catch (UnsupportedEncodingException e) {
                    Toast.makeText(MainActivity.this, "Could not find weather", Toast.LENGTH_SHORT).show();
                }
            i.putExtra(WEATHER_MESSAGE,message);

            weatherComponents.clear();
            }
        });


}
}
