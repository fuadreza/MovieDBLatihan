package io.github.fuadreza.moviedblatihan.ui.upcoming;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import io.github.fuadreza.moviedblatihan.R;
import io.github.fuadreza.moviedblatihan.api.MovieRepository;
import io.github.fuadreza.moviedblatihan.model.Movie;
import io.github.fuadreza.moviedblatihan.ui.detail.DetailMovieActivity;

public class UpcomingFragment extends Fragment {

    private RecyclerView recycler;
    private UpcomingAdapter adapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    private MovieRepository repository;
    private UpcomingView callback;

    public UpcomingFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upcoming, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        super.onViewCreated(view, savedInstanceState);

        bindUI(view);

        initUI();

        repository.getUpcomingMovies(callback);

        swipeRefreshLayout.setOnRefreshListener(refreshListener);
    }

    private void bindUI(View view){
        repository = MovieRepository.getInstance();
        recycler = view.findViewById(R.id.rv_movie);
        progressBar = view.findViewById(R.id.progress_bar);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
    }

    private void initUI() {
        callback = new UpcomingView() {
            @Override
            public void onSuccess(List<Movie> movies) {
                adapter = new UpcomingAdapter(movies, callback);
                recycler.setAdapter(adapter);
            }

            @Override
            public void onError() {
                Toast.makeText(getActivity(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void showLoading() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void hideLoading() {
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onClick(Movie movie) {
                Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
                intent.putExtra(DetailMovieActivity.MOVIE_ID, movie.getId());
                startActivity(intent);
            }
        };

        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            repository.getUpcomingMovies(callback);
            swipeRefreshLayout.setRefreshing(false);
        }
    };
}
