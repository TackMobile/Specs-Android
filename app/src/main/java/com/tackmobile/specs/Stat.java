package com.tackmobile.specs;

import android.content.Context;
import android.widget.TextView;

/**
 * Created by anato on 4/25/2016.
 */
public class Stat {

    private Context mContext;
    private TextView mView;
    private String mName;
    private String mValue;

    public Stat(Context context, int statName, String statValue, TextView textView) {
        mView = textView;
        mName = context.getString(statName);
        mValue = statValue;
        mContext = context;
    }

    public String getName() {
        return mName;
    }

    public String getValue() {
        return mValue;
    }

    public void updateView() {
        mView.setText(mValue);
    }

}
