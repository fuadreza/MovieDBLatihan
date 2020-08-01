package io.github.fuadreza.moviedblatihan.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Dibuat dengan kerjakerasbagaiquda oleh Shifu pada tanggal 15/02/2019.
 */
public class Setting {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PREF_NAME = "reminder_movie";

    public static final String KEY_CEK_RELEASE_TODAY = "checkedRelease";
    public static final String KEY_CEK_DAILY_REMINDER = "checkedDaily";

    public Setting(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public Boolean getDailyReminderCheck() {
        return sharedPreferences.getBoolean(KEY_CEK_DAILY_REMINDER, false);
    }

    public void setDailyReminderCheck(Boolean checked) {
        editor.putBoolean(KEY_CEK_DAILY_REMINDER, checked);
        editor.commit();
    }

    public Boolean getReleaseTodayCheck(){
        return sharedPreferences.getBoolean(KEY_CEK_RELEASE_TODAY, false);
    }

    public void setReleaseTodayCheck(Boolean checked) {
        editor.putBoolean(KEY_CEK_RELEASE_TODAY, checked);
        editor.commit();
    }
}
