package io.github.fuadreza.moviedblatihan.service;

import android.app.AlarmManager;
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

import java.util.Calendar;

import io.github.fuadreza.moviedblatihan.R;
import io.github.fuadreza.moviedblatihan.ui.home.HomeActivity;

/**
 * Dibuat dengan kerjakerasbagaiquda oleh Shifu pada tanggal 15/02/2019.
 */
public class DailyReminderReceiver extends BroadcastReceiver {
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";

    private final int ID_DAILY = 1;

    public DailyReminderReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = "Tonton Sekarang";
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        int notifId = ID_DAILY;

        showAlarmNotification(context, title, message, notifId);


    }

    private void showAlarmNotification(Context context, String title, String message, int notifId) {
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager channel";

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(context, HomeActivity.class);

        PendingIntent pendingIntent = TaskStackBuilder.create(context)
                .addNextIntent(intent)
                .getPendingIntent(notifId, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, message)
                .setSmallIcon(R.drawable.ic_access_time)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int impHigh = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, impHigh);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId(CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(notifId, builder.build());
    }

    public void setDailyAlarm(Context context, String type, String time, String message){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, DailyReminderReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);

        String timeArray[] = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY , intent ,0 );
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(context, "Daily reminder set up", Toast.LENGTH_SHORT ).show();
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminderReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY, intent, 0);
        alarmManager.cancel(pendingIntent);

        Toast.makeText(context, "Reminder removed", Toast.LENGTH_SHORT).show();
    }
}
