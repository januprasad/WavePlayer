package me.xiaok.waveplayer.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import me.xiaok.waveplayer.LibManager;
import me.xiaok.waveplayer.PlayerController;
import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.fragments.AlbumsFragment;
import me.xiaok.waveplayer.fragments.ArtistsFragment;
import me.xiaok.waveplayer.fragments.GenreFragment;
import me.xiaok.waveplayer.fragments.PlayListFragment;
import me.xiaok.waveplayer.fragments.SongsFragment;
import me.xiaok.waveplayer.utils.LogUtils;

/**
 * 主activity
 *
 * Created by GeeKaven on 15/8/16.
 */
public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeActivity";

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    public ViewPager pager;
    public CustomPagerAdapter mAdapter;
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu);
        }

        setupInstance();

        mToolBar.setTitle(R.string.nav_library);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_scan:
//                Navigate.to(this, NowPlayingMusic.class, NowPlayingMusic.EXTRA_NOW_PLAYING, info.song);
                refreshLibrary();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupInstance() {
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView)findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        pager = (ViewPager) findViewById(R.id.view_pager);
        mAdapter = new CustomPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(mAdapter);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        pager.setCurrentItem(1);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        switch (menuItem.getItemId()) {
            case R.id.nav_library:
                break;
            case R.id.nav_quit:
                PlayerController.stop();
                System.exit(0);
                break;
        }
        mDrawerLayout.closeDrawers();
        return true;
    }

    public class CustomPagerAdapter extends FragmentPagerAdapter {

        private Fragment mSongsFragment;
        private Fragment mArtistsFragment;
        private Fragment mAlbumsFragment;
        private Fragment mGenresFragment;
        private Fragment mPlayListsFragment;

        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    //播放列表
                    if (mPlayListsFragment == null) {
                        mPlayListsFragment = new PlayListFragment();
                    }
                    return mPlayListsFragment;
                case 1:
                    //歌曲
                    if (mSongsFragment == null) {
                        mSongsFragment = new SongsFragment();
                    }
                    return mSongsFragment;
                case 2:
                    //艺术家
                    if (mArtistsFragment == null) {
                        mArtistsFragment = new ArtistsFragment();
                    }
                    return mArtistsFragment;
                case 3:
                    //专辑
                    if (mAlbumsFragment == null) {
                        mAlbumsFragment = new AlbumsFragment();
                    }
                    return mAlbumsFragment;
                case 4:
                    //类型
                    if (mGenresFragment == null) {
                        mGenresFragment = new GenreFragment();
                    }
                    return mGenresFragment;
            }
            return new Fragment();
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.tab_playlist);
                case 1:
                    return getResources().getString(R.string.tab_music);
                case 2:
                    return getResources().getString(R.string.tab_artist);
                case 3:
                    return getResources().getString(R.string.tab_album);
                case 4:
                    return getResources().getString(R.string.tab_genres);
                default:
                    return "tab: " + position;
            }
        }

        public void clear() {
            mSongsFragment = null;
            mArtistsFragment = null;
            mAlbumsFragment = null;
            mGenresFragment = null;
            mPlayListsFragment = null;
        }
    }

    public void refreshLibrary() {

        String[] paths;
        File[] files;
        StorageManager sm = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
        // 获取sdcard的路径：外置和内置
        try {
            paths = (String[]) sm.getClass().getMethod("getVolumePaths", null).invoke(sm, null);
            files = new File[paths.length];
            for (int i = 0; i < paths.length; i++) {
                files[i] = new File(paths[i]);
            }
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                new ScanTask(this).execute(files);
            }else{
                Toast.makeText(this, "没有SD卡", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ScanTask extends AsyncTask<File, String, Integer> {

        private Context context;
        private ProgressDialog dialog;
        private int count = 0;

        public ScanTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("正在扫描....");
            dialog.show();
        }

        @Override
        protected Integer doInBackground(File... files) {
            for (File f : files) {
                getAllFiles(f, new String[] {".mp3"});
            }
            return count;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            dialog.setMessage(values[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            dialog.cancel();
            Toast.makeText(context, "音乐: " + integer, Toast.LENGTH_SHORT).show();
        }

        private void getAllFiles(File file, String[] ext) {
            if (file != null) {
                if (file.isDirectory()) {
                    File[] listFile = file.listFiles();
                    if (listFile != null) {
                        for (File f : listFile) {
                            getAllFiles(f, ext);
                        }
                    }
                } else {
                    String filename = file.getAbsolutePath();
                    publishProgress(String.format("...%s",filename.substring(filename.length() - 20)));
                    for (String s : ext) {
                        if (filename.endsWith(s)) {
                            MediaScannerConnection.scanFile(context,
                                    new String[]{ filename }, null,
                                    new MediaScannerConnection.OnScanCompletedListener() {
                                        public void onScanCompleted(String path, Uri uri) {
                                            Log.i("ExternalStorage", "Scanned " + path + ":");
                                            Log.i("ExternalStorage", "-> uri=" + uri);
                                        }
                                    });
                            count++;
                            break;
                        }
                    }
                }
            }
        }
    }
}
