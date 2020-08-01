package io.github.fuadreza.moviedblatihan.ui.favorite;

import android.database.Cursor;

import java.util.List;

import io.github.fuadreza.moviedblatihan.model.Movie;

public interface FavoriteView {
    void onClick(Movie movie);

    void preExecute();

    void postExecute(Cursor movies);
}
