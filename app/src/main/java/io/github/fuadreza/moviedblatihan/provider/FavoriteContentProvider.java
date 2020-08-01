package io.github.fuadreza.moviedblatihan.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import io.github.fuadreza.moviedblatihan.db.DatabaseHelper;
import io.github.fuadreza.moviedblatihan.db.Favorite;
import io.github.fuadreza.moviedblatihan.db.FavoriteHelper;
import io.github.fuadreza.moviedblatihan.ui.favorite.FavoriteFragment;

public class FavoriteContentProvider extends ContentProvider {

    private static final String AUTHORITY = "io.github.fuadreza.moviedblatihan";
    private static final String BASE_PATH = "movies";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    private static final int MOVIES = 1;
    private static final int MOVIES_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, MOVIES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", MOVIES_ID);
    }

    private SQLiteDatabase sqLiteDatabase;
    private FavoriteHelper favoriteHelper;

    @Override
    public boolean onCreate() {
        favoriteHelper = FavoriteHelper.getInstance(getContext());
//        sqLiteDatabase = databaseHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        favoriteHelper.open();
        Cursor cursor;

        switch (uriMatcher.match(uri)) {
            case MOVIES:
                cursor = favoriteHelper.queryProvider();
                break;
            case MOVIES_ID:
                cursor = favoriteHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        /*long id = sqLiteDatabase.insert(Favorite.TABLE_FAVORITE, null, values);

        if (id >0) { Uri mUri = ContentUris.withAppendedId(CONTENT_URI, id);
            return mUri;
        }
        throw new SQLException("Insertion Failed for URI :" + uri);*/

        favoriteHelper.open();
        long added;

        switch (uriMatcher.match(uri)) {
            case MOVIES:
                added = favoriteHelper.insertProvider(values);
                break;
            default:
                added = 0;
                break;
        }

        getContext().getContentResolver().notifyChange(CONTENT_URI, new FavoriteFragment.DataObserver(new Handler(), getContext()));

//        Toast.makeText(getContext(), "URI " + Uri.parse(CONTENT_URI + "/" + added), Toast.LENGTH_SHORT).show();

        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int delCount = 0;
        switch (uriMatcher.match(uri)) {
            case MOVIES:
                delCount = sqLiteDatabase.delete(Favorite.TABLE_FAVORITE, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        return delCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
