package io.github.fuadreza.moviedblatihan.ui.upcoming;

import java.util.List;

import io.github.fuadreza.moviedblatihan.MainView;
import io.github.fuadreza.moviedblatihan.model.Movie;

public interface UpcomingView extends MainView {
    void onSuccess(List<Movie> movies);

    void onError();

    void onClick(Movie movie);
}
