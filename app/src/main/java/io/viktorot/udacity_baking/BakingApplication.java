package io.viktorot.udacity_baking;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import io.viktorot.udacity_baking.widget.RecipeWidget;
import timber.log.Timber;

public class BakingApplication extends Application {

    public static void updateWidgets(Context context) {
        Application app = (Application) context.getApplicationContext();
        Intent intent = new Intent(app, RecipeWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int[] ids = AppWidgetManager
                .getInstance(app)
                .getAppWidgetIds(new ComponentName(app, RecipeWidget.class));

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        app.sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
