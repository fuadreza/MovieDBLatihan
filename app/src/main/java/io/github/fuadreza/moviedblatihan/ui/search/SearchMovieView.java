package io.github.fuadreza.moviedblatihan.ui.search;

import java.util.List;

import io.github.fuadreza.moviedblatihan.MainView;
import io.github.fuadreza.moviedblatihan.model.Movie;

public interface SearchMovieView extends MainView {
    void onSuccess(List<Movie> movies);

    void onError();

    void onClick(Movie movie);
}