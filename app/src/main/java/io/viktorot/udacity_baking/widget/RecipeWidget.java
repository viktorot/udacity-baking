package io.viktorot.udacity_baking.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import io.viktorot.udacity_baking.Prefs;
import io.viktorot.udacity_baking.R;
import io.viktorot.udacity_baking.data.Recipe;

public class RecipeWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int id : appWidgetIds) {
            update(context, appWidgetManager, id);
        }
    }

    private void update(Context context, AppWidgetManager manager, int widgetId) {
        Recipe recipe = Prefs.getFavRecipe(context);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe);

        if (recipe == null) {
            views.setTextViewText(R.id.title, context.getString(R.string.no_fav_recipe));
        } else {
            views.setTextViewText(R.id.title, recipe.name);
            views.setRemoteAdapter(R.id.list, new Intent(context, RecipeWidgetRemoteViewService.class));
        }

        manager.updateAppWidget(widgetId, views);
    }
}
