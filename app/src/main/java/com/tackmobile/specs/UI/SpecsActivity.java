package com.tackmobile.specs.UI;

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
import com.tackmobile.specs.R;
import com.tackmobile.specs.Stat;
import com.tackmobile.specs.Utils.DeviceUtils;
import com.tackmobile.specs.Utils.ScreenUtils;
import com.tackmobile.specs.Utils.StorageUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SpecsActivity extends AppCompatActivity {
  private ArrayList<Stat> deviceStats;
  private ArrayList<Stat> osStats;
  private ArrayList<Stat> storageStats;
  private ArrayList<Stat> screenStats;
  private ArrayList<Stat> otherStats;

  private BottomBar mBottomBar;

  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.toolbarTitle) TextView mToolbarTitle;
  @Bind(R.id.device_layout) TableLayout mDeviceLayout;
  @Bind(R.id.os_layout) TableLayout mOsLayout;
  @Bind(R.id.storage_layout) TableLayout mStorageLayout;
  @Bind(R.id.screen_layout) TableLayout mScreenLayout;
  @Bind(R.id.other_layout) TableLayout mOtherLayout;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    ButterKnife.bind(this);
    mToolbarTitle.setText(DeviceUtils.getDeviceName());
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
          mStorageLayout.setVisibility(View.VISIBLE);
          mScreenLayout.setVisibility(View.GONE);
          mOtherLayout.setVisibility(View.GONE);
        } else if (menuItemId == R.id.bottomBarItemTwo) {
          mDeviceLayout.setVisibility(View.GONE);
          mOsLayout.setVisibility(View.GONE);
          mStorageLayout.setVisibility(View.GONE);
          mScreenLayout.setVisibility(View.VISIBLE);
          mOtherLayout.setVisibility(View.GONE);
        } else if (menuItemId == R.id.bottomBarItemThree) {
          mDeviceLayout.setVisibility(View.GONE);
          mOsLayout.setVisibility(View.GONE);
          mStorageLayout.setVisibility(View.GONE);
          mScreenLayout.setVisibility(View.GONE);
          mOtherLayout.setVisibility(View.VISIBLE);
        }
      }

      @Override
      public void onMenuTabReSelected(@IdRes int menuItemId) {

      }
    });
  }

  public void share() {
    final StringBuilder sb = new StringBuilder();
    sb.append("\n").append(getString(R.string.device)).append("\n");
    for (Stat s : deviceStats) {
      sb.append(s.getName()).append(": ").append(s.getValue()).append("\n");
    }
    sb.append("\n").append(getString(R.string.operating_system)).append("\n");
    for (Stat s : osStats) {
      sb.append(s.getName()).append(": ").append(s.getValue()).append("\n");
    }
    sb.append("\n").append(getString(R.string.storage)).append("\n");
    for (Stat s : storageStats) {
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

  private void loadStats() {
    deviceStats = new ArrayList<>();
    osStats = new ArrayList<>();
    storageStats = new ArrayList<>();
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
    String manufacturer = DeviceUtils.capitalize(Build.MANUFACTURER);
    String model = Build.MODEL;

    TextView makeView = (TextView) findViewById(R.id.device_make);
    TextView modelView = (TextView) findViewById(R.id.device_model);

    deviceStats.add(new Stat(this, R.string.make, manufacturer, makeView));
    deviceStats.add(new Stat(this, R.string.model, model, modelView));

    for (Stat s : deviceStats) {
      s.updateView();
    }

    TextView sdkVerView = (TextView) findViewById(R.id.sdk_ver_text);
    TextView releaseView = (TextView) findViewById(R.id.release_text);
    TextView codenameView = (TextView) findViewById(R.id.codename_text);
    TextView localeView = (TextView) findViewById(R.id.locale_text);

    osStats.add(new Stat(this, R.string.sdk_version, sdkVer, sdkVerView));
    osStats.add(new Stat(this, R.string.release, release, releaseView));
    osStats.add(new Stat(this, R.string.codename, codename, codenameView));
    osStats.add(new Stat(this, R.string.locale, locale, localeView));

    for (Stat s : osStats) {
      s.updateView();
    }

    //Storage
    TextView totalStorageView = (TextView) findViewById(R.id.total_storage);
    TextView remainingStorageView = (TextView) findViewById(R.id.remainingStorage);

    storageStats.add(new Stat(this, R.string.totalStorage, StorageUtils.getTotalInternalMemorySize(), totalStorageView));
    storageStats.add(new Stat(this, R.string.usedStorage, StorageUtils.getAvailableInternalMemorySize(), remainingStorageView));

    for (Stat s : storageStats) {
      s.updateView();
    }

    // screen
    ScreenUtils screenUtils = new ScreenUtils(this);

    String size = screenUtils.getSize(config);
    String res = screenUtils.getDpi(dm);
    String dpi = String.valueOf(dm.densityDpi);
    String whPx = screenUtils.getScreenSizePx(dm);
    String whDp = screenUtils.getScreenSizeDp(dm);
    String dx = String.valueOf(dm.density);
    String orientation = screenUtils.getOrientation();
    String refresh = screenUtils.getRefreshRate();

    TextView orientView = (TextView) findViewById(R.id.orientation_text);
    TextView refreshView = (TextView) findViewById(R.id.refresh_rate);
    TextView sizeView = (TextView) findViewById(R.id.size_text);
    TextView resView = (TextView) findViewById(R.id.res_text);
    TextView dpiView = (TextView) findViewById(R.id.dpi_text);
    TextView whPxView = (TextView) findViewById(R.id.width_height_px_text);
    TextView whDpView = (TextView) findViewById(R.id.width_height_dp_text);
    TextView dxView = (TextView) findViewById(R.id.dx_text);

    screenStats.add(new Stat(this, R.string.orientation, orientation, orientView));
    screenStats.add(new Stat(this, R.string.refresh_rate, refresh, refreshView));
    screenStats.add(new Stat(this, R.string.size, size, sizeView));
    screenStats.add(new Stat(this, R.string.resolution, res, resView));
    screenStats.add(new Stat(this, R.string.dpi, dpi, dpiView));
    screenStats.add(new Stat(this, R.string.width_height, whPx, whPxView));
    screenStats.add(new Stat(this, R.string.width_height, whDp, whDpView));
    screenStats.add(new Stat(this, R.string.density_multiplier, dx, dxView));

    for (Stat s : screenStats) {
      s.updateView();
    }

    // other
    String fontScale = String.valueOf(config.fontScale);

    TextView fontScaleView = (TextView) findViewById(R.id.fontscale_text);

    otherStats.add(new Stat(this, R.string.font_scale, fontScale, fontScaleView));

    for (Stat s : otherStats) {
      s.updateView();
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