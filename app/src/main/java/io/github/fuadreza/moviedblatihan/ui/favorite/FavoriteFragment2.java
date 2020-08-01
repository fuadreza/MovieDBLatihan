package io.github.fuadreza.moviedblatihan.ui.favorite;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import io.github.fuadreza.moviedblatihan.R;
import io.github.fuadreza.moviedblatihan.db.FavoriteHelper;
import io.github.fuadreza.moviedblatihan.model.Movie;
import io.github.fuadreza.moviedblatihan.provider.FavoriteContentProvider;
import io.github.fuadreza.moviedblatihan.ui.detail.DetailMovieActivity;

import static io.github.fuadreza.moviedblatihan.db.FavoriteHelper.mapCursorToArrayList;

public class FavoriteFragment2 extends Fragment implements FavoriteView {

    private RecyclerView recycler;
    private FavoriteAdapter adapter;
    private FavoriteView callback;
    private FavoriteHelper helper;
    private ProgressBar progressBar;

    private static final String EXTRA_STATE = "EXTRA_STATE";

    private static HandlerThread handlerThread;
    private DataObserver myObserver;

    public FavoriteFragment2() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        initUI();

        adapter = new FavoriteAdapter(getContext(), this);

//        recycler.setAdapter(adapter);

        if (savedInstanceState == null) {
            new LoadMovieAsync(getContext(), this).execute();
        } else {
            Toast.makeText(getContext(), "ISI", Toast.LENGTH_SHORT).show();
            ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (movies != null) {
                adapter.setListMovies(movies);
            }
        }
//        helper.queryProvider();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        bindUI(view);
        return view;
    }

    /*@Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        helper = FavoriteHelper.getInstance(this.getContext());
//        helper.open();

        bindUI(view);

        initUI();

//        adapter = new FavoriteAdapter(getContext(), this);

//        recycler.setAdapter(adapter);

        if (savedInstanceState == null) {
            Toast.makeText(getContext(), "NULL", Toast.LENGTH_SHORT).show();
            new LoadMovieAsync(getContext(), this).execute();
        } else {
            Toast.makeText(getContext(), "ISI", Toast.LENGTH_SHORT).show();
            ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (movies != null) {
                adapter.setListMovies(movies);
            }
        }

    }*/

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getMovies());
    }

    private void bindUI(View view) {
        recycler = view.findViewById(R.id.rv_fav);
        progressBar = view.findViewById(R.id.progress_bar);

        recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler.setHasFixedSize(true);
    }

    private void initUI() {
//        List<Movie> movies = helper.getAllFavorite();

        /*callback = new FavoriteView() {
            @Override
            public void onClick(Movie movie) {
                Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
                intent.putExtra(DetailMovieActivity.MOVIE_ID, movie.getId());
                startActivity(intent);
            }
        };*/

//        adapter = new FavoriteAdapter(movies, callback);

        handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        myObserver = new DataObserver(handler, getContext());
        getContext().getContentResolver().registerContentObserver(FavoriteContentProvider.CONTENT_URI, true, myObserver);

//        adapter = new FavoriteAdapter(getContext(), this);

//        Toast.makeText(getContext(), "Content Resolver " + getActivity().getContentResolver(), Toast.LENGTH_SHORT).show();

//        recycler.setAdapter(adapter);


    }

    @Override
    public void preExecute() {
        Toast.makeText(getContext(), "Pre Execute", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void postExecute(Cursor movies) {
        progressBar.setVisibility(View.INVISIBLE);
//        Toast.makeText(getContext(), "Post Execute", Toast.LENGTH_SHORT).show();
        ArrayList<Movie> listMovie = mapCursorToArrayList(movies);
        if (listMovie.size() > 0) {
            adapter.setListMovies(listMovie);
            Toast.makeText(getContext(), "Movie size " + listMovie.size(), Toast.LENGTH_SHORT).show();
            recycler.setAdapter(adapter);
        } else {
            adapter.setListMovies(new ArrayList<Movie>());
        }
        recycler.setAdapter(adapter);
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
        intent.putExtra(DetailMovieActivity.MOVIE_ID, movie.getId());
        startActivity(intent);
    }

    private static class LoadMovieAsync extends AsyncTask<Void, Void, Cursor> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<FavoriteView> weakCallback;

        private LoadMovieAsync(Context context, FavoriteView callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return context.getContentResolver().query(FavoriteContentProvider.CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor notes) {
            super.onPostExecute(notes);
            weakCallback.get().postExecute(notes);
        }
    }

    public static class DataObserver extends ContentObserver {
        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadMovieAsync(context, (FavoriteView) context).execute();
        }
    }
}
