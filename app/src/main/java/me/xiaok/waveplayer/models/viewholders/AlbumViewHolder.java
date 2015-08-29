package me.xiaok.waveplayer.models.viewholders;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;

import me.xiaok.waveplayer.LibManager;
import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.WaveApplication;
import me.xiaok.waveplayer.activities.AlbumActivity;
import me.xiaok.waveplayer.models.Album;
import me.xiaok.waveplayer.models.PlayList;
import me.xiaok.waveplayer.utils.LogUtils;
import me.xiaok.waveplayer.utils.Navigate;

/**
 * Created by GeeKaven on 15/8/18.
 */
public class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Palette.PaletteAsyncListener, PopupMenu.OnMenuItemClickListener {

    private static final String TAG = "AlbumViewHolder";
    private static final int BACKGROUND_COLOR = 0;
    private static final int TITLE_COLOR = 1;
    private static final int SUB_TITLE_COLOR = 2;
    //每一个ViewHoler都保存着背景色，标题色，副标题色
    private int[] colorCache = null;
    //默认背景色，标题色，副标题色
    private int defaultBackgroundColor;
    private int defaultTitleColor;
    private int defaultSubTitleColor;

    private ObjectAnimator backgroundAnimator;
    private ObjectAnimator titleAnimator;
    private ObjectAnimator subTitleAnimator;

    private AsyncTask<Bitmap, Void, Palette> mPaletteTask;

    private View itemView;
    private FrameLayout mRoot;
    private SimpleDraweeView mAlbumImg;
    private ImageView mClickMore;
    private TextView mAlbumName;
    private TextView mArtistName;
    private Album ref;
    private Context context;

    public AlbumViewHolder(View itemView) {
        super(itemView);

        this.itemView = itemView;
        context = itemView.getContext();

        defaultBackgroundColor = itemView.getResources().getColor(R.color.grid_default_background);
        defaultTitleColor = itemView.getResources().getColor(R.color.grid_default_title);
        defaultSubTitleColor = itemView.getResources().getColor(R.color.grid_default_subtitle);

        mRoot = (FrameLayout) itemView.findViewById(R.id.root);
        mAlbumImg = (SimpleDraweeView) itemView.findViewById(R.id.back_img);
        mClickMore = (ImageView) itemView.findViewById(R.id.click_more);
        mAlbumName = (TextView) itemView.findViewById(R.id.item_title);
        mArtistName = (TextView) itemView.findViewById(R.id.item_text);
        mAlbumImg.setAspectRatio(1.0f);

        mRoot.setOnClickListener(this);
        mClickMore.setOnClickListener(this);
    }

