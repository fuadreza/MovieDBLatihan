package io.github.fuadreza.moviedblatihan.api;

import android.util.Log;

import io.github.fuadreza.moviedblatihan.BuildConfig;
import io.github.fuadreza.moviedblatihan.model.Movie;
import io.github.fuadreza.moviedblatihan.model.MovieResponse;
import io.github.fuadreza.moviedblatihan.ui.detail.DetailMovieView;
import io.github.fuadreza.moviedblatihan.ui.now_playing.NowPlayingView;
import io.github.fuadreza.moviedblatihan.ui.upcoming.UpcomingView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {
    private static MovieRepository repository;

    private TheMoviesDBApi api;

    public MovieRepository(TheMoviesDBApi api) {
        this.api = api;
    }

    public static MovieRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new MovieRepository(retrofit.create(TheMoviesDBApi.class));
        }

        return repository;
    }

    public void getNowPlayingMovies(final NowPlayingView callback) {
        api.getNowPlayingMovies(BuildConfig.MVDB_API_KEY)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            MovieResponse movieResponse = response.body();
                            if (movieResponse != null && movieResponse.getMovies() != null) {
                                Log.d("LEWAT", "1. LEWAT NGGAK? ");
//                                callback.showLoading();
                                Log.d("LEWAT", "2. LEWAT NGGAK? ");
                                callback.onSuccess(movieResponse.getMovies());
                                callback.hideLoading();
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getUpcomingMovies(final UpcomingView callback) {
        api.getUpcomingMovies(BuildConfig.MVDB_API_KEY)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            MovieResponse movieResponse = response.body();
                            if (movieResponse != null && movieResponse.getMovies() != null) {
                                callback.showLoading();
                                callback.onSuccess(movieResponse.getMovies());
                                callback.hideLoading();
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getMovie(int idMovie, final DetailMovieView callback) {
        api.getMovie(idMovie, BuildConfig.MVDB_API_KEY)
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        if (response.isSuccessful()) {
                            Movie movie = response.body();
                            if (movie != null) {
                                callback.onSuccess(movie);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void searchMovie(String nameMovie, final UpcomingView callback) {
        api.searchMovie(nameMovie, BuildConfig.MVDB_API_KEY)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            MovieResponse movieResponse = response.body();
                            if (movieResponse != null && movieResponse.getMovies() != null) {
                                callback.showLoading();
                                callback.onSuccess(movieResponse.getMovies());
                                callback.hideLoading();
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }
}
