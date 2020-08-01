package io.github.fuadreza.moviedblatihan.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import io.github.fuadreza.moviedblatihan.BuildConfig;
import io.github.fuadreza.moviedblatihan.R;
import io.github.fuadreza.moviedblatihan.ui.home.HomeActivity;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

//        CharSequence widgetText = context.getString(R.string.appwidget_text);

        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId );
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_widget);
//        views.setTextViewText(R.id.appwidget_text, widgetText);

        views.setRemoteAdapter(R.id.stackView, intent );
        Intent intentToast = new Intent(context, HomeActivity.class);
        intentToast.setAction(BuildConfig.TOAST_ACTION_BUNDLE);
        intentToast.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId );

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent pendingIntentToast = PendingIntent.getBroadcast(context,0 ,intentToast ,PendingIntent.FLAG_UPDATE_CURRENT );
        views.setPendingIntentTemplate(R.id.stackView, pendingIntentToast );

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(BuildConfig.TOAST_ACTION_BUNDLE)){
            int appIdWidget = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            int indexView = intent.getIntExtra(BuildConfig.EXTRA_STATE, 0 );
            Toast.makeText(context, "touch" + indexView, Toast.LENGTH_SHORT ).show();
        }
        super.onReceive(context, intent);
    }
}

