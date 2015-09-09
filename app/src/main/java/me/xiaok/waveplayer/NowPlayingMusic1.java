package me.xiaok.waveplayer;

import android.os.Bundle;

import me.xiaok.waveplayer.activities.BaseActivity;

/**
 * Created by GeeKaven on 15/9/9.
 */
public class NowPlayingMusic1 extends BaseActivity {
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_now;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mToolBar != null) {
            setSupportActionBar(mToolBar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
        }
    }
}
