package io.github.fuadreza.favoritemovie.activity;

import android.database.Cursor;

import io.github.fuadreza.favoritemovie.model.Movie;

public interface FavoriteView {

    void onClick(Movie movie);

    void preExecute();

    void postExecute(Cursor movies);

}
