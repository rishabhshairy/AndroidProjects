package com.example.rishabh.hikersapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView longi, lati, alt, addr,accr;
    LocationManager locationManager;
    LocationListener locationListener;
    List<Address> addressList;

    public void updateLocationInfo(Location location)
    {
        Log.i("Current location",location.toString());

        longi.setText(Double.toString(location.getLongitude()));
        lati.setText(Double.toString(location.getLatitude()));
        alt.setText(Double.toString(location.getAltitude()));
        accr.setText(Double.toString(location.getAccuracy()));

        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
        String address="";
        try {
            addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(addressList.get(0).getThoroughfare()!=null)
            {
                address+=addressList.get(0).getThoroughfare()+"\r\n";

            }
            if (addressList.get(0).getSubThoroughfare()!=null)
            {
                address+=addressList.get(0).getSubThoroughfare()+"\r\n";
            }

            if (addressList.get(0).getLocality()!=null)
            {
                address+=addressList.get(0).getLocality()+"\r\n";

            }
            if (addressList.get(0).getPostalCode()!=null)
            {
                address+=addressList.get(0).getPostalCode()+"\r\n";
            }
            if (addressList.get(0).getCountryName()!=null)
            {
                address+=addressList.get(0).getCountryName();
            }

            addr.setText(address);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        longi = (TextView) findViewById(R.id.longitude);
        lati = (TextView) findViewById(R.id.latitude);
        alt = (TextView) findViewById(R.id.altitude);
        addr = (TextView) findViewById(R.id.address);
        accr=(TextView)findViewById(R.id.accuracy);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        else
        {
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if(location!=null)
                {
                    updateLocationInfo(location);
                }
            }
        }
    }
}
