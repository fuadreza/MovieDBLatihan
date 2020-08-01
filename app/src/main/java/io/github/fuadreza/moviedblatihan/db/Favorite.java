package io.github.fuadreza.moviedblatihan.db;

import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.List;

public class Favorite {

    public static final String TABLE_FAVORITE = "TABLE_FAVORITE";

    public static final class MovieColumns implements BaseColumns{
        public static final String MOVIE_ID = "ID";
        public static final String MOVIE_TITLE= "TITLE";
        public static final String MOVIE_POSTER_PATH = "POSTER_PATH";
        public static final String MOVIE_RELEASE_DATE = "RELEASE_DATE";
        public static final String MOVIE_RATING = "RATING";
//        public static final List<Integer> genreIds;
        public static final String MOVIE_OVERVIEW = "OVERVIEW";
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static float getColumnFloat(Cursor cursor, String columnName) {
        return cursor.getFloat(cursor.getColumnIndex(columnName));
    }

}
