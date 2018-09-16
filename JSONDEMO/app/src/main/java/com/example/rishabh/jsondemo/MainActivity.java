package com.example.rishabh.jsondemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public class DownloadTask extends AsyncTask<String,Void,String>

    {

        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection urlConnection=null;
            String result="";
            try{
                url=new URL(urls[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while (data!=-1)

                {
                    char current=(char) data;
                    result+=current;
                    data=reader.read();

                }

                return result;
            }

            catch (Exception e)
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
                String weatherInfo=jsonObject.getString("main");
                Log.i("main content",weatherInfo);
                JSONArray arr=new JSONArray(weatherInfo);
                for (int i=0;i<arr.length();i++)
                {
                    JSONObject jsonObject1=arr.getJSONObject(i);

                    Log.i("main",jsonObject1.getString("main"));
                    Log.i("description",jsonObject1.getString("description"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownloadTask task=new DownloadTask();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q=London,uk&&appid=bedcf6ac538c2a0a584093c4fe6fa2a2");
    }
}
