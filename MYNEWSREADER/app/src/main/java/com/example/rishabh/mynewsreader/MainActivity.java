package com.example.rishabh.mynewsreader;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> titleList=new ArrayList<>();
    ArrayList<String> contentList=new ArrayList<>();
    ArrayAdapter arrayAdapter;
    SQLiteDatabase articleDB;

    public void updateListView()
    {
        Cursor c=articleDB.rawQuery("SELECT * FROM articles",null);
         int contentIndex=c.getColumnIndex("content");
         int titleIndex=c.getColumnIndex("title");
         if (c.moveToFirst())
         {
             titleList.clear();
             contentList.clear();

             do {

                 titleList.add(c.getString(titleIndex));
                 contentList.add(c.getString(contentIndex));
             }while (c.moveToNext());

             arrayAdapter.notifyDataSetChanged();
         }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView=(ListView)findViewById(R.id.listView);
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,titleList);
        listView.setAdapter(arrayAdapter);



        articleDB=this.openOrCreateDatabase("News",MODE_PRIVATE,null);
        articleDB.execSQL("CREATE TABLE IF NOT EXISTS articles (id INTEGER PRIMARY KEY,articleId INTEGER,title VARCHAR, content VARCHAR)");

          DownloadTask task=new DownloadTask();
        try {
            task.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateListView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getApplicationContext(),NewsActivity.class);
                intent.putExtra("content",contentList.get(i));
                startActivity(intent);
            }
        });
    }

    //download task
    public class DownloadTask extends AsyncTask<String,Void,String>
    {
        URL url;
        String result="";
        HttpsURLConnection urlConnection;
        @Override
        protected String doInBackground(String... urls) {
            try {
                url=new URL(urls[0]);
                urlConnection=(HttpsURLConnection)url.openConnection();
                 InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                 int data=reader.read();
                 while (data!=-1)
                 {
                     char c=(char)data;
                     result+=c;
                     data=reader.read();

                 }

                JSONArray jsonArray=new JSONArray(result);
                int numberOfItems=20;
                if (jsonArray.length()<20)
                {
                    numberOfItems=jsonArray.length();
                }
                articleDB.execSQL("DELETE FROM articles");
                for (int i=0;i<numberOfItems;i++)
                {
                    String articleId=jsonArray.getString(i);
                    url=new URL("https://hacker-news.firebaseio.com/v0/item/"+articleId+".json?print=pretty");
                    urlConnection=(HttpsURLConnection)url.openConnection();
                    in=urlConnection.getInputStream();
                    reader=new InputStreamReader(in);
                    data=reader.read();
                    String articleInfo="";
                    while (data!=-1)
                    {
                        char c=(char)data;
                        articleInfo+=c;
                        data=reader.read();
                    }
                    JSONObject jsonObject=new JSONObject(articleInfo);
                    if (!jsonObject.isNull("title")&& !jsonObject.isNull("url"))
                    {
                        String articleTitle=jsonObject.getString("title");
                        String articleUrl=jsonObject.getString("url");

                        url=new URL(articleUrl);
                        urlConnection=(HttpsURLConnection)url.openConnection();
                        in=urlConnection.getInputStream();
                        reader=new InputStreamReader(in);
                        data=reader.read();
                        String articleContent="";
                        while (data!=-1)
                        {
                            char c=(char)data;
                            articleContent+=c;
                            data=reader.read();
                        }

                        String sql="INSERT INTO articles (id,title,content) VALUES(? ,? , ?)";
                        SQLiteStatement statement=articleDB.compileStatement(sql);
                        statement.bindString(1,articleId);
                        statement.bindString(2,articleTitle);
                        statement.bindString(3,articleContent);
                        statement.execute();
                    }

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.i("Urls Content",result);
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            updateListView();
        }
    }


}
