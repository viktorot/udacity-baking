package io.viktorot.udacity_baking;

import android.support.v4.app.FragmentManager;
import android.view.View;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.viktorot.udacity_baking.data.Recipe;
import io.viktorot.udacity_baking.data.Step;
import io.viktorot.udacity_baking.ui.details.StepDetailsFragment;
import io.viktorot.udacity_baking.ui.recipe.RecipeListFragment;
import io.viktorot.udacity_baking.ui.step.StepListFragment;

public abstract class Navigator {

    private FragmentManager manager;

    @NonNull
    private View mainHolder;

    @Nullable
    private View detailsHolder;

    public abstract void close();

    public void init(@NonNull FragmentManager manager, @NonNull View main, @Nullable View details) {
        this.manager = manager;
        mainHolder = main;
        detailsHolder = details;
    }

    public void clear() {
        mainHolder = null;
        detailsHolder = null;
    }

    public void navigateToRecipeList() {
        manager.beginTransaction()
                .replace(mainHolder.getId(), RecipeListFragment.newInstance(), RecipeListFragment.TAG)
                .commit();
    }

    public void navigateToStepList(@NonNull Recipe recipe) {
        manager.beginTransaction()
                .replace(mainHolder.getId(), StepListFragment.newInstance(recipe), StepListFragment.TAG)
                .addToBackStack(StepListFragment.TAG)
                .commit();
    }

    public void navigateToStepDetails(@NonNull Step step) {
        manager.beginTransaction()
                .replace(mainHolder.getId(), StepDetailsFragment.newInstance(step), StepDetailsFragment.TAG)
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
