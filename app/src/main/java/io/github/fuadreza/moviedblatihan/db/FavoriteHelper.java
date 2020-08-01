package io.github.fuadreza.moviedblatihan.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import io.github.fuadreza.moviedblatihan.model.Movie;

public class FavoriteHelper {
    private static final String DATABASE_TABLE = Favorite.TABLE_FAVORITE;
    private static DatabaseHelper dataBaseHelper;
    private static FavoriteHelper INSTANCE;
    private static SQLiteDatabase database;

    public FavoriteHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static FavoriteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavoriteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();
        if (database.isOpen())
            database.close();
    }

    public List<Movie> getAllFavorite() {
        List<Movie> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                Favorite.MovieColumns.MOVIE_ID + " ASC",
                null);
        cursor.moveToFirst();
        Movie movie;
        if (cursor.getCount() > 0) {
            do {
                movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Favorite.MovieColumns.MOVIE_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(Favorite.MovieColumns.MOVIE_TITLE)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(Favorite.MovieColumns.MOVIE_POSTER_PATH)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(Favorite.MovieColumns.MOVIE_RELEASE_DATE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(Favorite.MovieColumns.MOVIE_OVERVIEW)));

                arrayList.add(movie);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public static ArrayList<Movie> mapCursorToArrayList(Cursor cursor){
        ArrayList<Movie> movieList = new ArrayList<>();

        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(Favorite.MovieColumns.MOVIE_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(Favorite.MovieColumns.MOVIE_TITLE));
            String releaseDate = cursor.getString(cursor.getColumnIndexOrThrow(Favorite.MovieColumns.MOVIE_RELEASE_DATE));
            Float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(Favorite.MovieColumns.MOVIE_RATING));
            String overview = cursor.getString(cursor.getColumnIndexOrThrow(Favorite.MovieColumns.MOVIE_OVERVIEW));
            String posterPath = cursor.getString(cursor.getColumnIndexOrThrow(Favorite.MovieColumns.MOVIE_POSTER_PATH));
            movieList.add(new Movie(id, title, releaseDate, rating, overview, posterPath));
        }

        return movieList;
    }

    public long insertFavorite(Movie movies){
        ContentValues args = new ContentValues();
        args.put(Favorite.MovieColumns.MOVIE_ID, movies.getId());
        args.put(Favorite.MovieColumns.MOVIE_TITLE, movies.getTitle());
        args.put(Favorite.MovieColumns.MOVIE_POSTER_PATH, movies.getPosterPath());
        args.put(Favorite.MovieColumns.MOVIE_RELEASE_DATE, movies.getReleaseDate());
        args.put(Favorite.MovieColumns.MOVIE_RATING, movies.getRating());
        args.put(Favorite.MovieColumns.MOVIE_OVERVIEW, movies.getOverview());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public long removeFavorite(int id){
        return database.delete(DATABASE_TABLE, Favorite.MovieColumns.MOVIE_ID + "=" + id,null);
    }

    public boolean isFavorite(int id){
        String query = "SELECT * FROM " + DATABASE_TABLE + " WHERE ID='" + id + "'";

        Cursor cursor = database.rawQuery(query,null);

        boolean isFavorite = cursor.getCount() > 0;

        cursor.close();

        return isFavorite;
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE, null
                , Favorite.MovieColumns.MOVIE_ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , Favorite.MovieColumns.MOVIE_ID + " ASC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, Favorite.MovieColumns.MOVIE_ID + " = ?", new String[]{id});
    }
}
