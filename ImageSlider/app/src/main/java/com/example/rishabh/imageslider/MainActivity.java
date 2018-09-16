package com.example.rishabh.imageslider;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    customSwip customSwip;

    Button b1;

    ImageView imageView;
    int[] imgResource={R.drawable.capture1,
            R.drawable.capture2,
            R.drawable.capture3,
            R.drawable.capture4,
            R.drawable.capture5};
    int img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager=(ViewPager)findViewById(R.id.viewPager);

        final MediaPlayer mp=MediaPlayer.create(this,R.raw.gets);
        customSwip=new customSwip(this);
        viewPager.setAdapter(customSwip);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //mp.start();
                switch (position)
                {
                    case 0:
                        Toast.makeText(MainActivity.this, "Picture 1", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "Picture 2", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }



}
