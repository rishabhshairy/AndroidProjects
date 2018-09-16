package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Rishabh on 1/17/2018.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    String loc;
    String offLoc;

    public EarthquakeAdapter(Context context, List<Earthquake> resource) {
        super(context,0,resource);
    }


    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        View listitemView=convertView;

        if (listitemView==null)
        {
            listitemView= LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent,false);
        }

        Earthquake currentEarthquake=getItem(position);

        TextView magnitude=(TextView)listitemView.findViewById(R.id.magnitude);
        String mag=formatMagnitude(currentEarthquake.getMagnitude());
        magnitude.setText(mag);
        TextView offLocation=(TextView)listitemView.findViewById(R.id.offsetlocation);
        TextView location=(TextView)listitemView.findViewById(R.id.location);
        if (currentEarthquake.getLocation().contains("of"))
        {
            String parts[]=currentEarthquake.getLocation().split("of");
            offLoc=parts[0];
            loc=parts[1];
        }
        else {
            offLoc="near the";
            loc=currentEarthquake.getLocation();
        }

        offLocation.setText(offLoc);
        location.setText(loc);

        TextView date=(TextView)listitemView.findViewById(R.id.date);
        TextView time=(TextView)listitemView.findViewById(R.id.time);
        Date dateObject=new Date(currentEarthquake.getDate());
        String formattedDate=formatDate(dateObject);
        date.setText(formattedDate);
        Date timeObject=new Date(currentEarthquake.getDate());
        String formattedTime=formatTime(timeObject);
        time.setText(formattedTime);
        
        
        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();

         // Get the appropriate background color based on the current earthquake magnitude
         int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());

         // Set the color on the magnitude circle
         magnitudeCircle.setColor(magnitudeColor);

        
        return listitemView;
    }

    private int getMagnitudeColor(double magnitude) {

        int magnitudeColorResourceId;
    int magnitudeFloor = (int) Math.floor(magnitude);
    switch (magnitudeFloor) {
        case 0:
        case 1:
            magnitudeColorResourceId = R.color.magnitude1;
            break;
        case 2:
            magnitudeColorResourceId = R.color.magnitude2;
            break;
        case 3:
            magnitudeColorResourceId = R.color.magnitude3;
            break;
        case 4:
            magnitudeColorResourceId = R.color.magnitude4;
            break;
        case 5:
            magnitudeColorResourceId = R.color.magnitude5;
            break;
        case 6:
            magnitudeColorResourceId = R.color.magnitude6;
            break;
        case 7:
            magnitudeColorResourceId = R.color.magnitude7;
            break;
        case 8:
            magnitudeColorResourceId = R.color.magnitude8;
            break;
        case 9:
            magnitudeColorResourceId = R.color.magnitude9;
            break;
        default:
            magnitudeColorResourceId = R.color.magnitude10plus;
            break;
    }
    return ContextCompat.getColor(getContext(), magnitudeColorResourceId);

    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat dateFormat=new SimpleDateFormat("hh:m a");
        return dateFormat.format(dateObject);
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat=new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

     private String formatMagnitude(double magnitude) {
     DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
     return magnitudeFormat.format(magnitude);
    }


}
