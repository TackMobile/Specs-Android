package com.tackmobile.specs;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SpecsActivity extends AppCompatActivity {
  private ArrayList<Stat> deviceStats;
  private ArrayList<Stat> osStats;
  private ArrayList<Stat> screenStats;
  private ArrayList<Stat> otherStats;

  private BottomBar mBottomBar;

  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.toolbarTitle) TextView mToolbarTitle;
  @Bind(R.id.device_layout) TableLayout mDeviceLayout;
  @Bind(R.id.os_layout) TableLayout mOsLayout;
  @Bind(R.id.screen_layout) TableLayout mScreenLayout;
  @Bind(R.id.other_layout) TableLayout mOtherLayout;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    ButterKnife.bind(this);
    mToolbarTitle.setText(getDeviceName());
    setSupportActionBar(mToolbar);
    getSupportActionBar().setTitle("");
    createBottomBar(savedInstanceState);
    loadStats();
  }

  private void createBottomBar(Bundle savedInstanceState) {
    mBottomBar = BottomBar.attach(this, savedInstanceState);
    mBottomBar.noTabletGoodness();
    mBottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
      @Override
      public void onMenuTabSelected(@IdRes int menuItemId) {
        if (menuItemId == R.id.bottomBarItemOne) {
          mDeviceLayout.setVisibility(View.VISIBLE);
          mOsLayout.setVisibility(View.VISIBLE);
          mScreenLayout.setVisibility(View.GONE);
          mOtherLayout.setVisibility(View.GONE);
        } else if (menuItemId == R.id.bottomBarItemTwo) {
          mDeviceLayout.setVisibility(View.GONE);
          mOsLayout.setVisibility(View.GONE);
          mScreenLayout.setVisibility(View.VISIBLE);
          mOtherLayout.setVisibility(View.GONE);
        } else if (menuItemId == R.id.bottomBarItemThree) {
          mDeviceLayout.setVisibility(View.GONE);
          mOsLayout.setVisibility(View.GONE);
          mScreenLayout.setVisibility(View.GONE);
          mOtherLayout.setVisibility(View.VISIBLE);
        }
      }

      @Override
      public void onMenuTabReSelected(@IdRes int menuItemId) {
        if (menuItemId == R.id.bottomBarItemOne) {
          // The user reselected item number one, scroll your content to top.
        }
      }
    });
  }

  public void share() {
    final StringBuilder sb = new StringBuilder();
    sb.append(getString(R.string.operating_system)).append("\n");
    for (Stat s : osStats) {
      sb.append(s.getName()).append(": ").append(s.getValue()).append("\n");
    }
    sb.append("\n").append(getString(R.string.screen)).append("\n");
    for (Stat s : screenStats) {
      sb.append(s.getName()).append(": ").append(s.getValue()).append("\n");
    }
    sb.append("\n").append(getString(R.string.other)).append("\n");
    for (Stat s : otherStats) {
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

  public void visitTackWebsite(View v) {
    Intent i = new Intent(Intent.ACTION_VIEW);
    i.setData(Uri.parse("http://www.tackmobile.com"));
    startActivity(i);
  }

  private class Stat {
    private TextView view;
    private String name;
    private String value;

    public Stat(int statName, String statValue, TextView textView) {
      view = textView;
      name = getString(statName);
      value = statValue;
    }

    public String getName() {
      return name;
    }

    public String getValue() {
      return value;
    }

    public void updateView() {
      view.setText(value);
    }
  }

  private void loadStats() {
    deviceStats = new ArrayList<>();
    osStats = new ArrayList<>();
    screenStats = new ArrayList<>();
    otherStats = new ArrayList<>();

    final Configuration config = getResources().getConfiguration();
    final DisplayMetrics dm = new DisplayMetrics();
    final String[] codenames = getResources().getStringArray(R.array.version_code);

    // operating system
    String sdkVer = String.valueOf(Build.VERSION.SDK_INT);
    String release = Build.VERSION.RELEASE;

    String codename;
    if (Build.VERSION.SDK_INT > codenames.length) {
      codename = "Codename for " + Build.VERSION.RELEASE;
    } else {
      codename = codenames[Math.min(Build.VERSION.SDK_INT - 1, codenames.length - 1)];
    }
    String locale = config.locale.getDisplayName();
    String manufacturer = Build.MANUFACTURER;
    String model = Build.MODEL;

    TextView makeView = (TextView) findViewById(R.id.device_make);
    TextView modelView = (TextView) findViewById(R.id.device_model);

    deviceStats.add(new Stat(R.string.make, manufacturer, makeView));
    deviceStats.add(new Stat(R.string.model, model, modelView));

    for (Stat s : deviceStats) {
      s.updateView();
    }

    TextView sdkVerView = (TextView) findViewById(R.id.sdk_ver_text);
    TextView releaseView = (TextView) findViewById(R.id.release_text);
    TextView codenameView = (TextView) findViewById(R.id.codename_text);
    TextView localeView = (TextView) findViewById(R.id.locale_text);

    osStats.add(new Stat(R.string.sdk_version, sdkVer, sdkVerView));
    osStats.add(new Stat(R.string.release, release, releaseView));
    osStats.add(new Stat(R.string.codename, codename, codenameView));
    osStats.add(new Stat(R.string.locale, locale, localeView));

    for (Stat s : osStats) {
      s.updateView();
    }

    // screen
    String size = getSize(config);
    String res = getDpi(dm);
    String dpi = String.valueOf(dm.densityDpi);
    String whPx = getScreenSizePx(dm);
    String whDp = getScreenSizeDp(dm);
    String dx = String.valueOf(dm.density);

    TextView sizeView = (TextView) findViewById(R.id.size_text);
    TextView resView = (TextView) findViewById(R.id.res_text);
    TextView dpiView = (TextView) findViewById(R.id.dpi_text);
    TextView whPxView = (TextView) findViewById(R.id.width_height_px_text);
    TextView whDpView = (TextView) findViewById(R.id.width_height_dp_text);
    TextView dxView = (TextView) findViewById(R.id.dx_text);

    screenStats.add(new Stat(R.string.size, size, sizeView));
    screenStats.add(new Stat(R.string.resolution, res, resView));
    screenStats.add(new Stat(R.string.dpi, dpi, dpiView));
    screenStats.add(new Stat(R.string.width_height, whPx, whPxView));
    screenStats.add(new Stat(R.string.width_height, whDp, whDpView));
    screenStats.add(new Stat(R.string.density_multiplier, dx, dxView));

    for (Stat s : screenStats) {
      s.updateView();
    }

    // other
    String fontScale = String.valueOf(config.fontScale);

    TextView fontScaleView = (TextView) findViewById(R.id.fontscale_text);

    otherStats.add(new Stat(R.string.font_scale, fontScale, fontScaleView));

    for (Stat s : otherStats) {
      s.updateView();
    }
  }

  private String getSize(Configuration config) {
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
      case Configuration.SCREENLAYOUT_SIZE_UNDEFINED:
      default:
        return getString(R.string.undefined);
    }
  }

  private String getDpi(DisplayMetrics dm) {
    getWindowManager().getDefaultDisplay().getMetrics(dm);

    switch (dm.densityDpi) {
      case DisplayMetrics.DENSITY_LOW:
        return getString(R.string.density_ldpi);
      case DisplayMetrics.DENSITY_MEDIUM:
        return getString(R.string.density_mdpi);
      case DisplayMetrics.DENSITY_HIGH:
        return getString(R.string.density_hdpi);
      case DisplayMetrics.DENSITY_280:
        return getString(R.string.density_280dpi);
      case DisplayMetrics.DENSITY_XHIGH:
        return getString(R.string.density_xhdpi);
      case DisplayMetrics.DENSITY_400:
        return getString(R.string.density_400dpi);
      case DisplayMetrics.DENSITY_XXHIGH:
        return getString(R.string.density_xxhdpi);
      case DisplayMetrics.DENSITY_560: // between xx and xxx
        return getString(R.string.density_560dpi);
      case DisplayMetrics.DENSITY_XXXHIGH:
        return getString(R.string.density_xxxhdpi);
      case DisplayMetrics.DENSITY_TV:
        return getString(R.string.tvdpi);
      default:
        return getString(R.string.unknown_dpi);
    }
  }

  private String getScreenSizePx(DisplayMetrics dm) {
    return dm.widthPixels + "px by " + dm.heightPixels + "px";
  }

  private String getScreenSizeDp(DisplayMetrics dm) {
    return ((int) (dm.widthPixels / dm.density)) + "dp by " +
        ((int) (dm.heightPixels / dm.density)) + "dp";
  }

  public String getDeviceName() {
    String manufacturer = Build.MANUFACTURER;
    String model = Build.MODEL;
    if (model.startsWith(manufacturer)) {
      return capitalize(model);
    } else {
      return capitalize(manufacturer) + " " + model;
    }
  }


  private String capitalize(String s) {
    if (s == null || s.length() == 0) {
      return "";
    }
    char first = s.charAt(0);
    if (Character.isUpperCase(first)) {
      return s;
    } else {
      return Character.toUpperCase(first) + s.substring(1);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    share();
    return super.onOptionsItemSelected(item);
  }
}