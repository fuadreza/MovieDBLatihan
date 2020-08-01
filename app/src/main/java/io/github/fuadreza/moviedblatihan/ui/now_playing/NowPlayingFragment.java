package io.github.fuadreza.moviedblatihan.ui.now_playing;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.github.fuadreza.moviedblatihan.R;
import io.github.fuadreza.moviedblatihan.api.MovieRepository;
import io.github.fuadreza.moviedblatihan.model.Movie;
import io.github.fuadreza.moviedblatihan.ui.detail.DetailMovieActivity;

public class NowPlayingFragment extends Fragment {
    private RecyclerView recycler;
    private NowPlayingAdapter adapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final String KEY_MOVIES = "MOVIES";
    private ArrayList<Movie> _movies = new ArrayList<>();

    private MovieRepository repository;
    private NowPlayingView callback;

    private static final String BUNDLE_RECYCLER = "recycler_bundle";
    private static final String LIST_STATE = "list_state";
    private Parcelable savedRecyclerLayoutState;

    public NowPlayingFragment() {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putParcelableArrayList(KEY_MOVIES, _movies);
        Log.d("MOVIE", "MOVIES " + _movies);
        outState.putParcelable(BUNDLE_RECYCLER, recycler.getLayoutManager().onSaveInstanceState());
//        outState.putSerializable(STATE_ITEMS, _movies);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_now_playing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        _movies = new ArrayList<>();

        bindUI(view);

        adapter = new NowPlayingAdapter(_movies, callback);


        if (savedInstanceState == null) {

            initUI();

            recycler.setAdapter(adapter);

            repository.getNowPlayingMovies(callback);

            swipeRefreshLayout.setOnRefreshListener(refreshListener);

        } else {
            _movies = savedInstanceState.getParcelableArrayList(KEY_MOVIES);
            savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER);
            adapter.refill(_movies);

//            adapter.refill(_movies);
            recycler.setAdapter(adapter);
            recycler.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }

    private void bindUI(View view) {
        repository = MovieRepository.getInstance();
        recycler = view.findViewById(R.id.rv_movie);
        progressBar = view.findViewById(R.id.progress_bar);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void initUI() {
        callback = new NowPlayingView() {
            @Override
            public void onSuccess(List<Movie> movies) {
                adapter = new NowPlayingAdapter(movies, callback);
                _movies = (ArrayList<Movie>) movies;
                recycler.setAdapter(adapter);
//                adapter.refil(_movies);
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

//        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            repository.getNowPlayingMovies(callback);
            swipeRefreshLayout.setRefreshing(false);
        }
    };
}
