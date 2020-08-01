package io.github.fuadreza.moviedblatihan.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.github.fuadreza.moviedblatihan.BuildConfig;
import io.github.fuadreza.moviedblatihan.R;
import io.github.fuadreza.moviedblatihan.model.Movie;
import io.github.fuadreza.moviedblatihan.provider.FavoriteContentProvider;

/**
 * Dibuat dengan kerjakerasbagaiquda oleh Shifu pada tanggal 15/02/2019.
 */
public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private int appWidgetId;
    private Cursor cursor;
    private ArrayList<Movie> movies = new ArrayList<>();
    private final List<Bitmap> mWidgetItems = new ArrayList<>();
    Movie dataFavorite;

    public StackRemoteViewsFactory(Context applicationContext, Intent intent) {
        context = applicationContext;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        cursor = context.getContentResolver().query(FavoriteContentProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onDataSetChanged() {
        final long dataSetToken = Binder.clearCallingIdentity();
        cursor = context.getContentResolver().query(FavoriteContentProvider.CONTENT_URI, null, null, null, null);
        Binder.restoreCallingIdentity(dataSetToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.favorite_item);
        if (cursor.moveToPosition(position)) {
            dataFavorite = new Movie(cursor);
            String urlImage = BuildConfig.IMAGE_BASE_URL;
            Bitmap bitmap = null;
            String posterPath = dataFavorite.getPosterPath();
            try {
                /*bitmap = Picasso.with(context).load(poster).get();
                remoteViews.setImageViewBitmap(R.id.iv_movieWidget, bitmap );*/
                bitmap = Glide.with(context)
                        .asBitmap()
                        .load(BuildConfig.IMAGE_BASE_URL + dataFavorite.getPosterPath())
                        .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                        .submit()
                .get();

                remoteViews.setImageViewBitmap(R.id.iv_movieWidget, bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            String title = dataFavorite.getTitle();
            remoteViews.setTextViewText(R.id.tv_appwidget_titleMovie, title);
        }
        Bundle bundle = new Bundle();
        bundle.putInt(BuildConfig.EXTRA_STATE, position);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        remoteViews.setOnClickFillInIntent(R.id.iv_movieWidget, intent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
