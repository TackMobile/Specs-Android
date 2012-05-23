package com.tackmobile.devicespecs;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

public class DeviceSpecsActivity extends Activity
{
	private String stats = "";
	private int    sdkVer;
	private String release;
	private String locale;
	private String size;
	private String dpiQual;
	private String dpi;
	private String whPx;
	private String whDp;
	private String dx;
	private String fontScale;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		loadStats();
	}

	public void share(View v)
	{
		Intent i = new Intent(android.content.Intent.ACTION_SEND);
		
		i.setType("plain/text");
		i.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@tackmobile.com"});
		i.putExtra(Intent.EXTRA_TEXT, stats);
		
		startActivity(Intent.createChooser(i, "Send Device Stats"));
	}
	
	private void loadStats()
	{
		final Configuration config = getResources().getConfiguration();
		final DisplayMetrics dm = new DisplayMetrics();
		
		sdkVer    = Build.VERSION.SDK_INT;
		release   = Build.VERSION.RELEASE;
		locale    = config.locale.getDisplayName();
		size      = getSize(config);
		dpiQual   = getDpi(dm);
		dpi       = String.valueOf(dm.densityDpi);
		whPx      = getScreenSizePx(dm);
		whDp      = getScreenSizeDp(dm);
		dx        = String.valueOf(dm.density);
		fontScale = String.valueOf(config.fontScale);
		
		stats = "SDK Version: "        + sdkVer    + "\n" +
				"Release: "            + release   + "\n" +
				"Locale: "             + locale    + "\n" +
				"Size: "               + size      + "\n" +
				"Resolution: "         + dpiQual   + "\n" +
				"DPI: "                + dpi       + "\n" +
				"Screen Size (px): "   + whPx      + "\n" +
				"Screen Size (dp): "   + whDp      + "\n" +
				"Density Multiplier: " + dx        + "\n" +
				"Font Scale: "         + fontScale;
		
		displayStats();
	}
	
	private void displayStats()
	{
		TextView sdkVerText = (TextView) findViewById(R.id.sdk_ver_text);
		TextView releaseText = (TextView) findViewById(R.id.release_text);
		TextView localeText = (TextView) findViewById(R.id.locale_text);
		TextView sizeText = (TextView) findViewById(R.id.size_text);
		TextView dpiQualText = (TextView) findViewById(R.id.dpi_qual_text);
		TextView dpiText = (TextView) findViewById(R.id.dpi_text);
		TextView whPxText = (TextView) findViewById(R.id.width_height_px_text);
		TextView whDpText = (TextView) findViewById(R.id.width_height_dp_text);
		TextView dxText = (TextView) findViewById(R.id.dx_text);
		TextView fontScaleText = (TextView) findViewById(R.id.fontscale_text);
		
		sdkVerText.setText(String.valueOf(sdkVer));
		releaseText.setText(release);
		localeText.setText(locale);
		sizeText.setText(size);
		dpiQualText.setText(dpiQual);
		dpiText.setText(dpi);
		whPxText.setText(whPx);
		whDpText.setText(whDp);
		dxText.setText(dx);		
		fontScaleText.setText(fontScale);
	}
	
	private String getSize(Configuration config)
	{
		int screenSize = config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
		
		switch (screenSize) {
		case Configuration.SCREENLAYOUT_SIZE_SMALL:
			return getString(R.string.small);
		case Configuration.SCREENLAYOUT_SIZE_NORMAL:
			return getString(R.string.normal);
		case Configuration.SCREENLAYOUT_SIZE_LARGE:
			return getString(R.string.large);
		default:
			return getString(R.string.undefined);
		}
	}
	
	private String getDpi(DisplayMetrics dm)
	{
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		switch (dm.densityDpi) {
		case DisplayMetrics.DENSITY_LOW:
			return getString(R.string.ldpi);
		case DisplayMetrics.DENSITY_MEDIUM:
			return getString(R.string.mdpi);
		case DisplayMetrics.DENSITY_HIGH:
			return getString(R.string.hdpi);
		case DisplayMetrics.DENSITY_XHIGH:
			return getString(R.string.xhdpi);
		case DisplayMetrics.DENSITY_TV:
			return getString(R.string.tvdpi);
		default:
			return getString(R.string.unknown_dpi);
		}
	}
	
	private String getScreenSizePx(DisplayMetrics dm)
	{
		return dm.widthPixels + "px by " + dm.heightPixels + "px";
	}
	
	private String getScreenSizeDp(DisplayMetrics dm)
	{
		return ((int) (dm.widthPixels / dm.density)) + "dp by " + 
			   ((int) (dm.heightPixels / dm.density)) + "dp";
	}
}