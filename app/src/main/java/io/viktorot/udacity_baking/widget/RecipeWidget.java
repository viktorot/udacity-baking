package io.viktorot.udacity_baking.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import io.viktorot.udacity_baking.R;

public class RecipeWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int id : appWidgetIds) {
            update(context, appWidgetManager, id);
        }
    }

    private void update(Context context, AppWidgetManager manager, int widgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe);
        views.setTextViewText(R.id.title, "title");
        views.setRemoteAdapter(R.id.list, new Intent(context, RecipeWidgetRemoteViewService.class));

        manager.updateAppWidget(widgetId, views);
    }
}
