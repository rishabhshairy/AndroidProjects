package com.example.rishabh.downloadingimages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    Button button;
    ImageView imageView ;
    DownloadImage downloadImage;

    public class DownloadImage extends AsyncTask<String,Void,Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap myBitmap;
            URL url;
            HttpsURLConnection urlConnection;
            try {
                url=new URL(urls[0]);
                urlConnection=(HttpsURLConnection)url.openConnection();
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
        button=(Button)findViewById(R.id.button);
        imageView=(ImageView)findViewById(R.id.imageView);
        downloadImage=new DownloadImage();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Tapped","Once");
                //https://thumb7.shutterstock.com/display_pic_with_logo/304117/495659485/stock-vector-vector-vintage-christmas-greeting-card-with-cartoon-santa-claus-holding-a-present-behind-a-495659485.jpg
                try {

                    Bitmap myImage;

                    myImage= downloadImage.execute("https://thumb7.shutterstock.com/display_pic_with_logo/304117/495659485/stock-vector-vector-vintage-christmas-greeting-card-with-cartoon-santa-claus-holding-a-present-behind-a-495659485.jpg").get();

                    imageView.setImageBitmap(myImage);
                }
                catch (InterruptedException e) {

                    e.printStackTrace();
                }
                catch (ExecutionException e) {

                    e.printStackTrace();
                }
            }
        });




    }
}
