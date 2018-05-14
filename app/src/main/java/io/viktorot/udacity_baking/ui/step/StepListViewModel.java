package io.viktorot.udacity_baking.ui.step;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.Objects;

import io.viktorot.udacity_baking.data.Recipe;
import io.viktorot.udacity_baking.data.Step;

public class StepListViewModel extends AndroidViewModel {

    public MutableLiveData<Recipe> recipe = new MutableLiveData<>();

    public StepListViewModel(@NonNull Application application) {
        super(application);
    }

    public void setData(@NonNull Recipe recipe) {
        this.recipe.setValue(recipe);
    }

    @NonNull
    public Recipe getData() {
        return Objects.requireNonNull(this.recipe.getValue());
    }
}
