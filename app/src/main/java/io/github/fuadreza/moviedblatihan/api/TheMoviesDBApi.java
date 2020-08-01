package io.github.fuadreza.moviedblatihan.api;

import io.github.fuadreza.moviedblatihan.model.Movie;
import io.github.fuadreza.moviedblatihan.model.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMoviesDBApi {

    @GET("movie/now_playing")
    Call<MovieResponse> getNowPlayingMovies(@Query("api_key") String apiKey);

    @GET("movie/upcoming")
    Call<MovieResponse> getUpcomingMovies(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}")
    Call<Movie> getMovie(
            @Path("movie_id") int id,
            @Query("api_key") String apiKEy
    );

    @GET("search/movie")
    Call<MovieResponse> searchMovie(
            @Query("query") String nameMovie,
            @Query("api_key") String apiKEy
    );

}
