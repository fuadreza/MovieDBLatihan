package io.github.fuadreza.moviedblatihan.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import io.github.fuadreza.moviedblatihan.R;
import io.github.fuadreza.moviedblatihan.ui.favorite.FavoriteFragment;
import io.github.fuadreza.moviedblatihan.ui.now_playing.NowPlayingFragment;
import io.github.fuadreza.moviedblatihan.ui.search.SearchFragment;
import io.github.fuadreza.moviedblatihan.ui.setting.NotificationActivity;
import io.github.fuadreza.moviedblatihan.ui.upcoming.UpcomingFragment;

public class HomeActivity extends AppCompatActivity {
    private String KEY_FRAGMENT = "NOW_PLAYING_TAG";
    private static final String KEY_TITLE = "TITLE_TAG";
    private Parcelable mListState = null;

    private final String NOW_PLAYING_TAG = "NOW_PLAYING_TAG";
    private final String UPCOMING_TAG = "UPCOMING_TAG";
    private final String SEARCH_TAG = "SEARCH_TAG";
    private final String FAVORITE_TAG = "FAVORITE_TAG";

    private Toolbar toolbar;
    private Fragment fragment = new NowPlayingFragment();
    private String title = "Home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*if (savedInstanceState == null) {
            loadFragment(fragment, KEY_FRAGMENT);
            toolbar.setTitle(R.string.title_now_playing);
            Log.d("HELLO", "HELLO HOME" + savedInstanceState);
//            fragment = getSupportFragmentManager().findFragmentByTag(NOW_PLAYING_TAG);
        } else {
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, KEY_FRAGMENT);
            title = savedInstanceState.getString(KEY_TITLE);
            Log.d("HELLO", "HELLO HOME" + fragment);

            loadFragment(fragment, KEY_FRAGMENT);
//            toolbar.setTitle(title);
        }*/

//        loadFragment(fragment, NOW_PLAYING_TAG);

//        toolbar.setTitle(R.string.title_now_playing);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if(savedInstanceState == null){
            loadFragment(fragment, KEY_FRAGMENT);
            toolbar.setTitle(title);
        }else{
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, KEY_FRAGMENT);
            title = savedInstanceState.getString(KEY_TITLE);

            loadFragment(fragment,KEY_FRAGMENT);
            toolbar.setTitle(title);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_TITLE, title);
        getSupportFragmentManager().putFragment(outState, KEY_FRAGMENT, fragment);
        super.onSaveInstanceState(outState);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_now_playing:
                    fragment = new NowPlayingFragment();
                    loadFragment(fragment, NOW_PLAYING_TAG);
                    toolbar.setTitle(R.string.title_now_playing);
                    KEY_FRAGMENT = NOW_PLAYING_TAG;
                    return true;
                case R.id.navigation_upcoming:
                    fragment = new UpcomingFragment();
                    loadFragment(fragment, UPCOMING_TAG);
                    toolbar.setTitle(R.string.title_upcoming);
                    KEY_FRAGMENT = UPCOMING_TAG;
                    return true;
                case R.id.navigation_search:
                    fragment = new SearchFragment();
                    loadFragment(fragment, SEARCH_TAG);
                    toolbar.setTitle(R.string.title_search);
                    KEY_FRAGMENT = SEARCH_TAG;
                    return true;
                case R.id.navigation_favorite:
                    fragment = new FavoriteFragment();
                    loadFragment(fragment, FAVORITE_TAG);
                    toolbar.setTitle(R.string.title_favorite);
                    KEY_FRAGMENT = FAVORITE_TAG;
                    return true;
            }
            loadFragment(fragment, KEY_FRAGMENT);
            toolbar.setTitle(title);
            return true;
        }
    };

    private void loadFragment(Fragment fragment, String TAG) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, fragment, TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }else if (id == R.id.action_notification){
            Intent mIntent = new Intent(this, NotificationActivity.class);
            startActivity(mIntent);
        }

        return super.onOptionsItemSelected(item);
    }

}
