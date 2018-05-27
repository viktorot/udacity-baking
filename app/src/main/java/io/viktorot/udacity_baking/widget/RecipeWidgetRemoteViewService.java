package io.viktorot.udacity_baking.widget;

import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import io.viktorot.udacity_baking.R;

public class RecipeWidgetRemoteViewService extends RemoteViewsService {

    private final RemoteViewsFactory FACTORY = new RemoteViewsFactory() {

        private String title;

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            title = "123";
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_item_recipe_ingredient);
            views.setTextViewText(R.id.name, "name");
            views.setTextViewText(R.id.quantity, "123");

            return null;
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

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return FACTORY;
    }
}
