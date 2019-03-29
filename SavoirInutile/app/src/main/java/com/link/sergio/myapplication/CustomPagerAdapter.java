package com.link.sergio.myapplication;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by camillemac on 27/03/2019.
 */

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<SavoirItem>  si;


    public CustomPagerAdapter(List<SavoirItem> si, Context mContext) {
        this.mContext= mContext;
        this.si=si;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public Object instantiateItem(ViewGroup collection, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.messages, collection, false);

        collection.addView(itemView);

        TextView title = itemView.findViewById(R.id.view_title);
        TextView date = itemView.findViewById(R.id.view_date);
        TextView description = itemView.findViewById(R.id.view_description);

        title.setText(si.get(position).getTitle());
        date.setText(si.get(position).getDate());
        description.setText(si.get(position).getDescription());

        return itemView;

    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public int getCount() {
        return si.size(); // number of maximum views in View Pager.
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1; // return true if both are equal.
    }
}
