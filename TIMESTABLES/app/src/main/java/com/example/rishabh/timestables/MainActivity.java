package com.example.rishabh.timestables;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView timesList;
    public void getTimesTable(int timesTable)
    {
        ArrayList<String> tableContent=new ArrayList<String>();

        for (int i=1;i<=10;i++){
            tableContent.add(Integer.toString(i*timesTable));
        }

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,tableContent);

        timesList.setAdapter(arrayAdapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timesList=(ListView) findViewById(R.id.timesList);
        final SeekBar timesSeekBar=(SeekBar) findViewById(R.id.timesSeekBar);
        timesSeekBar.setMax(20);
        timesSeekBar.setProgress(10);

        timesSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int min=1;
                int timesTable;
                if (i<min)
                {
                    timesTable=min;
                    timesSeekBar.setProgress(min);

                }
                else {
                    timesTable=i;
                }
                getTimesTable(timesTable);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        getTimesTable(10);

    }
}
