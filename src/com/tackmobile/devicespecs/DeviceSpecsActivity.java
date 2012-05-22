package com.tackmobile.devicespecs;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class DeviceSpecsActivity extends Activity implements OnClickListener {

	private String stats = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		buildStats();
		findViewById(R.id.share_btn).setOnClickListener(this);
	}

	public void buildStats() {
		final StringBuilder sb = new StringBuilder("");
		TextView sdkVerText = (TextView) findViewById(R.id.sdk_ver_text);
		TextView releaseText = (TextView) findViewById(R.id.release_text);
		TextView dpiQualText = (TextView) findViewById(R.id.dpi_qual_text);
		TextView sizeText = (TextView) findViewById(R.id.size_text);
		TextView dxText = (TextView) findViewById(R.id.dx_text);
		TextView dpiText = (TextView) findViewById(R.id.dpi_text);
		TextView whPxText = (TextView) findViewById(R.id.width_height_px_text);
		TextView whDpText = (TextView) findViewById(R.id.width_height_dp_text);
		TextView localeText = (TextView) findViewById(R.id.locale_text);
		TextView fontScaleText = (TextView) findViewById(R.id.fontscale_text);
		
		final int sdkInt = Build.VERSION.SDK_INT;
		sb.append("sdk version:").append("\t").append(sdkInt);
		sdkVerText.setText(String.valueOf(sdkInt));
		
		final String release = Build.VERSION.RELEASE;
		sb.append("sdk version:").append("\t").append(release);
		releaseText.setText(release);

		final DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		sb.append("densityDpi:").append("\t").append(dm.densityDpi);
		sb.append("density:").append("\t").append(dm.density);

		switch (dm.densityDpi) {
			case DisplayMetrics.DENSITY_LOW:
				dpiQualText.setText(R.string.ldpi);
				break;
			case DisplayMetrics.DENSITY_MEDIUM:
				dpiQualText.setText(R.string.mdpi);
				break;
			case DisplayMetrics.DENSITY_HIGH:
				dpiQualText.setText(R.string.hdpi);
				break;
			case DisplayMetrics.DENSITY_XHIGH:
				dpiQualText.setText(R.string.xhdpi);
				break;
			case DisplayMetrics.DENSITY_TV:
				dpiQualText.setText(R.string.tvdpi);
				break;
			default:
				dpiQualText.setText(R.string.unknown_dpi);
				break;
		}

		dxText.setText(String.valueOf(dm.density));
		dpiText.setText(String.valueOf(dm.densityDpi));

		final Configuration config = getResources().getConfiguration();
		int screenSize = config.screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;
		switch (screenSize) {
			case Configuration.SCREENLAYOUT_SIZE_SMALL:
				sizeText.setText(R.string.small);
				break;
			case Configuration.SCREENLAYOUT_SIZE_NORMAL:
				sizeText.setText(R.string.normal);
				break;
			case Configuration.SCREENLAYOUT_SIZE_LARGE:
				sizeText.setText(R.string.large);
				break;
			case Configuration.SCREENLAYOUT_SIZE_UNDEFINED:
				sizeText.setText(R.string.undefined);
				break;
		}
		
		int screenWidthPx, screenHeightPx;
		screenWidthPx = screenHeightPx = 0;
		int screenWidthDp, screenHeightDp;
		screenWidthDp = screenHeightDp = 0;
		
		if (sdkInt >= 13) {
			screenWidthPx = dm.widthPixels;
			screenHeightPx = dm.heightPixels;
			
			screenWidthDp = config.screenWidthDp;
			// missing 25dp?
			screenHeightDp = config.screenHeightDp;
		} else {
			screenWidthPx = dm.widthPixels;
			screenHeightPx = dm.heightPixels;

			screenWidthDp = (int) (screenWidthPx / dm.density);
			screenHeightDp = (int) (screenHeightPx / dm.density);
		}
		whPxText.setText(screenWidthPx+"px by "+screenHeightPx+"px");
		whDpText.setText(screenWidthDp+"dp by "+screenHeightDp+"dp");
		
		final Locale locale = config.locale;
		localeText.setText(locale.getDisplayName());
		
		final float fontScale = config.fontScale;
		fontScaleText.setText(String.valueOf(fontScale));
		
		stats = sb.toString();
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent(Intent.ACTION_SENDTO);
		i.putExtra(Intent.EXTRA_EMAIL, "support@tackmobile.com");
		i.putExtra(Intent.EXTRA_TEXT, stats);
		Intent choosenIntent = Intent.createChooser(i, "Send Device Stats");
		startActivity(choosenIntent);
	}
}