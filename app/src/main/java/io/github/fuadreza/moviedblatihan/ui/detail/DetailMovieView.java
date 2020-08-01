package io.github.fuadreza.moviedblatihan.ui.detail;

import io.github.fuadreza.moviedblatihan.MainView;
import io.github.fuadreza.moviedblatihan.model.Movie;

public interface DetailMovieView extends MainView {
    void onSuccess(Movie movie);

    void onError();
}
