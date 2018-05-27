package io.viktorot.udacity_baking.widget;

import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import io.viktorot.udacity_baking.Prefs;
import io.viktorot.udacity_baking.R;
import io.viktorot.udacity_baking.data.Ingredient;
import io.viktorot.udacity_baking.data.Recipe;

public class RecipeWidgetRemoteViewService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            private Recipe recipe;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                recipe = Prefs.getFavRecipe(getApplicationContext());
            }

            @Override
            public void onDestroy() {

            }

            @Override
            public int getCount() {
                if (recipe == null) {
                    return 0;
                }
                return recipe.ingredients.size();
            }

            @Override
            public RemoteViews getViewAt(int i) {
                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_item_recipe_ingredient);
                Ingredient ing = recipe.ingredients.get(i);
                views.setTextViewText(R.id.name, ing.name);
                views.setTextViewText(R.id.quantity, String.format("%f %s", ing.quantity, ing.measure));

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }
        };
    }
}
