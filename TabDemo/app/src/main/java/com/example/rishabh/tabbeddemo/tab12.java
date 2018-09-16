package com.example.rishabh.tabbeddemo;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Rishabh on 1/27/2018.
 */

public class tab12 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View myView=inflater.inflate(R.layout.tab2_frag,container,false);
        return myView;
    }
}
