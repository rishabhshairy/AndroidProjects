package com.example.rishabh.imageslider;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Rishabh on 10/16/2017.
 */

public class customSwip extends PagerAdapter {
    private int[] resources={R.drawable.capture1,
                            R.drawable.capture2,
                            R.drawable.capture3,
                            R.drawable.capture4,
                            R.drawable.capture5};
    int s1=R.raw.gets;

    private Context ctx;
    private LayoutInflater layoutInflater;

    public customSwip(Context c){
        ctx=c;
    }

    @Override
    public int getCount() {
        return resources.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView=layoutInflater.inflate(R.layout.custom_swip,container,false);
        ImageView imageView=(ImageView) itemView.findViewById(R.id.swip_image);
        imageView.setImageResource(resources[position]);
        container.addView(itemView);
        return itemView;

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}

