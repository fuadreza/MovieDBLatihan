package io.github.fuadreza.moviedblatihan.ui.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import io.github.fuadreza.moviedblatihan.R;
import io.github.fuadreza.moviedblatihan.model.Setting;
import io.github.fuadreza.moviedblatihan.service.DailyReminderReceiver;
import io.github.fuadreza.moviedblatihan.service.ReleaseTodayReminderReceiver;

/**
 * Dibuat dengan kerjakerasbagaiquda oleh Shifu pada tanggal 15/02/2019.
 */
public class NotificationActivity extends AppCompatActivity {
    private Setting setting;

    private Switch swDaily;
    private Switch swRelease;

    private DailyReminderReceiver dailyReminderReceiver;
    private ReleaseTodayReminderReceiver releaseTodayReminderReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        setting = new Setting(this);

        bindUI();

        dailyReminderReceiver = new DailyReminderReceiver();

        releaseTodayReminderReceiver = new ReleaseTodayReminderReceiver();

    }

    private void bindUI() {
        swDaily = findViewById(R.id.sw_daily_reminder);
        swRelease = findViewById(R.id.sw_release_reminder);

        swDaily.setChecked(setting.getDailyReminderCheck());
        swRelease.setChecked(setting.getReleaseTodayCheck());

        swDaily.setOnCheckedChangeListener(swDailyCheckListener);
        swRelease.setOnCheckedChangeListener(swReleaseCheckListener);
    }

    private CompoundButton.OnCheckedChangeListener swDailyCheckListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                setting.setDailyReminderCheck(true);
                String time = "07:00";
                String message = "Wow, 5 Daftar Film ini bikin pak Jokowi dan SBY Tercengang!";
//                setting.setDailyTime(time);
//                setting.setDailyMessage(message);
                dailyReminderReceiver.setDailyAlarm(NotificationActivity.this, DailyReminderReceiver.EXTRA_TYPE, time, message);
            }else {
                setting.setDailyReminderCheck(false);
                dailyReminderReceiver.cancelAlarm(NotificationActivity.this);
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener swReleaseCheckListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                setting.setReleaseTodayCheck(true);
                String time = "08:00";
                releaseTodayReminderReceiver.setReminderRelease(NotificationActivity.this, DailyReminderReceiver.EXTRA_TYPE, time, DailyReminderReceiver.EXTRA_MESSAGE);
            }else {
                setting.setReleaseTodayCheck(false);
                releaseTodayReminderReceiver.alarmCancel(NotificationActivity.this);
            }
        }
    };
}
