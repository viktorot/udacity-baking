package io.viktorot.udacity_baking;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import io.viktorot.udacity_baking.data.Recipe;

public class Prefs {

    private static final String KEY_RECIPE = "recipe";

    public static void saveFavRecipe(@NonNull Context context, @NonNull Recipe recipe) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        String json = new Gson().toJson(recipe);

        editor.putString(KEY_RECIPE, json);
        editor.apply();
    }

    @Nullable
    public static Recipe getFavRecipe(@NonNull Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(KEY_RECIPE, null);

        return new Gson().fromJson(json, Recipe.class);
    }
}
