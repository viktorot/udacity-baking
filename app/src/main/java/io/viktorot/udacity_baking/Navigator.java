package io.viktorot.udacity_baking;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.view.View;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.viktorot.udacity_baking.data.Recipe;
import io.viktorot.udacity_baking.data.Step;
import io.viktorot.udacity_baking.ui.details.StepDetailsActivity;
import io.viktorot.udacity_baking.ui.details.StepDetailsFragment;
import io.viktorot.udacity_baking.ui.recipe.RecipeListFragment;
import io.viktorot.udacity_baking.ui.step.StepListFragment;

public abstract class Navigator {

    private Context context;

    private FragmentManager manager;

    @NonNull
    private View mainHolder;

    @Nullable
    private View detailsHolder;

    @Nullable
    private View fullscreenHolder;


    public abstract void close();

    public void init(@NonNull Context context, @NonNull FragmentManager manager, @NonNull View main, @Nullable View fullscreen, @Nullable View details) {
        this.context = context;
        this.manager = manager;
        mainHolder = main;
        detailsHolder = details;
        fullscreenHolder = fullscreen;
    }

    public void clear() {
        mainHolder = null;
        detailsHolder = null;
        fullscreenHolder = null;
        context = null;
        manager = null;
    }

    public void navigateToRecipes() {
        int holderId;
        if (fullscreenHolder != null) {
            holderId = fullscreenHolder.getId();
        } else {
            holderId = mainHolder.getId();
        }

        manager.beginTransaction()
                .replace(holderId, RecipeListFragment.newInstance(), RecipeListFragment.TAG)
                .commit();
    }

    public void navigateToStepList(@NonNull Recipe recipe) {
        manager.beginTransaction()
                .replace(mainHolder.getId(), StepListFragment.newInstance(recipe), StepListFragment.TAG)
                .addToBackStack(StepListFragment.TAG)
                .commit();
    }

    public void navigateToStepDetails(@NonNull Recipe recipe, int index) {
//        Intent intent = StepDetailsActivity.getIntent(context, recipe, index);
//        context.startActivity(intent);

        int holderId;
        if (detailsHolder != null) {
            holderId = detailsHolder.getId();
        } else {
            holderId = mainHolder.getId();
        }

        manager.beginTransaction()
                .replace(holderId, StepDetailsFragment.newInstance(recipe, index), StepDetailsFragment.TAG)
                .addToBackStack(StepDetailsFragment.TAG)
                .commit();
    }

    public void back(){
        if (manager.getBackStackEntryCount() == 0) {
            close();
        } else {
            manager.popBackStack();
        }
    }

    public interface Provider {
        Navigator getNavigator();
    }
}
