package me.xiaok.waveplayer.activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import me.xiaok.waveplayer.R;

/**
 * Created by GeeKaven on 15/9/8.
 */
public class SettingActivity extends BaseActivity {
  @Override protected int getLayoutResource() {
    return R.layout.activity_setting;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mToolBar.setTitle("设置");
    getFragmentManager().beginTransaction().replace(R.id.container, new PrefsFragment()).commit();
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    finish();
  }

  @Override public void onBackPressed() {
    super.onBackPressed();
    finish();
  }

  public static class PrefsFragment extends PreferenceFragment {
    @Override public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.prefs);
    }
  }
}
