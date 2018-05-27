package io.viktorot.udacity_baking.ui.recipe;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.viktorot.udacity_baking.BakingApplication;
import io.viktorot.udacity_baking.Repo;
import io.viktorot.udacity_baking.data.Recipe;
import timber.log.Timber;

public class RecipeListViewModel extends AndroidViewModel {

    private Disposable dataDisposable;

    public MutableLiveData<List<Recipe>> recipes = new MutableLiveData<>();

    public RecipeListViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    private void init() {
        dispose();
        dataDisposable = BakingApplication.repo(getApplication()).getRecipes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError);
    }

    private void onSuccess(List<Recipe> items) {
        recipes.setValue(items);
    }

    private void onError(Throwable error) {
        Timber.e(error, "error getting recipes");
    }

    private void dispose() {
        if (dataDisposable != null) {
            dataDisposable.dispose();
        }
    }

    @Override
    protected void onCleared() {
        dispose();
        super.onCleared();
    }
}
