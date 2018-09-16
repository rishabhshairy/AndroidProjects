package com.example.rishabh.sharedprefrences;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);

         switch (item.getItemId())
         {
             case R.id.settings:
                 final AlertDialog.Builder builder=new AlertDialog.Builder(this);
                 builder.setMessage(R.string.dialogMessage);
                 builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         startActivityForResult(new Intent(Settings.ACTION_SETTINGS),0);
                     }
                 });
                 builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         return;
                     }

                 });
                 AlertDialog dialog=builder.create();
                 dialog.show();
                 return true;
             case R.id.help:
                 Toast.makeText(this, "Help Selected", Toast.LENGTH_SHORT).show();
                 return true;
             default:
                 return false;
         }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences sharedPreferences=this.getSharedPreferences("com.example.rishabh.sharedprefrences", Context.MODE_PRIVATE);
        ArrayList<String> friends=new ArrayList<>();

        friends.add("Sourav");
        friends.add("avinash");

        try {
            sharedPreferences.edit().putString("friends",ObjectSerializer.serialize(friends)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> newFriends=new ArrayList<>();

        try {
            newFriends= (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("friends",ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
