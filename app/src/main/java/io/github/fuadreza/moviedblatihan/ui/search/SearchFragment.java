package io.github.fuadreza.moviedblatihan.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.github.fuadreza.moviedblatihan.R;
import io.github.fuadreza.moviedblatihan.api.MovieRepository;
import io.github.fuadreza.moviedblatihan.model.Movie;
import io.github.fuadreza.moviedblatihan.ui.detail.DetailMovieActivity;
import io.github.fuadreza.moviedblatihan.ui.upcoming.UpcomingView;

public class SearchFragment extends Fragment {

    private RecyclerView recycler;
    private SearchMovieAdapter adapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText edSearch;
    private Button btnSearch;

    private MovieRepository repository;
    private UpcomingView callback;

    public SearchFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindUI(view);

        btnSearch.setOnClickListener(btnSearchListener);

        edSearch.setOnEditorActionListener(onEnterSearchListener);

        initUI();

        repository.getUpcomingMovies(callback);

        swipeRefreshLayout.setOnRefreshListener(refreshListener);
    }

    private void bindUI(View view){
        repository = MovieRepository.getInstance();
        recycler = view.findViewById(R.id.rv_movie);
        progressBar = view.findViewById(R.id.progress_bar);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        edSearch = view.findViewById(R.id.ed_search);
        btnSearch = view.findViewById(R.id.btn_search);
    }

    private void initUI() {
        callback = new UpcomingView() {
            @Override
            public void onSuccess(List<Movie> movies) {
                adapter = new SearchMovieAdapter(movies, callback);
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

    public void searchMovie() {
        repository.searchMovie(edSearch.getText().toString(), callback);
    }

    private View.OnClickListener btnSearchListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            searchMovie();
        }
    };

    private TextView.OnEditorActionListener onEnterSearchListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchMovie();
                return true;
            }
            return false;
        }
    };

    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            repository.getUpcomingMovies(callback);
            swipeRefreshLayout.setRefreshing(false);
        }
    };
}
