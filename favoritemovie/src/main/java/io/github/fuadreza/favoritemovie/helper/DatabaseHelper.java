package io.github.fuadreza.favoritemovie.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Dibuat dengan kerjakerasbagaiquda oleh Shifu pada tanggal 15/02/2019.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FavoriteMovie.db";
    private static final int DATABASE_VERSION = 24;
    private static DatabaseHelper INSTANCE;

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_FAVORITE = "CREATE TABLE " + Favorite.TABLE_FAVORITE + "(" +
                Favorite.MovieColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Favorite.MovieColumns.MOVIE_ID + " INTEGER NOT NULL, " +
                Favorite.MovieColumns.MOVIE_TITLE + " TEXT NOT NULL, " +
                Favorite.MovieColumns.MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                Favorite.MovieColumns.MOVIE_POSTER_PATH + " TEXT NOT NULL, " +
                Favorite.MovieColumns.MOVIE_RATING + " TEXT NOT NULL, " +
                Favorite.MovieColumns.MOVIE_OVERVIEW + " TEXT NOT NULL);";
        db.execSQL(CREATE_TABLE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Favorite.TABLE_FAVORITE);
        onCreate(db);
    }
}
