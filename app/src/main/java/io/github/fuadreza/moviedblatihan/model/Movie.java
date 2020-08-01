package io.github.fuadreza.moviedblatihan.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.github.fuadreza.moviedblatihan.db.Favorite;

public class Movie implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @SerializedName("vote_average")
    @Expose
    private float rating;

    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds;

    @SerializedName("overview")
    @Expose
    private String overview;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.posterPath);
        dest.writeString(this.releaseDate);
        dest.writeFloat(this.rating);
        dest.writeString(this.overview);
    }

    public Movie() {

    }

    private Movie(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.rating = in.readFloat();
        this.overview = in.readString();
    }

    public Movie(int id, String title, String releaseDate, Float rating, String overview, String posterPath){
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.overview = overview;
        this.posterPath = posterPath;
    }

    public Movie(Cursor cursor){
        this.id = Favorite.getColumnInt(cursor, Favorite.MovieColumns.MOVIE_ID);
        this.title = Favorite.getColumnString(cursor, Favorite.MovieColumns.MOVIE_TITLE);
        this.posterPath = Favorite.getColumnString(cursor, Favorite.MovieColumns.MOVIE_POSTER_PATH);
        this.releaseDate = Favorite.getColumnString(cursor, Favorite.MovieColumns.MOVIE_RELEASE_DATE);
        this.rating = Favorite.getColumnFloat(cursor, Favorite.MovieColumns.MOVIE_RATING);
        this.overview = Favorite.getColumnString(cursor, Favorite.MovieColumns.MOVIE_OVERVIEW);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
