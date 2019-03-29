package com.link.sergio.myapplication;

import android.content.Context;
import android.net.sip.SipSession;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ContentManager.DataListener, SwipeRefreshLayout.OnRefreshListener
{
    private static Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext= getApplicationContext();
        ContentManager.getInstance(this).getRequestData(this); //AsyncTask
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        if(!Utils.isNetworkAvailable(this)){
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "Mode hors-ligne activé", duration);
            toast.show();
        }
    }

    @Override
    public void onRefresh() {
        //ContentManager.getInstance(this).getRequestData(this); //AsyncTask
        if(!Utils.isNetworkAvailable(this)){
            mSwipeRefreshLayout.setRefreshing(false);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "Impossible de rafraichir", duration);
            toast.show();
        }else{
            ContentManager.getInstance(this).getRequestData(this);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "Page mise à jour!", duration);
            toast.show();
            mSwipeRefreshLayout.setRefreshing(false);
        }

    }


    @Override
    public void notifyRetrieved(List<SavoirItem> savoirItem)
    {
        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(savoirItem,this);

        ViewPager mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(mCustomPagerAdapter);

    }

    @Override
    public void notifyNotRetrieved()
    {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, "Erreur de chargement", duration);
        toast.show();

    }

    public static Context getContext(){
        return mContext;
    }

}
