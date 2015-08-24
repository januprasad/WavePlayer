package me.xiaok.waveplayer.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import me.xiaok.waveplayer.LibManager;
import me.xiaok.waveplayer.Player;
import me.xiaok.waveplayer.R;

/**
 * Created by GeeKaven on 15/8/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected AppBarLayout mAppBar;
    protected Toolbar mToolBar;
    //正在播放音乐的信息
    public static Player.Info info;

    public void toolBarClick() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (LibManager.isEmpty()) {
            LibManager.scanAll(this);
        }

        setContentView(getLayoutResource());

        mAppBar = (AppBarLayout)findViewById(R.id.app_bar_layout);
        mToolBar = (Toolbar)findViewById(R.id.toolbar);

        if (mToolBar != null && mAppBar != null) {
            setSupportActionBar(mToolBar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }

            mToolBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toolBarClick();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(Player.SONG_CHANGE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    abstract protected int getLayoutResource();

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Player.SONG_CHANGE)) {
                info = intent.getExtras().getParcelable(Player.INFO);
                update(intent);
            }

        }
    };

    public void update(Intent intent) {}
}
