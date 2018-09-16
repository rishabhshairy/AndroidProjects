package com.example.rishabh.adtest;

import android.content.Context;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;

import com.google.android.gms.ads.AdListener;

/**
 * Created by Rishabh on 2/7/2018.
 */

public class ToastAdListener extends AdListener {
    private Context mContext;
    private String mErrorReason;

    public ToastAdListener(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onAdOpened() {
        Toast.makeText(mContext, "Ad Opened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdClosed() {
        Toast.makeText(mContext, "Ad Closed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdLoaded() {
        Toast.makeText(mContext, "Ad Loaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdClicked() {
        super.onAdClicked();
    }

    @Override
    public void onAdFailedToLoad(int errorCode) {
        mErrorReason="";
        switch (errorCode)
        {
            case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                mErrorReason="INTERNAL ERROR OCCURED";
                break;
            case AdRequest.ERROR_CODE_INVALID_REQUEST:
                mErrorReason="INVALID REQUEST";
                break;
            case AdRequest.ERROR_CODE_NETWORK_ERROR:
                mErrorReason="NETWORK ERROR";
                break;
            case AdRequest.ERROR_CODE_NO_FILL:
                mErrorReason="NO FILL";
                break;

        }
        Toast.makeText(mContext,String.format("onAdLoadFailed(%s)",mErrorReason), Toast.LENGTH_SHORT).show();
    }

    public String getmErrorReason() {
        return mErrorReason==null?"":mErrorReason;
    }
}
