package com.tackmobile.specs;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class SpecsActivity extends Activity
{
	private ArrayList<Stat> osStats;
	private ArrayList<Stat> screenStats;
	private ArrayList<Stat> otherStats;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		loadStats();
	}

	public void share(View v)
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(getString(R.string.operating_system)).append("\n");
		for(Stat s : osStats) {
			sb.append(s.getName()).append(": ").append(s.getValue()).append("\n");
		}
		sb.append("\n").append(getString(R.string.screen)).append("\n");
		for(Stat s : screenStats) {
			sb.append(s.getName()).append(": ").append(s.getValue()).append("\n");
		}
		sb.append("\n").append(getString(R.string.other)).append("\n");
		for(Stat s : otherStats) {
			sb.append(s.getName()).append(": ").append(s.getValue()).append("\n");
		}
		sb.append("\n").append(getString(R.string.gathered_by_specs));
		
		String email = sb.toString();
		
		Intent i = new Intent(android.content.Intent.ACTION_SEND);
		
		i.setType("plain/text");
		// TODO: Allow this to be defined via a broadcast receiver request to run this application
		//i.putExtra(Intent.EXTRA_EMAIL, new String[]{"specs@tackmobile.com"});
		i.putExtra(Intent.EXTRA_TEXT, email);
		startActivity(Intent.createChooser(i, getString(R.string.send_device_specs)));
	}
	
	public void visitTackWebsite(View v)
	{
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse("http://www.tackmobile.com"));
		startActivity(i);
	}
	
	private class Stat
	{
		private TextView view;
		private String name;
		private String value;
		
		public Stat(int statName, String statValue, TextView textView)
		{
			view = textView;
			name = getString(statName);
			value = statValue;
		}
		
		public String getName()
		{
			return name;
		}
		
		public String getValue()
		{
			return value;
		}
		
		public void updateView()
		{
			view.setText(value);
		}
	}
	
	private void loadStats()
	{
		osStats = new ArrayList<Stat>();
		screenStats = new ArrayList<Stat>();
		otherStats = new ArrayList<Stat>();
		
		final Configuration config = getResources().getConfiguration();
		final DisplayMetrics dm = new DisplayMetrics();
		final String[] codenames = getResources().getStringArray(R.array.version_code);

		// operating system
		String sdkVer   = String.valueOf(Build.VERSION.SDK_INT);
		String release  = Build.VERSION.RELEASE;
		
		String codename;
		if(Build.VERSION.SDK_INT >= codenames.length) {
			codename = "Codename for " + Build.VERSION.RELEASE;
		} else {
			codename = codenames[Math.min(Build.VERSION.SDK_INT-1, codenames.length-1)];
		}
		String locale   = config.locale.getDisplayName();

		TextView sdkVerView   = (TextView) findViewById(R.id.sdk_ver_text);
		TextView releaseView  = (TextView) findViewById(R.id.release_text);
		TextView codenameView = (TextView) findViewById(R.id.codename_text);
		TextView localeView   = (TextView) findViewById(R.id.locale_text);
		
		osStats.add(new Stat(R.string.sdk_version, sdkVer,   sdkVerView));
		osStats.add(new Stat(R.string.release,     release,  releaseView));
		osStats.add(new Stat(R.string.codename,    codename, codenameView));
		osStats.add(new Stat(R.string.locale,      locale,   localeView));

		for(Stat s : osStats) {
			s.updateView();
		}
		
		// screen
		String size    = getSize(config);
		String dpiQual = getDpi(dm);
		String dpi     = String.valueOf(dm.densityDpi);
		String whPx    = getScreenSizePx(dm);
		String whDp    = getScreenSizeDp(dm);
		String dx      = String.valueOf(dm.density);

		TextView sizeView    = (TextView) findViewById(R.id.size_text);
		TextView dpiQualView = (TextView) findViewById(R.id.dpi_qual_text);
		TextView dpiView     = (TextView) findViewById(R.id.dpi_text);
		TextView whPxView    = (TextView) findViewById(R.id.width_height_px_text);
		TextView whDpView    = (TextView) findViewById(R.id.width_height_dp_text);
		TextView dxView      = (TextView) findViewById(R.id.dx_text);
		
		screenStats.add(new Stat(R.string.size,               size,    sizeView));
		screenStats.add(new Stat(R.string.resoultion,         dpiQual, dpiQualView));
		screenStats.add(new Stat(R.string.dpi,                dpi,     dpiView));
		screenStats.add(new Stat(R.string.width_height,       whPx,    whPxView));
		screenStats.add(new Stat(R.string.width_height,       whDp,    whDpView));
		screenStats.add(new Stat(R.string.density_multiplier, dx,      dxView));
		
		for(Stat s : screenStats) {
			s.updateView();
		}
		
		// other
		String fontScale = String.valueOf(config.fontScale);
		
		TextView fontScaleView = (TextView) findViewById(R.id.fontscale_text);
		
		otherStats.add(new Stat(R.string.font_scale, fontScale, fontScaleView));
		
		for(Stat s : otherStats) {
			s.updateView();
		}
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
    case Configuration.SCREENLAYOUT_SIZE_XLARGE:
      return getString(R.string.xlarge);
		default:
			return getString(R.string.undefined);
		}
	}

  private String getDpi(DisplayMetrics dm) {
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
      case DisplayMetrics.DENSITY_XXHIGH:
        return getString(R.string.xxhdpi);
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