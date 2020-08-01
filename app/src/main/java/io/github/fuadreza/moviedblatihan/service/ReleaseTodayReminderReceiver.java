package io.github.fuadreza.moviedblatihan.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.github.fuadreza.moviedblatihan.BuildConfig;
import io.github.fuadreza.moviedblatihan.R;
import io.github.fuadreza.moviedblatihan.api.MovieRepository;
import io.github.fuadreza.moviedblatihan.api.TheMoviesDBApi;
import io.github.fuadreza.moviedblatihan.db.Favorite;
import io.github.fuadreza.moviedblatihan.model.Movie;
import io.github.fuadreza.moviedblatihan.model.MovieResponse;
import io.github.fuadreza.moviedblatihan.ui.home.HomeActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Dibuat dengan kerjakerasbagaiquda oleh Shifu pada tanggal 15/02/2019.
 */
public class ReleaseTodayReminderReceiver extends BroadcastReceiver {
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";

    private final int ID_RELEASE = 2;

    public static String CHANNEL_ID = "channel_01";
    public static CharSequence CHANNEL_NAME = "movie channel";

    private MovieRepository repository;
    private ArrayList<Movie> movies = new ArrayList<>();

    public ReleaseTodayReminderReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        Toast.makeText(context, "JAM 8 LOH", Toast.LENGTH_SHORT).show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TheMoviesDBApi api = retrofit.create(TheMoviesDBApi.class);
        api.getNowPlayingMovies(BuildConfig.MVDB_API_KEY).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();
                    if (movieResponse != null && movieResponse.getMovies() != null) {

                        movies = movieResponse.getMovies();
//                        Toast.makeText(context, "SUKSES " + movies.get(0).getTitle(), Toast.LENGTH_SHORT).show();
                        showAlarmNotif(context, movies);
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }

    private void showAlarmNotif(Context context, ArrayList<Movie> listMovie) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, HomeActivity.class);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        ///////////////////

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(c);


        for (int i = 0; i < listMovie.size(); i++) {
            if (date.equals(listMovie.get(i).getReleaseDate())) {
//            if(listMovie.get(i).getReleaseDate().equals("2019-01-31")){
                intent.putExtra(Favorite.MovieColumns.MOVIE_TITLE, listMovie.get(i).getTitle());
                intent.putExtra(Favorite.MovieColumns.MOVIE_RELEASE_DATE, listMovie.get(i).getReleaseDate());
                intent.putExtra(Favorite.MovieColumns.MOVIE_RATING, listMovie.get(i).getOverview());
                intent.putExtra(Favorite.MovieColumns.MOVIE_POSTER_PATH, listMovie.get(i).getPosterPath());
                intent.putExtra(Favorite.MovieColumns.MOVIE_OVERVIEW, listMovie.get(i).getOverview());
                intent.putExtra(Favorite.MovieColumns.MOVIE_ID, listMovie.get(i).getId());

                PendingIntent pendingIntent = TaskStackBuilder.create(context)
                        .addNextIntent(intent)
                        .getPendingIntent(ID_RELEASE, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setContentTitle(listMovie.get(i).getTitle())
                        .setContentText(listMovie.get(i).getTitle() + "is now release")
                        .setSmallIcon(R.drawable.ic_access_time)
                        .setColor(ContextCompat.getColor(context, android.R.color.darker_gray))
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setContentIntent(pendingIntent)
                        .setSound(alarmSound)
                        .setAutoCancel(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

                    builder.setChannelId(CHANNEL_ID);
                    if (notificationManager != null) {
                        notificationManager.createNotificationChannel(channel);
                    }

//                    notificationManager.notify(ID_RELEASE, builder.build());
                }

                Notification notification = builder.build();

                if (notificationManager != null) {
                    notificationManager.notify(ID_RELEASE, notification);
                }
            }
        }

    }

    public void setReminderRelease(Context context, String type, String time, String message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseTodayReminderReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);

        String timeArray[] = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        int request = ID_RELEASE;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, request, intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent);
        Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show();
    }

    public void alarmCancel(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseTodayReminderReceiver.class);
        int request = ID_RELEASE;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                request, intent, 0);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
    }
}
