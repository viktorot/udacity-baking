package io.viktorot.udacity_baking.ui.details;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.Objects;

import io.viktorot.udacity_baking.data.Recipe;
import io.viktorot.udacity_baking.data.Step;

public class StepDetailsViewModel extends AndroidViewModel {

    public MutableLiveData<Recipe> recipe = new MutableLiveData<>();
    public MutableLiveData<Step> step = new MutableLiveData<>();

    private int stepCount = 0;
    private int index = -1;

    public StepDetailsViewModel(@NonNull Application application) {
        super(application);
    }

    public void setData(@NonNull Recipe recipe, int index) {
        this.index = index;
        this.recipe.setValue(recipe);
        this.stepCount = recipe.steps.size();

        Step step = getStepAt(index);
        this.step.setValue(step);
    }

    private Step getStepAt(int index) {
        Recipe recipe = Objects.requireNonNull(this.recipe.getValue());
        return recipe.steps.get(index);
    }

    public void goToNextStep() {
        if (index + 1 >= stepCount) {
            return;
        }

        Step step = getStepAt(++index);
        this.step.setValue(step);
    }

    public void goToPrevStep() {
        if (index - 1 < 0) {
            return;
        }

        Step step = getStepAt(--index);
        this.step.setValue(step);
    }
}
