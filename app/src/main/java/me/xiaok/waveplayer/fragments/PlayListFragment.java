package me.xiaok.waveplayer.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import me.xiaok.waveplayer.LibManager;
import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.adapters.PlayListAdapter;
import me.xiaok.waveplayer.models.PlayList;
import me.xiaok.waveplayer.utils.LogUtils;

/**
 * Created by GeeKaven on 15/8/24.
 */
public class PlayListFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "PlayListFragment";
    private RecyclerView mList;
    private PlayListAdapter mAdapter;
    private FrameLayout mRoot;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtils.v(TAG, "PlayListFragment onCreateView is called");
        View view = inflater.inflate(R.layout.com_list, container, false);

        mList = (RecyclerView) view.findViewById(R.id.list);
        mRoot = (FrameLayout) view.findViewById(R.id.root);

        addFloatingActionButton();

        mAdapter = new PlayListAdapter(LibManager.getPlayLists());
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        mList.setLayoutManager(layoutManager);
        mList.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.v(TAG, "PlayListFragment onResume is called");
    }

    private void addFloatingActionButton() {
        FloatingActionButton fab = new FloatingActionButton(getActivity());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.END | Gravity.BOTTOM;

        int navigationBarHeight = 0;
        Resources resources = this.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            navigationBarHeight = resources.getDimensionPixelSize(resourceId); //获取高度
        }

        layoutParams.setMargins(0, 0, 20, navigationBarHeight + 30);
        fab.setLayoutParams(layoutParams);
        fab.setImageResource(R.mipmap.ic_playlist_add_white_48dp);
        fab.setOnClickListener(this);

        mRoot.addView(fab);
    }

    @Override
    public void onClick(View view) {
        createPlaylist(view);
    }

    private void createPlaylist(final View view) {
        final TextInputLayout layout = new TextInputLayout(getActivity());
        final AppCompatEditText input = new AppCompatEditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("请输入歌单名");
        layout.addView(input);
        layout.setPadding(10,10,10,10);
        layout.setErrorEnabled(false);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("添加歌单")
                .setView(layout)
                .setPositiveButton(view.getContext().getString(R.string.action_create), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new Task(input.getText().toString()).execute(view);
                    }
                })
                .setNegativeButton(view.getContext().getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .show();

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getActivity().getResources().getColor(R.color.accent));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String error = LibManager.checkPlaylistName(getActivity(), charSequence.toString());
                if(error != null) {
                    layout.setError(error);
                    layout.setErrorEnabled(true);
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    layout.setErrorEnabled(false);
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public class Task extends AsyncTask<View, Void, PlayList> {

        private String playListName;

        public Task(String text) {
            this.playListName = text;
        }

        @Override
        protected PlayList doInBackground(View... views) {
            return LibManager.createPlaylist(views[0], playListName);
        }

        @Override
        protected void onPostExecute(PlayList playList) {
            if (playList != null) {
                mAdapter.notifyPlayListAdd(playList);
                Toast.makeText(getActivity(),
                        String.format(getActivity().getResources().getString(R.string.message_created_playlist), playList.getmPlayListName())
                        , Toast.LENGTH_LONG).show();
            }
        }
    }
}