    /**
     * 更新ViewHolder
     *
     * @param album
     */
    public void updateViewHolder(Album album) {
        if (mPaletteTask != null && !mPaletteTask.isCancelled())
            mPaletteTask.cancel(true);

        ref = album;

        if (backgroundAnimator != null) {
            backgroundAnimator.setDuration(0);
            backgroundAnimator.cancel();
        }
        if (titleAnimator != null) {
            titleAnimator.setDuration(0);
            titleAnimator.cancel();
        }
        if (subTitleAnimator != null) {
            subTitleAnimator.setDuration(0);
            subTitleAnimator.cancel();
        }

        mRoot.setBackgroundColor(defaultBackgroundColor);
        mAlbumName.setTextColor(defaultTitleColor);
        mArtistName.setTextColor(defaultSubTitleColor);

        mAlbumName.setText(album.getmAlbumName());
        mArtistName.setText(album.getmArtistName());

        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse("file://" + album.getmAlbumArt()))
                .setProgressiveRenderingEnabled(true)
                .build();
        if (colorCache == null) {
            //当colorCache未初始化时，通过ImagePipeline来获取到图片的Bitmap
            //然后运用Palette
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            DataSource<CloseableReference<CloseableImage>>
                    dataSource = imagePipeline.fetchDecodedImage(request, WaveApplication.getContext());
            dataSource.subscribe(new BaseBitmapDataSubscriber() {
                @Override
                protected void onNewResultImpl(Bitmap bitmap) {
                    if (colorCache == null) {
                        mPaletteTask = Palette.from(bitmap).generate(AlbumViewHolder.this);
                    }
                }

                @Override
                protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {

                }
            }, CallerThreadExecutor.getInstance());
        } else {

            backgroundAnimator.setDuration(300).start();
            titleAnimator.setDuration(300).start();
            subTitleAnimator.setDuration(300).start();
        }
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(mAlbumImg.getController())
                .build();
        mAlbumImg.setController(controller);
    }

    @Override
    public void onGenerated(Palette palette) {
        int bgColor = defaultBackgroundColor;
        int titleColor = defaultTitleColor;
        int subTitleColor = defaultSubTitleColor;

        if (palette.getVibrantSwatch() != null) {
            bgColor = palette.getVibrantColor(0);
            titleColor = palette.getVibrantSwatch().getTitleTextColor();
            subTitleColor = palette.getVibrantSwatch().getBodyTextColor();
        } else if (palette.getLightVibrantSwatch() != null) {
            bgColor = palette.getLightVibrantColor(0);
            titleColor = palette.getLightVibrantSwatch().getTitleTextColor();
            subTitleColor = palette.getLightVibrantSwatch().getBodyTextColor();
        } else if (palette.getDarkVibrantSwatch() != null) {
            bgColor = palette.getDarkVibrantColor(0);
            titleColor = palette.getDarkVibrantSwatch().getTitleTextColor();
            subTitleColor = palette.getDarkVibrantSwatch().getBodyTextColor();
        } else if (palette.getMutedSwatch() != null) {
            bgColor = palette.getMutedColor(0);
            titleColor = palette.getMutedSwatch().getTitleTextColor();
            subTitleColor = palette.getMutedSwatch().getBodyTextColor();
        } else if (palette.getLightMutedSwatch() != null) {
            bgColor = palette.getLightMutedColor(0);
            titleColor = palette.getLightMutedSwatch().getTitleTextColor();
            subTitleColor = palette.getLightMutedSwatch().getBodyTextColor();
        } else if (palette.getDarkMutedSwatch() != null) {
            bgColor = palette.getDarkMutedColor(0);
            titleColor = palette.getDarkMutedSwatch().getTitleTextColor();
            subTitleColor = palette.getDarkMutedSwatch().getBodyTextColor();
        }

        colorCache = new int[]{bgColor, titleColor, subTitleColor};

        if (backgroundAnimator != null) {
            backgroundAnimator.setDuration(0);
            backgroundAnimator.cancel();
        }
        if (titleAnimator != null) {
            titleAnimator.setDuration(0);
            titleAnimator.cancel();
        }
        if (subTitleAnimator != null) {
            subTitleAnimator.setDuration(0);
            subTitleAnimator.cancel();
        }

        backgroundAnimator = ObjectAnimator.ofObject(
                mRoot,
                "backgroundColor",
                new ArgbEvaluator(),
                defaultBackgroundColor,
                bgColor);
        titleAnimator = ObjectAnimator.ofObject(
                mAlbumName,
                "textColor",
                new ArgbEvaluator(),
                defaultTitleColor,
                titleColor);
        subTitleAnimator = ObjectAnimator.ofObject(
                mArtistName,
                "textColor",
                new ArgbEvaluator(),
                defaultSubTitleColor,
                subTitleColor);

        backgroundAnimator.setDuration(300).start();
        titleAnimator.setDuration(300).start();
        subTitleAnimator.setDuration(300).start();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.click_more:
                PopupMenu popupMenu = new PopupMenu(itemView.getContext(), mClickMore, Gravity.END);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_album, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
                break;
            case R.id.root:
                LogUtils.v(TAG, ref.toString());
                Navigate.to(itemView.getContext(), AlbumActivity.class, AlbumActivity.EXTRA_ALBUM, ref);
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.play_all:
                return true;
            case R.id.add_queue:
                return true;
            case R.id.add_playlist:
                final ArrayList<PlayList> playLists = LibManager.getPlayLists();
                String[] names = new String[playLists.size()];
                for (int i = 0; i < playLists.size(); i++) {
                    names[i] = playLists.get(i).getmPlayListName();
                }

                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.add_to_playlist_title))
                        .setItems(names, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, final int i) {
                                new AsyncTask<Void, Void, Void>() {
                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        LibManager.addSongListToPlaylist(
                                                context,
                                                playLists.get(i),
                                                LibManager.getAlbumSongs(ref)
                                        );
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        Toast.makeText(context, context.getString(R.string.message_add_to_playlist),Toast.LENGTH_SHORT).show();
                                    }
                                }.execute();
                            }
                        })
                        .setNegativeButton(context.getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();
                return true;
        }
        return true;
    }
}
