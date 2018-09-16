package com.example.rishabh.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> celebNames=new ArrayList<String>();
    ArrayList<String> celebURLs=new ArrayList<String>();
    Button option1;
    Button option2;
    Button option3;
    Button option4;
    ImageView celebImage;

    int chosenCeleb=0;
    int locationOfAnswer;
    String[] answers=new String[4];

    public void newQuestion()
    {
        Random rand=new Random();
        chosenCeleb=rand.nextInt(celebURLs.size());

        try {

            ImageDownload imageDownload=new ImageDownload();
            Bitmap celebImg;
            celebImg=imageDownload.execute(celebURLs.get(chosenCeleb)).get();
            celebImage.setImageBitmap(celebImg);
            locationOfAnswer=rand.nextInt(4);
            int incorrectAnswer;
            for (int i=0;i<4 ;i++)
            {
                if (i==locationOfAnswer)
                {
                    answers[i]=celebNames.get(chosenCeleb);

                }
                else {
                    incorrectAnswer=rand.nextInt(celebURLs.size());
                    while (incorrectAnswer==chosenCeleb)
                    {
                        incorrectAnswer=rand.nextInt(celebURLs.size());
                    }

                    answers[i]=celebNames.get(incorrectAnswer);
                }
            }

            option1.setText(answers[0]);
            option2.setText(answers[1]);
            option3.setText(answers[2]);
            option4.setText(answers[3]);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    public void celebChosen(View view) {

        if(view.getTag().toString().equals(Integer.toString(locationOfAnswer)))
        {
            Toast.makeText(this, "Correct Answer", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Wrong! It is"+celebNames.get(chosenCeleb), Toast.LENGTH_SHORT).show();
        }
        newQuestion();
    }

    public class DownloadTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection urlConnection=null;
            String result="";
            try{
                url=new URL(urls[0]);
                urlConnection=(HttpURLConnection) url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1)
                {
                    char current=(char)data;
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
    }

    public class ImageDownload extends AsyncTask<String, Void, Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap myBitmap;
            URL url;
            HttpURLConnection urlConnection;
            try{
                url=new URL(urls[0]);
                urlConnection=(HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream in=urlConnection.getInputStream();
                myBitmap= BitmapFactory.decodeStream(in);
                return myBitmap;

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        celebImage=(ImageView)findViewById(R.id.downImage);
        option1=(Button)findViewById(R.id.option1);
        option2=(Button)findViewById(R.id.option2);
        option3=(Button)findViewById(R.id.option3);
        option4=(Button)findViewById(R.id.option4);




        DownloadTask task=new DownloadTask();
        String html=null;

        try {
            html=task.execute("http://www.posh24.se/kandisar").get();
            String[] splitHtml=html.split("div class=\"sidebarInnerContainer\"");
            Pattern p=Pattern.compile("<img src=\"(.*?)\"");
            Matcher m=p.matcher(splitHtml[0]);
            while (m.find())

            {
                celebURLs.add(m.group(1));
                //System.out.println(m.group(1));
            }

            p=Pattern.compile("alt=\"(.*?)\"");
            m=p.matcher(splitHtml[0]);
            while (m.find())
            {
                celebNames.add(m.group(1));
                //System.out.println(m.group(1));
            }



        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        catch (ExecutionException e) {

            e.printStackTrace();

        }
        newQuestion();

    }
}
