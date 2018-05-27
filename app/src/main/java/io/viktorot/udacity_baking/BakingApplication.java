package io.viktorot.udacity_baking;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.VisibleForTesting;

import io.viktorot.udacity_baking.widget.RecipeWidget;
import timber.log.Timber;

public class BakingApplication extends Application {

    private Repo repo;

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

    public static Repo repo(Context context) {
        BakingApplication app = (BakingApplication) context.getApplicationContext();
        return app.repo;
    }

    @VisibleForTesting
    public void setRepo(Repo repo) {
        this.repo = repo;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        this.repo = new Repo();
    }
}
