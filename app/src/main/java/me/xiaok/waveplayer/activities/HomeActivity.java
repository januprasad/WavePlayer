package me.xiaok.waveplayer.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import me.xiaok.waveplayer.PlayerController;
import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.fragments.AlbumsFragment;
import me.xiaok.waveplayer.fragments.ArtistsFragment;
import me.xiaok.waveplayer.fragments.GenreFragment;
import me.xiaok.waveplayer.fragments.SongsFragment;

/**
 * 主activity
 *
 * Created by GeeKaven on 15/8/16.
 */
public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private CustomPagerAdapter mAdapter;
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupInstance() {
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView)findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
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
                finish();
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

        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    //播放列表
                    break;
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
    }
}
