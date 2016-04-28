package com.tackmobile.specs.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.tackmobile.specs.R;

/**
 * Created by anato on 4/25/2016.
 */
public class ScreenUtils {

    private Activity mActivity;

    public ScreenUtils(Activity activity) {
        mActivity = activity;
    }

    public String getSize(Configuration config) {
        int screenSize = config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                return mActivity.getString(R.string.small);
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return mActivity.getString(R.string.normal);
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                return mActivity.getString(R.string.large);
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                return mActivity.getString(R.string.xlarge);
            case Configuration.SCREENLAYOUT_SIZE_UNDEFINED:
            default:
                return mActivity.getString(R.string.undefined);
        }
    }

    public String getDpi(DisplayMetrics dm) {
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        switch (dm.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                return mActivity.getString(R.string.density_ldpi);
            case DisplayMetrics.DENSITY_MEDIUM:
                return mActivity.getString(R.string.density_mdpi);
            case DisplayMetrics.DENSITY_HIGH:
                return mActivity.getString(R.string.density_hdpi);
            case DisplayMetrics.DENSITY_280:
                return mActivity.getString(R.string.density_280dpi);
            case DisplayMetrics.DENSITY_XHIGH:
                return mActivity.getString(R.string.density_xhdpi);
            case DisplayMetrics.DENSITY_400:
                return mActivity.getString(R.string.density_400dpi);
            case DisplayMetrics.DENSITY_XXHIGH:
                return mActivity.getString(R.string.density_xxhdpi);
            case DisplayMetrics.DENSITY_560: // between xx and xxx
                return mActivity.getString(R.string.density_560dpi);
            case DisplayMetrics.DENSITY_XXXHIGH:
                return mActivity.getString(R.string.density_xxxhdpi);
            case DisplayMetrics.DENSITY_TV:
                return mActivity.getString(R.string.tvdpi);
            default:
                return mActivity.getString(R.string.unknown_dpi);
        }
    }

    public String getScreenSizePx(DisplayMetrics dm) {
        return dm.widthPixels + "px by " + dm.heightPixels + "px";
    }

    public String getScreenSizeDp(DisplayMetrics dm) {
        return ((int) (dm.widthPixels / dm.density)) + "dp by " +
                ((int) (dm.heightPixels / dm.density)) + "dp";
    }

    public String getOrientation() {
        int orient = mActivity.getResources().getConfiguration().orientation;
        if (orient == 1) {
            return "Portrait";
        } else if (orient == 2) {
            return "Landscape";
        }
        return null;
    }

    public String getRefreshRate() {
        Display display = ((WindowManager) mActivity
                .getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        float refreshRating = display.getRefreshRate();
        return String.valueOf(refreshRating) + " Hz";
    }

}
