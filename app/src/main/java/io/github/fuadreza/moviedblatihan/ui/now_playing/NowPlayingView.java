package io.github.fuadreza.moviedblatihan.ui.now_playing;

import java.util.List;

import io.github.fuadreza.moviedblatihan.MainView;
import io.github.fuadreza.moviedblatihan.model.Movie;

public interface NowPlayingView extends MainView {
    void onSuccess(List<Movie> movies);

    void onError();

    void onClick(Movie movie);
}