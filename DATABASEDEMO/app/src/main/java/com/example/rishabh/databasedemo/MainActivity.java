package com.example.rishabh.databasedemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText event,year;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        event=(EditText)findViewById(R.id.eventText);
        year=(EditText)findViewById(R.id.yearText);
        add=(Button)findViewById(R.id.button);
        try{
            final SQLiteDatabase myDatabase=this.openOrCreateDatabase("Users",MODE_PRIVATE,null);
            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS records (events VARCHAR , year INT(4))");

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String events=event.getText().toString();
                    int years=Integer.parseInt(year.getText().toString());
                    myDatabase.execSQL("INSERT INTO records (events,year) VALUES('" +events +"'," + years + ")");
                    event.setText("");
                    year.setText("");
                    Toast.makeText(MainActivity.this, "Event Added", Toast.LENGTH_SHORT).show();
                       Cursor c=myDatabase.rawQuery("SELECT * FROM records",null);

            int nameIndex=c.getColumnIndex("events");
            int ageIndex=c.getColumnIndex("year");

            c.moveToFirst();

            while (c!=null)
            {
                Log.i("event:",c.getString(nameIndex));
                Log.i("year:", String.valueOf(c.getInt(ageIndex)));
            }

                }
            });



        }catch (Exception e)
        {
            e.printStackTrace();
        }



    }
}
