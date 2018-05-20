package io.viktorot.udacity_baking;

import android.support.v4.app.FragmentManager;
import android.view.View;

import io.reactivex.annotations.NonNull;
import io.viktorot.udacity_baking.data.Recipe;
import io.viktorot.udacity_baking.ui.details.StepDetailsFragment;
import io.viktorot.udacity_baking.ui.recipe.RecipeListFragment;
import io.viktorot.udacity_baking.ui.step.StepContainerFragment;

public abstract class Navigator {

    private FragmentManager manager;

    @NonNull
    private View mainHolder;

    protected abstract void close();

    public void init(@NonNull FragmentManager manager, @NonNull View main) {
        this.manager = manager;
        mainHolder = main;
    }

    public void clear() {
        mainHolder = null;
        manager = null;
    }

    public void navigateToRecipes() {
        manager.beginTransaction()
                .replace(mainHolder.getId(), RecipeListFragment.newInstance(), RecipeListFragment.TAG)
                .commit();
    }

    public void navigateToStepList(@NonNull Recipe recipe) {
        manager.beginTransaction()
                .replace(mainHolder.getId(), StepContainerFragment.newInstance(recipe), StepContainerFragment.TAG)
                .addToBackStack(StepContainerFragment.TAG)
                .commit();
    }

    public void navigateToStepDetails(@NonNull Recipe recipe, int index) {
        manager.beginTransaction()
                .replace(mainHolder.getId(), StepDetailsFragment.newInstance(recipe, index), StepDetailsFragment.TAG)
                .addToBackStack(StepDetailsFragment.TAG)
                .commit();
    }

    public void back() {
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
