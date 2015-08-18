package me.xiaok.waveplayer.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.fragments.AlbumsFragment;
import me.xiaok.waveplayer.fragments.ArtistsFragment;
import me.xiaok.waveplayer.fragments.SongsFragment;

/**
 * 主activity
 *
 * Created by GeeKaven on 15/8/16.
 */
public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    //当前的Fragment
    private Fragment mCurrentFragment;
    private View mMiniPlayer;
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
        mMiniPlayer = findViewById(R.id.mini_player);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        switch (menuItem.getItemId()) {
            case R.id.nav_song:
                mCurrentFragment = new SongsFragment();
                break;
            case R.id.nav_artist:
                mCurrentFragment = new ArtistsFragment();
                break;
            case R.id.nav_album:
                mCurrentFragment = new AlbumsFragment();
                break;
            case R.id.nav_quit:
                finish();
                break;
        }
        transactionTo(mCurrentFragment);
        mDrawerLayout.closeDrawers();
        return true;
    }

    /**
     * 跳转Fragment
     * @param fragment
     */
    private void transactionTo(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
}
