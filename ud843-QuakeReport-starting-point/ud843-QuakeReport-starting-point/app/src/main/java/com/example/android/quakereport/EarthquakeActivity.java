/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>>{

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String USGS_REQUEST_URL= "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private EarthquakeAdapter earthquakeAdapter;
    private static final int Bundle_ID=1;
    private TextView emptyView;

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        String minMag= sharedPreferences.getString(getString(R.string.settings_min_magnitude_key),getString(R.string.settings_min_magnitude_default));
        Uri baseUri=Uri.parse(USGS_REQUEST_URL);
        Uri.Builder builder=baseUri.buildUpon();
        builder.appendQueryParameter("format","geojson");
        builder.appendQueryParameter("limit","10");
        builder.appendQueryParameter("minmag",minMag);
        builder.appendQueryParameter("orderby","time");

        return new EarthquakeLoader(this,builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> data) {

        earthquakeAdapter.clear();
        if (data!=null && !data.isEmpty())
        {
            earthquakeAdapter.addAll(data);
        }
        emptyView.setText("No Quakes found");

        View loadingView=(View)findViewById(R.id.inderminateBar);
        loadingView.setVisibility(View.GONE);

    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        earthquakeAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_settings)
        {
            Intent intent=new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    /* private class EarthquakeAysncTask extends AsyncTask<String,Void,List<Earthquake>>
    {

        @Override
        protected List<Earthquake> doInBackground(String... urls) {

            if (urls.length<1 && urls[0]==null)

            {return null;}

            List<Earthquake> earthquakeList=QueryUtils.fetchData(urls[0]);

            return  earthquakeList;

        }

        @Override
        protected void onPostExecute(List<Earthquake> data) {

            earthquakeAdapter.clear();

            if (data!=null && !data.isEmpty())
            {
                earthquakeAdapter.addAll(data);
            }

        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);


        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        emptyView=(TextView)findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(emptyView);

        //check for internet connection
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=connectivityManager.getActiveNetworkInfo();
        if (info!=null&&info.isConnected())
        {
            getLoaderManager().initLoader(Bundle_ID,null,this);
        }
        else {
            View loadingIndicator=(View)findViewById(R.id.inderminateBar);
            loadingIndicator.setVisibility(View.GONE);
            emptyView.setText("No Internet connection");
        }


        // Create a new {@link ArrayAdapter} of earthquakes
        earthquakeAdapter=new EarthquakeAdapter(this,new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(earthquakeAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Earthquake currentEarthquake=earthquakeAdapter.getItem(position);
                Uri earthquakeUri=Uri.parse(currentEarthquake.getUrl());
                Intent intent=new Intent(Intent.ACTION_VIEW,earthquakeUri);
                startActivity(intent);
            }
        });

    }
}
