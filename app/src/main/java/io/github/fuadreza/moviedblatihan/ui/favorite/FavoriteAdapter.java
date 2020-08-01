package io.github.fuadreza.moviedblatihan.ui.favorite;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import io.github.fuadreza.moviedblatihan.BuildConfig;
import io.github.fuadreza.moviedblatihan.R;
import io.github.fuadreza.moviedblatihan.model.Movie;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MovieViewHolder> {
    private ArrayList<Movie> movies = new ArrayList<>();
    private FavoriteView view;
    private Context context;

    public FavoriteAdapter(FavoriteView view){
        this.view = view;
    }

    public FavoriteAdapter(ArrayList<Movie> movies, FavoriteView view) {
        this.movies = movies;
        this.view = view;
    }

    public FavoriteAdapter(Context context, FavoriteView view){
        this.context = context;
        this.view = view;
    }

    public void setListMovies(ArrayList<Movie> movies){
        this.movies.clear();
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_movie, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
        movieViewHolder.bind(movies.get(i));
        /*movieViewHolder.title.setText(movies.get(i).getTitle());
        movieViewHolder.desc.setText(movies.get(i).getOverview());
        Glide.with(context)
                .load(BuildConfig.IMAGE_BASE_URL + movies.get(i).getPosterPath())
                .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                .into(movieViewHolder.poster);*/

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        Movie movie;
        TextView title;
        TextView desc;
        ImageView poster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            desc = itemView.findViewById(R.id.tv_desc);
            poster = itemView.findViewById(R.id.iv_poster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.onClick(movie);
                }
            });
        }

        public void bind(Movie movie) {
            this.movie = movie;
            title.setText(movie.getTitle());
            desc.setText(movie.getOverview());
            Glide.with(itemView)
                    .load(BuildConfig.IMAGE_BASE_URL + movie.getPosterPath())
                    .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                    .into(poster);
        }
    }
}
