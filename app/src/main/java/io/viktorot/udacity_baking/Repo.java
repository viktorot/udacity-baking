package io.viktorot.udacity_baking;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.annotations.Nullable;
import io.reactivex.schedulers.Schedulers;
import io.viktorot.udacity_baking.data.Recipe;
import io.viktorot.udacity_baking.service.RecipeService;
import io.viktorot.udacity_baking.service.RecipeServiceBuilder;

public class Repo {

    private RecipeService service;

    public Repo() {
        service = RecipeServiceBuilder.build();
    }

    public Single<List<Recipe>> getRecipes() {
        return service.getRecipes()
                .subscribeOn(Schedulers.io());
    }
}
