package io.github.fuadreza.favoritemovie.activity;

import android.content.ContentProviderClient;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import io.github.fuadreza.favoritemovie.R;
import io.github.fuadreza.favoritemovie.adapter.FavoriteAdapter;
import io.github.fuadreza.favoritemovie.model.Movie;
import io.github.fuadreza.favoritemovie.provider.FavoriteContentProvider;

import static io.github.fuadreza.favoritemovie.helper.FavoriteHelper.mapCursorToArrayList;

public class MainActivity extends AppCompatActivity implements FavoriteView {

    private RecyclerView recycler;
    private FavoriteAdapter adapter;
    private ProgressBar progressBar;

    private static final String EXTRA_STATE = "EXTRA_STATE";

    private static HandlerThread handlerThread;
    private DataObserver myObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new FavoriteAdapter(this, this);

        recycler = findViewById(R.id.rv_movie);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setHasFixedSize(true);

        handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        myObserver = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(FavoriteContentProvider.CONTENT_URI, true, myObserver);

        adapter = new FavoriteAdapter(this, MainActivity.this);
        recycler.setAdapter(adapter);

        if (savedInstanceState == null) {
            new LoadMovieAsync(this, MainActivity.this).execute();
        } else {
            ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (movies != null) {
                adapter.setListMovies(movies);
            }
        }
    }

    @Override
    public void onClick(Movie movie) {

    }

    @Override
    public void preExecute() {

    }

    @Override
    public void postExecute(Cursor movies) {
        ArrayList<Movie> listMovie = mapCursorToArrayList(movies);

        if (listMovie.size() > 0) {
            adapter.setListMovies(listMovie);
            recycler.setAdapter(adapter);
        } else {
            adapter.setListMovies(new ArrayList<Movie>());
        }
        recycler.setAdapter(adapter);
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
            ContentProviderClient contentProviderClient = context.getContentResolver().acquireContentProviderClient(FavoriteContentProvider.CONTENT_URI);
            Cursor cursor = null;
            try {
                cursor = contentProviderClient.query(FavoriteContentProvider.CONTENT_URI, null, null, null, null);
            } catch (RemoteException e) {
                Toast.makeText(context, "Can't Connect", Toast.LENGTH_SHORT).show();
            }
            return cursor;
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
//            new LoadMovieAsync(context, (FavoriteView) context).execute();
        }
    }
}
