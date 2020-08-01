package io.github.fuadreza.moviedblatihan.ui.detail;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import io.github.fuadreza.moviedblatihan.BuildConfig;
import io.github.fuadreza.moviedblatihan.R;
import io.github.fuadreza.moviedblatihan.api.MovieRepository;
import io.github.fuadreza.moviedblatihan.db.Favorite;
import io.github.fuadreza.moviedblatihan.db.FavoriteHelper;
import io.github.fuadreza.moviedblatihan.model.Movie;
import io.github.fuadreza.moviedblatihan.provider.FavoriteContentProvider;

public class DetailMovieActivity extends AppCompatActivity {

    public static String MOVIE_ID = "movie_id";
    private Boolean isFavorite = false;

    private Toolbar toolbar;
    private ProgressBar progressBar;
    private TextView title;
    private TextView desc;
    private ImageView poster;
    private Menu menuItem;

    private MovieRepository repository;
    private FavoriteHelper helper;
    private Movie movie_;
    private int idMovie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_movie_activity);

        initToolbar();

        initUI();

        idMovie = getIntent().getIntExtra(MOVIE_ID, idMovie);

        Uri uri = getIntent().getData();

        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            if (cursor != null) {

                if (cursor.moveToFirst()) movie_ = new Movie(cursor);
                cursor.close();
            }
        }

        helper = FavoriteHelper.getInstance(getApplicationContext());
        helper.open();

        repository = MovieRepository.getInstance();

        getMovie();


//

        favoriteState();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void initUI() {
        title = findViewById(R.id.tv_title);
        desc = findViewById(R.id.tv_desc);
        poster = findViewById(R.id.iv_poster);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void getMovie() {
        repository.getMovie(idMovie, new DetailMovieView() {
            @Override
            public void onSuccess(Movie movie) {
                toolbar.setTitle(movie.getTitle());
                title.setText(movie.getTitle());
                desc.setText(movie.getOverview());

//                Toast.makeText(DetailMovieActivity.this, "POSTER " + movie.getPosterPath(), Toast.LENGTH_SHORT).show();

                if (!isFinishing()) {
                    Glide.with(DetailMovieActivity.this)
                            .load(BuildConfig.IMAGE_BASE_URL + movie.getPosterPath())
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                            .into(poster);
                }
                movie_ = movie;
                Log.d("MOVIE", "" + movie_);
            }

            @Override
            public void onError() {
                finish();
            }

            @Override
            public void showLoading() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void hideLoading() {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite_menu, menu);
        menuItem = menu;
        setFavorite();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_to_favorite) {
            if (isFavorite) {
                removeFromFavorite();
            } else {
                addToFavorite();
            }
            isFavorite = !isFavorite;
            setFavorite();
        }

        return super.onOptionsItemSelected(item);
    }

    private void favoriteState() {
        isFavorite = helper.isFavorite(idMovie);
    }

    private void setFavorite() {
        if (isFavorite)
            menuItem.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_added_to_favorites));
        else
            menuItem.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_add_to_favorites));
    }

    private void addToFavorite() {
        //Jika menggunakan helper biasa
        /*long result = helper.insertFavorite(movie_);

        if (result <= 0)
            Toast.makeText(this, "Gagal menambah data", Toast.LENGTH_SHORT).show();*/

        //Jika menggunakan Content Provider

        ContentValues args = new ContentValues();
        args.put(Favorite.MovieColumns.MOVIE_ID, movie_.getId());
        args.put(Favorite.MovieColumns.MOVIE_TITLE, movie_.getTitle());
        args.put(Favorite.MovieColumns.MOVIE_POSTER_PATH, movie_.getPosterPath());
        args.put(Favorite.MovieColumns.MOVIE_RELEASE_DATE, movie_.getReleaseDate());
        args.put(Favorite.MovieColumns.MOVIE_RATING, movie_.getRating());
        args.put(Favorite.MovieColumns.MOVIE_OVERVIEW, movie_.getOverview());

        Uri uri = getContentResolver().insert(FavoriteContentProvider.CONTENT_URI, args);

        Toast.makeText(this, "Add to favorite " + title.getText(), Toast.LENGTH_SHORT).show();
    }

    private void removeFromFavorite() {
        long result = helper.removeFavorite(idMovie);

        if (result <= 0)
            Toast.makeText(this, "Gagal menghapus data", Toast.LENGTH_SHORT).show();

        Toast.makeText(this, "Remove from favorite " + title.getText(), Toast.LENGTH_SHORT).show();
    }
}
